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
    <g:set var="entityName" value="Date Ranges"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
    <style>
        label.padded{
            width: 50px;
        }
    </style>
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
                <div class="row">
                    <form>
                    <div class="col-md-6">
                        <h4>Select a Time Range</h4>
                        <div class="form-group">
                            <label for="fromDay" class="padded">From:</label>
                            <g:datePicker name="fromDay" precision="day" class="form-control" relativeYears="[-10..10]" value="${fromDay}"/>
                            <br/>
                            <label for="toDay" class="padded">To:</label>
                            <g:datePicker name="toDay" precision="day" class="form-control" relativeYears="[-10..10]" value="${toDay}"/>
                            <g:actionSubmit action="mediaRangeViewMetrics" class="btn btn-success btn-xs" value="Apply Range"/>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="pull-right-md">
                            <div class="pull-right-md" style="margin-bottom: 10px;">
                                <g:link action="mediaRangeViewMetrics" class="btn-sm btn-info" params="[rangePreset: 'week' , sort:params.sort?:'id', order:params.order?:'asc', max:params.max ?: 10]">Past Week</g:link>
                                <g:link action="mediaRangeViewMetrics" class="btn-sm btn-info" params="[rangePreset: 'month', sort:params.sort?:'id', order:params.order?:'asc', max:params.max ?: 10]">Past Month</g:link>
                                <g:link action="mediaRangeViewMetrics" class="btn-sm btn-info" params="[rangePreset: 'year' , sort:params.sort?:'id', order:params.order?:'asc', max:params.max ?: 10]">Past Year</g:link>
                                <g:link action="mediaRangeViewMetrics" class="btn-sm btn-info" params="[rangePreset: 'ytd'  , sort:params.sort?:'id', order:params.order?:'asc', max:params.max ?: 10]">Year to Date</g:link>
                            </div>
                            <div class="hidden-sm hidden-xs"><br/><br/><br/></div>
                            <label for="max">Max records to display</label>
                            <g:select name="max" from="[10,50,100,250,500,1000]" value="${params.max}"/>
                            <g:actionSubmit action="mediaRangeViewMetrics" class="btn btn-default btn-xs" name="Apply Limit" value="Apply Limit"/>
                        </div>
                    </div>
                    </form>
                </div>
                <div class="row">
                    <div class="col-lg-12">
                        <div class="table-responsive" style="margin-top:5px;">
                            <table class="table table-striped table-bordered table-hover">
                                <thead>
                                <tr>
                                    <g:sortableColumn class="idTables" params="[toDay:toDay?.time, fomDay:fromDay?.time]" property="id" title="${message(code: 'mediaItem.id.label', default: 'ID')}"/>

                                    <g:sortableColumn property="name" params="[toDay:toDay?.time, fromDay:fromDay?.time]" title="${message(code: 'mediaItem.name.label', default: 'Name')}"/>

                                    <g:sortableColumn property="storefrontViewCount" params="[toDay:toDay?.time, fromDay:fromDay?.time]" title="${message(code: 'mediaMetric.apiViewCount.label', default: 'Storefront Views')}"/>

                                    <g:sortableColumn property="apiViewCount" params="[toDay:toDay?.time, fromDay:fromDay?.time]" title="${message(code: 'mediaMetric.apiViewCount.label', default: 'Api Views')}"/>
                                </tr>
                                </thead>
                                <tbody>
                                <g:each in="${mediaItemInstanceList}" status="i" var="mediaItemInstance">
                                    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                                        <td>${mediaItemInstance?.id}</td>

                                        <td><g:link controller="mediaItem" action="show" id="${mediaItemInstance.id}">${fieldValue(bean: mediaItemInstance, field: "name")}</g:link></td>

                                        <g:if test="${MediaMetric.findAllByDayBetweenAndMedia(fromDay, toDay, mediaItemInstance)?.storefrontViewCount}">
                                            <td>${MediaMetric.findAllByDayBetweenAndMedia(fromDay, toDay, mediaItemInstance)?.storefrontViewCount?.sum() ?: 0}</td>
                                        </g:if>
                                        <g:else>
                                            <td>0</td>
                                        </g:else>
                                        <g:if test="${MediaMetric.findAllByDayBetweenAndMedia(fromDay, toDay, mediaItemInstance)?.apiViewCount}">
                                            <td>${MediaMetric.findAllByDayBetweenAndMedia(fromDay, toDay, mediaItemInstance)?.apiViewCount?.sum() ?: 0}</td>
                                        </g:if>
                                        <g:else>
                                            <td>0</td>
                                        </g:else>

                                    </tr>
                                </g:each>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <g:if test="${!mediaItemInstanceList}">
                    <div class="row">
                        <div class="col-md-12">
                            <strong>There are no recorded metrics for this date range.</strong>
                        </div>
                    </div>
                </g:if>
                <g:if test="${mediaItemInstanceCount > mediaItemInstanceList.size()}">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="pagination">
                                <g:paginate total="${mediaItemInstanceCount ?: 0}" params="[fromDay:fromDay?.time, toDay: toDay?.time]"/>
                            </div>
                        </div>
                    </div>
                </g:if>
                <div class="row">
                    <div class="col-md-12">
                        <br/>
                        <p>* If sorting by view count, items with 0 views for the specified day are omitted.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
