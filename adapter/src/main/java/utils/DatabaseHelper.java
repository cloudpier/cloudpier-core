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
package utils;

import eu.cloud4soa.api.util.exception.adapter.Cloud4SoaException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ledakis Giannis (SingularLogic)
 */
public class DatabaseHelper {
    private int BUFFER = 10485760;


public String storeDB(String host, String port, String dbUser,String dbPass, String dbName, String local_file) throws IOException, InterruptedException, Cloud4SoaException{
        String msg="No Backup";
     //   try {
            String executeCmd = "";
            executeCmd = "mysqldump -h "+host+" -P " + port + " -u " + dbUser + " --password=" + dbPass + " " + dbName + " -r "+local_file;
            System.out.println("Commmand to execute: "+executeCmd);
            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();
            if (processComplete == 0) {
                System.out.println("Backup taken successfully");
                msg="Backup taken successfully";
            } else {
                System.out.println("Could not take mysql backup");
                msg="Could not take mysql backup";
                throw  new Cloud4SoaException("Could not take mysql backup: "+executeCmd);

            }

        return msg;

}

public String restoredb(String host, String port, String dbUser,String dbPass, String dbName, String local_file) throws IOException, InterruptedException, Cloud4SoaException{
    String msg="Nothing done in restoredb!";

            /*
             * mysql MUST be in PATH
             */
            String[] executeCmd = new String[]{"SHELL", "PARAM", "mysql -h "+host+" -P " + port + " -u " + dbUser + " -p" + dbPass + " " + dbName + " < "+local_file};
            String osName = System.getProperty("os.name");
            
            if (osName.startsWith("Windows")) {
            	executeCmd[0] = "cmd.exe";
            	executeCmd[1] = "/C";
            }
            else {
            	/* assume *nix execution */
            	executeCmd[0] = "/bin/sh";
            	executeCmd[1] = "-c";            	
            }
            System.out.println("command >"+executeCmd);
            for(int i=0;i<executeCmd.length;i++)
                System.out.print(executeCmd[i]);
            
            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            
            int processComplete = runtimeProcess.waitFor();
            if (processComplete == 0) {
                System.out.println("success");
                msg="success";
            } else {
                System.out.println("restore failure");
                msg="restore failure";
                throw  new Cloud4SoaException("restore failed for "+executeCmd);
            }

    return msg;

}

public String restoredbForBeanstalk(String host, String port, String dbUser,String dbPass, String dbName, String local_file) throws IOException, InterruptedException, Cloud4SoaException{
    String msg="Nothing done in restoredb!";


            ///cmd:: mysqldump acme | mysql --host=hostname --user=username --password acme
            String executeCmd = "mysqldump "+dbName+" | mysql -h"+host+" -P " + port + " -u " + dbUser + " -p" + dbPass + " " + dbName + " -r "+local_file;
            System.out.println("run:--> "+executeCmd);
            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();
            if (processComplete == 0) {
                System.out.println("success");
                msg="success";
            } else {
                System.out.println("restore failure");
                msg="restore failure";
                throw  new Cloud4SoaException("restore failed for "+executeCmd);

            }

    return msg;

}


}
