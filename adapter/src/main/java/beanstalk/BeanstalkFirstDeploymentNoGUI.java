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

import eu.cloud4soa.api.util.exception.adapter.Cloud4SoaException;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.cloudwatch.model.InvalidParameterValueException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ProgressEvent;
import com.amazonaws.services.s3.model.ProgressListener;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import java.io.IOException;

/**
 *
 * @author Ledakis Giannis (SingularLogic)
 *  
 */
public class BeanstalkFirstDeploymentNoGUI {

    private AWSCredentials credentials;
    private TransferManager tx;
    private String bucketName;
    private JProgressBar pb;
    private JFrame frame;
    private Upload upload;
    private JButton button;
    private boolean uploaded_to_s3 = false;
    private boolean version_updated = false;
    private String war_name_on_s3;
    private String accessKeyId;
    private String secretAccessKey;
    private String appversion;
    private String appname;
    private String environment_name;
    private String host_name;



    public boolean deploy(String war, String AWSKeyId, String AWSSecretKey, String applicationname, String applicationversion,
            String environment,String bucket, String host) throws Cloud4SoaException {

        boolean ret=false;
      //  credentials = new PropertiesCredentials(BeanstalkDeploy.class.getResourceAsStream("AwsCredentials.properties"))
        BasicAWSCredentials basic_credentials = new BasicAWSCredentials(AWSKeyId, AWSSecretKey);

        // TransferManager manages a pool of threads, so we create a
        // single instance and share it throughout our application.
        tx = new TransferManager(basic_credentials);

        appname=applicationname;
        appversion=applicationversion;
        accessKeyId=AWSKeyId;
        secretAccessKey=AWSSecretKey;
        bucketName=bucket;
        environment_name=environment;
        host_name=host;
        war_name_on_s3=war;
        //STEP 1: UPLOAD

        //STEP 2: CREATE APP VERSION

        //STEP 3: DEPLOY


           actionPerformed(war_name_on_s3, accessKeyId, secretAccessKey, appname, appversion, environment_name, bucketName, host_name);

        return ret;
    }

    //version for cli
    public BeanstalkFirstDeploymentNoGUI(String[] args) throws Exception {

    }


    //version for lib. if true, jframe for choosing file and a loading bar will be shown
    public BeanstalkFirstDeploymentNoGUI(Boolean getJframe){

    }


  //  class ButtonListener implements ActionListener {

    public void actionPerformed(String war, String AWSKeyId, String AWSSecretKey, String applicationname, String applicationversion,
            String environment,String bucket, String host) throws Cloud4SoaException{


        ///////////////////////////
            File tmp=new File(war);
       war_name_on_s3=tmp.getName();
            upload_file_to_s3(bucket, war_name_on_s3, war, AWSKeyId, AWSSecretKey);
     

            uploaded_to_s3 = true;
            createAmazonS3Bucket();
            System.out.println("war_name_on_s3=" + war_name_on_s3);

            System.out.println("Starting......");


            // pause for a while
            Thread thisThread = Thread.currentThread();
            try {
                thisThread.sleep(15000);
            } catch (Throwable t) {
                throw new OutOfMemoryError("An Error has occured");
            }
            System.out.println("Ending......");


///////////////////////////starting CreateApplicationVersion
            if (uploaded_to_s3 == true) {
                System.out.println("starting CreateApplicationVersion");
                System.out.println("accessKeyId" + accessKeyId);
                System.out.println("secretAccessKey" + secretAccessKey);
                System.out.println("war_name_on_s3" + war_name_on_s3);
                System.out.println("bucketName" + bucketName);
                System.out.println("appname" + appname);
                System.out.println("appversion" + appversion);


                //CREATE APPLICATION - NO VERSION, NO WAR
                BeansCreateApplication bst_createapp = new BeansCreateApplication();
                try {
                    bst_createapp.creatapp(accessKeyId, secretAccessKey, appname,"cloud4soa-created-app" );
                } catch (Exception ex) {
                    Logger.getLogger(BeanstalkFirstDeploymentNoGUI.class.getName()).log(Level.SEVERE, null, ex);
                }



                //NOW, CREATE APPLICATION VERSION
                BeansCreateApplicationVersion bst_createversion = new BeansCreateApplicationVersion();

                try {
                    bst_createversion.creatappversion(accessKeyId, secretAccessKey, war_name_on_s3,
                            bucketName, appname, appversion);
                            version_updated=true;

                } catch (Exception ex) {
                    Logger.getLogger(BeanstalkFirstDeploymentNoGUI.class.getName()).log(Level.SEVERE, null, ex);

                }

            }
/////////////////////starting CreateEnvironment if environment not found

            

            //createenviroment if not given
            if(version_updated==true){
            BeansCreateEnvironment bst_createenvironment= new BeansCreateEnvironment();
                try {
                    bst_createenvironment.createupdateenvironment(accessKeyId, secretAccessKey, environment_name, appname, appversion, "descriptionUpdateby-app:"+appname+"-version:"+appversion);
                } catch ( InvalidParameterValueException ex) {
                   // Logger.getLogger(BeanstalkFirstDeploymentNoGUI.class.getName()).log(Level.SEVERE, null, ex);

            System.out.println("Enviroment already there!Starting BeansUpdateEnvironment");

            BeansUpdateEnvironment bst_updateenvironment= new BeansUpdateEnvironment();
                try {
                    bst_updateenvironment.updateenvironment(accessKeyId, secretAccessKey, environment_name, appname, appversion, "descriptionUpdateby-app:"+appname+"-version:"+appversion);
                } catch (Exception ex2) {
                    Logger.getLogger(BeanstalkDeploy.class.getName()).log(Level.SEVERE, null, ex2);
                }



                }



            }


        }//eom actionPerformed




	public void upload_file_to_s3(String bucketName, String keyName,String uploadFileName, String AWSKeyId, String AWSSecretKey) throws Cloud4SoaException {

            BasicAWSCredentials basic_credentials = new BasicAWSCredentials(AWSKeyId, AWSSecretKey);

            AmazonS3 s3client = new AmazonS3Client(basic_credentials);

        try {
            System.out.println("Uploading a new object to S3 from a file\n");
            File file = new File(uploadFileName);
            s3client.putObject(new PutObjectRequest(
            		                 bucketName, keyName, file));

         } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which " +
            		"means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());

            throw new Cloud4SoaException(ase.getMessage());

        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which " +
            		"means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
            throw new Cloud4SoaException(ace.getMessage());

        }
    }

    private void createAmazonS3Bucket() {
        try {
            if (tx.getAmazonS3Client().doesBucketExist(bucketName) == false) {


                //BUCKET CREATION //disable for update
                tx.getAmazonS3Client().createBucket(bucketName);


            }
        } catch (AmazonClientException ace) {
            JOptionPane.showMessageDialog(frame, "Unable to create a new Amazon S3 bucket: " + ace.getMessage(),
                    "Error Creating Bucket", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createContentPane() {
        JPanel panel = new JPanel();
        panel.add(button);
        panel.add(pb);

        JPanel borderPanel = new JPanel();
        borderPanel.setLayout(new BorderLayout());
        borderPanel.add(panel, BorderLayout.NORTH);
        borderPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return borderPanel;
    }
}
