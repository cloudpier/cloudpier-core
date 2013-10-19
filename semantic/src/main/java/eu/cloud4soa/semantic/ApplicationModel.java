/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.cloud4soa.semantic;

import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFSubject;
import eu.cloud4soa.api.datamodel.core.ApplicationInstance;
import eu.cloud4soa.api.datamodel.core.ApplicationSemanticModel;
import eu.cloud4soa.api.datamodel.core.annotations.SemanticRelation;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.impl.URIImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author vincenzo
 */
public class ApplicationModel implements eu.cloud4soa.api.semantic.ApplicationModel{
    final Logger logger = LoggerFactory.getLogger(ApplicationModel.class);

    private ApplicationSemanticModel applicationSemanticModel;

    @Override
    public ApplicationSemanticModel getApplicationSemanticModel() {

        logger.debug("getApplicationSemanticModel()");

        HashMap<Method,URI> releatedResources = exploreApplicationModel();
        
        ApplicationSemanticModel applicationSemanticModel = new ApplicationSemanticModel(releatedResources);

        logger.debug("created ApplicationSemanticModel: "+ applicationSemanticModel);

        logger.debug("return ApplicationSemanticModel "+applicationSemanticModel);

        return applicationSemanticModel;
    }
    
    private HashMap<Method,URI> exploreApplicationModel(){
        HashMap<Method,URI> releatedResources = new HashMap<Method, URI>();
        Method[] methods = null;
        try {
            methods = ApplicationInstance.class.getMethods();
        } catch (SecurityException ex) {
            logger.error("SecurityException", ex);
        }
        if(methods!=null){
            for( int i=0; i<methods.length; i++ ){
            String methodName = methods[ i ].getName();
            if( (methodName.startsWith( "get" ) || methodName.startsWith( "is" )) && methods[ i ].getParameterTypes().length == 0){
              // Skip over "getClass()"
                    if( !methodName.equals( "getClass" ) ){

                        SemanticRelation annotation = methods[ i ].getAnnotation(SemanticRelation.class);
                        if(annotation!=null){
                            Class semanticClass = annotation.semanticClass();
                            String semanticMethodName = annotation.methodName();
                            logger.debug("semanticClass: "+semanticClass);
                            logger.debug("semanticMethodName: "+semanticMethodName);

                            try {
                                RDF ann = semanticClass.getMethod(semanticMethodName, new Class[0]).getAnnotation(RDF.class);
                                if (ann != null) {
                                    URI attributeURI = new URIImpl(ann.value());
                                    releatedResources.put(methods[ i ], attributeURI);
                                }
                                else{
                                    RDFSubject rdfSubjectAnnotation = semanticClass.getMethod(semanticMethodName, new Class[0]).getAnnotation(RDFSubject.class);
                                    if (rdfSubjectAnnotation != null) {
                                        URI attributeURI = new URIImpl(rdfSubjectAnnotation.prefix());
                                        releatedResources.put(methods[ i ], attributeURI);
                                    }
                                }
                            } catch (NoSuchMethodException ex) {
                               logger.error("NoSuchMethodException: "+ex);
                            } catch (SecurityException ex) {
                               logger.error("SecurityException: "+ex);
                            }

                        }	
                    }
                }
            }
        }
        return releatedResources;
    }

}
