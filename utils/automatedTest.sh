#!/bin/bash

cd /mnt/c/Users/Thomas/Desktop/Data/CodRep_Sample_537_850

for file in *.txt;
do
    echo -------------------------------------------------
    echo *****Start of preprorocessing file $file******
    python3 /mnt/c/CodeRepository/Formatting-Error-Correction/Scripts/S5_LSTM-fix.py $file  > ../Logs_100_850/$file
    echo *****End of preprorocessing file $file******
    echo -------------------------------------------------
done