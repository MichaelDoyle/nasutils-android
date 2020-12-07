/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator4;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.core.Commit;

/**
 * Domain object representing the NAS device details
 * 
 * Annotated for parsing the XML output returned by ReadyNAS FrontView
 * using the Simple XML Framework.
 * 
 * @author michaeldoyle
 */
@Root(strict=false)
public class Device {
    
    public static final String CPU_SYS = "temp";
    public static final String DISK = "CHAN";
    public static final String FAN = "fan";
    public static final String UPS = "ups";

    @Element(name="id", required=false)
    private String mId;
    
    @Element(name="value", required=false)
    @Path("device_name")
    private String mName;
    
    @Element(name="value", required=false)
    @Path("device_model")
    private String mModel;
    
    @Element(name="value", data=true, required=false)
    @Path("device_status")
    private String mStatus;
    
    @Transient
    private String mTemperatureString;
    
    @Transient
    private int mTemperatureF;
    
    @Transient
    private int mTemperatureC;
    
    @Transient
    private int mRpm;
    
    @Transient
    private DeviceType mType;

    public Device() {
        // necessary to keep xml deserialization happy
    }
    
    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }
    
    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        this.mModel = model;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }
    
    public String getTemperatureString() {
        return mTemperatureString;
    }

    public void setTemperatureString(String temperatureString) {
        this.mTemperatureString = temperatureString;
    }

    public int getTemperatureF() {
        return mTemperatureF;
    }
    
    public String getTemperatureStringF() {
        return mTemperatureF + " F";
    }

    public void setTemperatureF(int temperatureF) {
        this.mTemperatureF = temperatureF;
    }

    public int getTemperatureC() {
        return mTemperatureC;
    }
    
    public String getTemperatureStringC() {
        return mTemperatureC + " C";
    }

    public void setTemperatureC(int temperatureC) {
        this.mTemperatureC = temperatureC;
    }
    
    public DeviceType getType() {
        return mType;
    }

    public void setmType(DeviceType type) {
        this.mType = type;
    }
    
    public int getRpm() {
        return mRpm;
    }

    public void setRpm(int rpm) {
        this.mRpm = rpm;
    }

    /** <p>Post deserialization processing</p> */
    @Commit
    public void postDeserialization() {
        if (mId != null) {
            if (FAN.equals(mId)) {
                parseFanRpm();
                mType = DeviceType.FAN;
            } else if (UPS.equals(mId)) {
                mType = DeviceType.UPS;
            } else if (CPU_SYS.equals(mId)) {
                parseCpuSysTemp();
                mType = DeviceType.CPU_SYS;
            } else if (mId.startsWith(DISK)) {
                parseDiskTemp();
                parseModel();
                mType = DeviceType.DISK;
            }
        }
    }

    /**
     * <p>Will string out temperature and status data from model 
     * name, assuming the model name is in the format:</p>
     * 
     * <p>Seagate ST2000DL003-9VT166 1863 GB , 34 C / 93 F , Write-cache ON</p>
     */
    private void parseModel() {
        String[] splits = mModel.split(",");
        if(splits.length >= 1) {
            mModel = splits[0];
        }
    }
    
    /**
     * <p>Will parse and set RPM from the model field, 
     * assuming the model name is in the format: "946  RPM"</p>
     */
    private void parseFanRpm() {
        String[] splits = mModel.split(" ");
        if(splits.length > 1) {
            try {
                if (splits[0].contains(".")) {
                    mRpm = (int) Double.parseDouble(splits[0]);
                } else {
                    mRpm = Integer.parseInt(splits[0]);
                }
            } catch(NumberFormatException e) {
                // just default to 0 (do nothing)
            }
        }
    }
    
    /**
     * <p>Will parse and set CPU/SYS temperature from the model field, 
     * assuming the model name is in the format:</p>
     * 
     * <p>50 C / 122 F [Normal 0-65 C / 32-149 F</p>
     */
    private void parseCpuSysTemp() {
        String[] splits = mModel.split(" ");
        if(splits.length > 4) {
            try {
                if (splits[0].contains(".")) {
                    mTemperatureC = (int) Double.parseDouble(splits[0]);
                    mTemperatureF = (int) Double.parseDouble(splits[3]);
                } else {
                    mTemperatureC = Integer.parseInt(splits[0]);
                    mTemperatureF = Integer.parseInt(splits[3]);
                }
            } catch(NumberFormatException e) {
                // just default to 0 (do nothing)
            }
        }
    }
    
    /**
     * <p>Will parse and set temperature from the model field, 
     * assuming the model name is in the format:</p>
     * 
     * <p>Seagate ST2000DL003-9VT166 1863 GB , 34 C / 93 F , Write-cache ON</p>
     */
    private void parseDiskTemp() {
        String[] splits = mModel.split(",");
        if(splits.length > 2) {
            String temp = splits[1].trim();
            mTemperatureString = temp;
            String[] tempSplits = temp.split(" ");
            try {
                if (tempSplits[0].contains(".")) {
                    mTemperatureC = (int) Double.parseDouble(tempSplits[0]);
                    mTemperatureF = (int) Double.parseDouble(tempSplits[3]);
                } else {
                    mTemperatureC = Integer.parseInt(tempSplits[0]);
                    mTemperatureF = Integer.parseInt(tempSplits[3]);
                }
            } catch(NumberFormatException e) {
                // just default to 0 (do nothing)
            }
        }
    }
}
