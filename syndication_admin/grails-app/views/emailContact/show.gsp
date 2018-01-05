<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 11/25/15
  Time: 2:45 PM
--%>

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
    <g:set var="entityName" value="${message(code: 'emailContact.label', default: 'Email Contact')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<div id="show-emailContact" class="container-fluid" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:hasError/>

    <div class="row">
        <div class="col-md-6 col-md-offset-1">
            <dl class="dl-horizontal">

                <g:if test="${emailContactInstance?.id}">
                    <dt id="id-label" class="word_wrap"><g:message code="emailContact.id.label" default="Id"/></dt>
                    <dd class="word_wrap">${emailContactInstance?.id}</dd>
                </g:if>

                <g:if test="${emailContactInstance?.name}">
                    <dt id="name-label" class="word_wrap"><g:message code="emailContact.name.label" default="Name"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${emailContactInstance}" field="name"/></dd>
                </g:if>

                <g:if test="${emailContactInstance?.email}">
                    <dt id="email-label" class="word_wrap"><g:message code="emailContact.email.label" default="Email Address"/></dt>
                    <dd class="word_wrap">${emailContactInstance?.email}</dd>
                </g:if>
            </dl>
        </div>
        <div class="col-md-4" style="min-width: 16em;">
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h1 class="panel-title">User Tools</h1>
                </div>
                <div class="panel-body">
                    <g:link action="sendTestEmail" id="${emailContactInstance.id}" class="btn btn-success"><i class="fa fa-envelope"></i> Send Test Message</g:link>
                </div>
            </div>
        </div>
    </div>


    <fieldset class="buttons">
        <g:form url="[resource:emailContactInstance, action:'delete']" method="DELETE">
            <g:actionSubmit class="btn btn-warning" value="Edit" action="edit"/>
            <g:actionSubmit class="btn btn-danger" action="delete" id="${emailContactInstance?.id}" onclick="return confirm('${message(code: 'default.button.delete.emailContact.confirm', default: 'Are you sure?')}');" value="Delete"/>
            <g:actionSubmit class="btn btn-default" action="index" value="Cancel"/>
        </g:form>
    </fieldset>
</div>
</body>
</html>
