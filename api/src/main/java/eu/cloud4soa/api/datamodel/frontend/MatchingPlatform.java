/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.cloud4soa.api.datamodel.frontend;

import java.util.List;

/**
 *
 * @author vincenzo
 */
public abstract class MatchingPlatform {
    private List<PaaSInstance> listPaaSInstance;
    private List<SlaContract> listSlaContract;

    /**
     * @return the listPaaSInstance
     */
    public List<PaaSInstance> getListPaaSInstance() {
        return listPaaSInstance;
    }

    /**
     * @return the listSlaContract
     */
    public List<SlaContract> getListSlaContract() {
        return listSlaContract;
    }
}
