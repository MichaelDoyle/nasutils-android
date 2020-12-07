/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service.istat;

import android.text.TextUtils;
import android.util.Log;

import com.github.michaeldoyle.istat4j.client.IstatException;
import com.github.michaeldoyle.istat4j.client.IstatService;
import com.github.michaeldoyle.istat4j.client.IstatServiceFactory;
import com.github.michaeldoyle.istat4j.client.IstatUnauthorizedException;
import com.github.michaeldoyle.istat4j.client.ServerConfiguration;
import com.md.nasutils.model.Telemetry;
import com.md.nasutils.model.TelemetryCpu;
import com.md.nasutils.model.TelemetryLoad;
import com.md.nasutils.model.TelemetryMemory;
import com.md.nasutils.model.TelemetryNetwork;
import com.md.nasutils.service.http.NasConfiguration;

import java.util.ArrayList;
import java.util.List;

/** 
 * @author michaeldoyle
 */
public class ReadyNasIstatServiceImpl implements ReadyNasIstatService {

    private static final String TAG = ReadyNasIstatServiceImpl.class.getSimpleName();

    private IstatService mService;
    
    public ReadyNasIstatServiceImpl(){
        mService = IstatServiceFactory.getService();
    }

    @Override
    public Telemetry getTelemetry(NasConfiguration config) { 
        return getTelemetrySince(config, -2);
    }
    
    @Override
    public Telemetry getTelemetrySince(NasConfiguration config, long since) {
        String hostname = ServerConfiguration.getLocalHostname();
        if (TextUtils.isEmpty(hostname)) {
            hostname = "nasutils";
        }
        
        ServerConfiguration sc = new ServerConfiguration(
                hostname,
                ServerConfiguration.generateUuid(), 
                config.getHostname(), 
                config.getIstatPort(), 
                config.getIstatPasscode());
        mService.setServerConfiguration(sc);
        
        Telemetry telemetry = new Telemetry();
        telemetry.setCpuTelemetry(new ArrayList<TelemetryCpu>());
        telemetry.setNetworkTelemetry(new ArrayList<TelemetryNetwork>());
        telemetry.setAuthorized(true);
        
        try {
            com.github.michaeldoyle.istat4j.model.Telemetry istatTelemetry = mService.getTelemetry(since);
            
            if (istatTelemetry != null) {
                telemetry.setDiskSid(istatTelemetry.getDiskSid());
                telemetry.setFanSid(istatTelemetry.getFanSid());
                telemetry.setRequestId(istatTelemetry.getRequestId());
                telemetry.setTempSid(istatTelemetry.getTempSid());
                telemetry.setUptime(istatTelemetry.getUptime());
                
                com.github.michaeldoyle.istat4j.model.Load istatLoad = istatTelemetry.getLoad();
                if (istatLoad != null) {
                    TelemetryLoad load = new TelemetryLoad();
                    load.setFifteenMinuteAverage(istatLoad.getFifteenMinuteAverage());
                    load.setFiveMinuteAverage(istatLoad.getFiveMinuteAverage());
                    load.setOneMinuteAverage(istatLoad.getOneMinuteAverage());
                    telemetry.setLoad(load);
                }
                
                com.github.michaeldoyle.istat4j.model.Memory istatMemory = istatTelemetry.getMemory();
                if (istatLoad != null) {
                    TelemetryMemory memory = new TelemetryMemory();
                    memory.setActive(istatMemory.getActive());
                    memory.setFree(istatMemory.getFree());
                    memory.setInactive(istatMemory.getInactive());
                    memory.setPageIns(istatMemory.getPageIns());
                    memory.setPageOuts(istatMemory.getPageOuts());
                    memory.setSwapTotal(istatMemory.getSwapTotal());
                    memory.setSwapUsed(istatMemory.getSwapUsed());
                    memory.setTotal(istatMemory.getTotal());
                    memory.setWired(istatMemory.getWired());
                    telemetry.setMemory(memory);
                }
                
                List<com.github.michaeldoyle.istat4j.model.TelemetryCpu> istatTelemetryCpu = 
                        istatTelemetry.getCpuTelemetry();
                if (istatTelemetryCpu != null) {
                    for (com.github.michaeldoyle.istat4j.model.TelemetryCpu c : istatTelemetryCpu) {
                        TelemetryCpu telemetryCpu = new TelemetryCpu();
                        telemetryCpu.setId(c.getId());
                        telemetryCpu.setNice(c.getNice());
                        telemetryCpu.setSystem(c.getSystem());
                        telemetryCpu.setUser(c.getUser());
                        telemetry.getCpuTelemetry().add(telemetryCpu);
                    }
                }
                
                List<com.github.michaeldoyle.istat4j.model.TelemetryNetwork> istatTelemetryNetwork = 
                        istatTelemetry.getNetworkTelemetry();
                if (istatTelemetryNetwork != null) {
                    for (com.github.michaeldoyle.istat4j.model.TelemetryNetwork n : istatTelemetryNetwork) {
                        TelemetryNetwork telemetryNetwork = new TelemetryNetwork();
                        telemetryNetwork.setId(n.getId());
                        telemetryNetwork.setDownloadBytes(n.getDownloadBytes());
                        telemetryNetwork.setTime(n.getTime());
                        telemetryNetwork.setUploadBytes(n.getUploadBytes());
                        telemetry.getNetworkTelemetry().add(telemetryNetwork);
                    }
                }
            }
            
        } catch (IstatException e) {
            Log.i(TAG, "Exception calling iStat." , e);
            throw new ReadyNasIstatServiceException("Exception calling iStat.", e);
        } catch (IstatUnauthorizedException e) {
            telemetry.setAuthorized(false);
        }
        
        return telemetry;
    }
}
