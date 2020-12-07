/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.test;

import static com.md.nasutils.service.readynas.Raidiator4Constants.DATE_FORMAT;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;

import android.test.AndroidTestCase;

import com.md.nasutils.model.raidiator4.BackupJob;
import com.md.nasutils.model.raidiator4.BackupStatus;
import com.md.nasutils.model.raidiator4.Log;
import com.md.nasutils.model.raidiator4.NasStatus;
import com.md.nasutils.model.raidiator4.ProtocolStandard;
import com.md.nasutils.model.raidiator4.SmartDiskInfo;
import com.md.nasutils.model.raidiator4.StatusLogs;
import com.md.nasutils.service.readynas.DateFormatTransformer;

public class Raidiator42Test extends AndroidTestCase {
    
    private Serializer serializer;
    
    @Override
    public void setUp() {
        DateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        RegistryMatcher m = new RegistryMatcher();
        m.bind(Date.class, new DateFormatTransformer(format));
        serializer = new Persister(m);
    }
    
    public void testStatus() throws Exception {
        InputStream in = this.getClass().getClassLoader()
                .getResourceAsStream("assets/radiator42/ultra2status.xml");

        NasStatus status = serializer.read(NasStatus.class, in);

        assertTrue(status.getHostname() != null);

        assertEquals("SN123456789", status.getNasSerial());
    }
    
    public void testSmart() throws Exception {
        InputStream in = this.getClass().getClassLoader()
                .getResourceAsStream("assets/radiator42/smart.xml");

        SmartDiskInfo sdi = serializer.read(SmartDiskInfo.class, in);

        assertTrue(sdi.getAttributes() != null);
        assertEquals(24, sdi.getAttributes().size());
        assertEquals("Spin Up Time", sdi.getAttributes().get(0).getName());
    }
    
    public void testServices() throws Exception {
        InputStream in = this.getClass().getClassLoader()
                .getResourceAsStream("assets/radiator42/protocolStandard.xml");

        ProtocolStandard ps = serializer.read(ProtocolStandard.class, in);

        assertEquals(1, ps.getIsCifsEnabled());
    }
    
    public void testLogs() throws Exception {
        InputStream in = this.getClass().getClassLoader()
                .getResourceAsStream("assets/radiator42/logs.xml");

        StatusLogs statusLogs = serializer.read(StatusLogs.class, in);
        List<Log> logs = statusLogs.getLogs();
        
        assertEquals(100, logs.size());
    }

    public void testBackups() throws Exception {
        InputStream in = this.getClass().getClassLoader()
                .getResourceAsStream("assets/radiator42/backups.xml");

        BackupStatus status = serializer.read(BackupStatus.class, in);
        assertEquals(status.getBackupJobs().size(), 2);

        BackupJob job = status.getBackupJobs().get(0);
        assertEquals("001", job.getId());
        assertEquals("001", job.getJob());
        assertEquals("[dropzone]//Boston Pictures", job.getSource());
        assertEquals("[backup]//Test", job.getDest());
        assertEquals("Daily Every 24 hr between 20-23", job.getWhen());
        assertEquals("Completed Thu Sep 4 22:38", job.getStatus());
        assertEquals(1, job.getEnable());

        job = status.getBackupJobs().get(1);
        assertEquals("002", job.getId());
        assertEquals("002", job.getJob());
        assertEquals("[dropzone]//Test2", job.getSource());
        assertEquals("[backup]//Test2", job.getDest());
        assertEquals("Not scheduled", job.getWhen());
        assertEquals("Ready", job.getStatus());
        assertEquals(0, job.getEnable());
    }
}
