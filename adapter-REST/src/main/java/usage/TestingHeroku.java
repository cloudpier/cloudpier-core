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
import eu.cloud4soa.adapter.rest.exception.AdapterClientException;
import eu.cloud4soa.adapter.rest.impl.AdapterClientCXF;
import eu.cloud4soa.adapter.rest.request.CreateApplicationRequest;
import eu.cloud4soa.adapter.rest.request.CreateDatabaseRequest;
import eu.cloud4soa.adapter.rest.request.CreateSSHKeyRequest;
import eu.cloud4soa.adapter.rest.request.DeleteApplicationRequest;
import eu.cloud4soa.adapter.rest.request.DeleteDatabaseRequest;
import eu.cloud4soa.adapter.rest.request.DeleteSSHKeyRequest;
import eu.cloud4soa.adapter.rest.response.Response;
import java.net.UnknownHostException;

public class TestingHeroku {

	public static final String adapterURL = "http://c4s.herokuapp.com";

    public static final String apiKey = "2c4bc784d7fcad8156c2f801375c1fe54e7f8ec7";
    public static final String secretKey = "UZPYSQVJMQLVNNVK6GSZQPRUTAZ+QKNB9QCKDWVNQMK=";   
	
    private static String applicationName = "herokutestapplication";
    
	public static void main(String[] args) throws AdapterClientException,UnknownHostException{
		
		AdapterClientCXF client = new AdapterClientCXF();
	    CustomerCredentials credentials= new CustomerCredentials(apiKey, secretKey);

	    Response<?> response;
	    // create
	    
	    CreateApplicationRequest createApplicationRequest = new CreateApplicationRequest();
	    createApplicationRequest.setBaseUrl(adapterURL);
	    createApplicationRequest.setLanguage("java");
	    createApplicationRequest.setApplicationName(applicationName);
	    response = client.send(createApplicationRequest, credentials);
	    System.out.println(response );
	    
	    CreateSSHKeyRequest createSSHKeyRequest = new CreateSSHKeyRequest();
	    createSSHKeyRequest.setBaseUrl(adapterURL);
	    createSSHKeyRequest.setApplicationName(applicationName);
	    createSSHKeyRequest.setSshKey("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDJpPjRV5uis4vJoHmmZP/Ynpq16L+fE0aReiCM9mi7+urnVNJLiwka12+TEM7y9hbfr7NBia9+x6+1eLC3yi/cuVOcDAxewN48HBrr1JMmcnxDqIHEObaAcn5NorQTvx8ondhB2kNAcg3zTFUCnUhYhVGMKmPQ/On/orMvESBh5IBpQfBv9gMGoABqauuKAP2KdcufY89GaKPsuwtCJJNOWt5DBOtORui58smcTC+JEtwzkdZlPIzYiQpwPQabgaFO2OECbVE0tWvaatcJ3RdD9S1mxP+2I//W77fddfZt+aTts4YNVWxB1a9oIowugty4TUvybhFx8j+tvb01tlHh user@host");
	    
	  //  response = client.send(createSSHKeyRequest, credentials);
	  //  System.out.println(response);
	    
	    CreateDatabaseRequest createDatabaseRequest = new CreateDatabaseRequest();
	    createDatabaseRequest.setBaseUrl(adapterURL);
	    createDatabaseRequest.setApplicationName(applicationName);
	    
	    createDatabaseRequest.setDatabaseName("whatever");
	    createDatabaseRequest.setDatabasePassword("whatever");
	    createDatabaseRequest.setDatabaseType("whatever");
	    createDatabaseRequest.setDatabaseUser("whatever");
	    
	  //  response = client.send(createDatabaseRequest, credentials);
	  //  System.out.println(response);
	    
	    // create eom
	    
	    // delete
	    
	    DeleteDatabaseRequest deleteDatabaseRequest = new DeleteDatabaseRequest();
	    deleteDatabaseRequest.setBaseUrl(adapterURL);
	    deleteDatabaseRequest.setApplicationName(applicationName);
	    deleteDatabaseRequest.setDatabaseName("whatever");
	    
	    //response = client.send(deleteDatabaseRequest, credentials);
	  //  System.out.println(response);
	    
	    DeleteSSHKeyRequest deleteSSHKeyRequest = new DeleteSSHKeyRequest();
	    deleteSSHKeyRequest.setBaseUrl(adapterURL);
	    deleteSSHKeyRequest.setApplicationName(applicationName);
	    deleteSSHKeyRequest.setSshKey("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDJpPjRV5uis4vJoHmmZP/Ynpq16L+fE0aReiCM9mi7+urnVNJLiwka12+TEM7y9hbfr7NBia9+x6+1eLC3yi/cuVOcDAxewN48HBrr1JMmcnxDqIHEObaAcn5NorQTvx8ondhB2kNAcg3zTFUCnUhYhVGMKmPQ/On/orMvESBh5IBpQfBv9gMGoABqauuKAP2KdcufY89GaKPsuwtCJJNOWt5DBOtORui58smcTC+JEtwzkdZlPIzYiQpwPQabgaFO2OECbVE0tWvaatcJ3RdD9S1mxP+2I//W77fddfZt+aTts4YNVWxB1a9oIowugty4TUvybhFx8j+tvb01tlHh user@host");
	    
	   // response = client.send(deleteSSHKeyRequest, credentials);
	   // System.out.println(response);
	    
	    DeleteApplicationRequest deleteApplicationRequest = new DeleteApplicationRequest();
	    deleteApplicationRequest.setBaseUrl(adapterURL);
	    deleteApplicationRequest.setApplicationName(applicationName);
	    
	  //  response = client.send(deleteApplicationRequest, credentials);
	  //  System.out.println(response);
	    
	    // delete eom
	}
}
