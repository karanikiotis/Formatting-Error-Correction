from stat import SF_APPEND
import numpy as np
import pickle
import pdb
from utils.helper_func import truncate
from Scripts.tokenizer import tokenize
from nltk.util import ngrams
from nltk.lm.preprocessing import pad_both_ends 
from utils import Score_Detect_Functions as sdf

tokens_available = ["<start>","<end>","<eos>","<tab>","<spacetab>","<dot>","<comma>","<semicolon>","<colon>","<exclamation>",
                    "<at>","<hash>","<dollar>","<perc>","<caret>","<and>","<power>","<open_par>","<close_par>","<minus>",
                    "<plus>","<equal>","<open_bracket>","<close_bracket>","<open_curly>","<close_curly>","<less_than>",
                    "<greater_than>","<quest>","<back_slash>","<keyword>","<literal>","<lit>","<word>","<space>",
                    "<com>","<slash>","<string>","<char>","<number>","<unk>"]
d = dict((c, i) for i, c in enumerate(tokens_available))

def ngramScore(code):
    """
    Description:

    Inputs:
        code () :

    Outputs:
        tokenScoreS () :
        snippetsScores () :
        fileScore () :
        snippets () :
        snippetsLengths () :
    """
    
    # We have trained a 10-gram model that will be used for code snippet scoring
    # Load 10-gram model
    lm = sdf.ngramModelImport(r'C:\CodeRepository\Formatting-Error-Correction\10-Gram Model','10_gram_model_v2.p')

    # For each one of 20-token snippets, we are going to calculate its score
    # Next, in order to calculate the score for each token, we are going to
    # see in which snippets, each token belongs to.
    # Each token's score will be the mean of the scores of the code snippets where it belongs to

    # TOK_PER_SNIP represents the number of tokens that its code snippet will be consisted of 
    tokensPerSnippet = 20 
    # Tokenize code
    print('Step 0: Source code file tokenization...\n')
    [tokens,lengths] = tokenize(code)
    # Encode tokens. Each token of the source code will be represent by a positive integer value
    tokens = [d[x] for x in tokens]
    # Calculate the score of the whole source code file regarding its formattion
    print('Step 0.1: Source code file scoring...\n')
    fileScore = sdf.calcScore(tokens,lm)
    #pdb.set_trace()
    # Break source code file into snippets (ngrams) that will consist of TOK_PER_SNIP tokens
    snippets = list(ngrams(tokens,tokensPerSnippet))

    print('Step 1: Snippets length calculation...\n')
    snippetsLengths = sdf.snippetLengthCalculation(snippets,lengths,tokensPerSnippet)

    print('Step 2: Snippets score calculation using entropy metric...\n')
    snippetsScores = sdf.snippetsScoreCalculation(snippets,lm)

    print('Step 3: Encoding of snippets...\n')
    # Encoding of snippets: the first snippet is represented by zero, the second snippet is represented by one etc 
    snippetsEncoded = [] 
    for j,_ in enumerate(snippets):
        snippetsEncoded.append(j)
    # Encoding of tokens: so the first token of the file is represented by zero, the second is represented by one etc
    # We perform a different kind of encoding than the one that we performed before
    # With this encoding, we just obtain the position of each token in the source code file
    codeTokensEnc = []
    for j,_ in enumerate(tokens):
        codeTokensEnc.append(j)
    # Pair each encoded snippet with the its corresponding score 
    snippetsScoresEnc = {}
    for j in zip(snippetsEncoded,snippetsScores):
        snippetsScoresEnc.update([j])

    print('Step 4: Making a dictionary that will contain each token and its number of occurence in the source code file...\n')
    # Make n-grams that will consist of TOK_PER_SNIP tokens each.
    # Each gram will not contain each token itself, but the token's position.
    # Example: possible ngram: [0,1,2,3,4,5,...] --> contains the first token of the source code file, the second token etc
    snip_grams_enc = list(ngrams(codeTokensEnc,tokensPerSnippet))
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

    print('Step 5: Making a dictionary that will contain each token and a list of the snippets that this token belongs to...\n')
    # key---> each token encoded 
    # value---> a list that consists of the windows(20-token grams) that this token is part of
    tokenOccur = {}
    for j,snip in enumerate(snip_grams_enc):
        for k in snip:
            if(k in list(tokenOccur.keys())):
                tokenOccur[k].append(j)
            else:
                tokenOccur.update([(k,[j])])

    print('Step 6: Calculation of each token score...')
    tokenScores = sdf.tokensScoreCalculation(tokenOccur,snippetsScoresEnc)

    return tokenScores, snippetsScores, fileScore, snippets, snippetsLengths




