from stat import SF_APPEND
import numpy as np
import time 
import datetime
from nltk.util import ngrams
from utils import Score_Detect_Functions as sdf
import S7_Parameters as Params
import debugpy

# debugpy.log_to('C:\CodeRepository\Formatting-Error-Correction\Logs')
# debugpy.listen(5678)
# print('Debugging Session\n')
# debugpy.wait_for_client()



def ngramScore(code,tokens,lengths):
    """
    Description:
        # For each one of 20-token snippets, we are going to calculate its score
        # Next, in order to calculate the score for each token, we are going to
        # see in which snippets, each token belongs to.
        # Each token's score will be the mean of the scores of the code snippets where it belongs to

    Inputs:
        code () :

    Outputs:
        tokenScores () :
        snippetsScores () :
        fileScore () :
        snippets () :
        snippetsLengths () :
    """
    funcStartTime = time.time()
    # We have trained a 10-gram model that will be used for code snippet scoring
    # Load 10-gram model
    lm = sdf.ngramModelImport(r'/mnt/c/CodeRepository/Formatting-Error-Correction/10_Gram_Model','10_gram_model_v2.p')
    #Encode tokens using tokensMapping dictionary in order to be represented as integer numbers
    tokensEncoded = sdf.tokensEncode(tokens, Params.tokensMapping)
    # Break source code file into snippets (ngrams) that will consist of TOK_PER_SNIP tokens
    snippets = sdf.gramsFormationStep(tokensEncoded, Params.TOK_PER_SNIP, 3)

    # Calculate the score of the whole source code file regarding its formattion
    print('Step 0.1: Source code file scoring...')
    start_time = time.time()
    fileScore = sdf.calcScore(tokensEncoded,lm)
    print("Step 0.1: ---- %s seconds ---\n" % round( (time.time() - start_time), 4))


    print('Step 1: Snippets length calculation...')
    start_time = time.time()
    snippetsLengths = sdf.snippetLengthCalculation(snippets, lengths, Params.TOK_PER_SNIP)
    print("Step 1: ---- %s seconds ---\n" % round( (time.time() - start_time), 4))


    print('Step 2: Snippets score calculation using entropy metric...')
    start_time = time.time()
    snippetsScores = sdf.snippetsScoreCalculation(snippets, lm)
    print("Step 2: ---- %s seconds ---\n" % round( (time.time() - start_time), 4))

    print('Step 3: Encoding of snippets...')
    start_time = time.time()
    # Encoding of snippets: the first snippet is represented by zero, the second snippet is represented by one etc 
    snippetsEncoded = [] 
    for j,_ in enumerate(snippets):
        snippetsEncoded.append(j)
    # Encoding of tokens: so the first token of the file is represented by zero, the second is represented by one etc
    # We perform a different kind of encoding than the one that we performed before
    # With this encoding, we just obtain the position of each token in the source code file
    codeTokensEnc = []
    for j,_ in enumerate(tokensEncoded):
        codeTokensEnc.append(j)
    # Pair each encoded snippet with the its corresponding score 
    snippetsScoresEnc = {}
    for j in zip(snippetsEncoded,snippetsScores):
        snippetsScoresEnc.update([j])
    print("Step 3: ---- %s seconds ---\n" % round( (time.time() - start_time), 4))


    print('Step 4: Making a dictionary that will contain each token and its number of occurences in the source code file...')
    start_time = time.time()
    # Make n-grams that will consist of TOK_PER_SNIP tokens each.
    # Each gram will not contain each token itself, but the token's index.
    # Example: possible ngram: [0,1,2,3,4,5,...] --> contains the first token of the source code file, the second token etc
    snip_grams_enc = sdf.gramsFormationStep(codeTokensEnc, Params.TOK_PER_SNIP, 3)
    # We are making a dict named num_token_occur that will contain the number of occurences of each token in the source code file
    numTokenOccur = {}
    # for each code shippet we made previously
    for snip in snip_grams_enc:
        # for each token in this specifc code snippet
        for token_pos in snip:
            # if we already have this token in our dictionary
            if(token_pos in list(numTokenOccur.keys())):
                # add one more occurence on its number of occurences
                numTokenOccur[token_pos]+=1
            else:
                # otherwise, add a new entry on the dictionary
                numTokenOccur.update([(token_pos,1)])
    print("Step 4: ---- %s seconds ---\n" % round( (time.time() - start_time), 4))


    print('Step 5: Making a dictionary that will contain each token and a list of the snippets that this token belongs to...')
    start_time = time.time()
    # key---> each token encoded 
    # value---> a list that consists of the windows(20-token grams) that this token is part of
    tokenOccur = {}
    for j,snip in enumerate(snip_grams_enc):
        for k in snip:
            if(k in list(tokenOccur.keys())):
                tokenOccur[k].append(j)
            else:
                tokenOccur.update([(k,[j])])
    print("Step 5: ---- %s seconds ---\n" % round( (time.time() - start_time), 4))


    print('Step 6: Calculation of each token score...')
    start_time = time.time()
    #breakpoint()
    tokenScores = sdf.tokensScoreCalculation(tokenOccur,snippetsScoresEnc)
    print("Step 6: ---- %s seconds ---\n" % round( (time.time() - start_time), 4))


    print("Total Time of scoring(seconds): ---- %s seconds ---" % round( (time.time() - funcStartTime), 4))
    print("Total Time of scoring(hours:mins:seconds): ---- %s ---\n" % str(datetime.timedelta(seconds = round( (time.time() - funcStartTime), 4))))
    # breakpoint()

    return tokenScores, snippetsScores, fileScore, snippets, snippetsLengths




