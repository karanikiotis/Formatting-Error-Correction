/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.souliss.internal.network.typicals;

/**
 * Typical T58 Pressure Sensor Derived from T51 Analog input, half-precision
 * floating point
 *
 * @author Stephen Olesen
 * @since 1.8.0
 */
public class SoulissT58 extends SoulissT51 {

    public SoulissT58(String sSoulissNodeIPAddressOnLAN, int iIDNodo, int iSlot, String sOHType) {
        super(sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
        this.setType(Constants.Souliss_T58_PressureSensor);
    }
}
