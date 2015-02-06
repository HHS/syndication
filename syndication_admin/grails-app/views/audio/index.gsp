%{--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<%@ page import="com.ctacorp.syndication.Audio" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'audio.label', default: 'Audio')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<div id="list-audio" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>

    <div class="row">
        <div class="col-lg-12">
            <!-- /.panel-heading -->
            <div class="panel panel-info">
                <div class="table-responsive">
                    <table class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <g:sortableColumn class="idTables" property="id" title="${message(code: 'audio.id.label', default: 'ID')}"/>

                            <g:sortableColumn property="name" title="${message(code: 'audio.name.label', default: 'Name')}"/>

                            <g:sortableColumn property="description" title="${message(code: 'audio.description.label', default: 'Description')}"/>

                            <g:sortableColumn property="sourceUrl" title="${message(code: 'audio.sourceUrl.label', default: 'Source Url')}"/>

                            <g:sortableColumn property="dateSyndicationCaptured" title="${message(code: 'audio.dateSyndicationCaptured.label', default: 'Date Syndication Captured')}"/>

                            <g:sortableColumn property="dateSyndicationUpdated" title="${message(code: 'audio.dateSyndicationUpdated.label', default: 'Date Syndication Updated')}"/>

                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${audioInstanceList}" status="i" var="audioInstance">
                            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                                <td>${audioInstance?.id}</td>

                                <td><g:link action="show" id="${audioInstance.id}"><span class="limited-width-md ellipse">${fieldValue(bean: audioInstance, field: "name")}</span></g:link></td>

                                <td><span class="limited-width-lg ellipse">${fieldValue(bean: audioInstance, field: "description")}</span></td>

                                <td><span class="limited-width-md ellipse break-url">${fieldValue(bean: audioInstance, field: "sourceUrl")}</span></td>

                                <td><g:formatDate date="${audioInstance.dateSyndicationCaptured}"/></td>

                                <td><g:formatDate date="${audioInstance.dateSyndicationUpdated}"/></td>

                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                    <g:if test="${audioInstanceCount > params.max}">
                        <div class="pagination">
                            <g:paginate total="${audioInstanceCount ?: 0}"/>
                        </div>
                    </g:if>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
