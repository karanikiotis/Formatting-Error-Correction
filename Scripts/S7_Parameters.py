#   
tokensAvailable = ["<start>","<end>","<eos>","<tab>","<spacetab>","<dot>","<comma>","<semicolon>","<colon>","<exclamation>",
                    "<at>","<hash>","<dollar>","<perc>","<caret>","<and>","<power>","<open_par>","<close_par>","<minus>",
                    "<plus>","<equal>","<open_bracket>","<close_bracket>","<open_curly>","<close_curly>","<less_than>",
                    "<greater_than>","<quest>","<back_slash>","<keyword>","<literal>","<lit>","<word>","<space>",
                    "<com>","<slash>","<string>","<char>","<number>","<unk>"]
#
replacements = ["","","\n","\t","    ",".",",",";",":","!","@","#","$", "%","^","&","*","(",")","-","+","=","[","]","{",
               "}","<",">","?","\\","for ","true ","int "," abc "," ","/* comment */","/"," abc123 ","a",
               "123","<unk>"]

# Number of suggested fixes
numOfSuggFixes = 3 
# Number of tokens that will be checked for the formating error
numOfCheckedTok = 3
# Represents the number of tokens that its code snippet will be consisted of
TOK_PER_SNIP = 20
# Dictionary that maps tokens to tokens ID
tokensMapping = dict((c, i) for i, c in enumerate(tokensAvailable))
# Main project path
path = r'/Users/Shared/c/CodeRepository/Formatting-Error-Correction/'
# Path that fixed source code files are saved
fix_dir = "/Users/Shared/c/CodeRepository/Formatting-Error-Correction/Fixes/"