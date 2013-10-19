<?php
require_once(__DIR__.'/silex.phar');
require_once(__DIR__.'/phpcclib.php');

use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;

class PreRequest{

    private $app;

    public function __construct(Silex\Application $app, $authenticationProvider){
        $this->app = $app;
        
        $this->init();
        $this->map();
    }
    
    public function init(){
        // load credentials
    }
    
    public function map(){
        $app = $this->app;
        $app->before(function (Request $request) use ($app){
            if(!$app->auth($request)){
                $app->abort(401, "Authorization required.");
            }
        });
    }
}

?>
