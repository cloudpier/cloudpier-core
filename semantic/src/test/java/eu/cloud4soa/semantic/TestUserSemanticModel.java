/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cloud4soa.semantic;

import eu.cloud4soa.api.datamodel.core.UserInstance;
import eu.cloud4soa.api.datamodel.core.UserSemanticModel;
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
public class TestUserSemanticModel {
    final Logger logger = LoggerFactory.getLogger(TestUserSemanticModel.class);
    HashMap<Method, URI> releatedResourcesTest;
    
    @Before
    public void setUp() {
        releatedResourcesTest = new HashMap<Method, URI>();
        Method method = null;
        URI uri = null;
        try {
            method = UserInstance.class.getMethod("getUriId", new Class[0]);
            uri = new URIImpl("http://www.cloud4soa.eu/v0.1/user-model#User/");
            releatedResourcesTest.put(method, uri);
            
            method = UserInstance.class.getMethod("getFamilyname", new Class[0]);
            uri = new URIImpl("http://xmlns.com/foaf/0.1/family_name");
            releatedResourcesTest.put(method, uri);
            
            method = UserInstance.class.getMethod("getFirstName", new Class[0]);
            uri = new URIImpl("http://xmlns.com/foaf/0.1/firstName");
            releatedResourcesTest.put(method, uri);
            
            method = UserInstance.class.getMethod("getGeekcode", new Class[0]);
            uri = new URIImpl("http://xmlns.com/foaf/0.1/geekcode");
            releatedResourcesTest.put(method, uri);
            
            method = UserInstance.class.getMethod("getSurname", new Class[0]);
            uri = new URIImpl("http://xmlns.com/foaf/0.1/surname");
            releatedResourcesTest.put(method, uri);
            
            method = UserInstance.class.getMethod("getBirthday", new Class[0]);
            uri = new URIImpl("http://xmlns.com/foaf/0.1/birthday");
            releatedResourcesTest.put(method, uri);
            
//            method = UserInstance.class.getMethod("getHoldsaccount", new Class[0]);
//            uri = new URIImpl("http://xmlns.com/foaf/0.1/holdsAccount");
//            releatedResourcesTest.put(method, uri);
            
            method = UserInstance.class.getMethod("getCloud4SoaAccountUriId", new Class[0]);
            uri = new URIImpl("http://www.cloud4soa.eu/v0.1/user-model#Cloud4SoaAccount/");
            releatedResourcesTest.put(method, uri);
            
            method = UserInstance.class.getMethod("getAccountname", new Class[0]);
            uri = new URIImpl("http://xmlns.com/foaf/0.1/accountName");
            releatedResourcesTest.put(method, uri);

        } catch (NoSuchMethodException ex) {
            logger.error("NoSuchMethodException", ex);
        } catch (SecurityException ex) {
            logger.error("SecurityException", ex);
        }
        
    }
 
    @Ignore @Test
    public void testReleatedResources(){
        UserModel um = new UserModel();
        UserSemanticModel usm = um.getUserSemanticModel();
        HashMap<Method, URI> releatedResources = usm.getReleatedResources();
        Assert.assertEquals(releatedResourcesTest.size(), releatedResources.size());
        for (Entry<Method, URI> entry : releatedResourcesTest.entrySet()) {
            System.out.println(entry.getKey());
            Assert.assertTrue(releatedResources.containsKey(entry.getKey()));
            Assert.assertEquals(entry.getValue(), releatedResources.get(entry.getKey()));        
        }
    }
}
