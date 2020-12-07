/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service.readynas;

import android.content.res.Resources;
import android.text.Html;
import android.text.TextUtils;

import com.md.nasutils.NasUtilsApplication;
import com.md.nasutils.R;
import com.md.nasutils.model.BackupJob;
import com.md.nasutils.model.Dictionary;
import com.md.nasutils.model.Disk;
import com.md.nasutils.model.Enclosure;
import com.md.nasutils.model.Fan;
import com.md.nasutils.model.Log;
import com.md.nasutils.model.LogLevel;
import com.md.nasutils.model.Logs;
import com.md.nasutils.model.Nas;
import com.md.nasutils.model.NetworkInterface;
import com.md.nasutils.model.Response;
import com.md.nasutils.model.Service;
import com.md.nasutils.model.Temperature;
import com.md.nasutils.model.Ups;
import com.md.nasutils.model.Volume;
import com.md.nasutils.model.raidiator4.AddOn;
import com.md.nasutils.model.raidiator4.AddOnDetail;
import com.md.nasutils.model.raidiator4.AddOns;
import com.md.nasutils.model.raidiator4.BackupStatus;
import com.md.nasutils.model.raidiator4.Device;
import com.md.nasutils.model.raidiator4.Drive;
import com.md.nasutils.model.raidiator4.ModelInfo;
import com.md.nasutils.model.raidiator4.NasHealth;
import com.md.nasutils.model.raidiator4.NasStatus;
import com.md.nasutils.model.raidiator4.ProtocolDiscovery;
import com.md.nasutils.model.raidiator4.ProtocolStandard;
import com.md.nasutils.model.raidiator4.ProtocolStreaming;
import com.md.nasutils.model.raidiator4.SmartDiskInfo;
import com.md.nasutils.model.raidiator4.SmartDiskInfoAttribute;
import com.md.nasutils.model.raidiator4.StatusLogs;
import com.md.nasutils.service.http.HttpClient;
import com.md.nasutils.service.http.NasConfiguration;

import org.apache.http.message.BasicNameValuePair;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.md.nasutils.service.readynas.Raidiator4Constants.*;

/**
 * FrontView (RAIDiator 4.x) implementation for {@link ReadyNasService}
 * 
 * @author michaeldoyle
 */
public class Raidiator4ReadyNasService implements ReadyNasService {

    private static final String TAG = Raidiator4ReadyNasService.class.getSimpleName();

    private HttpClient mHttpClient;
    private Locale mLocale;
    private DateFormat mFormat;
    private boolean mHasMatchingLocale;
    
    public Raidiator4ReadyNasService(){
        mHttpClient = new HttpClient();
        mLocale = Locale.getDefault();
        mFormat = new SimpleDateFormat(DATE_FORMAT, mLocale);
    }
    
    @Override
    public Nas getStatus(NasConfiguration config){
        
        NasStatus status = mHttpClient.get(config, GET_STATUS_ALL, NasStatus.class);

        Nas nas = new Nas();
        
        nas.setSerialNumber(status.getNasSerial());
        nas.setHostname(status.getHostname());
        
        ModelInfo modelInfo = status.getModelInfo();
        if (modelInfo != null) {
            nas.setModel(modelInfo.getDisplayModel());
            nas.setFirmware(modelInfo.getDisplayFirmware());
            nas.setArchitecture(modelInfo.getArchitecture());
            nas.setMemory(modelInfo.getMemory());
        }
                
        NasHealth health = mHttpClient.get(config, GET_STATUS_HEALTH, NasHealth.class);

        Enclosure enclosure = new Enclosure();
        nas.setEnclosures(Arrays.asList(enclosure));
        
        enclosure.setTemperatures(new ArrayList<Temperature>());

        for (Device device : health.getTemps()) {
            Temperature temp = new Temperature();
            temp.setId(device.getId());
            temp.setLabel(device.getName());
            temp.setName(device.getName());
            temp.setStatus(device.getStatus());
            temp.setTemperature(device.getTemperatureC());
            temp.setTemperatureMin(1);
            temp.setTemperatureMax(80);
            enclosure.getTemperatures().add(temp);
        }
        
        enclosure.setFans(new ArrayList<Fan>());

        for (Device device : health.getFans()) {
            Fan fan = new Fan();
            fan.setId(device.getId());
            fan.setLabel(device.getName());
            fan.setName(device.getName());
            fan.setSpeed(device.getRpm());
            fan.setSpeedMin(712);
            fan.setSpeedMax(9999);
            fan.setStatus(device.getStatus());
            enclosure.getFans().add(fan);
        }

        enclosure.setDisks(new ArrayList<Disk>());

        // we get different disk data from status and health urls
        List<Device> healthDrives = health.getDrives();
        List<Drive> statusDrives = status.getDrives();

        if (healthDrives.size() <= statusDrives.size()) {
            for (int i=0; i<healthDrives.size(); i++) {
                Disk disk = new Disk();
                                
                Device healthDrive = healthDrives.get(i);
                
                disk.setId(healthDrive.getId());
                disk.setStatus(healthDrive.getStatus());
                disk.setLabel(healthDrive.getName());
                disk.setTemperature(healthDrive.getTemperatureC());
                disk.setTemperatureMin(1);
                disk.setTemperatureMax(80);
                
                Drive statusDrive = statusDrives.get(i);
                
                disk.setName(statusDrive.getDriveName());
                disk.setCapacityGb(statusDrive.getCapacity());
                disk.setChannel(statusDrive.getChannel());
                disk.setFirmwareVersion(statusDrive.getFirmware());
                disk.setModel(statusDrive.getModel());
                disk.setSerialNumber(statusDrive.getSerial());

                enclosure.getDisks().add(disk);
            }
        }
        
        nas.setUps(new ArrayList<Ups>());

        for (Device device : health.getUps()) {
            if (!"NA".equals(device.getStatus())) {
                Ups ups = new Ups();
                ups.setId(device.getId());
                ups.setLabel(device.getName());
                ups.setName(device.getName());
                ups.setStatus(device.getStatus());
                
                String model = device.getModel();
                String[] splits = model.split(",");
                
                try {
                    ups.setModel(splits[0]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    ups.setModel(device.getModel());
                }
                
                try {
                    String split = splits[1];
                    String charge = split.substring(split.indexOf(":") + 2, split.length());
                    ups.setBatteryCharge(charge);
                } catch (ArrayIndexOutOfBoundsException e) {
                } catch (IndexOutOfBoundsException e) {
                }
                
                try {
                    ups.setBatteryRuntime(splits[2]);
                } catch (ArrayIndexOutOfBoundsException e) {
                }
    
                nas.getUps().add(ups);
            }
        }
        
        nas.setNetworkInterfaces(new ArrayList<NetworkInterface>());
        
        for (com.md.nasutils.model.raidiator4.NetworkInterface iface : status.getNetworkInterfaces()) {
            NetworkInterface ni = new NetworkInterface();
            ni.setInterfaceName(iface.getInterfaceName());              
            ni.setStatus(iface.getInterfaceStatus());
            ni.setInterfaceType(iface.getInterfaceType());
            ni.setMacAddress(iface.getMacAddress());
            ni.setIpAddress(iface.getIpAddress());
            ni.setIpAddressType(iface.getIpAddressType());
            ni.setIpv6Address(iface.getIpv6Address());
            ni.setIpv6AddressType(iface.getIpv6AddressType());
            nas.getNetworkInterfaces().add(ni);
        }
        
        nas.setVolumes(new ArrayList<Volume>());
        
        for (com.md.nasutils.model.raidiator4.Volume vol : status.getVolumes()) {
            Volume volume = new Volume();
            volume.setName(vol.getName());
            volume.setStatus(vol.getMode().toUpperCase(Locale.US));
            
            if(vol.getRaidStatusDescription() != null) {
                volume.setHealth(Html.fromHtml(vol.getRaidStatusDescription()).toString());
            }
            
            volume.setRaidLevel(vol.getRaidLevel());
            volume.setSnapshotSize(vol.getSnapshotSize());
            volume.setPercentageUsed(vol.getPctUsed());
            volume.setUsageText(vol.getUsage());
            nas.getVolumes().add(volume);
        }
        
        return nas;
    }

    @Override
    public Logs getStatusLogs(NasConfiguration config){
        mHasMatchingLocale = true;
        
        StatusLogs payload = mHttpClient.get(config, GET_STATUS_LOGS, StatusLogs.class);
        
        Logs retLogs = new Logs();
        retLogs.setLogs(new ArrayList<Log>());
        
        List<com.md.nasutils.model.raidiator4.Log> logs = payload.getLogs();
        if (payload.getLogs() != null) {
            for (com.md.nasutils.model.raidiator4.Log l : logs) {
                Log log = new Log();
                log.setDateString(l.getTime());
                
                if (mHasMatchingLocale) {
                    log.setTime(getDate(l.getTime()));
                }
                
                log.setId(l.getId());
                log.setDescription(l.getDescription());
                log.setLogLevel(LogLevel.getLogLevel(l.getSeverityImage()));
                retLogs.getLogs().add(log);
            }
        }
        
        return retLogs;
    }
    
    @Override
    public Response shutdown(NasConfiguration config, boolean forceFsck, boolean forceQuotaCheck){
        List<BasicNameValuePair> postData = 
                createPowerPostData(POWEROFF, forceFsck, forceQuotaCheck);
        
        com.md.nasutils.model.raidiator4.Response response = mHttpClient.post(
                config, GET_HANDLER, postData,
                com.md.nasutils.model.raidiator4.Response.class);
        
        return new Response(response.getMessage());
    }
    
    @Override
    public Response restart(NasConfiguration config, boolean forceFsck, boolean forceQuotaCheck){
        List<BasicNameValuePair> postData = 
                createPowerPostData(REBOOT, forceFsck, forceQuotaCheck);
        
        com.md.nasutils.model.raidiator4.Response response = mHttpClient.post(
                config, GET_HANDLER, postData,
                com.md.nasutils.model.raidiator4.Response.class);
        
        return new Response(response.getMessage());
    }
    
    @Override
    public Response locateDisk(NasConfiguration config, String disk){
        List<BasicNameValuePair> postData = new ArrayList<>();
        postData.add(new BasicNameValuePair(OPERATION, SET));
        postData.add(new BasicNameValuePair(COMMAND, BLINK));
        postData.add(new BasicNameValuePair(DISK, disk));
        postData.add(new BasicNameValuePair(PAGE, VOLUME));
        postData.add(new BasicNameValuePair(OUTER_TAB, TAB_VOLUME_C));
        postData.add(new BasicNameValuePair(INNER_TAB, TAB_RAID_SETTINGS));
        
        com.md.nasutils.model.raidiator4.Response response = mHttpClient.post(
                config, GET_HANDLER, postData,
                com.md.nasutils.model.raidiator4.Response.class);
        
        return new Response(response.getMessage());
    }
    
    @Override
    public Disk getSmartDiskInfo(NasConfiguration config, String diskIndex, String deviceName) {
        String url = GET_SMART_DISK_INFO + diskIndex;
        SmartDiskInfo payload = mHttpClient.get(config, url, SmartDiskInfo.class);
        
        Disk disk = new Disk();
        disk.setSerialNumber(payload.getSerial());
        disk.setSmartAttributes(new HashMap<String, String>());
        
        for (SmartDiskInfoAttribute attr : payload.getAttributes()) {
            if (attr.getName() != null) {
                disk.getSmartAttributes().put(attr.getName(), attr.getValue());
            }
        }
        
        return disk;    
    }
    
    @Override
    public Dictionary getDictionary(NasConfiguration config) {
        throw new UnsupportedOperationException("Not available for this device.");
    }
    
    @Override
    public Nas getServices(NasConfiguration config) {
                
        Map<String, Service> services = new LinkedHashMap<>();

        ProtocolStandard protocolsStandard = getProtocolStandard(config);
        services.put(SERVICE_CIFS, getServiceCifs(protocolsStandard));
        services.put(SERVICE_NFS, getServiceNfs(protocolsStandard));
        services.put(SERVICE_AFP, getServiceAfp(protocolsStandard));
        services.put(SERVICE_FTP, getServiceFtp(protocolsStandard));
        services.put(SERVICE_HTTP, getServiceHttp(protocolsStandard));
        services.put(SERVICE_HTTPS, getServiceHttps(protocolsStandard));
        services.put(SERVICE_RSYNC, getServiceRsync(protocolsStandard));
                
        ProtocolDiscovery protocolsDiscovery = getProtocolDiscovery(config);
        services.put(SERVICE_BONJOUR, getServiceBonjour(protocolsDiscovery));
        services.put(SERVICE_UPNP, getServiceUpnp(protocolsDiscovery));
                
        ProtocolStreaming protocolsStreaming = getProtocolStreaming(config);
        services.put(SERVICE_DLNA,  getServiceReadyDlna(protocolsStreaming));
        services.put(SERVICE_DAAP, getServiceItunes(protocolsStreaming));
        
        Nas nas = new Nas();
        nas.setServices(services);
        
        return nas;
    }
    
    @Override
    public Response setServices(NasConfiguration config, Nas nas) {
        
        StringBuffer message = new StringBuffer();

        com.md.nasutils.model.raidiator4.Response standardResponse = setProtocolStandard(config, nas);
        
        if (!TextUtils.isEmpty(standardResponse.getMessage())) {
            message.append(standardResponse.getMessage()).append("\n");
        }
        
        com.md.nasutils.model.raidiator4.Response streamingResponse = setProtocolStreaming(config, nas);
        
        if (!TextUtils.isEmpty(streamingResponse.getMessage())) {
            message.append(streamingResponse.getMessage()).append("\n");
        }
        
        com.md.nasutils.model.raidiator4.Response discoveryResponse = setProtocolDiscovery(config, nas);
        
        if (!TextUtils.isEmpty(discoveryResponse.getMessage())) {
            message.append(discoveryResponse.getMessage()).append("\n");
        }
                
        Response response = new Response();
        response.setMessage(message.toString());
        
        return response;
    }
    
    @Override
    public Service getFtpServiceDetails(NasConfiguration config) {
        ProtocolStandard protocolStandard = getProtocolStandard(config);
        return getServiceFtp(protocolStandard);
    }
    
    @Override
    public Response recalibrateFan(NasConfiguration config) {
        List<BasicNameValuePair> postData = new ArrayList<>();
        postData.add(new BasicNameValuePair(OPERATION, SET));
        postData.add(new BasicNameValuePair(COMMAND, RECALIBRATE_FAN));
        postData.add(new BasicNameValuePair(PAGE, STATUS));
        postData.add(new BasicNameValuePair(OUTER_TAB, NONE));
        postData.add(new BasicNameValuePair(INNER_TAB, NONE));
        
        com.md.nasutils.model.raidiator4.Response response = mHttpClient.post(
                config, GET_HANDLER, postData,
                com.md.nasutils.model.raidiator4.Response.class);
        
        return new Response(response.getMessage());
    }
    
    @Override
    public Response rescanDlna(NasConfiguration config) {
        List<BasicNameValuePair> postData = new ArrayList<>();
        postData.add(new BasicNameValuePair(OPERATION, SET));
        postData.add(new BasicNameValuePair(COMMAND, RESCAN_DLNA));
        postData.add(new BasicNameValuePair(PAGE, SERVICES));
        
        com.md.nasutils.model.raidiator4.Response response = mHttpClient.post(
                config, GET_HANDLER, postData,
                com.md.nasutils.model.raidiator4.Response.class);
        
        return new Response(response.getMessage());
    }
    
    private ProtocolStandard getProtocolStandard(NasConfiguration config) {
        List<BasicNameValuePair> postData = new ArrayList<>();
        postData.add(new BasicNameValuePair(OPERATION, GET));
        postData.add(new BasicNameValuePair(PAGE, SERVICES));
        postData.add(new BasicNameValuePair(INNER_TAB, NONE));
        postData.add(new BasicNameValuePair(OUTER_TAB, TAB_STANDARD));
        postData.add(new BasicNameValuePair(SERVICE_CIFS_ENABLED, "0"));
        postData.add(new BasicNameValuePair(SERVICE_NFS_ENABLED, "0"));
        postData.add(new BasicNameValuePair(SERVICE_AFP_ENABLED, "0"));
        postData.add(new BasicNameValuePair(SERVICE_FTP_ENABLED, "0"));
        postData.add(new BasicNameValuePair(SERVICE_HTTP_ENABLED, "0"));
        postData.add(new BasicNameValuePair(SERVICE_HTTPS_ENABLED, "0"));
        postData.add(new BasicNameValuePair(SERVICE_RSYNC_ENABLED, "0"));
        postData.add(new BasicNameValuePair(SERVICE_AFP_ADVERTISE_ATALKD, ""));
        postData.add(new BasicNameValuePair(SERVICE_AFP_ADVERTISE_BONJOUR, "0"));
        postData.add(new BasicNameValuePair(SERVICE_FTP_PORT, ""));
        postData.add(new BasicNameValuePair(SERVICE_FTP_PASSIVE_START, ""));
        postData.add(new BasicNameValuePair(SERVICE_FTP_PASSIVE_END, ""));
        postData.add(new BasicNameValuePair(SERVICE_FTP_MASQUERADE_ADDRESS, ""));
        postData.add(new BasicNameValuePair(SERVICE_HTTPS_PORT1, ""));
        postData.add(new BasicNameValuePair(SERVICE_HTTPS_PORT, ""));
        postData.add(new BasicNameValuePair(SERVICE_HTTPS_SSL_KEY_HOST, ""));
        postData.add(new BasicNameValuePair(SERVICE_NFS_THREADS, ""));
        postData.add(new BasicNameValuePair(SERVICE_FTP_MODE, ""));
        postData.add(new BasicNameValuePair(SERVICE_FTP_UPLOAD_RESUME, ""));
        postData.add(new BasicNameValuePair(SERVICE_HTTP_WEBSERVER_SHARE, ""));
        postData.add(new BasicNameValuePair(SERVICE_HTTP_WEBSERVER_SHARE_USE_PASSWORD, ""));
                
        return mHttpClient.post(config, GET_HANDLER, postData, ProtocolStandard.class);
    }
    
    private ProtocolStreaming getProtocolStreaming(NasConfiguration config) {
        List<BasicNameValuePair> postData = new ArrayList<>();
        postData.add(new BasicNameValuePair(OPERATION, GET));
        postData.add(new BasicNameValuePair(PAGE, SERVICES));
        postData.add(new BasicNameValuePair(INNER_TAB, NONE));
        postData.add(new BasicNameValuePair(OUTER_TAB, TAB_STREAMING));
        postData.add(new BasicNameValuePair(SERVICE_DLNA_ENABLED, "0"));
        postData.add(new BasicNameValuePair(SERVICE_DAAP_ENABLED, "0"));
        postData.add(new BasicNameValuePair(SERVICE_DLNA_DATABASE_UPDATE_ENABLED, "0"));
        postData.add(new BasicNameValuePair(SERVICE_DLNA_TIVO_SUPPORT_ENABLED, "0"));
        
        return mHttpClient.post(config, GET_HANDLER, postData,  ProtocolStreaming.class);
    }
    
    private ProtocolDiscovery getProtocolDiscovery(NasConfiguration config) {
        List<BasicNameValuePair> postData = new ArrayList<>();
        postData.add(new BasicNameValuePair(OPERATION, GET));
        postData.add(new BasicNameValuePair(PAGE, SERVICES));
        postData.add(new BasicNameValuePair(INNER_TAB, NONE));
        postData.add(new BasicNameValuePair(OUTER_TAB, TAB_DISCOVERY));
        postData.add(new BasicNameValuePair(SERVICE_UPNP_ENABLED, "0"));
        postData.add(new BasicNameValuePair(SERVICE_BONJOUR_ENABLED, "0"));
        postData.add(new BasicNameValuePair(SERVICE_BONJOUR_ADVERTISE_FRONTVIEW, "0"));
        postData.add(new BasicNameValuePair(SERVICE_BONJOUR_ADVERTISE_PRINTER, "0"));
        postData.add(new BasicNameValuePair(SERVICE_BONJOUR_ADVERTISE_AFP, "0"));

        return mHttpClient.post(config, GET_HANDLER, postData, ProtocolDiscovery.class);
    }
    
    private Service getServiceCifs(ProtocolStandard protocolsStandard) {
        Service service = new Service();
        service.setName(SERVICE_CIFS);
        service.setEnabled(getBool(protocolsStandard.getIsCifsEnabled()));
        service.setEnabledOnUi(getBool(protocolsStandard.getIsCifsEnabledOnUi()));
        
        Map<String, Object> metaData = new HashMap<>();
        service.setMetaData(metaData);

        Map<String, String> options = new HashMap<>();
        service.setOptions(options);
        
        return service;
    }
    
    private Service getServiceNfs(ProtocolStandard protocolsStandard) {
        Service service = new Service();
        service.setName(SERVICE_NFS);
        service.setEnabled(getBool(protocolsStandard.getIsNfsEnabled()));
        service.setEnabledOnUi(getBool(protocolsStandard.getIsNfsEnabledOnUi()));

        Map<String, String> options = new HashMap<>();
        options.put(SERVICE_ATTR_NFS_THREADS, protocolsStandard.getNfsThreads());
        service.setOptions(options);
        
        Map<String, Object> metaData = new HashMap<>();
        metaData.put(SERVICE_ATTR_NFS_THREADS_ENABLED_ON_UI, getBool(protocolsStandard.getIsNfsThreadsEnabledOnUi()));
        service.setMetaData(metaData);
        
        return service;
    }
    
    private Service getServiceAfp(ProtocolStandard protocolsStandard) {
        Service service = new Service();        
        service.setName(SERVICE_AFP);
        service.setEnabled(getBool(protocolsStandard.getIsAfpEnabled()));
        service.setEnabledOnUi(getBool(protocolsStandard.getIsAfpEnabledOnUi()));

        Map<String, String> options = new HashMap<>();
        options.put(SERVICE_ATTR_AFP_ADVERTISE_BONJOUR, protocolsStandard.getIsAfpAdvertiseBonjourEnabled());
        service.setOptions(options);

        Map<String, Object> metaData = new HashMap<>();
        metaData.put(SERVICE_ATTR_AFP_ADVERTISE_BONJOUR_ENABLED_ON_UI, getBool(protocolsStandard.getIsAfpAdvertiseBonjourEnabledOnUi()));
        service.setMetaData(metaData);
        
        return service;
    }
    
    private Service getServiceFtp(ProtocolStandard protocolsStandard) {
        Service service = new Service();
        service.setName(SERVICE_FTP);
        service.setEnabled(getBool(protocolsStandard.getIsFtpEnabled()));       
        service.setEnabledOnUi(getBool(protocolsStandard.getIsFtpEnabledOnUi()));

        Map<String, String> options = new HashMap<>();
        options.put(SERVICE_ATTR_FTP_PASSIVE_START, protocolsStandard.getFtpPassiveStart());
        options.put(SERVICE_ATTR_FTP_PASSIVE_END, protocolsStandard.getFtpPassiveEnd());
        options.put(SERVICE_ATTR_FTP_MASQUERADE_ADDRESS, protocolsStandard.getFtpMasqueradeAddressServer());
        options.put(SERVICE_ATTR_FTP_PORT, protocolsStandard.getFtpPort());
        options.put(SERVICE_ATTR_FTP_MODE, protocolsStandard.getFtpModeServer());
        options.put(SERVICE_ATTR_FTP_UPLOAD_RESUME, protocolsStandard.getFtpUploadResumeServer());
        service.setOptions(options);

        Map<String, Object> metaData = new HashMap<>();
        metaData.put(SERVICE_ATTR_FTP_PASSIVE_START_ENABLED_ON_UI, getBool(protocolsStandard.getIsFtpPassiveStartEnabledOnUi()));
        metaData.put(SERVICE_ATTR_FTP_PASSIVE_END_ENABLED_ON_UI, getBool(protocolsStandard.getIsFtpPassiveEndEnabledOnUi()));
        metaData.put(SERVICE_ATTR_FTP_MASQUERADE_ADDRESS_ENABLED_ON_UI, getBool(protocolsStandard.getIsFtpMasqueradeAddressEnabledOnUi()));
        metaData.put(SERVICE_ATTR_FTP_PORT_ENABLED_ON_UI, getBool(protocolsStandard.getIsFtpPortEnabledOnUi()));
        metaData.put(SERVICE_ATTR_FTP_MODE_ENABLED_ON_UI, getBool(protocolsStandard.getIsFtpModeEnabledOnUi()));
        metaData.put(SERVICE_ATTR_FTP_UPLOAD_RESUME_ENABLED_ON_UI, getBool(protocolsStandard.getIsFtpUploadResumeEnabledOnUi()));
        service.setMetaData(metaData);
        
        return service;
    }
    
    private Service getServiceHttp(ProtocolStandard protocolsStandard) {
        Service service = new Service();
        service.setName(SERVICE_HTTP);
        service.setEnabled(getBool(protocolsStandard.getIsHttpEnabled()));
        service.setEnabledOnUi(getBool(protocolsStandard.getIsHttpEnabledOnUi()));

        Map<String, String> options = new HashMap<>();

        String loginEnabled = protocolsStandard
                .getIsHttpWebserverShareUsePassword() != null ? protocolsStandard
                .getIsHttpWebserverShareUsePassword() : "0";
        options.put(SERVICE_ATTR_HTTP_WEBSERVER_SHARE_USE_PASSWORD, loginEnabled);

        String shareName = protocolsStandard
                .getHttpWebServerShare() != null ? protocolsStandard
                .getHttpWebServerShare() : "0";
        options.put(SERVICE_ATTR_HTTP_WEBSERVER_SHARE, shareName);

        service.setOptions(options);

        Map<String, Object> metaData = new HashMap<>();
        metaData.put(SERVICE_ATTR_HTTP_SHARE_NAMES, new ArrayList<>(protocolsStandard.getWebServerShares().values()));
        metaData.put(SERVICE_ATTR_HTTP_WEBSERVER_SHARE_ENABLED_ON_UI, getBool(protocolsStandard.getIsHttpWebServerShareEnabledOnUi()));
        metaData.put(SERVICE_ATTR_HTTP_WEBSERVER_SHARE_USE_PASSWORD_ENABLED_ON_UI, getBool(protocolsStandard.getIsHttpWebserverShareUsePasswordEnabledOnUi()));
        service.setMetaData(metaData);
        
        return service;
    }
    
    private Service getServiceHttps(ProtocolStandard protocolsStandard) {
        Service service = new Service();
        service.setName(SERVICE_HTTPS);
        service.setEnabled(getBool(protocolsStandard.getIsHttpsEnabled()));
        service.setEnabledOnUi(getBool(protocolsStandard.getIsHttpsEnabledOnUi()));

        Map<String, String> options = new HashMap<>();
        options.put(SERVICE_ATTR_HTTPS_PORT_2, protocolsStandard.getHttpsPort2());
        options.put(SERVICE_ATTR_HTTPS_PORT_1, protocolsStandard.getHttpsPort1());
        options.put(SERVICE_ATTR_HTTPS_SSL_KEY_HOST, protocolsStandard.getHttpsSslKeyHost());
        service.setOptions(options);
        
        Map<String, Object> metaData = new HashMap<>();
        metaData.put(SERVICE_ATTR_HTTPS_PORT_1_ENABLED_ON_UI, getBool(protocolsStandard.getIsHttpsPort1EnabledOnUi()));
        metaData.put(SERVICE_ATTR_HTTPS_PORT_2_ENABLED_ON_UI, getBool(protocolsStandard.getIsHttpsPort2EnabledOnUi()));
        metaData.put(SERVICE_ATTR_HTTPS_SSL_KEY_HOST_ENABLED_ON_UI, getBool(protocolsStandard.getIsHttpsSslKeyHostEnabledOnUi()));
        service.setMetaData(metaData);
        
        return service;
    }
    
    private Service getServiceRsync(ProtocolStandard protocolsStandard) {
        Service service = new Service();
        service.setName(SERVICE_RSYNC);
        service.setEnabled(getBool(protocolsStandard.getIsRsyncEnabled()));
        service.setEnabledOnUi(getBool(protocolsStandard.getIsRsyncEnabledOnUi()));
        
        Map<String, String> options = new HashMap<>();
        service.setOptions(options);
        
        Map<String, Object> metaData = new HashMap<>();
        service.setMetaData(metaData);
        
        return service;
    }
    
    private Service getServiceReadyDlna(ProtocolStreaming protocolsStreaming) {
        Service service = new Service();
        service.setName(SERVICE_DLNA);
        service.setEnabled(getBool(protocolsStreaming.getIsDlnaEnabled()));
        service.setEnabledOnUi(getBool(protocolsStreaming.getIsDlnaEnabledOnUi()));

        Map<String, String> options = new HashMap<>();
        options.put(SERVICE_ATTR_DLNA_DATABASE_UPDATE_ENABLED, protocolsStreaming.getIsDlnaDatabaseUpdateEnabled());
        options.put(SERVICE_ATTR_DLNA_TIVO_SUPPORT_ENABLED, protocolsStreaming.getIsDlnaTivoSupportEnabled());
        service.setOptions(options);

        Map<String, Object> metaData = new HashMap<>();
        metaData.put(SERVICE_ATTR_DLNA_DATABASE_UPDATE_ENABLED_ENABLED_ON_UI, getBool(protocolsStreaming.getIsDlnaDatabaseUpdateEnabledOnUi()));
        metaData.put(SERVICE_ATTR_DLNA_TIVO_SUPPORT_ENABLED_ENABLED_ON_UI, getBool(protocolsStreaming.getIsDlnaTivoSupportEnabledOnUi()));
        service.setMetaData(metaData);
        
        return service;
    }
    
    private Service getServiceItunes(ProtocolStreaming protocolsStreaming) {
        Service service = new Service();        
        service.setName(SERVICE_DAAP);
        service.setEnabled(getBool(protocolsStreaming.getIsItunesStreamingEnabled()));
        service.setEnabledOnUi(getBool(protocolsStreaming.getIsItunesStreamingEnabledOnUi()));

        Map<String, String> options = new HashMap<>();
        service.setOptions(options);
        
        Map<String, Object> metaData = new HashMap<>();
        service.setMetaData(metaData);
        
        return service;
    }
    
    private Service getServiceBonjour(ProtocolDiscovery protocolsDiscovery) {
        Service service = new Service();
        service.setName(SERVICE_BONJOUR);
        service.setEnabled(getBool(protocolsDiscovery.getIsBonjourEnabled()));
        service.setEnabledOnUi(getBool(protocolsDiscovery.getIsBonjourEnabledOnUi()));

        Map<String, String> options = new HashMap<>();
        options.put(SERVICE_ATTR_BONJOUR_ADVERTISE_AFP, protocolsDiscovery.getIsBonjourAdvertiseAfpEnabled());
        options.put(SERVICE_ATTR_BONJOUR_ADVERTISE_FRONTVIEW, protocolsDiscovery.getIsBonjourAdvertiseFrontviewEnabled());
        options.put(SERVICE_ATTR_BONJOUR_ADVERTISE_PRINTER, protocolsDiscovery.getIsBonjourAdvertisePrinterEnabled());
        service.setOptions(options);

        Map<String, Object> metaData = new HashMap<>();
        metaData.put(SERVICE_ATTR_BONJOUR_ADVERTISE_PRINTER_ENABLED_ON_UI, getBool(protocolsDiscovery.getIsBonjourAdvertisePrinterEnabledOnUi()));
        metaData.put(SERVICE_ATTR_BONJOUR_ADVERTISE_AFP_ENABLED_ON_UI, getBool(protocolsDiscovery.getIsBonjourAdvertiseAfpEnabledOnUi()));
        metaData.put(SERVICE_ATTR_BONJOUR_ADVERTISE_FRONTVIEW_ENABLED_ON_UI, getBool(protocolsDiscovery.getIsBonjourAdvertiseFrontviewEnabledOnUi()));
        service.setMetaData(metaData);
        
        return service;
    }
    
    private Service getServiceUpnp(ProtocolDiscovery protocolsDiscovery) {
        Service service = new Service();        
        service.setName(SERVICE_UPNP);
        service.setEnabled(getBool(protocolsDiscovery.getIsUpnpEnabled()));
        service.setEnabledOnUi(getBool(protocolsDiscovery.getIsUpnpEnabledOnUi()));
        
        Map<String, String> options = new HashMap<>();
        service.setOptions(options);
        
        Map<String, Object> metaData = new HashMap<>();
        service.setMetaData(metaData);
        
        return service;
    }
    
    private com.md.nasutils.model.raidiator4.Response setProtocolStandard(NasConfiguration config, Nas nas) {
        Map<String, Service> services = nas.getServices();
        
        List<BasicNameValuePair> postData = new ArrayList<>();
        postData.add(new BasicNameValuePair(OPERATION, SET));
        postData.add(new BasicNameValuePair(PAGE, SERVICES));
        postData.add(new BasicNameValuePair(INNER_TAB, NONE));
        postData.add(new BasicNameValuePair(OUTER_TAB, TAB_STANDARD));
        postData.add(new BasicNameValuePair(COMMAND, "null"));
        
        Service cifs = services.get(SERVICE_CIFS);
        postData.add(new BasicNameValuePair(SERVICE_CIFS_ENABLED, getBinary(cifs.isEnabled())));
        
        Service nfs = services.get(SERVICE_NFS);
        postData.add(new BasicNameValuePair(SERVICE_NFS_ENABLED, getBinary(nfs.isEnabled())));
        postData.add(new BasicNameValuePair(SERVICE_NFS_THREADS, nfs.getOptionByName(SERVICE_ATTR_NFS_THREADS)));
        
        Service afp = services.get(SERVICE_AFP);
        postData.add(new BasicNameValuePair(SERVICE_AFP_ENABLED, getBinary(afp.isEnabled())));
        postData.add(new BasicNameValuePair(SERVICE_AFP_ADVERTISE_ATALKD, "0")); // not used
        postData.add(new BasicNameValuePair(SERVICE_AFP_ADVERTISE_BONJOUR, afp.getOptionByName(SERVICE_ATTR_AFP_ADVERTISE_BONJOUR)));
        
        Service ftp = services.get(SERVICE_FTP);
        postData.add(new BasicNameValuePair(SERVICE_FTP_ENABLED, getBinary(ftp.isEnabled())));
        postData.add(new BasicNameValuePair(SERVICE_FTP_PORT, ftp.getOptionByName(SERVICE_ATTR_FTP_PORT)));
        postData.add(new BasicNameValuePair(SERVICE_FTP_PASSIVE_START, ftp.getOptionByName(SERVICE_ATTR_FTP_PASSIVE_START)));
        postData.add(new BasicNameValuePair(SERVICE_FTP_PASSIVE_END, ftp.getOptionByName(SERVICE_ATTR_FTP_PASSIVE_END)));
        postData.add(new BasicNameValuePair(SERVICE_FTP_MASQUERADE_ADDRESS, ftp.getOptionByName(SERVICE_ATTR_FTP_MASQUERADE_ADDRESS)));
        postData.add(new BasicNameValuePair(SERVICE_FTP_MODE, ftp.getOptionByName(SERVICE_ATTR_FTP_MODE)));
        postData.add(new BasicNameValuePair(SERVICE_FTP_UPLOAD_RESUME, ftp.getOptionByName(SERVICE_ATTR_FTP_UPLOAD_RESUME)));
        
        Service http = services.get(SERVICE_HTTP);
        postData.add(new BasicNameValuePair(SERVICE_HTTP_ENABLED, getBinary(http.isEnabled())));
        postData.add(new BasicNameValuePair(SERVICE_HTTP_WEBSERVER_SHARE, http.getOptionByName(SERVICE_ATTR_HTTP_WEBSERVER_SHARE)));
        postData.add(new BasicNameValuePair(SERVICE_HTTP_WEBSERVER_SHARE_USE_PASSWORD, http.getOptionByName(SERVICE_ATTR_HTTP_WEBSERVER_SHARE_USE_PASSWORD)));      
        
        Service https = services.get(SERVICE_HTTPS);
        postData.add(new BasicNameValuePair(SERVICE_HTTPS_ENABLED, getBinary(https.isEnabled())));
        postData.add(new BasicNameValuePair(SERVICE_HTTPS_PORT1, https.getOptionByName(SERVICE_ATTR_HTTPS_PORT_1)));
        postData.add(new BasicNameValuePair(SERVICE_HTTPS_PORT, https.getOptionByName(SERVICE_ATTR_HTTPS_PORT_2)));
        postData.add(new BasicNameValuePair(SERVICE_HTTPS_SSL_KEY_HOST, https.getOptionByName(SERVICE_ATTR_HTTPS_SSL_KEY_HOST)));
        
        Service rsync = services.get(SERVICE_RSYNC);
        postData.add(new BasicNameValuePair(SERVICE_RSYNC_ENABLED, getBinary(rsync.isEnabled())));
        
        return mHttpClient.post(config, GET_HANDLER, postData, com.md.nasutils.model.raidiator4.Response.class);
    }
    
    private com.md.nasutils.model.raidiator4.Response setProtocolStreaming(NasConfiguration config, Nas nas) {
        Map<String, Service> services = nas.getServices();
        
        List<BasicNameValuePair> postData = new ArrayList<>();
        postData.add(new BasicNameValuePair(OPERATION, SET));
        postData.add(new BasicNameValuePair(PAGE, SERVICES));
        postData.add(new BasicNameValuePair(INNER_TAB, NONE));
        postData.add(new BasicNameValuePair(OUTER_TAB, TAB_STREAMING));
        postData.add(new BasicNameValuePair(COMMAND, "null"));
        
        Service upnpav = services.get(SERVICE_DLNA);
        postData.add(new BasicNameValuePair(SERVICE_DLNA_ENABLED, getBinary(upnpav.isEnabled())));
        postData.add(new BasicNameValuePair(SERVICE_DLNA_MODIFIED, "1")); // changes won't apply unless this is specified
        postData.add(new BasicNameValuePair(SERVICE_DLNA_DATABASE_UPDATE_ENABLED, upnpav.getOptionByName(SERVICE_ATTR_DLNA_DATABASE_UPDATE_ENABLED)));
        postData.add(new BasicNameValuePair(SERVICE_DLNA_TIVO_SUPPORT_ENABLED, upnpav.getOptionByName(SERVICE_ATTR_DLNA_TIVO_SUPPORT_ENABLED)));
        
        Service upnp = services.get(SERVICE_DAAP);
        postData.add(new BasicNameValuePair(SERVICE_DAAP_ENABLED, getBinary(upnp.isEnabled())));
        
        return mHttpClient.post(config, GET_HANDLER, postData,  com.md.nasutils.model.raidiator4.Response.class);
    }
    
    private com.md.nasutils.model.raidiator4.Response setProtocolDiscovery(NasConfiguration config, Nas nas) {
        Map<String, Service> services = nas.getServices();
        
        List<BasicNameValuePair> postData = new ArrayList<>();
        postData.add(new BasicNameValuePair(OPERATION, SET));
        postData.add(new BasicNameValuePair(PAGE, SERVICES));
        postData.add(new BasicNameValuePair(INNER_TAB, NONE));
        postData.add(new BasicNameValuePair(OUTER_TAB, TAB_DISCOVERY));
        postData.add(new BasicNameValuePair(COMMAND, "null"));
        
        Service upnp = services.get(SERVICE_UPNP);
        postData.add(new BasicNameValuePair(SERVICE_UPNP_ENABLED, getBinary(upnp.isEnabled())));
        
        Service bonjour = services.get(SERVICE_BONJOUR);
        postData.add(new BasicNameValuePair(SERVICE_BONJOUR_ENABLED, getBinary(bonjour.isEnabled())));
        postData.add(new BasicNameValuePair(SERVICE_BONJOUR_MODIFIED, "1")); // changes won't apply unless this is specified
        postData.add(new BasicNameValuePair(SERVICE_BONJOUR_ADVERTISE_FRONTVIEW, bonjour.getOptionByName(SERVICE_ATTR_BONJOUR_ADVERTISE_FRONTVIEW)));
        postData.add(new BasicNameValuePair(SERVICE_BONJOUR_ADVERTISE_PRINTER, bonjour.getOptionByName(SERVICE_ATTR_BONJOUR_ADVERTISE_PRINTER)));
        postData.add(new BasicNameValuePair(SERVICE_BONJOUR_ADVERTISE_AFP, bonjour.getOptionByName(SERVICE_ATTR_BONJOUR_ADVERTISE_AFP)));
            
        return mHttpClient.post(config, GET_HANDLER, postData, com.md.nasutils.model.raidiator4.Response.class);
    }

    /**
     * Generic method to create post data for reboot and shutdown
     * 
     * @param command typically reboot or shutdown
     * @param forceFsck force file system check on next boot?
     * @param forceQuotaCheck force quota check on next boot?
     * @return list of name/value pairs
     */
    private List<BasicNameValuePair> createPowerPostData(String command,
            boolean forceFsck, boolean forceQuotaCheck) {
        List<BasicNameValuePair> postData = new ArrayList<>();
        postData.add(new BasicNameValuePair(OPERATION, SET));
        postData.add(new BasicNameValuePair(PAGE, SYSTEM));
        postData.add(new BasicNameValuePair(OUTER_TAB, TAB_SHUTDOWN));
        postData.add(new BasicNameValuePair(INNER_TAB, NONE));
        postData.add(new BasicNameValuePair(SHUTDOWN_OPTION_2, TRUE));
        postData.add(new BasicNameValuePair(COMMAND, command));
        
        if (forceFsck) {
            postData.add(new BasicNameValuePair(FORCE_FILE_SYSTEM_CHECK, TRUE));
        }
        
        if (forceQuotaCheck) {
            postData.add(new BasicNameValuePair(FORCE_QUOTA_CHECK, TRUE));
        }
        
        return postData;
    }

    @Override
    public Nas getBackups(NasConfiguration config){
        List<BasicNameValuePair> postData = new ArrayList<>();
        postData.add(new BasicNameValuePair(OPERATION, GET));
        postData.add(new BasicNameValuePair(PAGE, PAGE_BACKUP_STATUS));
        postData.add(new BasicNameValuePair(OUTER_TAB, TAB_BACKUP_JOBS));
        postData.add(new BasicNameValuePair(INNER_TAB, NONE));

        BackupStatus payload = mHttpClient.post(config, GET_HANDLER, postData, BackupStatus.class);

        List<BackupJob> backupJobs = new ArrayList<>();

        for(com.md.nasutils.model.raidiator4.BackupJob b : payload.getBackupJobs()) {
            BackupJob job = new BackupJob();
            job.setBackupId(b.getId());
            job.setName(b.getJob());
            job.setStatus(getStatusCode(b.getStatus()));
            job.setStatusDescription(b.getStatusDescription());
            job.setSchedule(b.getWhen());
            job.setSource(b.getSource());
            job.setDestination(b.getDest());
            backupJobs.add(job);
        }

        Nas nas = new Nas();
        nas.setBackupJobs(backupJobs);

        return nas;
    }

    @Override
    public Response startBackup(NasConfiguration config, String backupJob) {
        List<BasicNameValuePair> postData = new ArrayList<>();
        postData.add(new BasicNameValuePair(OPERATION, SET));
        postData.add(new BasicNameValuePair(COMMAND, GO));
        postData.add(new BasicNameValuePair(PAGE, PAGE_BACKUP));
        postData.add(new BasicNameValuePair(OUTER_TAB, TAB_BACKUP_JOBS));
        postData.add(new BasicNameValuePair(INNER_TAB, NONE));
        postData.add(new BasicNameValuePair(CURRENT_BACKUP_JOB, backupJob));

        com.md.nasutils.model.raidiator4.Response response =
                mHttpClient.post(config, GET_HANDLER, postData,
                        com.md.nasutils.model.raidiator4.Response.class);

        return new Response(response.getStatus());
    }

    @Override
    public Nas getAddOns(NasConfiguration config) {
        List<BasicNameValuePair> postData = new ArrayList<>();
        postData.add(new BasicNameValuePair(OPERATION, GET));
        postData.add(new BasicNameValuePair(PAGE, PAGE_NASSTATE));
        postData.add(new BasicNameValuePair(GET_COMMAND, GET_ADDON_LIST));

        AddOns payload = mHttpClient.post(config, GET_HANDLER, postData, AddOns.class);

        Map<String, com.md.nasutils.model.AddOn> addOns = new HashMap<>();

        for(AddOn a : payload.getAddOns()) {
            com.md.nasutils.model.AddOn addOn = new com.md.nasutils.model.AddOn();
            addOn.setId(a.getName());

            if (a.getStatus() != null) {
                addOn.setStatus(a.getStatus().toUpperCase());
            }

            addOn.setName(a.getDisplayName());
            addOn.setVersion(a.getVersion());
            addOns.put(a.getName(), addOn);
        }

        Nas nas = new Nas();
        nas.setAddOns(addOns);

        return nas;
    }

    @Override
    public Response toggleAddOns(NasConfiguration config, List<com.md.nasutils.model.AddOn> addOns) {
        Resources resources = NasUtilsApplication.getContext().getResources();
        Response r = new Response(resources.getString(R.string.success));

        for (com.md.nasutils.model.AddOn a : addOns) {
            // Loop through and request changes 1-by-1,
            // unfortunately nothing better we can do at this time
            String url = null;

            if ("READYNAS_PHOTOS_II".equals(a.getId())) {
                // for whatever reason, ReadyNAS Photos II plays by its own rules
                url = "ON".equals(a.getStatus()) ? GET_TOGGLE_RNP2_ON : GET_TOGGLE_RNP2_OFF;
            } else {
                // most, if not all addons are generated by a template and follow
                // the same URL patterns. rather than make an extra request to retrieve
                // the "handler" URL, we assume this pattern
                url = "ON".equals(a.getStatus()) ? GET_TOGGLE_ADDON_ON : GET_TOGGLE_ADDON_OFF;
                url = url.replaceAll("\\$\\{SERVICE\\}", a.getId());
            }

            com.md.nasutils.model.raidiator4.Response response =
                    mHttpClient.get(config, url, com.md.nasutils.model.raidiator4.Response.class);

            if (!"success".equals(response.getStatus())) {
                // return after the first failure
                r.setMessage(response.getMessage());
                break;
            }
        }

        return r;
    }

    // We can get extra details for each addon using this method
    // It's a shame this isn't available all in one call
    public AddOnDetail getAddOn(NasConfiguration config, String name) {
        String url = GET_ADDON + "/" + name + "/" + name + ".xml";
        return mHttpClient.get(config, url, AddOnDetail.class);
    }

    private Date getDate(String dateString) {

        Date retVal = null;
        
        if(!TextUtils.isEmpty(dateString)) {
            try {
                // Step 1: Try with current Locale
                retVal = mFormat.parse(dateString);
            } catch (ParseException pe1) {
                android.util.Log.i(TAG, "Could not parse with Locale: " + mLocale.toString());
                
                // Step 2: If we could not parse with current Locale, 
                // iterate through available Locales and try and find the right one
                for (Locale loc : SimpleDateFormat.getAvailableLocales()) {
                    mLocale = loc;
                    android.util.Log.i(TAG, "Trying Locale: " + mLocale.toString());
                    try {
                        mFormat = new SimpleDateFormat(DATE_FORMAT, mLocale);
                        retVal = mFormat.parse(dateString);
                    } catch (ParseException pe2) {
                        continue;
                    }
                    
                    if (retVal != null) {
                        break;
                    }
                }
            }
            
            if (retVal == null) {
                mHasMatchingLocale = false;
            }
        }
        
        return retVal;
    }

    private boolean getBool(int i) {
        return i == 1 ? true : false;
    }
    
    private boolean getBool(String s) {
        if (!TextUtils.isEmpty(s)) {
            return ENABLED.equals(s);
        }
        return false;
    }
    
    private String getBinary(boolean i) {
        return i == true ? "1" : "0";
    }

    public String getStatusCode(String string) {
        int start = string.indexOf("<img src=\"") + 10;
        int end = string.indexOf("\">", start);
        String substr = string.substring(start, end);

        if (Raidiator4Constants.LED_ERROR.contains(substr)) {
            return "ERROR";
        } else if (Raidiator4Constants.LED_WARNING.contains(substr)) {
            return "WARN";
        } else {
            return "OK";
        }
    }
}
