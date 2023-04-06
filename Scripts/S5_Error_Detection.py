import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
import numpy as np
from tensorflow import keras
from utils.tokenizer import tokenize
import S7_Parameters as Params

def lstmErrorDetection(code):
    """
    Description:
        Use a trained LSTM network in order to detect possible tokens that are formatted wrongly. 

    Inputs:
        code (String): A string that represents the source code file for fixing its formattion.

    Outputs:
        errProb (List): A list of the probabilities of each token to be a formatting error. We call them, error probabilities.
        startPositionsPerToken (List): A list of the starting position of each token.
        suggestedFixes (List): 
    """

    # Importing LSTM Model
    lstm_model = keras.models.load_model(Params.path+"LSTM Model/LSTM_v3.h5")

    d = dict((c, i) for i, c in enumerate(Params.tokensAvailable))
    d_inv = dict((i, c) for i, c in enumerate(Params.tokensAvailable))

    [tokens, lengths] = tokenize(code)

    # Append <start> and <end> tokens   
    tokens_enc = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0] \
                 + [d[x] for x in tokens] \
                 + [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]

    for i in range(20):
        # zero length for 20 <start> tokens
        lengths.insert(0, 0)
        # zero length for 20 <end> tokens
        lengths.insert(len(lengths), 0)

    # Holds the starting position of each token
    startPositionsPerToken = []  
    # Holds the error propability acording to LSTM, for the next token of the iterating 20-gram.
    # Next tokens with higher error probabilities are more likely not to be formatted wrongly.
    errProb = [] 
    suggestedFixes = {}
    ngrams = []
    idxOfNextToken = []

    for i in range(len(tokens_enc)-19):
        ngrams.append(tokens_enc[i : i+20])

    # Calculation of index of the next token.
    # On each position of the Numpy array, we have the index of the token
    # that is going to appear after each 20-gram.
    for i in range(len(ngrams)-1):
        idxOfNextToken.append(ngrams[i+1][19])

    curPosition = 0
    # Iterating through each one of the 20-grams.
    for i, testgram in enumerate(ngrams[:-21]):
        # Converting each 20-gram from a list into an NumPy array in order
        # to be used as input to the LSTM model.
        iterGram = np.asarray(testgram)
        # LSTM is using the iterating 20-gram as input and is producing one estimation per token that exists on
        # tokensAvailable list. That estimation represents the probability for each token of the vocabulary, to be the next token after 
        # this 20-gram. PredictionsLSTM is a 41x1 array.
        predictionsLSTM = lstm_model.predict(iterGram.reshape(1, 20), verbose = 0)
        # Keep the index of the token that is the most possible to appear after the iterating 20-gram, 
        # by finding the highest probability among all.
        predictedToken = (np.where(predictionsLSTM[0] == np.amax(predictionsLSTM[0]))[0][0]) 
        # Sort probabilities on descending order. possibleFixes is a list that contains all the possible tokens
        # on descending order. Tokens are represented with their characters.
        possibleFixes = [repl for _,repl in sorted(zip(predictionsLSTM[0], Params.replacements), reverse = True)] 
        # Calculate the starting position of the current token
        curPosition += lengths[i+19]
        # Append the starting position of the current token to a list that holds the starting positions of each token
        startPositionsPerToken.append(curPosition)
        # Keep the tokens with the highest probabilities. These tokens are going to be the suggested fixes. 
        # The number of tokens that we keep as suggested fixes for the iterating 20-gram, is defined through a parameter called numOfSuggFixes.
        # So, for each ngram, we are keeping a specific number of tokens as suggested fixes. 
        topFixesCurGram = possibleFixes[:Params.numOfSuggFixes]
        suggestedFixes.update({curPosition:topFixesCurGram})
        # Calculation of error probability according to LSTM, for the next token of the iterating 20-gram.
        # So, next tokens with higher error probabilities are more likely not to be formatted correctly.
        errProb.append(1.0 - predictionsLSTM[0][idxOfNextToken[i]])
        # print("Position: ", curPosition)
        # print("Predicted next token: ", d_inv.get(predictedToken))
        # print("Token found in code: ", d_inv.get(idxOfNextToken[i])) 
        # print("Error probability: ", 1.0 - predictionsLSTM[0][idxOfNextToken[i]], "\n")

    return errProb, startPositionsPerToken, suggestedFixes