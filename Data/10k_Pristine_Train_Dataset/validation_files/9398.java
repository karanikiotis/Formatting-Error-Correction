/**
 * =============================================================================
 *
 * ORCID (R) Open Source
 * http://orcid.org
 *
 * Copyright (c) 2012-2014 ORCID, Inc.
 * Licensed under an MIT-Style License (MIT)
 * http://orcid.org/open-source-license
 *
 * This copyright and license information (including a link to the full license)
 * shall be included in its entirety in all copies or substantial portion of
 * the software.
 *
 * =============================================================================
 */
package org.orcid.jaxb.model.common_v2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.orcid.utils.DateUtils;

public class LastModifiedDateTest {

    @Test
    public void testWithLongs() {
        LastModifiedDate empty = new LastModifiedDate();
        LastModifiedDate _1000 = new LastModifiedDate(DateUtils.convertToXMLGregorianCalendar(1000));
        LastModifiedDate _1001 = new LastModifiedDate(DateUtils.convertToXMLGregorianCalendar(1001));
        assertTrue(_1000.after(null));
        assertTrue(_1000.after(empty));
        assertFalse(empty.after(_1000));
        assertFalse(empty.after(empty));
        assertFalse(_1000.after(_1000));
        assertTrue(_1001.after(_1000));
        assertFalse(_1000.after(_1001));
    }

    @Test
    public void testWithDates() {
        LastModifiedDate empty = new LastModifiedDate();
        LastModifiedDate _1000 = new LastModifiedDate(DateUtils.convertToXMLGregorianCalendar(new Date(1000)));
        LastModifiedDate _1001 = new LastModifiedDate(DateUtils.convertToXMLGregorianCalendar(new Date(1001)));
        assertTrue(_1000.after(null));
        assertTrue(_1000.after(empty));
        assertFalse(empty.after(_1000));
        assertFalse(empty.after(empty));
        assertFalse(_1000.after(_1000));
        assertTrue(_1001.after(_1000));
        assertFalse(_1000.after(_1001));
    }
}
