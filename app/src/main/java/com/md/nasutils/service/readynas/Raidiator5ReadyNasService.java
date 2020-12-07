/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service.readynas;

import android.content.res.Resources;
import android.text.TextUtils;

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
import com.md.nasutils.model.raidiator4.AddOnDetail;
import com.md.nasutils.model.raidiator5.AddonCollection;
import com.md.nasutils.model.raidiator5.BackupJobCollection;
import com.md.nasutils.model.raidiator5.Command;
import com.md.nasutils.model.raidiator5.CustomRequest;
import com.md.nasutils.model.raidiator5.Error;
import com.md.nasutils.model.raidiator5.FsBrokerRequest;
import com.md.nasutils.model.raidiator5.FsBrokerResponse;
import com.md.nasutils.model.raidiator5.GetRequest;
import com.md.nasutils.model.raidiator5.HealthCollection;
import com.md.nasutils.model.raidiator5.LogCollection;
import com.md.nasutils.model.raidiator5.Network;
import com.md.nasutils.model.raidiator5.NetworkLink;
import com.md.nasutils.model.raidiator5.Nml;
import com.md.nasutils.model.raidiator5.Options;
import com.md.nasutils.model.raidiator5.Protocol;
import com.md.nasutils.model.raidiator5.ProtocolCollection;
import com.md.nasutils.model.raidiator5.Request;
import com.md.nasutils.model.raidiator5.SetRequest;
import com.md.nasutils.model.raidiator5.Share;
import com.md.nasutils.model.raidiator5.Shutdown;
import com.md.nasutils.model.raidiator5.SmartInfo;
import com.md.nasutils.model.raidiator5.SystemInfo;
import com.md.nasutils.model.raidiator5.Transaction;
import com.md.nasutils.model.raidiator5.VolumeCollection;
import com.md.nasutils.service.http.HttpClient;
import com.md.nasutils.service.http.NasConfiguration;
import com.md.nasutils.util.UnitConversionUtils;

import org.apache.http.message.BasicNameValuePair;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.md.nasutils.service.readynas.Raidiator4Constants.DATE_FORMAT;
import static com.md.nasutils.service.readynas.Raidiator4Constants.GET_ADDON;
import static com.md.nasutils.service.readynas.Raidiator4Constants.GET_TOGGLE_ADDON_OFF;
import static com.md.nasutils.service.readynas.Raidiator4Constants.GET_TOGGLE_ADDON_ON;
import static com.md.nasutils.service.readynas.Raidiator4Constants.GET_TOGGLE_RNP2_OFF;
import static com.md.nasutils.service.readynas.Raidiator4Constants.GET_TOGGLE_RNP2_ON;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_ATTR_HTTP_SHARE_NAMES;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_BONJOUR;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_FTP;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_HTTP;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SERVICE_HTTPS;
import static com.md.nasutils.service.readynas.Raidiator4Constants.SUFFIX_ENABLED_ON_UI;

/**
 * XML based implementation for {@link ReadyNasService}. 
 * Works with the RAIDiator 5 devices (Duo v2 and NV+ v2)
 * 
 * @author michaeldoyle
 */
public class Raidiator5ReadyNasService implements ReadyNasService {

    private static final String TAG = Raidiator4ReadyNasService.class.getSimpleName();

    private static final String PROLOG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    
    private Serializer mSerializer;
    private HttpClient mHttpClient;
    private Locale mLocale;
    private DateFormat mFormat;
    private boolean mHasMatchingLocale;
    
    public Raidiator5ReadyNasService() {
        mHttpClient = new HttpClient();
        Format format = new Format(0, PROLOG); 
        mSerializer = new Persister(new AnnotationStrategy(), format);
        mLocale = Locale.getDefault();
        mFormat = new SimpleDateFormat(DATE_FORMAT, mLocale);
    }
    
    public HttpClient getHttpClient() {
        return mHttpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.mHttpClient = httpClient;
    }
    
    public Serializer getSerializer() {
        return mSerializer;
    }

    public void setSerializer(Serializer serializer) {
        this.mSerializer = serializer;
    }

    @Override
    public Nas getStatus(NasConfiguration config) {
        
        Resources resources = NasUtilsApplication.getContext().getResources();
                
        SystemInfo systemInfo = getSystemInfo(config);
        
        Nas nas = new Nas();
        nas.setSerialNumber(systemInfo.getSerial());
        nas.setModel(systemInfo.getModel());
        nas.setFirmware(systemInfo.getFirmwareName() + " " + systemInfo.getFirmwareVersion());
        nas.setMemory(systemInfo.getMemory() + " MB");
        nas.setArchitecture("ARM");
        
        Network network = getNetwork(config);
        
        nas.setHostname(network.getHostname());
        
        List<NetworkInterface> ifaces = new ArrayList<>();
        
        List<NetworkLink> networkLinks = network.getNetworkLinks();
        for (NetworkLink link : networkLinks) {
            NetworkInterface iface = new NetworkInterface();
            iface.setInterfaceName(link.getName());
            iface.setInterfaceType(resources.getString(R.string.network_interface_physical));
            iface.setIpAddress(link.getIpAddress());
            iface.setIpAddressType(link.getIpType());
            iface.setMacAddress(link.getMacAddress());
            iface.setStatus(link.getStatus().toUpperCase(Locale.US));
            ifaces.add(iface);
        }

        nas.setNetworkInterfaces(ifaces);
        
        HealthCollection healthCollection = getHealth(config);

        nas.setEnclosures(new ArrayList<Enclosure>());
        
        Enclosure enc = new Enclosure();
        
        enc.setFans(new ArrayList<Fan>());
        for (com.md.nasutils.model.raidiator5.Fan fan : healthCollection.getFans()) {
            Fan f = new Fan();
            f.setId(fan.getDeviceId());
            String systemFan = resources.getString(R.string.fan_system);
            f.setLabel("SYS".equals(fan.getDeviceName()) ? systemFan : fan.getDeviceName());
            f.setName(fan.getDeviceName());
            f.setSpeed(fan.getSpeed());
            f.setSpeedMin(1);
            f.setSpeedMax(9999);
            f.setStatus(fan.getStatus().toUpperCase(Locale.US));
            enc.getFans().add(f);
        }
        
        enc.setTemperatures(new ArrayList<Temperature>());
        for (com.md.nasutils.model.raidiator5.Temperature temp : healthCollection.getTemperatures()) {
            Temperature t = new Temperature();
            t.setId(temp.getId());
            t.setLabel("System");
            t.setName("System");
            t.setStatus(temp.getStatus().toUpperCase(Locale.US));
            t.setTemperatureMin(1);
            t.setTemperatureMax(80);
            t.setTemperature(temp.getValue().intValue());
            enc.getTemperatures().add(t);
        }
        
        String[] firmware = systemInfo.getHddFirmware().split(",");
        String[] serial = systemInfo.getHddSerial().split(",");
        String[] model = systemInfo.getHddModel().split(",");
        
        enc.setDisks(new ArrayList<Disk>());
        for (com.md.nasutils.model.raidiator5.Disk disk : healthCollection.getDisks()) {
            Disk d = new Disk();                    
            d.setId(disk.getDeviceId().toString());
            d.setTemperatureMin(1);
            d.setTemperatureMax(80);
            d.setTemperature(disk.getTemperature());
            d.setLabel(disk.getDeviceName() + " " + disk.getDeviceId());
            d.setName(disk.getResourceId());
            d.setChannel(disk.getDeviceId());
            d.setStatus(disk.getStatus().toUpperCase(Locale.US));
            d.setCapacityGb(disk.getCapacity());
            
            int i = disk.getDeviceId()-1;
            
            try {
                d.setFirmwareVersion(firmware[i]);
            } catch (ArrayIndexOutOfBoundsException e) {
                android.util.Log.i(TAG, "Exception parsing firmware verison.", e);
            }
            
            try {
                d.setSerialNumber(serial[i]);
            } catch (ArrayIndexOutOfBoundsException e) {
                android.util.Log.i(TAG, "Exception parsing serial nmber.", e);
            }
            
            try {
                d.setModel(model[i]);
            } catch (ArrayIndexOutOfBoundsException e) {
                android.util.Log.i(TAG, "Exception parsing model verison.", e);
            }
            
            enc.getDisks().add(d);
        }
        nas.getEnclosures().add(enc);
        
        FsBrokerResponse fsBrokerResponse = getFileSystem(config);
        VolumeCollection volumeCollection = getVolumes(config);
        
        nas.setVolumes(new ArrayList<Volume>());
        for (com.md.nasutils.model.raidiator5.Volume volume : volumeCollection.getVolumes()) {
            Volume vol = new Volume();
            vol.setName(volume.getName());
            vol.setStatus(volume.getState().toUpperCase(Locale.US));
            vol.setHealth(volume.getState().toUpperCase(Locale.US));
            vol.setRaidLevel(systemInfo.getRaidLevel());
            
            com.md.nasutils.model.raidiator5.Volume fsBrokerVol = fsBrokerResponse.getVolumeByName(volume.getName());
            
            double freeGb = UnitConversionUtils.bytesToGigabytes(fsBrokerVol.getFreeBytes());
            vol.setFreeGb(freeGb);

            double capacityGb = UnitConversionUtils.bytesToGigabytes(fsBrokerVol.getCapacityBytes());
            vol.setCapacityGb(capacityGb);
        
            double usedGb = capacityGb - freeGb;
            double percentageUsed = (usedGb / capacityGb) * 100;
            
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
        
        nas.setUps(new ArrayList<Ups>());
        for (com.md.nasutils.model.raidiator5.Ups ups : healthCollection.getUpses()) {
            if (!"not_present".equals(ups.getStatus())) {
                Ups u = new Ups();
                u.setId(ups.getId());
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
    
    private String getNmlDocument(String resourceId, String resourceType) {
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
        request.setResourceId(resourceId);
        request.setResourceType(resourceType);
        requests.add(request);
        
        StringWriter writer = new StringWriter();
        try {
            mSerializer.write(document, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return writer.toString();
    }
    
    private SystemInfo getSystemInfo(NasConfiguration config) {
        
        String document = getNmlDocument("SystemInfo", "SystemInfo");
        Nml nml = mHttpClient.post(config, "/dbbroker", document, Nml.class);
        SystemInfo systemInfo = (SystemInfo) nml.getTransaction()
                .getResponse("njl_id_2").getResult().getGetResults();
        
        return systemInfo;
    }
    
    private Network getNetwork(NasConfiguration config) {
        
        String document = getNmlDocument("Network", "Network");
        
        Nml nml = mHttpClient.post(config, "/dbbroker", document, Nml.class);
        
        Network network = (Network) nml.getTransaction()
                .getResponse("njl_id_2").getResult().getGetResults();
        
        return network;
    }
    
    private HealthCollection getHealth(NasConfiguration config) {
        
        String document = getNmlDocument("HealthInfo", "Health_Collection");
        
        Nml nml = mHttpClient.post(config, "/dbbroker", document, Nml.class);
        
        HealthCollection healthCollection = (HealthCollection) nml
                .getTransaction().getResponse("njl_id_2").getResult().getGetResults();
        
        return healthCollection;
    }
    
    private VolumeCollection getVolumes(NasConfiguration config) {
        
        String document = getNmlDocument("Volumes", "Volume_Collection");
        
        Nml nml = mHttpClient.post(config, "/dbbroker", document, Nml.class);
        
        VolumeCollection volumeCollection = (VolumeCollection) nml
                .getTransaction().getResponse("njl_id_2").getResult().getGetResults();
        
        return volumeCollection;
    }
    
    private FsBrokerResponse getFileSystem(NasConfiguration config) {
        Nml fsInfoDocument = new Nml();
        fsInfoDocument.setSrc("nas");
        fsInfoDocument.setDst("nas_utils");

        Transaction fsInfoTransaction = new Transaction();
        fsInfoDocument.setTransaction(fsInfoTransaction);
        fsInfoTransaction.setId("njl_id_1");
        
        List<Request> fsInfoRequests = new ArrayList<>();
        fsInfoTransaction.setRequests(fsInfoRequests);
        
        GetRequest fsInfoRequest = new GetRequest();
        fsInfoRequest.setId("njl_id_2");
        fsInfoRequest.setResourceId("FSInfo");
        fsInfoRequest.setResourceType("FSInfo");
        
        FsBrokerRequest fsbRequest = new FsBrokerRequest();
        fsbRequest.setId("njl_id_2");
        fsbRequest.setMethod("fs.info");
        fsInfoRequest.setFsBrokerRequest(fsbRequest);
        
        fsInfoRequests.add(fsInfoRequest);
        
        StringWriter writer = new StringWriter();
        try {
            mSerializer.write(fsInfoDocument, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        List<BasicNameValuePair> postData = new ArrayList<>();
        postData.add(new BasicNameValuePair("request", writer.toString()));
        FsBrokerResponse fsBrokerResponse = mHttpClient.post(config, "/fsbroker/", postData, FsBrokerResponse.class);
        
        return fsBrokerResponse;
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
        GetRequest request = new GetRequest();
        request.setId("njl_id_2");
        request.setResourceType("Log_Collection");
        
        requests.add(request);

        StringWriter writer = new StringWriter();
        try {
            mSerializer.write(document, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Nml nml = mHttpClient.post(config, "/dbbroker", writer.toString(), Nml.class);
        
        LogCollection logCollection = (LogCollection) nml.getTransaction()
                .getResponse("njl_id_2").getResult().getGetResults();
        
        Logs logs = new Logs();
        logs.setLogs(new ArrayList<Log>());
        
        mHasMatchingLocale = true;

        List<com.md.nasutils.model.raidiator5.Log> logColl = logCollection.getLogs();
        // logs come reverse sorted
        for (int i=logColl.size() - 1; i >= 0; i--) {
            com.md.nasutils.model.raidiator5.Log l = logColl.get(i);
            
            Log log = new Log();
            log.setDescription(l.getMessage());
            log.setDateString(l.getLocalizedDateString());
            if (mHasMatchingLocale) {
                log.setTime(getDate(l.getLocalizedDateString()));
            }
            log.setLogLevel(LogLevel.getLogLevel(l.getSeverity()));
            log.setArgs(new HashMap<String, String>());
            logs.getLogs().add(log);
        }
        
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
        
        // For shutdown, RAIDiator 5 responds back with the transaction id instead of the request id
        com.md.nasutils.model.raidiator5.Response response = nml.getTransaction().getResponse("njl_id_1");
        
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
        
        SmartInfo smartInfo = (SmartInfo) nml.getTransaction()
                .getResponse("njl_id_2").getResult().getGetResults();

        Disk retDisk = new Disk();
        retDisk.setSerialNumber(smartInfo.getSerial());
        retDisk.setSmartAttributes(smartInfo.getAttributes());

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
        
        com.md.nasutils.model.raidiator5.Dictionary dictionary = (com.md.nasutils.model.raidiator5.Dictionary) nml.getTransaction()
                .getResponse("njl_id_2").getResult().getGetResults();
        
        Dictionary retDictionary = new Dictionary();
        
        List<Language> languages = new ArrayList<>();
        for (com.md.nasutils.model.raidiator5.Language l : dictionary.getLanguages()) {
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
        
        ProtocolCollection protocolCollection = (ProtocolCollection) nml
                .getTransaction().getResponse("njl_id_2").getResult().getGetResults();
        
        nas.setServices(new LinkedHashMap<String, Service>());
        for(Protocol p : protocolCollection.getProtocols()) {
            if (!SERVICE_BONJOUR.equals(p.getId())) {
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
            options.setOptions(s.getOptions());
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
        
        com.md.nasutils.model.raidiator5.Response response = nml.getTransaction().getResponse("njl_id_2");
        
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
                .getTransaction().getResponse("njl_id_2").getResult().getGetResults();
        
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
        
        com.md.nasutils.model.raidiator5.Response response = nml.getTransaction().getResponse("njl_id_2");
        
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

        BackupJobCollection backupJobCollection = (BackupJobCollection) nml
                .getTransaction().getResponse("njl_id_2").getResult().getGetResults();

        List<BackupJob> backups = new ArrayList<>();
        for(com.md.nasutils.model.raidiator5.BackupJob b : backupJobCollection.getBackupJobs()) {
            BackupJob job = new BackupJob();
            job.setBackupId(b.getJobId());
            job.setName(b.getJobName());
            job.setStatus(b.getStatusResult());
            job.setStatusDate(b.getStatusDate());
            job.setStatusDescription(b.getStatus());
            job.setSource(b.getSource());
            job.setDestination(b.getDest());

            if (b.getSchedule() != null) {
                job.setSchedule(b.getSchedule().getDisplaySchedule());
            }

            backups.add(job);
        }
        nas.setBackupJobs(backups);

        return nas;
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

        com.md.nasutils.model.raidiator5.Response response = nml.getTransaction().getResponse("njl_id_2");

        return new Response(response.getStatus());
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
        request.setResourceId("InstalledAddons");
        request.setResourceType("addon_Collection");
        requests.add(request);

        StringWriter writer = new StringWriter();
        try {
            mSerializer.write(document, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Nml nml = mHttpClient.post(config, "/dbbroker", writer.toString(), Nml.class);

        Nas nas = new Nas();

        AddonCollection addOnCollection = (AddonCollection) nml
                .getTransaction().getResponse("njl_id_2").getResult().getGetResults();

        Map<String, AddOn> addOns = new HashMap<>();
        for(com.md.nasutils.model.raidiator5.AddOn a : addOnCollection.getAddOns()) {
            AddOn addOn = new AddOn();
            addOn.setId(a.getName());
            if(a.getStatus() != null) {
                addOn.setStatus(a.getStatus().toUpperCase());
            }            addOn.setName(a.getFriendlyName());
            addOn.setVersion(a.getVersion());
            addOns.put(a.getName(), addOn);
        }
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

            // RAIDiator 5 works the same as RAIDiator 4, so we just reuse the response model
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
    
    private int getBinary(boolean i) {
        return i == true ? 1 : 0;
    }
}
