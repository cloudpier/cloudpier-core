/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.cloud4soa.api.datamodel.frontend;

import java.lang.reflect.Method;
import java.util.HashMap;
import javax.xml.bind.annotation.XmlRootElement;
import org.ontoware.rdf2go.model.node.URI;

/**
 *
 * @author vincenzo
 */
@XmlRootElement()
public class ApplicationSemanticModel extends eu.cloud4soa.api.datamodel.core.ApplicationSemanticModel{

    public ApplicationSemanticModel(HashMap<Method, URI> releatedResources) {
        super(releatedResources);
    }

}
