<%@ page import="grails.util.Holders" %>
<!DOCTYPE html>
%{--
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
--}%
<html>
    <head>
        <title>HHS Media Services API Docs</title>
        <link href='//fonts.googleapis.com/css?family=Droid+Sans:400,700' rel='stylesheet' type='text/css'/>
        <asset:stylesheet src="swagger/screen.css"/>
        <asset:javascript src="swagger/lib/swagger.js"/>

        <script type="application/javascript">
        $(function () {
            window.swaggerUi = new SwaggerUi({
                url: "${createLink(controller: 'swagger', action:'api')}",
                dom_id: "swagger-ui-container",
                supportedSubmitMethods: ['get', 'post', 'put', 'delete'],
                onComplete: function (swaggerApi, swaggerUi) {
                    if (console) {
                        console.log("Loaded SwaggerUI")
                    }
                    $('pre code').each(function (i, e) {
                        hljs.highlightBlock(e)
                    });
                },
                onFailure: function (data) {
                    if (console) {
                        console.log("Unable to Load SwaggerUI");
                        console.log(data);
                    }
                },
                docExpansion: "none"
            });

            $('#input_apiKey').change(function () {
                var key = $('#input_apiKey')[0].value;
                console.log("key: " + key);
                if (key && key.trim() != "") {
                    console.log("added key " + key);
                    window.authorizations.add("key", new ApiKeyAuthorization("api_key", key, "header"));
                }
            });
            window.swaggerUi.load();
        });
        </script>

        <style type="text/css">
            .extraFooter .features{
                margin-left: 35px;
            }

            .extraFooter ul li{
                list-style-type: disc;
                margin-bottom: 10px;
            }

            .extraFooter ul li li{
                list-style-type: circle;
                margin-left: 20px;
                margin-bottom: 0px;
            }

            .example{
                display: inline-block;
                width:200px;
            }

            .description{
                display: inline-block;
            }

            #header{
                height:73px;
                width:972px;
                background-image: url(${assetPath(src:'swagger/syndication_header.png')});
                background-repeat: no-repeat;
                padding:20px 0px 0px 25px;
            }
            #header span{
                /*vertical-align: bottom;*/
                /*display: inline-block;*/
            }
            #footer{
                width:997px;
                padding:14px;
                height:59px;
            }
            #header, #footer{
                margin-left: auto;
                margin-right: auto;
                margin-top:25px;
            }

        </style>
    </head>
    <body>
        <g:render template="gtm"/>
        <div id='header'>
            <div class="header">
                <span><br/><a id="logo" href="${grails.util.Holders.config.API_SERVER_URL}">HHS Media Services API</a></span>
            </div>
        </div>

        <div id="message-bar" class="swagger-ui-wrap">
            &nbsp;
        </div>

        <div id="swagger-ui-container" class="swagger-ui-wrap"></div>
        <br/>
        <div class="swagger-ui-wrap">
            <p style="font-family: 'Droid Sans', sans-serif">
                A markdown formatted documentation page is available <g:link controller="swagger" action="docs">here</g:link>. Raw Markdown can be obtained by adding the 'raw=true' query param, or by using this link: <g:link controller='swagger' action='docs' params='[raw:true]'>${createLink(controller: 'swagger', action: 'docs', params: [raw:true], absolute: true)}</g:link>.
            </p>
        </div>

        <div id="footer">
            <g:render template="footer"/>
        </div>
    </body>
</html>
