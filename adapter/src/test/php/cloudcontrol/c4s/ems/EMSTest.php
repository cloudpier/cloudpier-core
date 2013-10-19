<?php
/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of EMSTest
 *
 * @author dn
 */
$includePath = get_include_path();
set_include_path($includePath.":".__DIR__.'/../../../../../main/php/cloudcontrol/c4s/vendor/');
//$includePath = get_include_path();
//set_include_path(get_include_path().":/usr/share/php");
//$includePath = get_include_path();
//set_include_path(get_include_path().":/usr/share/php/PEAR");
//$includePath = get_include_path();
//echo $includePath;

require_once(__DIR__.'/../../../../../main/php/cloudcontrol/c4s/vendor/silex.phar');
require_once(__DIR__.'/../../../../../main/php/cloudcontrol/c4s/ems/EMS.php');

use Silex\WebTestCase;

class EMSTest extends WebTestCase{

    public function createApplication(){
        $ems = new EMS();
        $ems->run();

        $app = $ems->getApp();
        $app['debug'] = true;
        unset($app['exception_handler']);

        return $app;
    }

    public function testIndexPage(){
        $client = $this->createClient();
        $crawler = $client->request('GET', '/');
        $this->assertTrue($client->getResponse()->isOk());
        $crawler = $client->request('POST', '/');
        $this->assertEquals(405,$client->getResponse()->getStatusCode());
        $crawler = $client->request('PUT', '/');
        $this->assertEquals(405,$client->getResponse()->getStatusCode());
        $crawler = $client->request('DELETE', '/');
        $this->assertEquals(405,$client->getResponse()->getStatusCode());
    }

    public function testApplicationPage_401(){
        $client = $this->createClient();
        $crawler = $client->request('GET', '/application/c4s');
        $this->assertEquals(401,$client->getResponse()->getStatusCode());
        $crawler = $client->request('POST', '/application/c4s');
        $this->assertEquals(401,$client->getResponse()->getStatusCode());
        $crawler = $client->request('PUT', '/application/c4s');
        $this->assertEquals(401,$client->getResponse()->getStatusCode());
        $crawler = $client->request('DELETE', '/application/c4s');
        $this->assertEquals(401,$client->getResponse()->getStatusCode());
    }
}
?>