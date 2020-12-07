/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.util;

public class UnitConversionUtils {

    public static int celciusToFahrenheit(int temp) {
        return (int) ((temp * 1.8) + 32);
    }
    
    public static double bytesToGigabytes(double bytes) {
        return bytes / (1024 * 1024 * 1024);
    }
    
    public static double megabytesToGigabytes(double mb) {
        return mb / (1024 * 1024);
    }
}
