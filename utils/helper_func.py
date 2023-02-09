import math
import numpy as np
import sys
import os
import S7_Parameters as Params

def truncate(number, digits):
    """
    Description:

    Inputs:

    Outputs:

    """
    stepper = 10.0 ** digits
    return math.trunc(stepper * number) / stepper

def tokenScoreInterp(tokenScores):
    """
    Description:

    Inputs:

    Outputs:

    """

    min_score_token = min(tokenScores)
    max_score_token = max(tokenScores)
    tokenScoresMapped = []
    for i in tokenScores:
        tokenScoresMapped.append(truncate(np.interp(i,[min_score_token,max_score_token],[1,0]),4))
    return tokenScoresMapped

def errProbModif(errProb, tokenScoresMapped):
    """
    Description:

    Inputs:

    Outputs:

    """
    for i in range(len(errProb)):
        errProb[i] *= 1-tokenScoresMapped[i]
        errProb[i] = truncate(errProb[i],4)
    return errProb

def writeFixedCode(fileName,acc_codes):
    """
    Description:

    Inputs:

    Outputs:

    """
    path = os.path.join(Params.fixDir, fileName)
    os.makedirs(path, exist_ok = True)
    for i, code in enumerate(acc_codes):
        with open(os.path.join(path, str(i+1) + ".txt"), 'w') as f:
            f.write(code)
    print("Possible fixed codes saved in folder: " + path)