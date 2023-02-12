import pickle
import sys
import numpy as np
from nltk.util import ngrams
from nltk.lm.preprocessing import pad_both_ends
from utils.helper_func import truncate
from utils import tokenizer

import debugpy
# debugpy.listen(5678)
# print('Debugging Session\n')
# debugpy.wait_for_client()

def calcScore(snip, lm): 
    """
    Description:
        A function that calculates the score of a code snippet regarding its formattion using entropy metric
    Inputs: 
        snip (List of integers) : A list of ID's of the tokens that the code snippet consists of
        lm (Nltk object) : N-gram language model used to score each snippet.
    Outputs:
        score (float) : Score of the code snippet
    """
    if(type(snip) is str):
        snip,_ = tokenizer.tokenize(snip)
    # Pad both the start and the end of the tokens list in order to signify the start and the end of the code snippet
    snipPadded = list(pad_both_ends(snip, n=10))
    # Formattion of 10-grams
    snipFinal = list(ngrams(snipPadded,10))
    # Using entropy metric to score the code snippet regarding its formattion
    score = lm.entropy(snipFinal)
    return score


def ngramModelImport(path, filename):
    """
    Description:
        A function that is used to import the trained n-gram model. N-gram model is used to score both code's snippets and code's tokens.

    Inputs: 
        path (String): Absolute path on which the model's files is located.
        filename (String): Name of the model's file.

    Outputs:
        lm (Nltk object) : N-gram language model used to score each snippet.

    """

    finalPath = path + '/' + filename 
    with open(finalPath, "rb") as fp:
        lm = pickle.load(fp)
    return lm 


def snippetLengthCalculation(snippets, tokensLength, tokensPerSnip):
    """
    Description:
        
    Inputs: 
        
    Outputs:
        
    """

    pos = 0
    snipLengths = []
    for i, snip in enumerate(snippets):
        pos += sum(tokensLength[i * tokensPerSnip: (i * tokensPerSnip ) + len(snip)]) 
        snipLengths.append(pos)
    
    return snipLengths


def snippetsScoreCalculation(snippets, lm):
    """
    Description:
        A function that is used to calculate entropy score for each of the snippets that the source code file consists of.

    Inputs: 
        snippets (List): A list of lists, each one of them represents a code snippet. Each snippet 
            consist of a certain number of tokens. Tokens are represented through each IDs.
        lm (Nltk object): N-gram language model used to score each snippet.

    Outputs:
        snipScores (List): A list of the scores for each code's snippet.

    """

    snipsScores = [] 
    numOfIterations = 0
    for snip in snippets:
        # Calculate score for the current code snippet
        score = calcScore(snip,lm)
        # Append the score to the list
        snipsScores.append(score)
        numOfIterations += 1
    snipsScores = processSnipScores(snipsScores)
    print(f'Step 2: Total number of iterations(snippetsScoreCalculation) ---- {numOfIterations} ----' )
     
    return snipsScores


def processSnipScores(snippetsScores):
    """
    Description:
        This function is responsible for preprocessing snippet's scores, which are produced using entropy metric.
        In case, a score with Inf value is produced we are using the max score between the snippets to replace it.

    Inputs: 
        snippetsScores (List): A list of snippets scores produced from snippetsScoreCalculation function

    Outputs:
        snipScores (List): A list of the preprocessed snippets scores.

    """

    # Convert snippetsScores list into a Numpy array.
    snippetsScores = np.array(snippetsScores)
    # Make a mask which is True, only on indices where snippetsScores equal to Inf.
    maskInf = np.zeros(snippetsScores.size, dtype = bool)
    maskInf[snippetsScores == float('inf')] = True
    # Make a new Numpy array which has only the non-Inf values of snippetsScores Numpy array.
    snippetScoresMask = np.ma.array(snippetsScores, mask = maskInf)
    for j,s in enumerate(snippetsScores):
        # Checking the case if an inf value exists in snips_scores list
        # In this case, we set this value equal to the max score that appeared in our snip_score list.
        # Max score will be the worst score that appeared
        # Disclaimer: As entropy is getting bigger values, this means a worse formattion score.
        if(s == float('inf')): 
            snippetsScores[j] = np.max(snippetScoresMask)
    return snippetsScores


def tokensScoreCalculation(tokenOccur, snipScoresEnc):
    """
    Description:

    Inputs:
        tokenOccur ():
        snipScoresEnc (): 

    Outputs:
        tokensScores ():
    """

    scorePerToken = {}
    for key in list(tokenOccur.keys()):
        curr_tok_score = 0
        for snip in tokenOccur[key]:
            curr_tok_score += snipScoresEnc[snip]
        score = curr_tok_score/len(tokenOccur[key])
        score = truncate(score,2)
        scorePerToken.update([(key,score)])
    tokensScores = list(scorePerToken.values())
    
    return tokensScores


def tokensEncode(tokens, tokensMap):
    """
    Description:
        This responsible is responsible for token encoding according to vocabulary. So, the first token in the vocabulary will be 
            represented by 1, the second one will be represented by 2 etc.

    Inputs:
        tokens (List):
        tokensMap ():

    Outputs:
        tokensEnc (List):
    """

    # Encode tokens. Each token of the source code will be represent by a positive integer value
    tokensEnc = [tokensMap[x] for x in tokens]
    return tokensEnc


def gramsFormationStep(tokensEncoded, tokensPerSnippet, step):
    """
    Description:

    Inputs:

    Outputs
    """

    snips = []
    for idx in range(0, len(tokensEncoded) - tokensPerSnippet + step, step):
        snips.append(tokensEncoded[idx : idx + tokensPerSnippet])
    
    return snips



def errorDetectionThresh(code, thresh): #Returns the tokens with higher score than the THRESHOLD
    """
    Description:

    Inputs:
         

    Outputs:

    """

    THRESHOLD = thresh

    poss_err_tokens = {}
    s_tokens,_,_,_,_ = get_score(code)
    for key in list(s_tokens.keys()):
        if(s_tokens.get(key,None) >= THRESHOLD):
            poss_err_tokens.update([(key,s_tokens.get(key,None))])

    [tokens,lengths] = tokenizer.tokenize(code)

    len_sum = [1]
    for j in range(0,len(lengths)):
        len_sum.append(len_sum[j]+lengths[j])

    token_len = {}
    for j,leng in enumerate(len_sum):
        token_len.update([(j,leng)])

    err_tokens_info = []
    for key in list(poss_err_tokens.keys()):
        for k in list(token_len.keys()):
            if(key == k):
                err_tokens_info.append((key,tokens[key],token_len.get(key,None),s_tokens.get(key,None)))
    return err_tokens_info


def errorDetectionTopN(code, topn): #Returns the top 10 tokens with the highest score

    """
    Description:

    Inputs:

    Outputs:

    """

    n = topn

    s_tokens,_,_ = get_score(code)
    sort_s_tokens = sorted(s_tokens.items(),key = lambda x:x[1],reverse = True)
    sort_s_tokens = dict(sort_s_tokens)

    i = 1
    poss_err_tokens = {}
    for key in list(sort_s_tokens.keys()):
        if(i<= n):
            poss_err_tokens.update([(key,sort_s_tokens.get(key,None))])
            i+=1
        else:
            break

    [tokens,lengths] = tokenizer.tokenize(code)

    len_sum = [1]
    for j in range(0,len(lengths)):
        len_sum.append(len_sum[j]+lengths[j])

    token_len = {}
    for j,leng in enumerate(len_sum):
        token_len.update([(j,leng)])

    err_tokens_info = []
    for key in list(poss_err_tokens.keys()):
        for k in list(token_len.keys()):
            if(key == k):
                err_tokens_info.append((key,tokens[key],token_len.get(key,None),poss_err_tokens.get(key,None)))
    
    return err_tokens_info
