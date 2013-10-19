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

import com.amazonaws.services.cloudwatch.model.InvalidParameterValueException;
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
 * Makes a request to the Alexa Web Information Service UrlInfo action.
 */
public class BeansDeleteApplicationVersion {

     private static final String ACTION_NAME = "DeleteApplicationVersion";
  //  private static final String ACTION_NAME = "CheckDNSAvailability";
    private String application_name = "";
    //private String enviroment_name = "";
    private String app_version_label = "";
   // private String description = "";
    private static final String RESPONSE_GROUP_NAME = "Rank,ContactInfo,LinksInCount";
    private static String service_host = "elasticbeanstalk.us-east-1.amazonaws.com";
    private  String AWS_BASE_URL = "https://" + service_host + "/?";
    private static final String HASH_ALGORITHM = "HmacSHA256";

    private static final String DATEFORMAT_AWS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private static final String SERVICE_VERSION = "2010-12-01";
    private static String DEFAULT_ENCODING = "UTF-8";

    private String accessKeyId;
    private String secretAccessKey;
    private String war;

    
   public BeansDeleteApplicationVersion(){

    }
    public BeansDeleteApplicationVersion(String accessKeyId, String secretAccessKey,String application,String appversion) {
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
       //this.enviroment_name = environment;
        this.application_name = application;
        this.app_version_label = appversion;
        //this.description = description;
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
        queryParams.put("ApplicationName", application_name);

        // queryParams.put("CNAMEPrefix", enviroment_name);
        queryParams.put("AWSAccessKeyId", accessKeyId);///////COMMON
        //queryParams.put("SolutionStackName", "32bit Amazon Linux running Tomcat 7");

        //queryParams.put("Description", description);

       // queryParams.put("EnvironmentName", enviroment_name);
        queryParams.put("Operation", ACTION_NAME);///////COMMON

        queryParams.put("SignatureVersion", "2");///////COMMON
        queryParams.put("SignatureMethod", HASH_ALGORITHM);///////COMMON

       // queryParams.put("SourceBundle.S3Bucket", "elasticbeanstalk-us-east-1-601887962018");
     //   queryParams.put("SourceBundle.S3Key", "201119338g-SimpleWar.war");
        queryParams.put("Timestamp", timestamp);///////COMMON
        //queryParams.put("Url", site);
        queryParams.put("Version", SERVICE_VERSION);///////COMMON(version of REST API)

        //if VersionLabel is used , applicationwill be updated also and deployd
        //if not, we should use also the updateenviromnet
        queryParams.put("VersionLabel", app_version_label);

        String query = "";
        boolean first = true;
        for (String name : queryParams.keySet()) {
            if (first)
                first = false;
            else
                query += "&";

            //query += name + "=" + URLEncoder.encode(queryParams.get(name), "UTF-8");
            query += name + "=" + urlEncode(queryParams.get(name), false);
        }

        return query;
    }


//    public static void main(String[] args) throws Exception {
        /*if (args.length < 3) {
        System.err.println("Usage: UrlInfo ACCESS_KEY_ID " +
        "SECRET_ACCESS_KEY site");
        System.exit(-1);
        }

        // Read command line parameters

        String accessKey = args[0];
        String secretKey = args[1];
        String site = args[2];*/
        /*
        String accessKey = "AKIAJRSZ7FBNKBAOUR6A";
        String secretKey = "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";
        String war = "cloud4soa";

        BeansCreateEnvironment updater = new BeansCreateEnvironment(accessKey, secretKey, war);

        String query = updater.buildQuery();

        String toSign = "GET\n" + SERVICE_HOST + "\n/\n" + query;

        System.out.println("String to sign:\n" + toSign + "\n");

        String signature = updater.generateSignature(toSign);

        String uri = AWS_BASE_URL + query + "&Signature=" +
                URLEncoder.encode(signature, "UTF-8");

        System.out.println("Making request to:\n");
        System.out.println(uri + "\n");

        // Make the Request

        String xmlResponse = makeRequest(uri);

        // Print out the XML Response

        System.out.println("Response:\n");
        System.out.println(xmlResponse);
    }
    */


    public void deleteApplicationVersion(String accessKeyId, String secretAccessKey, String application, String appversion ) throws InvalidParameterValueException {

        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
        //this.enviroment_name = environment;
        this.application_name = application;
        this.app_version_label = appversion;
        //this.description = description;


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
            // String accessKey = "AKIAJRSZ7FBNKBAOUR6A";
            //String secretKey = "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";
            // String war = "cloud4soa";
            BeansDeleteApplicationVersion updater = new BeansDeleteApplicationVersion(accessKeyId, secretAccessKey, application,appversion);
            String query = updater.buildQuery();
            String toSign = "GET\n" + service_host + "\n/\n" + query;
            System.out.println("String to sign:\n" + toSign + "\n");
            String signature = null;
            try {
                signature = updater.generateSignature(toSign);
            } catch (SignatureException ex) {
                Logger.getLogger(BeansDeleteApplicationVersion.class.getName()).log(Level.SEVERE, null, ex);
            }
            //String uri = AWS_BASE_URL + query + "&Signature=" + URLEncoder.encode(signature, "UTF-8");
            String uri = AWS_BASE_URL + query + "&Signature=" + urlEncode(signature, false);
            System.out.println("Making request to:\n");
            System.out.println(uri + "\n");
            // Make the Request
            String xmlResponse = null;
            try {
                xmlResponse = makeRequest(uri);
            } catch (IOException ex) {
                Logger.getLogger(BeansDeleteApplicationVersion.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Print out the XML Response
            System.out.println("Response:\n");
            System.out.println(xmlResponse);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BeansDeleteApplicationVersion.class.getName()).log(Level.SEVERE, null, ex);
        }
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
