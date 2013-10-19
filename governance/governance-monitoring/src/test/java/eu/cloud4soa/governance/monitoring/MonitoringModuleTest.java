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
package eu.cloud4soa.governance.monitoring;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.semantic.app.ApplicationDeployment;
import eu.cloud4soa.api.datamodel.semantic.ent.PaaSProvider;
import eu.cloud4soa.api.datamodel.semantic.paas.PaaSOffering;

/**
 * Note: When the build fails while running this test or setting up the
 * applicationContext the repository-test artifact could not been found or the
 * ord-context.xml of the repository-test artifact is not onto the classpath. <br>
 * So monitoringJobRepository and monitoringStatisticRepository were not
 * initialized and cannot be autowired. <br>
 * Furthermore the initialization of the applicationContext of the
 * monitoringModule / -Worker will fail as expected.
 * 
 * Hint: Spying the applicationContext might be helpful. <br>
 * Let the MonitoringModuleTest implement ApplicationContextAware and ignore all
 * test methods. <br>
 * Then look for just initialized repository beans. <br>
 * Best practise: Map<?,?> aC =
 * applicationContext.getBeansWithAnnotation(Repository.class)
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
@Ignore
@ContextConfiguration(locations = { "classpath:MonitoringModule-test-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class MonitoringModuleTest {

	@Autowired
	private MonitoringModule monitoringModule;
        
	private ApplicationInstance applicationInstance;

	@Before
	public void setUp() {
		applicationInstance = new ApplicationInstance();
		applicationInstance.getApplication().setDeployment(new ApplicationDeployment());
		PaaSOffering offer = new PaaSOffering();
		offer.setPaaSProvider(new PaaSProvider());
		applicationInstance.getApplication().getDeployment().setDeployingLocation(offer);
		applicationInstance.setAdapterUrl("http://c4sadnewyosujavaapp.testaccountname.cloudbees.net");
//		applicationInstance.setUriId("e4de2bb5-3984-4286-822c-fc485cdee824");
//		applicationInstance.setUriId("195b7068-4482-4c52-87b5-e16a19b15962");
		applicationInstance.setUriId("3cef6df3-40c9-420f-8187-25f2a39151d6");
                //monitoringModule = new MonitoringModule();
	}

	@Test
	public void testWiring() {
		Assert.assertNotNull(monitoringModule);
	}

	//@Ignore
	@Test
	public void testStartMonitoring() {
            String uriId=applicationInstance.getUriId();
            Assert.assertNotNull(uriId);
            Assert.assertNotNull(monitoringModule);
		monitoringModule.startMonitoring(uriId);

		Assert.assertTrue(monitoringModule.getMonitoringStatistics(applicationInstance.getUriId()).size() > 0);
	}
        /*
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
*/

	@Ignore
	@Test
	public void testRemoveMonitoringJob() {
		monitoringModule.startMonitoring(applicationInstance.getUriId());

		Assert.assertNotNull(monitoringModule.getMonitoringJob(applicationInstance.getUriId()));

		monitoringModule.stopMonitoring(applicationInstance.getUriId());

		Assert.assertFalse(monitoringModule.getMonitoringJob(applicationInstance.getUriId()).isEnabled());
	}
}
