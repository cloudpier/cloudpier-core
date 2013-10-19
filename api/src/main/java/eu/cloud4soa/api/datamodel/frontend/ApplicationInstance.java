/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.cloud4soa.api.datamodel.frontend;

import java.io.Serializable;
import eu.cloud4soa.api.datamodel.semantic.app.Application;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author vincenzo
 */
@XmlRootElement()
@XmlType(name = "applicationInstance", namespace="eu.cloud4soa.api.datamodel.frontend")
public class ApplicationInstance extends eu.cloud4soa.api.datamodel.core.ApplicationInstance implements Serializable{

    public ApplicationInstance() {
        super();
    }

    
    public ApplicationInstance(Application application) {
        super(application);
    }
    
}
