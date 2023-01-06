/*
 ***************************************************************************************
 *  Copyright (C) 2006 EsperTech, Inc. All rights reserved.                            *
 *  http://www.espertech.com/esper                                                     *
 *  http://www.espertech.com                                                           *
 *  ---------------------------------------------------------------------------------- *
 *  The software in this package is published under the terms of the GPL license       *
 *  a copy of which has been included with this distribution in the license.txt file.  *
 ***************************************************************************************
 */
package com.espertech.esper.example.cycledetect;

public class TransactionEvent {
    private final String fromAcct;
    private final String toAcct;
    private final double amount;

    public TransactionEvent(String fromAcct, String toAcct, double amount) {
        this.fromAcct = fromAcct;
        this.toAcct = toAcct;
        this.amount = amount;
    }

    public String getFromAcct() {
        return fromAcct;
    }

    public String getToAcct() {
        return toAcct;
    }

    public double getAmount() {
        return amount;
    }
}
