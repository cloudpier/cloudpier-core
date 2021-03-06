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
 * generated by http://RDFReactor.semweb4j.org ($Id: CodeGenerator.java 1765 2010-02-11 09:51:13Z max.at.xam.de $) on 9/19/11 6:31 PM
 */
package eu.cloud4soa.api.datamodel.semantic;

import com.viceversatech.rdfbeans.annotations.RDFBean;
import com.viceversatech.rdfbeans.annotations.RDFSubject;
/**
 * This class manages access to these properties:
 * <ul>
 *   <li> Abstract </li>
 *   <li> AccessRights </li>
 *   <li> AllValuesFrom </li>
 *   <li> AlternativeTitle </li>
 *   <li> Alternativelabel </li>
 *   <li> Audience </li>
 *   <li> AudienceEducationLevel </li>
 *   <li> Bandwidth </li>
 *   <li> Broader </li>
 *   <li> Broadermatch </li>
 *   <li> Broadertransitive </li>
 *   <li> Busarchitecture </li>
 *   <li> Changenote </li>
 *   <li> Closematch </li>
 *   <li> Companyname </li>
 *   <li> ConformsTo </li>
 *   <li> Contributor </li>
 *   <li> Coresnumber </li>
 *   <li> Coverage </li>
 *   <li> Cpu </li>
 *   <li> Cpuarchitecture </li>
 *   <li> DNAchecksum </li>
 *   <li> Date </li>
 *   <li> DateAccepted </li>
 *   <li> DateAvailable </li>
 *   <li> DateCopyrighted </li>
 *   <li> DateCreated </li>
 *   <li> DateIssued </li>
 *   <li> DateModified </li>
 *   <li> DateSubmitted </li>
 *   <li> DateValid </li>
 *   <li> Definition </li>
 *   <li> Depiction </li>
 *   <li> Description </li>
 *   <li> DisjointWith </li>
 *   <li> Editorialnote </li>
 *   <li> Elements1_1Contributor </li>
 *   <li> Elements1_1Coverage </li>
 *   <li> Elements1_1Identifier </li>
 *   <li> Elements1_1Publisher </li>
 *   <li> Elements1_1Relation </li>
 *   <li> Elements1_1Rights </li>
 *   <li> Elements1_1Source </li>
 *   <li> EquivalentProperty </li>
 *   <li> Exactmatch </li>
 *   <li> Example </li>
 *   <li> Extent </li>
 *   <li> Format </li>
 *   <li> HasFormat </li>
 *   <li> HasPart </li>
 *   <li> Hiddenlabel </li>
 *   <li> Historynote </li>
 *   <li> Identifier </li>
 *   <li> Inmappingrelationwith </li>
 *   <li> Inscheme </li>
 *   <li> InstructionalMethod </li>
 *   <li> InverseOf </li>
 *   <li> IsFormatOf </li>
 *   <li> IsPartOf </li>
 *   <li> IsReferencedBy </li>
 *   <li> IsReplacedBy </li>
 *   <li> IsRequiredBy </li>
 *   <li> IsVersionOf </li>
 *   <li> Language </li>
 *   <li> Lat_long </li>
 *   <li> Latency </li>
 *   <li> License </li>
 *   <li> Location </li>
 *   <li> Maker </li>
 *   <li> Mediator </li>
 *   <li> MemberOf </li>
 *   <li> MembershipClass </li>
 *   <li> MinCardinality </li>
 *   <li> Name </li>
 *   <li> Narrower </li>
 *   <li> Narrowermatch </li>
 *   <li> Narrowertransitive </li>
 *   <li> Nickname </li>
 *   <li> Notation </li>
 *   <li> Note </li>
 *   <li> OnProperty </li>
 *   <li> OneOf </li>
 *   <li> Page </li>
 *   <li> Preferredlabel </li>
 *   <li> Primarytopicof </li>
 *   <li> Provenance </li>
 *   <li> ProvidePaaS </li>
 *   <li> Providehardware </li>
 *   <li> ProvidesSoftwareComponent </li>
 *   <li> Providesoftware </li>
 *   <li> Publisher </li>
 *   <li> Ram </li>
 *   <li> References </li>
 *   <li> Related </li>
 *   <li> Relatedmatch </li>
 *   <li> Relation </li>
 *   <li> Replaces </li>
 *   <li> Requires </li>
 *   <li> Requireshardware </li>
 *   <li> Requiressoftware </li>
 *   <li> Rights </li>
 *   <li> RightsHolder </li>
 *   <li> Scopenote </li>
 *   <li> Source </li>
 *   <li> SpatialCoverage </li>
 *   <li> Speed </li>
 *   <li> Status </li>
 *   <li> TableOfContents </li>
 *   <li> TemporalCoverage </li>
 *   <li> TermsCreator </li>
 *   <li> TermsDate </li>
 *   <li> TermsSubject </li>
 *   <li> TermsType </li>
 *   <li> Termstatus </li>
 *   <li> Title </li>
 *   <li> Type </li>
 *   <li> UnionOf </li>
 *   <li> VersionInfo </li>
 *   <li> Www_cloud4soa_euv0_1infrastructural_domainCapacity </li>
 *   <li> Www_cloud4soa_euv0_1user_modelProvidesResource </li>
 * </ul>
 *
 * This class was generated by <a href="http://RDFReactor.semweb4j.org">RDFReactor</a> on 9/19/11 6:31 PM
 * Modified version 0.1
 */
@RDFBean("http://www.w3.org/2000/01/rdf-schema#Class")
public class Thing {

	private String uriId;


	@RDFSubject(prefix="http://www.w3.org/2000/01/rdf-schema#Class/")
	public String getUriId() {
		return uriId;
	}

	public void setUriId( String uriId ) {
		this.uriId = uriId;
	}

}