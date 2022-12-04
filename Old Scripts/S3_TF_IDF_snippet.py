import sys
sys.path.insert(0,'C:\CodeRepository\Formatting-Error-Correction')
import os
import pickle
from collections import Counter
from nltk.util import ngrams
from Old_Scripts.tokenizer import tokenize

#with open(r'C:\CodeRepository\Thesis\Data\occurences_new.p', 'rb') as fp: 
  #occurences = pickle.load(fp)
  #print(f'All_counts: {occurences}\n')
  #print(f'Total number of bigrams: {len(occurences.keys())}\n')

def tf_idf_snippet(code_snippet,vocab,corpus_idf):

    tokens_available = ["<start>","<end>","<eos>","<tab>","<spacetab>","<dot>","<comma>","<semicolon>","<colon>","<exclamation>",
                        "<at>","<hash>","<dollar>","<perc>","<caret>","<and>","<power>","<open_par>","<close_par>","<minus>",
                        "<plus>","<equal>","<open_bracket>","<close_bracket>","<open_curly>","<close_curly>","<less_than>",
                        "<greater_than>","<quest>","<back_slash>","<keyword>","<literal>","<lit>","<word>","<space>",
                        "<com>","<slash>","<string>","<char>","<number>","<unk>"]
    d = dict((c, i) for i, c in enumerate(tokens_available))

    N = 10016 #corpus size

    #1.Tokenization of the code snippet
    [tokens,length] = tokenize(code_snippet) 
    #2.Encoding the tokens -- Add 0 & 1 in order to indicate the start and the end of the code snippet 
    tokens_enc = [0]+[d[x] for x in tokens]+[1] 
    #3.Bigrams formation
    code_snippet_bigrams = list(ngrams(tokens_enc, 2)) 
    #4.Number of each bigram appearances
    num_of_bigram_appear = dict(Counter(code_snippet_bigrams))
    #5.Total number of bigrams of the code snippet
    num_of_bigrams = len(code_snippet_bigrams) 
 
    #Computation of TF for each bigram of the code snippet
    snippet_tf = {}
    for key,value in vocab.items():
        if(key in num_of_bigram_appear.keys()):
            snippet_tf.update({key: round((num_of_bigram_appear[key]/num_of_bigrams),4)})
        else:
            snippet_tf.update({key: 0})
    #print(f'\nCode Snippet snippet_tf:{snippet_tf}\n\n')

    #Computation of IDF for each bigram of the code snippet 
    snippet_idf = {}
    for key in snippet_tf.keys():
        if(key in corpus_idf.keys()): #check if the current_bigram exists in the corpus
            snippet_idf.update({key: corpus_idf[key]})
        else: #if does not, give IDF a zero value
            snippet_idf.update({key: 0})
    #print(f'Code Snippet IDF: {snippet_idf}\n\n')

    #Computation of TF-IDF for each bigram of the code snippet
    snippet_tf_idf = {} 
    i = 0
    keys = list(corpus_idf.keys())
    for tf_value,idf_value in zip(snippet_tf.values(),corpus_idf.values()):
        snippet_tf_idf.update({keys[i]:round(tf_value*idf_value,4)})
        i += 1
    #print(f'Code Snippet snippet_Tf-IDF: {snippet_tf_idf}')

    return snippet_tf_idf


