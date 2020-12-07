/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service.readynas;

import com.md.nasutils.model.AddOn;
import com.md.nasutils.model.Dictionary;
import com.md.nasutils.model.Disk;
import com.md.nasutils.model.Logs;
import com.md.nasutils.model.Nas;
import com.md.nasutils.model.Response;
import com.md.nasutils.model.Service;
import com.md.nasutils.service.http.NasConfiguration;

import java.util.List;

/**
 * Services for interacting with a ReadyNas device
 * 
 * @author michaeldoyle
 */
public interface ReadyNasService {

    /**
     * Interrogates the ReadyNAS for comprehensive status details
     * such as device information, fan/cpu temps, drive capacity, etc
     * 
     * @param config nas config details
     * @return comprehensive NAS status details
     */
    Nas getStatus(NasConfiguration config);

    /**
     * Interrogates the ReadyNAS for its log history
     * 
     * @param config nas config details
     * @return log history from the last year
     */
    Logs getStatusLogs(NasConfiguration config);

    /**
     * Send a shutdown request to the ReadyNAS
     * 
     * @param config nas config details
     * @param forceFsck force file system check on next boot?
     * @param forceQuotaCheck force quota check on next boot?
     * @return generic {@link Response} indicating the status of the request
     */
    Response shutdown(NasConfiguration config, boolean forceFsck, boolean forceQuotaCheck);

    /**
     * Send a reboot request to the ReadyNAS
     * 
     * @param config nas config details
     * @param forceFsck force file system check on next boot?
     * @param forceQuotaCheck force quota check on next boot?
     * @return generic {@link Response} indicating the status of the request
     */
    Response restart(NasConfiguration config, boolean forceFsck, boolean forceQuotaCheck);
    
    /**
     * Locate disk by blinking its corresponding LCD.
     * 
     * @param config nas config details
     * @param disk index of the disk (0, 1, etc)
     * @return
     */
    Response locateDisk(NasConfiguration config, String disk);

    /**
     * Interrogates the ReadyNAS for SMART+ info for a given disk.
     * 
     * @param config nas config details
     * @param disk index of the disk (0, 1, etc)
     * @return SMART+ disk attributes
     */
    Disk getSmartDiskInfo(NasConfiguration config, String disk, String deviceName);
    
    /**
     * Get the dictionary of display text for log messages;
     * 
     * @param config
     * @return
     */
    Dictionary getDictionary(NasConfiguration config);
    
    /**
     * Interrogates the ReadyNAS for available services and their status
     * 
     * @param config nas config details
     * @return Nas object with services details populated
     */
    Nas getServices(NasConfiguration config);
    
    /**
     * Turn services on/off and change related options.
     * 
     * @param config nas config details
     * @param nas Nas object with services details populated
     * @return
     */
    Response setServices(NasConfiguration config, Nas nas);
    
    /**
     * Retrieve FTP service details
     * 
     * @param config nas config details
     * @return Service containing FTP details
     */
    Service getFtpServiceDetails(NasConfiguration config);
    
    /**
     * Recalibrate system fan
     * 
     * @param config nas config details
     * @return
     */
    Response recalibrateFan(NasConfiguration config);
    
    /**
     * Rescan DLNA
     * 
     * @param config nas config details
     * @return
     */
    Response rescanDlna(NasConfiguration config);

    /**
     * Interrogates the ReadyNAS for backup jobs
     *
     * @param config nas config details
     * @return Nas object with backup details populated
     */
    Nas getBackups(NasConfiguration config);

    /**
     * Run the given backup job
     *
     * @param config nas config details
     * @param backupJob name of the backup job to run
     * @return
     */
    Response startBackup(NasConfiguration config, String backupJob);

    /**
     * Interrogates the ReadyNAS for add-ons
     *
     * @param config nas config details
     * @return Nas object with add-on details populated
     */
    Nas getAddOns(NasConfiguration config);

    /**
     * Start/stop the given add-ons
     *
     * @param config
     * @param addOns
     * @return
     */
    Response toggleAddOns(NasConfiguration config, List<AddOn> addOns);
}
