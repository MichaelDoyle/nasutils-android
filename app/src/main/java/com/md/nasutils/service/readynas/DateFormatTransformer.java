/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service.readynas;

import org.simpleframework.xml.transform.Transform;

import java.text.DateFormat;
import java.util.Date;

/**
 * Used to configure the Simple XML Framework to
 * properly serialize and/or deserialize dates based on
 * a given format
 * 
 * @author michaeldoyle
 */
public class DateFormatTransformer implements Transform<Date> {

    @SuppressWarnings("unused")
    private static final String TAG = DateFormatTransformer.class.getSimpleName();

    private DateFormat mDateFormat;

    public DateFormatTransformer(DateFormat dateFormat) {
        this.mDateFormat = dateFormat;
    }

    @Override
    public Date read(String value) throws Exception {
        return mDateFormat.parse(value);
    }

    @Override
    public String write(Date value) throws Exception {
        return mDateFormat.format(value);
    }
}
