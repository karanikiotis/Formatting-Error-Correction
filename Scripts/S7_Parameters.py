tokensAvailable = ["<start>","<end>","<eos>","<tab>","<spacetab>","<dot>","<comma>","<semicolon>","<colon>","<exclamation>",
                    "<at>","<hash>","<dollar>","<perc>","<caret>","<and>","<power>","<open_par>","<close_par>","<minus>",
                    "<plus>","<equal>","<open_bracket>","<close_bracket>","<open_curly>","<close_curly>","<less_than>",
                    "<greater_than>","<quest>","<back_slash>","<keyword>","<literal>","<lit>","<word>","<space>",
                    "<com>","<slash>","<string>","<char>","<number>","<unk>"]

replacements = ["","","\n","\t","    ",".",",",";",":","!","@","#","$", "%","^","&","*","(",")","-","+","=","[","]","{",
               "}","<",">","?","\\","for ","true ","int "," abc "," ","/* comment */","/"," abc123 ","a",
               "123","<unk>"]

numOfSuggFixes = 3 

# TOK_PER_SNIP represents the number of tokens that its code snippet will be consisted of
TOK_PER_SNIP = 20

tokensMapping = dict((c, i) for i, c in enumerate(tokensAvailable))

fix_dir = "/mnt/c/CodeRepository/Formatting-Error-Correction/Fixes"