/*
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Annotated for parsing the XML output returned by RAIDiator 5
 * using the Simple XML Framework.
 *
 * @author michaeldoyle
 */
@Root(name="Schedule", strict=false)
public class BackupSchedule {

    @Element(name="Enabled", required=false)
    private int mEnabled;

    @Element(name="Frequency", required=false)
    private int mFrequency;

    @Element(name="Start", required=false)
    private int mStart;

    @Element(name="End", required=false)
    private int mEnd;

    @Element(name="DayOfWeek", required=false)
    private String mDayOfWeek;

    public BackupSchedule() {
        // necessary to keep xml deserialization happy
    }

    public int getEnabled() {
        return mEnabled;
    }

    public void setEnabled(int enabled) {
        this.mEnabled = enabled;
    }

    public int getFrequency() {
        return mFrequency;
    }

    public void setFrequency(int mFrequency) {
        this.mFrequency = mFrequency;
    }

    public int getStart() {
        return mStart;
    }

    public void setStart(int start) {
        this.mStart = start;
    }

    public int getEnd() {
        return mEnd;
    }

    public void setEnd(int end) {
        this.mEnd = end;
    }

    public String getDayOfWeek() {
        return mDayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.mDayOfWeek = dayOfWeek;
    }

    public String getDisplaySchedule() {

        if (mDayOfWeek != null) {
            StringBuilder b = new StringBuilder(mDayOfWeek);

            b.append(" every ")
                    .append(mFrequency)
                    .append(" hr between ")
                    .append(mStart)
                    .append("-")
                    .append(mEnd);

            return b.toString().replace("sun,mon,tue,wed,thu,fri,sat", "Daily");
        }

        return "";
    }
}
