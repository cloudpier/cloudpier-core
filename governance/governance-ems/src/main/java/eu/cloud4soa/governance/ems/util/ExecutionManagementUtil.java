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
package eu.cloud4soa.governance.ems.util;

//import cloudadapter.Adapter;
import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.codec.binary.Base64;

public class ExecutionManagementUtil {

    final static Logger logger = LoggerFactory.getLogger(ExecutionManagementUtil.class);

    public static <T> String extractAdapterLocation(ApplicationInstance applicationInstance) {
        String target = "";


        //get adapter location URL from database


        return target;
    }

    /**
     * QuickFix for WP3
     *
     * private final static String cloudControlAdapterLocation =
     * "c4s.cloudcontrolled.com";
     *
     * public static <T> String extractAdapterLocation(T t){ return
     * cloudControlAdapterLocation;
     *
     * }
     */
    public static <T> void infixAdapterLocation(T t, String url) {
    }

    public static <T> String extractDeploymentLocation(T t) {
        return null;
    }

    public static <T> void infixDeploymentLocation(T t, String url) {
        /*
         * only if we monitor 1:1 - adapter:application
         */
//		if(t instanceof ApplicationInstance){
//			((ApplicationInstance)t).setDeploymentIP(url);
//		}
    }

    public static String getCloudBeesAdapterPath() {
//        System.out.println( ClassLoader.getSystemResource("extras/CloudBeesC4SAdapter-1.0-SNAPSHOT.war"));
        System.out.println(ExecutionManagementUtil.class.getClassLoader().getResourceAsStream("extras/CloudBeesC4SAdapter-1.0-SNAPSHOT.war"));
        String path = "";
        try {
//        InputStream inputStream=ClassLoader.getSystemResourceAsStream("extras/CloudBeesC4SAdapter-1.0-SNAPSHOT.war");
            InputStream inputStream = ExecutionManagementUtil.class.getClassLoader().getResourceAsStream("extras/CloudBeesC4SAdapter-1.0-SNAPSHOT.war");
            // write the inputStream to a FileOutputStream
            File tmp_file = new File(System.getProperty("java.io.tmpdir")+"/"+"CloudBeesC4SAdapter-1.0-SNAPSHOT.war");
            OutputStream out = new FileOutputStream(tmp_file);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            inputStream.close();
            out.flush();
            out.close();

            logger.debug("New file created!");

            path = tmp_file.getCanonicalPath();
            logger.debug(path);
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }


        return path;
    }

    public static String getCloudFoundryAdapterPath() {
        System.out.println(ExecutionManagementUtil.class.getClassLoader().getResourceAsStream("extras/CloudFoundryC4SAdapter-1.0-SNAPSHOT.war"));
        String path = "";
        try {
            InputStream inputStream = ExecutionManagementUtil.class.getClassLoader().getResourceAsStream("extras/CloudFoundryC4SAdapter-1.0-SNAPSHOT.war");
            // write the inputStream to a FileOutputStream
            File tmp_file = new File(System.getProperty("java.io.tmpdir")+"/"+"CloudFoundryC4SAdapter-1.0-SNAPSHOT.war");
            OutputStream out = new FileOutputStream(tmp_file);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            inputStream.close();
            out.flush();
            out.close();

            logger.debug("New file created!");

            path = tmp_file.getCanonicalPath();
            logger.debug(path);
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }


        return path;
    }

    public static String getEmptyApplicationPath() {
        System.out.println(ExecutionManagementUtil.class.getClassLoader().getResourceAsStream("extras/EmptyApp.war"));
        String path = "";
        try {
            InputStream inputStream = ExecutionManagementUtil.class.getClassLoader().getResourceAsStream("extras/EmptyApp.war");
            // write the inputStream to a FileOutputStream
            File tmp_file = new File(System.getProperty("java.io.tmpdir")+"/"+"EmptyApp.war");
            OutputStream out = new FileOutputStream(tmp_file);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            inputStream.close();
            out.flush();
            out.close();

            logger.debug("New file created!");

            path = tmp_file.getCanonicalPath();
            logger.debug(path);
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }


        return path;
    }
    
    public static String getBeanstalkAdapterPath() {
        System.out.println(ExecutionManagementUtil.class.getClassLoader().getResourceAsStream("extras/BeanstalkC4SAdapter-1.0-SNAPSHOT.war"));
        String path = "";
        try {
            InputStream inputStream = ExecutionManagementUtil.class.getClassLoader().getResourceAsStream("extras/BeanstalkC4SAdapter-1.0-SNAPSHOT.war");
            // write the inputStream to a FileOutputStream
            File tmp_file = new File(System.getProperty("java.io.tmpdir")+"/"+"BeanstalkC4SAdapter-1.0-SNAPSHOT.war");
            OutputStream out = new FileOutputStream(tmp_file);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            inputStream.close();
            out.flush();
            out.close();

            logger.debug("New file created!");

            path = tmp_file.getCanonicalPath();
            logger.debug(path);
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }


        return path;
    }

    // ssh://a2f3b6f89a8840e5bb9a46d714bead0a@c4swgit-cloud4soaexpress.rhcloud.com/~/git/c4swgit.git/
    public static String[] convertFromOpenshift(String str) {
        str = str.trim();
        String[] ret = new String[]{"", ""};
        String[] temps = str.split("~");
        ret[0] = temps[0].substring(6, temps[0].length() - 1);
        ret[1] = "~" + temps[1];
        return ret;
    }

    // git@heroku.com:c4stest11.git
    public static String[] convertFromHeroku(String str) {
        str = str.trim();
        String[] ret = new String[]{"", ""};
        String[] temps = str.split(":");
        ret[0] = temps[0];
        ret[1] = temps[1];
        return ret;
    }
    // ssh://c4sadjledapp1@cloudcontrolled.com/repository.git
    public static String[] convertFromCloudControl(String str) {
        str = str.trim();
        String replaced = str.replace("ssh://", "");
        String[] ret = new String[]{"", ""};
        String[] temps = replaced.split(".com/");
        ret[0] = temps[0]+".com";
        ret[1] = "/"+temps[1];
        return ret;
    }

    public static void GenerateSSHKeyPair(String userid) {
        int retvalue;
        try {
            System.out.println(System.getProperty("user.home"));
            System.err.print(" SSH RSA KEY generation...");
            System.err.flush();
            Runtime.getRuntime().exec(new String[]{"ssh-keygen", //
                        "-q" /* quiet */, //
                        "-t", "rsa", //
                        "-P", "", //
                        "-C", userid + "@cloud", //
                        "-f", System.getProperty("user.home") + "/.ssh/" + userid //
                    }).waitFor();

            System.err.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//EoM

    public static String createCloudControlApiKey(String mail, String password){
        String apiKey="";
        
         String orig = mail+":"+password;

        //encoding  byte array into base 64
        byte[] encoded = Base64.encodeBase64(orig.getBytes());     
      
        System.out.println("Original String: " + orig );
        String encString=new String(encoded);
        System.out.println("Base64 Encoded String : " +encString );

        
        apiKey=encString;
        return apiKey;
    }
    public static String getPublicKey(long userid) {
        String sshkey = "";
        //Read C4SOA-Proxy key If Exists Pr register            
        String pubkeypath = System.getProperty("user.home") + "/.ssh/" + userid + ".pub";
        System.out.println("PUBLIC KEY PATH:" + pubkeypath);
        //Read the key
        try {
            BufferedReader br = new BufferedReader(new FileReader(pubkeypath));
            String strLine = "";
            while ((strLine = br.readLine()) != null) {
                // Print the content on the console
                sshkey += strLine;
                System.out.println("strLine:" + strLine);
            }

            System.out.println("SSH key exists and is " + sshkey);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Public key for user " + userid + " DOES NOT exist");

        } //SSH key does not exist

        System.out.println("The final key is " + sshkey);

        return sshkey;
    }
    

    public static void main(String[] args) {
    String url="ssh://a2f3b6f89a8840e5bb9a46d714bead0a@c4swgit-cloud4soaexpress.rhcloud.com/~/git/c4swgit.git";
        System.out.println("url=ssh://a2f3b6f89a8840e5bb9a46d714bead0a@c4swgit-cloud4soaexpress.rhcloud.com/~/git/c4swgit.git");
        String[] convertFromOpenshift = convertFromOpenshift(url);
        System.out.println("1:"+convertFromOpenshift[0]);
        System.out.println("2:"+convertFromOpenshift[1]);
        url="git@heroku.com:c4stest11.git";
        String[] convertHeroku = convertFromHeroku(url);
        System.out.println("heroku");
        System.out.println("url=git@heroku.com:c4stest11.git");
        System.out.println("1:"+convertHeroku[0]);
        System.out.println("2:"+convertHeroku[1]);
        url="ssh://c4sadjledapp1@cloudcontrolled.com/repository.git";
        String[] convertCC = convertFromCloudControl(url);
        System.out.println("cloudcontrol");
        System.out.println("url="+ url);
        System.out.println("1:"+convertCC[0]);
        System.out.println("2:"+convertCC[1]);
        
    }
    
}//EoC
