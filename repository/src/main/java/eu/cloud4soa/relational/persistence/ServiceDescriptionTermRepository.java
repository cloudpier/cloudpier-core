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

import eu.cloud4soa.relational.datamodel.ServiceDescriptionTerm;
import eu.cloud4soa.relational.persistence.support.AbstractHbmDao;

@Repository
public class ServiceDescriptionTermRepository extends AbstractHbmDao<ServiceDescriptionTerm> {

	@Autowired
	public ServiceDescriptionTermRepository(SessionFactory sessionFactory) {
		super(sessionFactory, ServiceDescriptionTerm.class);
	}
	
	public void store(ServiceDescriptionTerm service_description_term) {
		this.saveOrUpdate(service_description_term);
	}

	public void update(ServiceDescriptionTerm service_description_term) {
		super.update(service_description_term);
	}

	public void delete(ServiceDescriptionTerm service_description_term) {
		super.delete(service_description_term);
	}

	public List<ServiceDescriptionTerm> retrieveAll() {
		return this.findAll();
	}
	
    public List<ServiceDescriptionTerm> retrieveAll(Long slaTemplateId) {
        List<ServiceDescriptionTerm> serviceDescriptionTermList =
        		this.find("slaTemplate.id = ?", slaTemplateId);
        return serviceDescriptionTermList;
	}	
}
