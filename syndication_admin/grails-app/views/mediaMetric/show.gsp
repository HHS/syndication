%{--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ctacorp.syndication.metric.MediaMetric" %>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'mediaMetric.label', default: 'Media Metrics')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="show-mediaMetric" class="container-fluid" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <synd:message/>
            <synd:errors/>
            <synd:error/>
            <div class="row">
                <div class="col-sm-7 col-sm-offset-2">
                    <dl class="dl-horizontal">
			
                    <g:if test="${mediaMetricInstance?.day}">
                        <dt id="day-label" class="word_wrap"><g:message code="mediaMetric.day.label" default="Day" /></dt>
                        <dd class="word_wrap"><g:formatDate date="${mediaMetricInstance?.day}" /></dd>
                    </g:if>

                    <g:if test="${mediaMetricInstance?.storefrontViewCount}">
                        <dt id="storefrontViewCount-label" class="word_wrap"><g:message code="mediaMetric.storefrontViewCount.label" default="Storefront View Count" /></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${mediaMetricInstance}" field="storefrontViewCount"/></dd>
                    </g:if>

                    <g:if test="${mediaMetricInstance?.apiViewCount}">
                        <dt id="apiViewCount-label" class="word_wrap"><g:message code="mediaMetric.apiViewCount.label" default="Api View Count" /></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${mediaMetricInstance}" field="apiViewCount"/></dd>
                    </g:if>

                    <g:if test="${mediaMetricInstance?.media}">
                        <dt id="media-label" class="word_wrap"><g:message code="mediaMetric.media.label" default="Media" /></dt>
                        <dd class="word_wrap"><g:link controller="mediaItem" action="show" id="${mediaMetricInstance?.media?.id}">${mediaMetricInstance?.media?.encodeAsHTML()}</g:link></dd>
                    </g:if>
                    </dl>
                </div>
            </div>
            <div class="col-sm-8">
            <sec:ifAnyGranted roles="ROLE_ADMIN">
                <g:form url="[resource:mediaMetricInstance, action:'delete']" method="DELETE">
                    <fieldset class="buttons">
                        <g:link class="btn btn-success" action="edit" resource="${mediaMetricInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
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
