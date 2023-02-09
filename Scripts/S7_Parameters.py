# A list of all the available tokens that a source code file can be consisted of. 
# We name the above list as vocabulary.
tokensAvailable = ["<start>","<end>","<eos>","<tab>","<spacetab>","<dot>","<comma>","<semicolon>","<colon>","<exclamation>",
                    "<at>","<hash>","<dollar>","<perc>","<caret>","<and>","<power>","<open_par>","<close_par>","<minus>",
                    "<plus>","<equal>","<open_bracket>","<close_bracket>","<open_curly>","<close_curly>","<less_than>",
                    "<greater_than>","<quest>","<back_slash>","<keyword>","<literal>","<lit>","<word>","<space>",
                    "<com>","<slash>","<string>","<char>","<number>","<unk>"]
# A list of all the replacements that can be a possible fix
replacements = ["","","\n","\t","    ",".",",",";",":","!","@","#","$", "%","^","&","*","(",")","-","+","=","[","]","{",
               "}","<",">","?","\\","for ","true ","int "," abc "," ","/* comment */","/"," abc123 ","a",
               "123","<unk>"]
# Number of suggested fixes
numOfSuggFixes = 3 
# Number of tokens that will be checked for the formating error
numOfCheckedTok = 3
# Represents the number of tokens that its code snippet will be consisted of
tokensPerSnippet = 20
# Step that will be used to produce N-grams. It is used during tokens scoring in order to reduce total calculation time
ngramStep = 3
# Dictionary that maps tokens to tokens ID
tokensMapping = dict((c, i) for i, c in enumerate(tokensAvailable))
# Main project path
path = r'/Users/Shared/c/CodeRepository/Formatting-Error-Correction/'
# Path that fixed source code files are saved
fixDir = "/Users/Shared/c/CodeRepository/Formatting-Error-Correction/Fixes/"