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


package eu.cloud4soa.api.datamodel.semantic.ea;

import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFBean;
import com.viceversatech.rdfbeans.annotations.RDFSubject;

@RDFBean("http://www.cloud4soa.eu/v0.1/qos-model#Uptime")
public class Uptime extends Technology_Service_Quality {
    
    private Float hasPercentage;

    @Override
    @RDFSubject(prefix="http://www.cloud4soa.eu/v0.1/qos-model#")
    public String getUriId() {
            return super.getUriId();
    }

    @RDF("http://www.cloud4soa.eu/v0.1/qos-model#hasPercentage")
    public Float getHasPercentage() {
        return hasPercentage;
    }

    public void setHasPercentage(Float hasPercentage) {
        this.hasPercentage = hasPercentage;
    }
    

}
