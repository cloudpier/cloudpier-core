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

import eu.cloud4soa.relational.datamodel.Paas;
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
public class PaasRepository extends AbstractHbmDao<Paas> implements IPaasRepository {

	@Autowired
	public PaasRepository(SessionFactory sessionFactory) {
		super(sessionFactory, Paas.class);
	}
	
	public void store(Paas paas) {
		this.saveOrUpdate(paas);
	}

	public void update(Paas paas) {
		super.update(paas);
	}

	public void delete(Paas paas) {
		super.delete(paas);
	}

	public List<Paas> retrieveAll() {
		return this.findAll();
	}	
    
    public Paas findByUriId(String uriId) {
        Paas result;
        
        List<Paas> appList = this.find("uriID = ?", uriId);
        if ( appList == null || appList.size() == 0 ) {
            result = null;
        } else if ( appList.size() > 1) {
            throw new IndexOutOfBoundsException("Found more then one PaaS with the same id");
        } else {
            result = appList.get(0);
        }
        return result;
    }

}