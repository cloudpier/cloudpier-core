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

@RDFBean("http://www.cloud4soa.eu/v0.1/paas-model#Operation")
public class Operation extends PlatformLayer{

	private java.lang.String informationReturned;
	private java.lang.String operationCommand;
	private OperationType operationType;
	private List<Parameter> requiredParameter=new ArrayList<Parameter>();
	private List<Exception> thrownException=new ArrayList<Exception>();
	private java.lang.String description;
	private java.lang.String title;





	@Override
	@RDFSubject(prefix="http://www.cloud4soa.eu/v0.1/paas-model#")
	public String getUriId() {
		return super.getUriId();
	}
	


    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#hasInformationReturned")
	public java.lang.String getInformationReturned() {
		return informationReturned;
	}
	
	public void setInformationReturned( java.lang.String informationReturned ) {
		this.informationReturned = informationReturned;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#hasOperationCommand")
	public java.lang.String getOperationCommand() {
		return operationCommand;
	}
	
	public void setOperationCommand( java.lang.String operationCommand ) {
		this.operationCommand = operationCommand;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#hasOperationType")
	public OperationType getOperationType() {
		return operationType;
	}
	
	public void setOperationType( OperationType operationType ) {
		this.operationType = operationType;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#requireParameter")
	public List<Parameter> getRequiredParameters() {
		return requiredParameter;
	}
	
	public void setRequiredParameters( List<Parameter> requiredParameter ) {
		this.requiredParameter = requiredParameter;
	}
    @RDF("http://www.cloud4soa.eu/v0.1/paas-model#throwException")
	public List<Exception> getThrownException() {
		return thrownException;
	}
	
	public void setThrownException( List<Exception> thrownException ) {
		this.thrownException = thrownException;
	}
    @RDF("http://purl.org/dc/terms/description")
	public java.lang.String getDescription() {
		return description;
	}
	
	public void setDescription( java.lang.String description ) {
		this.description = description;
	}
    @RDF("http://purl.org/dc/terms/title")
	public java.lang.String getTitle() {
		return title;
	}
	
	public void setTitle( java.lang.String title ) {
		this.title = title;
	}
}
