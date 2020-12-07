/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service.http;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * <p>Provides an SSLSocketFactory that will blindly trust any SSL certificate.
 * This is generally not a wise thing to do in a production environment.
 * Not verifying the authenticity of the SSL certificate can potentially
 * leave you open to man-in-the-middle attacks.</p>
 * 
 * <p>In the case of the ReadyNAS, the SSL certificate is self signed. 
 * To work around this, we use this class. Communication with the ReadyNAS will
 * still be encrypted, but the authenticity of the ReadyNAS device will be unknown.
 * For users on a private network, this should not represent a significant threat.
 * Internet clients should use their best judgment.</p>
 * 
 * @author michaeldoyle
 */
public class TrustingSslSocketFactory extends SSLSocketFactory {

    @SuppressWarnings("unused")
    private static final String TAG = TrustingSslSocketFactory.class.getSimpleName();

    private SSLContext mSslContext = SSLContext.getInstance("TLS");

    public TrustingSslSocketFactory(KeyStore truststore)
            throws NoSuchAlgorithmException, KeyManagementException,
            KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        mSslContext.init(null, new TrustManager[] { tm }, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port,
            boolean autoClose) throws IOException, UnknownHostException {
        return mSslContext.getSocketFactory().createSocket(socket, host, port,
                autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return mSslContext.getSocketFactory().createSocket();
    }
}
