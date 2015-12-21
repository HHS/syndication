<%--
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
--%>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Syndicated Media Preview & Extraction Testing</title>
    <meta name="layout" content="dashboard"/>
    <asset:stylesheet src="prettify/prettify.css"/>
    <asset:javascript src="prettify/prettify.js"/>
    <script type="text/javascript">
        $(document).ready(function(){
            prettyPrint()
        })
    </script>
    <style>
        .mobile-preview{
            border:1px solid black;
            overflow: auto;
            padding: 5px;
        }

        img{
            max-width: 100%;
        }

        .mobile-size-320x568{
            width: 320px;
            height: 568px;
        }

        .mobile-size-768x1024{
            width: 768px;
            height: 1024px;
        }

        .mobile-size-320x480{
            width: 320px;
            height: 480px;
        }

        .mobile-size-360x640{
            width: 360px;
            height: 640px;
        }

        /*Temporary workaround for proper rendering in firefox*/
        .mobile-size-320x568  .pull-right{
            float: none !important;
        }
        .mobile-size-320x480 .pull-right{
            float: none !important;
        }
        .mobile-size-360x640 .pull-right{
            float: none !important;
        }

    </style>
</head>

<body>
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Syndicated Media Preview & Extraction Testing</h1>
        </div>
    </div>

    <g:if test='${flash.message}'>
        <div class="alert alert-info alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
            ${flash.message}
        </div>
    </g:if>

    <g:if test='${flash.error}'>
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
            ${flash.error}
        </div>
    </g:if>

    <div class="row">
        <div class="col-lg-8 col-lg-offset-2">
            <form class="form-horizontal" action="index">
                <fieldset>
                    <!-- Text input-->
                    <div class="form-group">
                        <label class="col-md-2 control-label" for="sourceUrl">Source URL:</label>
                        <div class="col-md-10">
                            <input id="sourceUrl" name="sourceUrl" type="text" placeholder="Enter the URL of the media to test" class="form-control input-md" value=${sourceUrl}>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-2 col-md-offset-10">
                            <g:submitButton name="checkButton" value="Try it out!" class="btn btn-success pull-right"/>
                        </div>
                    </div>
                </fieldset>
            </form>

            <g:if test="${extractedContent}">
                <ul class="nav nav-pills" role="tablist">
                    <li role="presentation" class="active"><a href="#previewTab" role="tab" data-toggle="tab">Browser Preview</a></li>
                    <li role="presentation"><a href="#previewMobileTab" role="tab" data-toggle="tab">Mobile Preview</a></li>
                    <li role="presentation"><a href="#sourceTab" role="tab" data-toggle="tab">Raw Source</a></li>
                </ul>
                <br/>
                <div class="tab-content">
                    <div class="row tab-pane active" id="previewTab">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h1 class="panel-title">Extracted Content</h1>
                            </div>
                            <div class="panel-body">
                                ${extractedContent.encodeAsRaw()}
                            </div>
                        </div>
                    </div>
                    <div class="row tab-pane" id="previewMobileTab">
                        <div class="alert alert-info">Previews are constrained to mobile phone resolution in order of popularity.
                         5 pixel padding has been added to all previews, and images are constrained to 100% maximum to prevent cropping.
                         Exact look & feel will depend on your style sheets.</div>
                        <h3>320x568</h3>
                        <div class="mobile-preview mobile-size-320x568">${extractedContent.encodeAsRaw()}</div>
                        <h3>768x1024</h3>
                        <div class="mobile-preview mobile-size-768x1024">${extractedContent.encodeAsRaw()}</div>
                        <h3>320x480</h3>
                        <div class="mobile-preview mobile-size-320x480">${extractedContent.encodeAsRaw()}</div>
                        <h3>360x640</h3>
                        <div class="mobile-preview mobile-size-360x640">${extractedContent.encodeAsRaw()}</div>
                    </div>
                    <div class="row tab-pane" id="sourceTab">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h1 class="panel-title">Extracted Content</h1>
                            </div>
                            <div class="panel-body">
                                <pre class="prettyprint linenums">
                                    <code class="lang-html">
                                        ${extractedContent}
                                    </code>
                                </pre>
                            </div>
                        </div>
                    </div>
                </div>
            </g:if>
        </div>
    </div>
</div>
</body>
</html>