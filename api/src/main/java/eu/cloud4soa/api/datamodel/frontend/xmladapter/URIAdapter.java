/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package eu.cloud4soa.api.datamodel.frontend.xmladapter;
//
//import javax.xml.bind.annotation.adapters.XmlAdapter;
//import org.ontoware.rdf2go.model.node.URI;
//import org.ontoware.rdf2go.model.node.impl.URIImpl;
//
///**
// *
// * @author vins
// */
//public class URIAdapter extends XmlAdapter<String,URI> {
//    public URI unmarshal(String val) throws Exception {
//        return new URIImpl(val);
//    }
//    public String marshal(URI val) throws Exception {
//        return val.toString();
//    }    
//}

package eu.cloud4soa.api.datamodel.frontend.xmladapter;

import java.net.URI;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author vins
 */
public class URIAdapter extends XmlAdapter<String,URI> {
    public URI unmarshal(String val) throws Exception {
        return new URI(val);
    }
    public String marshal(URI val) throws Exception {
        return val.toString();
    }    
}

