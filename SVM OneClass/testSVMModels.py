def testModels(models, maxConf, data):
	total = 0
	tp = 0
	tn = 0
	fp = 0
	fn = 0
	for i in range(len(data)):
		conf = []
		for j in range(len(models)):
			temp_conf = models[j].decision_function([data[i]])[0]
			temp_conf /= maxConf[j]
			if(temp_conf > 1):
				temp_conf = 1
			if(temp_conf < -1):
				temp_conf = -1
			conf.append(temp_conf)
		if(sum(conf) >= 0):
			tp += 1
		else:
			fn += 1
		total += 1
	print('Total: ', total)
	print('True Positive: ', tp)
	print('True Negative:  ', tn)
	print('False Positive: ', fp)
	print('False Negative: ', fn)
