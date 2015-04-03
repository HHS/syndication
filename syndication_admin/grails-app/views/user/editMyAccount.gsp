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
<%@ page import="com.ctacorp.syndication.authentication.Role; com.ctacorp.syndication.authentication.User" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>
    <asset:javascript src="passwordValidator.js"/>
</head>

<body>
%{--<div id="page-wrapper">--}%
    <div id="edit-user" class="container-fluid" role="main">
        <h1>My Account</h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>

        <div class="row">
            <div class="col-lg-8 ">

                <g:form class="form-horizontal" url="[resource: userInstance, action: 'updateMyAccount']" onsubmit="checkPass(); return false;" method="PUT">
                    <g:hiddenField name="version" value="${userInstance?.version}"/>
                    <div class="row">
                        <div class="form-group ${hasErrors(bean: userInstance, field: 'name', 'error')} ">
                            <label for="name" class="col-sm-4 control-label">
                                <g:message code="user.name.label" default="Name"/>
                            </label>

                            <div class="col-sm-5">
                                <g:textField autocomplete="off" name="name" placeholder="display name" value="${userInstance?.name}" class="form-control"/>
                            </div>
                        </div>

                        <div class="form-group ${hasErrors(bean: userInstance, field: 'username', 'error')} required">
                            <label for="username" class="col-sm-4 control-label">
                                <g:message code="user.username.label" default="Email"/><span class="required-indicator">*</span>
                            </label>

                            <div class="col-sm-5">
                                <g:textField autocomplete="off" placeholder="username" name="username" required="" value="${userInstance?.username}" class="form-control"/>
                            </div>
                        </div>

                        <div class="form-group ${hasErrors(bean: userInstance, field: 'password', 'error')} required">
                            <label for="password" class="col-sm-4 control-label">
                                <g:message code="user.password.label" default="Password"/><span class="required-indicator">*</span>
                            </label>

                            <div class="col-sm-5">
                                <g:passwordField autocomplete="off" placeholder="password" id="pass1" name="password" required="" value="${userInstance?.password}" class="form-control"/>
                            </div>
                        </div>
                        <div class="form-group ${hasErrors(bean: userInstance, field: 'password', 'error')} required">
                            <label for="password" class="col-sm-4 control-label">
                                <g:message code="user.password.label" default="Retype-Password"/><span class="required-indicator">*</span>
                            </label>

                            <div class="col-sm-5">
                                <g:passwordField autocomplete="off" placeholder="re-type password" id="pass2" onkeyup="checkPass(); return false;" name="passwordVerify" required="" value="${userInstance?.password}" class="form-control"/>
                                <span id="confirmMessage" class="confirmMessage"></span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-4 control-label" for="create"></label>
                            <div class="col-sm-8">
                                <g:actionSubmit class="btn btn-success" action="updateMyAccount" value="${message(code: 'default.button.update.label', default: 'Update')}"/>
                                <g:link class="button" controller="dashboard" action="syndDash" value="Cancel"><button type="button" class="btn">Cancel</button></g:link>
                            </div>
                        </div>
                    </div>
                </g:form>
            </div>
            <div class="hidden-lg"><br></div>
            <div class="col-lg-4">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title">Password Guidelines:</h3>
                    </div>

                    <div class="panel-body">
                        <ul>
                            <li>Be at least 8 characters</li>
                            <li>Contain at least one uppercase letter</li>
                            <li>Contain at least one lowercase letter</li>
                            <li>Contain at least one number</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
