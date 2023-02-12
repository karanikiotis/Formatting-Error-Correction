import os
import pickle
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
import tensorflow as tf
import numpy as np
import sys
from datetime import datetime
sys.path.insert(0, '/Users/Shared/c/CodeRepository/Formatting-Error-Correction/')
import Scripts.S7_Parameters as Params
from utils.tokenizer import tokenize

import debugpy
debugpy.listen(5678)
print('Debugging Session\n')
debugpy.wait_for_client()

tf.random.set_seed(7)
model = tf.keras.Sequential()
model.add(tf.keras.layers.Embedding(len(Params.tokensAvailable), 32, input_length = 20))
model.add(tf.keras.layers.LSTM(300, input_shape = (20,32),return_sequences = True))
model.add(tf.keras.layers.Dropout(rate = 0.2))
model.add(tf.keras.layers.LSTM(300, input_shape = (20,300)))
model.add(tf.keras.layers.Dropout(rate = 0.2))
model.add(tf.keras.layers.Dense(41, activation = 'softmax', use_bias = True))
optimizer = tf.keras.optimizers.Adam(learning_rate = 0.001)
model.compile(loss = 'categorical_crossentropy', optimizer = 'adam', metrics = ['accuracy'])

earlystopping = tf.keras.callbacks.EarlyStopping(monitor = "val_loss", min_delta = 0.01, mode = "min", patience = 3, restore_best_weights = True)

d = dict((c, i) for i, c in enumerate(Params.tokensAvailable))
d_inv = dict((i, c) for i, c in enumerate(Params.tokensAvailable))

directory = '/Users/Shared/c/CodeRepository/Data/LSTM_TrainingDataset'
os.chdir(directory)

xData = []
yData = []

start_time = datetime.now()
print(f'\n\nStarting of corpus preprocessing:{start_time.strftime("%H:%M:%S")}\n\n')
for num, fileName in enumerate(sorted(os.listdir(directory))):
    print(f'Iteration {num} -- Processing file with name: {fileName}...\n')
    file = open(fileName, "r", encoding = "utf-8", errors = 'ignore')
    code = file.read()

    [tokens, _] = tokenize(code)
    
    # Append <start> and <end> tokens   
    tokens_enc = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0] \
                + [d[x] for x in tokens] \
                + [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]

    ngrams = []
    for i in range(len(tokens_enc)-19):
        ngrams.append(tokens_enc[i : i+20])

    # Calculation of index of the next token.
    # On each position of the Numpy array, we have the index of the token
    # that is going to appear after each 20-gram.
    idxOfNextToken = []
    for i in range(len(ngrams)-1):
        idxOfNextToken.append(ngrams[i+1][19])
    idxOfNextToken.append(1)

    if(num == 0):
        xData = ngrams
        yData = idxOfNextToken
    else:
        xData += ngrams
        yData += idxOfNextToken
end_time = datetime.now()
print(f'\n\nEnd of corpus preprocessing:{end_time.strftime("%H:%M:%S")}\n\n')

print(f'Starting the training of LSTM Network...\n')
xData = np.array(xData)
yData = np.array(yData)
yData = tf.keras.utils.to_categorical(yData, num_classes = len(Params.tokensAvailable))
history = model.fit(xData, yData, epochs = 10, validation_split = 0.25, verbose = 1, batch_size = 256, callbacks = [earlystopping])
model.save('/Users/Shared/c/CodeRepository/Formatting-Error-Correction/LSTM Model/LSTM_v4.h5')

with open('/Users/Shared/c/CodeRepository/Formatting-Error-Correction/LSTM Model/history_LSTM_v4.pkl', 'wb') as f:
    pickle.dump(history, f)
