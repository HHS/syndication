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
    <title>Tag List</title>
    <meta name="layout" content="dashboard"/>

    <g:javascript>
        $(document).ready(function () {

            $(document).on('change', '#languageId', function(){
                updateTagList();
            });
            $(document).on('change', '#typeId', function(){
                updateTagList();
            });
            $(document).on('input', '#tagName', function(){
                updateTagList();
            });

            function updateTagList(){
                setTimeout(function () {
                    $article = $('#tagName').val();
                    $language = $('#languageId').val();
                    $typeId = $('#typeId').val();
                    $.ajax({ // create an AJAX call...
                        data: {tagName:$article,languageId:$language,typeId:$typeId}, // get the form data
                        type: 'POST', // GET or POST
                        url: '${g.createLink(controller: 'tag', action: 'search')}', // the file to call
                        success: function (response) { // on success..
                            $('#tagList').html(response); // update the DIV
                            $('#tagListTitle').html('Search Results');
                        }
                    });
                }, 300);

            }
        });
    </g:javascript>
</head>

<body>
<div id="page-wrapper">
    <div class="row">
        <div class="col-md-12">
            <h1 class="page-header" id="test">Browse Tags</h1>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-8">
            <synd:message/>
            <synd:error/>
            <synd:errors/>

            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Find Tag</h3>
                </div>

                <div class="panel-body">
                    <p>Begin typing the name of a tag and matching tags will be listed below</p>
                    <fieldset>
                        <div class="form-group">
                            <form id="tagSearchForm" action="${g.createLink(controller: 'tag', action: 'index')}">
                                <g:textField name="tagName" id="tagName" class="form-control"
                                             value="${prepopulatedTagName}"/>
                            </form>
                        </div>
                    </fieldset>
                    <g:link action="index">
                        <button type="button" class="btn btn-success pull-right">
                            <i class="fa fa-times-circle"></i> Reset
                        </button>
                    </g:link>
                </div>
            </div>


            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 id="tagListTitle" class="panel-title">Tag List</h3>
                </div>

                <div id="tagList" class="panel-body">
                    <g:render template="tagList"/>
                </div>
            </div>
        </div>

        <div class="col-lg-4">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <h3 class="panel-title">How to use Tag Listing</h3>
                </div>

                <div class="panel-body">
                    <ul>
                        <li>Browse existing tags with pagination buttons at the bottom of the page.</li>
                        <li>Sort tags by a particular property by clicking on the desired column.</li>
                        <li>To search for a tag, type it's name in the search box.</li>
                        <li>If a tag doesn't already exist, you can create one with the 'create' button.</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>