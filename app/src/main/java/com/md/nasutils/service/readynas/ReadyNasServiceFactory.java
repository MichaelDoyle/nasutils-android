/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service.readynas;

import com.md.nasutils.service.http.NasConfiguration;

/**
 * A very simple factory to create the appropriate ReadyNasService
 * based on OS Version
 *  
 * @author michaeldoyle
 */
public class ReadyNasServiceFactory {

    private static Os6ReadyNasService mOs6 = null;
    private static Raidiator5ReadyNasService mOs5  = null;
    private static Raidiator4ReadyNasService mOs4 = null;

    public synchronized static ReadyNasService getService(NasConfiguration config) {
        int osVersion = config.getOsVersion();

        switch(osVersion) {
        case 6:
            return getOs6ReadyNasService();
        case 5:
            return getOs5ReadyNasService();
        case 4:
            return getOs4ReadyNasService();
        default:
            throw new IllegalArgumentException(osVersion + " is not a supported OS version!");
        }
    }

    private static Os6ReadyNasService getOs6ReadyNasService() {
        if (mOs6 == null) {
            mOs6 = new Os6ReadyNasService();
        }
        return mOs6;
    }

    private static Raidiator5ReadyNasService getOs5ReadyNasService() {
        if (mOs5 == null) {
            mOs5 = new Raidiator5ReadyNasService();
        }
        return mOs5;
    }

    private static Raidiator4ReadyNasService getOs4ReadyNasService() {
        if (mOs4 == null) {
            mOs4 = new Raidiator4ReadyNasService();
        }
        return mOs4;
    }
}
