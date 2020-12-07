/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Response implements Parcelable {

    private String mMessage;
    private boolean mFailedValidation;

    public Response() {
    }
    
    public Response(String message) {
        this.mMessage = message;
    }
    
    public Response(String message, boolean failedValidation) {
        this.mMessage = message;
        this.mFailedValidation = failedValidation;
    }
    
    public Response(Parcel in) {
        mMessage = in.readString();
        mFailedValidation = in.readByte() == 1;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }
    
    
    
    public boolean isFailedValidation() {
        return mFailedValidation;
    }

    public void setFailedValidation(boolean failedValidation) {
        this.mFailedValidation = failedValidation;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mMessage);
        out.writeByte((byte) (mFailedValidation ? 1 : 0));
    }

    public static final Parcelable.Creator<Response> CREATOR = 
            new Parcelable.Creator<Response>() {
                public Response createFromParcel(Parcel in) {
                    return new Response(in);
                }
        
                public Response[] newArray(int size) {
                    return new Response[size];
                }
            };
}
