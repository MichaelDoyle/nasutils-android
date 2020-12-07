/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.test;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;

import android.test.AndroidTestCase;

import com.md.nasutils.model.raidiator5.BackupJobCollection;
import com.md.nasutils.model.raidiator5.CustomRequest;
import com.md.nasutils.model.raidiator5.Dictionary;
import com.md.nasutils.model.raidiator5.Disk;
import com.md.nasutils.model.raidiator5.DnsServer;
import com.md.nasutils.model.raidiator5.Fan;
import com.md.nasutils.model.raidiator5.FsBrokerResponse;
import com.md.nasutils.model.raidiator5.GetRequest;
import com.md.nasutils.model.raidiator5.HealthCollection;
import com.md.nasutils.model.raidiator5.Language;
import com.md.nasutils.model.raidiator5.Log;
import com.md.nasutils.model.raidiator5.LogCollection;
import com.md.nasutils.model.raidiator5.Network;
import com.md.nasutils.model.raidiator5.NetworkLink;
import com.md.nasutils.model.raidiator5.Nml;
import com.md.nasutils.model.raidiator5.Protocol;
import com.md.nasutils.model.raidiator5.ProtocolCollection;
import com.md.nasutils.model.raidiator5.Request;
import com.md.nasutils.model.raidiator5.Share;
import com.md.nasutils.model.raidiator5.Shutdown;
import com.md.nasutils.model.raidiator5.SmartInfo;
import com.md.nasutils.model.raidiator5.SystemInfo;
import com.md.nasutils.model.raidiator5.Temperature;
import com.md.nasutils.model.raidiator5.Transaction;
import com.md.nasutils.model.raidiator5.Ups;
import com.md.nasutils.model.raidiator5.Volume;
import com.md.nasutils.model.raidiator5.VolumeCollection;

public class Raidiator5Test extends AndroidTestCase {
        
    private static final String PROLOG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String CUSTOM_SHUTDOWN = PROLOG + "\n" + "<xs:nml dst=\"nas_utils\" src=\"nas\" xmlns:xs=\"http://www.netgear.com/protocol/transaction/NMLSchema-0.9\" xmlns=\"urn:netgear:nas:readynasd\"><xs:transaction id=\"njl_id_2\"><xs:custom id=\"njl_id_1\" resource-id=\"Shutdown\" resource-type=\"System\" name=\"Halt\"><Shutdown fsck=\"false\" halt=\"true\"/></xs:custom></xs:transaction></xs:nml>";
    private static final String GET_SMART_INFO = PROLOG + "\n" + "<xs:nml dst=\"nas_utils\" src=\"nas\" xmlns:xs=\"http://www.netgear.com/protocol/transaction/NMLSchema-0.9\" xmlns=\"urn:netgear:nas:readynasd\"><xs:transaction id=\"njl_id_2\"><xs:get id=\"njl_id_1\" resource-id=\"sdb\" resource-type=\"SMARTInfo\"/></xs:transaction></xs:nml>";
            
    private Serializer serializer;
    
    @Override
    public void setUp() {
        Format format = new Format(0, PROLOG); 
        serializer = new Persister(new AnnotationStrategy(), format);
    }
    
    private Nml getDocument(String fileName) throws Exception {
        InputStream in = this.getClass().getClassLoader()
                .getResourceAsStream(fileName);
        return serializer.read(Nml.class, in);
    }
    
    public void testDictionary() throws Exception {
        Nml document = getDocument("assets/raidiator5/response/dictionary.xml");
        
        Dictionary dictionary = (Dictionary) document.getTransaction()
                .getResponses().get(0).getResult().getGetResults();
        
        List<Language> languages = dictionary.getLanguages();
        assertEquals(15, languages.size());
        
        Language english = null;
        for (Language language : languages) {
            if ("en-us".equals(language.getCode())) {
                english = language;
            }
        }
        
        Map<String, String> englishItems = english.getItems();
        assertEquals(1, englishItems.size());
        assertEquals("Some Value", englishItems.get("SOME_KEY"));
    }
    
    public void testHealthCollection() throws Exception {
        Nml document = getDocument("assets/raidiator5/response/health.xml");

        HealthCollection healthInfo = (HealthCollection) document
                .getTransaction().getResponses().get(0).getResult().getGetResults();
        
        assertEquals(2, healthInfo.getDisks().size());
        assertEquals(1, healthInfo.getTemperatures().size());
        assertEquals(1, healthInfo.getFans().size());
                
        List<Disk> disks = healthInfo.getDisks();
        assertEquals("1", disks.get(0).getId());
        assertEquals("1", disks.get(0).getResourceId());
        assertEquals("Disk", disks.get(0).getResourceType());
        assertEquals("ok", disks.get(0).getStatus());
        assertEquals("Disk", disks.get(0).getDeviceName());
        assertEquals(Integer.valueOf(1), disks.get(0).getDeviceId());
        assertEquals("WDC WD20EFRX-68EUZN0 1863 GB", disks.get(0).getModel());
        assertEquals(Integer.valueOf(29), disks.get(0).getTemperature());

        List<Temperature> temps = healthInfo.getTemperatures();
        assertEquals("Summary", temps.get(0).getId());
        assertEquals("Summary", temps.get(0).getResourceId());
        assertEquals("Disk", temps.get(0).getResourceType());
        assertEquals("ok", temps.get(0).getStatus());
        assertEquals(Double.valueOf(26.0), temps.get(0).getValue());

        List<Fan> fans = healthInfo.getFans();
        assertEquals("SYS", fans.get(0).getId());
        assertEquals("SYS", fans.get(0).getResourceId());
        assertEquals("Fan", fans.get(0).getResourceType());
        assertEquals("Fan", fans.get(0).getDeviceName());
        assertEquals("SYS", fans.get(0).getDeviceId());
        assertEquals("ok", fans.get(0).getStatus());
        assertEquals(Integer.valueOf(744), fans.get(0).getSpeed());
        
        List<Ups> upses = healthInfo.getUpses();
        assertEquals("1", upses.get(0).getId());
        assertEquals("1", upses.get(0).getResourceId());
        assertEquals("UPS", upses.get(0).getResourceType());
        assertEquals("UPS", upses.get(0).getDeviceName());
        assertEquals("ok", upses.get(0).getStatus());
        assertEquals("CPS UPS CP825", upses.get(0).getModel());
        assertEquals("100%", upses.get(0).getBatteryCharge());
        assertEquals("57 min", upses.get(0).getBatteryRuntime());
    }
    
    public void testLogCollection() throws Exception {
        Nml document = getDocument("assets/raidiator5/response/logs.xml");
        
        LogCollection coll = (LogCollection) document.getTransaction()
                .getResponses().get(0).getResult().getGetResults();
        
        assertEquals(13, coll.getLogs().size());
        
        Log log = coll.getLogs().get(0);
        assertEquals("1", log.getSeverity());
        assertEquals("Tue Feb 18 06:04:24 PST 2014", log.getLocalizedDateString());
        assertEquals(
                "Successfully installed ReadyNAS Photos II addon package (version: 1.0.6)", 
                log.getMessage());
    }
    
    public void testNetwork() throws Exception {
        Nml document = getDocument("assets/raidiator5/response/network.xml");

        Network network = (Network) document.getTransaction().getResponses()
                .get(0).getResult().getGetResults();
        
        assertEquals("icepick", network.getHostname());
        assertEquals("domain", network.getDnsDomain());
        assertEquals("192.168.1.1", network.getGateway());
        assertEquals("WORKGROUP", network.getWorkgroup());
        
        List<DnsServer> dnsServers = network.getDnsServers();
        assertEquals(3, dnsServers.size());
        assertEquals(Integer.valueOf(1), dnsServers.get(0).getPriority());
        assertEquals("75.75.75.75", dnsServers.get(0).getIpAddress());
        
        List<NetworkLink> ifaces = network.getNetworkLinks();
        assertEquals(1, ifaces.size());
        assertEquals("192.168.1.255", ifaces.get(0).getBroadcast());
        assertEquals("192.168.1.202", ifaces.get(0).getIpAddress());
        assertEquals("static", ifaces.get(0).getIpType());
        assertEquals("00:00:00:00:00:00", ifaces.get(0).getMacAddress());
        assertEquals("1500", ifaces.get(0).getMtu());
        assertEquals("eth0", ifaces.get(0).getName());
        assertEquals("255.255.255.0", ifaces.get(0).getNetmask());
        assertEquals("up", ifaces.get(0).getStatus());
    }
    
    public void testSystemInfo() throws Exception {
        Nml document = getDocument("assets/raidiator5/response/systemInfo.xml");
                
        SystemInfo systemInfo = (SystemInfo) document.getTransaction()
                .getResponses().get(0).getResult().getGetResults();
        
        // SystemInfo
        assertEquals("ReadyNAS Duo v2", systemInfo.getModel());
        assertEquals("READYNAS-SN", systemInfo.getSerial());
        assertEquals("RAIDiator", systemInfo.getFirmwareName());
        assertEquals("5.3.10-T3", systemInfo.getFirmwareVersion());
        assertEquals(Integer.valueOf(256), systemInfo.getMemory());
        assertEquals(Integer.valueOf(0), systemInfo.getNumberOfCpus());
        assertEquals(Double.valueOf(0.0), systemInfo.getCpuFrequency());
        assertEquals("00:00:00:00:00:00", systemInfo.getMacAddress());
        assertEquals("en-us", systemInfo.getLanguage());
        assertEquals("X2", systemInfo.getRaidLevel());
        assertEquals("Western Digital,Seagate", systemInfo.getHddVendor());
        assertEquals("WDC WD20EFRX-68EUZN0,Seagate ST2000DL003-9VT166", systemInfo.getHddModel());
        assertEquals("WD-SN,ST-SN", systemInfo.getHddSerial());
        assertEquals("80.00A80,CC3C", systemInfo.getHddFirmware());
        assertEquals(Integer.valueOf(0), systemInfo.getRegistered());
        
        // RAID
        assertEquals("Ok", systemInfo.getRaidStatus());
        assertEquals(Double.valueOf(9.9), systemInfo.getRaidPercentageCompleted());
        assertEquals(Integer.valueOf(123646), systemInfo.getRaidSpeedKbs());
        assertEquals(Integer.valueOf(236), systemInfo.getRaidCompletionTimeMinutes());
    }
    
    public void testVolumeCollection() throws Exception {
        Nml document = getDocument("assets/raidiator5/response/volumes.xml");
        
        VolumeCollection coll = (VolumeCollection) document.getTransaction()
                .getResponses().get(0).getResult().getGetResults();
        
        assertEquals("False", coll.getExpandable());
        assertEquals(Integer.valueOf(2), coll.getInstalledDisks());
        assertEquals(Integer.valueOf(2), coll.getPermissableDisks());
        assertEquals("X-RAID2", coll.getType());
        
        List<Volume> volumes = coll.getVolumes();
        assertEquals(1, volumes.size());
        
        Volume volume = volumes.get(0);
        assertEquals(Long.valueOf(1948792752), volume.getCapacityBytes());
        assertEquals("C", volume.getName());
        assertEquals("1", volume.getResourceId());
        assertEquals("Volume", volume.getResourceType());
        assertEquals("Recovery", volume.getState());
        assertEquals(Integer.valueOf(1), volume.getRaidLevel());
    }
    
    public void testProtocolCollection() throws Exception {
        Nml document = getDocument("assets/raidiator5/response/protocols.xml");
        
        ProtocolCollection coll = (ProtocolCollection) document
                .getTransaction().getResponses().get(0).getResult().getGetResults();

        List<Protocol> protocols = coll.getProtocols();
        assertEquals(12, protocols.size());
        
        Protocol protocol = protocols.get(10);
        assertEquals("http", protocol.getId());
        assertEquals("http", protocol.getResourceId());
        assertEquals("Protocol", protocol.getResourceType());
        assertEquals(1, protocol.isEnabled());
        
        Map<String, String> options = protocol.getOptions().getOptions();
        assertEquals(2, options.size());
        assertEquals("0", options.get("login-enabled"));
        assertEquals("", options.get("share-name"));
        
        List<Share> shares = protocol.getShares();
        assertEquals(2, shares.size());
        assertEquals("2", shares.get(0).getId());
        assertEquals("2", shares.get(0).getResourceId());
        assertEquals("backup", shares.get(0).getName());
        assertEquals("Share", shares.get(0).getResourceType());
    }
    
    public void testSmart() throws Exception {
        Nml document = getDocument("assets/raidiator5/response/smart.xml");
        
        SmartInfo smartInfo = (SmartInfo) document
                .getTransaction().getResponses().get(0).getResult().getGetResults();
        
        assertEquals("WDC WD20EFRX-68EUZN0", smartInfo.getModel());
        assertEquals("WD-SN", smartInfo.getSerial());
        assertEquals("80.00A80", smartInfo.getFirmwareVersion());
        
        Map<String, String> attributes = smartInfo.getAttributes();
        assertEquals(3, attributes.size());
        assertEquals("0", attributes.get("Raw Read Error Rate"));
        assertEquals("4366", attributes.get("Spin Up Time"));
        assertEquals("7", attributes.get("Start Stop Count"));
    }
    
    public void testFsinfo() throws Exception {
        InputStream in = this.getClass().getClassLoader()
                .getResourceAsStream("assets/raidiator5/response/fsinfo.xml");
        FsBrokerResponse document = serializer.read(FsBrokerResponse.class, in);
        
        assertEquals("success", document.getStatus());
        assertEquals(1, document.getVolumes().size());
        
        Volume vol = document.getVolumes().get(0);
        assertEquals("C", vol.getName());
        assertEquals(Long.valueOf(1976928342016L), vol.getCapacityBytes());
        assertEquals(Long.valueOf(1976722530304L), vol.getFreeBytes());
    }
    
    public void testSerializeGetRequest() throws Exception {
        Nml document = new Nml();
        document.setDst("nas_utils");
        document.setSrc("nas");

        Transaction transaction = new Transaction();
        document.setTransaction(transaction);
        transaction.setId("njl_id_2");
        
        List<Request> requests = new ArrayList<>();
        transaction.setRequests(requests);
        Request request = new GetRequest();
        request.setId("njl_id_1");
        request.setResourceId("sdb");
        request.setResourceType("SMARTInfo");
        requests.add(request);
        
        StringWriter writer = new StringWriter();
        serializer.write(document, writer);
        
        assertEquals(GET_SMART_INFO, writer.toString());
    }
    
    public void testSerializeCustomRequest() throws Exception {
        Nml document = new Nml();
        document.setDst("nas_utils");
        document.setSrc("nas");

        Transaction transaction = new Transaction();
        document.setTransaction(transaction);
        transaction.setId("njl_id_2");
        
        List<Request> requests = new ArrayList<>();
        transaction.setRequests(requests);
        CustomRequest request = new CustomRequest();
        request.setId("njl_id_1");
        request.setResourceId("Shutdown");
        request.setResourceType("System");
        request.setName("Halt");
        requests.add(request);

        Shutdown shutdown = new Shutdown();
        request.setShutdown(shutdown);
        shutdown.setFsck("false");
        shutdown.setHalt("true");
        
        StringWriter writer = new StringWriter();
        serializer.write(document, writer);

        assertEquals(CUSTOM_SHUTDOWN, writer.toString());
    }

    public void testBackupCollection() throws Exception {
        Nml document = getDocument("assets/raidiator5/response/backup.xml");

        BackupJobCollection coll = (BackupJobCollection) document
                .getTransaction().getResponse("njl_id_2569").getResult().getGetResults();

        assertEquals(coll.getBackupJobs().size(), 2);
    }
}
