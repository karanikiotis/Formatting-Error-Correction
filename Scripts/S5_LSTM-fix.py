import sys
import os
import time 
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
import numpy as np
from datetime import datetime
import datetime as dt
from tensorflow import keras
sys.path.insert(0,r'/Users/Shared/c/CodeRepository/Formatting-Error-Correction/')
from utils.tokenizer import tokenize
from utils.helper_func import truncate
from utils import Score_Detect_Functions as sdf
import S4_Score_Detect
import S6_Error_Fixer
import S7_Parameters as Params

scriptStartTime_v2 = time.time()
scriptStartTime = datetime.now()
print(f'\n\nStarting Time:{scriptStartTime.strftime("%H:%M:%S")}\n\n')

# Importing LSTM Model
model_fw = keras.models.load_model(Params.path+"LSTM Model/LSTM_emb_dr_double.h5")
# Importing N-grams Model
lm = sdf.ngramModelImport(Params.path+"10_Gram_Model", "10_gram_model_v2.p")


d = dict((c, i) for i, c in enumerate(Params.tokensAvailable))
d_inv = dict((i, c) for i, c in enumerate(Params.tokensAvailable))

file = open(sys.argv[1], "r", encoding = "utf-8")
fileName = sys.argv[1].split('/')[-1]
code = file.read()

[tokens, lengths] = tokenize(code)

# Append <start> and <end> tokens   
tokens_enc = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0] \
            + [d[x] for x in tokens] \
            + [1, 1, 1, 1, 1, 1, 1, 1, 1, 1]

for i in range(10):
    # zero length for 10 <start> tokens
    lengths.insert(0, 0)
    # zero length for 10 <end> tokens
    lengths.insert(len(lengths), 0)

# Holds the starting position of each token
startPositionsPerToken = []  
# Holds the error propability acording to LSTM, for the next token of the iterating 10-gram.
# Next tokens with higher error probabilities are more likely not to be formatted correctly.
errProb = [] 
suggestedFixes = [] 
tengrams = []

for i in range(len(tokens_enc)-9):
    tengrams.append(tokens_enc[i : i+10])

# Calculation of index of the next token.
# On each position of the Numpy array, we have the index of the token
# that is going to appear after each 10-gram.
idxOfNextToken = []
for i in range(len(tengrams)-1):
    idxOfNextToken.append(tengrams[i+1][9])
idxOfNextToken = np.asarray(idxOfNextToken) 

curPosition = 0
# Iterating through each one of the 10-grams.
for i, testgram in enumerate(tengrams[:-11]):
    
    # Converting each 10-gram from a list into an NumPy array in order
    # to be used as input to the LSTM model.
    iterGram = np.asarray(testgram)
    # LSTM is using the iterating 10-gram as input and is producing one estimation per token that exists on
    # tokensAvailable list. That estimation represents the probability for each token, to be the next token after 
    # this 10-gram. PredictionsLSTM is a 41x1 array.
    predictionsLSTM = model_fw.predict(iterGram.reshape(1, 10), verbose=0)
    # Keep the index of the token that is the most possible to appear after the iterating 10-gram, 
    # by finding the highest probability among all.
    predictedToken = (np.where(predictionsLSTM[0] == np.amax(predictionsLSTM[0]))[0][0]) 
    # Sort probabilities on descending order. possibleFixes is a list that contains all the possible tokens
    # on descending order. Tokens are represented with their characters.
    possibleFixes = [repl for _,repl in sorted(zip(predictionsLSTM[0], Params.replacements),reverse = True)] 
    # Keep the tokens with the highest probabilities. These tokens are going to be the suggested fixes. 
    # The number of tokens that we keep as suggested fixes for the iterating Ngram, is defined through a parameter called numOfSuggFixes.
    # So, for each ngram, we are keeping a specific number of tokens as suggested fixes. 
    topFixesCurGram = possibleFixes[:Params.numOfSuggFixes]
    suggestedFixes.append(topFixesCurGram)
    # Calculate the starting position of the current token
    curPosition += lengths[i+9]
    # Append the starting position of the current token to a list that holds the starting positions of each token
    startPositionsPerToken.append(curPosition)
    # Calculation of error probability according to LSTM, for the next token of the iterating 10-gram.
    # So, next tokens with higher error probabilities are more likely not to be formatted correctly.
    errProb.append(1.0 - predictionsLSTM[0][idxOfNextToken[i]])
    #print("Position: ", pos)
    #print("Predicted next token: ", d_inv.get(predictedToken))
    #print("Token found in code: ", d_inv.get(y[i])) 
    #print("Error probability: ", 1.0 - predictionsLSTM[0][y[i]], "\n")


[scores_token,scores, file_score, snips,snip_lengths] = S4_Score_Detect.ngramScore(code,tokens,lengths)
end_time = datetime.now()
score_pre = file_score

print(f'Score before any fix: {score_pre}\n')

#print(f'\nScore of code before any fixing: {score_pre}\n')
# Modify probabilities based on token scoring, produced by N-grams Model
min_score_token = min(scores_token)
max_score_token = max(scores_token)
scores_token_mapped = []
for i in scores_token:
    scores_token_mapped.append(truncate(np.interp(i,[min_score_token,max_score_token],[1,0]),4))

for i in range(len(errProb)):
    errProb[i] *= 1-scores_token_mapped[i]
    errProb[i] = truncate(errProb[i],4)

#print(f'Score of tokens mapped to [0,1]:{scores_token_mapped}\n')
#print(f'New probs_fw:{probs_fw}')  
fixes_sorted = [x for _,x in sorted(zip(errProb, suggestedFixes), reverse=True)]
chars_sorted = [x for _,x in sorted(zip(errProb, startPositionsPerToken), reverse=True)]

###########################################
# Get 'fixed' codes and save them to file #
###########################################
print('Step 7: Fixing errorneous token ...')
start_time = time.time()

acc_codes = S6_Error_Fixer.getFixes(code, lm, chars_sorted, fixes_sorted, score_pre)
print("Step 7: ----Total time of fixing (seconds) %s seconds ---\n" % round( (time.time() - start_time), 4))

for i,c in enumerate(acc_codes):
    new_score = sdf.calcScore(c,lm) 
    print(f'New_score of fix {i}: {new_score}')

path = os.path.join(Params.fix_dir, fileName)
os.makedirs(path, exist_ok = True)
for i, code in enumerate(acc_codes):
    with open(os.path.join(path, str(i+1) + ".txt"), 'w') as f:
        f.write(code)
print(f'\n\nEnd Time:{end_time.strftime("%H:%M:%S")}\n')
print("Total execution time(hours:mins:seconds): ---- %s ---\n" % str(dt.timedelta(seconds = round((time.time() - scriptStartTime_v2), 4))))

print("Possible fixed codes saved in folder " + path)