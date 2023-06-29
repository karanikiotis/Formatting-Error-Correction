import pickle
from itertools import chain
from collections import Counter

#UNIQUE BIGRAMS OCCURENCES
def count_unique_occur(corpus_b):
  unique_bigram_per_file = [] #this list will contain the unique bigrams for each code snippet. So in the end, the i-th element of it will be a list that contains the unique bigrams(as tuples) of the
                            # i-th code snippet(simplified: a list of lists that each of them contains tuples)
  for b in corpus_b:
    num_of_b_appear = Counter(b)
    unique_bigram_per_file.append(list(num_of_b_appear.keys()))

  unique_occurences_per_bigram = dict( Counter(chain(*unique_bigram_per_file)))
  unique_occurences_per_bigram = dict(sorted(unique_occurences_per_bigram.items()))
  #print(f'\n\n****Unique occurences of each bigram on corpus:**** {unique_occurences_per_bigram}\n')

  return unique_occurences_per_bigram