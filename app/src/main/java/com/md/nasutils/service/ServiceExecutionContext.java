/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service;

import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Provides the context in which a service request should be executed
 * 
 * @author michaeldoyle
 */
public class ServiceExecutionContext {

    private SharedPreferences preferences;
    private Bundle extras;

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public Bundle getExtras() {
        return extras;
    }

    public void setExtras(Bundle extras) {
        this.extras = extras;
    }
}
