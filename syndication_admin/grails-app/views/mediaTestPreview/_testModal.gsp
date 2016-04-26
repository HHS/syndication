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
    <script type="text/javascript">
        $(document).ready(function(){
            prettyPrint()
        });
        
        $("#urlModal").on("click", function(){
            $("#spinnerDiv").show();
            $sourceUrl=$("#sourceUrl").val();
            $.ajax({
                data: {sourceUrl:$sourceUrl},
                type: 'GET',
                url: '${g.createLink(controller: 'mediaTestPreview', action: 'urlTestModal')}',
                success: function(response){
                    $("#spinnerDiv").fadeOut("fast");
                    $("#myModal").html(response);
                }
            });
        });
    </script>
    <style>
    .mobile-preview{
        border:1px solid black;
        overflow: auto;
        max-width:100%;
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

<div class="modal-dialog modal-lg">
    <div class="modal-content">
        <div class="modal-header" style="background-color: #F5F5F5">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title default-color3" id="myModalLabel">Url Test</h4>
        </div>
        <div class="modal-body">

            <div class="row">
                <div class="col-sm-11">
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

            <p>* If the remote server is unreachable the system will retry five times. This can take up to thirty seconds.</p>

            <div class="row">
                <div class="col-sm-8 col-sm-offset-2">
                    <fieldset>
                        <!-- Text input-->
                        <div class="form-group">
                            <div class="row">
                                <label class="col-md-2 control-label" for="sourceUrl">Source URL:</label>
                                <div class="col-md-9">
                                    <input id="sourceUrl" disabled="disabled" name="sourceUrl" type="text" placeholder="Enter the URL of the media to test" class="form-control input-md" value=${sourceUrl}>
                                </div>
                                <div id="spinnerDiv" style="width:50px;display: none;" class="col-md-1">
                                    <i class="fa fa-refresh fa-spin fa-lg"></i>
                                </div>
                            </div>
                        </div>
                    </fieldset>

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
        <div class="modal-footer" style="background-color: #F5F5F5">
            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
    </div>
</div>