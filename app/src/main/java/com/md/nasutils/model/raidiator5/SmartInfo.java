/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import com.md.nasutils.service.readynas.Raidiator5SmartAttributesConverter;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import java.util.Map;

/**
 * Annotated for parsing the XML output returned by ReadyNAS RAIDiator 5
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(name="SMARTInfo", strict=false)
public class SmartInfo implements ResultData {

    @Element(name="Model", required=false)
    private String mModel;
    
    @Element(name="Serial", required=false)
    private String mSerial;
    
    @Element(name="Firmware_Version", required=false)
    private String mFirmwareVersion;
    
    @Element(name="Attribute", required=false)
    @Convert(Raidiator5SmartAttributesConverter.class)
    private Map<String, String> mAttributes;
    
    public SmartInfo() {
        // necessary to keep xml deserialization happy
    }

    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        this.mModel = model;
    }

    public String getSerial() {
        return mSerial;
    }

    public void setSerial(String serial) {
        this.mSerial = serial;
    }

    public String getFirmwareVersion() {
        return mFirmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.mFirmwareVersion = firmwareVersion;
    }

    public Map<String, String> getAttributes() {
        return mAttributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.mAttributes = attributes;
    }
}
