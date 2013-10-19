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


package eu.cloud4soa.api.datamodel.semantic.app;

import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFBean;
import com.viceversatech.rdfbeans.annotations.RDFSubject;

/**
 *
 * @author vins
 */
@RDFBean("http://www.cloud4soa.eu/v0.1/application-domain#ApplicationArchive")
public class ApplicationArchive extends ApplicationPhysical {

    private java.lang.String title;	
    private java.lang.String fileName;
    private java.lang.String extensionName;

    @Override
    @RDFSubject(prefix="http://www.cloud4soa.eu/v0.1/application-domain#")
    public String getUriId() {
            return super.getUriId();
    }
    
    @RDF("http://purl.org/dc/terms/title")
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    @RDF("http://www.cloud4soa.eu/v0.1/application-domain#fileName")
        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    @RDF("http://www.cloud4soa.eu/v0.1/application-domain#extensionName")
        public String getExtensionName() {
            return extensionName;
        }

        public void setExtensionName(String extensionName) {
            this.extensionName = extensionName;
        }
    
}
