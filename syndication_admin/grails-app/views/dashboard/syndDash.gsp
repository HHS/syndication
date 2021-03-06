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
    <title>Syndication Dashboard</title>
    <meta name="layout" content="dashboard"/>
    <asset:javascript src="plugins/morris/raphael-2.1.0.min.js"/>
    <asset:javascript src="plugins/morris/morris.js"/>
    <asset:stylesheet src="plugins/morris/morris-0.4.3.min.css"/>

    <style>
        .timeline_thumbnail{
            max-width: 100%;
        }
    </style>
</head>

<body>
<div id="page-wrapper">
<div class="row">
    <div class="col-lg-12">
        <h1 class="page-header">Dashboard</h1>
    </div>
    <!-- /.col-lg-12 -->
</div>
<synd:message/>
<synd:hasError/>
<!-- /.row -->
<div class="row">
<div class="col-lg-8">
    <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER">
        <div class="panel panel-default">
            <div class="panel-heading">
                <i class="fa fa-bar-chart-o fa-fw"></i> <span id="areaChartLabel">Showing content by 'Date Syndication Captured'</span>
                <div class="pull-right">
                    <div class="btn-group">
                        <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                            Date Selector
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu pull-right" role="menu">
                            <li><a href="#" id="areaDateSelectorSyndicationCaptured"    class="areaDateSelector">Syndication Captured</a></li>
                            <li><a href="#" id="areaDateSelectorSyndicationUpdated"     class="areaDateSelector">Syndication Updated</a></li>
                            <li><a href="#" id="areaDateSelectorContentAuthored"        class="areaDateSelector">Content Authored</a></li>
                            <li><a href="#" id="areaDateSelectorContentUpdated"         class="areaDateSelector">Content Updated</a></li>
                            <li><a href="#" id="areaDateSelectorContentPublished"       class="areaDateSelector">Content Published</a></li>
                            <li><a href="#" id="areaDateSelectorContentReviewed"        class="areaDateSelector">Content Reviewed</a></li>
                        </ul>
                    </div>
                </div>
            </div>
            <!-- /.panel-heading -->
            <div class="panel-body">
                <div id="contentByAgencyArea"></div>
            </div>
            <!-- /.panel-body -->
        </div>
    </sec:ifAnyGranted>
    <sec:ifAnyGranted roles="ROLE_PUBLISHER">
        <div class="panel panel-default">
            <div class="panel-heading">
                <i class="fa fa-bar-chart-o fa-fw"></i> <span id="areaPublisherChartLabel">Showing content by 'Date Syndication Captured'</span>
                <div class="pull-right">
                    <div class="btn-group">
                        <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                            Date Selector
                            <span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu pull-right" role="menu">
                            <li><a href="#" id="areaDateSelectorSyndicationCaptured"    class="areaPublisherDateSelector">Syndication Captured</a></li>
                            <li><a href="#" id="areaDateSelectorSyndicationUpdated"     class="areaPublisherDateSelector">Syndication Updated</a></li>
                            <li><a href="#" id="areaDateSelectorTotal"        class="areaPublisherDateSelector">Total Items</a></li>
                        </ul>
                    </div>
                </div>
            </div>
            <!-- /.panel-heading -->
            <div class="panel-body">
                <div id="publisherContentArea"></div>
            </div>
            <!-- /.panel-body -->
        </div>
    </sec:ifAnyGranted>
    <!-- /.panel -->
    <div class="panel panel-default visible-lg">
        <g:render template="timeline"/>
    </div>
<!-- /.panel -->
</div>
<!-- /.col-lg-8 -->
<div class="col-lg-4">
    <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER">
        <div class="panel panel-default">
            <div class="panel-heading">
                <i class="fa fa-bell fa-fw"></i> Notifications Panel
            </div>
            <!-- /.panel-heading -->
            <g:render template="recentEvents"/>
            <!-- /.panel-body -->
        </div>
        <!-- /.panel -->
        <div class="panel panel-default">
            <div class="panel-heading">
                <i class="fa fa-bar-chart-o fa-fw"></i> # of Media Items by Agency
            </div>

            <div class="panel-body">
                <div id="contentByAgencyDonut"></div>
                <!--
                <a href="#" class="btn btn-default btn-block">View Details</a>
                -->
            </div>
            <!-- /.panel-body -->
        </div>
    </sec:ifAnyGranted>
    <!-- /.panel -->
    <div class="panel panel-default">
        <div class="panel-heading">
            <i class="fa fa-bar-chart-o fa-fw"></i> # of Media Items by Type
        </div>

        <div class="panel-body">
            <div id="contentDistributionDonut"></div>
            <!--
            <a href="#" class="btn btn-default btn-block">View Details</a>
            -->
        </div>
        <!-- /.panel-body -->
    </div>
    <!-- /.panel -->
</div>

<div class="hidden-lg">
    <g:render template="timeline"/>
</div>

<!-- /.col-lg-4 -->
</div>
<!-- /.row -->
</div>
<!-- /#page-wrapper -->

<g:render template="scripts"/>
</body>
</html>