/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator5;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.core.Commit;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Specifically for responses from the fsbroker from RAIDiator 5
 * 
 * @author michaeldoyle
 */
@Root(strict=false, name="nml")
@NamespaceList({
        @Namespace(reference="http://www.netgear.com/protocol/transaction/NMLSchema-0.9", prefix="xs"),
        @Namespace(reference="urn:netgear:nas:readynasd") })
@Namespace(reference="http://www.netgear.com/protocol/transaction/NMLSchema-0.9", prefix="xs")
public class FsBrokerResponse {
            
    @Path("transaction/response")
    @Attribute(name="status", required=false)
    private String mStatus;
    
    @Path("transaction/response/result/get-s")
    @Text(data=true, required=false)
    private String mResult;
    
    @Transient
    private List<Volume> mVolumes;
    
    public FsBrokerResponse() {
        // necessary to keep xml deserialization happy
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        this.mStatus = status;
    }

    public String getResult() {
        return mResult;
    }

    public void setResult(String result) {
        this.mResult = result;
    }
    
    public List<Volume> getVolumes() {
        return mVolumes;
    }

    public void setVolumes(List<Volume> volumes) {
        this.mVolumes = volumes;
    }
    
    public Volume getVolumeByName(String name) {
        for (Volume vol : mVolumes) {
            if (vol.getName().equals(name)) {
                return vol;
            }
        }
        return null;
    }
    
    /**
     * Do any required post processing in this method.
     * 
     * For some reason, we get an embedded XML document encoded as CDATA for the response.
     * We could create an object to represent this response, and bind it through
     * Simple a second time. For simplicity, we'll just parse it manually instead.
     * 
     * <?xml version="1.0" encoding="utf-8"?>
     * <fsinfo>
     *     <volume mount="/c" size="1976928342016" free="1976722530304"/>
     * </fsinfo>
     */
    @Commit
    private void postProcess() {
        mVolumes = new ArrayList<>();
        
        if(mResult != null) {
            String[] lines = mResult.split("\n");
            
            for (String line : lines) {
                String l = line.trim();
                
                if (l.startsWith("<volume")) {
                    String[] pairs = l.replace("<volume ","").replace("/>","").split(" ");      
                    mVolumes.add(createVolume(pairs));
                }
            }
        }
    }
    
    /**
     * @param pairs an array of String, formatted as key="value"
     * @return a Volume object constructed from the parsed out key/value pairs
     */
    private Volume createVolume(String[] pairs) {
        Volume vol = new Volume();
        
        for (String s : pairs) {
            String[] split = s.split("=");
            if (split.length > 1) {
                String key = split[0];
                String value = split[1].replace("\"","");
                
                if ("mount".equals(key)) {
                    vol.setName(value.replace("/", "").toUpperCase(Locale.US));
                } else if ("size".equals(key)) {
                    vol.setCapacityBytes(Long.valueOf(value));
                } else if ("free".equals(key)) {
                    vol.setFreeBytes(Long.valueOf(value));
                }
            }
        }
        
        return vol;
    }
}
