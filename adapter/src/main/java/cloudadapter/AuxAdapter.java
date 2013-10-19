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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.ParseXmlString;
import beanstalk.BeansCheckDNSAvailabity;
import beanstalk.BeansDeleteApplication;
import beanstalk.BeansTerminateEnvironment;
import beanstalk.BeanstalkDeploy;
import beanstalk.BeanstalkDeployNoGUI;
import beanstalk.BeanstalkFirstDeployment;
import beanstalk.BeanstalkFirstDeploymentNoGUI;

import com.cloudbees.api.BeesClient;
import com.cloudbees.api.DatabaseInfo;

import eu.cloud4soa.api.datamodel.semantic.app.Application;
import eu.cloud4soa.api.util.exception.adapter.Cloud4SoaException;

import java.io.File;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cloudfoundry.caldecott.client.HttpTunnelFactory;
import org.cloudfoundry.caldecott.client.TunnelHelper;
import org.cloudfoundry.caldecott.client.TunnelServer;
import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.DatabaseCredentials;
import org.cloudfoundry.client.lib.domain.*;
import org.cloudfoundry.client.lib.domain.CloudApplication.AppState;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.ResourceAccessException;

/**
 *
 * @author Ledakis Giannis (SingularLogic)
 * Auxiliary class for interaction needed for some PaaS providers.
 * Methods are divided by PaaS provider
 */
public class AuxAdapter {

   
    /////////////////////////--CLOUDBEES--//////////////////////////////////////////////////////////////////////
    public static String deployCloudBees(String war, String ApiKey, String SecretKey, String applicationname,
            String account, String type, String apiversion, String description) throws Cloud4SoaException {
        String ret = "";


        String bees_server = "https://api.cloudbees.com/api";


        ///default type= xml
        if (type.equalsIgnoreCase("")) {
            type = "xml";
        }

        ///default apiversion= 1.0
        if (apiversion.equalsIgnoreCase("")) {
            apiversion = "1.0";
        }
        ///default applicationname= account
        if (applicationname.equalsIgnoreCase("")) {
            applicationname = account + "-cloud4soa-app";
        }



        //create app_id literal in the form of username/applicationname
        String app_id = account + "/" + applicationname;
        //what is environment is not quite clear. Sees like environment is the account name, that is the second part of the created link
        String environment = account;

        BeesClient bees = new BeesClient(bees_server, ApiKey, SecretKey, type, apiversion);
        try {
            bees.applicationDeployWar(app_id, environment, description, war, null, null);
            // return deployed;
        } catch (Exception ex) {
            // Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
            throw new Cloud4SoaException(ex.getMessage());

        }

        utils.ParseXmlString parser = new ParseXmlString();
        String url = parser.parse(bees.xml_response, "ApplicationDeployArchiveResponse", "url");

        System.out.println("Application deployed with URL: " + url);

        ret = url;
        return ret;
    }

    ///eom DeployCloudBees
    public static String operateCloudBees(String Operation, String ApiKey, String SecretKey, String applicationname,
            String account, String type, String apiversion) throws Cloud4SoaException {

        String ret = "";
        String cloudbees_resp = "";

        String bees_server = "https://api.cloudbees.com/api";


        ///default type= xml
        if (type.equalsIgnoreCase("")) {
            type = "xml";
        }

        ///default apiversion= 1.0
        if (apiversion.equalsIgnoreCase("")) {
            apiversion = "1.0";
        }
        ///default applicationname= account
        if (applicationname.equalsIgnoreCase("")) {
            applicationname = account + "-cloud4soa-app";
        }



        //create app_id literal in the form of username/applicationname
        String app_id = account + "/" + applicationname;
        //what is environment is not quite clear. Sees like environment is the account name, that is the second part of the created link
        String environment = account;

        BeesClient bees = new BeesClient(bees_server, ApiKey, SecretKey, type, apiversion);
        utils.ParseXmlString parser = new ParseXmlString();

        try {
            if (Operation.equalsIgnoreCase("START")) {
                bees.applicationStart(app_id);
                cloudbees_resp = parser.parse(bees.xml_response, "ApplicationStatusResponse", "status");
            }
            if (Operation.equalsIgnoreCase("STOP")) {
                bees.applicationStop(app_id);
                cloudbees_resp = parser.parse(bees.xml_response, "ApplicationStatusResponse", "status");
            }
            if (Operation.equalsIgnoreCase("DELETE")) {
                bees.applicationDelete(app_id);
                //cloudbees_resp=parser.parse(bees.xml_response, "ApplicationDeleteResponse", "deleted");
            }
            if (Operation.equalsIgnoreCase("INFO")) {
                bees.applicationInfo(app_id);
                cloudbees_resp = parser.parse(bees.xml_response, "application", "status");
            }
            if (Operation.equalsIgnoreCase("URL")) {
                bees.applicationInfo(app_id);
                cloudbees_resp = parser.parse(bees.xml_response, "application", "url");
            }
            if (Operation.equalsIgnoreCase("RESTART")) {
                bees.applicationRestart(app_id);
            }

            if (Operation.equalsIgnoreCase("LIST")) {
                bees.applicationList();
                cloudbees_resp = parser.parse(bees.xml_response, "ApplicationListResponse", "status");
            }
        } catch (Exception ex) {
            //Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
            throw new Cloud4SoaException(ex.getMessage());

        }

        ret = cloudbees_resp;
        return ret;
    }//eom OperateCloudBees

    public static DatabaseObject operateDatabaseCloudBees(String Operation, String ApiKey, String SecretKey, String applicationname,
            String account, String dbtype, String apiversion, String dbname, String dbuser, String dbpassport) throws Cloud4SoaException {

        DatabaseObject dbobj = new DatabaseObject();

        String bees_server = "https://api.cloudbees.com/api";
        String type = "xml";

        if (apiversion.equalsIgnoreCase("")) {
            apiversion = "1.0";
        }
        ///default applicationname= account
        if (applicationname.equalsIgnoreCase("")) {
            applicationname = account + "-cloud4soa-app";
        }


        BeesClient bees = new BeesClient(bees_server, ApiKey, SecretKey, type, apiversion);
        //utils.ParseXmlString parser = new ParseXmlString();

        try {
            if (Operation.equalsIgnoreCase("DBCREATE")) {

                bees.databaseCreate(account, dbname, dbuser, dbpassport);
                //parser.parse(bees.xml_response, "DatabaseCreateResponse", "databaseId");
            }

            if (Operation.equalsIgnoreCase("DBLIST")) {
                bees.databaseList();
            }

            if (Operation.equalsIgnoreCase("DBINFO")) {
                DatabaseInfo cloudbeesbdinfo = bees.databaseInfo(dbname, false);
                dbobj.setDbhost(cloudbeesbdinfo.getMaster());
                dbobj.setPort(cloudbeesbdinfo.getPort());
                dbobj.setDbname(cloudbeesbdinfo.getName());

            }

            if (Operation.equalsIgnoreCase("DBDELETE")) {

                bees.databaseDelete(dbname);
            }

        } catch (Exception ex) {
            throw new Cloud4SoaException(ex.getMessage());

        }
        return dbobj;

    }//eom OperateDatabaseCloudBees

    
    
    /////////////////////////--BEANSTALK--//////////////////////////////////////////////////////////////////////
    public static void updateBeanstalk(String war, String AWSKeyId, String AWSSecretKey, String applicationname, String appversion,
            String environment, String bucket, String host) throws Cloud4SoaException {
        boolean deployed = true;

        ///if no war file give show file chooser
        boolean showJFileLoader = false;
        if (war.equalsIgnoreCase("")) {
            showJFileLoader = true;
        }
        ///if no host give give default
        String host_name = host;
        if (host.equalsIgnoreCase("")) {
            host_name = "elasticbeanstalk.us-east-1.amazonaws.com";
        }

        ///if no bucket given, create bucket name from AWSKeyId
        //for this update function we don't actually create the bucket, just trying to guess the name
        String bucket_name = bucket;
        if (bucket.equalsIgnoreCase("")) {
            bucket_name = "s3-cloud4soa-autobucket-" + AWSKeyId.toLowerCase();
        }

        ///if no environment name given, create environment name from AWSKeyId
        //for this update function we don't actually create the environment, just trying to guess the name
        String environment_name = environment;
        if (environment.equalsIgnoreCase("")) {
            environment_name = "autogenenvironment-" + AWSKeyId.toLowerCase();
        }


        if (showJFileLoader == true) {
            BeanstalkDeploy bstDeploy = new BeanstalkDeploy(true);
            bstDeploy.deploy(war, AWSKeyId, AWSSecretKey, applicationname, appversion, environment_name, bucket_name, host_name);
        } else {
            BeanstalkDeployNoGUI bstDeploy = new BeanstalkDeployNoGUI(false);
            bstDeploy.deploy(war, AWSKeyId, AWSSecretKey, applicationname, appversion, environment_name, bucket_name, host_name);
        }


        //  return deployed;
    }

    public static void deleteBeanstalk(String AWSKeyId, String AWSSecretKey, String environment) {
        boolean deployed = true;


        ///if no environment name given, create environment name from AWSKeyId
        //for this update function we don't actually create the environment, just trying to guess the name
        String environment_name = environment;
        if (environment.equalsIgnoreCase("")) {
            environment_name = "autogenenvironment-" + AWSKeyId.toLowerCase();
        }



        BeansTerminateEnvironment bstDelete = new BeansTerminateEnvironment();
        BeansDeleteApplication bstappDelete = new BeansDeleteApplication();
        try {
            //delete environment
            bstDelete.terminateenvironment(AWSKeyId, AWSSecretKey, environment);
            //now delete application
            String appname = environment;
            bstappDelete.deleteApplication(AWSKeyId, AWSSecretKey, appname);
            //  return deployed;
        } catch (Exception ex) {
            Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
     public static void commitBeanstalk(String war, String AWSKeyId, String AWSSecretKey, String applicationname, String appversion,
            String environment, String bucket, String host) throws Cloud4SoaException {
        boolean deployed = true;

        ///if no war file give show file chooser
        boolean showJFileLoader = false;
        if (war.equalsIgnoreCase("")) {
            showJFileLoader = true;
        }
        ///if no host give give default
        String host_name = host;
        if (host.equalsIgnoreCase("")) {
            host_name = "elasticbeanstalk.us-east-1.amazonaws.com";
        }

        ///if no bucket given, create bucket name from AWSKeyId
        String bucket_name = bucket;
        if (bucket.equalsIgnoreCase("")) {
            bucket_name = "s3-cloud4soa-autobucket-" + AWSKeyId.toLowerCase();
        }

        ///if no environment name given, create environment name from AWSKeyId
        String environment_name = environment;
        // if (environment.equalsIgnoreCase("")) {
        //     environment_name = "c4soa-" + AWSKeyId.toLowerCase().;
        //  }

        //Check if CNAME is available. CNAME is envirmonment name

        BeansCheckDNSAvailabity check_availability = new BeansCheckDNSAvailabity(AWSKeyId, AWSSecretKey, environment);
        if (check_availability.CheckAvailability() == true) {

            if (showJFileLoader == true) {
                BeanstalkFirstDeployment beans_first = new BeanstalkFirstDeployment(showJFileLoader);
                beans_first.deploy(war, AWSKeyId, AWSSecretKey, applicationname, appversion, environment_name, bucket_name, host_name);
            } else {
                BeanstalkFirstDeploymentNoGUI beans_first = new BeanstalkFirstDeploymentNoGUI(showJFileLoader);
                beans_first.deploy(war, AWSKeyId, AWSSecretKey, applicationname, appversion, environment_name, bucket_name, host_name);

            }

        }//end if available
        else {
            System.out.println("Environment Name :" + environment + " is not available.Please choose another environment name.");
        }//end if name not available



        // return deployed;
    }

     
    /////////////////////////--CLOUDFOUNDRY--//////////////////////////////////////////////////////////////////////
    public static String CC_URL = "http://api.cf.cloud4soa.eu";
    //public static String CC_URL = "https://api.cloudfoundry.com";
    public static String DEFAULT_RUNTIME = "java";
    public static String DEFAULT_FRAMEWORK = "java_web";
    public static int DEFAULT_MEM = 512;
    public static String TUNNEL_LOCAL_HOST = "localhost";
    public static int TUNNEL_LOCAL_PORT = 51534;

    private static String createApplicationCFbase(String email, String password, String appName, CloudFoundryClient client) throws Cloud4SoaException {
        if (appName == null || appName.isEmpty()) {
            throw new Cloud4SoaException("Application name cannot be empty");
        }
        try {
            List<String> uris = new ArrayList<String>();
            String uri = generateAppURL(appName);
            uris.add(uri);
            client.createApplication(appName, new Staging(DEFAULT_RUNTIME, DEFAULT_FRAMEWORK), DEFAULT_MEM, uris, null, true);
            return uri;
        } catch (Exception e) {
            throw new Cloud4SoaException(e.getMessage());
        }
    }

    public static String createApplicationCF(String email, String password, String appName) throws Cloud4SoaException {
        CloudFoundryClient client = null;
        try {
            client = init(email, password);
            String uri = AuxAdapter.createApplicationCFbase(email, password, appName, client);
            return uri;
        } catch (Exception e) {
            throw new Cloud4SoaException(e.getMessage());
        } finally {
            if (client != null) finalize(client);
        }
    }

    public static void deleteApplicationCF(String email, String password, String appName) throws Cloud4SoaException {
        if (appName == null || appName.isEmpty()) {
            return;
        }
        CloudFoundryClient client = null;
        try {
            client = init(email, password);
            client.deleteApplication(appName);
        } catch (CloudFoundryException e) {
            if (!e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new Cloud4SoaException(e.getMessage());
            }
            // appliation already deleted or no present, so no problem
        } finally {
            if (client != null) finalize(client);
        }
    }

    public static Application getApplicationCF(String email, String password, String appName) throws Cloud4SoaException {
       CloudFoundryClient client = null;
        try {
            client = init(email, password);
            CloudApplication capp = client.getApplication(appName);
            Application app = new Application();
            app.setApplicationcode(capp.getName());
            return app;
        } catch (CloudFoundryException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new Cloud4SoaException("The application " + appName + " cannot be found");
            }
            throw new Cloud4SoaException(e.getMessage());
        } finally {
            if (client != null) finalize(client);
        }
    }

    public static String getApplicationUriCF(String email, String password, String appName) throws Cloud4SoaException {
        CloudFoundryClient client = null;
        try {
            client = init(email, password);
            CloudApplication app = client.getApplication(appName);
            return "http://" + app.getUris().get(0);
        }  catch (CloudFoundryException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new Cloud4SoaException("The application " + appName + " cannot be found");
            }
            throw new Cloud4SoaException(e.getMessage());
        } finally {
            if (client != null) finalize(client);
        }
    }

    public static String deployCF(String email, String password, String appName, String binary) throws Cloud4SoaException {
        CloudFoundryClient client = null;
        try {
            client = init(email, password);
            CloudApplication app = client.getApplication(appName);
            File f = new File(binary);
            client.stopApplication(appName);
            client.uploadApplication(appName, f);
            client.startApplication(appName);
            return "http://" + app.getUris().get(0);
        }  catch (CloudFoundryException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new Cloud4SoaException("The application " + appName + " cannot be found");
            }
            throw new Cloud4SoaException(e.getMessage());
        } catch (IOException e) {
            throw new Cloud4SoaException("An error occured while uploading the application.");
        } finally {
            if (client != null) finalize(client);
        }
    }

    public static String createAndDeployCF(String email, String password, String appName, String binary) throws Cloud4SoaException {
        CloudFoundryClient client = null;
        try {
            client = init(email, password);
            try {
                client.getApplication(appName);
            } catch (CloudFoundryException ce) {
                if (ce.getStatusCode() == HttpStatus.NOT_FOUND) {
                    AuxAdapter.createApplicationCFbase(email, password, appName, client);
                } else {
                    throw ce;
                }
            }
            File f = new File(binary);
            client.stopApplication(appName);
            client.uploadApplication(appName, f);
            client.startApplication(appName);
            CloudApplication app = client.getApplication(appName);
            return "http://" + app.getUris().get(0);
        } catch (CloudFoundryException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new Cloud4SoaException("The application " + appName + " cannot be found");
            }
            throw new Cloud4SoaException(e.getMessage());
        } catch (IOException e) {
            throw new Cloud4SoaException("An error occured while uploading the application.");
        } finally {
            if (client != null) finalize(client);
        }
    }

    public static String getAppStatusCF(String email, String password, String appName) throws Cloud4SoaException {
        CloudFoundryClient client = null;
        try {
            client = init(email, password);
            CloudApplication app = client.getApplication(appName);
            String res = "deployed";
            try {
                client.getFile(appName, 0, "app", 0, 1);
            } catch (CloudFoundryException e) {
                try {
                    client.getFile(appName, 0, "tomcat", 0, 1);
                } catch (CloudFoundryException e2) {
                    res = "created";
                }
            }
            return res;
        } catch (CloudFoundryException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new Cloud4SoaException("The application " + appName + " cannot be found");
            }
            throw new Cloud4SoaException(e.getMessage());
        } finally {
            if (client != null) finalize(client);
        }
    }

    public static String getRunningStatusCF(String email, String password, String appName) throws Cloud4SoaException {
        CloudFoundryClient client = null;
        try {
            client = init(email, password);
            CloudApplication app = client.getApplication(appName);

            //Checking instances logs for crash information
            CrashesInfo crashes = client.getCrashes(appName);
            List<CrashInfo> crashinfo = crashes.getCrashes();
            for (CrashInfo crash : crashinfo) {
                if (!crash.getInstance().isEmpty()) {
                    return "crashed";
                }
            }

            String res = "unknown";
            AppState status = app.getState();

            if (status.equals(AppState.STARTED)) {
                boolean running = false;
                /*
                 * Unfortunately, STARTED does not mean running. 
                 * We have to do additional checks.
                 * TODO: refactor this in an utility class.
                 */
                HttpURLConnection huc = null;
                try {
                    String urlstr = getApplicationUriCF(email, password, appName) + "/c4s/ems/application";
                    URL url = new URL(urlstr);
                    huc = (HttpURLConnection) url.openConnection();
                    huc.setRequestMethod("GET");

                    int code = huc.getResponseCode();
                    running = (code == HttpURLConnection.HTTP_OK);
                } catch (MalformedURLException e) {
                    throw new Cloud4SoaException(e.getMessage());
                } catch (IOException e) {
                    /* not started: does nothing */
                } finally {
                    if (huc != null) {
                        huc.disconnect();
                    }
                }

                res = (running)? "running" : "started";

            } else if (status.equals(AppState.STOPPED)) {
                res = "stopped";
            }

            return res;
        } catch (CloudFoundryException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new Cloud4SoaException("The application " + appName + " cannot be found");
            }
            throw new Cloud4SoaException(e.getMessage());
        } finally {
            if (client != null) finalize(client);
        }
    }

    // These 2 methods are in the remote adapter now.
    /*
    public static String operateCloudFoundry(String Operation, String email, String password, String appName) throws Cloud4SoaException {

        String ret = "";
        CloudFoundryClient client = null;
        try {
            client = init(email, password);
            if (Operation.equalsIgnoreCase("START")) {
                client.startApplication(appName);
            }
            if (Operation.equalsIgnoreCase("STOP")) {
                client.stopApplication(appName);
            }
            
        } catch (Exception ex) {
            //Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
            throw new Cloud4SoaException(ex.getMessage());

        } finally {
            if (client != null) finalize(client);
        }

        return ret;
    }
    
    public static DatabaseObject operateDatabaseCloudFoundry(String Operation, String email, String password, String appName,
            String account, String dbtype, String apiversion, String dbname, String dbuser, String dbpassport) throws Cloud4SoaException {

        DatabaseObject dbobj = new DatabaseObject();
        CloudFoundryClient client = null;
        try {
            client = init(email, password);
            if (Operation.equalsIgnoreCase("DBCREATE")) {

                CloudService service = new CloudService();
                service.setTier("free");
                service.setName(dbname);
                //MYSQL DATABASE (implementation of other systems (Redis, Mongo, etc) necessary via "switch" statements
                service.setVersion("5.1");
                service.setVendor("mysql");
                service.setType("database");
                client.createService(service);
                //client.bindService(appName, dbName);
            }

            if (Operation.equalsIgnoreCase("DBLIST")) {
                List<CloudService> services = client.getServices();
            }

            if (Operation.equalsIgnoreCase("DBINFO")) {
                //Access only via Caldecott
            }

            if (Operation.equalsIgnoreCase("DBDELETE")) {
                client.deleteService(dbname);
            }

        } catch (Exception ex) {
            throw new Cloud4SoaException(ex.getMessage());

        } finally {
            if (client != null) finalize(client);
        }
        return dbobj;

    }
    */
    private static TunnelServer cftunnel = null;
    
    public static DatabaseCredentials openTunnelCF(String email, String password, String dbtype, String dbname) throws Cloud4SoaException {
        CloudFoundryClient client = null;
        System.out.println("openTunnelCF start->email:"+email+",  password,:"+password+"  dbtype,:"+dbtype+"  dbname:"+dbname);
        try {
            client = init(email, password);
            
            try {
                client.getService(dbname);
            } catch (CloudFoundryException e) {
                if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                    throw new Cloud4SoaException("The database " + dbname + " cannot be found.");
                } else {
                    throw new Cloud4SoaException(e.getMessage());
                }
            }
            
            CloudApplication serverApp = null;
            try {
                serverApp = client.getApplication(TunnelHelper.getTunnelAppName());
            } catch (CloudFoundryException e) {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    System.out.println("Deploying Caldecott Server app");
                    TunnelHelper.deployTunnelApp(client);
                    
                    Thread.sleep(10000);
                    
                    serverApp = client.getApplication(TunnelHelper.getTunnelAppName());
                } else {
                    throw new Cloud4SoaException(e.getMessage());
                }
            }
            if (!serverApp.getState().equals(CloudApplication.AppState.STARTED)) {
                System.out.println("Starting Caldecott server app: "+serverApp.getName());
                client.startApplication(serverApp.getName());
                System.out.println("Started");
            }
            
            TunnelHelper.bindServiceToTunnelApp(client, dbname);
            
            Thread.sleep(10000);
            
            InetSocketAddress local = new InetSocketAddress(AuxAdapter.TUNNEL_LOCAL_HOST, AuxAdapter.TUNNEL_LOCAL_PORT);
            String url = TunnelHelper.getTunnelUri(client);
            Map<String, String> info = TunnelHelper.getTunnelServiceInfo(client, dbname);
            String host = info.get("hostname");
            int port = Integer.valueOf(info.get("port"));
            String auth = TunnelHelper.getTunnelAuth(client);
            
            String svc_username = info.get("username");
            String svc_passwd = info.get("password");
            String svc_dbname = info.get("db") != null ? info.get("db") : info.get("name");
            String txt_dbname = info.get("db") != null ? "db" : "name";
            String svc_vhost = info.get("vhost");
            System.out.println("TunnelHelper.getTunnelServiceInfo ->"
                    + "svc_username:"+svc_username+",  svc_passwd,:"+svc_passwd+
                    "  svc_dbname,:"+svc_dbname+"  txt_dbname:"+txt_dbname+
                    "  svc_vhost,:"+svc_vhost+"  port:"+port +"auth"+auth);
           
            TunnelServer server = new TunnelServer(local, new HttpTunnelFactory(url, host, port, auth));
            System.out.println("TunnelServer initialiazed");
            server.start();
            System.out.println("Tunnel is running on " + AuxAdapter.TUNNEL_LOCAL_HOST + " port " + AuxAdapter.TUNNEL_LOCAL_PORT + " with auth=" + auth);
            if (svc_vhost != null) {
                System.out.println("Connect client with username=" + svc_username +" password=" + svc_passwd + " " + "vhost=" + svc_vhost);
            }
            else {
                System.out.println("Connect client with username=" + svc_username +" password=" + svc_passwd + " " + txt_dbname + "=" + svc_dbname);
            }
            AuxAdapter.cftunnel = server;
            return new DatabaseCredentials(svc_username, svc_passwd, svc_dbname);
        } catch (Cloud4SoaException ex) {
            throw new Cloud4SoaException(ex);
        } catch (InterruptedException ex) {
            throw new Cloud4SoaException(ex);
        } catch (NumberFormatException ex) {
            throw new Cloud4SoaException(ex);
        } finally {
            if (client != null) finalize(client);
        }
    }
    
    public static void closeTunnelCF() {
        if (cftunnel != null) {
            cftunnel.stop();
            System.out.println("Tunnel closed");
        }
    }

    public static CloudFoundryClient init(String email, String password) throws Cloud4SoaException {
        try {
            CloudFoundryClient client = new CloudFoundryClient(new CloudCredentials(email, password), new URL(CC_URL));
            client.login();
            return client;
        } catch (MalformedURLException e) {
            //ignore, hardcoded URL, can't be wrong.
            return null;
        } catch (ResourceAccessException e) {
            throw new Cloud4SoaException("Connection with the PaaS was refused. Please check that it's accessible");
        } catch (CloudFoundryException e) {
            if (e.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
                throw new Cloud4SoaException("Authentication error. Please check that the e-mail and password are correct.");
            }
            throw new Cloud4SoaException(e.getMessage());
        }
    }
    
    public static void finalize(CloudFoundryClient client) {
        client.logout();
    }

    private static String generateAppURL(String appName) {
        int ix1 = 2 + CC_URL.indexOf("//");
        int ix2 = CC_URL.indexOf('.');
        return CC_URL.substring(0, ix1) + appName + CC_URL.substring(ix2);
    }
}
