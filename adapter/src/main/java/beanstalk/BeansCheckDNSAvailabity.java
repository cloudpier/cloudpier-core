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



package beanstalk;


import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Creating new Application. After the creation, createapplicationversion must be used
 */
public class BeansCheckDNSAvailabity {

    private static final String ACTION_NAME = "CheckDNSAvailability";
    private String cname_prefix = "SimpleWar";
   // private static final String RESPONSE_GROUP_NAME = "Rank,ContactInfo,LinksInCount";

    private String SERVICE_HOST = "elasticbeanstalk.us-east-1.amazonaws.com";
    private String AWS_BASE_URL = "https://" + SERVICE_HOST + "/?";
    private static final String HASH_ALGORITHM = "HmacSHA256";
    private static final String DATEFORMAT_AWS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private static final String SERVICE_VERSION = "2010-12-01";
    private static final String DEFAULT_ENCODING = "UTF-8";

    private String accessKeyId;
    private String secretAccessKey;
    private String war;
    private String bucket;
    private String description;

    
    public BeansCheckDNSAvailabity(String accessKeyId, String secretAccessKey, String cname_prefix) {
    this.accessKeyId = accessKeyId;
    this.secretAccessKey = secretAccessKey;
    //this.war = war;
    //this.bucket=bucket;
    // this.app_version_label =appversion;
    this.cname_prefix =cname_prefix;
    // this.description =description;
    }
    

    /**
     * Generates a timestamp for use with AWS request signing
     *
     * @param date current date
     * @return timestamp
     */
    protected static String getTimestampFromLocalTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DATEFORMAT_AWS);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        //format.setTimeZone(TimeZone.getTimeZone("GMT+3"));
        return format.format(date);
    }

    /**
     * Computes RFC 2104-compliant HMAC signature.
     *
     * @param data The data to be signed.
     * @return The base64-encoded RFC 2104-compliant HMAC signature.
     * @throws java.security.SignatureException
     *          when signature generation fails
     */
    protected String generateSignature(String data)
            throws java.security.SignatureException {
        String result;
        try {
            // get a hash key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(
                    secretAccessKey.getBytes(), HASH_ALGORITHM);

            // get a hasher instance and initialize with the signing key
            Mac mac = Mac.getInstance(HASH_ALGORITHM);
            mac.init(signingKey);

            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes());

            // base64-encode the hmac
            // result = Encoding.EncodeBase64(rawHmac);
            result = new BASE64Encoder().encode(rawHmac);

        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : "
                    + e.getMessage());
        }
        return result;
    }

    /**
     * Makes a request to the specified Url and return the results as a String
     *
     * @param requestUrl url to make request to
     * @return the XML document as a String
     * @throws IOException
     */
    public static String makeRequest(String requestUrl) throws IOException {
        URL url = new URL(requestUrl);
        URLConnection conn = url.openConnection();
        InputStream in = conn.getInputStream();

        // Read the response
        StringBuffer sb = new StringBuffer();
        int c;
        int lastChar = 0;
        while ((c = in.read()) != -1) {
            if (c == '<' && (lastChar == '>'))
                sb.append('\n');
            sb.append((char) c);
            lastChar = c;
        }
        in.close();
        return sb.toString();
    }


    /**
     * Builds the query string
     */
    protected String buildQuery()
            throws UnsupportedEncodingException {
        String timestamp = getTimestampFromLocalTime(Calendar.getInstance().getTime());

        Map<String, String> queryParams = new TreeMap<String, String>();
        //queryParams.put("Action", ACTION_NAME);
      //  queryParams.put("ApplicationName", application_name);
         queryParams.put("CNAMEPrefix", cname_prefix);
        queryParams.put("AWSAccessKeyId", accessKeyId);
        //queryParams.put("Description", "descriptionversion1");
        queryParams.put("Operation", ACTION_NAME);
        queryParams.put("SignatureVersion", "2");
        queryParams.put("SignatureMethod", HASH_ALGORITHM);
  //      queryParams.put("SourceBundle.S3Bucket", bucket);
   //     queryParams.put("SourceBundle.S3Key", war);
        queryParams.put("Timestamp", timestamp);
        //queryParams.put("Url", site);
        queryParams.put("Version", SERVICE_VERSION);
   //     queryParams.put("VersionLabel", APP_VERSION_LABEL);

        String query = "";
        boolean first = true;
        for (String name : queryParams.keySet()) {
            if (first)
                first = false;
            else
                query += "&";

           // query += name + "=" + URLEncoder.encode(queryParams.get(name), "UTF-8");
            query += name + "=" + urlEncode(queryParams.get(name), false);
        }

        return query;
    }


    public boolean CheckAvailability( ) {
       boolean ret = false;


        try {
            /*if (args.length < 3) {
            System.err.println("Usage: UrlInfo ACCESS_KEY_ID " +
            "SECRET_ACCESS_KEY site");
            System.exit(-1);
            }
            // Read command line parameters
            String accessKey = args[0];
            String secretKey = args[1];
            String site = args[2];*/
            //String accessKey = "AKIAJRSZ7FBNKBAOUR6A";
            //  String secretKey = "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";
            //String war = "cloud4soa";
            // cname_prefix=application;
            //  BeansCheckDNSAvailabity newapp = new BeansCheckDNSAvailabity( accessKeyId, secretAccessKey,cname_prefix);
            String query = buildQuery();
            String toSign = "GET\n" + SERVICE_HOST + "\n/\n" + query;
            System.out.println("String to sign:\n" + toSign + "\n");
            String signature = generateSignature(toSign);
            //String uri = AWS_BASE_URL + query + "&Signature=" + URLEncoder.encode(signature, "UTF-8");
            String uri = AWS_BASE_URL + query + "&Signature=" + urlEncode(signature, false);
            System.out.println("Making request to:\n");
            System.out.println(uri + "\n");
            // Make the Request
            String xmlResponse = makeRequest(uri);
            // Print out the XML Response
            System.out.println("Response:\n");
            System.out.println(xmlResponse);
            //CheckDNSAvailabilityResult res= new CheckDNSAvailabilityResult(xmlResponse);
            String isavailable = utils.FindElementFromResponse.parseResponse1stlevel(xmlResponse, "CheckDNSAvailabilityResult", "Available");
            if (isavailable.equalsIgnoreCase("true")) {
                ret = true;
            }
        } catch (Exception ex) {
            Logger.getLogger(BeansCheckDNSAvailabity.class.getName()).log(Level.SEVERE, null, ex);
        }

           return ret;

    }

            private String urlEncode(String value, boolean path) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(value, DEFAULT_ENCODING)
                                        .replace("+", "%20")
                                        .replace("*", "%2A")
                                        .replace("%7E","~");
            if (path) {
                encoded = encoded.replace("%2F", "/");
            }
        } catch (UnsupportedEncodingException ex) {
          System.out.println("Unsupported Encoding Exception");
            throw new RuntimeException(ex);
        }
        return encoded;

            
    }




}
