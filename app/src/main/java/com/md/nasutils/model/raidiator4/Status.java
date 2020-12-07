/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.model.raidiator4;

/**
 * Container object for overall NAS Status
 * 
 * @author michaeldoyle
 */
public class Status {

    private NasHealth mHealth;
    private NasStatus mStatus;
    
    public Status(NasHealth health, NasStatus status) {
        super();
        this.mHealth = health;
        this.mStatus = status;
    }
    
    public NasHealth getHealth() {
        return mHealth;
    }
    
    public void setHealth(NasHealth health) {
        this.mHealth = health;
    }
    
    public NasStatus getStatus() {
        return mStatus;
    }
    
    public void setStatus(NasStatus status) {
        this.mStatus = status;
    }
}
