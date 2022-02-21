CHARS = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_'
CHARS_NUM = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_0123456789'
NUMBERS = '0123456789'

SYMBOLS = ["\n","\t",".",",",";","!","@","#","$","%","^","&","*","(",")","-","+","=","[","]","{","}","<",">","?","\\"]
TOKENS = ["<eos>","<tab>","<dot>","<comma>","<semicolon>","<exclamation>","<at>","<hash>","<dollar>","<perc>","<caret>","<and>","<power>","<open_par>","<close_par>","<minus>","<plus>","<equal>","<open_bracket>","<close_bracket>","<open_curly>","<close_curly>","<less_than>","<greater_than>","<quest>","<back_slash>"]
MAPPING = {x: TOKENS[i] for (i, x) in enumerate(SYMBOLS)}

KEYWORDS = ["abstract","assert","break","case","catch","class","continue","default","do","else","enum","exports","extends","final","finally","for","if","implements","import","instanceof","interface","module","native","new","package","private","protected","public","requires","return","static","strictfp","super","switch","synchronized","this","throw","throws","transient","try","volatile","while"]
LIT = ["boolean","byte","char","double","float","int","long","short","void"]
LITERAL = ["true","false","null"]

# Find the index of the last character of a word
def define_word(code):
	
	pos = -1
	flag = 1
	while(flag==1):
		if(pos==len(code)-1):
			pos += 1 
			break
		pos += 1
		if(code[pos] not in CHARS_NUM):
			flag = 0
	if(pos==-1):
		return 0
	else:
		return pos

# Find the index of the last digit of a number
def define_number(code):

	pos = -1
	flag = 1
	while((pos<len(code)-1)and(flag==1)):
		pos += 1
		if(code[pos] not in NUMBERS):
			flag = 0
	if(pos==-1):
		return 0
	else:
		return pos

# Find the index of the next end of line (used for single line comments)
def find_end_of_line(code):

	pos = -1
	flag = 1
	while((pos<len(code)-1)and(flag==1)):
		pos += 1
		if(code[pos]=="\n"):
			flag = 0
	if(pos==-1):
		return 0
	else:
		return pos

# Find the index of the end of a comment block (Disclaimer: Comment block in Java is declared as : /* ... */)
def find_end_of_comment_block(code):

	pos = -1
	flag = 1
	while((pos<len(code)-1)and(flag!=3)):
		pos += 1
		if(code[pos]=="*"): 
			flag = 2
		elif((code[pos]=="/")and(flag==2)):
			flag = 3
		else:
			flag = 1
	if(pos==-1):
		return 0
	else:
		return pos

# Find the index of the last character of a string
def find_end_of_string(code):

	pos = -1
	flag = 1
	while((pos<len(code)-1)and(flag==1)):
		pos += 1
		if(code[pos]=="\""):
			flag = 0
			if(pos>=1):
				if(code[pos-1]=="\\"):
					flag = 1
	if(pos==-1):
		return 0
	else:
		return pos

# Find the end of a char statement
def find_end_of_char(code):

	pos = -1
	flag = 1
	while((pos<len(code)-1)and(flag==1)):
		pos += 1
		if(code[pos]=="'"):
			flag = 0
			if(pos>=1):
				if(code[pos-1]=="\\"):
					flag = 1
	if(pos==-1):
		return 0
	else:
		return pos

# Check if the found word is a keyword
def check_keywords(word):

	if(word in KEYWORDS):
		return "<keyword>"
	elif(word in LITERAL):
		return "<literal>"
	elif(word in LIT):
		return "<lit>"
	else:
		return None

def tokenize(code):

	tokens = []
	lengths = []
	pos = -1
	while(True):
		if(pos==len(code)-1): 
			break
		pos += 1
		if(code[pos] in CHARS):	# Next characters belong to a word
			offset = define_word(code[pos+1:]) # Offset means the distance between the first and the last character of the word
			token = check_keywords(code[pos:pos+offset+1])
			pos += offset
			if(token==None):
				tokens.append('<word>')
			else:
				tokens.append(token)
			lengths.append(offset+1)
		elif(code[pos]==" "):	# Next character is a space
			temp = '<space>'
			offset = 1
			if(pos<=len(code)-4):
				if((code[pos+1]==" ")and(code[pos+2]==" ")and(code[pos+3]==" ")):	# Next characters are a whole spacetab
					temp = '<spacetab>'
					offset = 4 # On this case, offset represents the whole length of a spacetab
					pos += 3
			tokens.append(temp)
			lengths.append(offset)
		elif(code[pos]=="/"):
			if(pos<len(code)-2):
				if(code[pos+1]=="/"):	# Next characters belong to a single line comment
					offset = find_end_of_line(code[pos+2:]) + 2
					pos += offset
					tokens.append('<eos>')
					lengths.append(offset+1)
				elif(code[pos+1]=="*"):	# Next characters belong to a comment block
					offset = find_end_of_comment_block(code[pos+2:]) + 2
					pos += offset
					tokens.append('<com>')	
					lengths.append(offset+1)
				else:	# Next character is a simple slash
					tokens.append('<slash>')
					lengths.append(1)
			else:
				tokens.append('<slash>')
				lengths.append(1)
		elif(code[pos]== '\"'):	# Next characters belong to a string
			offset = find_end_of_string(code[pos+1:]) + 1
			pos += offset
			tokens.append('<string>')
			lengths.append(offset+1)
		elif(code[pos]=="'"):	# Next characters belong to a character statement
			offset = find_end_of_char(code[pos+1:]) + 1
			pos += offset
			tokens.append('<char>')
			lengths.append(offset+1)
		elif(code[pos] in NUMBERS):	# Next characters belong to a number
			offset = define_number(code[pos+1:])
			pos += offset
			tokens.append('<number>')
			lengths.append(offset+1)
		elif(code[pos] in SYMBOLS):	# Next character is a symbol
			tokens.append(MAPPING[code[pos]])
			lengths.append(1)
		else:	# Unknown character
			tokens.append('<unk>')
			lengths.append(1)
			
	return tokens, lengths