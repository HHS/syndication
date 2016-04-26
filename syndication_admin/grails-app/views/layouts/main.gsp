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
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="Grails"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <asset:javascript src="application.js"/>
    <asset:stylesheet src="sb-admin.css"/>
    <asset:stylesheet src="application.css"/>

    <g:layoutHead/>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>
<div id="wrapper">
    <g:render template="/dashboard/dashboardNavbar"/>
    <g:render template="/dashboard/dashboardLeftNav"/>
    <div id="page-wrapper">

        %{--Page Inner Nav--}%
        <a href="#list-html" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <br/>
        <sec:ifAnyGranted roles="ROLE_ADMIN">
            <g:if test="${!(params.controller in ["metricReport", "mediaPreviewThumbnail", "consumerMetrics"])}">
                <nav class="navbar navbar-default" role="navigation">
                    <div class="container-fluid">
                        <g:link class="navbar-brand" action="index"><i class="fa fa-list-ol"></i> List</g:link>
                        <g:if test="${params.controller == "tweet"}">
                            <g:link class="navbar-brand" action="importTweets"><i class="fa fa-cloud-download"></i> Import Tweets</g:link>
                            <g:link class="navbar-brand" controller="twitterAccount" action="create"><i class="fa fa-plus"></i> Add Twitter Account</g:link>
                        </g:if>
                        <g:else>
                        %{--don't show new button--}%
                            <g:if test="${params.controller != "registration"}">
                                <g:link class="navbar-brand" action="create"><i class="fa fa-plus"></i> New</g:link>
                            </g:if>
                        </g:else>
                        <g:if test="${params.controller in ["collection","html","image","infographic", "PDF", "tweet", "video", "questionAndAnswer", "FAQ"]}">
                            <g:link action="search" controller="mediaItem" params="[mediaType:mediaType]" class="navbar-brand"><i class="fa fa-search"></i> Search</g:link>
                        </g:if>
                    </div>
                </nav>
            </g:if>
        </sec:ifAnyGranted>
        <sec:ifAnyGranted roles="ROLE_PUBLISHER">
            <g:if test="${params.controller in ["campaign", "source", "collection","html","image","infographic", "PDF", "tweet","twitterAccount","twitterStatusCollector", "video", "questionAndAnswer", "FAQ"]}">
                <nav class="navbar navbar-default" role="navigation">
                    <div class="container-fluid">
                        <g:link class="navbar-brand" action="index"><i class="fa fa-list-ol"></i> List</g:link>
                        <g:if test="${params.controller in ["campaign", "collection", "html", "image", "infographic", "PDF", "twitterAccount", "twitterStatusCollector", "video", "questionAndAnswer", "FAQ"]}">
                            <g:link class="navbar-brand" action="create"><i class="fa fa-plus"></i> New</g:link>
                        </g:if>
                        <g:elseif test="${params.controller == "tweet"}">
                            <g:link class="navbar-brand" action="importTweets"><i class="fa fa-cloud-download"></i> Import Tweets</g:link>
                            <g:link class="navbar-brand" controller="twitterAccount" action="create"><i class="fa fa-plus"></i> Add Twitter Account</g:link>
                        </g:elseif>
                        <g:if test="${params.controller in ["collection","html","image","infographic", "PDF", "tweet", "video", "questionAndAnswer", "FAQ"]}">
                            <g:link action="search" controller="mediaItem" params="[mediaType:mediaType]" class="navbar-brand"><i class="fa fa-search"></i> Search</g:link>
                        </g:if>
                    </div>
                </nav>
            </g:if>
        </sec:ifAnyGranted>
        <sec:ifAnyGranted roles="ROLE_MANAGER">
            <g:if test="${params.controller in ["alternateImage", "campaign", "language", "extendedAttribute", "twitterAccount", "twitterStatusCollector", "source", "collection", "html", "image", "infographic", "PDF", "tweet", "video", "questionAndAnswer", "FAQ"]}">
                <nav class="navbar navbar-default" role="navigation">
                    <div class="container-fluid">
                        <g:if test="${params.controller == "tweet"}">
                            <g:link class="navbar-brand" action="index"><i class="fa fa-list-ol"></i> List</g:link>
                            <g:link class="navbar-brand" action="importTweets"><i class="fa fa-cloud-download"></i> Import Tweets</g:link>
                            <g:link class="navbar-brand" controller="twitterAccount" action="create"><i class="fa fa-plus"></i> Add Twitter Account</g:link>
                        </g:if>
                        <g:else>
                            <g:link class="navbar-brand" action="index"><i class="fa fa-list-ol"></i> List</g:link>
                            <g:link class="navbar-brand" action="create"><i class="fa fa-plus"></i> New</g:link>
                        </g:else>
                        <g:if test="${params.controller in ["collection","html","image","infographic", "PDF", "tweet", "video", "questionAndAnswer", "FAQ"]}">
                            <g:link action="search" controller="mediaItem" params="[mediaType:mediaType]" class="navbar-brand"><i class="fa fa-search"></i> Search</g:link>
                        </g:if>
                    </div>
                </nav>
            </g:if>
        </sec:ifAnyGranted>

        <g:layoutBody/>
        <div class="footer" role="contentinfo"></div>

        <div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
    </div>
</div>
</body>
</html>
