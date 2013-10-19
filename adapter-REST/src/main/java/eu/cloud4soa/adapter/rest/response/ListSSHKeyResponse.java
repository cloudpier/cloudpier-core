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


package eu.cloud4soa.adapter.rest.response;

import java.io.Serializable;
import java.util.Arrays;

import eu.cloud4soa.adapter.rest.aop.Method;
import eu.cloud4soa.adapter.rest.aop.Path;
import eu.cloud4soa.adapter.rest.aop.Method.HttpMethod;
import eu.cloud4soa.adapter.rest.aop.Path.Component;
import eu.cloud4soa.adapter.rest.response.model.SshKey;

@Method(HttpMethod.GET)
@Path(component=Component.EMS,path="/key")
public class ListSSHKeyResponse extends Response<ListSSHKeyResponse> implements Serializable{
	private static final long serialVersionUID = 6571898591514926743L;

	SshKey[] sshKeys = new SshKey[0];

	public ListSSHKeyResponse(){
	}
	
	public SshKey[] getSshKeys() {
		return sshKeys;
	}

	public void setSshKeys(SshKey[] sshKeys) {
		this.sshKeys = sshKeys;
	}

	@Override
	public String toString() {
		return "ListSSHKeyResponse [sshKeys=" + Arrays.toString(sshKeys) + ", Response=" + super.toString() + "]";
	}
}
