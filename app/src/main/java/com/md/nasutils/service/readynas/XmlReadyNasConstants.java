/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service.readynas;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Provides constant values commonly used when communicating with ReadyNAS
 * devices using the XML based NML protocol. This includes RAIDiator 5, OS6
 * and ReadyDATA devices.
 * 
 * @author michaeldoyle
 */
public final class XmlReadyNasConstants {

    // Services
    public static final String DB_BROKER = "/db_broker";

    // Resource IDs

    // Resource Types

    public static final List<String> LOG_LEVEL_ERROR = Collections
            .unmodifiableList(Arrays.asList(new String[] { "err", "crit", "emerge" }));

    public static final List<String> LOG_LEVEL_WARNING = Collections
            .unmodifiableList(Arrays
                    .asList(new String[] { "warning", "alert" }));

    public static final List<String> LOG_LEVEL_INFO = Collections
            .unmodifiableList(Arrays.asList(new String[] { "info", "notice" }));

    private XmlReadyNasConstants() {
        throw new AssertionError();
    }
}
