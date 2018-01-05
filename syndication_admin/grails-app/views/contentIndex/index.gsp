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
    <title>Content Indexing</title>
    <meta name="layout" content="dashboard"/>
    <asset:javascript src="tokenInput/jquery.tokeninput.js"/>
    <asset:stylesheet src="tokenInput/token-input.css"/>

    <g:javascript>
        $(document).ready(function() {
            $("#mediaSearchField").tokenInput("${g.createLink(controller:'contentIndex', action: 'mediaSearch')}", {});

            $("#sourceSearchField").tokenInput("${g.createLink(controller:'contentIndex', action: 'sourceSearch')}", {});

            $("#campaignSearchField").tokenInput("${g.createLink(controller:'contentIndex', action: 'campaignSearch')}", {});


            $("#tagSearchField").tokenInput("${g.createLink(controller: 'tag', action: 'tagSearch')}.json" + "?tagTypeId=" + $("#tagType").val() + "&languageId=" + $("#language").val());

            $("#tagType").change(function() {
                var tagTypeIdValue = $("#tagType").val();
                var languageIdValue = $("#language").val();
                updateTokenInput(tagTypeIdValue, languageIdValue)
            });
            $("#language").change(function() {
                var tagTypeIdValue = $("#tagType").val();
                var languageIdValue = $("#language").val();
                updateTokenInput(tagTypeIdValue, languageIdValue)
            });
            function updateTokenInput(tagTypeIdValue, languageIdValue){
                $("#tokenHolder").html('<g:textField name="tagIds" id="tagSearchField" class="form-control"/>');
                $("#tagSearchField").tokenInput("${g.createLink(controller: 'tag', action: 'tagSearch')}.json" + "?tagTypeId=" + tagTypeIdValue + "&languageId=" + languageIdValue);
            }

        });

    </g:javascript>
</head>

<body>
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Content Indexing</h1>
        </div>
    </div>

    <g:if test='${flash.message}'>
        <div class="alert alert-info alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
            ${flash.message}
        </div>
    </g:if>

    <ul class="nav nav-tabs" role="tablist">
        <li class="active"><a href="#mediaTab" role="tab" data-toggle="tab">Media</a></li>

        %{--<li><a href="#campaignTab" role="tab" data-toggle="tab">Campaigns</a></li>--}%

    </ul>
    <br/>
    <div class="tab-content">
        <div class="row tab-pane active" id="mediaTab">
            <div class="col-xs-12 col-sm-12 col-md-8 col-lg-6">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-sitemap fa-fw"></i> Reindex Media</h3>
                    </div>
                    <div class="panel-body">
                        <p>Begin typing the name of a media item, then select a match from the dropdown list. Add as many items as desired.</p>
                        <fieldset>
                            <div class="form-group">
                                <form id="mediaSearchForm" action="${g.createLink(controller:'contentIndex', action: 'reindexMedia')}">
                                    <g:textField name="mediaIds" id="mediaSearchField" class="form-control"/>
                                    <br>
                                    <button type="submit" id="reindexByNameSearch" class="btn btn-warning btn-block">Re-index Media By Name</button>
                                </form>
                            </div>
                        </fieldset>

                        <h4 class="text-center">OR</h4>

                        <p>Supply a single media ID, or a list of media IDs delimited by commas.</p>
                        <fieldset>
                            <div class="form-group">
                                <form id="mediaIdListForm" action="${g.createLink(controller:'contentIndex', action: 'reindexMedia')}">
                                    <g:textField name="mediaIds" style="width:100%" class="form-control"/>
                                    <br>
                                    <button type="submit"  id="reindexByIdList" class="btn btn-warning btn-block">Re-index Media By ID[s]</button>
                                </form>
                            </div>
                        </fieldset>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <div class="row">
        <div class="col-xs-12 col-sm-12 col-md-8 col-lg-6">
            <div class="panel panel-danger">
                <div class="panel-heading">
                    <h3 class="panel-title">Re-index All</h3>
                </div>
                <div class="panel-body">
                <g:if test="${fullReindexRunning}">
                    <button class="btn btn-danger" disabled>
                </g:if>
                <g:else>
                    <button class="btn btn-danger">
                </g:else>
                        <g:link style="color: white;" controller="contentIndex" action="bulkReindex"><i class="fa fa-sitemap fa-fw"></i> Run</g:link>
                    </button>
                    <span style="padding-left: 20px;"><b>Status: </b>${fullReindexRunning ? 'Running since' : 'Last ran on'} ${lastJobExecutionTime}, <b>Count: </b>${itemCount}</span>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>