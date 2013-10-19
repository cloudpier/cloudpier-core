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


package eu.cloud4soa.relational.persistence;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import eu.cloud4soa.relational.datamodel.GuaranteeTerm;
import eu.cloud4soa.relational.datamodel.SLATemplate;
import eu.cloud4soa.relational.datamodel.ServiceDescriptionTerm;
import eu.cloud4soa.relational.persistence.support.AbstractHbmDao;

@Repository
public class SLATemplateRepository extends AbstractHbmDao<SLATemplate> {
	
	@Autowired
	public SLATemplateRepository(SessionFactory sessionFactory) {
		super(sessionFactory, SLATemplate.class);
	}
	
	public void store(SLATemplate sla_template) {
		this.saveOrUpdate(sla_template);
	}

	public void update(SLATemplate sla_template) {
		super.update(sla_template);
	}

	public void delete(SLATemplate sla_template) {
		super.delete(sla_template);
	}

	public List<SLATemplate> retrieveAll() {
		return this.findAll();
	}
	
	public List<SLATemplate> retrieveAll(Long slaTemplateId) {		
		return this.find("id = ?", slaTemplateId);
	}
}
