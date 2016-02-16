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
                        <g:link params="[popularRange:'week']"    class="btn btn-default${popularRange == 'week' ?' active':''}">Week</g:link>
                        <g:link params="[popularRange:'month']"   class="btn btn-default${popularRange == 'month' ?' active':''}">Month</g:link>
                        <g:link params="[popularRange:'year']"    class="btn btn-default${popularRange == 'year' ?' active':''}">Year</g:link>
                        <g:link params="[popularRange:'alltime']" class="btn btn-default${popularRange == 'alltime' ?' active':''}">All Time</g:link>
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
            <h4>Collaborators</h4>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h1 class="panel-title">
                        Totals
                    </h1>
                </div>

                <div class="panel-body">
                    <li class="list-group-item">
                        <strong>number of publishers</strong><br><span>${publishers.size()}</span>
                    </li>
                    <li class="list-group-item">
                        <strong>number of Subscribers</strong><br><span>${totalSubscribers}</span>
                    </li>
                    <li class="list-group-item">
                        <strong>number of Media Items</strong><br><span>${totalMediaItems}</span>
                    </li>
                    <li class="list-group-item clearfix">
                        <strong>number of Partner Domains</strong><br><span>${partnerDomains.size()}</span><input type="button" class="btn btn-info pull-right" value="View Domains" data-toggle="modal" data-target="#myModal"/>
                    </li>
                </div>
            </div>
        </div>
        <div class="col-lg-4">
            <h4>General</h4>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h1 class="panel-title">
                        Total Hits
                    </h1>
                </div>

                <div class="panel-body">
                    <div class="form-group">
                        <div class="row">
                            <div class="col-md-12">
                                <h4>Select a Time Range</h4>
                                <div class="form-group">
                                    <label for="fromDay" class="padded">From:</label>
                                    <g:datePicker name="fromDay" precision="day" class="form-control" relativeYears="[-10..10]" value="${fromDay}"/>
                                </div>
                                <div class="form-group">
                                    <label for="toDay" class="padded">To:</label>
                                    <g:datePicker name="toDay" precision="day" class="form-control" relativeYears="[-10..10]" value="${toDay}"/>
                                    <input type="button" class="btn btn-success btn-xs totalRange" value="Apply Range"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <ul class="list-group">
                        <li  class="searchQuery list-group-item">
                            <strong>Storefront</strong><br/><span id="storefrontTotal">0</span>
                        </li>
                        <li class="searchQuery list-group-item">
                            <strong>API</strong><br/><span id="apiTotal">0</span>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <label>Storefront/API Views </label> include metrics that we keep track of internally.
                </div>
            </div>
        </div>
    </div>
    <!-- Modal for partner domains -->
    <div id="myModal" class="modal fade" role="dialog">
        <div class="modal-dialog">

            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Partner Domains</h4>
                </div>
                <div class="modal-body">
                    <ul class="list-group">
                        <g:each in="${partnerDomains}" var="domain">
                            <li class="list-group-item">
                                <strong>${domain}</strong>
                            </li>
                        </g:each>
                    </ul>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>

        </div>
    </div>
</div>
<script>
    $(document).ready(function(){
        updateTotalHits();
    });


    $(".totalRange").on("click", function(){
        updateTotalHits();
    });

    function updateTotalHits(){
        $.ajax({
            method: "POST",
            url: "${g.createLink(controller: 'metricReport', action: 'generalOverviewAjax')}",
            data: {fromDay_day:document.getElementById("fromDay_day").value, fromDay_month:document.getElementById("fromDay_month").value, fromDay_year:document.getElementById("fromDay_year").value,
                toDay_day:document.getElementById("toDay_day").value, toDay_month:document.getElementById("toDay_month").value, toDay_year:document.getElementById("toDay_year").value},
            success: function(data) {
                document.getElementById("storefrontTotal").innerHTML = data.storefrontCount;
                document.getElementById("apiTotal").innerHTML = data.apiCount;
            }
        })
    }
</script>
</body>
</html>
