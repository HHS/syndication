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
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'campaign.label', default: 'Campaign')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="show-campaign" class="container-fluid" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <synd:message/>
            <synd:errors/>
            <synd:error/>
            <div class="row">
                 <div class="col-sm-9 col-sm-offset-1">
                    <dl class="dl-horizontal">

                    <g:if test="${campaignInstance?.id}">
                        <dt id="id-label" class="word_wrap"><g:message code="campaign.id.label" default="Id" /></dt>
                        <dd class="word_wrap">${campaignInstance?.id}</dd>
                    </g:if>
                    <g:if test="${campaignInstance?.name}">
                        <dt id="name-label" class="word_wrap"><g:message code="campaign.name.label" default="Name" /></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${campaignInstance}" field="name"/></dd>
                    </g:if>

                    <g:if test="${campaignInstance?.description}">
                        <dt id="description-label" class="word_wrap"><g:message code="campaign.description.label" default="Description" /></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${campaignInstance}" field="description"/></dd>
                    </g:if>

                    <g:if test="${campaignInstance?.contactEmail}">
                        <dt id="contactEmail-label" class="word_wrap"><g:message code="campaign.contactEmail.label" default="Contact Email" /></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${campaignInstance}" field="contactEmail"/></dd>
                    </g:if>

                    <g:if test="${campaignInstance?.startDate}">
                        <dt id="startDate-label" class="word_wrap"><g:message code="campaign.startDate.label" default="Start Date" /></dt>
                        <dd class="word_wrap"><g:formatDate date="${campaignInstance?.startDate}" /></dd>
                    </g:if>

                    <g:if test="${campaignInstance?.endDate}">
                        <dt id="endDate-label" class="word_wrap"><g:message code="campaign.endDate.label" default="End Date" /></dt>
                        <dd class="word_wrap"><g:formatDate date="${campaignInstance?.endDate}" /></dd>
                    </g:if>

                    <g:if test="${campaignInstance?.source}">
                        <dt id="source-label" class="word_wrap"><g:message code="campaign.source.label" default="Source" /></dt>
                        <dd class="word_wrap"><g:link controller="source" action="show" id="${campaignInstance?.source?.id}">${campaignInstance?.source?.encodeAsHTML()}</g:link></dd>
                    </g:if>

                    <g:if test="${campaignInstance?.campaignMetrics}">
                        <dt id="campaignMetrics-label" class="word_wrap"><g:message code="campaign.campaignMetrics.label" default="Campaign Metrics" /></dt>
                        <g:each in="${campaignInstance.campaignMetrics}" var="c">
                            <dd class="word_wrap"><g:link controller="campaignMetric" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></dd>
                        </g:each>
                    </g:if>

                    <g:if test="${campaignInstance?.mediaItems}">
                        <dt id="mediaItems-label" class="word_wrap"><g:message code="campaign.mediaItems.label" default="Media Items" /></dt>
                        <g:each in="${campaignInstance.mediaItems}" var="m">
                            <dd class="word_wrap"><g:link controller="mediaItem" action="show" id="${m.id}">${m?.encodeAsHTML()}</g:link></dd>
                        </g:each>
                    </g:if>
                    </dl>
                </div>
            </div>
			<div class="col-sm-8">
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER">
                <g:form url="[resource:campaignInstance, action:'delete']" method="DELETE">
                    <fieldset class="buttons">
                        <g:link class="btn btn-success" action="edit" resource="${campaignInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                            <g:actionSubmit class="btn btn-danger" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                        <g:link class="btn btn-default" action="index">
                            Cancel
                        </g:link>
                    </fieldset>
                </g:form>
            </sec:ifAnyGranted>
            </div>
		</div>
	</body>
</html>
