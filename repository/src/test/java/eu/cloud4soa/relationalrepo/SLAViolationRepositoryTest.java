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


package eu.cloud4soa.relationalrepo;

import java.util.ArrayList;
import java.util.List;

import eu.cloud4soa.relational.datamodel.Breach;
import eu.cloud4soa.relational.datamodel.SLAViolation;
import eu.cloud4soa.relational.persistence.SLAEnforcementJobRepository;
import eu.cloud4soa.relational.persistence.SLAViolationRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:repository-context.xml"})
public class SLAViolationRepositoryTest {

	@Autowired
	private SLAViolationRepository slaViolationRepository;
	
	@Autowired
	private SLAEnforcementJobRepository slaEnforcementJobRepository;

	private SLAViolation slaViolation;
	
	@Before()
	public void setUp(){
		slaViolation = new SLAViolation("SampleApplication", "ResponseCode",
										 99, 88, null);
		System.out.println(slaViolation);
	}
	
	@Test
	public void testStore(){
		slaViolationRepository.store(slaViolation);
		Assert.assertTrue(0<slaViolationRepository.retrieveAllSLAViolations().size());
	}
	
	@Test
	@Transactional
	public void testDelete(){
		//slaViolationRepository.save(slaViolation);
		slaViolationRepository.store(slaViolation);
		List<SLAViolation> violations = slaViolationRepository.retrieveAllSLAViolations(); 
		//int size = slaViolationRepository.retrieveAllSLAViolations().size();
		int size = violations.size();
		slaViolationRepository.delete(slaViolation);
		int nsize = slaViolationRepository.retrieveAllSLAViolations().size();
		Assert.assertTrue(size>nsize);
	}
	
	@Test
	@Transactional
	public void testFindAll(){
		testStore();
	}
	
	//TODO: implement this
	@Test
	public void testFindAllBySLAEnforcementJob(){

	}
}
