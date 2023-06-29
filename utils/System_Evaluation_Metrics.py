def precision_at_k(actErrorPositions, detectErrorPositions, k, threshold = None):
    """
    Calculates precision at k for a list of files with just one error, optionally considering only detected error positions with a probability greater than a specified threshold.

    Parameters:
    actErrorPositions (List[int]): The actual error positions for each file.
    detectErrorPositions (List[List[Tuple[int, float]]]): A list of detected error positions and their probabilities for each file.
    k (int): The number of top error positions to consider.
    threshold (float, optional): The probability threshold. Defaults to None.

    Returns:
    precision(float): The precision at k.
    """

    numFiles = len(actErrorPositions)
    numOfCorrectItems = 0

    # Iterate through each file
    for i in range(numFiles):
        # Store the actual error position for the current file
        actErrorPos = actErrorPositions[i]
        # Store the list with detected error positions for the current file
        detectErrorPosList = detectErrorPositions[i]
        # Sort probabilities in descending order
        sortErrPos = sorted(detectErrorPosList, key = lambda x: x[1], reverse = True)
        # Keep just the k positions with the highest probabilties of being a formatting error
        topKErrPos = [x for x in sortErrPos[:k]]
        # Check if probability threshold is given
        if threshold is not None:
            # Keep the probabilities with the correspondig positions, whice are above the defined threshold
            topKErrPos = [pos for pos, prob in topKErrPos if prob > threshold]
        # Check if the actual error positions belong to the top k error positions
        if actErrorPos in topKErrPos:
            numOfCorrectItems += 1

    precision = numOfCorrectItems / numFiles 

    return precision

def mrr(actErrorPositions, detectErrorPositions):
    numFiles = len(actErrorPositions)
    totalRR = 0

    for i in range(numFiles):
        actErrorPos = actErrorPositions[i]
        detectErrorPosList = detectErrorPositions[i]
        sortErrPos = sorted(detectErrorPosList, key=lambda x: x[1], reverse=True)
        topErrPos = [x[0] for x in sortErrPos if x[0] is not None]
        currRank = topErrPos.index(actErrorPos) + 1 if actErrorPos in topErrPos else len(topErrPos)
        rr_i = 1 / currRank
        totalRR += rr_i

    mrr = (1 / numFiles) * totalRR

    return mrr