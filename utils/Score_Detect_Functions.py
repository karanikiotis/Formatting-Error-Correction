import pickle
import numpy as np
from nltk.util import ngrams
from nltk.lm.preprocessing import pad_both_ends
from utils.helper_func import truncate
from utils import tokenizer

def calcScore(snip, lm): 
    """
    Description:
        A function that calculates the score of a code snippet regarding its formattion using cross entropy metric
    Inputs: 
        snip (List of integers) : A list of ID's of the tokens that the code snippet consists of
        lm (Nltk object) : N-gram language model used to score each snippet.
    Outputs:
        score (float) : Score of the code snippet
    """

    if(type(snip) is str):
        snip,_ = tokenizer.tokenize(snip)
    # Pad both the start and the end of the tokens list in order to signify the start and the end of the code snippet
    snipPadded = list(pad_both_ends(snip, n = 10))
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
        This function is responsible for calculating length for each code's snippet.
    Inputs:
        snippets (List of Lists): A list of lists. Each one of the list represents the code snippets.
        tokensLength (List of Integers): A list tha represents each token's length.
        tokensPerSnip (Integer): An integer number that declares the number of the tokens that each code snippet has.
    Outputs:
        snipLengths (List of Integers): A list that represents the length of each code snippet.
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
            consists of a certain number of tokens. Tokens are represented through each IDs.
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
        This function is responsible for calculating cross entropy score for each token according to code snippets cross entropy scores.
    Inputs:
        tokenOccur (Dictionary): A dictionary that represents the code snippets that each token appears. For example 80: [21,22,23,24] 
        means that the 80th tokens appears on 21st, 22nd, 23rd and 24th code snippet.
        snipScoresEnc (Dictionary): A dictionary that represents entropy score for each code snippet

    Outputs:
        tokensScores (List): A list of cross entropy scores for each token.
    """

    scorePerToken = {}
    for key in list(tokenOccur.keys()):
        currTokenScore = 0
        for snip in tokenOccur[key]:
            currTokenScore += snipScoresEnc[snip]
        score = currTokenScore/len(tokenOccur[key])
        score = truncate(score,2)
        scorePerToken.update([(key,score)])
    tokensScores = list(scorePerToken.values())
    
    return tokensScores


def tokensEncode(tokens, tokensMap):
    """
    Description:
        This function is responsible for token encoding according to vocabulary. So, the first token in the vocabulary will be 
            represented by 1, the second one will be represented by 2 etc.
    Inputs:
        tokens (List): A list of file's tokens
        tokensMap (Dictionary): A dictionary that encodes each available token in vocabulary with an integer number
    Outputs:
        tokensEnc (List): A list of encoded tokens
    """

    # Encode tokens. Each token of the source code will be represent by a positive integer value
    tokensEnc = [tokensMap[x] for x in tokens]
    return tokensEnc


def gramsFormationStep(tokensList, tokensPerSnippet, step):
    """
    Description:
        This function is responsible for producing code snippets according to tokensPerSnippet and step arguments.

    Inputs:
        tokensList (List of Strings): A list which consists of file's tokens.
        tokensPerSnippet (Integer): An integer number that declares the number of the tokens that each code snippet is going to have.
        step (Integer): An integer number that declares the step between the different code snippets

    Outputs:
        snips (List of Lists): A list of lists. Each one of the list represents the code snippets.
    """

    snips = []
    for idx in range(0, len(tokensList) - tokensPerSnippet + step, step):
        snips.append(tokensList[idx : idx + tokensPerSnippet])

    return snips
