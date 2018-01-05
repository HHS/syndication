<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="x-ua-compatible" content="IE=edge">
    <title>Swagger UI</title>
    <link rel="icon" type="image/png" href="images/favicon-32x32.png" sizes="32x32" />
    <link rel="icon" type="image/png" href="images/favicon-16x16.png" sizes="16x16" />
    <asset:stylesheet src="swagger/typography.css" media="screen"/>
    <asset:stylesheet src="swagger/reset.css" media="screen"/>
    <asset:stylesheet src="swagger/screen.css" media="screen"/>
    <asset:stylesheet src="swagger/reset.css" media="print"/>
    <asset:stylesheet src="swagger/print.css" media="print"/>

    <asset:javascript src="swagger/lib/object-assign-pollyfill.js"/>
    <asset:javascript src="swagger/lib/jquery-1.8.0.min.js"/>
    <asset:javascript src="swagger/lib/jquery.slideto.min.js"/>
    <asset:javascript src="swagger/lib/jquery.wiggle.min.js"/>
    <asset:javascript src="swagger/lib/jquery.ba-bbq.min.js"/>
    <asset:javascript src="swagger/lib/handlebars-4.0.5.js"/>
    <asset:javascript src="swagger/lib/lodash.min.js"/>
    <asset:javascript src="swagger/lib/backbone-min.js"/>
    <asset:javascript src="swagger/swagger-ui.js"/>
    <asset:javascript src="swagger/lib/highlight.9.1.0.pack.js"/>
    <asset:javascript src="swagger/lib/highlight.9.1.0.pack_extended.js"/>
    <asset:javascript src="swagger/lib/jsoneditor.min.js"/>
    <asset:javascript src="swagger/lib/marked.js"/>
    <asset:javascript src="swagger/lib/swagger-oauth.js"/>

    <!-- Some basic translations -->
    <!-- <script src='lang/translator.js' type='text/javascript'></script> -->
    <!-- <script src='lang/ru.js' type='text/javascript'></script> -->
    <!-- <script src='lang/en.js' type='text/javascript'></script> -->
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
    <script type="text/javascript">
        $(function () {
            var url = window.location.search.match(/url=([^&]+)/);
            if (url && url.length > 1) {
                url = decodeURIComponent(url[1]);
            } else {
                url = "${syndication_server}/swagger.json";
            }

            hljs.configure({
                highlightSizeThreshold: 5000
            });

            // Pre load translate...
            if(window.SwaggerTranslator) {
                window.SwaggerTranslator.translate();
            }
            window.swaggerUi = new SwaggerUi({
                url: url,
                dom_id: "swagger-ui-container",
                supportedSubmitMethods: ['get', 'post', 'put', 'delete', 'patch'],
                onComplete: function(swaggerApi, swaggerUi){
                    if(typeof initOAuth == "function") {
                        initOAuth({
                            clientId: "your-client-id",
                            clientSecret: "your-client-secret-if-required",
                            realm: "your-realms",
                            appName: "your-app-name",
                            scopeSeparator: " ",
                            additionalQueryStringParams: {}
                        });
                    }

                    if(window.SwaggerTranslator) {
                        window.SwaggerTranslator.translate();
                    }
                },
                onFailure: function(data) {
                    log("Unable to Load SwaggerUI");
                },
                docExpansion: "none",
                jsonEditor: false,
                defaultModelRendering: 'schema',
                showRequestHeaders: false,
                showOperationIds: false
            });

            window.swaggerUi.load();

            function log() {
                if ('console' in window) {
                    console.log.apply(console, arguments);
                }
            }
        });
    </script>
</head>

<body class="swagger-section">
    <g:render template="/swagger/gtm"/>
    <div id='header'>
        <div class="swagger-ui-wrap">
            <div class="header">
                <span><br/><a id="logo" href="${grails.util.Holders.config.API_SERVER_URL}">HHS Media Services API</a></span>
            </div>
        </div>
    </div>

    <div id="message-bar" class="swagger-ui-wrap">&nbsp;</div>
    <div id="swagger-ui-container" class="swagger-ui-wrap"></div>
    <br/>
    <div class="swagger-ui-wrap">
        <p style="font-family: 'Droid Sans', sans-serif">
            A markdown formatted documentation page is available <g:link controller="swagger" action="docs">here</g:link>. Raw Markdown can be obtained by adding the 'raw=true' query param, or by using this link: <g:link controller='swagger' action='docs' params='[raw:true]'>${createLink(controller: 'swagger', action: 'docs', params: [raw:true], absolute: true)}</g:link>.
        </p>
    </div>

    <div id="footer">
        <g:render template="/swagger/footer"/>
    </div>
</body>
</html>
