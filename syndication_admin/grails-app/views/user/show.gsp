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
<%@ page import="com.ctacorp.syndication.authentication.User" %>
<%@ page import="com.ctacorp.syndication.authentication.UserRole" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<a href="#show-user" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

<div id="show-user" class="content scaffold-show" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:hasError/>
    %{--<ol class="property-list user">--}%

<div class="row">
        <div class="col-sm-9 col-sm-offset-1">
            <dl class="dl-horizontal">
                <dt id="id-label" class="word_wrap"><g:message code="user.id.label" default="ID"/></dt>
                <dd class="word_wrap">${user?.id}</dd>

                <dt id="authority-label" class="word_wrap"><g:message code="role.authority.label" default="Authority"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${UserRole.findAllByUser(User.get(user.id))?.role?.first()}" field="authority"/></dd>

                <g:if test="${user?.name}">
                    <dt id="name-label" class="word_wrap"><g:message code="user.name.label" default="Name"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${user}" field="name"/></dd>
                </g:if>

                <g:if test="${user?.username}">
                    <dt id="username-label" class="word_wrap"><g:message code="user.username.label" default="Username"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${user}" field="username"/></dd>
                </g:if>

                <dt id="enabled-label" class="word_wrap"><g:message code="user.enabled.label" default="Enabled"/></dt>
                <dd class="word_wrap"><g:formatBoolean boolean="${user?.enabled}"/></dd>

                <dt id="accountExpired-label" class="word_wrap"><g:message code="user.accountExpired.label" default="Account Expired"/></dt>
                <dd class="word_wrap"><g:formatBoolean boolean="${user?.accountExpired}"/></dd>

                <dt id="accountLocked-label" class="word_wrap"><g:message code="user.accountLocked.label" default="Account Locked"/></dt>
                <dd class="word_wrap"><g:formatBoolean boolean="${user?.accountLocked}"/></dd>

                <dt id="passwordExpired-label" class="word_wrap"><g:message code="user.passwordExpired.label" default="Password Expired"/></dt>
                <dd class="word_wrap"><g:formatBoolean boolean="${user?.passwordExpired}"/></dd>

                <g:if test="${userInstance?.lastLogin}">
                    <dt id="lastLogin-label" class="word_wrap"><g:message code="user.lastLogin.label" default="Last Login Date"/></dt>
                    <dd class="word_wrap"><g:formatDate date="${user?.lastLogin}" format="MMM dd, yyyy"/>
                </g:if>

                <g:if test="${user?.likes}">
                    <dt id="likes-label" class="word_wrap"><g:message code="user.likes.label" default="Likes"/></dt>
                    <g:each in="${user.likes}" var="l">
                        <dd class="word_wrap"><g:link controller="mediaItem" action="show" id="${l.id}">${l?.encodeAsHTML()}</g:link></dd>
                    </g:each>
                </g:if>
            </dl>
        </div>
</div>
    <div class="col-sm-8">
    <g:form url="[resource: user, action: 'delete']" method="DELETE">
        <fieldset class="buttons">
            <g:actionSubmit class="btn btn-success" value="Edit" action="edit"/>
            <g:if test="${allowDelete}">
                <g:actionSubmit class="btn btn-danger" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                            onclick="return confirm('${message(code: 'user.delete.confirm.message', default: 'Are you sure?')}');"/>
            </g:if>
            <g:link class="btn btn-default" action="index">
                Cancel
            </g:link>
        </fieldset>
    </g:form>
    </div>
</div>
</body>
</html>
