import javac_parser
import S7_Parameters as Params
from utils.Score_Detect_Functions import calcScore

# import debugpy
# debugpy.listen(5678)
# print('Debugging Session\n')
# debugpy.wait_for_client()

#Java parser
parser = javac_parser.Java()

def getFixes(code, lm, tokenIdxSorted, fixSorted, score):

    """
    Description:
        
    Inputs: 
        code (String): A string that represents the source code file for fixing its formation.
        lm (Nltk object): N-gram language model used to score each snippet.
        tokenIdxSorted (List of Integers) : A sorted list of token's starting indices. The sorting has been performed
            according to the modified propabilities of the LSTM model. So, the 1st element of the list is the starting index 
            of the token, with the highest propability of being a formation error.
        fixSorted (List of Lists): A sorted list of lists, every one of them contains a possible replacements for each one of the tokens.
            So, the 1st list contains possible replacements for the token which has the highest propability of being a formation error.
        score (Float): A float number that represents the score, regarding the formation, of the source code file.
    Outputs:
        fixedCode (String): A list with strings, each one of them represents fixed code with a different fix. The fixed code has passed Java 
            parsing test and also has lower-better score than the original one.
    """
    # Store all fixed codes that passed Java parsing test
    fixedCode = []
    # Store all scores of fixed codes that passed Java parsing test
    fixedScores = []
    #1. TRY DELETING THE CHARACTER
    for tokenID in tokenIdxSorted[:Params.numOfCheckedTok]:
        # Erase errorneous token
        newCode = code[:tokenID] + code[tokenID + 1:]
        # Check if new code is parsable
        if not (parser.get_num_parse_errors(newCode)): 
            # Check if new code does not belong to the fixes that we have already suggested
            if newCode not in fixedCode:
                # Check if new code has better score than the current one
                scoreFixed = calcScore(newCode, lm)
                if scoreFixed < score:
                    fixedCode.append(newCode)
                    fixedScores.append(scoreFixed)
                    print(f'    Case 1 -- Delete errorneous token: Fixed code passed the Java parsing test, with higher score {scoreFixed}.')
                else:
                    fixedScores.append(scoreFixed)
                    print(f'    Case 1 -- Delete errorneous token: Fixed code passed the Java parsing test, with lower score {scoreFixed}.')
        else:
            print(f'    Case 1 -- Delete errorneous token: Fixed code did not passed the Java parsing test.')

    
    #2. TRY REPLACING THAT CHARACTER WITH POSSIBLE REPLACEMENTS
    for i, c in enumerate(tokenIdxSorted[:Params.numOfCheckedTok]):
        # For each one of the possible errorneous tokens, try the possible replacements-fixes
        # The number of tokens that will be checked are defined through numOfCheckedTok
        # The number of replacements that will be used as possible fixes are defined trough numOfSuggFixes
        for possRepl in fixSorted[i]:
            # Newline should also try to indentate properly
            if possRepl == "\n":
                codeIdx = c
                tabspaces = ""
                spaceCounter = 0
                # Iterate until a new line token is detected
                while code[codeIdx] != "\n":
                    # Check if the current character is space. If it is, raise the counter by 1. Otherwirse, zero-out the counter
                    if code[codeIdx] == " ":
                        spaceCounter += 1
                    else:
                        spaceCounter = 0
                    # If four consecutive spaces are detected, indentate by 1 tab and zero out space counter
                    if spaceCounter == 4:
                        tabspaces += "    "
                        spaceCounter = 0
                    # Reduce index by in order to access the previous character
                    codeIdx -= 1

                newCode = code[:c] + possRepl + tabspaces + code[c + 1:]
            else:
                newCode = code[:c] + possRepl + code[c + 1:]

            # Check if new code is parsable
            if not (parser.get_num_parse_errors(newCode)):
                # Check if new code does not belong to the fixes that we have already suggested
                if newCode not in fixedCode:
                    scoreFixed = calcScore(newCode,lm)
                    # Check if new code has better score than the current one
                    if scoreFixed < score:
                        fixedScores.append(scoreFixed)
                        fixedCode.append(newCode)
                        print(f'    Case 2 -- Replace errorneous token: Fixed code passed the Java parsing test, with higher score {scoreFixed}.')
                    else:
                        fixedScores.append(scoreFixed)
                        print(f'    Case 2 -- Replace errorneous token: Fixed code passed the Java parsing test, with lower score {scoreFixed}.')
            else:
                print(f'    Case 2 -- Replace errorneous token: Fixed code did not passed the Java parsing test.')

    #3. TRY APPENDING POSSIBLE REPLACEMENTS BEFORE THAT CHARACTER
    for i, c in enumerate(tokenIdxSorted[:Params.numOfCheckedTok]):
        for apnd in fixSorted[i]:
            # Newline should also try to indentate properly
            if apnd == "\n":
                codeIdx = c
                tabspaces = ""
                spaceCounter = 0
                # Iterate until a new line token is detected
                while code[codeIdx] != "\n":
                    # Check if the current character is space. If it is, raise the counter by 1. Otherwirse, zero-out the counter
                    if code[codeIdx] == " ":
                        spaceCounter += 1
                    else:
                        spaceCounter = 0
                    # If four consecutive spaces are detected, indentate by 1 tab and zero out space counter
                    if spaceCounter == 4:
                        tabspaces += "    "
                        spaceCounter = 0
                    # Reduce index by in order to access the previous character
                    char -= 1
                newCode = code[:c] + apnd + tabspaces + code[c:]
            else:
                newCode = code[:c] + apnd + code[c:]

            # Check if new code is parsable and improved
            if not (parser.get_num_parse_errors(newCode)):
                if newCode not in fixedCode:
                    scoreFixed = calcScore(newCode,lm)
                    if scoreFixed < score:
                        fixedCode.append(newCode)
                        fixedScores.append(scoreFixed)
                        print(f'    Case 3 -- Append after errorneous token: Fixed code passed the Java parsing test, with higher score {scoreFixed}.')
                    else:
                        fixedScores.append(scoreFixed)
                        print(f'    Case 3 -- Append after errorneous token: Fixed code passed the Java parsing test, with lower score {scoreFixed}.')
            else:
                print(f'    Case 3 -- Append after errorneous token: Fixed code did not passed the Java parsing test.')

    return fixedCode