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


package openshift;




import java.util.logging.Level;
import java.util.logging.Logger;
import com.openshift.client.*;
/**
 *
 * @author Ledakis Giannis (SingularLogic)
 */
public class OpenshiftCredentials {
    private IUser user;
    private ISSHPublicKey sshKey;

    /**
     * @return the user
     */

    public IUser getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(IUser user) {
        this.user = user;
    }

    /**
     * @return the sshKey
     */
    public ISSHPublicKey getSshKey() {
        return sshKey;
    }

    /**
     * @param sshKey the sshKey to set
     */
    public void setSshKey(ISSHPublicKey sshKey) {
        this.sshKey = sshKey;
    }

}
