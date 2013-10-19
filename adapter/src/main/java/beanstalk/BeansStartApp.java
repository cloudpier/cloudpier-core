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

package beanstalk;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;


import java.io.File;
import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;


/**
 *
 * @author Ledakis Giannis (SingularLogic)
 */
public class BeansStartApp {




    private AWSCredentials credentials;
    private TransferManager tx;
    private String bucketName;
   // private static String local_filename_and_path;
    private boolean uploaded_to_s3 = false;
    private boolean version_updated = false;
    private String war_name_on_s3;
    private String accessKeyId;
    private String secretAccessKey;
    private String appversion;
    private String appname;
    private String environment_name;
    private String host_name;

    //STARTS THE LATEST VERSION BEFORE WE STOPPED 





    public void startApp( String AWSKeyId, String AWSSecretKey, String applicationname, String latest_applicationversion,
            String environment){



            System.out.println("starting BeansUpdateEnvironment");

            BeansUpdateEnvironment bst_updateenvironment= new BeansUpdateEnvironment();
                try {
                    bst_updateenvironment.updateenvironment(AWSKeyId, AWSSecretKey, environment, applicationname, latest_applicationversion, "descriptionUpdateby-app:"+applicationname+"-version:"+latest_applicationversion);
                } catch (Exception ex) {
                    Logger.getLogger(BeansStartApp.class.getName()).log(Level.SEVERE, null, ex);
                }


        }//eom startApp

}

	

   
