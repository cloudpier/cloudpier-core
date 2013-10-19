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

package eu.cloud4soa.api.governance;

import eu.cloud4soa.api.datamodel.governance.SlaContract;
import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.datamodel.governance.ApplicationArchive;
import eu.cloud4soa.api.datamodel.governance.DeployApplicationParameters;
import eu.cloud4soa.api.datamodel.governance.DatabaseInfo;
import eu.cloud4soa.api.datamodel.soa.GitRepoInfo;
import eu.cloud4soa.api.util.exception.adapter.Cloud4SoaException;
import java.io.File;

/**
 *
 * @author vincenzo
 */
public interface ExecutionManagementServiceModule {
//    void deployApplication(core.ApplicationArchive, core.ApplicationInstance, core.PaaSInstance);
//      public String deployApplication(File file, ApplicationInstance applicationInstance, PaaSInstance paaSInstance, String publicKey, String privateKey, String accountName) throws Cloud4SoaException;
 //   @Deprecated  
 //   public String deployApplication(File file, ApplicationInstance applicationInstance, PaaSInstance paaSInstance, String publicKey, String privateKey, String accountName, String SLATemplateId) throws Cloud4SoaException;
    public String deployApplication( DeployApplicationParameters deployAppParameters) throws Cloud4SoaException;
      
      public GitRepoInfo deployThroughGit(String gitCommand, ApplicationInstance applicationInstance, PaaSInstance paaSInstance, String publicKey, String secretKey, String accountName) throws Cloud4SoaException;
      public GitRepoInfo deployThroughGit(String gitCommand, ApplicationInstance applicationInstance, PaaSInstance paaSInstance, String publicKey, String secretKey, String accountName, String SLATemplateId) throws Cloud4SoaException;
      public GitRepoInfo deployThroughGitFinalStep(GitRepoInfo gitRepoInfo, ApplicationInstance applicationInstance, PaaSInstance paaSInstance, String publicKey, String secretKey, String accountName) throws Cloud4SoaException;
      public GitRepoInfo deployThroughGitFinalStep(GitRepoInfo gitRepoInfo, ApplicationInstance applicationInstance, PaaSInstance paaSInstance, String publicKey, String secretKey, String accountName, String SLATemplateId) throws Cloud4SoaException;

//    gov.ApplicationArchive retrieveApplication(core.ApplicationInstance);
      ApplicationArchive retrieveApplication(ApplicationInstance applicationInstance) throws Cloud4SoaException;
//    // It's necessary?
//    //    difference between 1 and 3 is core.ApplicationArchive -> gov.ApplicationArchive
//    //    void deployApplication(gov.ApplicationArchive, core.ApplicationInstance, core.PaaSInstance)
      
//    void startStopApplication(core.ApplicationInstance, ui.StartStopCommand);
      void startStopApplication(ApplicationInstance applicationInstance, String startStopCommand, String publicKey, String secretKey, String accountName) throws Cloud4SoaException;
      
//
//    //    FROM UC7
//    void detectSlaViolation(sla.SlaContract);
      void detectSlaViolation(SlaContract slaContract) throws Cloud4SoaException;
      
      void unDeployApplication(ApplicationInstance applicationInstance, String publicKey, String privateKey, String accountName) throws Cloud4SoaException;
      
      //DB methods
      public DatabaseInfo createDatabase(ApplicationInstance applicationInstance, PaaSInstance paaSInstance, String publicKey, String secretKey, String accountName, String dbname, String dbuser, String dbpassword, String dbtype) throws Cloud4SoaException;
      //public String deleteDatabase(ApplicationInstance applicationInstance, PaaSInstance paaSInstance, String publicKey, String secretKey, String dbname, String dbuser, String dbpassword, String dbtype) throws Cloud4SoaException;
      public DatabaseInfo downloadDataBase(ApplicationInstance applicationInstance, String publicKey, String secretKey, String dbname, String dbuser, String dbpassword, String dbtype, String fileToStore) throws Cloud4SoaException;
      public DatabaseInfo restoreDataBase(ApplicationInstance applicationInstance, PaaSInstance paaSInstance, String publicKey, String secretKey, String dbname, String dbuser, String dbpassword, String dbtype,String fileToRestore) throws Cloud4SoaException;
      
      public void saveDeploymentInfoAndStartMonitoring( 
            ApplicationInstance applicationInstance,
            PaaSInstance paaSInstance,
            String deployedAppUrl,
            String adapterAppUrl,  
            String SLAContractID);
}
