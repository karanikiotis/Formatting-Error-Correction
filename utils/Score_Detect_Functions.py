import pickle
import sys
from nltk.util import ngrams
from nltk.lm.preprocessing import pad_both_ends
sys.path.insert(0,'/mnt/c/CodeRepository/Formatting-Error-Correction')
from utils.helper_func import truncate

from utils import tokenizer


# A list that contains all the tokens that can possibly appear on a source code file
# This list of unique tokens is the vocabulary 
vocabulary = ["<start>","<end>","<eos>","<tab>","<spacetab>","<dot>","<comma>","<semicolon>","<colon>","<exclamation>",
                    "<at>","<hash>","<dollar>","<perc>","<caret>","<and>","<power>","<open_par>","<close_par>","<minus>",
                    "<plus>","<equal>","<open_bracket>","<close_bracket>","<open_curly>","<close_curly>","<less_than>",
                    "<greater_than>","<quest>","<back_slash>","<keyword>","<literal>","<lit>","<word>","<space>",
                    "<com>","<slash>","<string>","<char>","<number>","<unk>"]

vocabEncoded = dict((c, i) for i, c in enumerate(vocabulary))

def calcScore(snip, lm): 
    """
    Description:
        A function that calculates the score of a code snippet regarding its formattion using entropy metric
    Inputs: 
        snip (List of integers) : A list of ID's of the tokens that the code snippet consists of
        lm () :
    Outputs:
        score (float) : Score of the code snippet
    """
    #### Fix in order to support both String and encoded tokens as fixes
    if(type(snip) is str):
        tokens,_ = tokenizer.tokenize(snip)
        snip = [vocabEncoded[x] for x in tokens]
    # Snippet's tokens are encoded
    # So, we make a new list called snip_transf that will include the names of the tokens instead of their encoded values
    snipTransf = []
    for i in snip:
        snipTransf.append(vocabulary[i])
    # Pad both the start and the end of the tokens list in order to signify the start and the end of the code snippet
    snipPadded = list(pad_both_ends(snipTransf, n=10))
    # Formattion of 10-grams
    snipFinal = list(ngrams(snipPadded,10))
    # Using entropy metric to score the code snippet regarding its formattion
    score = lm.entropy(snipFinal)
    return score


def ngramModelImport(path, filename):
    """
    Description:
        
    Inputs: 
        path ()
        filename ()
    Outputs:
        lm () : 
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

    Inputs: 

    Outputs:
        
    """

    snipsScores = [] 
    numOfIterations = 0
    for snip in snippets:
        # Calculate score for the current code snippet
        score = calcScore(snip,lm)    
        # Append the score to the list
        snipsScores.append(score)
        numOfIterations += 1
    for j,s in enumerate(snipsScores):
        # Checking the case if an inf value exists in snips_scores list
        # In this case, we set this value equal to the max score that appeared in our snip_score list.
        # Max score will be the worst score that appeared
        # Disclaimer: As entropy is getting bigger values, this means a worse formattion score.
        if(s == float('inf')): 
            snipsScores[j] = max(snipsScores)
    print(f'Step 2: Total number of iterations(snippetsScoreCalculation) ---- {numOfIterations} ----' )
    return snipsScores


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

    Inputs:

    Outputs
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
