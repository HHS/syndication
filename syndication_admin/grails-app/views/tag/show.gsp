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
    <title>Tag List</title>
    <meta name="layout" content="dashboard"/>
</head>

<body>
<div id="page-wrapper">
    <div class="row">
        <div class="col-md-12">
            <h1 class="page-header">Tag Details</h1>
        </div>
    </div>

    <g:if test='${flash.message}'>
        <div class="panel panel-default">
            <div class='panel-body'><span class="text-info"><i class="fa fa-info"></i> ${flash.message}</span></div>
        </div>
    </g:if>

    <div class="row">
        <ul class="property-list html">

            <g:if test="${tag.id}">
                <li class="fieldcontain">
                    <span id="id-label" class="property-label"><g:message code="tag.id.label" default="ID"/></span>
                    <span class="property-value" aria-labelledby="id-label">${tag.id}</span>
                </li>
            </g:if>

            <g:if test="${tag.name}">
                <li class="fieldcontain">
                    <span id="name-label" class="property-label"><g:message code="tag.name.label" default="Name"/></span>
                    <span class="property-value" aria-labelledby="id-label">${tag.name}</span>
                </li>
            </g:if>

            <g:if test="${tag.type}">
                <li class="fieldcontain">
                    <span id="type-label" class="property-label"><g:message code="tag.type.label" default="Type"/></span>
                    <span class="property-value" aria-labelledby="id-label">${tag.type.name}</span>
                </li>
            </g:if>

            <g:if test="${tag.language}">
                <li class="fieldcontain">
                    <span id="language-label" class="property-label"><g:message code="tag.language.label" default="Language"/></span>
                    <span class="property-value" aria-labelledby="id-label">${tag.language.name}</span>
                </li>
            </g:if>

            <g:if test="${tag.dateCreated}">
                <li class="fieldcontain">
                    <span id="dateCreated-label" class="property-label"><g:message code="tag.dateCreate.label" default="Date Created"/></span>
                    <span class="property-value" aria-labelledby="id-label">${Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", tag.dateCreated).format("MMM dd, yyyy 'at' h:mm a")}</span>
                </li>
            </g:if>

            <g:if test="${tag.lastUpdated}">
                <li class="fieldcontain">
                    <span id="lastUpdated-label" class="property-label"><g:message code="tag.lastUpdated.label" default="Last Updated"/></span>
                    <span class="property-value" aria-labelledby="id-label">${Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", tag.lastUpdated).format("MMM dd, yyyy 'at' h:mm a")}</span>
                </li>
            </g:if>
        </ul>
    </div>
        <div class="row">
            <div class="col-xs-12">
                <g:form class="form-horizontal">
                    <g:hiddenField name="id" value="${tag.id}"/>
                    <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_MANAGER,ROLE_USER">
                        <g:actionSubmit action="edit" class="btn btn-warning" value="Edit"/>
                        <g:actionSubmit action="delete" class="btn btn-danger" onclick="return confirm('Are you sure?');" value="Delete"/>
                    </sec:ifAnyGranted>

                    <g:link class="button" action="index">
                        <button type="button" class="btn">List</button>
                    </g:link>
                </g:form>
            </div>
        </div>

    <br>
    <g:if test="${mediaItemsList}">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 id="tagListTitle" class="panel-title">Media Items List</h3>
            </div>

            <div id="tagList" class="panel-body">
                <g:render template="mediaList"/>
            </div>
        </div>
    </g:if>
</div>
</body>
</html>