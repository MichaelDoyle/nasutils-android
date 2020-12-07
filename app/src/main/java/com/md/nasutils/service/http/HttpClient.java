/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service.http;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.md.nasutils.NasUtilsApplication;
import com.md.nasutils.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.md.nasutils.ui.fragment.PreferenceConstants.CONNECTION_TIMEOUT;
import static com.md.nasutils.ui.fragment.PreferenceConstants.SOCKET_TIMEOUT;

/**
 * <p>Facilitates HTTP/HTTPS requests to the ReadyNAS.</p>
 * 
 * <p>Be aware that HTTPS requests leverage the {@link TrustingSslSocketFactory} 
 * and will not verify the authenticity of the target end point.</p>
 * 
 * @author michaeldoyle
 */
public class HttpClient {
    
    private static final String TAG = HttpClient.class.getSimpleName();

    private DefaultHttpClient mHttpClient;
    private Serializer mSerializer;

    private Pattern CSRF_ID = Pattern.compile("csrfInsert\\(\"csrfpId\", \"([A-Za-z0-9\\-_=]+)\"\\);");

    public HttpClient() {
        configureXmlSerializer();
        configureHttpClient();
    }
    
    /**
     * Make an HTTP/HTTPS GET request for an XML document and 
     * deserialize the result
     * 
     * @param url the document to retrieve
     * @param clazz the class for which the document should be deserialized
     * @return instance of the given class populated from the xml document
     */
    public <T> T get(NasConfiguration nasConfig, String url, Class<T> clazz) {
        Log.i(TAG, "Fetching result for " + url + " and binding to " + clazz.getSimpleName());
        FirebaseCrashlytics craashlytics = FirebaseCrashlytics.getInstance();
        craashlytics.setCustomKey("request_url", url);
        craashlytics.setCustomKey("request_method", "GET");

        HttpResponse response = doGet(nasConfig, url);
        
        return deserializeResponse(response, clazz);
    }
    
    /**
     * Make an HTTP/HTTPS POST request to the given url
     * deserialize the resulting XML document
     * 
     * @param url the url which the post request will be sent
     * @param postData name/value pair post data to be sent
     * @param clazz the class for which the document should be deserialized
     * @return instance of the given class populated from the xml document
     */
    public <T> T post(NasConfiguration nasConfig, String url,
            List<BasicNameValuePair> postData, Class<T> clazz) {
        Log.i(TAG, "Fetching result for " + url + " and binding to " + clazz.getSimpleName());
        FirebaseCrashlytics craashlytics = FirebaseCrashlytics.getInstance();
        craashlytics.setCustomKey("request_url", url);
        craashlytics.setCustomKey("request_method", "POST");

        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(postData);
        } catch (UnsupportedEncodingException e) {
            handleException(e);
        }

        HttpResponse response = doPost(nasConfig, url, entity);
        
        return deserializeResponse(response, clazz);
    }
    
    /**
     * Make an HTTP/HTTPS POST request to the given url
     * deserialize the resulting XML document
     * 
     * @param url the url which the post request will be sent
     * @param postData string post data to be sent
     * @param clazz the class for which the document should be deserialized
     * @return instance of the given class populated from the xml document
     */
    public <T> T post(NasConfiguration nasConfig, String url,
            String postData, Class<T> clazz) {
        Log.i(TAG, "Fetching result for " + url + " and binding to " + clazz.getSimpleName());
        
        StringEntity entity = null;
        try {
            entity = new StringEntity(postData);
        } catch (UnsupportedEncodingException e) {
            handleException(e);
        }
                
        HttpResponse response = doPost(nasConfig, url, entity);
        
        return deserializeResponse(response, clazz);
    }
    
    /**
     * Deserialize an XML document contained in an HttpResponse
     * 
     * @param response HTTP/HTTPS response containing an XML payload
     * @param clazz the class for which the document should be deserialized
     * @return instance of the given class populated from the xml document
     */
    public <T> T deserializeResponse(HttpResponse response, Class<T> clazz) {
        InputStream stream = null;
        T result = null;
        
        if (response != null) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                try {
                    stream = response.getEntity().getContent();
                    try {
                        result = mSerializer.read(clazz, stream);
                    } finally {
                        stream.close();
                    }
                } catch (IllegalStateException e) {
                    handleException(e);
                } catch (IOException e) {
                    handleException(e);
                } catch (Exception e) {
                    handleException(e);
                }
            } else if (statusCode == 401) {
                // bad credentials
                throw new HttpClientException(NasUtilsApplication.getContext()
                        .getResources().getString(R.string.error_authorization));
            } else if (statusCode == 404) {
                // usually the wrong NAS Model was selected
                throw new HttpClientException(NasUtilsApplication.getContext()
                        .getResources().getString(R.string.error_not_found));
            } else if (statusCode == 500) {
                // FrontView may not be fully initialized yet
                throw new HttpClientException(NasUtilsApplication.getContext()
                        .getResources().getString(R.string.error_server_error));
            } else if (statusCode == 501) {
                // "not implemented" by frontview
                throw new HttpClientException(NasUtilsApplication.getContext()
                        .getResources().getString(R.string.error_not_implemented));
            } else {
                String reason = response.getStatusLine().getReasonPhrase();

                HttpClientException hce = new HttpClientException(NasUtilsApplication.getContext()
                        .getResources().getString(R.string.error_status_generic));

                // we failed to properly handle this HTTP code - report it
                FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
                crashlytics.log("Unhandled HTTP code " + statusCode + " " + reason);
                crashlytics.recordException(hce);

                throw hce;
            }
        }
        
        return result;
    }

    public String getCsrfId(NasConfiguration nasConfig) {
        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();

        crashlytics.log("Getting csrf token");
        String csrfId = null;

        HttpResponse response = doGet(nasConfig, "/admin/csrf.html");

        if (response != null) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                try {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String line = "";
                    while ((line = rd.readLine()) != null) {
                        Matcher m = CSRF_ID.matcher(line);
                        if(m.matches()) {
                            csrfId = m.group(1);
                            crashlytics.log("Found csrf token");
                        }
                    }
                } catch (IllegalStateException e) {
                    handleException(e);
                } catch (IOException e) {
                    handleException(e);
                } catch (Exception e) {
                    handleException(e);
                }
            }
        }

        return csrfId;
    }
    
    /**
     * Make an HTTP/HTTPS GET request and return the response
     */
    private HttpResponse doGet(NasConfiguration nasConfig, String url) {
        HttpResponse httpResponse = null;

        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("User-Agent", "NasUtils/1.0");
            httpGet.setHeader("Host", nasConfig.getHostname());
            
            UsernamePasswordCredentials creds = new UsernamePasswordCredentials(
                    nasConfig.getUsername(), nasConfig.getPassword());
            AuthScope auth = new AuthScope(nasConfig.getHostname(), nasConfig.getPort());
            mHttpClient.getCredentialsProvider().setCredentials(auth, creds);
            
            HttpHost httpHost = new HttpHost(nasConfig.getHostname(),
                    nasConfig.getPort(), nasConfig.getProtocol());
            httpResponse = mHttpClient.execute(httpHost, httpGet);
            Log.i(TAG, "Request status " + httpResponse.getStatusLine());
        } catch (HttpHostConnectException e) {
            throw new HttpClientException(NasUtilsApplication.getContext()
                    .getResources().getString(R.string.error_connection_refused));
        } catch (UnknownHostException e) {
            throw new HttpClientException(NasUtilsApplication.getContext()
                    .getResources().getString(R.string.error_unresolved_hostname));
        } catch (ConnectTimeoutException e) {
            throw new HttpClientException(NasUtilsApplication.getContext()
                    .getResources().getString(R.string.error_conn_timeout));
        } catch (SocketTimeoutException e) {
            throw new HttpClientException(NasUtilsApplication.getContext()
                    .getResources().getString(R.string.error_sock_timeout));
        } catch (ClientProtocolException e) {
            handleException(e);
        } catch (IOException e) {
            handleException(e);
        }
        
        return httpResponse;
    }
    
    /**
     * Make an HTTP/HTTPS POST request and return the response
     */
    private HttpResponse doPost(NasConfiguration nasConfig, String url, HttpEntity entity) {
        Log.i(TAG, "Sending post request " + url);

        String csrfId = null;
        if (nasConfig.getOsVersion() == 6) {
            csrfId = getCsrfId(nasConfig);
        }

        HttpResponse httpResponse = null;

        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(entity);
            httpPost.setHeader("User-Agent", "NasUtils/1.0");
            httpPost.setHeader("Host", nasConfig.getHostname());

            if(csrfId != null) {
                httpPost.setHeader("csrfpId", csrfId);
            }

            UsernamePasswordCredentials creds = new UsernamePasswordCredentials(
                    nasConfig.getUsername(), nasConfig.getPassword());
            AuthScope auth = new AuthScope(nasConfig.getHostname(), nasConfig.getPort());
            mHttpClient.getCredentialsProvider().setCredentials(auth, creds);
            
            HttpHost httpHost = new HttpHost(nasConfig.getHostname(),
                    nasConfig.getPort(), nasConfig.getProtocol());
            httpResponse = mHttpClient.execute(httpHost, httpPost);
            
            Log.i(TAG, "Request status " + httpResponse.getStatusLine());
        } catch (HttpHostConnectException e) {
            throw new HttpClientException(NasUtilsApplication.getContext()
                    .getResources().getString(R.string.error_connection_refused));
        } catch (UnknownHostException e) {
            throw new HttpClientException(NasUtilsApplication.getContext()
                    .getResources().getString(R.string.error_unresolved_hostname));
        } catch (ConnectTimeoutException e) {
            throw new HttpClientException(NasUtilsApplication.getContext()
                    .getResources().getString(R.string.error_conn_timeout));
        } catch (SocketTimeoutException e) {
            throw new HttpClientException(NasUtilsApplication.getContext()
                    .getResources().getString(R.string.error_sock_timeout));
        } catch (ClientProtocolException e) {
            handleException(e);
        } catch (IOException e) {
            handleException(e);
        }
        
        return httpResponse;
    }
    
    /**
     * Initialize the XML Serializer 
     */
    private void configureXmlSerializer() {
        Log.e(TAG, "Configuring XML Serializer.");
        Strategy strategy = new AnnotationStrategy();
        mSerializer = new Persister(strategy);
    }
    
    /**
     * Initialize the HttpClient using the {@link NasConfiguration}
     * provided in the constructor
     */
    private void configureHttpClient() {
        Log.e(TAG, "Initializing HttpClient.");

        // Allow unsigned and self signed certificates
        SSLSocketFactory sf = getTrustingSslSocketFactory();
        
        mHttpClient = getDefaultHttpClient(sf);
    }
    
    /**
     * @return An SSLSocketFactory that will blindly trust all hosts and certificates
     */
    private SSLSocketFactory getTrustingSslSocketFactory() {
        SSLSocketFactory sf = null;
        
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            sf = new TrustingSslSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        } catch (KeyStoreException e) {
            handleException(e);
        } catch (NoSuchAlgorithmException e) {
            handleException(e);
        } catch (CertificateException e) {
            handleException(e);
        } catch (IOException e) {
            handleException(e);
        } catch (KeyManagementException e) {
            handleException(e);
        } catch (UnrecoverableKeyException e) {
            handleException(e);
        }
        
        return sf;
    }
    
    private DefaultHttpClient getDefaultHttpClient(SSLSocketFactory sf) {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(NasUtilsApplication.getContext());

        // connection timeout in milliseconds
        int connTimeout;
        try {
            connTimeout = Integer.parseInt(pref.getString(CONNECTION_TIMEOUT, "15")) * 1000;
        } catch (NumberFormatException e) {
            connTimeout = 15000;
        }
        HttpConnectionParams.setConnectionTimeout(params, connTimeout);
        
        // socket timeout in milliseconds
        int sockTimeout;
        try {
            sockTimeout = Integer.parseInt(pref.getString(SOCKET_TIMEOUT, "30")) * 1000;
        } catch (NumberFormatException e) {
            sockTimeout = 30000;
        }
        HttpConnectionParams.setSoTimeout(params, sockTimeout);
        
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));

        ClientConnectionManager clientConnectionMananger = 
                new ThreadSafeClientConnManager(params, registry);

        return new DefaultHttpClient(clientConnectionMananger, params);
    }
        
    private void handleException(Exception e) {
        Log.e(TAG, "Exception: ", e);
        throw new HttpClientException(NasUtilsApplication.getContext()
                .getResources().getString(R.string.error_generic), e);
    }
}
