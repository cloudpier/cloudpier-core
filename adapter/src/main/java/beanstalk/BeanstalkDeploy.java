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
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.elasticbeanstalk.model.UpdateEnvironmentRequest;
import com.amazonaws.services.s3.model.ProgressEvent;
import com.amazonaws.services.s3.model.ProgressListener;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

public class BeanstalkDeploy {

    private AWSCredentials credentials;
    private TransferManager tx;
    private String bucketName;
   // private static String local_filename_and_path;
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

 //   public static void main(String[] args) throws Exception {
       // public void main() throws Exception {
        //credentials = new PropertiesCredentials(BeanstalkDeploy.class.getResourceAsStream("AwsCredentials.properties"));

        //TODO
        //take credentials form args not from file

        // TransferManager manages a pool of threads, so we create a
        // single instance and share it throughout our application.
      //  tx = new TransferManager(credentials);

        //TODO
        //take bucketName from args, if it was given, else create the following:
        //bucketName = "s3-cloud4soa-autobucket-" + credentials.getAWSAccessKeyId().toLowerCase();

    //    bucketName = "elasticbeanstalk-us-east-1-601887962018";
        //TODO
        //take war file path from args
        //local_filename_and_path ="../sdsdsds/sts.war";
      
  //      new BeanstalkDeploy(args);
   // }

    public boolean deploy(String war, String AWSKeyId, String AWSSecretKey, String applicationname, String applicationversion,
            String environment,String bucket, String host){//throws Exception {

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
        

        //STEP 1: UPLOAD

        //STEP 2: CREATE APP VERSION

        //STEP 3: DEPLOY



   //    if(war.equalsIgnoreCase("")){
    //    NoGUI ngui= new NoGUI();


    //    ngui.performDeploydeploy("/home/jled/NetBeansProjects/WebApplication1/dist/WebApplication1.war",accessKeyId , secretAccessKey, appname, appversion,
           // environment_name,bucketName, host_name);

     //   }
        
        //TODO
        //take war file path from args
        //local_filename_and_path ="../sdsdsds/sts.war";

       /// new BeanstalkDeploy(args);

        return ret;
    }

    //version for cli
    public BeanstalkDeploy(String[] args) throws Exception {

        //TODO: delete this and take them form args
        appversion=args[0];
        appname="SimpleWar";

        frame = new JFrame("Amazon S3 File Upload");
        button = new JButton("Choose File...");
        button.addActionListener(new ButtonListener());

        pb = new JProgressBar(0, 100);
        pb.setStringPainted(true);

        frame.setContentPane(createContentPane());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        


        accessKeyId = credentials.getAWSAccessKeyId();
        secretAccessKey = credentials.getAWSSecretKey();
    }


    //version for lib. if true, jframe for choosing file and a loading bar will be shown
    public BeanstalkDeploy(Boolean getJframe){

        if(getJframe==true){
        frame = new JFrame("Amazon S3 File Upload");
        button = new JButton("Choose File...");
        button.addActionListener(new ButtonListener());

        pb = new JProgressBar(0, 100);
        pb.setStringPainted(true);

        frame.setContentPane(createContentPane());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        }

    }


    class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {

            try {
            if (ae.equals(null)){
            System.out.println("RECEIVED null as parameter.Starting non-gui upload");

            }
            } catch (Exception e) {
                System.out.println("Exception null as parameter.Starting non-gui upload");
                e.printStackTrace();
            }


            if (!ae.equals(null)){
            JFileChooser fileChooser = new JFileChooser();
            int showOpenDialog = fileChooser.showOpenDialog(frame);
            if (showOpenDialog != JFileChooser.APPROVE_OPTION) {
                return;
            }


            //TODO if update it must not be called
            //if bucket does not exist create
            //createAmazonS3Bucket();

            //TODO. better take name from s3 not local
            war_name_on_s3 = fileChooser.getSelectedFile().getName();
            System.out.println("war_name_on_s3=" + war_name_on_s3);
            ProgressListener progressListener = new ProgressListener() {

                public void progressChanged(ProgressEvent progressEvent) {
                    if (upload == null) {
                        return;
                    }

                    pb.setValue((int) upload.getProgress().getPercentTransfered());

                    switch (progressEvent.getEventCode()) {
                        case ProgressEvent.COMPLETED_EVENT_CODE:
                            pb.setValue(10000);
                            break;
                        case ProgressEvent.FAILED_EVENT_CODE:
                            try {
                                AmazonClientException e = upload.waitForException();
                                JOptionPane.showMessageDialog(frame,
                                        "Unable to upload file to Amazon S3: " + e.getMessage(),
                                        "Error Uploading File", JOptionPane.ERROR_MESSAGE);
                            } catch (InterruptedException e) {
                            }
                            break;
                    }


                }
            };



            
            ///another example(no JFrame)
            /*
             *
            AWSCredentials myCredentials = new BasicAWSCredentials(...);
            TransferManager tx = new TransferManager(myCredentials);
            Upload myUpload = tx.upload(myBucket, myFile.getName(), myFile);

            while (myUpload.isDone() == false) {
            System.out.println("Transfer: " + myUpload.getDescription());
            System.out.println("  - State: " + myUpload.getState());
            System.out.println("  - Progress: " + myUpload.getProgress().getBytesTransfered());
            // Do work while we wait for our upload to complete...
            Thread.sleep(500);
            }

             *
             */


            File fileToUpload = fileChooser.getSelectedFile();
            PutObjectRequest request = new PutObjectRequest(
                    bucketName, fileToUpload.getName(), fileToUpload).withProgressListener(progressListener);
            //prepei na mpei me kapoio allon elegxo, me
            uploaded_to_s3 = true;
            upload = tx.upload(request);


            }//end of if not null


            //arkoudia wait


            System.out.println("uploaded+:"+uploaded_to_s3);
            System.out.println("Starting......");

            // pause for a while
            Thread thisThread = Thread.currentThread();
            try {
                thisThread.sleep(10);
            } catch (Throwable t) {
                throw new OutOfMemoryError("An Error has occured");
            }
            System.out.println("Ending......");

            //end arkoudias wait


            ////////////////////////////////////starting CreateApplicationVersion
            if (uploaded_to_s3 == true) {
            System.out.println("starting CreateApplicationVersion");
            System.out.println("accessKeyId" + accessKeyId);
            System.out.println("secretAccessKey" + secretAccessKey);
            System.out.println("war_name_on_s3" + war_name_on_s3);
            System.out.println("bucketName" + bucketName);
            System.out.println("appname" + appname);
            System.out.println("appversion" + appversion);


            BeansCreateApplicationVersion bst_createversion = new BeansCreateApplicationVersion();

            try {
            bst_createversion.creatappversion(accessKeyId, secretAccessKey, war_name_on_s3,
            bucketName, appname, appversion);
            version_updated=true;

            } catch (Exception ex) {
            Logger.getLogger(BeanstalkDeploy.class.getName()).log(Level.SEVERE, null, ex);

            }

            }
            ////////////////////////////////////starting UpdateEnvironment
            //TODO
            //take enviroment arg from args
            //createenviroment if not given
            if(version_updated==true){

            System.out.println("starting BeansUpdateEnvironment");

            BeansUpdateEnvironment bst_updateenvironment= new BeansUpdateEnvironment();
            try {
            bst_updateenvironment.updateenvironment(accessKeyId, secretAccessKey, environment_name, appname, appversion, "descriptionUpdateby-app:"+appname+"-version:"+appversion);
            } catch (Exception ex) {
            Logger.getLogger(BeanstalkDeploy.class.getName()).log(Level.SEVERE, null, ex);
            }


            //    UpdateEnvironmentRequest res1=new UpdateEnvironmentRequest();
            //  System.out.println("info:" +res1.getEnvironmentName()+"d"+res1.getEnvironmentId()+"s"+res1.getDescription());

            }


        }//eom actionPerformed
    }//end of CLASS ButtonListener


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
