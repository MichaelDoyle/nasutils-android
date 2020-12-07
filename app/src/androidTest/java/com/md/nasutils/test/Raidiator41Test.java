/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.test;

import static com.md.nasutils.service.readynas.Raidiator4Constants.DATE_FORMAT;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;

import android.test.AndroidTestCase;

import com.md.nasutils.model.raidiator4.NasHealth;
import com.md.nasutils.model.raidiator4.NasStatus;
import com.md.nasutils.service.readynas.DateFormatTransformer;

public class Raidiator41Test extends AndroidTestCase {

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
                .getResourceAsStream("assets/radiator41/nasstate.xml");
        
        NasStatus status = serializer.read(NasStatus.class, in);
        
        assertTrue(status.getHostname() != null);
        assertEquals("SN123456789", status.getNasSerial());
    }
    
    public void testHealth() throws Exception {
        InputStream in = this.getClass().getClassLoader()
                .getResourceAsStream("assets/radiator41/status.xml");
        
        NasHealth health = serializer.read(NasHealth.class, in);
        
        assertTrue(health.getDrives().size() > 0);
    }
}
