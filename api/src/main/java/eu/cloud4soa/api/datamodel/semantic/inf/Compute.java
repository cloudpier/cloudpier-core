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
import eu.cloud4soa.api.datamodel.semantic.measure.ComputingRange;
import eu.cloud4soa.api.datamodel.semantic.measure.NumericRange;
import eu.cloud4soa.api.datamodel.semantic.measure.StorageRange;

@RDFBean("http://www.cloud4soa.eu/v0.1/infrastructural-domain#Compute")
public class Compute extends Processing {

        private NumericRange hasCores;
        private java.lang.String architecture;
	private StorageRange cache;
	private StorageRange memory;
	private java.lang.String state;
	private java.lang.Boolean supportErrorCorrectCode;


	@Override
	@RDFSubject(prefix="http://www.cloud4soa.eu/v0.1/infrastructural-domain#")
	public String getUriId() {
		return super.getUriId();
	}
	


    @RDF("http://www.cloud4soa.eu/v0.1/infrastructural-domain#hasArchitecture")
	public java.lang.String getArchitecture() {
		return architecture;
	}
	
	public void setArchitecture( java.lang.String architecture ) {
		this.architecture = architecture;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/infrastructural-domain#hasCache")
	public StorageRange getCache() {
		return cache;
	}
	
	public void setCache( StorageRange cache ) {
		this.cache = cache;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/infrastructural-domain#hasCores")
	public NumericRange getHasCores() {
		return hasCores;
	}
	
	public void setHasCores( NumericRange hasCores ) {
		this.hasCores = hasCores;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/infrastructural-domain#hasMemory")
	public StorageRange getMemory() {
		return memory;
	}
	
	public void setMemory( StorageRange memory ) {
		this.memory = memory;
	}

    @RDF("http://www.cloud4soa.eu/v0.1/infrastructural-domain#hasState")
	public java.lang.String getState() {
		return state;
	}
	
	public void setState( java.lang.String state ) {
		this.state = state;
	}

    @RDF("http://www.cloud4soa.eu/v0.1/infrastructural-domain#supportErrorCorrectCode")
	public java.lang.Boolean getSupportErrorCorrectCode() {
		return supportErrorCorrectCode;
	}
	
	public void setSupportErrorCorrectCode( java.lang.Boolean supportErrorCorrectCode ) {
		this.supportErrorCorrectCode = supportErrorCorrectCode;
	}

}
