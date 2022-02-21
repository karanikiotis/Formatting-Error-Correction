from tensorflow import keras
from tokenizer import tokenize
import numpy as np
import score_snippet
import fixer
import math
import sys
import os

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

d     = dict((c, i) for i, c in enumerate(tokens_available))
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
    print(f'Iteration:{i}\nCurrent_Testgram:{test}')

    preds = model_fw.predict(test.reshape(1, 10), verbose=0)
    pred_token = (np.where( preds[0] == np.amax(preds[0]))[0][0]) 

    # Add 5 most probable fixes to sug_fixes
    pos_fixes = [repl for _,repl in sorted(zip(preds[0], replacements),reverse=True)] 
    pos_fixes = pos_fixes[:5]   
    sug_fixes.append(pos_fixes)

    # Assign probability to token's first character
    pos += lengths[i+9]
    chars_fw.append(pos)
    print("Position: ", pos)
    print("Predicted next token: ", d_inv.get(pred_token))
    print("Token found in code: ", d_inv.get(y[i])) 
    probs_fw.append(1.0 - preds[0][y[i]])
    print("Error probability: ", 1.0 - preds[0][y[i]], "\n")

#Printing of Tokens & Tokens length
print(f'Tokens:{tokens_enc}\n')
print(f'Tokens Length:{lengths}\n')

#Printing of Chars_fw and Probs_fw
print(f'Chars_fw:{chars_fw}\n')
print(f'Probs_fw(Error Probs):{[round(i,2) for i in probs_fw]}\n')

###################################################################
# Modify probabilities based on snippet scoring #
###################################################################
[scores, snip_lengths, edge_pos, edge_scores,edge_tok,snips] = score_snippet.get_score(code)

#Printing of Snippet, Score of Snippet and the length of each Snippet
for i,s in enumerate(scores):
    print(f'Snippet {i}:{snips[i]},  Score:{round(s,3)},  Length:{snip_lengths[i]}\n')

#Printing of edge positions, edge tokens, edge scores
print(f'Edge positions:{edge_pos}\nEdge Tokens:{edge_tok}\nEdge_Scores:{edge_scores}\n')

# Average score of the whole code before fixing
score_pre = sum(scores)/len(scores) 
print(f'\nAverage score of code before any fixing: {score_pre}\n')

# Modify probs based on scoring
for i in range(0,len(snip_lengths) - 1):
    for pos in range(snip_lengths[i], snip_lengths[i+1]):
        if pos in chars_fw:
            if pos in edge_pos:
                probs_fw[chars_fw.index(pos)] *= 1 - min(scores[i], edge_scores[math.floor(edge_pos.index(pos)/2)])
            else:
                probs_fw[chars_fw.index(pos)] *= 1 - scores[i]

fixes_sorted = [x for _,x in sorted(zip(probs_fw, sug_fixes), reverse=True)]
chars_sorted = [x for _,x in sorted(zip(probs_fw, chars_fw), reverse=True)]



















###########################################
# Get 'fixed' codes and save them to file #
###########################################
acc_codes = fixer.get_fixes(code, chars_sorted, fixes_sorted, score_pre)

new_scores = score_snippet.get_score(acc_codes)[0] #Score fixed code
score_new = sum(new_scores)/len(new_scores) 

print(f'Average score of code after fixing: {score_new}\n')

os.makedirs(fix_dir, exist_ok = True)

for i, code in enumerate(acc_codes):
    with open(fix_dir + str(i+1) + ".txt", 'w') as f:
        f.write(code)
print("Possible fixed codes saved in folder " + fix_dir)


####################################
# Print sorted character positions #
####################################
#print(' '.join(str(pos) for pos in chars_sorted))
#print(sorted(zip(probs_fw, chars_fw), reverse=True))