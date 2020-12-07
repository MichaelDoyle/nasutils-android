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

import com.md.nasutils.model.os6.BackupJobCollection;
import com.md.nasutils.model.os6.CustomRequest;
import com.md.nasutils.model.os6.Dictionary;
import com.md.nasutils.model.os6.Disk;
import com.md.nasutils.model.os6.DiskEnclosureCollection;
import com.md.nasutils.model.os6.DnsServer;
import com.md.nasutils.model.os6.EnclosureHealth;
import com.md.nasutils.model.os6.Fan;
import com.md.nasutils.model.os6.GetRequest;
import com.md.nasutils.model.os6.HealthCollection;
import com.md.nasutils.model.os6.Language;
import com.md.nasutils.model.os6.LogCollection;
import com.md.nasutils.model.os6.Network;
import com.md.nasutils.model.os6.NetworkLink;
import com.md.nasutils.model.os6.NetworkLinkCollection;
import com.md.nasutils.model.os6.Nml;
import com.md.nasutils.model.os6.Options;
import com.md.nasutils.model.os6.Protocol;
import com.md.nasutils.model.os6.ProtocolCollection;
import com.md.nasutils.model.os6.QueryRequest;
import com.md.nasutils.model.os6.Raid;
import com.md.nasutils.model.os6.Request;
import com.md.nasutils.model.os6.Shutdown;
import com.md.nasutils.model.os6.SystemInfo;
import com.md.nasutils.model.os6.Temperature;
import com.md.nasutils.model.os6.Transaction;
import com.md.nasutils.model.os6.Volume;
import com.md.nasutils.model.os6.VolumeCollection;

public class Os6Test extends AndroidTestCase {
        
    private static final String PROLOG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    private static final String CUSTOM_SHUTDOWN = PROLOG + "\n" + "<xs:nml dst=\"nas_utils\" src=\"nas\" xmlns:xs=\"http://www.netgear.com/protocol/transaction/NMLSchema-0.9\" xmlns=\"urn:netgear:nas:readynasd\"><xs:transaction id=\"njl_id_2\"><xs:custom id=\"njl_id_1\" resource-id=\"Shutdown\" resource-type=\"System\" name=\"Halt\"><Shutdown fsck=\"false\" halt=\"true\"/></xs:custom></xs:transaction></xs:nml>";
    private static final String GET_SMART_INFO = PROLOG + "\n" + "<xs:nml dst=\"nas_utils\" src=\"nas\" xmlns:xs=\"http://www.netgear.com/protocol/transaction/NMLSchema-0.9\" xmlns=\"urn:netgear:nas:readynasd\"><xs:transaction id=\"njl_id_2\"><xs:get id=\"njl_id_1\" resource-id=\"sdb\" resource-type=\"SMARTInfo\"/></xs:transaction></xs:nml>";
    private static final String QUERY_LOG_COLLECTION = PROLOG + "\n" + "<xs:nml dst=\"nas_utils\" src=\"nas\" xmlns:xs=\"http://www.netgear.com/protocol/transaction/NMLSchema-0.9\" xmlns=\"urn:netgear:nas:readynasd\"><xs:transaction id=\"njl_id_2\"><xs:query id=\"njl_id_1\" resource-type=\"Log_Collection\"><xs:qualifier/></xs:query></xs:transaction></xs:nml>";
    private static final String MULTI_REQUEST_TRANSACTION = PROLOG + "\n" + "<xs:nml dst=\"nas_utils\" src=\"nas\" xmlns:xs=\"http://www.netgear.com/protocol/transaction/NMLSchema-0.9\" xmlns=\"urn:netgear:nas:readynasd\"><xs:transaction id=\"njl_id_1\"><xs:get id=\"njl_id_2\" resource-id=\"SystemInfo\" resource-type=\"SystemInfo\"/><xs:get id=\"njl_id_3\" resource-id=\"Network\" resource-type=\"Network\"/><xs:get id=\"njl_id_4\" resource-id=\"network_link_list\" resource-type=\"network_link_collection\"/></xs:transaction></xs:nml>";
            
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
        Nml document = getDocument("assets/os6/response/dictionary.xml");
        
        Dictionary dictionary = (Dictionary) document.getTransaction()
                .getResponses().get(0).getResult().getData();
        
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
    
    public void testDiskEnclosure() throws Exception {
        Nml document = getDocument("assets/os6/response/diskEnclosure.xml");
        
        DiskEnclosureCollection coll = (DiskEnclosureCollection) document
                .getTransaction().getResponses().get(0).getResult().getData();
        
        List<Disk> disks = coll.getDiskEnclosures().get(0).getDiskCollection()
                .getDisks();
        assertEquals(2, disks.size());
        
        Disk disk = disks.get(0);
        assertEquals("sdb", disk.getResourceId());
    }
    
    public void testHealthCollection() throws Exception {
        Nml document = getDocument("assets/os6/response/healthInfo.xml");

        HealthCollection healthInfo = (HealthCollection) document
                .getTransaction().getResponses().get(0).getResult().getData();
        
        EnclosureHealth enclosureHealth = healthInfo.getEnclosureHealths().get(0);
        assertEquals(2, enclosureHealth.getDisks().size());
        assertEquals(3, enclosureHealth.getTemperatures().size());
        assertEquals(2, enclosureHealth.getFans().size());
        
        List<Disk> disks = enclosureHealth.getDisks();
        assertEquals("VBOX HARDDISK", disks.get(0).getModel());
        assertEquals(Integer.valueOf(-1), disks.get(0).getTemperature());

        List<Temperature> temps = enclosureHealth.getTemperatures();
        assertEquals("Temperature", temps.get(0).getDeviceName());
        assertEquals(Integer.valueOf(40), temps.get(0).getTemperature());

        List<Fan> fans = enclosureHealth.getFans();
        assertEquals("Fan", fans.get(0).getDeviceName());
        assertEquals(Integer.valueOf(5355), fans.get(0).getSpeed());
    }
    
    public void testLogCollection() throws Exception {
        Nml document = getDocument("assets/os6/response/logs.xml");
        
        LogCollection coll = (LogCollection) document.getTransaction()
                .getResponses().get(0).getResult().getData();
        assertEquals(12, coll.getLogs().size());
    }
    
    public void testNetwork() throws Exception {
        Nml document = getDocument("assets/os6/response/network.xml");

        Network network = (Network) document.getTransaction().getResponses()
                .get(0).getResult().getData();
        assertEquals("virtual-nas", network.getHostname());

    }
    
    public void testNetworkCollection() throws Exception {
        Nml document = getDocument("assets/os6/response/networkLinkCollection.xml");
        
        NetworkLinkCollection network = (NetworkLinkCollection) document
                .getTransaction().getResponses().get(0).getResult().getData();
        
        NetworkLink link = network.getNetworkLinks().get(0);        
        assertEquals("eth0", link.getLink());
        assertEquals("192.168.1.7", link.getIpAddress());
        
        DnsServer dns = link.getDnsServers().get(0);
        assertEquals(Integer.valueOf(0), dns.getPriority());
        assertEquals("75.75.75.75", dns.getIpAddress());
    }
    
    public void testSystemInfo() throws Exception {
        Nml document = getDocument("assets/os6/response/systemInfo.xml");
        
        SystemInfo systemInfo = (SystemInfo) document.getTransaction()
                .getResponses().get(0).getResult().getData();
        assertEquals("ReadyNASOS", systemInfo.getFirmwareName());
        assertEquals("6.0.6", systemInfo.getFirmwareVersion());
        assertEquals(Integer.valueOf(1024), systemInfo.getMemory());
    }
    
    public void testVolumeCollection() throws Exception {
        Nml document = getDocument("assets/os6/response/volumes.xml");
        
        VolumeCollection coll = (VolumeCollection) document.getTransaction()
                .getResponses().get(0).getResult().getData();
        
        List<Volume> volumes = coll.getVolumes();
        assertEquals(1, volumes.size());
        
        Volume volume = volumes.get(0);
        assertEquals("ac87953e-aec1-4e2e-abaa-ac7df0d14025", volume.getGuid());
        
        List<Raid> virtualDevices = volume.getVirtualDevices();
        assertEquals(1, virtualDevices.size());
        
        List<Disk> disks = virtualDevices.get(0).getDisks();
        assertEquals(2, disks.size());
        
        Disk disk = disks.get(0);
        assertEquals("sdb", disk.getResourceId());
    }
    
    public void testProtocolCollection() throws Exception {
        Nml document = getDocument("assets/os6/response/protocols.xml");
        
        ProtocolCollection coll = (ProtocolCollection) document
                .getTransaction().getResponse("njl_id_942").getResult().getData();

        List<Protocol> protocols = coll.getProtocols();
        assertEquals(16, protocols.size());
        
        Protocol protocol = protocols.get(0);
        assertEquals("cifs", protocol.getId());
        
        Options options = protocol.getOptions();
        assertEquals(4, options.getOptions().size());
    }
    
    public void testMultiRequestResponse() throws Exception {
        Nml document = getDocument("assets/os6/response/multiRequestTransaction.xml");
        
        SystemInfo systemInfo = (SystemInfo) document.getTransaction()
                .getResponses().get(0).getResult().getData();
        
        assertEquals("ReadyNASOS", systemInfo.getFirmwareName());
        assertEquals("6.0.8", systemInfo.getFirmwareVersion());
        assertEquals(Integer.valueOf(1024), systemInfo.getMemory());

        Network network = (Network) document.getTransaction().getResponses()
                .get(1).getResult().getData();
        
        assertEquals("virtual-nas", network.getHostname());

        NetworkLinkCollection networkLinks = (NetworkLinkCollection) document
                .getTransaction().getResponses().get(2).getResult().getData();
        
        NetworkLink link = networkLinks.getNetworkLinks().get(0);       
        assertEquals("eth0", link.getLink());
        assertEquals("192.168.1.7", link.getIpAddress());
        
        DnsServer dns = link.getDnsServers().get(0);
        assertEquals(Integer.valueOf(0), dns.getPriority());
        assertEquals("75.75.75.75", dns.getIpAddress());

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
    
    public void testSerializeQueryRequest() throws Exception {
        Nml document = new Nml();
        document.setDst("nas_utils");
        document.setSrc("nas");

        Transaction transaction = new Transaction();
        document.setTransaction(transaction);
        transaction.setId("njl_id_2");
        
        List<Request> requests = new ArrayList<>();
        transaction.setRequests(requests);
        Request request = new QueryRequest();
        request.setId("njl_id_1");
        request.setResourceType("Log_Collection");
        requests.add(request);

        StringWriter writer = new StringWriter();
        serializer.write(document, writer);

        assertEquals(QUERY_LOG_COLLECTION, writer.toString());
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
    
    public void testSerializeMultiRequestTransaction() throws Exception {
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
        
        StringWriter writer = new StringWriter();
        serializer.write(document, writer);
        
        assertEquals(MULTI_REQUEST_TRANSACTION, writer.toString());
    }

    public void testBackupCollection() throws Exception {
        Nml document = getDocument("assets/os6/response/backups.xml");

        BackupJobCollection coll = (BackupJobCollection) document
                .getTransaction().getResponse("njl_id_2608").getResult().getData();

        assertEquals(coll.getBackupJobs().size(), 2);
    }
}
