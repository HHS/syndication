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
    <g:set var="entityName" value="Dates"/>
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
            <g:if test="${!mediaItemInstanceList}">
                There are no views for any content on these day(s).
                <br>
                <br>
            </g:if>
            <g:else>
                <div class="table-responsive">
                    <table class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>

                            <g:sortableColumn class="idTables" params="[day:day?.time]" property="id" title="${message(code: 'mediaItem.id.label', default: 'ID')}"/>

                            <g:sortableColumn property="name" params="[day:day?.time]" title="${message(code: 'mediaItem.name.label', default: 'Name')}"/>

                            <g:sortableColumn property="storefrontViewCount" params="[day:day?.time]" title="${message(code: 'mediaMetric.apiViewCount.label', default: 'Storefront Views')}"/>

                            <g:sortableColumn property="apiViewCount" params="[day:day?.time]" title="${message(code: 'mediaMetric.apiViewCount.label', default: 'API Views')}"/>

                        </tr>
                        </thead>
                        <tbody>

                        <g:each in="${mediaItemInstanceList}" status="i" var="mediaItemInstance">
                            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                                <td>${mediaItemInstance?.id}</td>

                                <td><g:link controller="mediaItem" action="show" id="${mediaItemInstance.id}">${fieldValue(bean: mediaItemInstance, field: "name")}</g:link></td>

                                <g:if test="${MediaMetric.findByDayBetweenAndMedia(day, day + 1,mediaItemInstance)?.storefrontViewCount}">
                                    <td>${MediaMetric.findByDayBetweenAndMedia(day, day + 1,mediaItemInstance)?.storefrontViewCount ?: 0}</td>
                                </g:if>
                                <g:else>
                                    <td>0</td>
                                </g:else>
                                <g:if test="${MediaMetric.findByDayBetweenAndMedia(day, day + 1,mediaItemInstance)?.apiViewCount}">
                                    <td>${MediaMetric.findByDayBetweenAndMedia(day, day + 1,mediaItemInstance)?.apiViewCount ?: 0}</td>
                                </g:if>
                                <g:else>
                                    <td>0</td>
                                </g:else>

                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                </div>
            </g:else>

            <div class="row">
                <div class="col-md-6">
                    <div class="pagination">
                        <g:paginate total="${mediaItemInstanceCount ?: 0}" params="[day:day?.time]"/>
                    </div>
                </div>
                <div class="col-md-6 hidden-sm hidden-xs">
                    <div class="pull-right">
                        * If sorting by total views, than items with no views will not be shown.
                    </div>
                </div>
                <div class="col-md-6 hidden-lg hidden-md">
                    <br>
                    * If sorting by total views, than items with no views will not be shown.
                </div>
            </div>
                <div class="row">
                    <div class="col-md-6">
                        <g:form class="form" action="mediaViewMetrics">
                            <div class="form-group">
                                <h2>Select a Specific Day</h2>
                            </div>
                            <div class="form-group">
                                <g:datePicker name="day" precision="day" class="form-control" relativeYears="[-10..10]"  value="${day}"/>
                            </div>
                            <div class="form-group">
                                <g:actionSubmit action="mediaViewMetrics" class="btn btn-success" value="Submit Date"/>
                            </div>
                        </g:form>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
</html>
