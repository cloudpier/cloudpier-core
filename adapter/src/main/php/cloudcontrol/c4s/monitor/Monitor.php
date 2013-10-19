<?php
/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$includePath = get_include_path ();
set_include_path ($includePath.":".__DIR__.'/../vendor/');

require_once(__DIR__.'/../vendor/silex.phar');
require_once(__DIR__.'/../vendor/Controller.php');
require_once(__DIR__.'/Environment.php');
require_once(__DIR__.'/../vendor/AuthenticationProvider.php');

use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;

/**
 * Description of Monitor
 *
 * @author dn
 */
class Monitor{
    
    private $app;
    private $authenticationProvider;

    public function __construct(){
        $this->app = new Silex\Application();
        $this->customizeErrorPage();
        
        $this->authenticationProvider = new AuthenticationProvider();
    }

    public function getApp(){
        return $this->app;
    }

    public function getAuthenticationProvider(){
        return $this->authenticationProvider;
    }

    public function customizeErrorPage(){
        $app = $this->getApp();
        $app->error(function (Exception $e) use ($app) {
            $code = $e->getStatusCode();
            return new Response(
                json_encode(array('message' => $e->getMessage())),
                $code,
                array('Content-Type' => 'application/json')
            );
        });
    }

    private function makeUpMapping(){
        $app = $this->getApp();
        $authenticationProvider = $this->getAuthenticationProvider();
        $authenticationProvider->readCredentials();

        $app->before(function(Request $request) use ($app,$authenticationProvider){
            if($request->getPathInfo() == "/"){
                return true;
            }
            if(!$authenticationProvider->requestIsValid($request)){
                $app->abort(401, "Unauthorized.");
            }
        });
        $app->get("/",function (Request $request) use ($app){
            return json_encode(array(
                'module' => array(
                        'moduleName' => 'Monitor',
                        'description' => 'cloud4soa monitor',
                        'version' => '1.0'
                        )
                    )
                )
            ;
        });
        $app->get("detail",function (Request $request) use ($app){
            return json_encode(Environment::testDatabase());
        });

        $app->after(function (Request $request) use ($app){
        });
    }

    public function run(){
        $this->makeUpMapping();
        $this->app->run();
    }
}