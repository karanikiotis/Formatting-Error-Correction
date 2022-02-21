import sys
sys.path.insert(0,'C:\CodeRepository\Formatting-Error-Correction')
from Scripts.tokenizer import tokenize

def find_err_token(filename,err_offset):

    f = open(r'C:\CodeRepository\Formatting-Error-Correction\Data\CodRep_Sample'+'\\'+filename,'r',encoding = 'utf-8')
    code = f.read()

    [tokens,lengths] = tokenize(code)

    tok_enum = {}
    for j,tok in enumerate(tokens):
        tok_enum.update([(j,tok)])

    len_sum = [1]
    for j in range(0,len(lengths)):
        len_sum.append(len_sum[j]+lengths[j])

    token_len = {}
    for j,leng in enumerate(len_sum):
        token_len.update([(j,leng)])

    for j in list(token_len.keys()):
        if(token_len[j] == err_offset):
            found_key = j

    return found_key