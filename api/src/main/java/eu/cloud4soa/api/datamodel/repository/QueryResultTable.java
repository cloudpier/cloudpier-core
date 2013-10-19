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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author frarav
 */
@XmlRootElement(name = "QueryResultTable")
public class QueryResultTable implements Iterable<QueryResultRow> {
    
    private List<QueryResultRow>    resultingRows;
    private List<String>            variables;

    public QueryResultTable(List<QueryResultRow> resultingRows, List<String> variables) {
        this.resultingRows = resultingRows;
        this.variables = variables;
    }

    public QueryResultTable() {
        resultingRows = new ArrayList<QueryResultRow>();
        variables = new ArrayList<String>();
    }

    public void setResultingRows(List<QueryResultRow> resultingRows) {
        this.resultingRows = resultingRows;
    }

    public List<QueryResultRow> getResultingRows() {
        return resultingRows;
    }

    public void setVariables(List<String> variables) {
        this.variables = variables;
    }   
    
    public List<String> getVariables() {
        return this.variables;
    }
    
    @Override
    public Iterator<QueryResultRow> iterator() {
        return this.resultingRows.iterator();
    }
}
