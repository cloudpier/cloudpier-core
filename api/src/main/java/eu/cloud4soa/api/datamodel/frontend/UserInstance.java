/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.cloud4soa.api.datamodel.frontend;

import eu.cloud4soa.api.datamodel.frontend.xmladapter.URIAdapter;
import eu.cloud4soa.api.datamodel.semantic.user.User;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.ontoware.rdf2go.model.node.URI;

/**
 *
 * @author vincenzo
 */
@XmlRootElement()
@XmlType(name = "userInstance", namespace="eu.cloud4soa.api.datamodel.frontend")
public class UserInstance extends eu.cloud4soa.api.datamodel.core.UserInstance{

    public UserInstance() {
        super();
    }
    
    
    public UserInstance(User user) {
        super(user);
    }
    
}
