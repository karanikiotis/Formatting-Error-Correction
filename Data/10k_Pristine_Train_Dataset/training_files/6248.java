/*
 * Copyright (c) 2015 by Robert Bärhold, Zuse Institute Berlin
 *
 * Licensed under the BSD License, see LICENSE file for details.
 *
 */
package org.xtreemfs.common.quota;

public class QuotaConstants {

    public final static long UNLIMITED_VOUCHER = 0;
    public final static long UNLIMITED_QUOTA   = 0;
    public final static long NO_QUOTA          = -1;

    public static final long    DEFAULT_VOUCHER_SIZE = 10 * 1024 * 1024; // 10 MB

    public final static boolean CHECK_QUOTA_ON_CHOWN = false;
}
