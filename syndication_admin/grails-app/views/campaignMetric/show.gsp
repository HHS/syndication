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
		<g:set var="entityName" value="${message(code: 'campaignMetric.label', default: 'Campaign Metric')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="show-campaignMetric" class="container-fluid" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <synd:message/>
            <synd:errors/>
            <synd:hasError/>
            <div class="row">
                <div class="col-sm-7 col-sm-offset-2">
                    <dl class="dl-horizontal">
			
                    <g:if test="${campaignMetricInstance?.campaign}">
                        <dt id="campaign-label" class="word_wrap"><g:message code="campaignMetric.campaign.label" default="Campaign" /></dt>
                        <dd class="word_wrap"><g:link controller="campaign" action="show" id="${campaignMetricInstance?.campaign?.id}">${campaignMetricInstance?.campaign?.encodeAsHTML()}</g:link></dd>
                    </g:if>

                    <g:if test="${campaignMetricInstance?.storefrontViewCount}">
                        <dt id="storefrontViewCount-label" class="word_wrap"><g:message code="campaignMetric.storefrontViewCount.label" default="Storefront View Count" /></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${campaignMetricInstance}" field="storefrontViewCount"/></dd>
                    </g:if>

                    <g:if test="${campaignMetricInstance?.apiViewCount}">
                        <dt id="apiViewCount-label" class="word_wrap"><g:message code="campaignMetric.apiViewCount.label" default="Api View Count" /></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${campaignMetricInstance}" field="apiViewCount"/></dd>
                    </g:if>

                    <g:if test="${campaignMetricInstance?.day}">
                        <dt id="day-label" class="word_wrap"><g:message code="campaignMetric.day.label" default="Day" /></dt>
                        <dd class="word_wrap"><g:formatDate date="${campaignMetricInstance?.day}" /></dd>
                    </g:if>
			        </dl>
                </div>
            </div>
            <div class="col-sm-8">
                <sec:ifAnyGranted roles="ROLE_ADMIN">
                    <g:form url="[resource:campaignMetricInstance, action:'delete']" method="DELETE">
                        <fieldset class="buttons">
                            <g:link class="btn btn-success" action="edit" resource="${campaignMetricInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
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
