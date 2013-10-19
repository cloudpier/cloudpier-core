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


package eu.cloud4soa.adapter.rest.response;

import java.io.Serializable;
import java.util.Arrays;

import eu.cloud4soa.adapter.rest.response.model.Database;

/**
 * 
 * @author Denis Neuling (dn@cloudcontrol.de)
 */
public class ListDatabaseResponse extends Response<ListDatabaseResponse> implements Serializable{
	private static final long serialVersionUID = 784945630851539433L;
	
	private Database[] databases = new Database[0];
	
	public ListDatabaseResponse(){
	}

	public Database[] getDatabases() {
		return databases;
	}

	public void setDatabases(Database[] databases) {
		this.databases = databases;
	}

	@Override
	public String toString() {
		return "ListDatabaseResponse [databases=" + Arrays.toString(databases) + ", Response=" + super.toString() + "]";
	}
}
