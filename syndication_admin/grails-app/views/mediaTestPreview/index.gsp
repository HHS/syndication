<%--
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Syndicated Media Preview & Extraction Testing</title>
    <meta name="layout" content="dashboard"/>
    <asset:stylesheet src="prettify/prettify.css"/>
    <asset:javascript src="prettify/prettify.js"/>
    <script type="text/javascript">

        function startProgressBar(){
            var $progress = $('.progress');
            var $progressBar = $('.progress-bar');

            setTimeout(function() {
                $progressBar.css('width', '15%');
                setTimeout(function() {
                    $progressBar.css('width', '30%');
                    setTimeout(function() {
                        $progressBar.css('width', '45%');
                        setTimeout(function() {
                            $progressBar.css('width', '60%');
                            setTimeout(function() {
                                $progressBar.css('width', '75%');
                                setTimeout(function() {
                                    $progressBar.css('width', '90%');
                                }, 4000); // WAIT 5 milliseconds
                            }, 4000); // WAIT 5 milliseconds
                        }, 5000); // WAIT 5 milliseconds
                    }, 4000); // WAIT 3 seconds
                }, 4000); // WAIT 3 seconds
            }, 4000); // WAIT 3 second
        }

        $(document).ready(function(){
            prettyPrint();

            $("#testUrl").on("click", function(){
                document.getElementById("testUrl").disabled = true;
                var $progress = $('.progress');
                var $progressBar = $('.progress-bar');
                $progressBar.css('width', '5%');
                $progress.css('display', 'block');
                startProgressBar();

                $("#spinnerDiv").show();
                $sourceUrl=$("#sourceUrl").val();
                $.ajax({
                    data: {sourceUrl:$sourceUrl},
                    type: 'GET',
                    url: '${g.createLink(controller: 'mediaTestPreview', action: 'urlTest')}',
                    success: function(response){
                        setTimeout(function() {
                            $progressBar.css('width', '100%');
                        }, 300); // WAIT 5 milliseconds
                        $("#spinnerDiv").fadeOut("fast");

                        setTimeout(function() {
                            $progress.css('display', 'none');
                            document.getElementById("testUrl").disabled = false;
                            $("#testResults").html(response);
                        }, 800); // WAIT 5 milliseconds

                    }
                });
            });
        });

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

    <p>* If the remote server is unreachable the system will retry five times. This can take up to thirty seconds.</p>
    <div class="progress" hidden>
        <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 5%;"></div>
    </div>
    <div id="spinnerDiv" style="width:50px;display: none;" class="col-md-1">
        <i class="fa fa-refresh fa-spin fa-lg"></i>
    </div>

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
                            <input type="button" id="testUrl" value="Try it out!" class="btn btn-success pull-right"/>
                        </div>
                    </div>
                </fieldset>
            </form>

        </div>
    </div>
    <div id="testResults">

    </div>
</div>
</body>
</html>