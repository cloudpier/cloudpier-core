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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
    public static Date parseW3CDate(String dateString) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",
                Locale.US);
        int tzMinuteIndex = dateString.length()-3;
        if(dateString.charAt(tzMinuteIndex) == ':') //strip ':' from timezone since sdf can't handle it
        {
            dateString = dateString.substring(0, tzMinuteIndex) + dateString.substring(tzMinuteIndex+1);
        }
        
        Date d = sdf.parse(dateString);
        return d;
    }
    
    public static Date parseRssDate(String dateString) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z",
                Locale.US);
        Date d = sdf.parse(dateString);
        return d;
    }
    
    public static Date parseW3CDateWithFractionalSeconds(String dateString) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                Locale.US);
        
        Date d = sdf.parse(dateString);
        return d;
    }
    
    public static String toW3CDateString(Date d)
    {
        //SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy'T'");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",
                Locale.US);
        String dateString = sdf.format(d);
        
        //insert the : char into the timezone to make it truly W3C
        int tzMinuteIndex = dateString.length()-2;
        dateString = dateString.substring(0, tzMinuteIndex) + ":" + dateString.substring(tzMinuteIndex);
        return dateString;
    }
    
    public static Date parseW3CDateRobust(String dateString) throws ParseException
    {
        try{
            return parseW3CDate(dateString);
        }
        catch(ParseException e)
        {
            return parseW3CDateWithFractionalSeconds(dateString);
        }
    }
}
