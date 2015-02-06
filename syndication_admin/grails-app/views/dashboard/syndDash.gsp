%{--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Syndication Dashboard</title>
    <meta name="layout" content="dashboard"/>
    <asset:javascript src="/plugins/morris/raphael-2.1.0.min.js"/>
    <asset:javascript src="/plugins/morris/morris.js"/>
    <asset:stylesheet src="/plugins/morris/morris-0.4.3.min.css"/>
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
<synd:error/>
<!-- /.row -->
<div class="row">
<div class="col-lg-8">
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
                    %{--<li class="divider"></li>--}%
                    %{--<li><a href="#">Separated link</a>--}%
                    %{--</li>--}%
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
<!-- /.panel -->
<div class="panel panel-default visible-lg">
    <div class="panel-heading">
        <i class="fa fa-clock-o fa-fw"></i> Timeline
    </div>
    <!-- /.panel-heading -->
    <div class="panel-body">
        <ul class="timeline">
        <g:each in="${timelineEvents}" var="timelineEvent" status="i">
            <li class="${i%2==0?'':'timeline-inverted'}">
                <div class="timeline-badge info">
                    <synd:mediaIcon mediaType="${timelineEvent?.type}"/>
                </div>
                <div class="timeline-panel">
                    <div class="timeline-heading">
                        %{--TODO switch this to the real storefront URL--}%
                        %{--<h4 class="timeline-title"><a href="#">${timelineEvent.title}</a></h4>--}%
                        <h4 class="timeline-title" style="word-wrap: break-word">${timelineEvent?.title}</h4>
                        <p>
                            <small class="text-muted"><i class="fa fa-clock-o"></i> <prettytime:display date="${timelineEvent?.timestamp}" /></small>
                        </p>
                    </div>

                    <div class="timeline-body">
                        <p><synd:shortenString string="${timelineEvent?.message}"/></p>
                    </div>
                </div>
            </li>
        </g:each>
        </ul>
    </div>
    <!-- /.panel-body -->
</div>
<!-- /.panel -->
</div>
<!-- /.col-lg-8 -->
<div class="col-lg-4">
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
    %{--<div class="chat-panel panel panel-default">--}%
        %{--<div class="panel-heading">--}%
            %{--<i class="fa fa-comments fa-fw"></i>--}%
            %{--Chat--}%
            %{--<div class="btn-group pull-right">--}%
                %{--<button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">--}%
                    %{--<i class="fa fa-chevron-down"></i>--}%
                %{--</button>--}%
                %{--<ul class="dropdown-menu slidedown">--}%
                    %{--<li>--}%
                        %{--<a href="#">--}%
                            %{--<i class="fa fa-refresh fa-fw"></i> Refresh--}%
                        %{--</a>--}%
                    %{--</li>--}%
                    %{--<li>--}%
                        %{--<a href="#">--}%
                            %{--<i class="fa fa-check-circle fa-fw"></i> Available--}%
                        %{--</a>--}%
                    %{--</li>--}%
                    %{--<li>--}%
                        %{--<a href="#">--}%
                            %{--<i class="fa fa-times fa-fw"></i> Busy--}%
                        %{--</a>--}%
                    %{--</li>--}%
                    %{--<li>--}%
                        %{--<a href="#">--}%
                            %{--<i class="fa fa-clock-o fa-fw"></i> Away--}%
                        %{--</a>--}%
                    %{--</li>--}%
                    %{--<li class="divider"></li>--}%
                    %{--<li>--}%
                        %{--<a href="#">--}%
                            %{--<i class="fa fa-sign-out fa-fw"></i> Sign Out--}%
                        %{--</a>--}%
                    %{--</li>--}%
                %{--</ul>--}%
            %{--</div>--}%
        %{--</div>--}%
        %{--<!-- /.panel-heading -->--}%
        %{--<div class="panel-body">--}%
            %{--<ul class="chat">--}%
                %{--<li class="left clearfix">--}%
                    %{--<span class="chat-img pull-left">--}%
                        %{--<img src="http://placehold.it/50/55C1E7/fff" alt="User Avatar" class="img-circle"/>--}%
                    %{--</span>--}%

                    %{--<div class="chat-body clearfix">--}%
                        %{--<div class="header">--}%
                            %{--<strong class="primary-font">Jack Sparrow</strong>--}%
                            %{--<small class="pull-right text-muted">--}%
                                %{--<i class="fa fa-clock-o fa-fw"></i> 12 mins ago--}%
                            %{--</small>--}%
                        %{--</div>--}%

                        %{--<p>--}%
                            %{--Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur bibendum ornare dolor, quis ullamcorper ligula sodales.--}%
                        %{--</p>--}%
                    %{--</div>--}%
                %{--</li>--}%
                %{--<li class="right clearfix">--}%
                    %{--<span class="chat-img pull-right">--}%
                        %{--<img src="http://placehold.it/50/FA6F57/fff" alt="User Avatar" class="img-circle"/>--}%
                    %{--</span>--}%

                    %{--<div class="chat-body clearfix">--}%
                        %{--<div class="header">--}%
                            %{--<small class=" text-muted">--}%
                                %{--<i class="fa fa-clock-o fa-fw"></i> 13 mins ago</small>--}%
                            %{--<strong class="pull-right primary-font">Bhaumik Patel</strong>--}%
                        %{--</div>--}%

                        %{--<p>--}%
                            %{--Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur bibendum ornare dolor, quis ullamcorper ligula sodales.--}%
                        %{--</p>--}%
                    %{--</div>--}%
                %{--</li>--}%
                %{--<li class="left clearfix">--}%
                    %{--<span class="chat-img pull-left">--}%
                        %{--<img src="http://placehold.it/50/55C1E7/fff" alt="User Avatar" class="img-circle"/>--}%
                    %{--</span>--}%

                    %{--<div class="chat-body clearfix">--}%
                        %{--<div class="header">--}%
                            %{--<strong class="primary-font">Jack Sparrow</strong>--}%
                            %{--<small class="pull-right text-muted">--}%
                                %{--<i class="fa fa-clock-o fa-fw"></i> 14 mins ago</small>--}%
                        %{--</div>--}%

                        %{--<p>--}%
                            %{--Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur bibendum ornare dolor, quis ullamcorper ligula sodales.--}%
                        %{--</p>--}%
                    %{--</div>--}%
                %{--</li>--}%
                %{--<li class="right clearfix">--}%
                    %{--<span class="chat-img pull-right">--}%
                        %{--<img src="http://placehold.it/50/FA6F57/fff" alt="User Avatar" class="img-circle"/>--}%
                    %{--</span>--}%

                    %{--<div class="chat-body clearfix">--}%
                        %{--<div class="header">--}%
                            %{--<small class=" text-muted">--}%
                                %{--<i class="fa fa-clock-o fa-fw"></i> 15 mins ago</small>--}%
                            %{--<strong class="pull-right primary-font">Bhaumik Patel</strong>--}%
                        %{--</div>--}%

                        %{--<p>--}%
                            %{--Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur bibendum ornare dolor, quis ullamcorper ligula sodales.--}%
                        %{--</p>--}%
                    %{--</div>--}%
                %{--</li>--}%
            %{--</ul>--}%
        %{--</div>--}%
        %{--<!-- /.panel-body -->--}%
        %{--<div class="panel-footer">--}%
            %{--<div class="input-group">--}%
                %{--<input id="btn-input" type="text" class="form-control input-sm" placeholder="Type your message here..."/>--}%
                %{--<span class="input-group-btn">--}%
                    %{--<button class="btn btn-warning btn-sm" id="btn-chat">--}%
                        %{--Send--}%
                    %{--</button>--}%
                %{--</span>--}%
            %{--</div>--}%
        %{--</div>--}%
        %{--<!-- /.panel-footer -->--}%
    %{--</div>--}%
    <!-- /.panel .chat-panel -->
</div>

<div class="hidden-lg">
    <!-- /.panel -->
    <div class="panel panel-default">
        <div class="panel-heading">
            <i class="fa fa-clock-o fa-fw"></i> Timeline
        </div>
        <!-- /.panel-heading -->
        <div class="panel-body">
            <ul class="timeline">
                <g:each in="${timelineEvents}" var="timelineEvent" status="i">
                    <li class="${i%2==0?'':'timeline-inverted'}">
                        <div class="timeline-badge info">
                            <synd:mediaIcon mediaType="${timelineEvent?.type}"/>
                        </div>
                        <div class="timeline-panel">
                            <div class="timeline-heading">
                                %{--TODO switch this to the real storefront URL--}%
                                %{--<h4 class="timeline-title"><a href="#">${timelineEvent.title}</a></h4>--}%
                                <h4 class="timeline-title" style="word-wrap: break-word">${timelineEvent?.title}</h4>
                                <p>
                                    <small class="text-muted"><i class="fa fa-clock-o"></i> <prettytime:display date="${timelineEvent?.timestamp}" /></small>
                                </p>
                            </div>

                            <div class="timeline-body">
                                <p><synd:shortenString string="${timelineEvent?.message}"/></p>
                            </div>
                        </div>
                    </li>
                </g:each>
            </ul>
        </div>
        <!-- /.panel-body -->
    </div>
    <!-- /.panel -->
</div>

<!-- /.col-lg-4 -->
</div>
<!-- /.row -->
</div>
<!-- /#page-wrapper -->

%{--<script src="${resource(dir: 'js/syndDash', file: 'syndDash.js')}"></script>--}%
<g:render template="scripts"/>
</body>
</html>