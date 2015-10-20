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
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'twitterAccount.label', default: 'Twitter Account')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<div id="show-twitterAccount" class="container-fluid" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>

    <div class="row">
        <div class="col-md-10 col-md-offset-2">
            <dl class="dl-horizontal">
                <g:if test="${twitterAccountInstance?.id}">
                    <dt id="id-label" class="word_wrap"><g:message code="twitterAccountInstance.id.label" default="Id"/></dt>
                    <dd class="word_wrap">${twitterAccountInstance?.id}</dd>
                </g:if>

                <g:if test="${twitterAccountInstance?.accountName}">
                    <dt id="name-label" class="word_wrap"><g:message code="twitterAccountInstance.accountName.label" default="Account Name"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${twitterAccountInstance}" field="accountName"/></dd>
                </g:if>

            </dl>
        </div>
    </div>

    <fieldset class="buttons">
        <g:form  url="[resource:twitterAccountInstance, action:'edit']">
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER, ROLE_USER">
                <g:actionSubmit class="btn btn-warning" value="Edit" action="edit"/>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN">
                <g:actionSubmit class="btn btn-danger" onclick="return confirm('Are you sure?! This action will delete all Tweet items associated with this account!');" value="Delete" action="delete"/>
            </sec:ifAnyGranted>
            <g:link class="button" action="index">
                <button type="button" class="btn">Cancel</button>
            </g:link>
        </g:form>
    </fieldset>
</div>
</body>
</html>
