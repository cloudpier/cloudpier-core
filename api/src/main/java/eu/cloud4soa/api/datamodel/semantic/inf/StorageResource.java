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


package eu.cloud4soa.api.datamodel.semantic.inf;

import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFBean;
import com.viceversatech.rdfbeans.annotations.RDFSubject;
import eu.cloud4soa.api.datamodel.semantic.measure.NetworkingRange;
import eu.cloud4soa.api.datamodel.semantic.measure.StorageRange;

@RDFBean("http://www.cloud4soa.eu/v0.1/infrastructural-domain#StorageResource")
public class StorageResource extends Storage {

	private NetworkingRange bandwidth;
	private StorageRange capacity;

	@Override
	@RDFSubject(prefix="http://www.cloud4soa.eu/v0.1/infrastructural-domain#")
	public String getUriId() {
		return super.getUriId();
	}
	


    @RDF("http://www.cloud4soa.eu/v0.1/infrastructural-domain#bandwidth")
	public NetworkingRange getBandwidth() {
		return bandwidth;
	}
	
	public void setBandwidth( NetworkingRange bandwidth ) {
		this.bandwidth = bandwidth;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/infrastructural-domain#capacity")
	public StorageRange getCapacity() {
		return capacity;
	}
	
	public void setCapacity( StorageRange capacity ) {
		this.capacity = capacity;
	}
}
