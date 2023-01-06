import javac_parser
import pdb
from S4_Score_Detect import ngramScore
from utils.Score_Detect_Functions import calcScore

#Java parser
parser = javac_parser.Java()

#######################
# Try possible fixes #
######################

# Trying the fixes on/adjacent to
# 5 most probable erroneous characters

def getFixes(code, lm, tokenIdxSorted, fixSorted, score):

    """
    Description:
        
    Inputs: 
        code (String): A string that represents the source code file for fixing its formation.
        lm (): 
        tokenIdxSorted (List of Integers) : A sorted list of tokens' starting indices of the source code file. The sorting has been performed
            according to the modified, the modification was done using the scoring mechanism, propabilities of the LSTM model.
            So, the 1st element of the list is the token ID with the highest propability of being a formation error.
        fixSorted (List of Lists):
        score (Float): A float number that represents the score, regarding the formation, of the source code file.
    Outputs:
        acc_codes ()
    """

    acc_codes = []  # Store all 'fixed' codes that passed parse test

    #1. TRY DELETING THE CHARACTER
    for tokenID in tokenIdxSorted[:3]:
        #pdb.set_trace()
        new_code = code[:tokenID-1] + code[tokenID:]
        # Check if new code is parsable and improved
        if not (parser.get_num_parse_errors(new_code)): 
            if new_code not in acc_codes:
                score_new = calcScore(new_code, lm)
                if score_new < score:
                    acc_codes.append(new_code)
        else:
            print(f'Fixed code(1) did not passed the Java parsing test.')


    #2. TRY REPLACING THAT CHARACTER WITH POSSIBLE REPLACEMENTS
    for i, c in enumerate(tokenIdxSorted[:3]):
        for repl in fixSorted[i]:    
            # Newline should also try to indentate properly
            if repl == "\n":
                char = c
                tabspaces = ""
                tmp = 0
                while code[char] != "\n":
                    if code[char] == " ":
                        tmp += 1
                    else:
                        tmp = 0

                    if tmp == 4:
                        tabspaces += "    "
                        tmp = 0
                    char -= 1

                new_code = code[:c - 1] + repl + tabspaces + code[c:]
            else:
                new_code = code[:c - 1] + repl + code[c:]

            # Check if new code is parsable and improved
            if not (parser.get_num_parse_errors(new_code)):
                if new_code not in acc_codes:
                    score_new = calcScore(new_code,lm)
                    if score_new < score:
                        acc_codes.append(new_code)
            else:
                print(f'Fixed code(2) did not passed the Java parsing test.')

    #3. TRY APPENDING POSSIBLE REPLACEMENTS BEFORE THAT CHARACTER
    for i, c in enumerate(tokenIdxSorted[:3]):
        for apnd in fixSorted[i]:
            # Newline should also try to indentate properly
            if apnd == "\n":
                char = c
                tabspaces = ""
                tmp = 0

                while code[char] != "\n":
                    if code[char] == " ":
                        tmp += 1
                    else:
                        tmp = 0

                    if tmp == 4:
                        tabspaces += "    "
                        tmp = 0
                    char -= 1
                new_code = code[:c - 1] + apnd + tabspaces + code[c - 1:]
            else:
                new_code = code[:c - 1] + apnd + code[c - 1:]

            # Check if new code is parsable and improved
            if not (parser.get_num_parse_errors(new_code)):
                if new_code not in acc_codes:
                    score_new = calcScore(new_code,lm)
                    if score_new < score:
                        acc_codes.append(new_code)
            else:
                print(f'Fixed code(3) did not passed the Java parsing test.')

    return acc_codes


def removeChar(code, tokenIdxSorted, score):

    """
    Description: A function that tries to fix formattion error by removing the token of the source code file 
        that are highly possible to be a formattion error.
        
    Inputs: 
        code ():
        charsSorted () :
        score ()
    Outputs:
        acc_codes ()
    """

    # For the first five tokens which are the most possible to be a formatting error:
    for c in tokenIdxSorted[:5]:
        pdb.set_trace()
        # Remove the corresponding token and form the new source code 
        new_code = code[:c-1] + code[c:]
        # Check if the newsource code is parsable using Java Parser
        if not (parser.get_num_parse_errors(new_code)): 
            # Check if the new code is a fix that has been already performed
            if new_code not in acc_codes:
                # Score new source code, regarding its formattion, using the implemented scoring mechanism
                score_new = calcScore(new_code, lm)
                # Check if the new source code has a better score than the old one
                if score_new < score:
                    # If yes, add new source code as a possible fix
                    acc_codes.append(new_code)
        else:
            print(f'Fixed code(1) did not passed the Java parsing test.')
    return acc_codes