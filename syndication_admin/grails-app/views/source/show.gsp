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
<%@ page import="com.ctacorp.syndication.Source" %>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'source.label', default: 'Source')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="show-source" class="container-fluid" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <synd:message/>
            <synd:errors/>
            <synd:error/>
            <div class="row">
                <div class="col-sm-9 col-sm-offset-1">
                    <dl class="dl-horizontal">

                    <g:if test="${sourceInstance?.id}">
                        <dt id="id-label" class="word_wrap"><g:message code="source.id.label" default="Id" /></dt>
                        <dd class="word_wrap">${sourceInstance?.id}</dd>
                    </g:if>
                    <g:if test="${sourceInstance?.name}">
                        <dt id="name-label" class="word_wrap"><g:message code="source.name.label" default="Name" /></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${sourceInstance}" field="name"/></dd>
                    </g:if>

                    <g:if test="${sourceInstance?.acronym}">
                        <dt id="acronym-label" class="word_wrap"><g:message code="source.acronym.label" default="Acronym" /></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${sourceInstance}" field="acronym"/></dd>
                    </g:if>

                    <g:if test="${sourceInstance?.description}">
                        <dt id="description-label" class="word_wrap"><g:message code="source.description.label" default="Description" /></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${sourceInstance}" field="description"/></dd>
                    </g:if>
                    <g:if test="${sourceInstance?.contactEmail}">
                        <dt id="contactEmail-label" class="word_wrap"><g:message code="source.contactEmail.label" default="Contact Email" /></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${sourceInstance}" field="contactEmail"/></dd>
                    </g:if>
                    <g:if test="${sourceInstance?.websiteUrl}">
                        <dt id="websiteUrl-label" class="word_wrap"><g:message code="source.websiteUrl.label" default="Website Url" /></dt>
                        <dd class="word_wrap"><a target="_blank" href="${sourceInstance?.websiteUrl}"><g:fieldValue bean="${sourceInstance}" field="websiteUrl"/></a></dd>
                    </g:if>

                    <g:if test="${sourceInstance?.largeLogoUrl}">
                        <dt id="largeLogoUrl-label" class="word_wrap"><g:message code="source.largeLogoUrl.label" default="Large Logo Url" /></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${sourceInstance}" field="largeLogoUrl"/></dd>
                    </g:if>

                    <g:if test="${sourceInstance?.smallLogoUrl}">
                        <dt id="smallLogoUrl-label" class="word_wrap"><g:message code="source.smallLogoUrl.label" default="Small Logo Url" /></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${sourceInstance}" field="smallLogoUrl"/></dd>
                    </g:if>
			        </dl>
                </div>
            </div>
			<div class="col-sm-8">
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER">
                <g:form url="[resource:sourceInstance, action:'delete']" method="DELETE">
                    <fieldset class="buttons">
                        <g:link class="btn btn-success" action="edit" resource="${sourceInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                        <g:actionSubmit class="btn btn-danger" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                        <g:link class="button" action="index">
                            <button type="button" class="btn">Cancel</button>
                        </g:link>
                    </fieldset>
                </g:form>
            </sec:ifAnyGranted>
            </div>
		</div>
	</body>
</html>
