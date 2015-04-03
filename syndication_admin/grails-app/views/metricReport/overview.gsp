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
    <g:set var="entityName" value="Overview"/>
    <title>Overview</title>
    <style type="text/css">
        .searchQuery {
            word-wrap: break-word;
        }

        .searchQuery span{
            font-style: italic;
        }
    </style>
</head>

<body>
<div id="list-mediaMetric" class="content scaffold-list" role="main">
    <g:render template="header" model="[entityName:entityName]"/>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <div class="row">
        <div class="col-lg-4">
            <h4>Most Popular Queries</h4>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h1 class="panel-title">
                        Top Ten
                    </h1>
                </div>

                <div class="panel-body">
                    <div class="btn-group btn-group-sm btn-group-justified" role="group" aria-label="...">
                        <g:link action="overview" params="[popularRange:'week']"    class="btn btn-default${popularRange == 'week' ?' active':''}">Week</g:link>
                        <g:link action="overview" params="[popularRange:'month']"   class="btn btn-default${popularRange == 'month' ?' active':''}">Month</g:link>
                        <g:link action="overview" params="[popularRange:'year']"    class="btn btn-default${popularRange == 'year' ?' active':''}">Year</g:link>
                        <g:link action="overview" params="[popularRange:'alltime']" class="btn btn-default${popularRange == 'alltime' ?' active':''}">All Time</g:link>
                    </div>
                    <br/>
                    <ul class="list-group">
                        <g:each in="${mostPopular}" var="popularQuery">
                            <li class="searchQuery list-group-item"><strong>${popularQuery.searchCount} Times</strong><br/><span>${popularQuery.query}</span>
                            </li>
                        </g:each>
                    </ul>
                </div>
            </div>

        </div>

        <div class="col-lg-4">
            <h4>Subscribers</h4>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h1 class="panel-title">
                        Total Subscribers
                    </h1>
                </div>

                <div class="panel-body">
                    ${subscribers.size()}
                </div>
            </div>
            %{--<div class="panel panel-primary">--}%
                %{--<div class="panel-heading">--}%
                    %{--<h1 class="panel-title">--}%
                        %{--Subscribers by Domain--}%
                    %{--</h1>--}%
                %{--</div>--}%

                %{--<div class="panel-body">--}%
                    %{--Placeholder - we don't collect this information in the user account--}%
                %{--</div>--}%
            %{--</div>--}%
        </div>

        <div class="col-lg-4">
            <h4>Google Analytics Overview</h4>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h1 class="panel-title">
                        Google Analytics
                    </h1>
                </div>

                <div class="panel-body">
                    <label for="start">Since&nbsp;</label>
                    <g:form action="overview">
                        <g:datePicker name="start" precision="day" years="${(new java.util.GregorianCalendar().get(Calendar.YEAR))..2006}" value="${start?:new Date()-30}"/>
                        <g:submitButton name="lookup" value="Go" class="btn btn-xs btn-success"/>
                    </g:form>
                    <br/>
                    <g:if test="${googleOverview?.error}">
                        <div class="alert alert-danger" role="alert">
                            <i class="fa fa-exclamation-circle"></i>
                            <span class="sr-only">Error:</span>
                            ${googleOverview.error}
                        </div>
                    </g:if>
                    <g:else>
                        <g:each in="${googleOverview?.columnHeaders}" var="title" status="i">
                            <li class="searchQuery list-group-item">
                                <strong>${title.name - 'ga:'}</strong><br/>
                                <g:if test="${title.name == 'ga:avgTimeOnPage'}">
                                    <span>${googleOverview?.rows[0][i].split("\\.")[0]} Seconds</span>
                                </g:if>
                                <g:else>
                                    <span>${googleOverview?.rows[0][i]}</span>
                                </g:else>
                            </li>
                        </g:each>
                    </g:else>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
