
%{--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%
<%@ page import="com.ctacorp.syndication.authentication.Role; com.ctacorp.syndication.authentication.User" %>
<%@ page import="com.ctacorp.syndication.authentication.UserRole" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<a href="#list-user" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

<div id="list-user" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>
    <div class="table-responsive">
        <table class="table table-hover">
            <thead>
            <tr>

                <g:sortableColumn class="idTables" property="user.id" title="${message(code: 'user.id.label', default: 'ID')}" params="[role:params.role]"/>

                <g:sortableColumn property="user.username" title="${message(code: 'user.username.label', default: 'Username')}" params="[role:params.role]"/>

                <g:sortableColumn property="user.name" title="${message(code: 'user.name.label', default: 'Name')}" params="[role:params.role]"/>

                <g:sortableColumn property="user.accountExpired" title="${message(code: 'user.accountExpired.label', default: 'Account Expired')}" params="[role:params.role]"/>

                <g:sortableColumn property="user.accountLocked" title="${message(code: 'user.accountLocked.label', default: 'Account Locked')}" params="[role:params.role]"/>

                <g:sortableColumn property="user.enabled" title="${message(code: 'user.enabled.label', default: 'Enabled')}" params="[role:params.role]"/>

                <g:sortableColumn property="role" title="${message(code: 'user.role.label', default: 'Role')}" params="[role:params.role]"/>

            </tr>
            </thead>
            <tbody>
            <g:each in="${userRoles}" status="i" var="userRoleInstance">
                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                    <td>${userRoleInstance?.user?.id}</td>

                    <td><g:link action="show" id="${userRoleInstance.user.id}">${fieldValue(bean: userRoleInstance.user, field: "username")}</g:link></td>

                    <td>${fieldValue(bean: userRoleInstance.user, field: "name")}</td>

                    <td><g:formatBoolean boolean="${userRoleInstance.user.accountExpired}"/></td>

                    <td><g:formatBoolean boolean="${userRoleInstance.user.accountLocked}"/></td>

                    <td><g:formatBoolean boolean="${userRoleInstance.user.enabled}"/></td>

                    <td>${userRoleInstance.role}</td>

                </tr>
            </g:each>
            </tbody>
        </table>
    </div>
    <g:if test="${userInstanceCount > params.max}">
        <div class="pagination">
            <g:paginate total="${userInstanceCount ?: 0}" params="[role:params.role]"/>
        </div>
    </g:if>
    <br/>
    <br/>
    <div class="row">
        <div class="col-md-12">
            <form class="form-horizontal" action="index.gsp">
                <fieldset>
                    <!-- Form Name -->
                    <legend>Search - Filter</legend>

                    <!-- Select Basic -->
                    <div class="form-group">
                        <label class="col-md-4 control-label" for="role">Role</label>
                        <div class="col-md-4">
                            <sec:ifAnyGranted roles="ROLE_MANAGER">
                                <g:select name="role" from="${com.ctacorp.syndication.authentication.Role.findAllByAuthorityInList(["ROLE_USER", "ROLE_BASIC", "ROLE_STATS"])}" optionKey="authority" noSelection="['Any':'Any']" class="form-control" value="${currentRole}"/>
                            </sec:ifAnyGranted>
                            <sec:ifAnyGranted roles="ROLE_ADMIN">
                                <g:select name="role" from="${com.ctacorp.syndication.authentication.Role.list()}" noSelection="['Any':'Any']" optionKey="authority" class="form-control" value="${currentRole}"/>
                            </sec:ifAnyGranted>
                        </div>
                    </div>

                    <!-- Search input-->
                    <div class="form-group">
                        <label class="col-md-4 control-label" for="search">Search by Name/Username</label>
                        <div class="col-md-4">
                            <input id="search" name="search" type="search" placeholder="Name / Username" class="form-control input-md" value="${params?.search}">
                            <p class="help-block">Type in a search term, or leave blank for listing</p>
                        </div>
                    </div>

                    <!-- Multiple Radios (inline) -->
                    <div class="form-group">
                        <label class="col-md-4 control-label" for="searchSelector">Search</label>
                        <div class="col-md-4">
                            <label class="radio-inline" for="searchSelector-0">
                                <input type="radio" name="searchSelector" id="searchSelector-0" value="user.name" ${params.searchSelector ? params?.searchSelector == 'user.name' ? "checked='true'" : "" : "checked='true'"}>
                                Name
                            </label>
                            <label class="radio-inline" for="searchSelector-1">
                                <input type="radio" name="searchSelector" id="searchSelector-1" value="user.username" ${params?.searchSelector == 'user.username' ? "checked='true'" : ""}>
                                Username
                            </label>
                        </div>
                    </div>

                    <!-- Button -->
                    <div class="form-group">
                        <label class="col-md-4 control-label" for="apply"></label>
                        <div class="col-md-4">
                            <g:submitButton id="apply" name="apply" class="btn btn-primary" value="Apply"/>
                        </div>
                    </div>

                </fieldset>
            </form>
        </div>
    </div>
</div>
</body>
</html>
