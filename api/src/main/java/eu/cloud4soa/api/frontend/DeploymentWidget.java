/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.cloud4soa.api.frontend;

import eu.cloud4soa.api.datamodel.frontend.ApplicationArchive;
import eu.cloud4soa.api.datamodel.frontend.ApplicationInstance;
import eu.cloud4soa.api.datamodel.frontend.PaaSInstance;

/**
 *
 * @author vincenzo
 */
public interface DeploymentWidget {
//void deployApplication(ui.ApplicationArchive, ui.ApplicationInstance ui.PaaSInstance)
    public void deployApplication(ApplicationArchive applicationArchive, ApplicationInstance applicationInstance, PaaSInstance paaSInstance);

}
