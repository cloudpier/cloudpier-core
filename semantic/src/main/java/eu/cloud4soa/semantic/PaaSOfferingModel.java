/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.cloud4soa.semantic;

import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFSubject;
import eu.cloud4soa.api.datamodel.core.PaaSInstance;
import eu.cloud4soa.api.datamodel.core.PaaSSemanticModel;
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
public class PaaSOfferingModel implements eu.cloud4soa.api.semantic.PaaSOfferingModel{
    final Logger logger = LoggerFactory.getLogger(PaaSOfferingModel.class);

    private PaaSSemanticModel paaSSemanticModel;

    @Override
    public PaaSSemanticModel getPaaSSemanticModel() {
        logger.error("UnsupportedOperationException("+"Not supported yet."+")");
        //        throw new UnsupportedOperationException("Not supported yet.");

        logger.debug("getPaaSSemanticModel()");
        
        HashMap<Method,URI> releatedResources = exploreApplicationModel();

        PaaSSemanticModel paaSSemanticModel = new PaaSSemanticModel(releatedResources);

        logger.debug("created PaaSSemanticModel: "+ paaSSemanticModel);

        logger.debug("return PaaSSemanticModel "+paaSSemanticModel);

        return paaSSemanticModel;
    }
    
    private HashMap<Method,URI> exploreApplicationModel(){
        HashMap<Method,URI> releatedResources = new HashMap<Method, URI>();
        Method[] methods = null;
        try {
            methods = PaaSInstance.class.getMethods();
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
