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
<%@ page import="com.ctacorp.syndication.ExtendedAttribute" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'extendedAttribute.label', default:'Extended Attribute')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<div id="list-extendedAttribute" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>
    <div class="row">
        <div class="col-lg-12">
            <!-- /.panel-heading -->
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <g:sortableColumn property="id" title="${message(code: 'extendedAttribute.name.label', default: 'ID')}"/>

                            <g:sortableColumn property="name" title="${message(code: 'extendedAttribute.name.label', default: 'Name')}"/>

                            <g:sortableColumn property="value" title="${message(code: 'extendedAttribute.value.label', default: 'Value')}"/>

                            <th><g:message code="extendedAttribute.mediaItem.label" default="Media Item"/></th>

                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${extendedAttributeInstanceList}" status="i" var="extendedAttributeInstance">
                            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                                <td>${extendedAttributeInstance?.id}</td>

                                <td><g:link action="show" id="${extendedAttributeInstance.id}"><span class="limited-width-md ellipse">${fieldValue(bean: extendedAttributeInstance, field: "name")}</span></g:link></td>

                                <td><span class="limited-width-lg ellipse">${fieldValue(bean: extendedAttributeInstance, field: "value")}</span></td>

                                <td>
                                    <g:link controller="mediaItem" action="show" id="${extendedAttributeInstance.mediaItem.id}">
                                        <span class="limited-width-lg ellipse">${fieldValue(bean: extendedAttributeInstance, field: "mediaItem")}</span>
                                    </g:link>
                                </td>

                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                    <g:if test="${extendedAttributeInstanceCount > params.max}">
                        <div class="pagination">
                            <g:paginate total="${extendedAttributeInstanceCount ?: 0}"/>
                        </div>
                    </g:if>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
