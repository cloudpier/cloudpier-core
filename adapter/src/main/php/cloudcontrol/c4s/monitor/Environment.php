<?php
/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of Environment
 *
 * @author dn
 */
class Environment {

    public static function collectSystemResources(){
        return $memory = array(
            'limit' => ini_get('memory_limit'),
            'usage' => memory_get_usage(),
            'allocated' => memory_get_usage(true),
            'peakUsage' => memory_get_peak_usage(true),
        );
    }
    
    private static function retrieveCredentials(){
        if(isset($_ENV) && isset($_ENV['CRED_FILE'])){            
            $string = file_get_contents($_ENV['CRED_FILE'], false);
            if ($string != false) {
                $creds = json_decode($string, true);
                return $config = array(
                    'MYSQL_HOSTNAME' => $creds['MYSQLS']['MYSQLS_HOSTNAME'],
                    'MYSQL_DATABASE' => $creds['MYSQLS']['MYSQLS_DATABASE'],
                    'MYSQL_USERNAME' => $creds['MYSQLS']['MYSQLS_USERNAME'],
                    'MYSQL_PASSWORD' => $creds['MYSQLS']['MYSQLS_PASSWORD']
                );
            }
        }else{
            return null;
        }
    }

    public static function testDatabase(){
        $credentials = Environment::retrieveCredentials();
        if(!is_null($credentials)){
            $resource = mysql_connect($credentials['MYSQL_HOSTNAME'], $credentials['MYSQL_USERNAME'], $credentials['MYSQL_PASSWORD']);
            
            if($resource===false){
                return $dbStatus = array(
                    'message' => "MySQL connection failed."
                );
                
            }else{
                $db = mysql_select_db($credentials['MYSQL_DATABASE']);
                mysql_close($resource);
                $message;
                if($db===false){
                    $message = "Database is unknown.";
                }else{
                    $message = "Connection ready.";
                }
                return $dbStatus = array(
                    'message' => $message
                );
            }
        }else{
            return $dbStatus = array(
                'message' => 'No database available.'
            );
        }
    }
}