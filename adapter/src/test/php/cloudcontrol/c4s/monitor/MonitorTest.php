<?php
/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$includePath = get_include_path();
set_include_path($includePath.":".__DIR__.'/../../../../../main/php/cloudcontrol/c4s/vendor/');

require_once(__DIR__.'/../../../../../main/php/cloudcontrol/c4s/vendor/silex.phar');
require_once(__DIR__.'/../../../../../main/php/cloudcontrol/c4s/monitor/Monitor.php');

use Silex\WebTestCase;
/**
 * Description of MonitorTest
 *
 * @author dn
 */
class MonitorTest extends WebTestCase{

    public function createApplication(){
        $ems = new Monitor();
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

    public function testDetailPage_401(){
        $client = $this->createClient();
        $crawler = $client->request('GET', '/detail');
        $this->assertEquals(401,$client->getResponse()->getStatusCode());
        $crawler = $client->request('POST', '/detail');
        $this->assertEquals(401,$client->getResponse()->getStatusCode());
        $crawler = $client->request('PUT', '/detail');
        $this->assertEquals(401,$client->getResponse()->getStatusCode());
        $crawler = $client->request('DELETE', '/detail');
        $this->assertEquals(401,$client->getResponse()->getStatusCode());
    }
}
?>