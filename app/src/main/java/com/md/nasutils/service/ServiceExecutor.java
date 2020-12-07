/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service;

import android.os.Parcelable;

/**
 * Provides a common interface for executing a service request
 * 
 * @author michaeldoyle
 */
public interface ServiceExecutor {

    Parcelable execute(ServiceExecutionContext context);
}
