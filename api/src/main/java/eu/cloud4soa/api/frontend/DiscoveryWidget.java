/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.cloud4soa.api.frontend;

import eu.cloud4soa.api.datamodel.frontend.ApplicationInstance;
import eu.cloud4soa.api.datamodel.frontend.MatchingPlatform;
import eu.cloud4soa.api.datamodel.frontend.PaaSInstance;
import eu.cloud4soa.api.datamodel.frontend.PaaSProviderDetails;
import eu.cloud4soa.api.datamodel.frontend.PaaSRating;

/**
 *
 * @author vincenzo
 */
public interface DiscoveryWidget {
//List<ui.PaaSInstance>, List<ui.SlaContract> searchForMatchingPlatform(ui.ApplicationInstance)
    public MatchingPlatform searchForMatchingPlatform(ApplicationInstance applicationInstance);
//ui.PaaSInstance searchForPaaS(ui.PaaSInstance)
    public PaaSInstance searchForPaaS(PaaSInstance paaSInstance);
//void ratePaaS(ui.PaaSInstance, ui.PaaSRating)
    public void ratePaaS(PaaSInstance paaSInstance, PaaSRating paaSRating);
//    ui.PaaSProviderDetails getPaaSProviderDetails(ui.PaaSInstance)
    public PaaSProviderDetails getPaaSProviderDetails(PaaSInstance paaSInstance);
}
