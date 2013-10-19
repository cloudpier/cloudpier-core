/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.cloud4soa.api.frontend;

import eu.cloud4soa.api.datamodel.frontend.ApplicationInstance;

/**
 *
 * @author vincenzo
 */
public interface MonitoringWidget {
//void startStopApplication(ui.ApplicationInstance, ui.StartStopCommand)
    public void startStopApplication(ApplicationInstance applicationInstance, String startStopCommand);
}
