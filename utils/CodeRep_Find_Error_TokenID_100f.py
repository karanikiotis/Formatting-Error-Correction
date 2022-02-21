#****Finding the ID of the Errorneous Token for all 100 Java files of CodRep_Sample****
import sys
import os
sys.path.insert(0,'C:\CodeRepository\Formatting-Error-Correction')
from Scripts.tokenizer import tokenize

f = open(r'C:\\CodeRepository\\Formatting-Error-Correction\\Data\\out.txt','r',encoding = 'utf-8')
err_offsets = []
for i in list(f.readlines()):
    if(i != '\n'):
        err_offsets.append(int(i))
#print(err_offsets)

folder_path = r'C:\\CodeRepository\\Formatting-Error-Correction\\Data\\CodRep_Sample' 
os.chdir(folder_path)

names_ordered = []
for k,name in enumerate(os.listdir()):
    names_ordered.append(int(name.split('.')[0]))
names_ordered.sort()

for k,name_split in enumerate(names_ordered):
    names_ordered[k] = str(name_split) + '.txt' 
#print(names_ordered)

for i,file in enumerate(names_ordered):
    f = open(r'C:\CodeRepository\Formatting-Error-Correction\Data\CodRep_Sample'+'\\'+file,'r',encoding = 'utf-8')
    code = f.read()

    [tokens,lengths] = tokenize(code)
        
    tok_enum = {}
    for j,tok in enumerate(tokens):
        tok_enum.update([(j,tok)])

    len_sum = [1]
    for j in range(0,len(lengths)):
        len_sum.append(len_sum[j]+lengths[j]  )

    token_len = {}
    for j,leng in enumerate(len_sum):
        token_len.update([(j,leng)])
    #print(token_len)

    for j in list(token_len.keys()):
        if(token_len[j] == err_offsets[i]):
            found_key = j
    print(f'File:{i}, Errorneous_Token_Key:{found_key}, Total file length:{len_sum[-1]}')
