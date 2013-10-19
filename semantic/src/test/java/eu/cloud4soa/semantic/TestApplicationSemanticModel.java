/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cloud4soa.semantic;

import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.core.ApplicationSemanticModel;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map.Entry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author vins
 */
public class TestApplicationSemanticModel {
    final Logger logger = LoggerFactory.getLogger(TestApplicationSemanticModel.class);
    HashMap<Method, URI> releatedResourcesTest;
    
    @Before
    public void setUp() {
        releatedResourcesTest = new HashMap<Method, URI>();
        Method method = null;
        URI uri = null;
        try {
            method = ApplicationInstance.class.getMethod("getUriId", new Class[0]);
            uri = new URIImpl("http://www.imolinfo.it/ontologie/ea/v1.1/application-domain-model#Application/");
            releatedResourcesTest.put(method, uri);
            
            method = ApplicationInstance.class.getMethod("getAcronym", new Class[0]);
            uri = new URIImpl("http://www.imolinfo.it/ontologie/ea/v1.1/application-domain-model#acronym");
            releatedResourcesTest.put(method, uri);
            
            method = ApplicationInstance.class.getMethod("getApplicationcode", new Class[0]);
            uri = new URIImpl("http://www.imolinfo.it/ontologie/ea/v1.1/application-domain-model#application_code");
            releatedResourcesTest.put(method, uri);
            
            method = ApplicationInstance.class.getMethod("getDigest", new Class[0]);
            uri = new URIImpl("http://www.cloud4soa.eu/v0.1/application-domain#digest");
            releatedResourcesTest.put(method, uri);
            
            method = ApplicationInstance.class.getMethod("getProgramminglanguage", new Class[0]);
            uri = new URIImpl("http://purl.org/dc/terms/title");
            releatedResourcesTest.put(method, uri);
            
            method = ApplicationInstance.class.getMethod("getProgramminglanguageVersion", new Class[0]);
            uri = new URIImpl("http://purl.org/dc/terms/hasVersion");
            releatedResourcesTest.put(method, uri);
            
            method = ApplicationInstance.class.getMethod("getVersion", new Class[0]);
            uri = new URIImpl("http://purl.org/dc/terms/hasVersion");
            releatedResourcesTest.put(method, uri);
            
            method = ApplicationInstance.class.getMethod("getOwnerUriId", new Class[0]);
            uri = new URIImpl("http://www.cloud4soa.eu/v0.1/user-model#Developer/");
            releatedResourcesTest.put(method, uri);
            
            method = ApplicationInstance.class.getMethod("getSizeUriId", new Class[0]);
            uri = new URIImpl("http://www.cloud4soa.eu/v0.1/measure#Capacity/");
            releatedResourcesTest.put(method, uri);
            
            method = ApplicationInstance.class.getMethod("getSizeQuantity", new Class[0]);
            uri = new URIImpl("http://www.cloud4soa.eu/v0.1/measure#has_quantity");
            releatedResourcesTest.put(method, uri);
            
            method = ApplicationInstance.class.getMethod("getStatusUriId", new Class[0]);
            uri = new URIImpl("http://www.cloud4soa.eu/v0.1/application-domain#Application_Status/");
            releatedResourcesTest.put(method, uri);

        } catch (NoSuchMethodException ex) {
            logger.error("NoSuchMethodException", ex);
        } catch (SecurityException ex) {
            logger.error("SecurityException", ex);
        }
        
    }
 
    @Ignore @Test
    public void testReleatedResources(){
        ApplicationModel am = new ApplicationModel();
        ApplicationSemanticModel asm = am.getApplicationSemanticModel();
        HashMap<Method, URI> releatedResources = asm.getReleatedResources();
        Assert.assertEquals(releatedResourcesTest.size(), releatedResources.size());
        for (Entry<Method, URI> entry : releatedResourcesTest.entrySet()) {
            Assert.assertTrue(releatedResources.containsKey(entry.getKey()));
            Assert.assertEquals(entry.getValue(), releatedResources.get(entry.getKey()));        
        }
    }
}
