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

package cloudbees;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FilePermission;
import java.util.PropertyPermission;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;
import utils.UnZipper;
import utils.ZipHelper;
import utils.ZipTool;


/**
 *
 * @author Ledakis Giannis (SingularLogic)
 * @deprecated
 * This class is currently not being used. It's purpose is to create and add a CloudBees compatible xml file to the
 * war archive of an web application.
 */
public class CreateBeesXMLfile {

private static String path1="dist"+ File.separator;

    public static void createfile(String user, String appid, String war_name) {
    try {
    File makefile = new File("cloudbees-web2.xml");
    FileWriter fwrite = new FileWriter(makefile);
    String file_content="";
    file_content+="<cloudbees-web-app xmlns='http://www.cloudbees.com/xml/webapp/1'>"+
    "<appid>"+user+"/"+appid+"</appid>"+
    "<context-param>"+
    "<param-name>application.environment</param-name>"+
    "<param-value>prod</param-value>"+
    "</context-param>"+
    "</cloudbees-web-app>";
    fwrite.write(file_content);
    fwrite.flush();
    fwrite.close();


    ////////UNZIP WAR FILE


        try
        {
            System.out.println("About to unzip the file '" + war_name+".war" + "'");
            UnZipper.unzip("/home/jled/NetBeansProjects/CloudConnectorCLI/dist/"+war_name+".war");
            System.out.println("Successfully unzipped the file '" + war_name +".swar"+ "'");
        }
        catch (IOException e)
        {
            System.err.println("Problems unzipping the file '" + war_name +".war"+ "'");
            e.printStackTrace();
            System.exit(1);
        }










    ///creating file inside the appropiate folder, in order to inject into war
       CreateBeesXMLfile bxml= new CreateBeesXMLfile();
        try {
        bxml.copy_cloudbees_web_xml_file(war_name);
        System.out.println("cloudbees-web.xml file copied inside :"+war_name+"/WEB-INF/");

        } catch (Exception ex) {
        Logger.getLogger(CreateBeesXMLfile.class.getName()).log(Level.SEVERE, null, ex);
        }
    //directly inject into war file



    } catch (IOException e) {
    e.printStackTrace();
    }
    }

    //checking if cloudbees-web.xml exists inside the war 
    public boolean check_if_file_exists(String war_name) {
    boolean ret=false;
        File findFile = new File(path1+war_name
        + File.separator + "WEB-INF" + File.separator +
        "cloudbees-web.xml");


        
    ret=findFile.isFile();
        System.out.println("file found?"+ret);

    return ret;
  }
    //copy cloudbees-web.xml exists inside the war
    public boolean copy_cloudbees_web_xml_file(String war_name) throws Exception {
    //public void CopyFile(File in, File out) throws Exception {
    boolean ret=false;
         File in=new File("cloudbees-web2.xml");
         new File(path1+war_name+ File.separator + "WEB-INF").mkdirs();
         File out=new File(path1+war_name
        + File.separator + "WEB-INF" + File.separator +
        "cloudbees-web3.xml");

         FileInputStream fis = new FileInputStream(in);
         FileOutputStream fos = new FileOutputStream(out);
         byte[] buf = new byte[1024];
         int i = 0;
             while((i=fis.read(buf))!=-1) {
             fos.write(buf, 0, i);
         }
         fis.close();
         fos.close();
         File war= new File(path1+war_name+".war");

         File[] files_to_add = new File[1];
         files_to_add[0]=out;
       ZipTool.addFilesToExistingZip(war, files_to_add);

    return ret;
  }



     


}