# A list of all the available tokens that a source code file can be consisted of. 
# We name the above list as vocabulary.
tokensAvailable = ["<start>","<end>","<eos>","<tab>","<spacetab>","<dot>","<comma>","<semicolon>","<colon>","<exclamation>",
                    "<at>","<hash>","<dollar>","<perc>","<caret>","<and>","<power>","<open_par>","<close_par>","<minus>",
                    "<plus>","<equal>","<open_bracket>","<close_bracket>","<open_curly>","<close_curly>","<less_than>",
                    "<greater_than>","<quest>","<back_slash>","<keyword>","<literal>","<lit>","<word>","<space>",
                    "<com>","<slash>","<string>","<char>","<number>","<unk>"]

# A list of all the replacements that can be a possible fix.
replacements = ["","","\n","\t","    ",".",",",";",":","!","@","#","$", "%","^","&","*","(",")","-","+","=","[","]","{",
               "}","<",">","?","\\","for ","true ","int "," abc "," ","/* comment */","/"," abc123 ","a",
               "123","<unk>"]

# Dictionary that maps tokens to tokens ID.
tokensMapping = dict((c, i) for i, c in enumerate(tokensAvailable))

# Number of suggested fixes.
numOfSuggFixes = 3

# Number of tokens that will be taken into consideration as detected positions for possible formating error.
numOfCheckedTok = 5

# Represents the number of tokens that its code snippet will be consisted of.
tokensPerSnippet = 20

# Step that will be used to produce N-grams. It is used during tokens scoring in order to reduce total calculation time.
ngramStep = 3

# Main project path.
path = r'/Users/Shared/c/CodeRepository/Formatting-Error-Correction/'

# Path that fixed source code files are saved.
fixDir = "/Users/Shared/c/CodeRepository/Formatting-Error-Correction/Fixes/"

# Configured tabspace, used during fixing.
tabSpace = '    '

# Probability Threshold. Only tokens with error probabilities above this threshold are taken into consideration for fixing
probThresh = 0.75

# Return to the user only the fixed file with the best score.
returnBestScoreFile = False

# This parameter defines whether we are going to filter possible error positions according to the probability threshold
# or by choosing the numofCheckedTok first tokens.
probThreshActive = False