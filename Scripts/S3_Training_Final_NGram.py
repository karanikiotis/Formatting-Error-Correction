#Basic Functions - Classes used from NTLK Package

#1. ngrams: used to make N-grams of a specified order

#2. pad_both_ends: used to add start & end symbol to our corpus files-sentences. Order should be chosen according to n-grams order

#3. everygrams: used to make n-grams of all orders. The highest order is defined through max_len parameter

#4. flatten: Creates the vocab of our ngrams model. It consists of all words-tokens of our corpus

#5. padded_everygram_pipeline: creates two iterators:
        #i. sentences padded and turned into sequences 
        #ii. sentences padded as above and chained together for a flat stream of words

import sys
import os
sys.path.insert(0,'/Users/Shared/c/CodeRepository/Formatting-Error-Correction/')
import pickle

from utils.tokenizer import tokenize
from nltk.util import everygrams
from nltk.lm.preprocessing import padded_everygram_pipeline
from nltk.lm import KneserNeyInterpolated
from S1_Corpus_Bigrams_Occurences import count_occur

directory = 'LSTM_TrainingDataset'
_,corpusTok = count_occur(directory) 
ord = 10
trainSet,vocab = padded_everygram_pipeline(ord,corpusTok)
kn = KneserNeyInterpolated(ord)
kn.fit(trainSet,vocab)

filename = '/Users/Shared/c/CodeRepository/Formatting-Error-Correction/10_Gram_Model/10_gram_model_v4.p'
outfile = open(filename,'wb')
pickle.dump(kn,outfile)
outfile.close

