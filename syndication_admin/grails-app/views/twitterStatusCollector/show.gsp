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
    <g:set var="entityName" value="${message(code: 'twitterStatusCollector.label', default: 'Tweet Auto Importer')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<div id="show-twitterStatusCollector" class="container-fluid" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:hasError/>

    <div class="row">
        <div class="col-md-10 col-md-offset-2">
            <dl class="dl-horizontal">
                <g:if test="${twitterStatusCollector?.id}">
                    <dt id="id-label" class="word_wrap"><g:message code="twitterStatusCollector.id.label" default="Id"/></dt>
                    <dd class="word_wrap">${twitterStatusCollector?.id}</dd>
                </g:if>

                <g:if test="${twitterStatusCollector?.hashTags}">
                    <dt id="hashTags-label" class="word_wrap"><g:message code="twitterStatusCollector.hashTags.label" default="Hash Tags"/></dt>
                    <dd class="word_wrap">${twitterStatusCollector?.hashTags}</dd>
                </g:if>

                <g:if test="${twitterStatusCollector?.twitterAccounts?.accountName}">
                    <dt id="twitterAccounts-label" class="word_wrap"><g:message code="twitterStatusCollector.twitterAccounts.accountName.label" default="Twitter Account"/></dt>
                    <dd class="word_wrap">${twitterStatusCollector?.twitterAccounts[0]}</dd>
                </g:if>

                <g:if test="${twitterStatusCollector?.collection}">
                    <dt id="collection-label" class="word_wrap"><g:message code="collection.name.label" default="Collection of Statuses"/></dt>
                    <dd class="word_wrap"><g:link action="show" id="${twitterStatusCollector?.collection?.id}" controller="collection">${twitterStatusCollector?.collection?.name}</g:link> </dd>
                </g:if>

                <g:if test="${twitterStatusCollector?.startDate}">
                    <dt id="startDate-label" class="word_wrap"><g:message code="collection.startDate.label" default="Start Date"/></dt>
                    <dd class="word_wrap">${twitterStatusCollector.startDate.format("yyyy-MM-dd")}</dd>
                </g:if>

                <g:if test="${twitterStatusCollector?.endDate}">
                    <dt id="endDate-label" class="word_wrap"><g:message code="collection.endDate.label" default="End Date"/></dt>
                    <dd class="word_wrap">${twitterStatusCollector.endDate.format("yyyy-MM-dd")}</dd>
                </g:if>
            </dl>
        </div>
    </div>

    <fieldset class="buttons">
        <g:form  url="[resource:twitterStatusCollector, action:'edit']">
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER, ROLE_USER">
                %{--<g:actionSubmit class="btn btn-warning" value="Edit" action="edit"/>--}%
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PUBLISHER">
                <g:actionSubmit class="btn btn-danger" onclick="return confirm('Are you sure you want to delete this Tweet Auto Importer?!');" value="Delete" action="delete"/>
            </sec:ifAnyGranted>
            <g:link class="btn btn-default" action="index">
                Cancel
            </g:link>
        </g:form>
    </fieldset>
</div>
</body>
</html>
