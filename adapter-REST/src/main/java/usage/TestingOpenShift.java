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


package usage;

import eu.cloud4soa.adapter.rest.auth.CustomerCredentials;
import eu.cloud4soa.adapter.rest.common.Operation;
import eu.cloud4soa.adapter.rest.exception.AdapterClientException;
import eu.cloud4soa.adapter.rest.impl.AdapterClientCXF;
import eu.cloud4soa.adapter.rest.request.ApplicationRequest;
import eu.cloud4soa.adapter.rest.request.CreateApplicationRequest;
import eu.cloud4soa.adapter.rest.request.CreateDatabaseRequest;
import eu.cloud4soa.adapter.rest.request.CreateSSHKeyRequest;
import eu.cloud4soa.adapter.rest.request.DeleteApplicationRequest;
import eu.cloud4soa.adapter.rest.request.DeleteDatabaseRequest;
import eu.cloud4soa.adapter.rest.request.DeleteSSHKeyRequest;
import eu.cloud4soa.adapter.rest.request.ListApplicationRequest;
import eu.cloud4soa.adapter.rest.request.OperationRequest;
import eu.cloud4soa.adapter.rest.request.UpdateApplicationRequest;
import eu.cloud4soa.adapter.rest.response.Response;
import eu.cloud4soa.adapter.rest.auth.CustomerCredentials;
import eu.cloud4soa.adapter.rest.response.ApplicationResponse;
import eu.cloud4soa.adapter.rest.response.CreateApplicationResponse;
import eu.cloud4soa.adapter.rest.response.CreateDatabaseResponse;
import eu.cloud4soa.adapter.rest.response.DeleteApplicationResponse;
import eu.cloud4soa.adapter.rest.response.ListApplicationResponse;
import eu.cloud4soa.adapter.rest.response.OperationResponse;

public class TestingOpenShift {

   // public static final String target = "http://c4sadapter-cloud4soaexpress.rhcloud.com/";
    public static final String target = "http://rhoshiftadapter.testurl.cloudbees.net/";

    public static final String apiKey = "g.ledakis@gmail.com";
    public static final String secretKey = "!depR66!";   

    private static String applicationName = "Super";
    
	public static void main(String[] args) throws AdapterClientException{
		
		AdapterClientCXF adapterClient = new AdapterClientCXF();
	    //CustomerCredentials credentials= new CustomerCredentials(apiKey, secretKey);
            CustomerCredentials credentials = new CustomerCredentials(apiKey+"_"+secretKey, secretKey);

	   // Response<?> response;
	    // create

            CreateApplicationRequest createAppRequest = new CreateApplicationRequest();
            createAppRequest.setBaseUrl(target);
            String applicationTitle = "testpython";
            //String applicationTitleLowerCase = applicationTitle.toLowerCase();                        
            createAppRequest.setApplicationName(applicationTitle);           
            createAppRequest.setLanguage("Java");
            
            ListApplicationRequest listAppRequest = new ListApplicationRequest();
            listAppRequest.setBaseUrl(target);
            
            ApplicationRequest appRequest= new ApplicationRequest();
            appRequest.setBaseUrl(target);
            appRequest.setApplicationName("test1");
            
            
            UpdateApplicationRequest updateAppRequest= new UpdateApplicationRequest();
            updateAppRequest.setBaseUrl(target);
            updateAppRequest.setApplicationName("cbadapter");
            
            DeleteApplicationRequest deleteAppRequest= new DeleteApplicationRequest();
            deleteAppRequest.setBaseUrl(target);
            deleteAppRequest.setApplicationName("test111");
            
            
            OperationRequest opRequest = new OperationRequest();
            opRequest.setOperation(Operation.start);
            opRequest.setBaseUrl(target);
            opRequest.setApplicationName("super");
            
            CreateDatabaseRequest createDbRequest = new CreateDatabaseRequest();
            createDbRequest.setApplicationName("test1");
            createDbRequest.setBaseUrl(target);
            createDbRequest.setDatabaseName("test1");
            createDbRequest.setDeploymentName("test1");
            
            
           // String publicKey = "5b21501361eeb8e843667d9a5ef48586f2bb200f";
           // String secretKey = publicKey;//"_"+api_secret+
            try {   
                CreateApplicationResponse response = adapterClient.send(createAppRequest, credentials);
                //ListApplicationResponse response = adapterClient.send(listAppRequest, credentials);
                //ApplicationResponse response = adapterClient.send(appRequest, credentials);
                
                //DeleteApplicationResponse response = adapterClient.send(deleteAppRequest, credentials);
               //OperationResponse response = adapterClient.send(opRequest, credentials);
               // UpdateApplicationResponse response = adapterClient.send(updateAppRequest, credentials);
                //CreateDatabaseResponse response = adapterClient.send(createDbRequest, credentials);
                int status = response.getStatusCode().ordinal();             
                System.out.println("response:" + " - " + response.getStatusCode().toString() + " " + (status > 199 && status < 300 ? "successfull" : "failed") + " " );
                //String deployedAppUrl=response.getApplication().getUrl();
                //String gitUri=response.getApplication().getRepository();
                //System.out.println(deployedAppUrl);           
                //System.out.println(gitUri);           
                
            } catch (Exception e) {
                System.out.println("caught exception");
               System.out.println("xxxxxxx"+e.getMessage());
                //e.printStackTrace();
            }            
            
            
	  //  response = client.send(deleteApplicationRequest, credentials);
	  //  System.out.println(response);
	    
	    // delete eom
	}
}
