import matplotlib.pyplot as plt
import numpy as np

plt.style.use('seaborn')

THR = 10000
x = np.linspace(0,100000,num = 10000) #Minimum of counts

score = []
for i in range(len(x)):
        score_temp = 1 - (np.abs((x[i]-THR))/(max(x[i],THR)))
        score.append(score_temp)

plt.figure()
plt.plot(x,score)
plt.plot([10000,10000],[0,1],c = 'g')
plt.xlabel('Minimum of Counts')
plt.ylabel('Score')
plt.legend(['Score','THR'])
plt.title('Min(counts) of Snippet -- Score of Snippet')

plt.show()



