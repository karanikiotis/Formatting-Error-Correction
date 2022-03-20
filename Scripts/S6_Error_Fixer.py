import javac_parser
from S4_Score_Detect import get_score

#Java parser
parser = javac_parser.Java()

#######################
# Try possible fixes #
######################

# Trying the fixes on/adjacent to
# 5 most probable erroneous characters

def get_fixes(code, chars_sorted, fixes_sorted, score):
    acc_codes = []  # Store all 'fixed' codes that passed parse test

    #1. TRY DELETING THE CHARACTER
    for c in chars_sorted[:5]:
        new_code = code[:c-1] + code[c:]
    
        # Check if new code is parsable and improved
        if not (parser.get_num_parse_errors(new_code)): 
            if new_code not in acc_codes:
                score_new = get_score(new_code)[2]
                if score_new < score:
                    acc_codes.append(new_code)
        else:
            print(f'Fixed code(1) did not passed the Java parsing test.')

    #2. TRY REPLACING THAT CHARACTER WITH POSSIBLE REPLACEMENTS
    for i, c in enumerate(chars_sorted[:5]):
        for repl in fixes_sorted[i]:    
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
                    score_new = get_score(new_code)[2]
                    if score_new < score:
                        acc_codes.append(new_code)
            else:
                print(f'Fixed code(2) did not passed the Java parsing test.')

    #3. TRY APPENDING POSSIBLE REPLACEMENTS BEFORE THAT CHARACTER
    for i, c in enumerate(chars_sorted[:5]):
        for apnd in fixes_sorted[i]:
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
                    score_new = get_score(new_code)[2]
                    if score_new < score:
                        acc_codes.append(new_code)
            else:
                print(f'Fixed code(3) did not passed the Java parsing test.')

    return acc_codes