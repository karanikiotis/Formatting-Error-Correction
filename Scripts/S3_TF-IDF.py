import sys
import os
import pickle
import math
from collections import Counter
from nltk.util import ngrams
from Old_Scripts.tokenizer import tokenize
from S1_corpus_bigrams_occurences import count_occur
from S2_corpus_bigrams_unique_occurences import count_unique_occur

sys.path.insert(0,'C:\CodeRepository\Thesis\Predict-Fix\Scripts')

with open(r'C:\CodeRepository\Thesis\Data\occurences_new.p', 'rb') as fp: 
  occurences = pickle.load(fp)
  #print(f'All_counts: {occurences}\n')
  #print(f'Total number of bigrams: {len(occurences.keys())}\n')

corpus_bigrams = count_occur()
unique_occurences = count_unique_occur(corpus_bigrams)

os.chdir(r'C:\CodeRepository\Thesis\Predict-Fix\Scripts')

tokens_available = ["<start>","<end>","<eos>","<tab>","<spacetab>","<dot>","<comma>","<semicolon>","<colon>","<exclamation>",
                    "<at>","<hash>","<dollar>","<perc>","<caret>","<and>","<power>","<open_par>","<close_par>","<minus>",
                    "<plus>","<equal>","<open_bracket>","<close_bracket>","<open_curly>","<close_curly>","<less_than>",
                    "<greater_than>","<quest>","<back_slash>","<keyword>","<literal>","<lit>","<word>","<space>",
                    "<com>","<slash>","<string>","<char>","<number>","<unk>"]

d = dict((c, i) for i, c in enumerate(tokens_available))

#Reading the code snippet file
f = open(sys.argv[1],'r',encoding = 'utf-8')
code_snippet = f.read() 

[tokens,length] = tokenize(code_snippet) #Tokenization of the code snippet
tokens_enc = [0]+[d[x] for x in tokens]+[1] #Add 0 & 1 in order to indicate the start and the end of the code snippet 

code_snippet_bigrams = list(ngrams(tokens_enc, 2)) #Bigrams formation for the current code snippet

num_of_bigram_appear = dict(Counter(code_snippet_bigrams))
num_of_bigram_appear = dict(sorted(num_of_bigram_appear.items())) #Number of appearances for each bigram of the current code snippet

num_of_bigrams = len(code_snippet_bigrams) #Total number of bigrams of the current code snippet

N = 10016 #corpus size

#1.Computation of Term Frequency(TF) for each bigram of the current code snippet
TF = {}
for key,value in num_of_bigram_appear.items():
    TF.update({key: round((value/num_of_bigrams),3)})
print(f'\nCode Snippet TF:{TF}\n\n')

#2.Computation of DF for the whole corpus
DF = {}
for key,value in unique_occurences.items() :
    DF.update({key: round((value/N),3)})
#print(f'DF(for whole corpus):{DF}\n\n')

#3.Computation of IDF for the whole corpus
IDF = {}
for key,value in DF.items():
    IDF.update({key: round(math.log(N/(value+1)),3)})
#print(f'IDF:{IDF}\n\n')

#4.Formation of IDF for the current_code_snippet 
curr_code_snippet_IDF = {}
for key in TF.keys():
    if(key in IDF.keys()): #check if the current_bigram exists in the corpus
        curr_code_snippet_IDF.update({key: IDF[key]})
    else:
        curr_code_snippet_IDF.update({key: 0})
print(f'Code Snippet IDF: {curr_code_snippet_IDF}\n\n')

#5.Computation of TF-IDF for each bigram of the current code snippet
TF_IDF = {} 
i = 0
for tf_value,idf_value in zip(TF.values(),curr_code_snippet_IDF.values()):
    TF_IDF.update({list(TF.keys())[i]:round(tf_value*idf_value,3)})
    i += 1
print(f'Code Snippet TF-IDF: {TF_IDF}')




