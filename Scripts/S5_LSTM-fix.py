import sys
import os
from datetime import datetime
sys.path.insert(0,'C:\CodeRepository\Formatting-Error-Correction')
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3' 
from tensorflow import keras
from utils.tokenizer import tokenize
from utils.helper_func import truncate
import numpy as np
import S4_Score_Detect
import S6_Error_Fixer

start_time = datetime.now()
print(f'\n\nStarting Time:{start_time.strftime("%H:%M:%S")}\n\n')

model_fw = keras.models.load_model("C:/CodeRepository/Formatting-Error-Correction/LSTM Model/LSTM_emb_dr_double.h5")
fix_dir = "_FIXES/"

tokens_available = ["<start>","<end>","<eos>","<tab>","<spacetab>","<dot>","<comma>","<semicolon>","<colon>","<exclamation>",
                    "<at>","<hash>","<dollar>","<perc>","<caret>","<and>","<power>","<open_par>","<close_par>","<minus>",
                    "<plus>","<equal>","<open_bracket>","<close_bracket>","<open_curly>","<close_curly>","<less_than>",
                    "<greater_than>","<quest>","<back_slash>","<keyword>","<literal>","<lit>","<word>","<space>",
                    "<com>","<slash>","<string>","<char>","<number>","<unk>"]

replacements = ["","","\n","\t","    ",".",",",";",":","!","@","#","$", "%","^","&","*","(",")","-","+","=","[","]","{",
               "}","<",">","?","\\","for ","true ","int "," abc "," ","/* comment */","/"," abc123 ","a",
               "123","<unk>"]

d = dict((c, i) for i, c in enumerate(tokens_available))
d_inv = dict((i, c) for i, c in enumerate(tokens_available))

file = open(sys.argv[1], "r", encoding = "utf-8")
code = file.read()

[tokens, lengths] = tokenize(code)

# Append <start> and <end> tokens   
tokens_enc = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0] \
            + [d[x] for x in tokens] \
            + [1, 1, 1, 1, 1, 1, 1, 1, 1, 1]

for i in range(10):
    lengths.insert(0, 0)            # zero length for 10 <start> tokens
    lengths.insert(len(lengths), 0) # zero length for 10 <end> tokens
    

chars_fw = [] # stores the starting offset of each token 
probs_fw = [] # stores the error propability of prediction for each token

tengrams = []
sug_fixes = [] # stores suggested tokens-fixes based on LSTM prediction

#Tengrams formation
for i in range(len(tokens_enc)-9):
    tengrams.append(tokens_enc[i : i+10])

######################################################################
# Calculate forward error probabilities using the forward LSTM model #
######################################################################

y = []# The next token for a tengram is the last token in the next tengram
for i in range(len(tengrams)-1):
    y.append(tengrams[i+1][9])

y = np.asarray(y) #Disclaimer: y stores the next token of the current 10-gram = first token of the next 10-gram

pos = 1

for i, testgram in enumerate(tengrams[:-11]):

    test = np.asarray(testgram)
    #print(f'Iteration:{i}\nCurrent_Testgram:{test}')

    preds = model_fw.predict(test.reshape(1, 10), verbose=0)
    pred_token = (np.where( preds[0] == np.amax(preds[0]))[0][0]) 

    # Add 5 most probable fixes to sug_fixes
    pos_fixes = [repl for _,repl in sorted(zip(preds[0], replacements),reverse=True)] 
    pos_fixes = pos_fixes[:5]   
    sug_fixes.append(pos_fixes)

    # Assign probability to token's first character
    pos += lengths[i+9]
    chars_fw.append(pos)
    #print("Position: ", pos)
    #print("Predicted next token: ", d_inv.get(pred_token))
    #print("Token found in code: ", d_inv.get(y[i])) 
    probs_fw.append(1.0 - preds[0][y[i]])
    #print("Error probability: ", 1.0 - preds[0][y[i]], "\n")

#Printing of Tokens & Tokens length
#print(f'Tokens:{tokens_enc}\n')
#print(f'Tokens Length:{lengths}\n')

#Printing of Chars_fw and Probs_fw
#print(f'Chars_fw:{chars_fw}\n')
#print(f'Probs_fw(Error Probs):{[round(i,2) for i in probs_fw]}\n')

###################################################################
# Modify probabilities based on token scoring #
###################################################################
[scores_token,scores,file_score,snips,snip_lengths] = S4_Score_Detect.ngramScore(code)
end_time = datetime.now()
print(f'\n\nEnd Time:{end_time.strftime("%H:%M:%S")}')

# score_pre = file_score
# #print(f'Scores of Tokens:{scores_token}\n')
# print(f'\nScore of code before any fixing: {score_pre}\n')

# min_score_token = min(scores_token)
# max_score_token = max(scores_token)
# scores_token_mapped = []
# for i in scores_token:
#     scores_token_mapped.append(truncate(np.interp(i,[min_score_token,max_score_token],[1,0]),4))

# for i in range(len(probs_fw)):
#     probs_fw[i] *= 1-scores_token_mapped[i]
#     probs_fw[i] = truncate(probs_fw[i],4)

# #print(f'Score of tokens mapped to [0,1]:{scores_token_mapped}\n')
# #print(f'New probs_fw:{probs_fw}')

# fixes_sorted = [x for _,x in sorted(zip(probs_fw, sug_fixes), reverse=True)]
# chars_sorted = [x for _,x in sorted(zip(probs_fw, chars_fw), reverse=True)]

# ###########################################
# # Get 'fixed' codes and save them to file #
# ###########################################
# acc_codes = S6_Error_Fixer.get_fixes(code, chars_sorted, fixes_sorted, score_pre)
# for i,c in enumerate(acc_codes):
#     _,_,new_score,_,_ = S4_Score_Detect.get_score(c) 
#     print(f'New_score of fix {i}: {new_score}\n')

# os.makedirs(fix_dir, exist_ok = True)
# for i, code in enumerate(acc_codes):
#     with open(fix_dir + str(i+1) + ".txt", 'w') as f:
#         f.write(code)
# print("Possible fixed codes saved in folder " + fix_dir)

