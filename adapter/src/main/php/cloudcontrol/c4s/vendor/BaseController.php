<?php

/*
*  Copyright 2012 cloudControl GmbH
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/

abstract class BaseController {

    // methods used for provisioning, up- and downgrades and deprovisioning
    abstract public function create($data);
    abstract public function update($id, $data);
    abstract public function delete($id);

    // health check for e.g. pingdom or newrelic
    // overwrite these two methods with something meaningful
    public static function get_hc_secret() {
        return false;
    }

    public function health_check() {
        return false;
    }
}