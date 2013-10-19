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
import eu.cloud4soa.api.datamodel.semantic.paas.PaaSOffering;

@RDFBean("http://www.cloud4soa.eu/v0.1/infrastructural-domain#DB_Deployment")
public class DBDeployment extends InformationStoreInstance {

    private java.lang.String Url;
    private PaaSOffering paasOffering;
    private DBDeployment migratedFrom;

    @Override
    @RDFSubject(prefix = "http://www.cloud4soa.eu/v0.1/infrastructural-domain#")
    public String getUriId() {
        return super.getUriId();
    }

    @RDF("http://www.cloud4soa.eu/v0.1/infrastructural-domain#hasUrl")
    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    @RDF("http://www.cloud4soa.eu/v0.1/infrastructural-domain#DB_deployment_location")
    public PaaSOffering getDeploymentLocation() {
        return paasOffering;
    }

    public void setDeploymentLocation(PaaSOffering paasOffering) {
        this.paasOffering = paasOffering;
    }

    @RDF("http://www.cloud4soa.eu/v0.1/infrastructural-domain#migratedFrom")
    public DBDeployment getMigratedFrom() {
        return migratedFrom;
    }

    public void setMigratedFrom(DBDeployment migratedFrom) {
        this.migratedFrom = migratedFrom;
    }
}
