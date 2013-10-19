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


package eu.cloud4soa.relational.persistence.support;

import eu.cloud4soa.relational.datamodel.AbstractModel;
import java.io.Serializable;
import java.util.List;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;



/**
 * Abstract class for providing hbm/orm operations (persist, retrieve, delete etc.)
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 * 
 */
@SuppressWarnings({"unchecked","hiding"})
@Component
public abstract class AbstractHbmDao<T extends AbstractModel<T>> extends HibernateDaoSupport implements IHbmDao<T> {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	protected Class<T> clazz;

	public AbstractHbmDao() {
	}

	public AbstractHbmDao(SessionFactory sessionFactory, Class<T> clazz) {
		this.setSessionFactory(sessionFactory);
		this.clazz = clazz;
	}

	public <T> void delete(T t) {
		log.debug("Deleting " + t.getClass());

		this.getHibernateTemplate().delete(t);
	}

	public <T> void deleteAll() {
		log.debug("Deleting all of " + this.clazz);

		this.getHibernateTemplate().deleteAll(
				this.getHibernateTemplate().loadAll(this.clazz));
	}

	public <T> void evict(T t) {
		this.getHibernateTemplate().evict(t);
	}

	public <T> List<T> find(String queryPart , Object...args){
		return this.getHibernateTemplate().find("FROM " + this.clazz.getName() +" WHERE " + queryPart, args);
	}
	
	public <T> List<T> findAll() {
		log.debug("Loading all Rows of " + this.clazz);
		
		return (List<T>) this.getHibernateTemplate().loadAll(this.clazz);
	}

	public <T> List<T> findBy(String field, Object value) {
		log.debug("Finding by " + field + " = " + value.toString() + " of " + this.clazz);

		return this.getHibernateTemplate().find("from " + this.clazz.getName() + " where " + field + "=?",
				value);
	}

	public <T> T findById(Serializable id) {
		log.debug("Loading " + this.clazz.getClass() + " by ID");
		return ((T) this.getHibernateTemplate().load(this.clazz, id));
	}

	public <T> T findUniqueBy(String field, Object value) {
		log.debug("Finding unique by " + field + " = " + value.toString()
				+ " of " + this.clazz);
		
		String query = "from " + this.clazz.getName() + " " + "where " + field
				+ "=?";
		List<T> entities = this.getHibernateTemplate().find(query, value);

		if (entities == null || entities.size() == 0) {
			return null;
		} else if (entities.size() == 1) {
			return entities.get(0);
		} else {
			throw new RuntimeException("The field " + field + " is not unique");
		}
	}

	public <T> void flush() {
		log.debug("Flushing Hibernate Session");

		this.getHibernateTemplate().flush();
	}

	public <T> T merge(T t) {
		return this.getHibernateTemplate().merge(t);
	}

	public <T> void refresh(T t) {
		this.getHibernateTemplate().refresh(t);
	}

	public <T> Serializable save(T t) {
		log.debug("Saving " + t.getClass());

		this.validate(t);
		Serializable id = this.getHibernateTemplate().save(t);
		return id;
	}

	public <T> void saveAll(T... entities) {
		log.debug("Saving Collection of untyped Entities");

		for (T entity : entities) {
			this.save(entity);
		}
	}

	public <T> void saveOrUpdate(T t) {
		log.debug("Saving or updating " + t.getClass());

		this.validate(t);
		this.getHibernateTemplate().saveOrUpdate(t);
	}

	public <T> void saveOrUpdateAll(T... ts) {
		log.debug("Saving or updating collection");

		for (T t : ts) {
			this.validate(t);
			this.getHibernateTemplate().saveOrUpdate(t);
		}
	}

	public <T> void update(T t) {
		log.debug("Updating " + t.getClass());

		this.validate(t);
		this.getHibernateTemplate().update(t);
	}

	public <T> void updateAll(T... entities) {
		log.debug("Saving Collection of " + this.clazz);

		for (T t : entities) {
			this.validate(t);
			this.update(t);
		}
	}

	/**
	 * Not supported yet.
	 */
	public <T> void validate(T t) {
		/*
		 * (non-javadoc)
		 * not supported yet
		 */
	}
}
