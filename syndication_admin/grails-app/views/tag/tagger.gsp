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
    <title>Tag List</title>
    <meta name="layout" content="dashboard"/>
    <asset:javascript src="tokenInput/jquery.tokeninput.js"/>
    <asset:stylesheet src="tokenInput/token-input.css"/>
    <g:javascript>
        $(document).ready(function () {
           $("#mediaIds").tokenInput("${g.createLink(controller: 'tag', action: 'mediaSearch')}");
           $("#tagIds").tokenInput("${g.createLink(controller: 'tag', action: 'tagSearch')}");

             $(".tagSpecifics").change(function () {
            var tagLangId = $("#languageId").val();
            var tagTypeId = $("#tagTypeId").val();
            updateTokenInput(tagTypeId, tagLangId);
        })

        function updateTokenInput(tagTypeId, languageId){
            $("#tokenHolder").html('<g:textField name="tagIds"/>')

            $.getJSON("${g.createLink(controller: 'tag', action: 'getTagsForSyndicationId')}.json" + "?tagTypeId="+tagTypeId+"&languageId="+languageId, function(data){
                console.log(data);
                $("#tagIds").tokenInput("${g.createLink(controller: 'tag', action: 'tagSearch')}.json" + "?tagTypeId="+tagTypeId+"&languageId="+languageId, {
                    prePopulate:data
                });
            });
        }
        });
    </g:javascript>
</head>

<body>
<div id="page-wrapper">
    <div class="row">
        <div class="col-md-12">
            <h1 class="page-header">Tag Media</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-8">
            <synd:message/>
            <synd:hasError/>
            <synd:errors/>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Tag Media Items</h3>
                </div>

                <div class="panel-body">
                    <g:form action="tagItems">
                        <label for="mediaIds">Select media items</label>
                        <g:textField name="mediaIds" id="mediaIds"/>
                        <br/>

                        <label for="tagIds">Select tags</label>
                        <div id="tokenHolder">
                            <g:textField name="tagIds"/>
                        </div>
                        <br/>
                        <div class="row">
                            <div class="col-md-6 col-lg-6">
                                <div class="form-group">
                                    <label class="col-md-4" for="languageLabel">Language</label>
                                    <div class="col-md-12">
                                        <g:select name="languageId" value="${languageId}" from="${languages}" optionKey="id" optionValue="name" class="form-control tagSpecifics"/>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6 col-lg-6">
                                <div class="form-group">
                                    <label class="col-md-4" for="typeLabel">Type</label>
                                    <div class="col-md-12">
                                        <g:select name="tagTypeId" value="${tagTypeId}" from="${tagTypes}" optionKey="id" optionValue="name" class="form-control tagSpecifics"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <br>

                        <g:submitButton name="Tag Items" id="tagButton"
                                        class="btn btn-default btn-lg btn-success pull-right"/>
                    </g:form>
                </div>
            </div>
        </div>
        <div class="col-lg-4">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <h1 class="panel-title">How to use Tagger</h1>
                </div>
                <div class="panel-body">
                    <ul>
                        <li>Look up one or more media items to tag</li>
                        <li>Look up one or more tags</li>
                        <li>Click "Tag Items"</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>