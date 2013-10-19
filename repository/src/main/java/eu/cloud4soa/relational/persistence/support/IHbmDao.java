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

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 * 
 * @param <T>
 */
@SuppressWarnings("hiding")
public interface IHbmDao<T> {

	/**
	 * Deletes the given entity from the persistent layer.
	 * 
	 * @param t
	 *            the entity to handle with
	 */
	public <T> void delete(T t);

	/**
	 * Delete all concerning entities from the persistent layer.
	 */
	public <T> void deleteAll();

	/**
	 * Removes the entity from the first/second level cache. <br>
	 * Note: this might fail when the transaction already was closed or flushed
	 * in time with that method call
	 * 
	 * @param t
	 *            the entity to handle with
	 */
	public <T> void evict(T t);

	/**
	 * Find all concering entities.
	 * 
	 * @return matches all rows of the concerning table
	 */
	public <T> List<T> findAll();

	/**
	 * Find entity by given field with given value.
	 * 
	 * @param field
	 *            the property to look for
	 * @param value
	 *            the value of the property to look for
	 * @return matches of all rows of the concerning table with the given
	 *         properties
	 */
	public <T> List<T> findBy(String field, Object value);

	/**
	 * Find unique entity by given field with given value.
	 * 
	 * @param field
	 *            the unique property to look for
	 * @param value
	 *            the unique value of the property to look for
	 * @return matches of all rows of the concerning table with the given
	 *         properties
	 */
	public <T> T findUniqueBy(String field, Object value);

	/**
	 * Flushes the transaction.
	 */
	public <T> void flush();

	/**
	 * Merging the given entity with the concerning persisted copy.
	 * 
	 * @param t
	 *            the entity to handle with
	 * @return T the merged result
	 */
	public <T> T merge(T t);

	/**
	 * Refreshes and merges the given entity and loads all persisted and not
	 * retrieved properties.
	 * 
	 * @param t
	 *            the entity to handle with
	 */
	public <T> void refresh(T t);

	/**
	 * Persists the given entity.
	 * 
	 * @param t
	 *            the entity to handle with
	 * @return Serializable the serialized value
	 */
	public <T> Serializable save(T t);

	/**
	 * Persists all given entities.
	 * 
	 * @param entities
	 */
	public <T> void saveAll(T... entities);

	/**
	 * Persists or updates the given entity.
	 * 
	 * @param t
	 *            the entity to handle with
	 */
	public <T> void saveOrUpdate(T t);

	/**
	 * Persists or updates all given entities.
	 * 
	 * @param entities
	 *            the entities to handle with
	 */
	public <T> void saveOrUpdateAll(T... entities);

	/**
	 * Updates the given entity.
	 * 
	 * @param t
	 *            the entity to handle with
	 */
	public <T> void update(T t);

	/**
	 * Updates all given entities.
	 * 
	 * @param t
	 *            the entity to handle with
	 */
	public <T> void updateAll(T... t);

	/**
	 * Validates the given entity.
	 * 
	 * @param t
	 *            the entity to handle with
	 */
	public <T> void validate(T t);

	/**
	 * Rertrieves the entity by its id.
	 * 
	 * @param id
	 *            the id to look for
	 * @return T the retrieved result
	 */
	public <T> T findById(Serializable id);
}
