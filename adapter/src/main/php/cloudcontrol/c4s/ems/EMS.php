<?php
/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$includePath = get_include_path ();
set_include_path ($includePath.":".__DIR__.'/../vendor/');

require_once(__DIR__.'/../vendor/silex.phar');
require_once(__DIR__.'/../vendor/Controller.php');
require_once(__DIR__.'/../vendor/phpcclib.php');
require_once(__DIR__.'/../vendor/AuthenticationProvider.php');

use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;

/**
 * Description of EMS
 *
 * @author dn
 */
class EMS{

    private $app;
    private $api;
    private $authenticationProvider;

    public function __construct(){
        $this->app = new Silex\Application();
        $this->customizeErrorPage();
        $this->api = new API();
        $this->app->api = $this->api;
        $this->authenticationProvider = new AuthenticationProvider($this->api);
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

    public function getApp(){
        return $this->app;
    }

    public function getAuthenticationProvider(){
        return $this->authenticationProvider;
    }

    private function makeUpMapping(){
        $app = $this->getApp();
        $authenticationProvider = $this->getAuthenticationProvider();
        $authenticationProvider->readCredentials();

        $app->before(function (Request $request) use ($app,$authenticationProvider){
            if($request->getPathInfo() == "/"){
                return true;
            }

            if(!$authenticationProvider->requestIsValid($request) || !$authenticationProvider->grantCCAccess()){
                $app->abort(401, "Unauthorized.");
            }
        });
        $app->get("/",function (Request $request) use ($app){
            return json_encode(array(
                'module' => array(
                        'moduleName' => 'EMS',
                        'description' => 'cloud4soa execution management module adapter',
                        'version' => '1.0'
                        )
                    )
                )
            ;
        });

        /*
         * application relating stuff goes here
         */
        $app->get('/application', function(Request $request) use ($app){
            try{
                $ccResponse = $app->api->application_getList();
                $applications = array();
                foreach ($ccResponse as $value) {
                    $application = array();
                    $application["applicationName"] = $value->name;
                    $application["language"] = $value->type->name;

                    array_push($applications, $application);
                }
                $c4sResponse = array();
                $c4sResponse["applications"] = $applications;

                return new Response(
                    json_encode($c4sResponse),
                    200,
                    array('Content-Type' => 'application/json')
                );
            }catch(CCException $cce){
                $app->abort($cce->getCode(), $cce->getMessage());
            }catch(Exception $e){
                $app->abort(503, 'Not Available.');
            }
        });
        
        $app->get('/application/{name}', function(Request $request, $name) use ($app){
            try{
                $ccResponse = $app->api->application_getDetails($name);
                $application = array(
                    "applicationName" => $ccResponse->name,
                    "created" => $ccResponse->date_created,
                    "modified" => $ccResponse->date_modified,
                    "repository" => $ccResponse->repository,
                    "language" => $ccResponse->type->name
                );

                $c4sResponse = array(
                    'application' => $application
                );

                error_log(json_encode($c4sResponse));

                return new Response(
                    json_encode($c4sResponse),
                    200,
                    array('Content-Type' => 'application/json')
                );
            }catch(CCException $cce){
                $app->abort($cce->getCode(), $cce->getMessage());
            }catch(Exception $e){
                $app->abort(503, 'Not Available.');
            }
        });

        $app->post('/application/{name}', function(Request $request, $name) use ($app){
            $content = $request->getContent();
            $content = json_decode($content);
            $type = $content->language;
            
            try{
                $ccResponse = $app->api->application_create($name,$type);

                $application = array(
                    "applicationName" => $ccResponse->name,
                    "created" => $ccResponse->date_created,
                    "modified" => $ccResponse->date_modified,
                    "repository" => $ccResponse->repository,
                    "language" => $ccResponse->type->name
                );

                $c4sResponse = array(
                    'application' => $application
                );

                return new Response(
                    json_encode($c4sResponse),
                    201,
                    array('Content-Type' => 'application/json')
                );
            }catch(CCException $cce){
                $app->abort($cce->getCode(), $cce->getMessage());
            }catch(Exception $e){
                $app->abort(503, 'Not Available.');
            }
        });

        /**
         * TODO
         */
        $app->put('/application/{name}', function(Request $request, $name) use ($app){
            return new Response(
                json_encode(array("message" => "Not Implemented")),
                501,
                array('Content-Type' => 'application/json')
            );
        });

        $app->delete('/application/{name}', function(Request $request, $name) use ($app) {
            try{
                $ccResponse = $app->api->application_delete($name);
                return new Response(
                    json_encode(array("message" => 'Application successfully deleted.')),
                    202,
                    array('Content-Type' => 'application/json')
                );
            }catch(CCException $cce){
                $app->abort($cce->getCode(), $cce->getMessage());
            }catch(Exception $e){
                $app->abort(503, 'Not Available.');
            }
        });

        /*
         * deployment relating stuff goes here
         */
        $app->get('/application/{name}/deployment', function(Request $request, $name) use ($app) {
            try{
                $ccResponse = $app->api->deployment_getList($name);

                $deployments = array();
                foreach ($ccResponse as $value) {
                    $deployment = array();
                    $deployment["deploymentName"] = $value->name;
                    $deployment["applicationName"] = $name;

                    array_push($deployments, $deployment);
                }
                $c4sResponse = array();
                $c4sResponse["deployments"] = $deployments;

                return new Response(
                    json_encode($c4sResponse),
                    200,
                    array('Content-Type' => 'application/json')
                );
            }catch(CCException $cce){
                $app->abort($cce->getCode(), $cce->getMessage());
            }catch(Exception $e){
                $app->abort(503, 'Not Available.');
            }
        });

        $app->get('/application/{name}/deployment/{deployment}', function(Request $request, $name, $deployment) use ($app) {
            try{
                $ccResponse = $app->api->deployment_getDetails($name,$deployment);
                $deployment = array(
                    "applicationName" => $name,
                    "deploymentName" => $deployment,
                    "subDomain" => $ccResponse->default_subdomain,
                    "state" => $ccResponse->state,
                    "created" => $ccResponse->date_created,
                    "modified" => $ccResponse->date_modified
                );

                $c4sResponse = array(
                    'deployment' => $deployment
                );

                return new Response(
                    json_encode($c4sResponse),
                    200,
                    array('Content-Type' => 'application/json')
                );
            }catch(CCException $cce){
                $app->abort($cce->getCode(), $cce->getMessage());
            }catch(Exception $e){
                $app->abort(503, 'Not Available.');
            }
        });

        $app->post('/application/{name}/deployment/{deployment}', function(Request $request, $name, $deployment) use ($app) {
            try{
                $ccResponse = $app->api->deployment_create($name,$deployment);

                $deployment = array(
                    "applicationName" => $name,
                    "deploymentName" => $deployment,
                    "subDomain" => $ccResponse->default_subdomain,
                    "state" => $ccResponse->state,
                    "created" => $ccResponse->date_created,
                    "modified" => $ccResponse->date_modified
                );

                $c4sResponse = array(
                    'deployment' => $deployment
                );

                return new Response(
                    json_encode($c4sResponse),
                    201,
                    array('Content-Type' => 'application/json')
                );
            }catch(CCException $cce){
                $app->abort($cce->getCode(), $cce->getMessage());
            }catch(Exception $e){
                $app->abort(503, 'Not Available.');
            }
        });

        /**
         * TODO
         */
        $app->put('/application/{name}/deployment/{deployment}', function(Request $request, $name, $deployment) use ($app) {
            return new Response(
                json_encode(array("message" => "Not Implemented")),
                501,
                array('Content-Type' => 'application/json')
            );
//            try{
//                $ccResponse = $app->api->deployment_update($name,$deployment, null);
//
//                error_log(json_encode($ccResponse));
//
//                $deployment = array(
//                    "applicationName" => $name,
//                    "deploymentName" => $deployment,
//                    "subDomain" => $ccResponse->default_subdomain,
//                    "state" => $ccResponse->state,
//                    "created" => $ccResponse->date_created,
//                    "modified" => $ccResponse->date_modified
//                );
//
//                $c4sResponse = array(
//                    'deployment' => $deployment
//                );
//
//                return new Response(
//                    json_encode($c4sResponse),
//                    201,
//                    array('Content-Type' => 'application/json')
//                );
//            }catch(CCException $cce){
//                $app->abort($cce->getCode(), $cce->getMessage());
//            }catch(Exception $e){
//                $app->abort(503, 'Not Available.');
//            }
        });

         $app->delete('/application/{name}/deployment/{deployment}', function(Request $request, $name, $deployment) use ($app) {
            try{
                $ccResponse = $app->api->deployment_delete($name,$deployment);

                return new Response(
                    json_encode(array('message' => "Deployment successfully deleted.")),
                    202,
                    array('Content-Type' => 'application/json')
                );
            }catch(CCException $cce){
                $app->abort($cce->getCode(), $cce->getMessage());
            }catch(Exception $e){
                $app->abort(503, 'Not Available.');
            }
        });

        /*
         * database relating stuff goes here
         */
        $app->get('/application/{name}/deployment/{deployment}/database', function(Request $request, $name, $deployment) use ($app) {

            try{
                $ccResponse = $app->api->deployment_getAddonDetails($name,$deployment, 'mysqls.free');

                $databases = array();
                $databaseMeta = array();
                foreach ($ccResponse as $addon) {
                    if($addon->addon_option->name == 'mysqls.free'){
                        $databaseMeta["databaseName"]   = $addon->settings->MYSQLS_DATABASE;
                        $databaseMeta["userName"]       = $addon->settings->MYSQLS_USERNAME;
                        $databaseMeta["password"]       = $addon->settings->MYSQLS_PASSWORD;
                        $databaseMeta["host"]           = $addon->settings->MYSQLS_HOSTNAME;
                        $databaseMeta["port"]           = $addon->settings->MYSQLS_PORT;
                        array_push($databases, $databaseMeta);
                        break;
                    }
                }

                $c4sResponse = array('databases' => $databases);
                
                return new Response(
                    json_encode($c4sResponse),
                    200,
                    array('Content-Type' => 'application/json')
                );
            }catch(CCException $cce){
                $app->abort($cce->getCode(), $cce->getMessage());
            }catch(Exception $e){
                $app->abort(503, 'Not Available.');
            }
        });

        $app->get('/application/{name}/deployment/{deployment}/database/{database}', function(Request $request, $name, $deployment) use ($app) {
            try{
                /**
                 * TODO make manageable
                 */
                $ccResponse = $app->api->deployment_getAddonDetails($name,$deployment, 'mysqls.free');

                $databaseMeta = array();
                foreach ($ccResponse as $addon) {
                    if($addon->addon_option->name == 'mysqls.free'){
                        $databaseMeta["databaseName"]   = $addon->settings->MYSQLS_DATABASE;
                        $databaseMeta["userName"]       = $addon->settings->MYSQLS_USERNAME;
                        $databaseMeta["password"]       = $addon->settings->MYSQLS_PASSWORD;
                        $databaseMeta["host"]           = $addon->settings->MYSQLS_HOSTNAME;
                        $databaseMeta["port"]           = $addon->settings->MYSQLS_PORT;
                        break;
                    }
                }

                $c4sResponse = array(
                    "database" => $databaseMeta
                );

                return new Response(
                    json_encode($c4sResponse),
                    200,
                    array('Content-Type' => 'application/json')
                );
            }catch(CCException $cce){
                $app->abort($cce->getCode(), $cce->getMessage());
            }catch(Exception $e){
                $app->abort(503, 'Not Available.');
            }
        });

        $app->post('/application/{name}/deployment/{deployment}/database/{database}', function(Request $request, $name, $deployment, $database) use ($app) {
            try{
                /**
                 * TODO make manageable
                 */
                $ccResponse = $app->api->deployment_addAddon($name,$deployment, 'mysqls.free');

                $databaseMeta = array();
                $databaseMeta["databaseName"]   = $ccResponse->settings->MYSQLS_DATABASE;
                $databaseMeta["userName"]       = $ccResponse->settings->MYSQLS_USERNAME;
                $databaseMeta["password"]       = $ccResponse->settings->MYSQLS_PASSWORD;
                $databaseMeta["host"]           = $ccResponse->settings->MYSQLS_HOSTNAME;
                $databaseMeta["port"]           = $ccResponse->settings->MYSQLS_PORT;

                $c4sResponse = array(
                    "database" => $databaseMeta
                );

                return new Response(
                    json_encode($c4sResponse),
                    201,
                    array('Content-Type' => 'application/json')
                );
            }catch(CCException $cce){
                $app->abort($cce->getCode(), $cce->getMessage());
            }catch(Exception $e){
                $app->abort(503, 'Not Available.');
            }
        });

        /**
         * TODO
         */
        $app->put('/application/{name}/deployment/{deployment}/database/{database}', function(Request $request, $name, $deployment, $database) use ($app) {
            return new Response(
                json_encode(array("message" => "Not Implemented")),
                501,
                array('Content-Type' => 'application/json')
            );
        });

        $app->delete('/application/{name}/deployment/{deployment}/database/{database}', function(Request $request, $name, $deployment, $database) use ($app) {
            try{
                /**
                 * TODO make manageable
                 */
                $ccResponse = $app->api->deployment_removeAddon($name, $deployment, 'mysqls.free');

                return new Response(
                    json_encode(array("message" => "Database successfully deleted.")),
                    202,
                    array('Content-Type' => 'application/json')
                );
            }catch(CCException $cce){
                $app->abort($cce->getCode(), $cce->getMessage());
            }catch(Exception $e){
                $app->abort(503, 'Not Available.');
            }
        });

        /*
         * operation relating stuff goes here
         */
        $app->post('/application/{name}/operation/{op}', function(Request $request, $name, $op) use ($app) {
            switch($op){
                case 'nop' :
                    return new Response(
                        json_encode(array("message" => "Request was ignored. Cause: No Operation Mode.")),
                        200,
                        array('Content-Type' => 'application/json')
                    );
                    break;
                case 'start':
                    try{
                        $ccResponse = $app->api->deployment_create($name,'default');
                        return new Response(
                            json_encode(array('message' => "Deployment successfully created.")),
                            201,
                            array('Content-Type' => 'application/json')
                        );
                    }catch(CCException $cce){
                        $app->abort($cce->getCode(), $cce->getMessage());
                    }catch(Exception $e){
                        $app->abort(503, 'Not Available.');
                    }
                case 'stop' :
                    try{
                        $ccResponse = $app->api->deployment_delete($name,'default');
                        return new Response(
                            json_encode(array('message' => "Deployment successfully deleted.")),
                            202,
                            array('Content-Type' => 'application/json')
                        );
                    }catch(CCException $cce){
                        $app->abort($cce->getCode(), $cce->getMessage());
                    }catch(Exception $e){
                        $app->abort(503, 'Not Available.');
                    }
                default:
                    return new Response(
                        json_encode(array("message" => "Not Implemented: ".$op.".")),
                        501,
                        array('Content-Type' => 'application/json')
                    );
            }
        });

        $app->after(function (Request $request) use ($app){
        });
    }
    public function run(){
        $this->makeUpMapping();
        $this->app->run();
    }
}