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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package usage;

import eu.cloud4soa.adapter.rest.AdapterClient;
import eu.cloud4soa.adapter.rest.auth.Credentials;
import eu.cloud4soa.adapter.rest.auth.CustomerCredentials;
import eu.cloud4soa.adapter.rest.common.Operation;
import eu.cloud4soa.adapter.rest.exception.AdapterClientException;
import eu.cloud4soa.adapter.rest.impl.AdapterClientCXF;
import eu.cloud4soa.adapter.rest.request.ApplicationRequest;
import eu.cloud4soa.adapter.rest.request.CreateApplicationRequest;
import eu.cloud4soa.adapter.rest.request.CreateSSHKeyRequest;
import eu.cloud4soa.adapter.rest.request.DeleteApplicationRequest;
import eu.cloud4soa.adapter.rest.request.ListApplicationRequest;
import eu.cloud4soa.adapter.rest.request.ExtendedMonitorRequest;
import eu.cloud4soa.adapter.rest.request.OperationRequest;
import eu.cloud4soa.adapter.rest.request.Request;
import eu.cloud4soa.adapter.rest.request.UpdateApplicationRequest;
import eu.cloud4soa.adapter.rest.response.ApplicationResponse;
import eu.cloud4soa.adapter.rest.response.CreateApplicationResponse;
import eu.cloud4soa.adapter.rest.response.CreateSSHKeyResponse;
import eu.cloud4soa.adapter.rest.response.DeleteApplicationResponse;
import eu.cloud4soa.adapter.rest.response.ExtendedMonitorResponse;
import eu.cloud4soa.adapter.rest.response.ListApplicationResponse;
import eu.cloud4soa.adapter.rest.response.OperationResponse;
import eu.cloud4soa.adapter.rest.response.UpdateApplicationResponse;
import eu.cloud4soa.api.util.exception.adapter.Cloud4SoaException;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

/**
 *
 * @author jled
 */
public class TestingCloudBees {
    


    
 public static void main(String[] args) throws AdapterClientException {
 
     /*
     String target = "http://c4sadapter.testaccountname.cloudbees.net";
     String api_key = "4184E8A5D19D02D9";
     String api_secret = "UZPYSQVJMQLVNNVK6GSZQPRUTAZ+QKNB9QCKDWVNQMK=";   
     
     AdapterClientCXF clientCXF = new AdapterClientCXF();
     CustomerCredentials usercreds= new CustomerCredentials(target, target);
          
     ListApplicationRequest listAppRequest = new ListApplicationRequest();
     listAppRequest.setBaseUrl(target);
          
     OperationRequest oprequest = new OperationRequest();
     oprequest.setApplicationName("cloud4soa1");
     oprequest.setOperation(Operation.stop);
     oprequest.setBaseUrl(target);
          
     clientCXF.send(listAppRequest, usercreds);
     */
     
            AdapterClient adapterClient = new AdapterClientCXF();
            String target = "http://cbadapter.testurl.cloudbees.net/";
            String api_key = "5B3C021C879DC288";
            String api_secret = "8KQIBWUEHRWTWGEIHI5EVLHCXW3YL3FQ9OYZC1NBER4=";
            String account="testurl";
            
            //CreateApplicationRequest

            CreateApplicationRequest createAppRequest = new CreateApplicationRequest();
            createAppRequest.setBaseUrl(target);
            String applicationTitle = "appapapa";
            String applicationTitleLowerCase = applicationTitle.toLowerCase();                        
            createAppRequest.setApplicationName(applicationTitleLowerCase);           
            createAppRequest.setLanguage("Java");
            
            ListApplicationRequest listAppRequest = new ListApplicationRequest();
            listAppRequest.setBaseUrl(target);
            
            ApplicationRequest appRequest= new ApplicationRequest();
            appRequest.setBaseUrl(target);
            appRequest.setApplicationName("cbadapter");
            
            
            UpdateApplicationRequest updateAppRequest= new UpdateApplicationRequest();
            updateAppRequest.setBaseUrl(target);
            updateAppRequest.setApplicationName("cbadapter");
            
            DeleteApplicationRequest deleteAppRequest= new DeleteApplicationRequest();
            deleteAppRequest.setBaseUrl(target);
            deleteAppRequest.setApplicationName("appapapa");
            
            
            OperationRequest opRequest = new OperationRequest();
            opRequest.setOperation(Operation.start);
            opRequest.setBaseUrl(target);
            opRequest.setApplicationName("ddde");
            
            ExtendedMonitorRequest monrequest = new ExtendedMonitorRequest();
            monrequest.setBaseUrl(target);
            
            
            
           // String publicKey = "5b21501361eeb8e843667d9a5ef48586f2bb200f";
           // String secretKey = publicKey;//"_"+api_secret+
            CustomerCredentials credentials = new CustomerCredentials(api_key+"_"+api_secret+"_"+account, api_secret);
            try {   
                //CreateApplicationResponse response = adapterClient.send(createAppRequest, credentials);
                //ListApplicationResponse response = adapterClient.send(listAppRequest, credentials);
                //ApplicationResponse response = adapterClient.send(appRequest, credentials);
               // DeleteApplicationResponse response = adapterClient.send(deleteAppRequest, credentials);
                ExtendedMonitorResponse response = adapterClient.send(monrequest, credentials);
                //OperationResponse response = adapterClient.send(opRequest, credentials);
               // UpdateApplicationResponse response = adapterClient.send(updateAppRequest, credentials);
                int status = response.getStatusCode().ordinal();             
                System.out.println("response:" + " - " + response.getStatusCode().toString() + " " + (status > 199 && status < 300 ? "successfull" : "failed") + " " );
                System.out.println("tostr:" +response.toString()+ " ");
                System.out.println("details:" +response.getMetrics()[0].getMetricName()+" - " +response.getMetrics()[0].getValue() + " ");
                System.out.println("details:" +response.getMetrics()[1].getMetricName()+" - " +response.getMetrics()[1].getValue() + " ");
                System.out.println("details:" +response.getMetrics()[2].getMetricName()+" - " +response.getMetrics()[2].getValue() + " ");
                System.out.println("details:" +response.getMetrics()[3].getMetricName()+" - " +response.getMetrics()[3].getValue() + " ");
                //String deployedAppUrl=response.getApplication().getUrl();
                //System.out.println(deployedAppUrl);           
                
            } catch (Exception e) {
                System.out.println("caught exception");
               System.out.println("xxxxxxx"+e.getMessage());
                //e.printStackTrace();
            }            
            
            
            
            
            
            
            
            //CreateSSHKeyRequest
  /*          
            String apikey = "5b21501361eeb8e843667d9a5ef48586f2bb200f";
            String appname = "panosc4spythonapp1";
            //String sshkey = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDQ1KOUq3l7smVjvbaO1+fTNsldXrKk+A+z/H4nR0g9UdVFRyvVvmFwSBfXCzV7qZluj0evXEoSfAUBvq6c4ZoJ3dsDR4Saq2RwxkeoYeglFU093XrABP3owt4kAQp2aNfWSoIJMguniMlwqpq+b3nmkl3OKOG9B2+r252hMS+oVfMwgcojoBSXrQufY3taEZ/8oDPGeSNXnjKD6XGQf1hWZ9LcAguc8ngf/o6gRLx9qijDFgyqUdsjqI8CSrzDySuI7dpESCx2503sQ7yhxJImz2jivTefwf/hoJHTY7u0+2tXDjBzYtwh4zUgnw0AfA2HWtXpxfGC+3b3zfvVYzAj pgouvas@aias";
            String sshkey = "";
            sshkey = getPublicKey(20);
            
            CreateSSHKeyRequest createsshrequest = new CreateSSHKeyRequest();
            createsshrequest.setBaseUrl(herokuAdapterLocation);
            createsshrequest.setApiKey(apikey);
            createsshrequest.setSshKey(sshkey);
            createsshrequest.setApplicationName(appname);
            createsshrequest.setDeploymentName(appname);
            
            String publicKey = apikey;
            String secretKey = apikey;
            CustomerCredentials credentials = new CustomerCredentials(publicKey, secretKey);            
            
            try {                  
                  CreateSSHKeyResponse response = adapterClient.send(createsshrequest, credentials);     
                  int status = response.getStatusCode().ordinal(); 
                  System.out.println("response:" + " - " + response.getStatusCode().toString() + " " + (status > 199 && status < 300 ? "successfull" : "failed") );
            } catch (AdapterClientException e) {     
               e.printStackTrace();      
            }
 */
 }//EoM main
 
 
 public static String getPublicKey(int userid){
            String sshkey="";
            //Read C4SOA-Proxy key If Exists Pr register            
            String pubkeypath = System.getProperty("user.home")+"/.ssh/"+userid+".pub";
            System.out.println("PUBLIC KEY PATH:"+pubkeypath);
            //Read the key
            try {
                BufferedReader br = new BufferedReader(new FileReader(pubkeypath));
                String strLine="";
                while ((strLine = br.readLine()) != null)   {
                    // Print the content on the console
                    sshkey+=strLine;
                    System.out.println("strLine:"+strLine);
                 }     

                System.out.println("SSH key exists and is "+sshkey);
            } catch (Exception ex) {     
                ex.printStackTrace();
                System.out.println("Public key for user "+userid+ " DOES NOT exist");
                
            } //SSH key does not exist
            
            System.out.println("The final key is "+sshkey);        
     
     return sshkey;
 }
 
}//EoC