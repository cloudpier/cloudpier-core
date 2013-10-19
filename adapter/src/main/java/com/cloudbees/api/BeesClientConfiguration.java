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

/**
 * @author Olivier Lamy
 */
public class BeesClientConfiguration {

    private String serverApiUrl;
    private String apiKey;
    private String secret;
    private String format;
    private String version;
    private String proxyHost;
    private int proxyPort;
    private String proxyUser;
    private String proxyPassword;

    public BeesClientConfiguration(String serverApiUrl, String apiKey, String secret, String format, String version) {
        this.serverApiUrl = serverApiUrl;
        this.apiKey = apiKey;
        this.secret = secret;
        this.format = format;
        this.version = version;
    }

    public String getServerApiUrl()
    {
        return serverApiUrl;
    }

    public void setServerApiUrl( String serverApiUrl )
    {
        this.serverApiUrl = serverApiUrl;
    }

    public String getProxyHost()
    {
        return proxyHost;
    }

    public void setProxyHost( String proxyHost )
    {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort()
    {
        return proxyPort;
    }

    public void setProxyPort( int proxyPort )
    {
        this.proxyPort = proxyPort;
    }

    public String getProxyUser()
    {
        return proxyUser;
    }

    public void setProxyUser( String proxyUser )
    {
        this.proxyUser = proxyUser;
    }

    public String getProxyPassword()
    {
        return proxyPassword;
    }

    public void setProxyPassword( String proxyPassword )
    {
        this.proxyPassword = proxyPassword;
    }

    public String getApiKey()
    {
        return apiKey;
    }

    public void setApiKey(String apiKey)
    {
        this.apiKey = apiKey;
    }

    public String getSecret()
    {
        return secret;
    }

    public void setSecret( String secret )
    {
        this.secret = secret;
    }

    public String getFormat()
    {
        return format;
    }

    public void setFormat( String format )
    {
        this.format = format;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
    }

}
