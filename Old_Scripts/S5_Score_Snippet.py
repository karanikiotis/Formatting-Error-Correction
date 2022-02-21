import sys
sys.path.insert(0,'C:\CodeRepository\Formatting-Error-Correction')
import os
import math
import numpy as np
from numpy.linalg import norm

def score_cos_similarity(snippet_TF_IDF,corpus_TF_IDF):
    scores = []

    for c in corpus_TF_IDF:
        numerator = np.dot(list(snippet_TF_IDF.values()), list(c.values()))
        denominator = norm(list(snippet_TF_IDF.values()))*norm(list(c.values()))
        cos_sim = numerator/denominator
        scores.append(cos_sim)
    
    final_score = sum(scores) / len(scores)

    return final_score
    