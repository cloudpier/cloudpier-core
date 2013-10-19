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


package eu.cloud4soa.api.datamodel.semantic.paas;

import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFBean;
import com.viceversatech.rdfbeans.annotations.RDFSubject;
import eu.cloud4soa.api.datamodel.semantic.Thing;
import java.util.ArrayList;
import java.util.List;

@RDFBean("http://www.cloud4soa.eu/v0.1/paas-model#Channel")
public class Channel extends PlatformLayer {

	private List<Operation> supportedOperations = new ArrayList<Operation>();
	private java.lang.String version;
	private java.lang.String AccessRights;
	private java.lang.String Description;
        private java.lang.String title;



	@Override
	@RDFSubject(prefix="http://www.cloud4soa.eu/v0.1/paas-model#")
	public String getUriId() {
		return super.getUriId();
	}
	


    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#supportOperation")
	public List<Operation> getSupportedOperations() {
		return supportedOperations;
	}
	
	public void setSupportedOperations( List<Operation> supportedOperations ) {
		this.supportedOperations = supportedOperations;
	}
    @RDF("http://purl.org/dc/terms/hasVersion")
	public java.lang.String getVersion() {
		return version;
	}
	
	public void setVersion( java.lang.String version ) {
		this.version = version;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#hasAccessRights")
	public java.lang.String getAccessRights() {
		return AccessRights;
	}
	
	public void setAccessRights( java.lang.String AccessRights ) {
		this.AccessRights = AccessRights;
	}
    @RDF("http://purl.org/dc/terms/description")
	public java.lang.String getDescription() {
		return Description;
	}
	
	public void setDescription( java.lang.String Description ) {
		this.Description = Description;
	}
    @RDF("http://purl.org/dc/terms/title")
	public java.lang.String geTitle() {
		return title;
	}
	
	public void setTitle( java.lang.String title ) {
		this.title = title;
	}

}
