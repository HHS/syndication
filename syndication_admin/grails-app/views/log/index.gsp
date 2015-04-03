<%--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
--%>


<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %><html>
<head>
    <title>System Log Viewer</title>
    <meta name="layout" content="dashboard"/>
    <asset:javascript src="logViewer.js"/>
    <asset:stylesheet src="logStyles.css"/>
</head>

<body>
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">System Log Viewer</h1>
        </div>
    </div>

    <g:if test='${flash.message}'>
        <div class="alert alert-info alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
            ${flash.message}
        </div>
    </g:if>

    <ul class="nav nav-tabs" role="tablist">
        <li class="active"><a id="adminTab" href="#adminLogs" role="tab" data-toggle="tab">Admin Dashboard</a></li>
        <li><a id="apiTab" href="#api" role="tab" data-toggle="tab">API</a></li>
        <li><a id="cmsTab" href="#cms" role="tab" data-toggle="tab">CMS Manager</a></li>
        <li><a id="storefrontTab" href="#storefront" role="tab" data-toggle="tab">Storefront</a></li>
        <li><a id="tinyTab" href="#tinyUrl" role="tab" data-toggle="tab">Tiny Url</a></li>
        <li><a id="tagTab" href="#tagCloud" role="tab" data-toggle="tab">Tag Cloud</a></li>
    </ul>

    <br/>

    <div class="tab-content">
        <div class="row tab-pane active" id="adminLogs">
            <div class="col-md-12">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <span class="panel-title">Admin Logs</span>
                        <span class="pull-right">
                            <button class="btn btn-info btn-xs" id="adminConstrainedScreenButton">Constrained</button>
                            <button class="btn btn-warning btn-xs" id="adminFullscreenButton">Unconstrained</button>
                        </span>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-12">
                                <div id="adminLogDisplay" class="logDisplayWindow logViewPanelConstrained"></div>
                            </div>
                        </div>
                        <br/>
                        <div class="row">
                            <div class="col-md-12">
                                <div class="pull-right">
                                    <ul class="nav nav-pills">
                                        <li class="active adminPill"><a href="#" id="adminErrorButton">Errors</a></li>
                                        <li class="adminPill"><a href="#" id="adminInfoButton">Details</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row tab-pane" id="api">
            <div class="col-md-12">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <span class="panel-title">API Logs</span>
                        <span class="pull-right">
                            <button class="btn btn-info btn-xs" id="apiConstrainedScreenButton">Constrained</button>
                            <button class="btn btn-warning btn-xs" id="apiFullscreenButton">Unconstrained</button>
                        </span>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-12">
                                <div id="apiLogDisplay" class="logDisplayWindow logViewPanelConstrained"></div>
                            </div>
                        </div>
                        <br/>
                        <div class="row">
                            <div class="col-md-12">
                                <div class="pull-right">
                                    <ul class="nav nav-pills">
                                        <li class="active apiPill"><a href="#" id="apiErrorButton">Errors</a></li>
                                        <li class="apiPill"><a href="#" id="apiInfoButton">Details</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row tab-pane" id="cms">
            <div class="col-md-12">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <span class="panel-title">CMS Manager Logs</span>
                        <span class="pull-right">
                            <button class="btn btn-info btn-xs" id="cmsConstrainedScreenButton">Constrained</button>
                            <button class="btn btn-warning btn-xs" id="cmsFullscreenButton">Unconstrained</button>
                        </span>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-12">
                                <div id="cmsLogDisplay" class="logDisplayWindow logViewPanelConstrained"></div>
                            </div>
                        </div>
                        <br/>
                        <div class="row">
                            <div class="col-md-12">
                                <div class="pull-right">
                                    <ul class="nav nav-pills">
                                        <li class="active cmsPill"><a href="#" id="cmsLogButton">Log</a></li>
                                        <li class="cmsPill"><a href="#" id="cmsApiKeyLogButton">Api Key Log</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row tab-pane" id="storefront">
            <div class="col-md-12">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <span class="panel-title">Storefront Logs</span>
                        <span class="pull-right">
                            <button class="btn btn-info btn-xs" id="storefrontConstrainedScreenButton">Constrained</button>
                            <button class="btn btn-warning btn-xs" id="storefrontFullscreenButton">Unconstrained</button>
                        </span>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-12">
                                <div id="storefrontLogDisplay" class="logDisplayWindow logViewPanelConstrained"></div>
                            </div>
                        </div>
                        <br/>
                        <div class="row">
                            <div class="col-md-12">
                                <div class="pull-right">
                                    <ul class="nav nav-pills">
                                        <li class="active storefrontPill"><a href="#" id="storefrontErrorButton">Errors</a></li>
                                        <li class="storefrontPill"><a href="#" id="storefrontInfoButton">Details</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row tab-pane" id="tinyUrl">
            <div class="col-md-12">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <span class="panel-title">TinyURL Logs</span>
                        <span class="pull-right">
                            <button class="btn btn-info btn-xs" id="tinyConstrainedScreenButton">Constrained</button>
                            <button class="btn btn-warning btn-xs" id="tinyFullscreenButton">Unconstrained</button>
                        </span>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-12">
                                <div id="tinyLogDisplay" class="logDisplayWindow logViewPanelConstrained"></div>
                            </div>
                        </div>
                        <br/>
                        <div class="row">
                            <div class="col-md-12">
                                <div class="pull-right">
                                    <ul class="nav nav-pills">
                                        <li class="active tinyPill"><a href="#" id="tinyErrorButton">Errors</a></li>
                                        <li class="tinyPill"><a href="#" id="tinyInfoButton">Details</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row tab-pane" id="tagCloud">
            <div class="col-md-12">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <span class="panel-title">Tag Cloud Logs</span>
                        <span class="pull-right">
                            <button class="btn btn-info btn-xs" id="tagConstrainedScreenButton">Constrained</button>
                            <button class="btn btn-warning btn-xs" id="tagFullscreenButton">Unconstrained</button>
                        </span>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-12">
                                <div id="tagLogDisplay" class="logDisplayWindow logViewPanelConstrained"></div>
                            </div>
                        </div>
                        <br/>
                        <div class="row">
                            <div class="col-md-12">
                                <div class="pull-right">
                                    <ul class="nav nav-pills">
                                        <li class="active tagPill"><a href="#" id="tagErrorButton">Errors</a></li>
                                        <li class="tagPill"><a href="#" id="tagInfoButton">Details</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <span><strong>Note:</strong> Logs are limited to the latest 1MB of logging information.</span>
</div>
</body>
</html>