import sys
import time 
import datetime as dt
import argparse
from datetime import datetime
sys.path.insert(0,r'/Users/Shared/c/CodeRepository/Formatting-Error-Correction/')
from utils import tokenizer
from utils import helper_func as helpFuncs
from utils import Score_Detect_Functions as sdf
import S4_Token_Scoring
import S5_Error_Detection
import S6_Error_Fixing
import S7_Parameters as Params

parser = argparse.ArgumentParser(description = 'Formating Error Detection and Fixing end-to-end pipeline.')
parser.add_argument('--file', help = 'Source code file to be processed.',required = True)
parser.add_argument('--mode', help = 'Mode: single or batch.')
args = parser.parse_args()

if(args.mode == 'single'):
    file = open(args.file, "r", encoding = "utf-8")
    fileName = args.file.split('/')[-1]
    code = file.read()
elif (args.mode == 'batch'):
    pass
else:
    pass

scriptStartTime = datetime.now()
scriptStartTime_v2 = time.time()

print(f'\n\nStarting Time:{scriptStartTime.strftime("%H:%M:%S")}\n\n')

# Step 1: Importing N-grams Model
lm = sdf.ngramModelImport(Params.path+"10_Gram_Model", "10_gram_model_v4.p")

# Step 2: Source code file tokenization
[tokens, lengths] = tokenizer.tokenize(code)

# Step 3: Calculate score for each token that is part of the source code file and also calculate the total score 
# of the source code file
[tokenScores, snippetsScores, fileScore, snippets, snippetsLengths] = S4_Token_Scoring.ngramScore(tokens,lengths)

# Step 4: Error Detection through LSTM network
[errProb, startPositionsPerToken, suggestedFixes, errProbPos] = S5_Error_Detection.lstmErrorDetection(code)
print(f'Source code score before any fix: {fileScore}')
scorePos = [(x,tokenScores) for tokenScores,x in sorted(zip(tokenScores, startPositionsPerToken), reverse = True)]

# Step 5: Modify probabilities based on token scoring, produced by N-grams model
tokenScoresMapped = helpFuncs.tokenScoreInterp(scorePos,errProbPos)
errProb = helpFuncs.errProbModif(errProbPos, tokenScoresMapped)
errProbSorted = [(pos,prob) for prob,pos in sorted(zip(list(errProb.values()),list(errProb.keys())), reverse = True)]

# Step 6 : Sorting suggested fixes and possible formating error positions according to errProb in descending order.
if(Params.probThreshActive):
    fixesSorted = [suggestedFixes[pos] for pos, prob in errProbSorted if prob > Params.probThresh]
    possErrPositions = [pos for prob,pos in sorted(zip(list(errProb.values()),list(errProb.keys())), reverse = True) if prob > Params.probThresh]
    print(f'Detected positions as possible formatting errors: {possErrPositions}')
    print(f'Error probabilities of possible formatting errors: {[prob[1] for prob in errProbSorted if prob[1] > Params.probThresh]}\n')
else:
    fixesSorted = [suggestedFixes[pos] for pos, prob in errProbSorted][:Params.numOfCheckedTok]
    possErrPositions = [pos for prob,pos in sorted(zip(list(errProb.values()),list(errProb.keys())), reverse = True)][:Params.numOfCheckedTok]
    print(f'Detected positions as possible formatting errors: {possErrPositions}')
    print(f'Error probabilities of possible formatting errors: {[prob[1] for prob in errProbSorted][:Params.numOfCheckedTok]}\n')

# Step 7: Fix detected formattion errors
print('Step 7: Fixing errorneous token ...\n')
start_time = time.time()
fixedCode, fixedScores = S6_Error_Fixing.getFixes(code, lm, possErrPositions, fixesSorted, fileScore)
print("\nStep 7: Total time of fixing (seconds) ---- %s seconds---" % round( (time.time() - start_time), 4))
print("Step 7: Total Time of fixing(hours:mins:seconds): ---- %s ---\n" % str(dt.timedelta(seconds = round( (time.time() - start_time), 4))))

# Step 8: Write fixed source code files
if (fixedCode != []):
    # Check if we want to return to the user just the file with the best score
    if(Params.returnBestScoreFile):
        # Find the best score
        bestFixScore = min(fixedScores)
        # Find the index with the best score
        idxBestFixScore = fixedScores.index(bestFixScore)
        helpFuncs.writeFixedCode(fileName, fixedCode[idxBestFixScore])
    else:
        helpFuncs.writeFixedCode(fileName, fixedCode, batch = True)
else:
    print("No error tokens have been detected. The source code file is clear.\n")

end_time = datetime.now()
print("Total execution time(hours:mins:seconds): ---- %s ---\n" % str(dt.timedelta(seconds = round((time.time() - scriptStartTime_v2), 4))))
print(f'\nEnd Time:{end_time.strftime("%H:%M:%S")}\n')