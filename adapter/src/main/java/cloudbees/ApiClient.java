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


package cloudbees;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
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

import javax.net.ssl.HttpsURLConnection;


/**
 *
 * @author Ledakis Giannis (SingularLogic)
 */
public class ApiClient
{
    protected String format = "xml";

    private String serverApiUrl = "https://api.cloudbees.com/api";

    private String api_key;

    private String secret;

    private String version = "1.0";

    private String sigVersion = "1";

    public ApiClient(String serverApiUrl, String apikey, String secret,
        String format, String version)
    {
        if (serverApiUrl != null) {
            this.serverApiUrl = serverApiUrl;
        }
        if (apikey != null) {
            this.api_key = apikey;
        }
        if (secret != null) {
            this.secret = secret;
        }
        if (format != null) {
            this.format = format;
        }
        if (version != null) {
            this.version = version;
        }
    }

    public String getRequestURL(String method,
        Map<String, String> methodParams) throws Exception
    {
        return getRequestURL(method, methodParams, true);
    }

    public String getRequestURL(String method,
        Map<String, String> methodParams, boolean asActionParam) throws Exception
    {
        HashMap<String, String> urlParams = getDefaultParameters();

        StringBuilder requestURL = getApiUrl(asActionParam ? null : method);
        requestURL.append("?");

        for (Map.Entry<String, String> entry : methodParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            urlParams.put(key, value);
        }

        if (asActionParam)
            urlParams.put("action", method);

        String signature = calculateSignature(urlParams);
        Iterator<Map.Entry<String, String>> it =
            urlParams.entrySet().iterator();
        for (int i = 0; it.hasNext(); i++) {
            Map.Entry<String, String> entry = it.next();
            String key = entry.getKey();
            String value = entry.getValue();
            if (i > 0)
                requestURL.append("&");
            requestURL.append(URLEncoder.encode(key, "UTF-8"));
            requestURL.append("=");
            if(value != null){
                requestURL.append(URLEncoder.encode(value, "UTF-8"));
            }
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
        ArrayList<Map.Entry<String, String>> sortedParams =
            new ArrayList<Map.Entry<String, String>>(entries.entrySet());
        Collections.sort(sortedParams,
            new Comparator<Map.Entry<String, String>>()
            {
                public int compare(Entry<String, String> e1,
                    Entry<String, String> e2)
                {
                    return e1.getKey().compareTo(e2.getKey());
                }
            });
        for (Map.Entry<String, String> entry : sortedParams) {
            String key = entry.getKey();
            String value = entry.getValue();
            sigData.append(key);
            if(value != null)
                sigData.append(value);
        }

        // append the signature
        String signature = getSignature(sigData.toString(), secret);
        return signature;
    }

    protected StringBuilder getApiUrl(String method)
    {
        StringBuilder requestURL = new StringBuilder();
        requestURL.append(serverApiUrl);
        if (method != null) {
            requestURL.append("/");
            requestURL.append(method);
        }
        return requestURL;
    }

    private HashMap<String, String> getDefaultParameters()
    {
        HashMap<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("format", format);
        urlParams.put("v", version);
        urlParams.put("api_key", api_key);
        urlParams.put("timestamp",
            new Long(System.currentTimeMillis() / 1000).toString());
        urlParams.put("sig_version", sigVersion);
        return urlParams;
    }

    public static String getSignature(String data, String secret) throws Exception
    {
        String s = data + secret;
        String sig = md5(s);
        return sig;
    }

    public static String md5(String message)
    {
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

    public String executeRequest(String url, String postBody) throws Exception
    {
        StringBuffer response = new StringBuffer();

        URL myurl = new URL(url);
        HttpURLConnection con;
        if (myurl.getProtocol().equals("https")) {
            con = (HttpsURLConnection)myurl.openConnection();
        } else {
            con = (HttpURLConnection)myurl.openConnection();
        }

        if (postBody != null) {
            con.setDoOutput(true);
            OutputStream out = con.getOutputStream();
            out.write(postBody.getBytes("utf-8"));
            out.close();
        }

        InputStream ins = null;
        try {
            ins = con.getInputStream();
            addStreamToBuffer(response, ins);
        } catch (IOException e) {
            if (ins == null && con.getResponseCode() != 200) {
                ins = con.getErrorStream();
                addStreamToBuffer(response, ins);
            }
        } finally {
            if (ins != null)
                ins.close();
        }

        return response.toString();
    }

    private void addStreamToBuffer(StringBuffer response, InputStream ins) throws IOException
    {
        InputStreamReader isr = new InputStreamReader(ins);
        char[] chars = new char[1024];
        int numRead = isr.read(chars, 0, chars.length);
        while (numRead != -1) {
            response.append(new String(chars, 0, numRead));
            numRead = isr.read(chars, 0, chars.length);
        }
    }
}
