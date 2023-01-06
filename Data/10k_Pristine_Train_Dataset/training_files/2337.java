/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.digitalstrom.internal.client.constants;

/**
 * @author Alexander Betker
 * @since 1.3.0
 * @version digitalSTROM-API 1.14.5
 */
public enum SensorIndexEnum {

    ACTIVE_POWER(2),
    OUTPUT_CURRENT(3),
    ELECTRIC_METER(4),
    TEMPERATURE_INDOORS(2),
    RELATIVE_HUMIDITY_INDOORS(3),
    TEMPERATURE_OUTDOORS(2),
    RELATIVE_HUMIDITY_OUTDOORS(3);

    private final int index;

    private SensorIndexEnum(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

}
