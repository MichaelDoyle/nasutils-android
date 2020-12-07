/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service;

/**
 * Constants primarily for use with the {@link ReadyNasIntentService}
 * 
 * @author michaeldoyle
 */
public final class ReadyNasServiceConstants {

    // Extra identifiers
    public static final String EXTRA_ACTION = "com.md.nasutils.EXTRA_ACTION";
    public static final String EXTRA_ADDONS = "com.md.nasutils.ADDONS";
    public static final String EXTRA_BACKUP_JOB = "com.md.nasutils.EXTRA_BACKUP_JOB";
    public static final String EXTRA_DISK = "com.md.nasutils.EXTRA_DISK";
    public static final String EXTRA_DEVICE = "com.md.nasutils.EXTRA_DEVICE";
    public static final String EXTRA_FORCE_FSCK = "com.md.nasutils.EXTRA_FORCE_FSCK";
    public static final String EXTRA_FORCE_QUOTA_CHECK = "com.md.nasutils.EXTRA_FORCE_QUOTA_CHECK";
    public static final String EXTRA_NAS = "com.md.nasutils.EXTRA_NAS";
    public static final String EXTRA_NAS_CONFIG = "com.md.nasutils.EXTRA_NAS_CONFIG";
    public static final String EXTRA_NUMBER_OF_PACKETS = "com.md.nasutils.EXTRA_NUMBER_OF_PACKETS";
    public static final String EXTRA_PORT = "com.md.nasutils.EXTRA_PORT";
    public static final String EXTRA_RESULT_RECEIVER = "com.md.nasutils.EXTRA_RESULT_RECEIVER";
    public static final String EXTRA_SEND_AS_BROADCAST = "com.md.nasutils.EXTRA_SEND_AS_BROADCAST";
    public static final String EXTRA_SINCE = "com.md.nasutils.EXTRA_SINCE";
    public static final String READY_NAS_RESULT = "com.md.nasutils.READY_NAS_RESULT";
    public static final String EXTRA_WIDGET_ID = "com.md.nasutils.WIDGET_ID";

    private ReadyNasServiceConstants() {
        throw new AssertionError();
    }
}
