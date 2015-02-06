
%{--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'tinyUrl.label', default: 'TinyURL')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="show-tinyUrl" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ul class="property-list tinyUrl">

                <g:if test="${tinyUrl?.id}">
                    <li class="fieldcontain">
                        <span id="id-label" class="property-label"><g:message code="tinyUrl.id.label" default="ID" /></span>

                        <span class="property-value" aria-labelledby="id-label">${tinyUrl.id}</span>

                    </li>
                </g:if>

                <g:if test="${tinyUrl?.guid}">
                    <li class="fieldcontain">
                        <span id="guid-label" class="property-label"><g:message code="tinyUrl.guid.label" default="Guid" /></span>

                        <span class="property-value" aria-labelledby="guid-label">${tinyUrl.guid}</span>

                    </li>
                </g:if>

                <g:if test="${tinyUrl?.syndicationId}">
                    <li class="fieldcontain">
                        <span id="syndicationId-label" class="property-label"><g:message code="tinyUrl.syndicationId.label" default="Syndication Id" /></span>

                        <span class="property-value" aria-labelledby="syndicationId-label">${tinyUrl.syndicationId}</span>

                    </li>
                </g:if>

                <g:if test="${tinyUrl?.tinyUrlToken}">
                    <li class="fieldcontain">
                        <span id="tinyUrlToken-label" class="property-label"><g:message code="tinyUrl.tinyUrlToken.label" default="TinyURL Token" /></span>

                        <span class="property-value" aria-labelledby="tinyUrlToken-label">${tinyUrl.tinyUrlToken}</span>

                    </li>
                </g:if>

				<g:if test="${tinyUrl?.targetUrl}">
				<li class="fieldcontain">
					<span id="targetUrl-label" class="property-label"><g:message code="tinyUrl.targetUrl.label" default="Target Url" /></span>
					
						<span class="property-value" aria-labelledby="targetUrl-label"><a href="${tinyUrl.targetUrl}">${tinyUrl.targetUrl}</a></span>
					
				</li>
				</g:if>

                <g:if test="${tinyUrl?.tinyUrl}">
                    <li class="fieldcontain">
                        <span id="tinyUrl-label" class="property-label"><g:message code="tinyUrl.tinyUrl.label" default="Tiny Url" /></span>

                        <span class="property-value" aria-labelledby="tinyUrl-label"><a href="${tinyUrl.tinyUrl}">${tinyUrl.tinyUrl}</a></span>

                    </li>
                </g:if>
			</ul>
			<g:form action='delete' method="DELETE">
                <g:hiddenField name="id" value="${tinyUrl?.id}"/>
				<fieldset class="buttons">
					<g:link class="edit" action="edit" id="${tinyUrl?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
