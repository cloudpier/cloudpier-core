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


package eu.cloud4soa.api.datamodel.semantic.ent;

import eu.cloud4soa.api.datamodel.semantic.foaf.Organization;
import com.viceversatech.rdfbeans.annotations.RDFBean;
import com.viceversatech.rdfbeans.annotations.RDFSubject;
import com.viceversatech.rdfbeans.annotations.RDF;

@RDFBean("http://www.enterprise-architecture.org/essential-metamodel.owl#Supplier")
public class Supplier extends Organization {

	private java.lang.String title;
	private java.lang.String description;



	@Override
	@RDFSubject(prefix="http://www.enterprise-architecture.org/essential-metamodel.owl#")
	public String getUriId() {
		return super.getUriId();
	}
	
   @RDF("http://purl.org/dc/terms/title")
	public java.lang.String getTitle() {
		return title;
	}
	
	public void setTitle( java.lang.String Name ) {
		this.title = Name;
	}

    @RDF("http://purl.org/dc/terms/description")
	public java.lang.String getDescription() {
		return description;
	}
	
	public void setDescription( java.lang.String description ) {
		this.description = description;
	}

}
