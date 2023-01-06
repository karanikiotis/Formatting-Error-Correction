/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nest.internal.messages;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Possible values for *_alarm_state:
 *
 * <dl>
 * <dt>ok</dt>
 * <dd>OK</dd>
 * <dt>warning</dt>
 * <dd>Warning - Smoke or CO Detected</dd>
 * <dt>emergency</dt>
 * <dd>Emergency - * Detected - move to fresh air</dd>
 * </dl>
 *
 * @author John Cocula
 * @since 1.9.0
 */
public enum AlarmState {
    OK("ok"),
    WARNING("warning"),
    EMERGENCY("emergency");

    private final String state;

    private AlarmState(String state) {
        this.state = state;
    }

    @JsonValue
    public String value() {
        return state;
    }

    @JsonCreator
    public static AlarmState forValue(String v) {
        for (AlarmState as : AlarmState.values()) {
            if (as.state.equals(v)) {
                return as;
            }
        }
        throw new IllegalArgumentException("Invalid alarm_state: " + v);
    }

    @Override
    public String toString() {
        return this.state;
    }
}
