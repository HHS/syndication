%{--
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ctacorp.syndication.SyndicationRequest" %>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'syndicationRequest.label', default: 'SyndicationRequest')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-syndicationRequest" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-syndicationRequest" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
                        <g:sortableColumn property="id" title="${message(code: 'syndicationRequest.id.label', default: 'Id')}" />

                        <g:sortableColumn property="requestedUrl" title="${message(code: 'syndicationRequest.requestedUrl.label', default: 'Requested Url')}" />
					
						<g:sortableColumn property="contactEmail" title="${message(code: 'syndicationRequest.contactEmail.label', default: 'Contact Email')}" />
					
						<g:sortableColumn property="requesterNote" title="${message(code: 'syndicationRequest.requesterNote.label', default: 'Requester Note')}" />
					
						<g:sortableColumn property="adminNote" title="${message(code: 'syndicationRequest.adminNote.label', default: 'Admin Note')}" />
					
						<g:sortableColumn property="status" title="${message(code: 'syndicationRequest.status.label', default: 'Status')}" />
					
						<th><g:message code="syndicationRequest.media.label" default="Media" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${syndicationRequestInstanceList}" status="i" var="syndicationRequestInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${syndicationRequestInstance.id}">${syndicationRequestInstance?.id}</g:link></td>

                        <td>${fieldValue(bean: syndicationRequestInstance, field: "requestedUrl")}</td>

                        <td>${fieldValue(bean: syndicationRequestInstance, field: "contactEmail")}</td>
					
						<td>${fieldValue(bean: syndicationRequestInstance, field: "requesterNote")}</td>
					
						<td>${fieldValue(bean: syndicationRequestInstance, field: "adminNote")}</td>
					
						<td>${fieldValue(bean: syndicationRequestInstance, field: "status")}</td>
					
						<td>${fieldValue(bean: syndicationRequestInstance, field: "media")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${syndicationRequestInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
