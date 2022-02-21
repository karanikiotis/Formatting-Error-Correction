import numpy as np
import pickle
import math
import sys

from utils.helper_func import truncate
from Scripts.tokenizer import tokenize
from nltk.util import everygrams, ngrams
from nltk.lm.preprocessing import pad_both_ends 


tokens_available = ["<start>","<end>","<eos>","<tab>","<spacetab>","<dot>","<comma>","<semicolon>","<colon>","<exclamation>",
                    "<at>","<hash>","<dollar>","<perc>","<caret>","<and>","<power>","<open_par>","<close_par>","<minus>",
                    "<plus>","<equal>","<open_bracket>","<close_bracket>","<open_curly>","<close_curly>","<less_than>",
                    "<greater_than>","<quest>","<back_slash>","<keyword>","<literal>","<lit>","<word>","<space>",
                    "<com>","<slash>","<string>","<char>","<number>","<unk>"]

d = dict((c, i) for i, c in enumerate(tokens_available))

#Loading 10-Gram Language Model used for scoring
with open(r"C:\CodeRepository\Formatting-Error-Correction\10-Gram Model\\10_gram_model_v2.p", "rb") as fp:
    lm = pickle.load(fp)

def calc_score(snip): 

    snip_transf = []
    for i in snip:
        snip_transf.append(tokens_available[i])
        
    snip_padded = list(pad_both_ends(snip_transf,n=10))
    snip_final = list(ngrams(snip_padded,10))
   
    score = lm.entropy(snip_final)

    return score

def get_score(code):
    TOK_PER_SNIP = 20 #We break the code into 20-token snippets

    [tokens,lengths] = tokenize(code)
    tokens = [d[x] for x in tokens]

    file_score = calc_score(tokens)

    snippets = list(ngrams(tokens,TOK_PER_SNIP))

    snips_scores = [] #score of each snippet according to calc_score
    for snip in snippets:
        score = calc_score(snip)    
        snips_scores.append(score)

    for j,s in enumerate(snips_scores):
        if(s == float('inf')): #Checking the case if an inf value exists in snips_scores list
            snips_scores[j] = 0
    for j,s in enumerate(snips_scores):
        if(s == 0):
            snips_scores[j] = max(snips_scores) #if yes, set inf value to be equal with the max value of snips_scores list

    snip_enc = [] #Encoding of snippets. So the first snippet is represented by zero, the second snippet is represented by one etc 
    for j,_ in enumerate(snippets):
        snip_enc.append(j)
    #print(snip_enc)

    code_tok_enc = []#Encoding of tokens. So the first token of the file is represented by zero, the second is represented by one etc
    for j,_ in enumerate(tokens):
        code_tok_enc.append(j)
    #print(code_tok_enc)

    snip_scores_enc = {}#Pairing of each encoded snippet with the its corresponding score 
    for j in zip(snip_enc,snips_scores):
        snip_scores_enc.update([j])
    #print(snip_scores_enc)

    snip_grams_enc = list(ngrams(code_tok_enc,TOK_PER_SNIP))
    #print(snip_grams_enc)

    num_token_occur = {}#number of each token's occurences 
    for snip in snip_grams_enc:
        for j in snip:
            if(j in list(num_token_occur.keys())):
                num_token_occur[j]+=1
            else:
                num_token_occur.update([(j,1)])
    #print(num_token_occur)

    token_occur = {}#key---> each token encoded , value---> a list tha consists of the windows(20-token grams) that this token is part of
    for j,snip in enumerate(snip_grams_enc):
        for k in snip:
            if(k in list(token_occur.keys())):
                token_occur[k].append(j)
            else:
                token_occur.update([(k,[j])])
    #print(token_occur)

    score_per_token = {} #key---> each token encode, value---> score(entropy) of this token
    for key in list(token_occur.keys()):
        curr_tok_score = 0
        for snip in token_occur[key]:
            curr_tok_score += snip_scores_enc[snip]
        score = curr_tok_score/len(token_occur[key])
        score = truncate(score,2)
        score_per_token.update([(key,score)])
    #print(score_per_token)

    return score_per_token,snips_scores,file_score


def error_detection(code,thresh): #Returns the tokens with higher score than the THRESHOLD
    THRESHOLD = thresh

    poss_err_tokens = {}
    s_tokens,_,_ = get_score(code)
    for key in list(s_tokens.keys()):
        if(s_tokens.get(key,None) >= THRESHOLD):
            poss_err_tokens.update([(key,s_tokens.get(key,None))])

    [tokens,lengths] = tokenize(code)

    len_sum = [1]
    for j in range(0,len(lengths)):
        len_sum.append(len_sum[j]+lengths[j])

    token_len = {}
    for j,leng in enumerate(len_sum):
        token_len.update([(j,leng)])

    err_tokens_info = []
    for key in list(poss_err_tokens.keys()):
        for k in list(token_len.keys()):
            if(key == k):
                err_tokens_info.append((key,tokens[key],token_len.get(key,None),s_tokens.get(key,None)))
    return err_tokens_info


def error_detection_v2(code,topn): #Returns the top 10 tokens with the highest score
    n = topn

    s_tokens,_,_ = get_score(code)
    sort_s_tokens = sorted(s_tokens.items(),key = lambda x:x[1],reverse = True)
    sort_s_tokens = dict(sort_s_tokens)

    i = 1
    poss_err_tokens = {}
    for key in list(sort_s_tokens.keys()):
        if(i<= n):
            poss_err_tokens.update([(key,sort_s_tokens.get(key,None))])
            i+=1
        else:
            break

    [tokens,lengths] = tokenize(code)

    len_sum = [1]
    for j in range(0,len(lengths)):
        len_sum.append(len_sum[j]+lengths[j])

    token_len = {}
    for j,leng in enumerate(len_sum):
        token_len.update([(j,leng)])

    err_tokens_info = []
    for key in list(poss_err_tokens.keys()):
        for k in list(token_len.keys()):
            if(key == k):
                err_tokens_info.append((key,tokens[key],token_len.get(key,None),poss_err_tokens.get(key,None)))
    
    return err_tokens_info
