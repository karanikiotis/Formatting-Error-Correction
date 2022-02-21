import sys
sys.path.insert(0,'C:\CodeRepository\Formatting-Error-Correction')
import os

from nltk.util import ngrams
from Scripts.S4_Score_Detect import get_score,calc_score
from utils.helper_func import truncate
from Scripts.tokenizer import tokenize
from utils.CodeRep_Find_Err_Token import find_err_token
from datetime import datetime

tokens_available = ["</s>","</s>","<eos>","<tab>","<spacetab>","<dot>","<comma>","<semicolon>","<colon>","<exclamation>",
                    "<at>","<hash>","<dollar>","<perc>","<caret>","<and>","<power>","<open_par>","<close_par>","<minus>",
                    "<plus>","<equal>","<open_bracket>","<close_bracket>","<open_curly>","<close_curly>","<less_than>",
                    "<greater_than>","<quest>","<back_slash>","<keyword>","<literal>","<lit>","<word>","<space>",
                    "<com>","<slash>","<string>","<char>","<number>","<unk>"]

d = dict((c, i) for i, c in enumerate(tokens_available))

f = open(r'C:\\CodeRepository\\Formatting-Error-Correction\\Data\\out.txt','r',encoding = 'utf-8')
err_offsets = []
for i in list(f.readlines()):
    if(i != '\n'):
        err_offsets.append(int(i))

folder_path = r'C:\\CodeRepository\\Formatting-Error-Correction\\Data\\CodRep_Sample' 
os.chdir(folder_path)

names_ordered = []
for k,name in enumerate(os.listdir()):
    names_ordered.append(int(name.split('.')[0]))
names_ordered.sort()

for k,name_split in enumerate(names_ordered):
    names_ordered[k] = str(name_split) +'.txt' 

TOK_PER_SNIP = 20
err_tokens_scores = []

for i,file in enumerate(names_ordered):
    print(f'File: {i}, Time: {datetime.now()}...')

    err_tok_key = find_err_token(file,err_offsets[i])

    #Reading each Java code file from CodRep Repository
    f = open(r'C:\CodeRepository\Formatting-Error-Correction\Data\CodRep_Sample'+'\\'+file, "r", encoding = 'utf-8')
    code = f.read()

    [tokens,length] = tokenize(code)
    tokens = [d[x] for x in tokens]

    snippets = list(ngrams(tokens,TOK_PER_SNIP))

    scores = []
    for snip in snippets:
        score = calc_score(snip)    
        scores.append(score)

    for j,s in enumerate(scores):
        if(s == float('inf')):
            scores[j] = 0

    for j,s in enumerate(scores):
        if(s == 0):
            scores[j] = max(scores)

    snip_enc = []
    code_tok_enc = []
    for j,_ in enumerate(snippets):
        snip_enc.append(j)
    #print(snip_enc)

    for j,_ in enumerate(tokens):
        code_tok_enc.append(j)
    #print(code_tok_enc)

    snip_scores_enc = {}
    for j in zip(snip_enc,scores):
        snip_scores_enc.update([j])
    #print(snip_scores_enc)

    snip_grams_enc = list(ngrams(code_tok_enc,TOK_PER_SNIP))
    #print(snip_grams_enc)

    num_token_occur = {}
    for snip in snip_grams_enc:
        for j in snip:
            if(j in list(num_token_occur.keys())):
                num_token_occur[j]+=1
            else:
                num_token_occur.update([(j,1)])
    #print(num_token_occur)

    token_occur = {}
    for j,snip in enumerate(snip_grams_enc):
        for k in snip:
            if(k in list(token_occur.keys())):
                token_occur[k].append(j)
            else:
                token_occur.update([(k,[j])])
    #print(token_occur)

    score_per_token = {}
    for key in list(token_occur.keys()):
        curr_tok_score = 0
        for snip in token_occur[key]:
            curr_tok_score += snip_scores_enc[snip]
        score = curr_tok_score/len(token_occur[key])
        score = truncate(score,2)
        score_per_token.update([(key,score)])
    #print(score_per_token)

    is_max = False
    if(score_per_token.get(err_tok_key,None) == max(list(score_per_token.values()))):
        is_max = True 
    #if(score_per_token.get(err_tok_key,None) in sorted(list(score_per_token.values()))[-20:]):
        #is_max = True

    err_tokens_scores.append(score_per_token.get(err_tok_key,None))

    print(f'-----ID of errorneous Token:-------{err_tok_key}')
    print(f'-----Score of errorneous Token:-------{score_per_token.get(err_tok_key,None)}')
    print(f'-----Is errorneous tokens\'s score in top 20 scores?------ {is_max}')
    #print(f'-------Scores of rest of Tokens:-------{score_per_token}\n\n\n\n')

print(f'-----Scores of ALL Errorneous Tokens of the first 100 Java files of CodRep:-----{err_tokens_scores}')