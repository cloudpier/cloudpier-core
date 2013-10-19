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


package beanstalk;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rds.AmazonRDSClient;
import com.amazonaws.services.rds.model.*;
import java.util.List;
import cloudadapter.DatabaseObject;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Vector;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeansDatabase {

    	private final Logger log = LoggerFactory.getLogger(getClass());

        

    public DatabaseObject createDatabaseAndReturnInfo(String AWSKeyId, String AWSSecretKey, String dbname, String dbType, String dbIdentifier, String dbClass, int dbSize, String dbUser, String dbPassword) {//throws Exception {
        DatabaseObject dbobj = new DatabaseObject();

        String endpoint_str = "";
        BasicAWSCredentials basic_credentials = new BasicAWSCredentials(AWSKeyId, AWSSecretKey);

        AmazonRDSClient rDSClient = new AmazonRDSClient(basic_credentials);

        //Minimum size per db type, as described in API Reference
        if (dbType != null && dbType.equalsIgnoreCase("MySQL") && dbSize < 5) {
            dbSize = 5;
        }

        if (dbType != null && (dbType.equalsIgnoreCase("oracle-se1") || dbType.equalsIgnoreCase("oracle-se") || dbType.equalsIgnoreCase("oracle-ee")) && dbSize < 10) {
            dbSize = 10;
        }

        CreateDBInstanceRequest create_dbinstance = new CreateDBInstanceRequest();
        create_dbinstance.setDBName(dbname);
        create_dbinstance.setEngine(dbType);
        create_dbinstance.setMasterUsername(dbUser);
        create_dbinstance.setMasterUserPassword(dbPassword);
        create_dbinstance.setDBInstanceIdentifier(dbIdentifier);
        create_dbinstance.setAllocatedStorage(dbSize);//min size =5GB for mysql,10gb for oracle
        create_dbinstance.setDBInstanceClass("db.m1.small");//db.m1.small//db.m1.large//db.m1.xlarge//db.m2.xlarge//db.m2.2xlarge//db.m2.4xlarge....
        String group = "c4sallowallipgroup";
        Vector sec_groups = new Vector();
        sec_groups.add(group);
        create_dbinstance.setDBSecurityGroups(sec_groups);
        DBInstance dbInstance_create = new DBInstance();
        System.out.println("will call createDBInstance");

        try {
            dbInstance_create = rDSClient.createDBInstance(create_dbinstance);
        } catch (AmazonClientException amazonClientException) {
            System.out.println("Error in Database Creation.");
        }
        System.out.println("called createDBInstance");



        String status = "";
        System.out.println("called createDBInstance");

        //wait 5 minutes for the first time
        int busyWaitingTime = 3000;//3sec
        //int busyWaitingTime=300000;
        //int busyWaitingTime=40000;  

        while (!status.equalsIgnoreCase("available")) {
            System.out.println("just got inside while");

            try {
                System.out.println("preparing for sleep");

                Thread.sleep(busyWaitingTime);
                System.out.println("woke up");

            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(BeansDatabase.class.getName()).log(Level.SEVERE, null, ex);

            }

            DescribeDBInstancesRequest describeDBInstancesRequest = new DescribeDBInstancesRequest();

            describeDBInstancesRequest.setDBInstanceIdentifier(dbname + "cloud4soaid");
            //describeDBInstancesRequest.setDBInstanceIdentifier("c4sdb2cloud4soaid");
            DBInstance dbInstance = new DBInstance();

            System.out.println("will call describeDBInstances");
            DescribeDBInstancesResult describeDBInstances = rDSClient.describeDBInstances(describeDBInstancesRequest);
            System.out.println("called describeDBInstances");

            List<DBInstance> dbInstances = describeDBInstances.getDBInstances();

            System.out.println("size--->" + dbInstances.size());
            System.out.println("status--->" + dbInstances.get(0).getDBInstanceStatus());
            System.out.println("dbname--->" + dbInstances.get(0).getDBName());
            dbobj.setDbhost(dbInstances.get(0).getDBInstanceStatus());


            if (dbInstances.get(0).getEndpoint() != null) {


                System.out.println("endpoint--->" + dbInstances.get(0).getEndpoint().getAddress());
                System.out.println("port--->" + dbInstances.get(0).getEndpoint().getPort());
                System.out.println("all--->" + dbInstances.get(0).toString());

                dbobj.setDbhost(dbInstances.get(0).getEndpoint().getAddress());
                dbobj.setDbname(dbInstances.get(0).getDBName());
                dbobj.setPort(dbInstances.get(0).getEndpoint().getPort());


                status = dbInstances.get(0).getDBInstanceStatus();



            }
       
                //after the first 5 minutes test every minute
                busyWaitingTime = 60000; 
        }
            System.out.println("just got outside while");
            System.out.println("just got outside while and endpoint is :"+dbobj.getDbhost()+ ", name is "+dbobj.getDbname());




        return dbobj;
    }        
        
        
    public String createDatabase(String AWSKeyId, String AWSSecretKey, String dbname, String dbType, String dbIdentifier, String dbClass, int dbSize, String dbUser, String dbPassword) {//throws Exception {

        String endpoint_str = "";
        BasicAWSCredentials basic_credentials = new BasicAWSCredentials(AWSKeyId, AWSSecretKey);

        AmazonRDSClient rDSClient = new AmazonRDSClient(basic_credentials);

        //Minimum size per db type, as described in API Reference
        if (dbType != null && dbType.equalsIgnoreCase("MySQL") && dbSize < 5) {
            dbSize = 5;
        }

        if (dbType != null && (dbType.equalsIgnoreCase("oracle-se1") || dbType.equalsIgnoreCase("oracle-se") || dbType.equalsIgnoreCase("oracle-ee")) && dbSize < 10) {
            dbSize = 10;
        }

        CreateDBInstanceRequest create_dbinstance = new CreateDBInstanceRequest();
        create_dbinstance.setDBName(dbname);
        create_dbinstance.setEngine(dbType);
        create_dbinstance.setMasterUsername(dbUser);
        create_dbinstance.setMasterUserPassword(dbPassword);
        create_dbinstance.setDBInstanceIdentifier(dbIdentifier);
        create_dbinstance.setAllocatedStorage(dbSize);//min size =5GB for mysql,10gb for oracle
        create_dbinstance.setDBInstanceClass("db.m1.small");//db.m1.small//db.m1.large//db.m1.xlarge//db.m2.xlarge//db.m2.2xlarge//db.m2.4xlarge....
       // create_dbinstance.setDBInstanceClass("db.m1.small");//db.m1.small//db.m1.large//db.m1.xlarge//db.m2.xlarge//db.m2.2xlarge//db.m2.4xlarge....
        String group = "c4sallowallipgroup";
        Vector sec_groups = new Vector();
        sec_groups.add(group);
        create_dbinstance.setDBSecurityGroups(sec_groups);
        DBInstance dbInstance = new DBInstance();

        try {
            dbInstance = rDSClient.createDBInstance(create_dbinstance);
        } catch (AmazonClientException amazonClientException) {
            log.debug("Error in Database Creation.");
        }

        Endpoint endpoint = new Endpoint();
        endpoint = dbInstance.getEndpoint();
        if (endpoint != null) {
            endpoint_str = endpoint.toString();
            log.debug("endpoint to string:" + endpoint_str);
            log.debug("endpoint get address:" + endpoint.getAddress());
        }
        return endpoint_str;
    }

    public DatabaseObject getDBInstanceInfo(String AWSKeyId, String AWSSecretKey, String dbname) {//throws Exception {
        BasicAWSCredentials basic_credentials = new BasicAWSCredentials(AWSKeyId, AWSSecretKey);
        AmazonRDSClient rDSClient = new AmazonRDSClient(basic_credentials);
        DescribeDBInstancesRequest describeDBInstancesRequest = new DescribeDBInstancesRequest();
        describeDBInstancesRequest.setDBInstanceIdentifier(dbname + "cloud4soaid");
        DBInstance dbInstance = new DBInstance();
        DescribeDBInstancesResult describeDBInstances = rDSClient.describeDBInstances(describeDBInstancesRequest);
        List<DBInstance> dbInstances = describeDBInstances.getDBInstances();

        log.debug("size--->" + dbInstances.size());
        log.debug("dbname--->" + dbInstances.get(0).getDBName());
        log.debug("endpoint--->" + dbInstances.get(0).getEndpoint().getAddress());
        log.debug("port--->" + dbInstances.get(0).getEndpoint().getPort());
        log.debug("all--->" + dbInstances.get(0).toString());
        DatabaseObject dbobj= new DatabaseObject();
        dbobj.setDbhost(dbInstances.get(0).getEndpoint().getAddress());
        dbobj.setDbname(dbInstances.get(0).getDBName());
        dbobj.setPort(dbInstances.get(0).getEndpoint().getPort());
        
        return dbobj;
        
    }

    public String getDBEndpoint(String AWSKeyId, String AWSSecretKey, String dbname) {//throws Exception {

        String endpoint_str = "";
        BasicAWSCredentials basic_credentials = new BasicAWSCredentials(AWSKeyId, AWSSecretKey);

        AmazonRDSClient rDSClient = new AmazonRDSClient(basic_credentials);

        ModifyDBInstanceRequest mod_db = new ModifyDBInstanceRequest(dbname + "cloud4soaid");
        DBInstance dbInstance = new DBInstance();
        ////CHECK THIS. in order to make correct ModifyDBInstanceRequest an attribute must be sent for modification.
        ///5 (GB) is the minimum
        mod_db.setAllocatedStorage(6);

        dbInstance = rDSClient.modifyDBInstance(mod_db);
        Endpoint endpoint = new Endpoint();
        endpoint = dbInstance.getEndpoint();
        if (endpoint != null) {
            endpoint_str = endpoint.getAddress();
            log.debug("endpoint to string:" + endpoint_str);/////{Address: cloud4soadbid.coaodqyxxykq.us-east-1.rds.amazonaws.com, Port: 3306, }
            log.debug("endpoint get address:" + endpoint.getAddress());/////cloud4soadbid.coaodqyxxykq.us-east-1.rds.amazonaws.com
        }


        return endpoint_str;
    }

    public boolean deleteDatabase(String AWSKeyId, String AWSSecretKey, String dbIdentifier) {//throws Exception {

        boolean ret = false;
        //  credentials = new PropertiesCredentials(BeanstalkDeployNoGUI.class.getResourceAsStream("AwsCredentials.properties"))
        BasicAWSCredentials basic_credentials = new BasicAWSCredentials(AWSKeyId, AWSSecretKey);

        //  SimpleDBUtils simpledb= new SimpleDBUtils();
        AmazonRDSClient rDSClient = new AmazonRDSClient(basic_credentials);
        // RestoreDBInstanceFromDBSnapshotRequest req= new RestoreDBInstanceFromDBSnapshotRequest();
        //  req.setDBName("dbname");
        //  req.setPort(3306);

        DescribeDBInstancesRequest ddbi = new DescribeDBInstancesRequest();

        DBInstance dbins = new DBInstance();
        dbins.getEndpoint();
        DeleteDBInstanceRequest delRequest = new DeleteDBInstanceRequest(dbIdentifier);
        // rDSClient.restoreDBInstanceFromDBSnapshot(req);
        delRequest.setSkipFinalSnapshot(true);
        rDSClient.deleteDBInstance(delRequest);

        return ret;
    }

    public boolean allowIPConnectionWithDB(String AWSKeyId, String AWSSecretKey, String dbIdentifier) {//throws Exception {

        boolean ret = false;
        String security_group = "Cloud4SoaSecGroup";
        BasicAWSCredentials basic_credentials = new BasicAWSCredentials(AWSKeyId, AWSSecretKey);

        AmazonRDSClient rDSClient = new AmazonRDSClient(basic_credentials);
        //1st step-->add group cloud4soa if not exist

        CreateDBSecurityGroupRequest create_secGroupRequest = new CreateDBSecurityGroupRequest(security_group, "GroupGeneratedByCloud4SoaAdapter");
        DBSecurityGroup securityGroup = new DBSecurityGroup();


        try {
            securityGroup = rDSClient.createDBSecurityGroup(create_secGroupRequest);
        } catch (AmazonClientException amazonClientException) {
            System.out.print("Error when trying to add Security Group.Security Group might exist already!");
        }

        //2nd step--> add IP to list of specific Security Group

        AuthorizeDBSecurityGroupIngressRequest ip2SecGroup = new AuthorizeDBSecurityGroupIngressRequest(security_group);
        //allow specific ip
        //ip2SecGroup.setCIDRIP("91.132.244.150/5");
        //allow everyone
        ip2SecGroup.setCIDRIP("0.0.0.0/0");
        try {
            rDSClient.authorizeDBSecurityGroupIngress(ip2SecGroup);
        } catch (AmazonClientException amazonClientException) {
            System.out.print("Error when trying to add the specific IP address to the security group.IP might be already entered!");

        }


        return ret;
    }
}