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
<%@ page import="com.ctacorp.syndication.authentication.Role; com.ctacorp.syndication.authentication.User" %>
<%@ page import="com.ctacorp.syndication.authentication.UserRole" %>
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
    <div id="messages">

    </div>
    <synd:message/>
    <synd:errors/>
    <synd:hasError/>

    <form role="search" class="form-horizontal" action="index">
        <g:render template="search"/>

        <div class="table-responsive">
            <table class="table table-hover">
                <thead>
                <tr>

                    <g:sortableColumn class="idTables" property="user.id" title="${message(code: 'user.id.label', default: 'ID')}" params="[role:params.role]"/>

                    <g:sortableColumn property="user.username" title="${message(code: 'user.username.label', default: 'Username')}" params="[role:params.role]"/>

                    <g:sortableColumn property="user.name" title="${message(code: 'user.name.label', default: 'Name')}" params="[role:params.role]"/>

                    <g:sortableColumn property="user.accountExpired" title="${message(code: 'user.accountExpired.label', default: 'Account Expired')}" params="[role:params.role]"/>

                    %{--<g:sortableColumn property="user.accountLocked" title="${message(code: 'user.accountLocked.label', default: 'Account Locked')}" params="[role:params.role]"/>--}%

                    <g:sortableColumn property="user.enabled" title="${message(code: 'user.enabled.label', default: 'Enabled')}" params="[role:params.role]"/>

                    <g:sortableColumn property="role" title="${message(code: 'user.role.label', default: 'Role')}" params="[role:params.role]"/>

                    <th>Reset Password</th>

                </tr>
                </thead>
                <tbody>
                <g:each in="${userRoles}" status="i" var="userRoleInstance">
                    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                        <td>${userRoleInstance?.user?.id}</td>

                        <td><g:link action="show" id="${userRoleInstance.user.id}">${fieldValue(bean: userRoleInstance.user, field: "username")}</g:link></td>

                        <td>${fieldValue(bean: userRoleInstance.user, field: "name")}</td>

                        <td><g:formatBoolean boolean="${userRoleInstance.user.accountExpired}"/></td>

                        %{--<td><g:formatBoolean boolean="${userRoleInstance.user.accountLocked}"/></td>--}%

                        <td><g:formatBoolean boolean="${userRoleInstance.user.enabled}"/></td>

                        <td>${userRoleInstance.role}</td>

                        <td><input type="button" id="resetPassword" name="resetPassword" onclick="resetPassword(${userRoleInstance.user.id})" class="btn btn-primary" value="Reset Password"/></td>

                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>
        <g:if test="${userInstanceCount > params.max}">
            <div class="pagination">
                <g:paginate total="${userInstanceCount ?: 0}" params="[
                        role:params.role,
                        search:params.search,
                        searchSelector:params.searchSelector
                ]"/>
            </div>
        </g:if>
    </form>
    <script>
        function resetPassword( id ) {
            $.ajax({
                type: "POST",
                data: {userId: id},
                url: "${g.createLink(controller:'user',action:'resetPassword')}",
                success: function (response ) {
                    document.getElementById("messages").innerHTML = "<div class='row'><div class=\"alert alert-info alert-dismissable break-word\">" +
                        "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">&times;</button>" +
                        response +
                        "</div></div>"
                },
                error:function( response ) {
                    document.getElementById("messages").innerHTML = "<div class='row'><div class=\"alert alert-danger alert-dismissable break-word\">" +
                            "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">&times;</button>" +
                            response.responseText +
                            "</div></div>"
                }
            });
        }
    </script>
</div>
</body>
</html>
