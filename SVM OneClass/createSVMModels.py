import os
from sklearn import svm
import pickle
from joblib import dump
from dotenv import load_dotenv
load_dotenv()

projectPath = os.getenv("PROJECT_PATH")

def frange(start, stop, step):
    i = start
    while(i < stop):
        yield i
        i += step

def createModels(data):
	nuStart = float(os.getenv("SVM_NU_START"))
	nuEnd = float(os.getenv("SVM_NU_END"))
	nuStep = float(os.getenv("SVM_NU_STEP"))
	gammaStart = float(os.getenv("SVM_GAMMA_START"))
	gammaEnd = float(os.getenv("SVM_GAMMA_END"))
	gammaStep = float(os.getenv("SVM_GAMMA_STEP"))

	nus = []
	gammas = []
	for i in frange(nuStart, nuEnd, nuStep):
		nus.append(i)
	for i in frange(gammaStart, gammaEnd, gammaStep):
		gammas.append(i)

	clf = []
	for nu in nus:
		for gamma in gammas:
			clf.append(svm.OneClassSVM(nu=nu, kernel="rbf", gamma=gamma))

	maxConf = []
	for i in range(len(clf)):
		clf[i].fit(data)
		maxConf.append(0)

	for i in range(len(data)):
		for j in range(len(clf)):
			c = clf[j].decision_function([data[i]])[0]
			if(c>maxConf[j]):
				maxConf[j] = c

	with open(os.path.join(projectPath, "Data", "maxConf.pkl"), "wb") as f:
		pickle.dump(maxConf, f)

	for i in range(len(clf)):
		with open(os.path.join(projectPath, "Data", "svmModels", "model_" + str(i + 1) + ".joblib"), "wb") as f:
			dump(clf[i], f)

	return clf, maxConf
