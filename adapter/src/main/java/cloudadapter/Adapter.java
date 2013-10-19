/*
 *  Copyright 2013 Cloud4SOA, www.cloud4soa.eu
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudadapter;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.DatabaseHelper;
import utils.ParseXmlString;
import beanstalk.BeansCheckDNSAvailabity;
import beanstalk.BeansCreateApplicationVersion;
import beanstalk.BeansDatabase;
import beanstalk.BeansDeleteApplicationVersion;
import beanstalk.BeansDescribeApplicationVersions;
import beanstalk.BeansDescribeEnvironments;
import beanstalk.BeansManageAppVersions;
import beanstalk.BeansStartApp;
import beanstalk.BeansUpdateEnvironment;
import beanstalk.BeanstalkDeployNoGUI;
import eu.cloud4soa.api.util.exception.adapter.Cloud4SoaException;
import eu.cloud4soa.api.datamodel.governance.DatabaseInfo;
import java.net.URL;
import openshift.Openshift_Aux;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.DatabaseCredentials;
import org.cloudfoundry.client.lib.domain.CloudApplication;

/**
 *
 * @author Ledakis Giannis (SingularLogic)
 * This class defines methods from Harmonized API as defined
 * by the work done in WP6 of Cloud4SOA project.
 * The methods of this class are used only when the 
 * usage of Remote Adapters is not possible.
 */
public class Adapter {

    public static final String AWS_BEANSTALK = "Beanstalk";
    public static final String CLOUDBEES_RUN = "CloudBees";
    public static final String GAE = "GoogleAppengine";
    public static final String CLOUDCONTROL = "CloudControl";
    public static final String OPENSHIFT_X = "OpenShift";//OpenShift Express
    public static final String CLOUDFOUNDRY = "CloudFoundry";

    /*TODO:
     * please try to define homogeneous objects which binds all needed
     * properties felt to use one hundred pieces of strings as parameter of
     * methods does not make really sense for me (dn)
     */
    public static String uploadAndDeployToEnv(String PaaS, String war, String publicKey, String secretKey, String accountName, String appName, String appVersion,
            String environment, String bucket, String host, String type, String apiversion, String description) throws Cloud4SoaException {
        String ret = "";
        String url = "";
        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {



            // fix in order to keep it simple
            //no environment name needed, just the application will do the work
            environment = appName;



            //check if environment(appname url) exists-> if exists call update, else call commit
            BeansCheckDNSAvailabity check_availability = new BeansCheckDNSAvailabity(publicKey, secretKey, environment);

            Boolean tmp = check_availability.CheckAvailability();
            Boolean app_exists = !tmp;

            if (app_exists == false) {
                System.out.println("Creating application for AWS Beanstalk");
                AuxAdapter.commitBeanstalk(war, publicKey, secretKey, appName, appVersion, environment, bucket, host);
            } else if (app_exists == true) {
                System.out.println("Updating application for AWS Beanstalk");
                AuxAdapter.updateBeanstalk(war, publicKey, secretKey, appName, appVersion, environment, bucket, host);
            }
            ///TODO store in a table the pairing between appname and appversion whenever doing deploying an application
            ///then we can stop and start again the latest application version that was deployed
            /// Table must be stored in a file in local filesystem or even better online(in user's aws filesystem)
            BeansManageAppVersions beans_versioning = new BeansManageAppVersions();
            beans_versioning.storeVersion(appName, appVersion, environment);
            url = "http://" + environment + ".elasticbeanstalk.com/";

        }//end if beanstalk
        else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            url = AuxAdapter.deployCloudBees(war, publicKey, secretKey, appName, accountName, type, apiversion, description);
        } else if (PaaS.equalsIgnoreCase(GAE)) {
//         AppEngine_deploy gae= new AppEngine_deploy();
//        gae.deployVersion(war, accountName, secretKey, appName, appVersion );
            System.out.println("Google Appengine Upload not implemented yet");
        } else if (PaaS.equalsIgnoreCase(CLOUDCONTROL)) {
            //CloudControl code here
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            url = AuxAdapter.createAndDeployCF(publicKey, secretKey, appName, war);
        }

        ret = url;
        return ret;
    }//eom uploadAndDeployToEnv
    //////////////////////////////////////////////////////////////////////////////////

    public static String deploy(String PaaS, String war, String publicKey, String secretKey, String accountName, String appName, String appVersion,
            String environment, String bucket, String host, String type, String apiversion, String description) throws Cloud4SoaException {
        String ret = "";
        String url = "";
        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {



            // fix in order to keep it simple
            //no environment name needed, just the application will do the work
            environment = appName;


            //TODO check if application exists-> if exists call update, else call commit

            //check if environment(appname url) exists-> if exists call update, else call commit
            BeansCheckDNSAvailabity check_availability = new BeansCheckDNSAvailabity(publicKey, secretKey, environment);

            Boolean tmp = check_availability.CheckAvailability();
            Boolean app_exists = !tmp;

            if (app_exists == false) {
                System.out.println("Creating application for AWS Beanstalk");
                AuxAdapter.commitBeanstalk("no_path_given", publicKey, secretKey, appName, appVersion, environment, bucket, host);
            } else if (app_exists == true) {
                System.out.println("Updating application for AWS Beanstalk");
                AuxAdapter.updateBeanstalk("no_path_given", publicKey, secretKey, appName, appVersion, environment, bucket, host);
            }
            ///TODO store in a table the pairing between appname and appversion whenever doing deploying an application
            ///then we can stop and start again the latest application version that was deployed
            /// Table must be stored in a file in local filesystem or even better online(in user's aws filesystem)
            BeansManageAppVersions beans_versioning = new BeansManageAppVersions();
            beans_versioning.storeVersion(appName, appVersion, environment);
            url = "http://" + environment + ".elasticbeanstalk.com/";

        }//end if beanstalk
        else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            System.out.println("Not supported from CloudBees.");
        } else if (PaaS.equalsIgnoreCase(GAE)) {
//         AppEngine_deploy gae= new AppEngine_deploy();
//        gae.deployVersion(war, accountName, secretKey, appName, appVersion );
            System.out.println("Google Appengine Upload not implemented yet");
        } else if (PaaS.equalsIgnoreCase(CLOUDCONTROL)) {
            //CloudControl code here
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            url = AuxAdapter.deployCF(publicKey, secretKey, appName, war);
        }

        ret = url;
        return ret;
    }//eom deploy
    //////////////////////////////////////////////////////////////////////////////////

    public static String deployToEnv(String PaaS, String war, String publicKey, String secretKey, String accountName, String appName, String appVersion,
            String environment, String bucket, String host, String type, String apiversion, String description) throws Cloud4SoaException {
        String ret = "";
        String url = "";
        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {



            // fix in order to keep it simple
            //no environment name needed, just the application will do the work
            environment = appName;


            //TODO check if application exists-> if exists call update, else call commit

            //check if environment(appname url) exists-> if exists call update, else call commit
            BeansCheckDNSAvailabity check_availability = new BeansCheckDNSAvailabity(publicKey, secretKey, environment);

            Boolean tmp = check_availability.CheckAvailability();
            Boolean app_exists = !tmp;

            if (app_exists == false) {
                System.out.println("Creating application for AWS Beanstalk");
                AuxAdapter.commitBeanstalk("no_path_given", publicKey, secretKey, appName, appVersion, environment, bucket, host);
            } else if (app_exists == true) {
                System.out.println("Updating application for AWS Beanstalk");
                AuxAdapter.updateBeanstalk("no_path_given", publicKey, secretKey, appName, appVersion, environment, bucket, host);
            }
            ///TODO store in a table the pairing between appname and appversion whenever doing deploying an application
            ///then we can stop and start again the latest application version that was deployed
            /// Table must be stored in a file in local filesystem or even better online(in user's aws filesystem)
            BeansManageAppVersions beans_versioning = new BeansManageAppVersions();
            beans_versioning.storeVersion(appName, appVersion, environment);
            url = "http://" + environment + ".elasticbeanstalk.com/";

        }//end if beanstalk
        else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            System.out.println("Not supported from CloudBees.");
        } else if (PaaS.equalsIgnoreCase(GAE)) {
//         AppEngine_deploy gae= new AppEngine_deploy();
//        gae.deployVersion(war, accountName, secretKey, appName, appVersion );
            System.out.println("Google Appengine Upload not implemented yet");
        } else if (PaaS.equalsIgnoreCase(CLOUDCONTROL)) {
            //CloudControl code here
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            url = AuxAdapter.deployCF(publicKey, secretKey, appName, war);
        }

        ret = url;
        return ret;
    }//eom deployToEnv
    //////////////////////////////////////////////////////////////////////////////////

    public static String undeploy(String PaaS, String war, String publicKey, String secretKey, String accountName, String appName, String appVersion,
            String environment, String bucket, String host, String type, String apiversion, String description) throws Cloud4SoaException {
        String ret = "";
        String url = "";
        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {
            //TODO also delete the version file
        }//end if beanstalk
        else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            System.out.println("Not supported from CloudBees.");
        } else if (PaaS.equalsIgnoreCase(GAE)) {
        } else if (PaaS.equalsIgnoreCase(CLOUDCONTROL)) {
            //CloudControl code here
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            //Not supported by CF
        }

        ret = url;
        return ret;
    }//eom undeploy
    //////////////////////////////////////////////////////////////////////////////////

    public static String attachEnvironment(String PaaS) throws Cloud4SoaException {
        String ret = "";
        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {
            //AWS_BEANSTALK code here
        } else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            //CLOUDBEES code here
            System.out.println("attachEnvironment method is not applicable for CloudBees RUN@cloud.");
        } else if (PaaS.equalsIgnoreCase(CLOUDCONTROL)) {
            //CloudControl code here
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            //Not supported by CF yet (will be available in CloudController V2)
        }
        return ret;
    }//eom attachEnvironment

    public static String stop(String PaaS, String publicKey, String secretKey, String accountName, String appName, String appVersion,
            String environment, String bucket, String host, String type, String apiversion, String description) throws Cloud4SoaException {
        String ret = null;



        //should keep current version also for future restarts
        //current version could be stored within the stopapp.war
        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {




            // fix in order to keep it simple
            //no environment name needed, just the application will do the work
            environment = appName;




            ///TODO: keep Working version before stop

            //Create unique version for StopApp.war
            String autogen = UUID.randomUUID().toString().replaceAll("-", "");
            String stop_unique_version = "version.stop." + autogen.toUpperCase();
            //TODO
            //Don't upload StopApp every time stop is called, just update environment with existing

            AuxAdapter.updateBeanstalk("extras/StopApp.war", publicKey, secretKey, appName, stop_unique_version, environment, bucket, host);
        } else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            AuxAdapter.operateCloudBees("STOP", publicKey, secretKey, appName, accountName, type, apiversion);
        } else if (PaaS.equalsIgnoreCase(CLOUDCONTROL)) {
            //CloudControl code here
//    	   AuxAdapter.operateCloudControl(
//    			   "STOP", 
//    			   publicKey, 
//    			   secretKey, 
//    			   accountName, 
//    			   appName, 
//    			   appVersion, 
//    			   environment, 
//    			   bucket, 
//    			   host, 
//    			   type, 
//    			   description/*, 
//    			   apiVersion*/
//    		);
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            //AuxAdapter.operateCloudFoundry("STOP", publicKey, secretKey, appName);
        }


        return ret;
    }//eom stop
    //////////////////////////////////////////////////////////////////////////////////

    public static String start(String PaaS, String publicKey, String secretKey, String accountName, String appName, String appVersion,
            String environment, String bucket, String host, String type, String apiversion, String description) throws Cloud4SoaException {
        String ret = null;


        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {


            // fix in order to keep it simple
            //no environment name needed, just the application will do the work
            environment = appName;




            BeansManageAppVersions beans_versioning = new BeansManageAppVersions();
            //get last version that worked. can also provide list of other version to choose from
            String latest_version = beans_versioning.findLatestVersion(appName, environment);
            BeansStartApp bst = new BeansStartApp();

            bst.startApp(publicKey, secretKey, appName, latest_version, environment);

        } else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            AuxAdapter.operateCloudBees("START", publicKey, secretKey, appName, accountName, type, apiversion);
        } else if (PaaS.equalsIgnoreCase(CLOUDCONTROL)) {
            //CloudControl code here
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            //AuxAdapter.operateCloudFoundry("START", publicKey, secretKey, appName);
        }


        return ret;
    }//eom start
    //////////////////////////////////////////////////////////////////////////////////

    public static String delete(String PaaS, String publicKey, String secretKey, String accountName, String appName, String appVersion,
            String environment, String bucket, String host, String type, String apiversion, String description) throws Cloud4SoaException {

        String ret = "";
        String resp = "";

        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {
            //System.out.println("Not implemented yet.");



            // fix in order to keep it simple
            //no environment name needed, just the application will do the work
            environment = appName;


            //TODO also delete the version file
            AuxAdapter.deleteBeanstalk(publicKey, secretKey, environment);
        } else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            resp = AuxAdapter.operateCloudBees("DELETE", publicKey, secretKey, appName, accountName, type, apiversion);
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            //Deprecated method
        }

        ret = resp;
        return ret;
    }//eom delete
    //////////////////////////////////////////////////////////////////////////////////

    public static String listApplications(String PaaS, String publicKey, String secretKey, String accountName, String appName, String appVersion,
            String environment, String bucket, String host, String type, String apiversion, String description) throws Cloud4SoaException {
        String ret = null;


        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {
            //  CreateAppVersionBeanstalk(war, publicKey, secretKey, appName, appVersion, environment, bucket, host);
        } else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            String msg = AuxAdapter.operateCloudBees("LIST", publicKey, secretKey, "", accountName, type, apiversion);
            System.out.println("cloubees_list_msg:" + msg);
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            //Deprecated, no need to implement
        }
        //CloudControl code here
        return ret;
    }//eom list applications
    //////////////////////////////////////////////////////////////////////////////////

    public static Boolean checkAppAvailability(String PaaS, String publicKey, String secretKey, String accountName, String appName,
            String environment, String type, String apiversion) throws Cloud4SoaException {
        Boolean ret = null;


        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {
            //  CreateAppVersionBeanstalk(war, publicKey, secretKey, appName, appVersion, environment, bucket, host);

            BeansCheckDNSAvailabity check_availability = new BeansCheckDNSAvailabity(publicKey, secretKey, environment);
            ret = check_availability.CheckAvailability();

        } else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            AuxAdapter.operateCloudBees("LIST", publicKey, secretKey, appName, accountName, type, apiversion);
            //check responce to find if the following app exists for the specific accountName
            //sample responce---->accountName/appName
            //<ApplicationListResponse>
            // <applications>
            //<ApplicationInfo>
            //<id>testaccountname/newapp</id>


        } else if (PaaS.equalsIgnoreCase(CLOUDCONTROL)) {
            //CloudControl code here
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            try {
                AuxAdapter.getApplicationCF(publicKey, secretKey, appName);
                ret = false;
            } catch (Exception e) {
                ret = true;
            }
        }

        return ret;
    }//eom checkAppAvailability
    /////////////////////////////////////////////////////////////////////////////

    public static String getAppURL(String PaaS, String publicKey, String secretKey, String accountName, String appName,
            String environment, String type, String apiversion) throws Cloud4SoaException {
        String ret = "";


        if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            String url = AuxAdapter.operateCloudBees("URL", publicKey, secretKey, appName, accountName, type, apiversion);
            System.out.println("responce----:" + url);

            ret = "http://" + url;
        } else if (PaaS.equalsIgnoreCase(OPENSHIFT_X)) {
            // OPENSHIFT
            //public key= username, secretkey=password
            Openshift_Aux open = new Openshift_Aux("", publicKey, secretKey);
            ///application name, cartrige(server) type. leave blank for jboss7
            ret = open.getApplicationUrl(appName);
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            ret = AuxAdapter.getApplicationUriCF(publicKey, secretKey, appName);
        }

        return ret;
    }

    public static String getAppStatus(String PaaS, String publicKey, String secretKey, String accountName, String appName,
            String environment, String type, String apiversion) throws Cloud4SoaException {
        String ret = "";

        String status = "";
        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {
            beanstalk.BeansDescribeEnvironments bst = new BeansDescribeEnvironments();
            String aws_resp = bst.getEnvironments(publicKey, secretKey, appName, "", "", "");
            utils.ParseXmlString parser = new ParseXmlString();
            status = parser.parse(aws_resp, "DescribeEnvironmentsResult", "Status");

        } else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            status = AuxAdapter.operateCloudBees("INFO", publicKey, secretKey, appName, accountName, type, apiversion);
            System.out.println("responce----:" + status);
            //check responce to find if the following app exists for the specific accountName
            //sample responce---->accountName/appName
            //<ApplicationListResponse>
            // <applications>
            //<ApplicationInfo>
            //<id>testaccountname/newapp</id>

        } else if (PaaS.equalsIgnoreCase(CLOUDCONTROL)) {
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            status = AuxAdapter.getAppStatusCF(publicKey, secretKey, appName);
        }

        ///////////////code to unify status responses of PaaSes

        //ParseXmlString parser= new ParseXmlString();
        //ret=parser.parse(cloudbees_status, "error", "message");

        ret = status;
        return ret;
    }//eom getAppStatus

    public static String getRunningStatus(String PaaS, String publicKey, String secretKey, String accountName, String appName,
            String environment, String type, String apiversion) throws Cloud4SoaException {
        String ret = "";

        String status = "";
        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {

            //TODO: at first find the where is the running application

            beanstalk.BeansDescribeEnvironments bst = new BeansDescribeEnvironments();
            String aws_resp = bst.getEnvironments(publicKey, secretKey, appName, "", "", "");
            utils.ParseXmlString parser = new ParseXmlString();
            status = parser.parse(aws_resp, "DescribeEnvironmentsResult", "Status");

        } else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            status = AuxAdapter.operateCloudBees("INFO", publicKey, secretKey, appName, accountName, type, apiversion);
            System.out.println("responce----:" + status);
            //check responce to find if the following app exists for the specific accountName
            //sample responce---->accountName/appName
            //<ApplicationListResponse>
            // <applications>
            //<ApplicationInfo>
            //<id>testaccountname/newapp</id>

        } else if (PaaS.equalsIgnoreCase(CLOUDCONTROL)) {
            //CloudControl code here
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            status = AuxAdapter.getRunningStatusCF(publicKey, secretKey, appName);
        }

        ///////////////code to unify status responses of PaaSes

        //ParseXmlString parser= new ParseXmlString();
        //ret=parser.parse(cloudbees_status, "error", "message");

        ret = status;
        return ret;
    }///getRunningStatus

//////////////DB RELATED METHODS//////////////////
    public static DatabaseInfo createDB(String PaaS, String publicKey, String secretKey, String accountName, String host, String type,
            String apiversion, String description, String dbname, String dbuser, String dbpassword) throws Cloud4SoaException {

        DatabaseInfo databaseInfo=new DatabaseInfo();


        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {

            BeansDatabase beans_db = new BeansDatabase();
            //beans_db.createDatabase(publicKey, secretKey, dbname, "MySQL", dbname + "cloud4soaid", "instance class is ignored", 5, dbuser, dbpassword);
            DatabaseObject dbobj = beans_db.createDatabaseAndReturnInfo(publicKey, secretKey, dbname, "MySQL", dbname + "cloud4soaid", "instance class is ignored", 5, dbuser, dbpassword);
            databaseInfo.setDatabaseName(dbobj.getDbname());
            databaseInfo.setHost(dbobj.getDbhost());
            databaseInfo.setDatabaseUrl(dbobj.getDbhost());
            databaseInfo.setPort(dbobj.getPort().toString());
            databaseInfo.setUserName(dbuser);
            databaseInfo.setPassword(dbpassword);
            
            //String acceptallips = "0.0.0.0/0";
            // beanstalk.BeansDatabase beansdb = new BeansDatabase();
            //call this only if it has not been already created
            //beans_db.allowIPConnectionWithDB(publicKey, secretKey, acceptallips);

        } else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            DatabaseObject dbobj=AuxAdapter.operateDatabaseCloudBees("DBCREATE", publicKey, secretKey, "", accountName, type, apiversion, dbname, dbuser, dbpassword);
            databaseInfo.setDatabaseName(dbobj.getDbname());
            databaseInfo.setHost(dbobj.getDbhost());
            databaseInfo.setDatabaseUrl(dbobj.getDbhost());
            databaseInfo.setPort(dbobj.getPort().toString());
            databaseInfo.setUserName(dbuser);
            databaseInfo.setPassword(dbpassword);
        
        } else if (PaaS.equalsIgnoreCase(CLOUDCONTROL)) {
            //CloudControl code here
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            //the appName is crucial for any DB operation in CloudFoundry
            //AuxAdapter.operateDatabaseCloudFoundry("DBCREATE", publicKey, secretKey, "", accountName, type, apiversion, dbname, dbuser, dbpassword);
        }        
        return databaseInfo;
    }

    public static DatabaseObject getDBInfo(String PaaS, String publicKey, String secretKey, String accountName, String host, String dbtype,
            String apiversion, String description, String dbname, String dbuser, String dbpassword) throws Cloud4SoaException {
        DatabaseObject dbObject = new DatabaseObject();

        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {
            //
            BeansDatabase beandb = new BeansDatabase();
            //beandb.getDBEndpoint(publicKey, secretKey,dbname);
            dbObject = beandb.getDBInstanceInfo(publicKey, secretKey, dbname);

        } else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            dbObject = AuxAdapter.operateDatabaseCloudBees("DBINFO", publicKey, secretKey, "", accountName, dbtype, apiversion, dbname, dbuser, dbpassword);
        } else if (PaaS.equalsIgnoreCase(CLOUDCONTROL)) {
            //CloudControl code here
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            //dbObject=AuxAdapter.operateDatabaseCloudFoundry("DBINFO", publicKey, secretKey, "", accountName, dbtype, apiversion, dbname, dbuser, dbpassword);
        }
        return dbObject;
    }

    public static String getDBList(String PaaS, String publicKey, String secretKey, String accountName, String host, String type,
            String apiversion, String description, String dbname, String dbuser, String dbpassword) throws Cloud4SoaException {
        String ret = null;

        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {
            //
            BeansDatabase beandb = new BeansDatabase();
            //beandb.getDBEndpoint(publicKey, secretKey,dbname);
            beandb.getDBInstanceInfo(publicKey, secretKey, dbname);

        } else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            AuxAdapter.operateDatabaseCloudBees("DBLIST", publicKey, secretKey, "", accountName, type, apiversion, dbname, dbuser, dbpassword);
        } else if (PaaS.equalsIgnoreCase(CLOUDCONTROL)) {
            //CloudControl code here
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            //AuxAdapter.operateDatabaseCloudFoundry("DBLIST", publicKey, secretKey, "", accountName, type, apiversion, dbname, dbuser, dbpassword);
        }
        return ret;
    }

    public static String deleteDB(String PaaS, String publicKey, String secretKey, String accountName, String host, String type,
            String apiversion, String description, String dbname, String dbuser, String dbpassword) throws Cloud4SoaException {
        String ret = null;

        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {

            BeansDatabase beans_db = new BeansDatabase();

            beans_db.deleteDatabase(publicKey, secretKey, dbname + "cloud4soaid");

        } else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            AuxAdapter.operateDatabaseCloudBees("DBDELETE", publicKey, secretKey, "", accountName, type, apiversion, dbname, dbuser, dbpassword);
        } else if (PaaS.equalsIgnoreCase(CLOUDCONTROL)) {
            //CloudControl code here
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            //AuxAdapter.operateDatabaseCloudFoundry("DBDELETE", publicKey, secretKey, "", accountName, type, apiversion, dbname, dbuser, dbpassword);
        }
        return ret;
    }

    public static void downloadDBCF(String publicKey, String secretKey, String dbtype, String dbname, String fileToStore) throws Cloud4SoaException {
        if (dbname == null || dbname.isEmpty()) {
            throw new Cloud4SoaException("database name cannot be empty");
        }
        try {
            DatabaseCredentials cred = AuxAdapter.openTunnelCF(publicKey, secretKey, dbtype, dbname);
            //DatabaseCredentials cred = new DatabaseCredentials("aa","aa");
            utils.DatabaseHelper dbhelp = new DatabaseHelper();
            ///forMySQL
            dbhelp.storeDB("127.0.0.1", String.valueOf(AuxAdapter.TUNNEL_LOCAL_PORT), cred.user, cred.pass, cred.tunnel_name, fileToStore);
            //TODO: Implement download for other services apart from mysql
        } catch (Exception ex) {
            Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
            throw new Cloud4SoaException("Erron when downloading db with name " + dbname + " at CloudFoundry. Message:" + ex.getMessage());
        } finally {
            AuxAdapter.closeTunnelCF();
        }
    }

    public static void restoreDBCF(String publicKey, String secretKey, String dbtype, String dbname, String fileToStore) throws Cloud4SoaException {
        if (dbname == null || dbname.isEmpty()) {
            throw new Cloud4SoaException("database name cannot be empty");
        }
        try {
            DatabaseCredentials cred = AuxAdapter.openTunnelCF(publicKey, secretKey, dbtype, dbname);
            utils.DatabaseHelper dbhelp = new DatabaseHelper();
            ///forMySQL
            dbhelp.restoredb("127.0.0.1", String.valueOf(AuxAdapter.TUNNEL_LOCAL_PORT), cred.user, cred.pass, cred.tunnel_name, fileToStore);
        } catch (Exception ex) {
            Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
            throw new Cloud4SoaException("Erron when downloading db with name " + dbname + " at CloudFoundry. Message:" + ex.getMessage());
        } finally {
            AuxAdapter.closeTunnelCF();
        }
    }

    public static String downloadDB_REST(String host, String port, String dbtype, String dbname, String dbuser, String dbpassword, String localfile) throws Cloud4SoaException {
        String ret = null;

        if (port == null || port.equalsIgnoreCase("")) {
            port = "3306";
        }

        utils.DatabaseHelper dbhelp = new DatabaseHelper();

        try {

            ///forMySQL
            dbhelp.storeDB(host, port, dbuser, dbpassword, dbname, localfile);
            //String db_content=dbhelp.getData(hostname, port, db_user, db_user_password, database);
            //System.out.println("db_content"+db_content);

        } catch (Exception ex) {
            Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
            throw new Cloud4SoaException("Erron when downloading db with name " + dbname + " at host " + host + ", for user " + dbuser + ". Message:" + ex.getMessage());
        }
        return ret;
    }

    public static String restoreDB_REST(String host, String port, String dbtype, String dbname, String dbuser, String dbpassword, String localfile) throws Cloud4SoaException {
        String ret = null;
        if (port == null || port.equalsIgnoreCase("")) {
            port = "3306";
        }
        utils.DatabaseHelper dbhelp = new DatabaseHelper();
        try {
            ///forMySQL
            dbhelp.restoredb(host, port, dbuser, dbpassword, dbname, localfile);

        } catch (Exception ex) {
            Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
            throw new Cloud4SoaException("Erron when restoring db from file, with db name " + dbname + " at host " + host + ", for user " + dbuser + ". Message:" + ex.getMessage());

        }

        return ret;
    }

    public static String downloadDB(String PaaS, String publicKey, String secretKey, String accountName, String host,
            String type, String apiversion, String description, String dbname, String dbuser, String dbpassword, String localfile) throws Cloud4SoaException {
        String ret = null;

        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {
            //  CreateAppVersionBeanstalk(war, publicKey, secretKey, appName, appVersion, environment, bucket, host);
            utils.DatabaseHelper dbhelp = new DatabaseHelper();
            try {
                String hostname = dbname + "cloud4soaid" + ".coaodqyxxykq.us-east-1.rds.amazonaws.com";

                dbhelp.storeDB(hostname, "3306", dbuser, dbpassword, dbname, localfile);

            } catch (Exception ex) {
                Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            // OperateCloudBees("DBLIST", publicKey, secretKey, "", accountName, type, apiversion);
          /*
             * String hostname="localhost"; String port="3306"; String
             * database="cloud4soa"; String db_user="root"; String
             * db_user_password="!uflow!";
             *
             */

            String hostname = "ec2-174-129-9-255.compute-1.amazonaws.com";
            String port = "3306";

            utils.DatabaseHelper dbhelp = new DatabaseHelper();
            try {

                dbhelp.storeDB(hostname, port, dbuser, dbpassword, dbname, localfile);

            } catch (Exception ex) {
                Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (PaaS.equalsIgnoreCase(CLOUDCONTROL)) {
            //CloudControl code here
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            //In remote adapter
        }
        return ret;
    }

    public static String restoreDB(String PaaS, String publicKey, String secretKey, String accountName, String host, String type,
            String apiversion, String description, String dbname, String dbuser, String dbpassword, String localfile) throws Cloud4SoaException {
        String ret = null;

        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {

            utils.DatabaseHelper dbhelp = new DatabaseHelper();
            try {
                String hostname = dbname + "cloud4soaid" + ".coaodqyxxykq.us-east-1.rds.amazonaws.com";
                dbhelp.restoredb(hostname, "3306", dbuser, dbpassword, dbname, localfile);

            } catch (Exception ex) {
                Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
            }


        } else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {

            String hostname = "ec2-174-129-9-255.compute-1.amazonaws.com";
            String port = "3306";

            utils.DatabaseHelper dbhelp = new DatabaseHelper();
            try {

                dbhelp.restoredb(hostname, port, dbuser, dbpassword, dbname, localfile);

            } catch (Exception ex) {
                Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ret;
    }

///////////////////START VERSIONING OPERATIONS///////////////////////////////////////
    public static String getAppVersions(String PaaS, String publicKey, String secretKey, String appName) throws Cloud4SoaException {
        String ret = "";

        //prosexe de 8elw na kanei kai deploy
        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {
            beanstalk.BeansDescribeApplicationVersions bst = new BeansDescribeApplicationVersions();
            bst.decribeApplicationVersions(publicKey, secretKey, appName);
        } else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            System.out.println("Operation Not Supported From CloudBees Run@Cloud");
        } else if (PaaS.equalsIgnoreCase(CLOUDCONTROL)) {
            //CloudControl code here
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            //Not applicable
        }
        return ret;
    }//eom getAppVersions

    //This method presuppose that app exists already, and just an new version is uploaded
    public static String createAppVersion(String PaaS, String war, String publicKey, String secretKey, String accountName, String appName, String appVersion,
            String environment, String bucket, String host, String type, String apiversion, String description) throws Cloud4SoaException {
        String ret = null;


        //prosexe de 8elw na kanei kai deploy
        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {
            BeansCreateApplicationVersion beansappversion = new BeansCreateApplicationVersion();
            try {
                beansappversion.creatappversion(publicKey, secretKey, war, bucket, appName, appVersion);
                //CreateAppVersionBeanstalk(war, publicKey, secretKey, appName, appVersion, environment, bucket, host);
            } catch (Exception ex) {
                Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
            }
            ////////////////////////////

            ////////////////////////////
        } else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            AuxAdapter.deployCloudBees(war, publicKey, secretKey, appName, accountName, type, apiversion, description);
        } else if (PaaS.equalsIgnoreCase(CLOUDCONTROL)) {
            //CloudControl code here
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            //Not applicable
        }
        return ret;
    }//eom createAppVersion

    public static String createApplication(String PaaS, String publicKey, String secretKey, String accountName, String appName, String description) throws Cloud4SoaException {
        String ret = null;


        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {
            ////////////////////////////
        } else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            //AuxAdapter.deployCloudBees(war, publicKey, secretKey, appName, accountName, type, apiversion, description);
        } else if (PaaS.equalsIgnoreCase(CLOUDCONTROL)) {
            // CLOUDCONTROL
//    	   ret = AuxAdapter.operateCloudControl("START", publicKey, secretKey, accountName, appName, appversion, environment, bucket, host, type, description, apiversion).toString();
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            AuxAdapter.createApplicationCF(publicKey, secretKey, appName);
        }
        return ret;
    }//eom createApplication
////////////////////////////////////////////////////////////////////////////////

    public static String deleteApplication(String PaaS, String war, String publicKey, String secretKey, String accountName, String appName,
            String environment, String bucket, String host, String type, String apiversion, String description) throws Cloud4SoaException {
        String ret = null;


        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {
            ////////////////////////////
        } else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            //AuxAdapter.deployCloudBees(war, publicKey, secretKey, appName, accountName, type, apiversion, description);
        } else if (PaaS.equalsIgnoreCase(CLOUDCONTROL)) {
            // TODO
            //CloudControl code here
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            AuxAdapter.deleteApplicationCF(publicKey, secretKey, appName);
        }
        return ret;
    }//eom deleteApplication
////////////////////////////////////////////////////////////////////////////////

    public static String updateApplication(String PaaS, String war, String publicKey, String secretKey, String accountName, String appName,
            String environment, String bucket, String host, String type, String apiversion, String description) throws Cloud4SoaException {
        String ret = null;


        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {
            ////////////////////////////
        } else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            //AuxAdapter.deployCloudBees(war, publicKey, secretKey, appName, accountName, type, apiversion, description);
        } else if (PaaS.equalsIgnoreCase(CLOUDCONTROL)) {
            //CloudControl code here
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            //Not applicable since there are no parameters to update, except the environment which is not 
            //available yet (it will be available on Cloud Controller V2)
        }
        return ret;
    }//eom updateApplication
////////////////////////////////////////////////////////////////////////////////

    public static String deleteAppVersion(String PaaS, String publicKey, String secretKey, String accountName, String appName, String appVersion,
            String environment, String bucket, String host, String type, String apiversion) throws Cloud4SoaException {
        String ret = "";


        if (PaaS.equalsIgnoreCase(AWS_BEANSTALK)) {
            beanstalk.BeansDeleteApplicationVersion bst = new BeansDeleteApplicationVersion();
            bst.deleteApplicationVersion(publicKey, secretKey, appName, appVersion);

            ///also delete the version from version files

        } else if (PaaS.equalsIgnoreCase(CLOUDBEES_RUN)) {
            System.out.println("Operation Not Supported From CloudBees Run@Cloud");
        } else if (PaaS.equalsIgnoreCase(CLOUDCONTROL)) {
            //CloudControl code here
        } else if (PaaS.equalsIgnoreCase(CLOUDFOUNDRY)) {
            //Not applicable
        }
        return ret;
    }//eom deleteAppVersion
// enviroment related operations have been removed
}
