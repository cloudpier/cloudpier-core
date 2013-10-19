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
 * Copyright 2010-2011, CloudBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cloudbees.api;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;

public class HttpClientHelper {
    public static HttpClient createClient(BeesClientConfiguration beesClientConfiguration)
    {
        HttpClient client = new HttpClient();
        String proxyHost = beesClientConfiguration.getProxyHost();
        if(proxyHost != null)
        {
            int proxyPort = beesClientConfiguration.getProxyPort();

            client.getHostConfiguration().setProxy(proxyHost,proxyPort);
            
            //if there are proxy credentials available, set those too
            Credentials proxyCredentials = null;
            String proxyUser = beesClientConfiguration.getProxyUser();
            String proxyPassword = beesClientConfiguration.getProxyPassword();
            if(proxyUser != null || proxyPassword != null)
                proxyCredentials = new UsernamePasswordCredentials(proxyUser, proxyPassword);
            if(proxyCredentials != null)
                client.getState().setProxyCredentials(AuthScope.ANY, proxyCredentials);
        }

        return client;
    }
}
