
%{--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<%@ page import="com.ctacorp.syndication.SyndicationRequest" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'syndicationRequest.label', default: 'SyndicationRequest')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="show-syndicationRequest" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ul class="property-list syndicationRequest">


                <g:if test="${syndicationRequestInstance?.id}">
                    <li class="fieldcontain">
                        <span id="id-label" class="property-label"><g:message code="syndicationRequest.id.label" default="Id" /></span>

                        <span class="property-value" aria-labelledby="id-label">${syndicationRequestInstance?.id}</span>

                    </li>
                </g:if>

				<g:if test="${syndicationRequestInstance?.requestedUrl}">
				<li class="fieldcontain">
					<span id="requestedUrl-label" class="property-label"><g:message code="syndicationRequest.requestedUrl.label" default="Requested Url" /></span>
					
						<span class="property-value" aria-labelledby="requestedUrl-label"><g:fieldValue bean="${syndicationRequestInstance}" field="requestedUrl"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${syndicationRequestInstance?.contactEmail}">
				<li class="fieldcontain">
					<span id="contactEmail-label" class="property-label"><g:message code="syndicationRequest.contactEmail.label" default="Contact Email" /></span>
					
						<span class="property-value" aria-labelledby="contactEmail-label"><g:fieldValue bean="${syndicationRequestInstance}" field="contactEmail"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${syndicationRequestInstance?.requesterNote}">
				<li class="fieldcontain">
					<span id="requesterNote-label" class="property-label"><g:message code="syndicationRequest.requesterNote.label" default="Requester Note" /></span>
					
						<span class="property-value" aria-labelledby="requesterNote-label"><g:fieldValue bean="${syndicationRequestInstance}" field="requesterNote"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${syndicationRequestInstance?.adminNote}">
				<li class="fieldcontain">
					<span id="adminNote-label" class="property-label"><g:message code="syndicationRequest.adminNote.label" default="Admin Note" /></span>
					
						<span class="property-value" aria-labelledby="adminNote-label"><g:fieldValue bean="${syndicationRequestInstance}" field="adminNote"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${syndicationRequestInstance?.status}">
				<li class="fieldcontain">
					<span id="status-label" class="property-label"><g:message code="syndicationRequest.status.label" default="Status" /></span>
					
						<span class="property-value" aria-labelledby="status-label"><g:fieldValue bean="${syndicationRequestInstance}" field="status"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${syndicationRequestInstance?.media}">
				<li class="fieldcontain">
					<span id="media-label" class="property-label"><g:message code="syndicationRequest.media.label" default="Media" /></span>
					
						<span class="property-value" aria-labelledby="media-label"><g:link controller="mediaItem" action="show" id="${syndicationRequestInstance?.media?.id}">${syndicationRequestInstance?.media?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${syndicationRequestInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="syndicationRequest.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${syndicationRequestInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${syndicationRequestInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="syndicationRequest.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${syndicationRequestInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
			</ul>
			<g:form url="[resource:syndicationRequestInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${syndicationRequestInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
