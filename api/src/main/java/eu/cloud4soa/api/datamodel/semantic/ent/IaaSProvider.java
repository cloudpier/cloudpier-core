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

import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFBean;
import com.viceversatech.rdfbeans.annotations.RDFSubject;
import eu.cloud4soa.api.datamodel.semantic.inf.HardwareComponent;
import java.util.ArrayList;
import java.util.List;

@RDFBean("http://www.cloud4soa.eu/v0.1/enterprise-domain#IaaSProvider")
public class IaaSProvider extends Supplier {

	private List<HardwareComponent> providesHardwareComponent = new ArrayList<HardwareComponent>();



	@Override
	@RDFSubject(prefix="http://www.cloud4soa.eu/v0.1/enterprise-domain#")
	public String getUriId() {
		return super.getUriId();
	}
	
    @RDF("http://www.cloud4soa.eu/v0.1/user-model#providesHardwareComponent")
	public List<HardwareComponent> getProvidesHardwareComponent() {
		return providesHardwareComponent;
	}
	
	public void setProvidesHardwareComponent( List<HardwareComponent> providesHardwareComponent ) {
		this.providesHardwareComponent = providesHardwareComponent;
	}
        
}