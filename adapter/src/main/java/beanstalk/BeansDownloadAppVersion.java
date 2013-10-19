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

/**
 *
 * @author Ledakis Giannis (SingularLogic)
 */






import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.internal.Constants;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;



public class BeansDownloadAppVersion {






private String downloadS3_File() throws IOException{
    String ret="";
    /*Writer writer = null;
    try {
    AWSCredentials myCredentials = new BasicAWSCredentials(String.valueOf(Constants.act), String.valueOf(Constants.sk));
    AmazonS3Client s3Client = new AmazonS3Client(myCredentials);
    S3Object object = s3Client.getObject(new GetObjectRequest("bucket", "file"));
    BufferedReader reader = new BufferedReader(new InputStreamReader(object.getObjectContent()));
    File file = new File("localFilename");
    writer = new OutputStreamWriter(new FileOutputStream(file));
    while (true) {
    String line = reader.readLine();
    if (line == null) {
    break;
    }
    writer.write(line + "\n");
    }
    writer.close();
    return ret;
    } catch (FileNotFoundException ex) {
    Logger.getLogger(BeansDownloadAppVersion.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
    try {
    writer.close();
    } catch (IOException ex) {
    Logger.getLogger(BeansDownloadAppVersion.class.getName()).log(Level.SEVERE, null, ex);
    }
    }*/
        return ret;
}


}
