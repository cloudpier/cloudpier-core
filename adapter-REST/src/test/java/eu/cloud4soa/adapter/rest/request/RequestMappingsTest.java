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


package eu.cloud4soa.adapter.rest.request;

import junit.framework.Assert;

import org.junit.Test;

import eu.cloud4soa.adapter.rest.aop.Method;
import eu.cloud4soa.adapter.rest.aop.Method.HttpMethod;
import eu.cloud4soa.adapter.rest.aop.Path;
import eu.cloud4soa.adapter.rest.aop.Path.Component;
import eu.cloud4soa.adapter.rest.util.ClassUtil;

/**
 * Test of REST RequestMappings
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
public class RequestMappingsTest {
	
	/*EMS*/
	@Test
	public void test_EMSRequest_spec(){
		invoke(new EMSRequest(), Method.HttpMethod.GET, "/ems", "");
	}
	/*EMS eom*/
	
	/*Monitor*/
	@Test
	public void test_MonitorRequest_spec(){
		invoke(new MonitorRequest(), Method.HttpMethod.GET, "/monitor", "");
	}
	
	@Test
	public void test_MonitorDetailRequest_spec(){
		invoke(new MonitorDetailRequest(), Method.HttpMethod.GET, "/monitor", "/detail");
	}
	/*Monitor eom*/
	
	/*create*/
	@Test
	public void test_CreateApplicationRequest_spec(){
		invoke(new CreateApplicationRequest(), Method.HttpMethod.POST, "/ems", "/application/${applicationName}");		
	}
	
	@Test
	public void test_CreateDeploymentRequest_spec(){
		invoke(new CreateDeploymentRequest(), Method.HttpMethod.POST, "/ems", "/application/${applicationName}/deployment/${deploymentName}");		
	}
	
	@Test
	public void test_CreateDatabaseRequest_spec(){
		invoke(new CreateDatabaseRequest(), Method.HttpMethod.POST, "/ems", "/application/${applicationName}/deployment/${deploymentName}/database/${databaseName}");		
	}
	
	@Test
	public void test_CreateSSHKeyRequest_spec(){
		invoke(new CreateSSHKeyRequest(), Method.HttpMethod.POST, "/ems", "/sshkey");		
	}
	/*create eom*/
	
	/*list*/
	@Test
	public void test_ListApplicationRequest_spec(){
                ListApplicationRequest aprequest=new ListApplicationRequest();
                aprequest.setBaseUrl("http://cloud4soa1.testaccountname.cloudbees.net");
		invoke(aprequest, Method.HttpMethod.GET, "/ems", "/application");
	}
	
	@Test
	public void test_ListDeploymentRequest_spec(){
		invoke(new ListDeploymentRequest(), Method.HttpMethod.GET, "/ems", "/application/${applicationName}/deployment");
	}
	
	@Test
	public void test_ListDatabaseRequest_spec(){
		invoke(new ListDatabaseRequest(), Method.HttpMethod.GET, "/ems", "/application/${applicationName}/deployment/${deploymentName}/database");
	}
	
	@Test
	public void test_ListSSHKeyRequest_spec(){
		invoke(new ListSSHKeyRequest(), Method.HttpMethod.GET, "/ems", "/sshkey");
	}
	/*list eom*/
	
	/*details*/
	@Test
	public void test_ApplicationRequest_spec(){
		invoke(new ApplicationRequest(), Method.HttpMethod.GET, "/ems", "/application/${applicationName}");
	}
	
	@Test
	public void test_DeploymentRequest_spec(){
		invoke(new DeploymentRequest(), Method.HttpMethod.GET, "/ems", "/application/${applicationName}/deployment/${deploymentName}");
	}
	
	@Test
	public void test_DatabaseRequest_spec(){
		invoke(new DatabaseRequest(), Method.HttpMethod.GET, "/ems", "/application/${applicationName}/deployment/${deploymentName}/database/${databaseName}");
	}
	/*details eom*/
	
	/*update*/
	@Test
	public void test_UpdateApplicationRequest_spec(){
		invoke(new UpdateApplicationRequest(), Method.HttpMethod.PUT, "/ems", "/application/${applicationName}");		
	}
	
	@Test
	public void test_UpdateDeploymentRequest_spec(){
		invoke(new UpdateDeploymentRequest(), Method.HttpMethod.PUT, "/ems", "/application/${applicationName}/deployment/${deploymentName}");		
	}
	
	@Test
	public void test_UpdateDatabaseRequest_spec(){
		invoke(new UpdateDatabaseRequest(), Method.HttpMethod.PUT, "/ems", "/application/${applicationName}/deployment/${deploymentName}/database/${databaseName}");		
	}
	/*update eom*/
	
	/*delete*/
	@Test
	public void test_DeleteApplicationRequest_spec(){
		invoke(new DeleteApplicationRequest(), Method.HttpMethod.DELETE, "/ems", "/application/${applicationName}");		
	}
	
	@Test
	public void test_DeleteDeploymentRequest_spec(){
		invoke(new DeleteDeploymentRequest(), Method.HttpMethod.DELETE, "/ems", "/application/${applicationName}/deployment/${deploymentName}");		
	}
	
	@Test
	public void test_DeleteDatabaseRequest_spec(){
		invoke(new DeleteDatabaseRequest(), Method.HttpMethod.DELETE, "/ems", "/application/${applicationName}/deployment/${deploymentName}/database/${databaseName}");		
	}
	
	@Test
	public void test_DeleteSSHKeyRequest_spec(){
		invoke(new DeleteSSHKeyRequest(), Method.HttpMethod.DELETE, "/ems", "/sshkey");		
	}
	/*delete eom*/
	
	/*operation*/
	@Test
	public void test_OperationRequest_spec(){
		invoke(new OperationRequest(), Method.HttpMethod.POST, "/ems", "/application/${applicationName}/operation/${operationName}");		
	}
	/*operation eom*/
	
	private <T> void invoke(Request<T> request, Method.HttpMethod method, String componentPath, String urlPath){
		String value = ClassUtil.getClassAnnotationValue(request.getClass(), Path.class, "path", String.class);
		Assert.assertEquals(urlPath, value);
		Component component = ClassUtil.getClassAnnotationValue(request.getClass(), Path.class, "component", Component.class);
		Assert.assertEquals(componentPath, component.el());
		HttpMethod m = ClassUtil.getClassAnnotationValue(request.getClass(), Method.class, "value", Method.HttpMethod.class);
		Assert.assertEquals(method, m);
		
		System.out.println(method+"\t"+component.el()+value);
	}
}
