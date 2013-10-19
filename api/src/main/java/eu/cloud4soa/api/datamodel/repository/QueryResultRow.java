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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cloud4soa.api.datamodel.repository;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author frarav
 */
@XmlRootElement(name = "QueryResultRow")
public class QueryResultRow {
    // < varname, value >
    private Map<String, String> resultRow;

    public QueryResultRow(Map<String, String> resultRow) {
        this.resultRow = resultRow;
    }

    public QueryResultRow() {
        resultRow = new HashMap<String, String>();
    }

    public void setResultRow(Map<String, String> resultRow) {
        this.resultRow = resultRow;
    }

    public Map<String, String> getResultRow() {
        return resultRow;
    }
    
    public String getStringValue(String varname){
        if(resultRow.containsKey(varname))
            return resultRow.get(varname);
        return null;
    }
    
}
