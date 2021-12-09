import sys
sys.path.insert(0,'C:\CodeRepository\Thesis\Predict-Fix\Scripts')
from S1_corpus_bigrams_occurences import count_occur
from S2_corpus_bigrams_unique_occurences import count_unique_occur
from collections import Counter
from Starting_Scripts.tokenizer import tokenize
import pickle

corpus_b = count_occur()
count_unique_occur(corpus_b)

#Test 1: Pickle File
#with open(r'/mnt/c/CodeRepository/Thesis/Data/occurences_new.p', 'rb') as fp:
  #all_counts = pickle.load(fp)
  #print(f'All_counts: {all_counts}\n')
  #print(f'Total number of bigrams: {len(all_counts.keys())}\n')

#Test 2: Bigrams occurences
#tokens_available = ["<start>","<end>","<eos>","<tab>","<spacetab>","<dot>","<comma>","<semicolon>","<colon>","<exclamation>",
 #                   "<at>","<hash>","<dollar>","<perc>","<caret>","<and>","<power>","<open_par>","<close_par>","<minus>",
  #                  "<plus>","<equal>","<open_bracket>","<close_bracket>","<open_curly>","<close_curly>","<less_than>",
   #                 "<greater_than>","<quest>","<back_slash>","<keyword>","<literal>","<lit>","<word>","<space>",
    #                "<com>","<slash>","<string>","<char>","<number>","<unk>"]

#d = dict((c, i) for i, c in enumerate(tokens_available))

#f = open(sys.argv[1], "r", encoding = "utf-8")
#code = f.read()

#[tokens,length] = tokenize(code)
#tokens_enc = [0]+[d[x] for x in tokens]+[1]
#print(f'{tokens_enc}\n')

#bigrams = []
#for i in range(0,len(tokens_enc)-1):
 # bigrams.append(tuple(tokens_enc[i:i+2]))
#print(f'{bigrams}\n')

#occurences = dict(Counter(bigrams))
#occurences = dict(sorted(occurences.items()))
#print(f'{occurences}')