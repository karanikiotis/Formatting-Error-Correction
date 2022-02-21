from Scripts.tokenizer import tokenize
from collections import Counter
from itertools import chain
from nltk.util import ngrams
import pickle
import os

def count_occur(folder_name):
    tokens_available = ["<start>","<end>","<eos>","<tab>","<spacetab>","<dot>","<comma>","<semicolon>","<colon>","<exclamation>",
                        "<at>","<hash>","<dollar>","<perc>","<caret>","<and>","<power>","<open_par>","<close_par>","<minus>",
                        "<plus>","<equal>","<open_bracket>","<close_bracket>","<open_curly>","<close_curly>","<less_than>",
                        "<greater_than>","<quest>","<back_slash>","<keyword>","<literal>","<lit>","<word>","<space>",
                        "<com>","<slash>","<string>","<char>","<number>","<unk>"]

    d = dict((c, i) for i, c in enumerate(tokens_available))

    folder_path = r'C:\CodeRepository\Formatting-Error-Correction\Data' + folder_name #Corpus_Java folder contains the 10K Java files
    os.chdir(folder_path)

    corpus_bigrams = [] #Each element of the list represents a list that contains all the bigrams(as tuples) of the i-th code snippet
    corpus_tokens = []
    for i,file in enumerate(os.listdir()):
        print(f'Iteration:{i}...')

        #1. Reading each Java code snippet
        f = open(file, "r", errors = 'ignore')
        code = f.read()

        #2. Tokenize code snippet
        [tokens,length] = tokenize(code) 
        corpus_tokens.append(tokens)
        #3. Encoding the tokens
        tokens_enc = [0]+[d[x] for x in tokens]+[1] #Add 0 & 1 in order to indicate the start and the end of the code snippet

        #4. Bigrams formation
        #current_snippet_bigrams = [] #represents the bigrams(as tuples) of the current code snippet    
        current_snippet_bigrams = list(ngrams(tokens_enc, 2)) 

        corpus_bigrams.append(current_snippet_bigrams)

    #Bigrams occurences on the Corpus
    occurences = chain.from_iterable(corpus_bigrams)
    occurences = dict(Counter(occurences))
    occurences = dict(sorted(occurences.items()))

    #print(f'\n\n****Occurences of each bigram on corpus:****')
    #print(f'{occurences}')
    #print(f'Number of bigrams: {len(occurences.keys())}')

    #Pickling the occurences file
    filename = r'C:\CodeRepository\Thesis\Data\occurences_new.p'
    outfile = open(filename,'wb')
    pickle.dump(occurences,outfile)
    outfile.close

    return corpus_bigrams,corpus_tokens
