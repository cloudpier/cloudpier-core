/*
 *  Copyright 2013 Cloud4SOA, www.cloud4soa.eu
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


/*
 * Copyright 2010-2011, CloudBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudbees.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.params.HttpParams;

public class BeesClientBase {
    Logger logger = Logger.getLogger(getClass().getSimpleName());
    protected String format = "xml";

    private String serverApiUrl = "http://localhost:8080/api";

    private String api_key;

    private String secret;

    private String version = "1.0";
    
    private String sigVersion = "1";

    private boolean verbose = true;

    private BeesClientConfiguration beesClientConfiguration;

    public BeesClientBase(BeesClientConfiguration beesClientConfiguration ) {
        if (beesClientConfiguration==null) {
            throw new IllegalArgumentException("BeesClientConfiguration cannot be null");
        }
        this.beesClientConfiguration = beesClientConfiguration;
        if (beesClientConfiguration.getServerApiUrl() != null) {
            this.serverApiUrl = beesClientConfiguration.getServerApiUrl();
        }
        if (beesClientConfiguration.getApiKey() != null) {
            this.api_key = beesClientConfiguration.getApiKey();
        }
        if (beesClientConfiguration.getSecret() != null) {
            this.secret = beesClientConfiguration.getSecret();
        }
        if (beesClientConfiguration.getFormat() != null) {
            this.format = beesClientConfiguration.getFormat();
        }
        if (beesClientConfiguration.getVersion() != null) {
            this.version = beesClientConfiguration.getVersion();
        }
    }

    public BeesClientBase(String serverApiUrl, String apiKey, String secret,
                          String format, String version) {
        this(new BeesClientConfiguration( serverApiUrl, apiKey, secret, format, version ));
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public String getRequestURL(String method, Map<String, String> methodParams)
    throws Exception {
        return getRequestURL(method, methodParams, true);
    }

    public String getRequestURL(String method, Map<String, String> methodParams, boolean asActionParam)
            throws Exception {
        HashMap<String, String> urlParams = getDefaultParameters();

        StringBuilder requestURL = getApiUrl(asActionParam ? null : method);
        requestURL.append("?");

        for (Map.Entry<String, String> entry : methodParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            urlParams.put(key, value);
        }
        
        if(asActionParam)
            urlParams.put("action", method);

        String signature = calculateSignature(urlParams);        
        Iterator<Map.Entry<String, String>> it = urlParams.entrySet().iterator();
        for (int i=0; it.hasNext(); i++) {
            Map.Entry<String, String> entry = it.next();
            String key = entry.getKey();
            String value = entry.getValue();
            if (i > 0)
                requestURL.append("&");
            requestURL.append(URLEncoder.encode(key, "UTF-8"));
            requestURL.append("=");
            requestURL.append(URLEncoder.encode(value, "UTF-8"));
        }
        
        requestURL.append("&");
        requestURL.append("sig");
        requestURL.append("=");
        requestURL.append(signature);

        return requestURL.toString();
    }
    
    private String calculateSignature(Map<String, String> entries) throws Exception
    {
        StringBuilder sigData = new StringBuilder();
        ArrayList<Map.Entry<String, String>> sortedParams = new ArrayList<Map.Entry<String, String>>(entries.entrySet());
        Collections.sort(sortedParams, new Comparator<Map.Entry<String, String>>(){
            public int compare(Entry<String, String> e1,
                    Entry<String, String> e2) {
                return e1.getKey().compareTo(e2.getKey());
            }
        });
        for (Map.Entry<String, String> entry : sortedParams) {
            String key = entry.getKey();
            String value = entry.getValue();
            sigData.append(key);
            sigData.append(value);
        }

        // append the signature
        String signature = getSignature(sigData.toString(), secret);
        return signature;
    }

    protected StringBuilder getApiUrl(String method) {
        StringBuilder requestURL = new StringBuilder();        
        requestURL.append(serverApiUrl);
        if(method != null)
        {
            requestURL.append("/");
            requestURL.append(method);
        }
        return requestURL;
    }

    private HashMap<String, String> getDefaultParameters() {
        HashMap<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("format", format);
        urlParams.put("v", version);
        urlParams.put("api_key", api_key);
        urlParams.put("timestamp", new Long(System.currentTimeMillis() / 1000)
                .toString());
        urlParams.put("sig_version", sigVersion);
        return urlParams;
    }

    public static String getSignature(String data, String secret)
            throws Exception {
        String s = data + secret;
        String sig = md5(s);
        return sig;
    }

    public static String md5(String message) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(message.getBytes("CP1252"));
            StringBuffer hex = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; ++i) {
                hex.append(Integer.toHexString((md5Bytes[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }

            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }

    public String executeRequest(String url) throws Exception {
        HttpClient httpClient = HttpClientHelper.createClient(this.beesClientConfiguration);
        GetMethod getMethod = new GetMethod(url);
        httpClient.executeMethod(getMethod);

        return getResponseString(getMethod.getResponseBodyAsStream());
    }

    private String getResponseString(InputStream ins) throws IOException {
        StringBuffer response = new StringBuffer();
        try {
            InputStreamReader isr = new InputStreamReader(ins);
            char[] chars = new char[1024];
            int numRead = isr.read(chars, 0, chars.length);
            while (numRead != -1) {
                response.append(new String(chars, 0, numRead));
                numRead = isr.read(chars, 0, chars.length);
            }
        } finally {
            ins.close();
        }
        return response.toString();
    }

    public InputStream executeCometRequest(String url) throws Exception {
        HttpClient httpClient = HttpClientHelper.createClient(this.beesClientConfiguration);
        HttpParams params = httpClient.getParams();
        params.setIntParameter(HttpConnectionParams.SO_TIMEOUT, 0);
        GetMethod getMethod = new GetMethod(url);
        httpClient.executeMethod(getMethod);
        return getMethod.getResponseBodyAsStream();
    }    

    protected void trace(String message)
    {
        if (verbose) {
            System.out.println(message);
        }
    }

    protected void traceResponse(String message)
    {
        if (verbose) System.out.println("xml response: " + message);
    }

    protected String executeUpload(String uploadURL, Map<String, String> params,
            Map<String, File> files, UploadProgress writeListener)
            throws Exception {
        HashMap<String, String> clientParams = getDefaultParameters();
        clientParams.putAll(params);

        PostMethod filePost = new PostMethod(uploadURL);
        try {
            ArrayList<Part> parts = new ArrayList<Part>();

            int fileUploadSize = 0;
            for (Map.Entry<String, File> fileEntry : files.entrySet()) {
                FilePart filePart = new FilePart(fileEntry.getKey(), fileEntry
                        .getValue());
                parts.add(filePart);
                fileUploadSize += filePart.length();
                //TODO: file params are not currently included in the signature,
                //      we should hash the file contents so we can verify them
            }

            for (Map.Entry<String, String> entry : clientParams.entrySet()) {
                parts.add(new StringPart(entry.getKey(), entry.getValue()));
            }

            // add the signature
            String signature = calculateSignature(clientParams);
            parts.add(new StringPart("sig", signature));

            ProgressUploadEntity uploadEntity = new ProgressUploadEntity(parts
                    .toArray(new Part[parts.size()]), filePost.getParams(),
                    writeListener, fileUploadSize);
            filePost.setRequestEntity(uploadEntity);
            HttpClient client = HttpClientHelper.createClient(this.beesClientConfiguration);
            client.getHttpConnectionManager().getParams().setConnectionTimeout(
                    10000);
            
            int status = client.executeMethod(filePost);
            String response = getResponseString(filePost.getResponseBodyAsStream());
            if (status == HttpStatus.SC_OK) {
                trace("upload complete, response=" + response);
            } else {
                trace("upload failed, response=" + HttpStatus.getStatusText(status));
            }
            return response;
        } finally {
            filePost.releaseConnection();
        }
    }

    class ProgressUploadEntity extends MultipartRequestEntity {
        private UploadProgress listener;
        long length;

        public ProgressUploadEntity(Part[] parts, HttpMethodParams params,
                UploadProgress listener, long length) {
            super(parts, params);
            this.listener = listener;
            this.length = length;
        }

        @Override
        public void writeRequest(OutputStream out) throws IOException {
            WriteListenerOutputStream listenStream = new WriteListenerOutputStream(
                    out, listener, length);
            super.writeRequest(listenStream);
        }
    }

    class WriteListenerOutputStream extends OutputStream {
        private OutputStream targetStream;
        private long bytesWritten;
        private boolean isClosed;
        private long bytesToSend;
        private UploadProgress writeListener;

        public WriteListenerOutputStream(OutputStream targetStream,
                UploadProgress writeListener, long length) {
            super();
            this.targetStream = targetStream;
            this.writeListener = writeListener;
            this.bytesToSend = length;
        }

        public void close() throws IOException {
            isClosed = true;
            targetStream.close();
        }

        public void flush() throws IOException {
            targetStream.flush();
        }

        public void write(byte[] b, int off, int len) throws IOException {
            targetStream.write(b, off, len);
            trackBytesWritten(len);
        }

        public void write(byte[] b) throws IOException {
            targetStream.write(b);
            trackBytesWritten(b.length);
        }

        public void write(int b) throws IOException {
            targetStream.write(b);
            trackBytesWritten(1);
        }

        public boolean isClosed() {
            return isClosed;
        }

        public long getBytesWritten() {
            return bytesWritten;
        }

        private void trackBytesWritten(long count) {
            bytesWritten += count;
            if (writeListener != null) {
                writeListener.handleBytesWritten(count, bytesWritten,
                        bytesToSend);
            }
        }
    }
    
}