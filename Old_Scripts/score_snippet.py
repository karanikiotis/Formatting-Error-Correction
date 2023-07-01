import numpy as np
from tokenizer import tokenize
import pickle

tokens_available = ["<start>","<end>","<eos>","<tab>","<spacetab>","<dot>","<comma>","<semicolon>","<colon>","<exclamation>",
                    "<at>","<hash>","<dollar>","<perc>","<caret>","<and>","<power>","<open_par>","<close_par>","<minus>",
                    "<plus>","<equal>","<open_bracket>","<close_bracket>","<open_curly>","<close_curly>","<less_than>",
                    "<greater_than>","<quest>","<back_slash>","<keyword>","<literal>","<lit>","<word>","<space>",
                    "<com>","<slash>","<string>","<char>","<number>","<unk>"]

d = dict((c, i) for i, c in enumerate(tokens_available))

#Score calculation of a code snippet
def calc_score(snip): 
    THR = 10000 

    with open("..\\Data\\occurences.p", "rb") as fp:
        all_counts = pickle.load(fp)

    bigrams = []

    # Snippet last in file
    if len(snip) == 1:
        snip.append(1)

    #Bigrams formation
    for i in range(len(snip) - 1):
        bigrams.append(snip[i: i + 2])

    counts = [] #is used to store the number of appearances for every possible bigram in the current code snippet according to
                #occurences.p file
    for b in bigrams:
        if tuple(b) in all_counts: #if current bigram exists in all_counts
            counts.append( all_counts.get(tuple(b)) )
        else:
            counts.append(0)

    # score = 0
    # for count in counts:
    #    score += 1.0 + (count - THR) / max(count, THR)
    # score /= len(counts)

    score = 1.0 - abs(min(counts) - THR) / max(min(counts), THR)
    return score


def get_score(code):
    TOK_PER_SNIP = 20 #We break the code into 20-token snippets

    snippets = [] #20-token snippets
    snip_lengths = [0] #length of each snippet
    scores = [] #score of each snippet according to calc_score

    edge_tokens, edge_pos = [], []  # Token pairs that are adjacent to the slice
    edge_scores = [] 

    [tokens, lengths] = tokenize(code)
    tokens = [d[x] for x in tokens]

    # Create snippets of TOK_PER_SNIP tokens each
    for i in range(0, len(tokens), TOK_PER_SNIP):
        snippets.append( tokens[ i:i + (min(TOK_PER_SNIP, len(tokens) - i)) ] )

    # Calculate each snippet's length
    pos = 0
    for i, snip in enumerate(snippets):
        pos += sum(lengths[i * TOK_PER_SNIP: (i * TOK_PER_SNIP ) + len(snip)]) 
        snip_lengths.append(pos)

    lpos = 0
    for i in range(len(snippets)):
        if i != 0:
            tpair = [ snippets[i - 1][len(snippets[i - 1]) - 1], snippets[i][0] ] #last token of the previous snippet combined
            edge_tokens.append(tpair)                                            #with the first token of the current snippet   
        lpos += sum(lengths[i * TOK_PER_SNIP: (i * TOK_PER_SNIP) + len(snippets[i])]) #left position
        if i < len(snippets) - 1:
            rpos = lpos + lengths[(i * TOK_PER_SNIP) + len(snippets[i]) - 1] #right position
            edge_pos.extend((lpos, rpos))

    # Get each snippet's score
    for snip in snippets:
        scores.append(calc_score(snip))

    # Get score for neighboring bigrams at snippets' edges
    for pair in edge_tokens:
        edge_scores.append(calc_score(pair))
        
    return scores, snip_lengths, edge_pos, edge_scores,edge_tokens,snippets