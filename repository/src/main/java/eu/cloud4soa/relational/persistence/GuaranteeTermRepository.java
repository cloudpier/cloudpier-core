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
import eu.cloud4soa.relational.persistence.support.AbstractHbmDao;

@Repository
public class GuaranteeTermRepository extends AbstractHbmDao<GuaranteeTerm> {

	@Autowired
	public GuaranteeTermRepository(SessionFactory sessionFactory) {
		super(sessionFactory, GuaranteeTerm.class);
	}
	
	public void store(GuaranteeTerm guarantee_term) {
		this.saveOrUpdate(guarantee_term);
	}

	public void update(GuaranteeTerm guarantee_term) {
		super.update(guarantee_term);
	}

	public void delete(GuaranteeTerm guarantee_term) {
		super.delete(guarantee_term);
	}

	public List<GuaranteeTerm> retrieveAll() {
		return this.findAll();
	}
	
	public List<GuaranteeTerm> retrieveAll(Long slaTemplateId) {
        List<GuaranteeTerm> guaranteeTermList =	this.find("slaTemplate.id = ?", slaTemplateId);
        return guaranteeTermList;
	}
}
