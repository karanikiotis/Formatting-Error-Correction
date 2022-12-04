import sys
sys.path.insert(0,'C:\CodeRepository\Formatting-Error-Correction')
import os
import math
from collections import Counter
from nltk.util import ngrams
from Old_Scripts.tokenizer import tokenize

#Computation of DF for the whole corpus
def df_corpus(vocab):
    N = 10016 #corpus size
    DF = {}
    for key,value in vocab.items() :
        DF.update({key: round((value/N),3)})
    #print(f'DF(for whole corpus):{DF}\n\n')
    return DF


#Computation of IDF for the whole corpus
def idf_corpus(df_corpus):
    N = 10016 #corpus size
    IDF = {}
    for key,value in df_corpus.items():
        IDF.update({key: round(math.log(N+1/(value+1)),4)})
    #print(f'IDF:{IDF}\n\n')
    return IDF


#Computation of TF-IDF for the whole corpus
def tf_idf_corpus(vocab,idf_corpus):

    tokens_available = ["<start>","<end>","<eos>","<tab>","<spacetab>","<dot>","<comma>","<semicolon>","<colon>","<exclamation>",
                        "<at>","<hash>","<dollar>","<perc>","<caret>","<and>","<power>","<open_par>","<close_par>","<minus>",
                        "<plus>","<equal>","<open_bracket>","<close_bracket>","<open_curly>","<close_curly>","<less_than>",
                        "<greater_than>","<quest>","<back_slash>","<keyword>","<literal>","<lit>","<word>","<space>",
                        "<com>","<slash>","<string>","<char>","<number>","<unk>"]
    d = dict((c, i) for i, c in enumerate(tokens_available))

    folder_path = r'C:\CodeRepository\Thesis\Data\Corpus_Java' #Corpus_Java folder contains the 10K Java files
    os.chdir(folder_path)

    corpus_TF_IDF = []
    for i,file in enumerate(os.listdir()):    
        #1. Reading each Java code snippet
        f = open(file, "r", errors = 'ignore')
        curr_file = f.read()
        #2. Tokenize code snippet
        [tokens,length] = tokenize(curr_file) 
        #3. Encoding the tokens -- Add 0 & 1 in order to indicate the start and the end of the code snippet 
        tokens_enc = [0]+[d[x] for x in tokens]+[1]
        #4.#Bigrams Formation 
        current_snippet_bigrams = list(ngrams(tokens_enc, 2)) 
        #5.#Number of each bigram appearances of the current corpus files
        num_of_bigram_appear = dict(Counter(current_snippet_bigrams))
        num_of_bigram_appear = dict(sorted(num_of_bigram_appear.items()))
        #6.Total number of bigrams of the current corpus file
        num_of_bigrams = len(current_snippet_bigrams) 

        #5.Computation of TF for the bigrams of the current corpus file
        TF = {}
        for key,value in vocab.items():
            if(key in num_of_bigram_appear.keys()):
                TF.update({key: round((num_of_bigram_appear[key]/num_of_bigrams),4)})
            else:
                TF.update({key: 0})

        #6.Computation of TF-IDF for each bigram of the current code snippet
        TF_IDF_curr_file = {} 
        i = 0
        keys = list(idf_corpus.keys())
        for tf_value,idf_value in zip(TF.values(),idf_corpus.values()):
            TF_IDF_curr_file.update({keys[i]:round(tf_value*idf_value,4)})
            i += 1
        
        corpus_TF_IDF.append(TF_IDF_curr_file)
    
    return corpus_TF_IDF

    