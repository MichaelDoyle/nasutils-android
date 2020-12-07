/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service.readynas;

import android.content.res.Resources;

import com.md.nasutils.NasUtilsApplication;
import com.md.nasutils.R;
import com.md.nasutils.model.AddOn;
import com.md.nasutils.model.BackupJob;
import com.md.nasutils.model.Dictionary;
import com.md.nasutils.model.Disk;
import com.md.nasutils.model.Enclosure;
import com.md.nasutils.model.Fan;
import com.md.nasutils.model.Language;
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
import com.md.nasutils.model.os6.AggregateCollection;
import com.md.nasutils.model.os6.Application;
import com.md.nasutils.model.os6.BackupJobCollection;
import com.md.nasutils.model.os6.Command;
import com.md.nasutils.model.os6.CustomRequest;
import com.md.nasutils.model.os6.DiskCollection;
import com.md.nasutils.model.os6.DiskEnclosure;
import com.md.nasutils.model.os6.DiskEnclosureCollection;
import com.md.nasutils.model.os6.EnclosureHealth;
import com.md.nasutils.model.os6.Error;
import com.md.nasutils.model.os6.GetRequest;
import com.md.nasutils.model.os6.HealthCollection;
import com.md.nasutils.model.os6.LocalAppCollection;
import com.md.nasutils.model.os6.LogCollection;
import com.md.nasutils.model.os6.Network;
import com.md.nasutils.model.os6.NetworkLink;
import com.md.nasutils.model.os6.NetworkLinkCollection;
import com.md.nasutils.model.os6.Nml;
import com.md.nasutils.model.os6.Options;
import com.md.nasutils.model.os6.PageQualifier;
import com.md.nasutils.model.os6.Protocol;
import com.md.nasutils.model.os6.ProtocolCollection;
import com.md.nasutils.model.os6.QueryRequest;
import com.md.nasutils.model.os6.Request;
import com.md.nasutils.model.os6.SetRequest;
import com.md.nasutils.model.os6.Share;
import com.md.nasutils.model.os6.Shutdown;
import com.md.nasutils.model.os6.SmartInfo;
import com.md.nasutils.model.os6.SystemInfo;
import com.md.nasutils.model.os6.Transaction;
import com.md.nasutils.model.os6.UpsHealth;
import com.md.nasutils.model.os6.VirtualNetworkLink;
import com.md.nasutils.model.os6.VirtualNetworkLinkCollection;
import com.md.nasutils.model.os6.VolumeCollection;
import com.md.nasutils.service.http.HttpClient;
import com.md.nasutils.service.http.NasConfiguration;
import com.md.nasutils.util.UnitConversionUtils;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;

import java.io.StringWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_HTTP_SHARE_NAMES;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_SNMP_CONTACT;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_SNMP_LOCATION;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_FTP;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_HTTP;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_HTTPS;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_READY_CLOUD;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_REPLICATE;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_SNMP;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SUFFIX_ENABLED_ON_UI;

/**
 * XML based implementation for {@link ReadyNasService}. 
 * Works with ReadyNAS OS6 and ReadyDATA OS devices.
 * 
 * @author michaeldoyle
 */
public class Os6ReadyNasService implements ReadyNasService {

    @SuppressWarnings("unused")
    private static final String TAG = Os6ReadyNasService.class.getSimpleName();

    private static final String PROLOG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    
    private Serializer mSerializer;
    
    private HttpClient mHttpClient;
    
    public Os6ReadyNasService() {
        mHttpClient = new HttpClient();
        Format format = new Format(0, PROLOG); 
        mSerializer = new Persister(new AnnotationStrategy(), format);
    }

    @Override
    public Nas getStatus(NasConfiguration config) {
        
        Resources resources = NasUtilsApplication.getContext().getResources();
        
        Nml document = new Nml();
        document.setDst("nas_utils");
        document.setSrc("nas");

        Transaction transaction = new Transaction();
        document.setTransaction(transaction);
        transaction.setId("njl_id_1");
        
        List<Request> requests = new ArrayList<>();
        transaction.setRequests(requests);
        
        Request request = new GetRequest();
        request.setId("njl_id_2");
        request.setResourceId("SystemInfo");
        request.setResourceType("SystemInfo");
        requests.add(request);
        
        request = new GetRequest();
        request.setId("njl_id_3");
        request.setResourceId("Network");
        request.setResourceType("Network");
        requests.add(request);
        
        request = new GetRequest();
        request.setId("njl_id_4");
        request.setResourceId("network_link_list");
        request.setResourceType("network_link_collection");
        requests.add(request);
        
        request = new GetRequest();
        request.setId("njl_id_5");
        request.setResourceId("HealthInfo");
        request.setResourceType("Health_Collection");
        requests.add(request);
        
        request = new GetRequest();
        request.setId("njl_id_6");
        request.setResourceId("Volumes");
        request.setResourceType("Volume_Collection");
        requests.add(request);
        
        request = new GetRequest();
        request.setId("njl_id_7");
        request.setResourceId("DiskEnclosure");
        request.setResourceType("DiskEnclosure_Collection");
        requests.add(request);
        
        request = new GetRequest();
        request.setId("njl_id_8");
        request.setResourceId("aggregate");
        request.setResourceType("aggregate_collection");
        requests.add(request);
        
        StringWriter writer = new StringWriter();
        try {
            mSerializer.write(document, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Nml nml = mHttpClient.post(config, "/dbbroker", writer.toString(), Nml.class);
        
        Nas nas = new Nas();
        
        SystemInfo systemInfo = (SystemInfo) nml.getTransaction()
                .getResponse("njl_id_2").getResult().getData();
        nas.setSerialNumber(systemInfo.getSerial());
        nas.setModel(systemInfo.getModel());
        nas.setFirmware(systemInfo.getFirmwareName() + " " + systemInfo.getFirmwareVersion());
        nas.setMemory(systemInfo.getMemory() + " MB");
        nas.setArchitecture(resources.getString(R.string.error_unavailable));
        
        Network network = (Network) nml.getTransaction()
                .getResponse("njl_id_3").getResult().getData();
        nas.setHostname(network.getHostname());

        NetworkLinkCollection networkLinks = (NetworkLinkCollection) nml
                .getTransaction().getResponse("njl_id_4").getResult().getData();
        
        List<NetworkInterface> ifaces = new ArrayList<>();
        for (NetworkLink link : networkLinks.getNetworkLinks()) {
            NetworkInterface iface = new NetworkInterface();
            iface.setInterfaceName(link.getLink());
            iface.setInterfaceType(resources.getString(R.string.network_interface_physical));
            iface.setIpAddress(link.getIpAddress());
            iface.setIpAddressType(link.getIpType());
            iface.setIpv6Address(link.getIpv6Address());
            iface.setIpv6AddressType(link.getIpv6type());
            iface.setMacAddress(link.getMacAddress());
            iface.setStatus(link.getState().toUpperCase(Locale.US));
            ifaces.add(iface);
            
            VirtualNetworkLinkCollection virtualLinks = link.getVirtualNetworkLinkCollection();
            if (virtualLinks != null) {
                List<VirtualNetworkLink> vnics = virtualLinks.getVirtualNetworkLinks();
                for (VirtualNetworkLink vnic : vnics) {
                    NetworkInterface viface = new NetworkInterface();
                    viface.setInterfaceName(vnic.getLink());
                    viface.setInterfaceType(resources.getString(R.string.network_interface_virtual));
                    viface.setIpAddress(vnic.getIpAddress());
                    viface.setIpAddressType(vnic.getIpType());
                    viface.setIpv6Address(vnic.getIpv6Address());
                    viface.setMacAddress(vnic.getMacAddress());
                    viface.setStatus(vnic.getState().toUpperCase(Locale.US));
                    viface.setOver(vnic.getOver());
                    ifaces.add(viface);
                }
            }
        }
        
        AggregateCollection aggregateLinks = (AggregateCollection) nml
                .getTransaction().getResponse("njl_id_8").getResult().getData();
        
        for (NetworkLink link : aggregateLinks.getAggregateNetworkLinks()) {
            NetworkInterface iface = new NetworkInterface();
            
            // all ifaces
            iface.setInterfaceName(link.getLink());
            iface.setInterfaceType(resources.getString(R.string.network_interface_aggregate));
            iface.setIpAddress(link.getIpAddress());
            iface.setIpAddressType(link.getIpType());
            iface.setIpv6Address(link.getIpv6Address());
            iface.setIpv6AddressType(link.getIpv6type());
            iface.setMacAddress(link.getMacAddress());
            iface.setStatus(link.getState().toUpperCase(Locale.US));
            
            // specific to bonded ifaces
            iface.setMode(link.getMode());
            iface.setOver(link.getOver());
            
            ifaces.add(iface);
        }
        
        nas.setNetworkInterfaces(ifaces);

        HealthCollection healthCollection = (HealthCollection) nml
                .getTransaction().getResponse("njl_id_5").getResult().getData();
        List<EnclosureHealth> enclosures = healthCollection.getEnclosureHealths();
        
        nas.setEnclosures(new ArrayList<Enclosure>());
        for (EnclosureHealth enclosure : enclosures) {
            Enclosure enc = new Enclosure();
            
            enc.setFans(new ArrayList<Fan>());
            for (com.md.nasutils.model.os6.Fan fan : enclosure.getFans()) {
                Fan f = new Fan();
                f.setId(fan.getDeviceId());
                String systemFan = resources.getString(R.string.fan_system);
                f.setLabel("Fan".equals(fan.getLabel()) ? systemFan : fan.getLabel());
                f.setName(fan.getDeviceName());
                f.setSpeed(fan.getSpeed());
                f.setSpeedMax(fan.getNormalMax());
                f.setSpeedMin(fan.getNormalMin());
                f.setStatus(fan.getStatus().toUpperCase(Locale.US));
                enc.getFans().add(f);
            }
            
            enc.setTemperatures(new ArrayList<Temperature>());
            for (com.md.nasutils.model.os6.Temperature temp : enclosure.getTemperatures()) {
                Temperature t = new Temperature();
                t.setId(temp.getDeviceId());
                t.setLabel(temp.getLabel());
                t.setName(temp.getDeviceName());
                t.setStatus(temp.getStatus().toUpperCase(Locale.US));
                t.setTemperatureMax(temp.getMaxTemperature());
                t.setTemperatureMin(temp.getMinTemperature());
                t.setTemperature(temp.getTemperature());
                enc.getTemperatures().add(t);
            }
            
            DiskEnclosureCollection diskEnclosureColl = (DiskEnclosureCollection) nml
                    .getTransaction().getResponse("njl_id_7").getResult().getData();
            
            DiskEnclosure diskEnclosure = diskEnclosureColl.getDiskEnclosure(enclosure.getResourceId());
            DiskCollection enclosureDisks = diskEnclosure.getDiskCollection();

            enc.setDisks(new ArrayList<Disk>());
            for (com.md.nasutils.model.os6.Disk encDisk : enclosureDisks.getDisks()) {
                Disk d = new Disk();    
                d.setCapacityGb(UnitConversionUtils.bytesToGigabytes(encDisk.getCapacityBytesAttribute()));
                d.setLabel(diskEnclosure.getName() + ": " + encDisk.getResourceType() + encDisk.getChannel());
                d.setModel(encDisk.getModelAttribute());
                d.setName(encDisk.getResourceId());
                d.setChannel(encDisk.getChannel());
                d.setStatus(encDisk.getDiskState().toUpperCase(Locale.US));
                d.setFirmwareVersion(encDisk.getFirmwareVersion());
                d.setSerialNumber(encDisk.getSerialNumber());       
                enc.getDisks().add(d);
                
                com.md.nasutils.model.os6.Disk disk = enclosure.getDisk(encDisk.getResourceId());
                if (disk!= null) {
                    d.setId(disk.getDeviceId());
                    d.setTemperatureMin(1);
                    d.setTemperatureMax(80);
                    d.setTemperature(disk.getTemperature());
                }
            }
            
            nas.getEnclosures().add(enc);
        }
        
        VolumeCollection volumeCollection = (VolumeCollection) nml
                .getTransaction().getResponse("njl_id_6").getResult().getData();
        
        nas.setVolumes(new ArrayList<Volume>());
        for (com.md.nasutils.model.os6.Volume volume : volumeCollection.getVolumes()) {
            Volume vol = new Volume();
            vol.setName(volume.getVolumeName());
            vol.setAutoExpand(volume.getAutoExpand());
            vol.setAutoReplace(volume.getAutoReplace());
            vol.setDelegation(volume.getDelegation());
            vol.setGuid(volume.getGuid());
            vol.setStatus(volume.getHealth());
            vol.setHealth(volume.getHealth());
            vol.setChecksum(volume.getChecksum());
            vol.setRaidLevel(systemInfo.getRaidLevel()); // for OS6, should this really be here?
            
            double allocatedGb = UnitConversionUtils.megabytesToGigabytes(volume.getAllocatedMegabytes());
            vol.setAllocatedGb(allocatedGb);

            double freeGb = UnitConversionUtils.megabytesToGigabytes(volume.getFreeMegabytes());
            vol.setFreeGb(freeGb);

            double capacityGb = UnitConversionUtils.megabytesToGigabytes(volume.getCapacityMegabytes());
            vol.setCapacityGb(capacityGb);

            double availableGb = UnitConversionUtils.megabytesToGigabytes(volume.getAvailableMegabytes());
            vol.setAvailableGb(availableGb);            

            double usedMb = volume.getCapacityMegabytes() - volume.getFreeMegabytes();
            double usedGb = UnitConversionUtils.megabytesToGigabytes(usedMb);
            double percentageUsed = (usedMb / volume.getCapacityMegabytes()) * 100;
            
            if (percentageUsed < 0) {
                // except for data quality issues, this really shouldn't happen
                percentageUsed = 0;
            }
            
            vol.setPercentageUsed((int) percentageUsed);
            
            NumberFormat format = NumberFormat.getNumberInstance();
            format.setMaximumFractionDigits(2);
            
            String usageText = resources.getString(R.string.volume_used);
            usageText = usageText.replace("{1}", format.format(usedGb) + "");
            usageText = usageText.replace("{2}", format.format(percentageUsed));
            usageText = usageText.replace("{3}", format.format(capacityGb) + "");
            vol.setUsageText(usageText);
            
            nas.getVolumes().add(vol);
            
            // ref to disks?    
        }
        
        List<UpsHealth> upsHealths = healthCollection.getUpsHealths();
        
        nas.setUps(new ArrayList<Ups>());
        for (UpsHealth upsHealth : upsHealths) {
            for (com.md.nasutils.model.os6.Ups ups : upsHealth.getUpses()) {
                Ups u = new Ups();
                u.setId(ups.getResourceId());
                u.setLabel(ups.getDeviceName());
                u.setName(ups.getDeviceName());
                u.setModel(ups.getModel());
                u.setBatteryCharge(ups.getBatteryCharge());
                u.setBatteryRuntime(ups.getBatteryRuntime());
                u.setStatus(ups.getStatus().toUpperCase(Locale.US));
                nas.getUps().add(u);
            }
        }
        
        return nas;
    }

    @Override
    public Logs getStatusLogs(NasConfiguration config) {
        Nml document = new Nml();
        document.setDst("nas_utils");
        document.setSrc("nas");

        Transaction transaction = new Transaction();
        document.setTransaction(transaction);
        transaction.setId("njl_id_1");
        
        List<Request> requests = new ArrayList<>();
        transaction.setRequests(requests);
        QueryRequest request = new QueryRequest();
        request.setId("njl_id_2");
        request.setResourceType("Log_Collection");
        
        PageQualifier qualifier = new PageQualifier();
        qualifier.setDirection(1);
        request.setQualifier(qualifier);
        
        requests.add(request);

        StringWriter writer = new StringWriter();
        try {
            mSerializer.write(document, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Nml nml = mHttpClient.post(config, "/dbbroker", writer.toString(), Nml.class);
        
        LogCollection logCollection = (LogCollection) nml.getTransaction()
                .getResponse("njl_id_2").getResult().getData();

        List<Log> logArrayList = new ArrayList<>();
        for (com.md.nasutils.model.os6.Log l : logCollection.getLogs()) {
            Log log = new Log();
            log.setDescription(l.getMessageTag());
            log.setTime(new Date(l.getTimestamp() * 1000));
            log.setId(l.getId());
            log.setLogLevel(LogLevel.getLogLevel(l.getSeverity()));
            log.setArgs(new HashMap<String, String>());
            if (l.getArgs() != null) {
                log.getArgs().putAll(l.getArgs().getArgs());
            }
            logArrayList.add(log);
        }

        Logs logs = new Logs();
        logs.setLogs(logArrayList.subList(0, Math.min(logArrayList.size(), 200)));

        return logs;
    }

    @Override
    public Response shutdown(NasConfiguration config, boolean forceFsck,
            boolean forceQuotaCheck) {
        return sendShutdown(config, forceFsck, true);
    }

    @Override
    public Response restart(NasConfiguration config, boolean forceFsck,
            boolean forceQuotaCheck) {
        return sendShutdown(config, forceFsck, false);
    }
    
    public Response sendShutdown(NasConfiguration config, boolean forceFsck, boolean halt) {
        Nml document = new Nml();
        document.setDst("nas_utils");
        document.setSrc("nas");

        Transaction transaction = new Transaction();
        document.setTransaction(transaction);
        transaction.setId("njl_id_1");
        
        List<Request> requests = new ArrayList<>();
        transaction.setRequests(requests);
        CustomRequest request = new CustomRequest();
        request.setId("njl_id_2");
        request.setResourceId("Shutdown");
        request.setResourceType("System");
        request.setName("Halt");
        requests.add(request);

        Shutdown shutdown = new Shutdown();
        request.setShutdown(shutdown);
        shutdown.setFsck(forceFsck ? "true" : "false");
        shutdown.setHalt(halt ? "true" : "false");
        
        StringWriter writer = new StringWriter();
        try {
            mSerializer.write(document, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Nml nml = mHttpClient.post(config, "/dbbroker", writer.toString(), Nml.class);
        
        com.md.nasutils.model.os6.Response response = nml.getTransaction().getResponse("njl_id_2");
        
        return new Response(response.getStatus());
    }


    @Override
    public Response locateDisk(NasConfiguration config, String disk) {
        throw new UnsupportedOperationException("Not available for this device.");
    }

    @Override
    public Disk getSmartDiskInfo(NasConfiguration config, String disk,  String deviceName) {
        
        Nml document = new Nml();
        document.setDst("nas_utils");
        document.setSrc("nas");

        Transaction transaction = new Transaction();
        document.setTransaction(transaction);
        transaction.setId("njl_id_1");
        
        List<Request> requests = new ArrayList<>();
        transaction.setRequests(requests);
        
        Request request = new GetRequest();
        request.setId("njl_id_2");
        
        // Although we request a specific resourceId, OS6 ignores it.
        // We'll get all SMART disks back.
        request.setResourceId(disk);
        
        request.setResourceType("SMARTInfo");
        requests.add(request);
        
        StringWriter writer = new StringWriter();
        try {
            mSerializer.write(document, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Nml nml = mHttpClient.post(config, "/dbbroker", writer.toString(), Nml.class);
                        
        Map<String, String> diskData = new LinkedHashMap<>();
        Disk retDisk = new Disk();
        
        com.md.nasutils.model.os6.Response response = nml.getTransaction().getResponse("njl_id_2");
        
        // ReadyDATA does not currently return SMART info
        if (response != null) {
            SmartInfo smartInfo = (SmartInfo) response.getResult().getData();
        
            // Loop through the SMART disks for the specific device 
            // we are after, and parse out the relevant values.
            String[] smartDisks = smartInfo.getSmartInfo().split("\n\n");
            for (int i=0; i < smartDisks.length; i++) {
                String[] lines = smartDisks[i].split("\n");
                if (lines.length > 0 && lines[0].contains(deviceName)) {
                    for(int j = 0; j < lines.length; j++) {
                        String[] keyValue = lines[j].split(":");
                        if (keyValue.length == 2) {
                            diskData.put(keyValue[0], keyValue[1].trim());
                        } else if (keyValue.length == 1) {
                            diskData.put(keyValue[0], "");
                        }
                    }
                    retDisk.setSerialNumber(diskData.get("Serial"));
                    retDisk.setChannel(Integer.parseInt(diskData.get("Channel")));
                    retDisk.setName(diskData.get("Device"));
                    retDisk.setSmartAttributes(diskData);
                    break;
                }
            }
        }
        return retDisk;     
    }
    
    public Dictionary getDictionary(NasConfiguration config) {
        Nml document = new Nml();
        document.setDst("nas_utils");
        document.setSrc("nas");

        Transaction transaction = new Transaction();
        document.setTransaction(transaction);
        transaction.setId("njl_id_1");
        
        List<Request> requests = new ArrayList<>();
        transaction.setRequests(requests);
        
        Request request = new GetRequest();
        request.setId("njl_id_2");
        request.setResourceId("Dictionary");
        request.setResourceType("System");
        requests.add(request);
        
        StringWriter writer = new StringWriter();
        try {
            mSerializer.write(document, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Nml nml = mHttpClient.post(config, "/dbbroker", writer.toString(), Nml.class);
        
        com.md.nasutils.model.os6.Dictionary dictionary = (com.md.nasutils.model.os6.Dictionary) nml.getTransaction()
                .getResponse("njl_id_2").getResult().getData();
        
        Dictionary retDictionary = new Dictionary();
        
        List<Language> languages = new ArrayList<>();
        for (com.md.nasutils.model.os6.Language l : dictionary.getLanguages()) {
            if (l.getItems() != null && l.getItems().size() > 0) {
                Language lang = new Language();
                lang.setCode(l.getCode());
                lang.setName(l.getName());
                
                Map<String, String> items = new HashMap<>();
                for (Map.Entry<String, String> e : l.getItems().entrySet()) {
                    items.put(e.getKey(), e.getValue());
                }
                
                lang.setItems(items);
                languages.add(lang);
            }
        }
        
        retDictionary.setLanguages(languages);
        
        return retDictionary;
    }
    
    @Override
    public Nas getServices(NasConfiguration config) {
        Nml document = new Nml();
        document.setDst("nas_utils");
        document.setSrc("nas");

        Transaction transaction = new Transaction();
        document.setTransaction(transaction);
        transaction.setId("njl_id_1");
        
        List<Request> requests = new ArrayList<>();
        transaction.setRequests(requests);
        
        Request request = new GetRequest();     
        request.setId("njl_id_2");
        request.setResourceId("Network");
        request.setResourceType("Network");
        requests.add(request);
        
        request = new GetRequest();
        request.setId("njl_id_3");
        request.setResourceId("Protocols");
        request.setResourceType("Protocol_Collection");
        requests.add(request);
        
        StringWriter writer = new StringWriter();
        try {
            mSerializer.write(document, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Nml nml = mHttpClient.post(config, "/dbbroker", writer.toString(), Nml.class);
        
        Nas nas = new Nas();
        
        Network network = (Network) nml.getTransaction()
                .getResponse("njl_id_2").getResult().getData();
        nas.setHostname(network.getHostname());

        ProtocolCollection protocolCollection = (ProtocolCollection) nml
                .getTransaction().getResponse("njl_id_3").getResult().getData();
        
        nas.setServices(new LinkedHashMap<String, Service>());
        for(Protocol p : protocolCollection.getProtocols()) {
            if (!SERVICE_READY_CLOUD.equals(p.getId()) && !SERVICE_REPLICATE.equals(p.getId())) {
                Service s = new Service();
                Map<String, String> options = new HashMap<>();
                s.setOptions(options);
                Map<String, Object> metaData = new HashMap<>();
                s.setMetaData(metaData);
                
                s.setName(p.getId());
                s.setEnabled(getBool(p.isEnabled()));
                
                if (SERVICE_HTTP.equals(p.getId()) || SERVICE_HTTPS.equals(p.getId())) {
                    s.setEnabledOnUi(false);
                } else {
                    s.setEnabledOnUi(true);
                }
                
                if (SERVICE_HTTP.equals(p.getId())) {
                    List<String> shares = new ArrayList<>();
                    for (Share share : p.getShares()) {
                        shares.add(share.getName());
                    }
                    metaData.put(SERVICE_ATTR_HTTP_SHARE_NAMES, shares);
                }

                if (p.getOptions() != null) {
                    Map<String, String> opts = p.getOptions().getOptions();
                    for (Map.Entry<String, String> o : opts.entrySet()) {
                        options.put(o.getKey(), o.getValue());
                        metaData.put(o.getKey() + SUFFIX_ENABLED_ON_UI, true);
                    }

                }
                
                nas.getServices().put(p.getId(), s);
            }
        }
                
        return nas;
    }
    
    @Override
    public Response setServices(NasConfiguration config, Nas nas) {
        Nml document = new Nml();
        document.setDst("nas_utils");
        document.setSrc("nas");

        Transaction transaction = new Transaction();
        document.setTransaction(transaction);
        transaction.setId("njl_id_1");
        
        List<Request> requests = new ArrayList<>();
        transaction.setRequests(requests);
        SetRequest request = new SetRequest();
        request.setId("njl_id_2");
        request.setResourceId("Protocols");
        request.setResourceType("Protocol_Collection");
        
        ProtocolCollection pc = new ProtocolCollection();
        request.setDocument(pc);
        requests.add(request);
        
        List<Protocol> protocols = new ArrayList<>();
        pc.setProtocol(protocols);
        
        for (Service s : nas.getServices().values()) {
            Protocol p = new Protocol();
            p.setId(s.getName());
            p.setResourceId(s.getName());
            p.setResourceType("Protocol");
            p.setEnabled(getBinary(s.isEnabled()));

            Options options = new Options();
            Map<String, String> opts = s.getOptions();

            if (s.getName().equals(SERVICE_SNMP)) {
                String contact = opts.get(SERVICE_ATTR_SNMP_CONTACT);
                options.setContact(contact != null ? contact : "");
                opts.remove(SERVICE_ATTR_SNMP_CONTACT);

                String location = opts.get(SERVICE_ATTR_SNMP_LOCATION);
                options.setLocation(location != null ? location : "");
                opts.remove(SERVICE_ATTR_SNMP_LOCATION);
            }

            options.setOptions(opts);
            p.setOptions(options);
            
            protocols.add(p);
        }
        
        StringWriter writer = new StringWriter();
        try {
            mSerializer.write(document, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Nml nml = mHttpClient.post(config, "/dbbroker", writer.toString(), Nml.class);
        
        com.md.nasutils.model.os6.Response response = nml.getTransaction().getResponse("njl_id_2");
        
        StringBuilder message = new StringBuilder(response.getStatus());
        Error error = response.getError();
        if (error!=null) {
            message.append(": ").append(error.getErrorDetails());
        }
        
        return new Response(message.toString());
    }
    
    @Override
    public Service getFtpServiceDetails(NasConfiguration config) {
        Nml document = new Nml();
        document.setDst("nas_utils");
        document.setSrc("nas");

        Transaction transaction = new Transaction();
        document.setTransaction(transaction);
        transaction.setId("njl_id_1");
        
        List<Request> requests = new ArrayList<>();
        transaction.setRequests(requests);
        
        Request request = new GetRequest();
        request.setId("njl_id_2");
        request.setResourceId("Protocols");
        request.setResourceType("Protocol_Collection");
        requests.add(request);
        
        StringWriter writer = new StringWriter();
        try {
            mSerializer.write(document, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Nml nml = mHttpClient.post(config, "/dbbroker", writer.toString(), Nml.class);
        
        ProtocolCollection protocolCollection = (ProtocolCollection) nml
                .getTransaction().getResponse("njl_id_2").getResult().getData();
        
        Protocol p = protocolCollection.getProtocol(SERVICE_FTP);

        Service s = new Service();
        s.setName(p.getId());
        s.setEnabled(getBool(p.isEnabled()));

        Map<String, String> options = new HashMap<>();
        s.setOptions(options);
        
        Map<String, Object> metaData = new HashMap<>();
        s.setMetaData(metaData);

        if (p.getOptions() != null) {
            Map<String, String> opts = p.getOptions().getOptions();
            for (Map.Entry<String, String> o : opts.entrySet()) {
                options.put(o.getKey(), o.getValue());
                metaData.put(o.getKey() + SUFFIX_ENABLED_ON_UI, true);
            }
        }
        
        return s;
    }
    
    @Override
    public Response recalibrateFan(NasConfiguration config) {
        throw new UnsupportedOperationException("Not available for this device.");
    }
    
    @Override
    public Response rescanDlna(NasConfiguration config) {
        Nml document = new Nml();
        document.setDst("nas_utils");
        document.setSrc("nas");

        Transaction transaction = new Transaction();
        document.setTransaction(transaction);
        transaction.setId("njl_id_1");
        
        List<Request> requests = new ArrayList<>();
        transaction.setRequests(requests);
        Command request = new Command();
        request.setId("njl_id_2");
        request.setResourceId("Protocols");
        request.setResourceType("Protocol");
        request.setCommandName("rescan-dlna");
        requests.add(request);
        
        StringWriter writer = new StringWriter();
        try {
            mSerializer.write(document, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Nml nml = mHttpClient.post(config, "/dbbroker", writer.toString(), Nml.class);
        
        com.md.nasutils.model.os6.Response response = nml.getTransaction().getResponse("njl_id_2");
        
        return new Response(response.getStatus());
    }

    @Override
    public Nas getBackups(NasConfiguration config) {
        Nml document = new Nml();
        document.setDst("nas_utils");
        document.setSrc("nas");

        Transaction transaction = new Transaction();
        document.setTransaction(transaction);
        transaction.setId("njl_id_1");

        List<Request> requests = new ArrayList<>();
        transaction.setRequests(requests);

        Request request = new GetRequest();
        request.setId("njl_id_2");
        request.setResourceId("BackupJobs");
        request.setResourceType("BackupJob_Collection");
        requests.add(request);

        StringWriter writer = new StringWriter();
        try {
            mSerializer.write(document, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Nml nml = mHttpClient.post(config, "/dbbroker", writer.toString(), Nml.class);

        Nas nas = new Nas();
        nas.setBackupJobs(new ArrayList<BackupJob>());

        Transaction trans = nml.getTransaction();

        if (trans.getResponses().size() > 0) {
            // ReadyDATA doesn't return backups, so we need to handle this

            BackupJobCollection backupJobCollection =
                    (BackupJobCollection) trans.getResponse("njl_id_2").getResult().getData();

            for (com.md.nasutils.model.os6.BackupJob b : backupJobCollection.getBackupJobs()) {
                BackupJob job = new BackupJob();
                job.setBackupId(b.getJobId());
                job.setName(b.getJobName());

                String status = b.getStatusResult() != null ? b.getStatusResult() : "NA";
                job.setStatus(status);

                job.setStatusDescription(b.getStatus());
                job.setStatusDate(b.getStatusDate());
                job.setSource(b.getSource());
                job.setDestination(b.getDest());

                if (b.getSchedule() != null) {
                    job.setSchedule(b.getSchedule().getDisplaySchedule());
                }

                nas.getBackupJobs().add(job);
            }
        }

        return nas;
    }

    @Override
    public Nas getAddOns(NasConfiguration config) {
        Nml document = new Nml();
        document.setDst("nas_utils");
        document.setSrc("nas");

        Transaction transaction = new Transaction();
        document.setTransaction(transaction);
        transaction.setId("njl_id_1");

        List<Request> requests = new ArrayList<>();
        transaction.setRequests(requests);

        Request request = new GetRequest();
        request.setId("njl_id_2");
        request.setResourceId("LocalInstalled");
        request.setResourceType("LocalApp_Collection");
        requests.add(request);

        StringWriter writer = new StringWriter();
        try {
            mSerializer.write(document, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Nml nml = mHttpClient.post(config, "/dbbroker", writer.toString(), Nml.class);

        Nas nas = new Nas();
        nas.setAddOns(new HashMap<String, AddOn>());

        Transaction trans = nml.getTransaction();

        LocalAppCollection localAppCollection =
                (LocalAppCollection) trans.getResponse("njl_id_2").getResult().getData();

        for (Application a : localAppCollection.getApplications()) {
            AddOn addOn = new AddOn();
            addOn.setId(a.getResourceId());
            if(a.getOnOff() != null) {
                addOn.setStatus(a.getOnOff().toUpperCase());
            }
            addOn.setName(a.getName());
            addOn.setServiceName(a.getServiceName());
            addOn.setVersion(a.getVersion());
            nas.getAddOns().put(a.getResourceId(), addOn);
        }

        return nas;
    }

    @Override
    public Response toggleAddOns(NasConfiguration config, List<AddOn> addOns) {

        Nml document = new Nml();
        document.setDst("nas_utils");
        document.setSrc("nas");

        Transaction transaction = new Transaction();
        transaction.setId("njl_id_1");
        document.setTransaction(transaction);

        List<Request> requests = new ArrayList<>();
        transaction.setRequests(requests);

        int n = 2;
        for (AddOn a : addOns) {
            SetRequest request = new SetRequest();
            request.setId("njl_id_" + n);
            request.setResourceId(a.getId());
            request.setResourceType("LocalApp");
            requests.add(request);

            Application app = new Application();
            app.setResourceType("LocalApp");
            app.setResourceId(a.getId());
            app.setState("ON".equals(a.getStatus()) ? "running" : "stopped");
            request.setDocument(app);
            n++;
        }

        StringWriter writer = new StringWriter();
        try {
            mSerializer.write(document, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mHttpClient.post(config, "/dbbroker", writer.toString(), Nml.class);

        // From what it seems, as long as calls are well formed,
        // they will always report "success"
        Resources resources = NasUtilsApplication.getContext().getResources();
        return new Response(resources.getString(R.string.success));
    }

    @Override
    public Response startBackup(NasConfiguration config, String backupJob) {
        Nml document = new Nml();
        document.setDst("nas_utils");
        document.setSrc("nas");

        Transaction transaction = new Transaction();
        document.setTransaction(transaction);
        transaction.setId("njl_id_1");

        List<Request> requests = new ArrayList<>();
        transaction.setRequests(requests);
        Command request = new Command();
        request.setId("njl_id_2");
        request.setResourceId(backupJob);
        request.setResourceType("BackupJob");
        request.setCommandName("run");
        requests.add(request);

        StringWriter writer = new StringWriter();
        try {
            mSerializer.write(document, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Nml nml = mHttpClient.post(config, "/dbbroker", writer.toString(), Nml.class);

        com.md.nasutils.model.os6.Response response = nml.getTransaction().getResponse("njl_id_2");

        return new Response(response.getStatus());
    }

    private boolean getBool(int i) {
        return i == 1 ? true : false;
    }
    
    private int getBinary(boolean i) {
        return i == true ? 1 : 0;
    }
}
