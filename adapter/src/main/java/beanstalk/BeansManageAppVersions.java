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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ledakis Giannis (SingularLogic)
 */
public class BeansManageAppVersions {

    public String storeVersion(String application, String appversion, String enviroment) {
        String ret = "";





        try {

            // Create one directory
            String tempdir = System.getProperty("java.io.tmpdir");
            System.out.println("TEMP DIR: " + tempdir);
            // Create one directory
             File verfolder=new File(tempdir+"/cloud4soaversions");
             if(verfolder.exists() == false){
             Boolean success=verfolder.mkdir();
             if (success) {
             System.out.println("Directory: "
             + verfolder.getAbsolutePath() + " created");
             }
             }



            File version_file = new File(verfolder.getPath()+"/bst_versions" + enviroment + ".txt");

            if (version_file.exists() == false) {
                version_file.createNewFile();
                System.out.println("creating bst_versions" + enviroment + ".txt file!");
            }

            //////////////WRITING TO FILE
            String lineToAdd = "app:-" + application + "-version:-" + appversion;


            FileWriter fwriter = new FileWriter(version_file, true);
            fwriter.write(lineToAdd + System.getProperty("line.separator"));
            fwriter.flush();
            fwriter.close();

        } catch (IOException ex) {
            System.out.println("There was a problem creating/writing to the beantstalk_versions file");
            ex.printStackTrace();
        }

        System.out.println("Stored version: " + appversion + " for application:" + application);
        return ret;
    }//eom storeVersion
    

    public String findLatestVersion(String application, String enviroment) {
        String ret = "";


        try {

            // Create one directory
            String tempdir = System.getProperty("java.io.tmpdir");
            System.out.println("TEMP DIR: " + tempdir);
            // Create one directory
            File verfolder=new File(tempdir+"/cloud4soaversions");
            if (verfolder.exists()==false){
                System.out.println("folder cloud4soaversions cant be found.");
            }

            FileInputStream version_file = new FileInputStream(verfolder.getPath()+"/bst_versions" + enviroment + ".txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(version_file));

            String strLine = null, tmp;

            while ((tmp = br.readLine()) != null) {
                strLine = tmp;
            }

            String lastline = strLine;
            System.out.println(lastline);
            String[] tmp_string = lastline.split("-version:-");
            ret = tmp_string[1];
            //     System.out.println("version="+ret);
            version_file.close();

        } catch (IOException ex) {
            System.out.println("There was a problem finding latest version");
            ex.printStackTrace();
        }


        return ret;
    }//eom




}
