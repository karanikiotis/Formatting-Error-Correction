import sys
import datetime
sys.path.insert(0,'C:\CodeRepository\Formatting-Error-Correction')
from Scripts.S4_Score_Detect import get_score,calc_score,error_detection,error_detection_v2

file = open(sys.argv[1], "r", encoding = "utf-8")
code = file.read()

score_threshold = 5

start_time = datetime.datetime.now()
info = error_detection(code,score_threshold)
#info = error_detection_v2(code,10)
print(f'{info}\n\n')
print(f'Possible Errors:{len(info)}\n\n')
print(f'Total Time:{((datetime.datetime.now()-start_time).total_seconds())/60} mins\n\n')
score_per_token,_,file_score = get_score(code)
print(f'Score:{file_score}\n\n')
