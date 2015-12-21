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
        <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER">
            <li><a href="#sourceTab" role="tab" data-toggle="tab">Sources</a></li>
        </sec:ifAnyGranted>
        <li><a href="#campaignTab" role="tab" data-toggle="tab">Campaigns</a></li>
        <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER">
            <li><a href="#tagTab" role="tab" data-toggle="tab">Tags</a></li>
        </sec:ifAnyGranted>
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

        <div class="row tab-pane" id="sourceTab">
            <div class="col-xs-12 col-sm-12 col-md-8 col-lg-6">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-map-marker fa-fw"></i> Re-index Source</h3>
                    </div>
                    <div class="panel-body">
                        <p>Begin typing the name of a source, then select a match from the dropdown list. Add as many items as desired.</p>
                        <fieldset>
                            <div class="form-group">
                                <form id="sourceSearchForm" action="${g.createLink(controller:'contentIndex', action: 'reIndexSource')}">
                                    <g:textField name="sourceIds" id="sourceSearchField" class="form-control"/>
                                    <br>
                                    <button type="submit" id="reindexBySourceNameSearch" class="btn btn-warning btn-block">Re-index Source By Name</button>
                                </form>
                            </div>
                        </fieldset>

                        <h4 class="text-center">OR</h4>

                        <p>Supply a single source ID, or a list of source IDs delimited by commas.</p>
                        <fieldset>
                            <div class="form-group">
                                <form id="sourceIdListForm" action="${g.createLink(controller:'contentIndex', action: 'reIndexSource')}">
                                    <g:textField name="sourceIds" style="width:100%" class="form-control"/>
                                    <br>
                                    <button type="submit" id="reindexBySourceIdList" class="btn btn-warning btn-block">Re-index Source by ID[s]</button>
                                </form>
                            </div>
                         </fieldset>
                    </div>
                </div>
            </div>
        </div>

        <div class="row tab-pane" id="campaignTab">
            <div class="col-xs-12 col-sm-12 col-md-8 col-lg-6">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-flag fa-fw"></i> Re-index Campaign</h3>
                    </div>
                    <div class="panel-body">
                        <p>Begin typing the name of a campaign, then select a match from the dropdown list. Add as many items as desired.</p>
                        <fieldset>
                            <div class="form-group">
                                <form id="campaignSearchForm" action="${g.createLink(controller:'contentIndex', action: 'reIndexCampaign')}">
                                    <g:textField name="campaignIds" id="campaignSearchField" class="form-control"/>
                                    <br>
                                    <button type="submit" id="reindexByCampaignNameSearch" class="btn btn-warning btn-block">Re-index Campaign By Name</button>
                                </form>
                            </div>
                        </fieldset>

                        <h4 class="text-center">OR</h4>

                        <p>Supply a single campaign ID, or a list of campaign IDs delimited by commas.</p>
                        <fieldset>
                            <div class="form-group">
                                <form id="campaignIdListForm" action="${g.createLink(controller:'contentIndex', action: 'reIndexCampaign')}">
                                    <g:textField name="campaignIds" style="width:100%" class="form-control"/>
                                    <br>
                                    <button type="submit" id="reindexByCampaignIdList" class="btn btn-warning btn-block">Re-index Campaign By ID[s]</button>
                                </form>
                            </div>
                        </fieldset>
                    </div>
                </div>
            </div>
        </div>

        <div class="row tab-pane" id="tagTab">
            <div class="col-xs-12 col-sm-12 col-md-8 col-lg-6">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title"><i class="fa fa-tags fa-fw"></i>  Re-index Tag</h3>
                    </div>
                    <div class="panel-body">
                        <div class="pull-right">
                            <label for="language">Language:</label>
                            <select name="language" id="language">
                                <g:each in="${languages}" var="language">
                                    <option value="${language.id}">${language.name}</option>
                                </g:each>
                            </select>
                            <span   style="margin-right:10px;">&nbsp;</span>
                            <label for="tagType">Type:</label>
                            <select name="tagType" id="tagType" style="width:20%">
                                <g:each in="${tagTypes}" var="tagType">
                                    <option value="${tagType.id}">${tagType.name}</option>
                                </g:each>
                            </select>
                        </div>
                        <p>Begin typing the name of a tag, then select a match from the dropdown list. Add as many items as desired.</p>
                        <fieldset>
                            <div class="form-group">
                                <form id="tagSearchForm" action="${g.createLink(controller:'contentIndex', action: 'reIndexTag')}">
                                    <div id="tokenHolder">
                                        <g:textField name="tagIds" id="tagSearchField" class="form-control"/>
                                    </div>
                                    <br>
                                    <button type="submit" id="reindexByTagNameSearch" class="btn btn-warning btn-block">Re-index Tag By Name</button>
                                </form>
                            </div>
                        </fieldset>

                        <h4 class="text-center">OR</h4>

                        <p>Supply a single tag ID, or a list of tag IDs delimited by commas.</p>
                        <fieldset>
                            <div class="form-group">
                                <form id="tagIdListForm" action="${g.createLink(controller:'contentIndex', action: 'reIndexTag')}">
                                    <g:textField name="tagIds" style="width:100%" class="form-control"/>
                                    <br>
                                    <button type="submit" id="reindexByTagIdList" class="btn btn-warning btn-block">Re-index Tag By ID[s]</button>
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
                    <fieldset>
                        <!-- Change this to a button or input when using this as a form -->
                        <g:link controller="contentIndex" action="reindexAllMedia" class="btn btn-danger" onclick="return confirm('Are you sure? This could take a long time.')"><i class="fa fa-sitemap fa-fw"></i> All Media</g:link>
                        <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER">
                            <g:link controller="contentIndex" action="reindexAllSource" class="btn btn-danger" onclick="return confirm('Are you sure? This could take a long time.')"><i class="fa fa-map-marker fa-fw"></i> All Sources</g:link>
                        </sec:ifAnyGranted>
                        <g:link controller="contentIndex" action="reindexAllCampaign" class="btn btn-danger" onclick="return confirm('Are you sure? This could take a long time.')"><i class="fa fa-flag fa-fw"></i> All Campaigns</g:link>
                        <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER">
                            <g:link controller="contentIndex" action="reindexAllTags" class="btn btn-danger" onclick="return confirm('Are you sure? This could take a long time.')"><i class="fa fa-tags fa-fw"></i> All Tags</g:link>
                        </sec:ifAnyGranted>
        </fieldset>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>