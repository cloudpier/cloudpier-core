/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.cloud4soa.api.frontend;

import eu.cloud4soa.api.datamodel.frontend.ApplicationInstance;
import eu.cloud4soa.api.datamodel.frontend.PaaSInstance;

/**
 *
 * @author vincenzo
 */
public interface MigrationWidget {
//void migrateApplication(ui.ApplicationInstance, ui.PaaSInstance)
    public void migrateApplication(ApplicationInstance applicationInstance, PaaSInstance paaSInstance);
}
