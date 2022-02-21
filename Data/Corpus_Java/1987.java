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
package com.espertech.esper.epl.agg.access;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.epl.expression.core.ExprEvaluatorContext;

public class AggregationStateMinMaxByEverWFilter extends AggregationStateMinMaxByEver {

    public AggregationStateMinMaxByEverWFilter(AggregationStateMinMaxByEverSpec spec) {
        super(spec);
    }

    @Override
    public void applyEnter(EventBean[] eventsPerStream, ExprEvaluatorContext exprEvaluatorContext) {
        EventBean theEvent = eventsPerStream[spec.getStreamId()];
        if (theEvent == null) {
            return;
        }
        Boolean pass = (Boolean) spec.getOptionalFilter().evaluate(eventsPerStream, true, exprEvaluatorContext);
        if (pass != null && pass) {
            super.addEvent(theEvent, eventsPerStream, exprEvaluatorContext);
        }
    }
}
