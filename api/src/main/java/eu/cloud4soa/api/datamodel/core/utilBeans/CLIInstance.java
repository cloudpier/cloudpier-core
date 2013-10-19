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
package eu.cloud4soa.api.datamodel.core.utilBeans;

import eu.cloud4soa.api.datamodel.semantic.paas.CLI;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
/**
 *
 * @author vins
 */
@XmlRootElement()
@XmlType(name = "cliInstance")
public class CLIInstance extends ChannelInstance{

    private CLI getCli() {
        return (CLI)super.channel;
    }
    
    public CLIInstance() {
        super.channel = new CLI();
    }

    public CLIInstance(CLI cli) {
        super.channel = cli;
    }
        
    public String getURL(){
        return getCli().getURL();
    }
    
    public void setURL(String uRL){
        getCli().setURL(uRL);
    }

}
