import pandas as pd
import os
from datetime import datetime
import sys
import time 
import datetime as dt
sys.path.insert(0,r'/Users/Shared/c/CodeRepository/Formatting-Error-Correction/')
sys.path.insert(0,r'/Users/Shared/c/CodeRepository/Formatting-Error-Correction/Scripts')
from utils import tokenizer
from utils import helper_func as helpFuncs
from utils import Score_Detect_Functions as sdf
from Scripts import S4_Token_Scoring, S5_Error_Detection
import Scripts.S7_Parameters as Params

errTokenPosTruth = pd.read_excel('/Users/Shared/c/CodeRepository/Data/out.xlsx', header = 0)
errTokenPosTruth['Position'] -= 1
errTokenPosTruth['Filename'] = errTokenPosTruth['Filename'].astype(str)
errTokenPosTruth['Filename'] = errTokenPosTruth['Filename'] + '.txt'

tokenDetectStats = {}
directory = '/Users/Shared/c/CodeRepository/Data/CodRep_Eval_Part2'
os.chdir(directory)

scriptStartTime = datetime.now()
scriptStartTime_v2 = time.time()
print(f'Starting Time:{scriptStartTime.strftime("%H:%M:%S")}')
print(f'-------------------------------------------------------------------------------------------------------------')

for num, fileName in enumerate(sorted(os.listdir(directory))):

    print(f'Iteration {num} -- Processing file with name: {fileName}...\n')
    file = open(fileName, "r", encoding = "utf-8", errors = 'ignore')
    code = file.read()

    # Step 1: Importing N-grams Model
    lm = sdf.ngramModelImport(Params.path+"10_Gram_Model", "10_gram_model_v2.p")

    # Step 2: Source code file tokenization
    [tokens, lengths] = tokenizer.tokenize(code)
    if(len(tokens) <= 20):
        print(f'Number of tokens is less than 20. Total tokens equals to {len(tokens)}. Proceeding with the next file')
        continue
    # Calculate total file length 
    totalFileLength = sum(lengths)

    # Step 3: Calculate score for each token that is part of the source code file and also calculate the total score 
    # of the source code file
    [tokenScores, snippetsScores, fileScore, snippets, snippetsLengths] = S4_Token_Scoring.ngramScore(tokens,lengths)

    # Step 4: Error Detection through LSTM network
    [errProb, startPositionsPerToken, suggestedFixes] = S5_Error_Detection.lstmErrorDetection(code)

    errProbPos = [(x,prob) for prob,x in sorted(zip(errProb, startPositionsPerToken), reverse = True)]
    scorePos = [(x,tokenScores) for tokenScores,x in sorted(zip(tokenScores, startPositionsPerToken), reverse = True)]

    # Step 5: Modify probabilities based on token scoring, produced by N-grams model
    tokenScoresMapped = helpFuncs.tokenScoreInterp(scorePos,errProbPos)
    errProb = helpFuncs.errProbModif(errProbPos, tokenScoresMapped)
    errProbSorted = [(pos,prob) for prob,pos in sorted(zip(list(errProb.values()),list(errProb.keys())), reverse = True)]

    # Step 6 : Sorting suggested fixes and possible formating error positions according to errProb in descending order.
    possErrPositions = [pos for prob,pos in sorted(zip(list(errProb.values()),list(errProb.keys())), reverse = True)]
    errProbSorted = [prob[1] for prob in errProbSorted]
    print(f'Detected positions as possible formatting errors: {possErrPositions[:Params.numOfCheckedTok]}\n')
    print(f'Error Probabilities: {errProbSorted[:Params.numOfCheckedTok]}\n')
    print(f'-------------------------------------------------------------------------------------------------------------')
    tokenDetectStats.update({fileName:[totalFileLength,possErrPositions[:Params.numOfCheckedTok],errProbSorted[:Params.numOfCheckedTok],int(errTokenPosTruth[errTokenPosTruth['Filename'] == fileName]['Position'])]})

end_time = datetime.now()
print("Total execution time(hours:mins:seconds): ---- %s ---\n" % str(dt.timedelta(seconds = round((time.time() - scriptStartTime_v2), 4))))
print(f'\nEnd Time:{end_time.strftime("%H:%M:%S")}\n')

df = pd.DataFrame.from_dict(tokenDetectStats, orient='index', columns = ['File Length','Detected Error Positions','Error Probability','True Error Position'])
numberOfSpottedPosition = []
for idx,pos in enumerate(df['True Error Position']):
    if(pos in df['Detected Error Positions'][idx]):
        numberOfSpottedPosition.append(df['Detected Error Positions'][idx].index(pos) + 1)
    else:
        numberOfSpottedPosition.append('>5')

df['Number of Spotted Pos'] = numberOfSpottedPosition
df.to_excel(Params.path + 'utils/Visualization/PositionOfDetectedErrors_20230403.xlsx', index = True, header = True)