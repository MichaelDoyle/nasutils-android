/*
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import android.test.suitebuilder.TestSuiteBuilder;

public class NasUtilsTestSuite extends TestSuite {

    public static Test suite() {
        return new TestSuiteBuilder(NasUtilsTestSuite.class).includeAllPackagesUnderHere().build();
    }
}

