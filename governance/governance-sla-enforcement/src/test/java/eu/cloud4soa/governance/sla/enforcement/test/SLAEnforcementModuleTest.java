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
package eu.cloud4soa.governance.sla.enforcement.test;


import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.semantic.app.ApplicationDeployment;
import eu.cloud4soa.api.datamodel.semantic.ent.PaaSProvider;
import eu.cloud4soa.api.datamodel.semantic.paas.PaaSOffering;
import eu.cloud4soa.api.governance.sla.enforcement.ISLAViolation;
import eu.cloud4soa.governance.monitoring.MonitoringModule;
import eu.cloud4soa.governance.sla.enforcement.SLAEnforcementModule;

/**
 * Note:
 * 		When the build fails while running this test or setting up the applicationContext
 * 		the repository-test artifact could not been found or the ord-context.xml 
 * 		of the repository-test artifact is not onto the classpath. <br>
 * 		So monitoringJobRepository and monitoringStatisticRepository were not initialized and cannot be autowired. <br>
 * 		Furthermore the initialization of the applicationContext of the monitoringModule / -Worker will fail as expected.
 * 
 * Hint: Spying the applicationContext might be helpful. <br>
 * 		 Implement ApplicationContextAware and ignore all test methods. <br> 
 * 		 Then look for just initialized repository beans. <br>
 * 		 Best practise: Map<?,?> aC = applicationContext.getBeansWithAnnotation(Repository.class)
 * 
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:*SLAEnforcementModule-test-context.xml"})
public class SLAEnforcementModuleTest{
	
	final Logger logger = LoggerFactory.getLogger(SLAEnforcementModuleTest.class);

	@Autowired
	private MonitoringModule monitoringModule;

	@Autowired
	private SLAEnforcementModule slaEnforcementModule;
	
	private ApplicationInstance applicationInstance; 
	
	@Before
	public void setUp(){		
		applicationInstance = new ApplicationInstance();
		applicationInstance.getApplication().setDeployment(new ApplicationDeployment());
		PaaSOffering offer = new PaaSOffering();
		offer.setPaaSProvider(new PaaSProvider());
		applicationInstance.getApplication().getDeployment().setDeployingLocation(offer);
		//applicationInstance.setDeploymentIP("http://c4s.localhost");
		applicationInstance.setDeploymentIP("http://c4s.cloudcontrolled.com");
		applicationInstance.setUriId("testStartMonitoring");
		// TODO: Should we change the above line to something like: applicationInstance.setUriId("testStartSLAEnforcement");
		
	}
	
	@Test
	public void testWiring(){		
		Assert.assertNotNull(monitoringModule);
	 	Assert.assertNotNull(slaEnforcementModule);
	}

	@Ignore @Test
	public void testStartEnforcement(){
		monitoringModule.startMonitoring(applicationInstance.getUriId());
		slaEnforcementModule.startEnforcement(applicationInstance);
		
		try {
			Thread.sleep(80000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		List<ISLAViolation> violations = slaEnforcementModule.getSLAViolations(applicationInstance);
		Iterator<ISLAViolation> iterator = violations.iterator();
		while (iterator.hasNext()) {
			ISLAViolation violation = iterator.next();
			System.out.println(violation);
		}

		Assert.assertTrue(slaEnforcementModule.getSLAViolations(applicationInstance).size()>0);
	}
	

}
