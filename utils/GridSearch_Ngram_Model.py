#Basic Functions - Classes used from NTLK Package

#1. ngrams: used to make N-grams of a specified order

#2. pad_both_ends: used to add start & end symbol to our corpus files-sentences. Order should be chosen according to n-grams order

#3. everygrams: used to make n-grams of all orders. The highest order is defined through max_len parameter

#4. flatten: Creates the vocab of our ngrams model. It consists of all words-tokens of our corpus

#5. padded_everygram_pipeline: creates two iterators:
        #i. sentences padded and turned into sequences 
        #ii. sentences padded as above and chained together for a flat stream of words

import sys
sys.path.insert(0,'C:\CodeRepository\Formatting-Error-Correction')

from datetime import datetime
from nltk.util import everygrams
from nltk.lm.preprocessing import padded_everygram_pipeline,pad_both_ends
from nltk.lm import KneserNeyInterpolated
from Scripts.S1_corpus_bigrams_occurences import count_occur

_,train_tok = count_occur('\\10k_Pristine_Train_Dataset\training_files') #Training set consist of 9000 Java files
_,test_tok = count_occur('\\10k_Pristine_Train_Dataset\validation_files') #Testing set consist of 1014 Java files

order = [x for x in range(3,11)]
entr = []
j = 0

for ord in order:
    #Training N-Grams of different orders: n = 3,4,5,6,7,8,9,10
    train_set,vocab = padded_everygram_pipeline(ord,train_tok)
    kn = KneserNeyInterpolated(ord)
    kn.fit(train_set,vocab)

    #Testing each trained N-gram model
    current_score = []
    for test in test_tok:
        curr_test_padded = list(pad_both_ends(test,n=ord))
        curr_test_set = list(everygrams(curr_test_padded,max_len = ord))

        current_score.append(kn.entropy(curr_test_set))
        
    entr.append(sum(current_score)/len(current_score))
    print(f'N-gram order:{ord}, Avg Score on Test Set:{entr[j]}, Time:{datetime.now()}')   

    j+=1   

