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


import eu.cloud4soa.relational.datamodel.Status;
import eu.cloud4soa.relational.persistence.support.AbstractHbmDao;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/**
 * 
 * @author pgouvas
 *
 */
@Repository
public class StatusRepository extends AbstractHbmDao<Status> implements IStatusRepository {

	@Autowired
	public StatusRepository(SessionFactory sessionFactory) {
		super(sessionFactory, Status.class);
	}
	
	public void store(Status status) {
		this.saveOrUpdate(status);
	}

	public void update(Status status) {
		super.update(status);
	}

	public void delete(Status status) {
		super.delete(status);
	}

	public List<Status> retrieveAll() {
		return this.findAll();
	}	

}