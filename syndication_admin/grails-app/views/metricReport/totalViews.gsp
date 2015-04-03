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
<%@ page import="com.ctacorp.syndication.metric.MediaMetric" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="Totals"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<div id="list-mediaMetric" class="content scaffold-list" role="main">
    <g:render template="header"/>

    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <div class="row">
        <div class="col-lg-12">
            <!-- /.panel-heading -->
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>

                            <g:sortableColumn class="idTables" property="id" title="${message(code: 'mediaItem.id.label', default: 'ID')}"/>

                            <g:sortableColumn property="name" title="${message(code: 'mediaItem.name.label', default: 'Name')}"/>

                            <g:sortableColumn property="storefrontViewCount" title="${message(code: 'mediaMetric.apiViewCount.label', default: 'Total Storefront Views')}"/>

                            <g:sortableColumn property="apiViewCount" title="${message(code: 'mediaMetric.apiViewCount.label', default: 'Total Api Views')}"/>

                        </tr>
                        </thead>
                        <tbody>

                        <g:each in="${mediaItemInstanceList}" status="i" var="mediaItemInstance">
                            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                                <td>${mediaItemInstance?.id}</td>

                                <td><g:link controller="mediaItem" action="show" id="${mediaItemInstance.id}">${fieldValue(bean: mediaItemInstance, field: "name")}</g:link></td>

                                <td>${MediaMetric.findAllByMedia(mediaItemInstance)?.storefrontViewCount?.sum() ?: 0}</td>

                                <td>${MediaMetric.findAllByMedia(mediaItemInstance)?.apiViewCount?.sum() ?: 0}</td>

                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="pagination">
                            <g:paginate total="${mediaItemInstanceCount ?: 0}"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
