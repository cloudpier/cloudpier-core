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


/**
 * generated by http://RDFReactor.semweb4j.org ($Id: CodeGenerator.java 1765 2010-02-11 09:51:13Z max.at.xam.de $) on 9/16/11 12:57 PM
 */
package eu.cloud4soa.api.datamodel.semantic.foaf;

import com.viceversatech.rdfbeans.annotations.RDF;
import com.viceversatech.rdfbeans.annotations.RDFBean;
import com.viceversatech.rdfbeans.annotations.RDFSubject;
import eu.cloud4soa.api.datamodel.semantic.Thing;
/**
 * This class manages access to these properties:
 * <ul>
 *   <li> AIMchatID </li>
 *   <li> Birthday </li>
 *   <li> Gender </li>
 *   <li> Holdsaccount </li>
 *   <li> Homepage </li>
 *   <li> ICQchatID </li>
 *   <li> JabberID </li>
 *   <li> MSNchatID </li>
 *   <li> Made </li>
 *   <li> Openid </li>
 *   <li> Personalmailbox </li>
 *   <li> Sha1sumofapersonalmailboxURIname </li>
 *   <li> Tipjar </li>
 *   <li> Weblog </li>
 *   <li> YahoochatID </li>
 * </ul>
 *
 * This class was generated by <a href="http://RDFReactor.semweb4j.org">RDFReactor</a> on 9/16/11 12:57 PM
 * Modified version 0.1
 */
@RDFBean("http://xmlns.com/foaf/0.1/Agent")
public class FoafAgent extends Thing {

	private java.lang.String aIMchatID;
	private java.util.Date birthday;
//	private {Male, Female} gender;
	private OnlineAccount holdsaccount;
	private Document homepage;
	private java.lang.String iCQchatID;
	private java.lang.String jabberID;
	private java.lang.String mSNchatID;
	private Thing made;
	private Document openid;
//	private Thing personalmailbox;
	private String personalmailbox;        
	private java.lang.String sha1sumofapersonalmailboxURIname;
	private Document tipjar;
	private Document weblog;
	private java.lang.String yahoochatID;





	@Override
	@RDFSubject(prefix="http://xmlns.com/foaf/0.1/Agent/")
	public String getUriId() {
		return super.getUriId();
	}
	


    @RDF("http://xmlns.com/foaf/0.1/aimChatID")
	public java.lang.String getAIMchatID() {
		return aIMchatID;
	}
	
	public void setAIMchatID( java.lang.String aIMchatID ) {
		this.aIMchatID = aIMchatID;
	}
    @RDF("http://xmlns.com/foaf/0.1/birthday")
	public java.util.Date getBirthday() {
		return birthday;
	}
	
	public void setBirthday( java.util.Date birthday ) {
		this.birthday = birthday;
	}
//    @RDF("http://xmlns.com/foaf/0.1/gender")
//	public A_1 getGender() {
//		return gender;
//	}
//	
//	public void setGender( A_1 gender ) {
//		this.gender = gender;
//	}
    @RDF("http://xmlns.com/foaf/0.1/holdsAccount")
	public OnlineAccount getHoldsaccount() {
		return holdsaccount;
	}
	
	public void setHoldsaccount( OnlineAccount holdsaccount ) {
		this.holdsaccount = holdsaccount;
	}
    @RDF("http://xmlns.com/foaf/0.1/homepage")
	public Document getHomepage() {
		return homepage;
	}
	
	public void setHomepage( Document homepage ) {
		this.homepage = homepage;
	}
    @RDF("http://xmlns.com/foaf/0.1/icqChatID")
	public java.lang.String getICQchatID() {
		return iCQchatID;
	}
	
	public void setICQchatID( java.lang.String iCQchatID ) {
		this.iCQchatID = iCQchatID;
	}
    @RDF("http://xmlns.com/foaf/0.1/jabberID")
	public java.lang.String getJabberID() {
		return jabberID;
	}
	
	public void setJabberID( java.lang.String jabberID ) {
		this.jabberID = jabberID;
	}
    @RDF("http://xmlns.com/foaf/0.1/msnChatID")
	public java.lang.String getMSNchatID() {
		return mSNchatID;
	}
	
	public void setMSNchatID( java.lang.String mSNchatID ) {
		this.mSNchatID = mSNchatID;
	}
    @RDF("http://xmlns.com/foaf/0.1/made")
	public Thing getMade() {
		return made;
	}
	
	public void setMade( Thing made ) {
		this.made = made;
	}
    @RDF("http://xmlns.com/foaf/0.1/openid")
	public Document getOpenid() {
		return openid;
	}
	
	public void setOpenid( Document openid ) {
		this.openid = openid;
	}
    @RDF("http://xmlns.com/foaf/0.1/mbox")
//	public Thing getPersonalmailbox() {
        public String getPersonalmailbox() {
		return personalmailbox;
	}
	
//	public void setPersonalmailbox( Thing personalmailbox ) {
    public void setPersonalmailbox( String personalmailbox ) {
		this.personalmailbox = personalmailbox;
	}
    @RDF("http://xmlns.com/foaf/0.1/mbox_sha1sum")
	public java.lang.String getSha1sumofapersonalmailboxURIname() {
		return sha1sumofapersonalmailboxURIname;
	}
	
	public void setSha1sumofapersonalmailboxURIname( java.lang.String sha1sumofapersonalmailboxURIname ) {
		this.sha1sumofapersonalmailboxURIname = sha1sumofapersonalmailboxURIname;
	}
    @RDF("http://xmlns.com/foaf/0.1/tipjar")
	public Document getTipjar() {
		return tipjar;
	}
	
	public void setTipjar( Document tipjar ) {
		this.tipjar = tipjar;
	}
    @RDF("http://xmlns.com/foaf/0.1/weblog")
	public Document getWeblog() {
		return weblog;
	}
	
	public void setWeblog( Document weblog ) {
		this.weblog = weblog;
	}
    @RDF("http://xmlns.com/foaf/0.1/yahooChatID")
	public java.lang.String getYahoochatID() {
		return yahoochatID;
	}
	
	public void setYahoochatID( java.lang.String yahoochatID ) {
		this.yahoochatID = yahoochatID;
	}

}