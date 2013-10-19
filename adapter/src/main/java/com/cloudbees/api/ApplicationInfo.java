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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("ApplicationInfo")
public class ApplicationInfo {
    private String id;
    private String title;
    private String created;
    private String status;
    
    @XStreamImplicit(itemFieldName="url")
    private List<String> urls;
    
    Map<String, String> settings;

    public ApplicationInfo(String id, String title, Date created,
            String status, String[] urls) {
        super();
        this.id = id;
        this.title = title;
        this.created = DateHelper.toW3CDateString(created);
        this.status = status;
        this.urls = new ArrayList<String>();
        for(String url : urls)
            this.urls.add(url);
    }
    
    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public Date getCreated() {
        if(created == null)
            return null;
        try {
            return DateHelper.parseW3CDate(created);
        } catch (ParseException e) {
            return null;
        }
    }
    public String getStatus() {
        return status;
    }
    public String[] getUrls() {
        if(urls == null)
            urls = new ArrayList<String>();
        
        return urls.toArray(new String[0]); 
    }

    public Map<String, String> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, String> settings) {
        this.settings = settings;
    }
}
