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

package openshift;



import java.util.logging.Level;
import java.util.logging.Logger;
import com.openshift.client.*;
import com.openshift.internal.client.Cartridge;
import java.io.IOException;
import eu.cloud4soa.api.util.exception.adapter.Cloud4SoaException;
import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;


/**
 *
 * @author Ledakis Giannis (SingularLogic)
 */
public class Openshift_Aux {

    String id = "";
    String username = "";
    String password = "";
    String passPhrase = "";

    public Openshift_Aux() {
    }

    public Openshift_Aux(String openshift_id, String openshift_username, String openshift_password) {
        id = openshift_id;
        username = openshift_username;
        password = openshift_password;
    }

    public Openshift_Aux(String openshift_username, String openshift_password) {
        username = openshift_username;
        password = openshift_password;
    }
    
    

    


    ///////////uncomment for 2.3.0

    public void test(String applicationName) throws Exception {


        try {

            Cartridge crt = new Cartridge(username);

            final IOpenShiftConnection connection =
                    new OpenShiftConnectionFactory().getConnection(id, username, password);
            IUser user = connection.getUser();
            System.out.println("11");
            //  ISSHPublicKey sshKey = SSHKeyPair.create(passPhrase, privateKeyPath, publicKeyPath);
            System.out.println("22");
            IDomain domain = user.getDefaultDomain();
            System.out.println("33");
            IApplication application = domain.createApplication(applicationName, ICartridge.JBOSSAS_7);
            //IApplication application = user.createApplication(applicationName, ICartridge.JBOSSAS_7);
            System.out.println("44--Application Created");
            application.start();
            System.out.println("55--Application started");


            // IApplication application = user.getApplicationByName(applicationName);
            //ICartridge cartridge = application.getCartridge();
            // cartridge.getName();

            System.out.println(application.getApplicationUrl());
            System.out.println(application.getName());
            System.out.println("gituri" + application.getGitUrl());
            System.out.println("66--Application geturl");

        } catch (OpenShiftException ex) {
            Logger.getLogger(Openshift_Aux.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public String getApplicationUrl(String applicationName) throws OpenShiftException {
        String ret = "";

        final IOpenShiftConnection connection =
                new OpenShiftConnectionFactory().getConnection(id, username, password);
        IUser user = connection.getUser();
        IDomain domain = user.getDefaultDomain();
        IApplication application = domain.getApplicationByName(applicationName);
        System.out.println("OpenShift API getApplicationByName called-url");
        ret = application.getApplicationUrl();



        return ret;

    }//eom getApplicationUrl

    public String getApplicationName(String applicationName) throws OpenShiftException {
        String ret = "";

        final IOpenShiftConnection connection =
                new OpenShiftConnectionFactory().getConnection(id, username, password);
        IUser user = connection.getUser();
        IDomain domain = user.getDefaultDomain();;
        IApplication application = domain.getApplicationByName(applicationName);
        System.out.println("OpenShift API getApplicationByName called-name");
        ret = application.getName();


        return ret;

    }//eom getApplicationName

    //mallon prepei na parw to git kai na kanw push
    public String deploy(String applicationName) throws OpenShiftException {
        String ret = "";

        final IOpenShiftConnection connection =
                new OpenShiftConnectionFactory().getConnection(id, username, password);
        IUser user = connection.getUser();
        IDomain domain = user.getDefaultDomain();;
        IApplication application = domain.getApplicationByName(applicationName);
        System.out.println("OpenShift API getApplicationByName called-git");
        System.out.println(application.getGitUrl());
        ret = application.getGitUrl();
        //TODO///
        ///git push



        return ret;

    }//eom getApplicationName

    public String startApplication(String applicationName) throws OpenShiftException {
        String ret = "";

        final IOpenShiftConnection connection =
                new OpenShiftConnectionFactory().getConnection(id, username, password);
        IUser user = connection.getUser();
        IDomain domain = user.getDefaultDomain();;
        IApplication application = domain.getApplicationByName(applicationName);
        System.out.println("OpenShift API getApplicationByName called-start");
        application.start();
        System.out.println("OpenShift API start called " + applicationName);



        return ret;

    }//eom startApplication

    public String stopApplication(String applicationName) throws OpenShiftException {
        String ret = "";

        final IOpenShiftConnection connection =
                new OpenShiftConnectionFactory().getConnection(id, username, password);
        IUser user = connection.getUser();
        IDomain domain = user.getDefaultDomain();;
        IApplication application = domain.getApplicationByName(applicationName);
        System.out.println("OpenShift API getApplicationByName called-stop");
        application.stop();
        System.out.println("OpenShift API stop called " + applicationName);



        return ret;

    }//eom stopApplication

    public String deleteApplication(String applicationName) throws OpenShiftException {
        String ret = "";

        final IOpenShiftConnection connection =
                new OpenShiftConnectionFactory().getConnection(id, username, password);
        IUser user = connection.getUser();
        IDomain domain = user.getDefaultDomain();;
        IApplication application = domain.getApplicationByName(applicationName);
        System.out.println("OpenShift API getApplicationByName called-delete");
        application.destroy();
        System.out.println("OpenShift API destroy called " + applicationName);



        return ret;

    }//eom deleteApplication

    public IUser createUser(String username, String password) throws OpenShiftException {
        IUser user = null;

        final IOpenShiftConnection connection =
                new OpenShiftConnectionFactory().getConnection(id, username, password);
        user = connection.getUser();
        System.out.println("username:" + user.getAuthIV());


        return user;
    }

    public String createDomain(String domainName, String passPhrase, String privateKeyPath, String publicKeyPath) throws OpenShiftException {
        String ret = "";

        final IOpenShiftConnection connection =
                new OpenShiftConnectionFactory().getConnection(id, username, password);
        IUser user = connection.getUser();
        //ISSHPublicKey sshKey = SSHKeyPair.create(passPhrase, privateKeyPath, publicKeyPath);

        IDomain domain = user.createDomain(domainName);;
        System.out.println("OpenShift API createDomain called");

        return ret;

    }//eom createapplication

    
    
        ///Register SSH KEY from File
    public Boolean registerSSHKey( String privateKeyPath, String publicKeyPath, String keyName) throws OpenShiftException {
        Boolean ret = false;
        try{
        final IOpenShiftConnection connection =
                new OpenShiftConnectionFactory().getConnection(id, username, password);
        IUser user = connection.getUser();
        System.out.println("user:"+user.toString());
        ISSHPublicKey sshKey = SSHKeyPair.load(privateKeyPath, publicKeyPath);
        user.putSSHKey(keyName, sshKey);
        System.out.println("key added");
        ret=true;
        }catch(Exception ex){
            //if Exception occured while trying to read info from the log, send the log unedited
            ret=false;
            throw new OpenShiftException("Error while adding key. "+". Exception :"+ex.getMessage());
        }

        return ret;

    }
        ///Delete SSH KEY
    public Boolean deleteSSHKey( String keyName) throws OpenShiftException {
        Boolean ret = false;
        try{
        final IOpenShiftConnection connection =
                new OpenShiftConnectionFactory().getConnection(id, username, password);
        IUser user = connection.getUser();
        user.deleteKey(keyName);
        ret= true;
        }catch(Exception ex){
            //if Exception occured while trying to read info from the log, send the log unedited
            ret=false;
            throw new OpenShiftException("Error while deleting key. "+". Exception :"+ex.getMessage());
        }

        
        return ret;

    }
    
    
    
        ///SSH KEY pair from String
	public static SSHKeyPair create(String privateKey,String publicKey) throws IOException, OpenShiftException {
		File privateKeyFile = File.createTempFile(createRandomString(), null);
		writeTo(privateKey, privateKeyFile);

		File publicKeyFile = File.createTempFile(createRandomString(), null);
		writeTo(publicKey, publicKeyFile);
		
		return SSHKeyPair.load(privateKeyFile.getAbsolutePath(), publicKeyFile.getAbsolutePath());
	}    
	private static String createRandomString() {
		return String.valueOf(System.currentTimeMillis());
	}
        
        public static void writeTo(String data, File file) throws IOException {
		StringReader reader = null;
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			reader = new StringReader(data);
			for (int character = -1; (character = reader.read()) != -1;) {
				writer.write(character);
			}
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
			if (reader != null) {
				reader.close();
			}
		}
	}
        
        
	        
}

   

