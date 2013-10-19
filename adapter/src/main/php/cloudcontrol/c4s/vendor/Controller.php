<?php
require_once(__DIR__.'/BaseController.php');

class Controller extends BaseController {

    public function create($data) {
        // your provisioning code here
        // see the examples below for what to expect and return
    }

    public function update($id, $data) {
        // your code for plan changes here
    }

    public function delete($id) {
        // your code for deprovisioning calls here
    }

    public static function get_hc_secret() {
        // return the secret used for /health-check?s=S3CR37
    }

    public function health_check() {
        // implement your health_check code here
    }
}