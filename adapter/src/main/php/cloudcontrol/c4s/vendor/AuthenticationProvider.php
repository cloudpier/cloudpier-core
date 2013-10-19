<?php
/*
 * Copyright 2012 cloudControl GmbH
 */

require_once(__DIR__.'/silex.phar');
require_once(__DIR__.'/Controller.php');
require_once(__DIR__.'/phpcclib.php');

use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;

class AuthenticationProvider{

    private $adapterCredentials;
    
    private $api;
    private $username;
    private $password;

    function __construct(API $api = NULL) {
        $this->api = $api;
    }

    public function readCredentials(){
        if(file_exists(__DIR__.'/../../_adapterCredentials.php')){
            include_once(__DIR__.'/../../_adapterCredentials.php');
        } else {
            throw new Exception("Credentials file not found!");
        }
        $this->adapterCredentials = $adapterCredentials;
    }

    public function grantCCAccess(){
        if(!isset($this->adapterCredentials['cloudControl.email']) || !isset($this->adapterCredentials['cloudControl.password'])) {
            return false;
        }

        if(isset($this->adapterCredentials['cloudControl.email']) && isset($this->adapterCredentials['cloudControl.password'])){
            try{
                $this->api->auth($this->adapterCredentials['cloudControl.email'], $this->adapterCredentials['cloudControl.password']);
                return true;
            }catch(Exception $e){
                return false;
            }
        }
        return false;
    }

    public function requestIsValid($request){
        if($request->headers->get("hash")==null || $request->headers->get("apiKey") ==null || $request->headers->get("apiKey") !=  $this->adapterCredentials['cloud4soa.apiKey']){
            return false;
        }

        $buffer = "";
        $content = json_decode($request->getContent());
        foreach ( ((array) $content) as $key => $value) {
            $buffer.=$value;
        }

        $buffer = $this->adapterCredentials['cloud4soa.apiKey'] . $buffer . $this->adapterCredentials['cloud4soa.secretKey'];
        $shaed = sha1($buffer);
        
        if($shaed == $request->headers->get("hash")){
            return true;
        }
        return false;
    }
}