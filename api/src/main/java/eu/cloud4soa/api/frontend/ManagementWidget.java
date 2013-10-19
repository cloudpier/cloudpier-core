/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.cloud4soa.api.frontend;

import eu.cloud4soa.api.datamodel.frontend.ApplicationSemanticModel;
import eu.cloud4soa.api.datamodel.frontend.PaaSInstance;
import eu.cloud4soa.api.datamodel.frontend.PaaSSemanticModel;
import eu.cloud4soa.api.datamodel.frontend.UserInstance;
import eu.cloud4soa.api.datamodel.frontend.UserSemanticModel;

/**
 *
 * @author vincenzo
 */
public interface ManagementWidget {
//ui.ApplicationSemanticModel createApplicationProfile()
    public ApplicationSemanticModel createApplicationProfile();
//ui.ApplicationSemanticModel updateApplicationProfile(ui.ApplicationSemanticModel)
    public ApplicationSemanticModel updateApplicationProfile(ApplicationSemanticModel applicationSemanticModel);
//ui.PaaSSemanticModel createPaaSProfile()
    public PaaSSemanticModel createPaaSProfile();
//ui.PaaSInstance getPaaSInstance(ui.PaaSInstance)
    public PaaSInstance getPaaSInstance(PaaSInstance paaSInstance);
//void storePaaSInstance(ui.PaaSInstance)
    public void storePaaSInstance(PaaSInstance paaSInstance);
//ui.UserSemanticModel createNewAccount()
    public UserSemanticModel createNewAccount();
//void createNewAccount(ui.UserInstance)
    public void createNewAccount(UserInstance userInstance);
//ui.UserInstance getUserInstance(ui.UserInstance)
    public UserInstance getUserInstance(UserInstance userInstance);
//void storeUserInstance(ui.UserInstance)
    public void storeUserInstance(UserInstance userInstance);
}
