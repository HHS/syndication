<%@ page import="grails.util.Holders" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Swagger UI</title>
    <link rel="icon" type="image/png" href="vendor/swagger-ui/images/favicon-32x32.png" sizes="32x32" />
    <link rel="icon" type="image/png" href="vendor/swagger-ui/images/favicon-16x16.png" sizes="16x16" />
    <asset:stylesheet src="swagger-ui/css/typography.css" />
    <asset:stylesheet src="swagger-ui/css/reset.css" />
    <asset:stylesheet src="swagger-ui/css/screen.css" />
    <asset:stylesheet src="swagger-ui/css/reset.css" />
    %{--<asset:stylesheet src="swagger-ui/css/print.css" />--}%
    <asset:javascript src="swagger-ui/lib/jquery-1.8.0.min.js" />
    <asset:javascript src="swagger-ui/lib/jquery.slideto.min.js" />
    <asset:javascript src="swagger-ui/lib/jquery.wiggle.min.js" />
    <asset:javascript src="swagger-ui/lib/jquery.ba-bbq.min.js" />
    <asset:javascript src="swagger-ui/lib/handlebars-2.0.0.js" />
    <asset:javascript src="swagger-ui/lib/underscore-min.js" />
    <asset:javascript src="swagger-ui/lib/backbone-min.js" />
    <asset:javascript src="swagger-ui/swagger-ui.js" />
    <asset:javascript src="swagger-ui/lib/highlight.7.3.pack.js" />
    <asset:javascript src="swagger-ui/lib/jsoneditor.min.js" />
    <asset:javascript src="swagger-ui/lib/marked.js" />
    <asset:javascript src="swagger-ui/lib/swagger-oauth.js" />
    <!-- Some basic translations -->
    <!-- <script src='lang/translator.js' type='text/javascript'></script> -->
    <!-- <script src='lang/ru.js' type='text/javascript'></script> -->
    <!-- <script src='lang/en.js' type='text/javascript'></script> -->

    <script type="text/javascript">
        $(function () {
            var url = window.location.search.match(/url=([^&]+)/);
            if (url && url.length > 1) {
                url = decodeURIComponent(url[1]);
            } else {
                url = "${swaggerJsonPath}";
                //url="http://petstore.swagger.io/v2/swagger.json"
            }

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
                            scopeSeparator: ",",
                            additionalQueryStringParams: {}
                        });
                    }

                    if(window.SwaggerTranslator) {
                        window.SwaggerTranslator.translate();
                    }

                    $('pre code').each(function(i, e) {
                        hljs.highlightBlock(e)
                    });

                    addApiKeyAuthorization();
                },
                onFailure: function(data) {
                    log("Unable to Load SwaggerUI");
                },
                docExpansion: "none",
                jsonEditor: false,
                apisSorter: "alpha",
                defaultModelRendering: 'schema',
                showRequestHeaders: false
            });

            function addApiKeyAuthorization() {}

            window.swaggerUi.load();

            function log() {
                if ('console' in window) {
                    console.log.apply(console, arguments);
                }
            }
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

        .swagger-section #header, #footer {
            margin-left: auto;
            margin-right: auto;
            margin-top: 25px;
        }

        .swagger-section #header {
            height: 73px;
            width: 972px;
            background-image: url(${assetPath(src:'swagger/syndication_header.png')});
            background-repeat: no-repeat;
            padding: 20px 0px 0px 25px;
        }

        .swagger-section #header a#logo {
            font-size: 1.5em;
            font-weight: bold;
            text-decoration: none;
            background: url(${assetPath(src:'swagger/syndication_logo.png')}) no-repeat left center;
            padding: 20px 0 20px 60px;
            color: white;
        }

        .swagger-section #header {
            background-color: white;
        }

        #header span {
        }

        #footer{
            width:997px;
            padding:14px;
            height:59px;
        }

        /*--------------footer styles---------------*/
        .global-footer-links ul{
            display: inline-block;
            list-style-type: none;
            margin: 10px 0px 10px 25px;
        }

        .global-footer-links li{
            padding: 4px 0px;
        }

        .global-footer-links a:link, a:visited{
            text-decoration: none;
            color: #025d9c;
            font-size: 12px;
            font-family: Verdana, sans-serif;
        }

        .footer-right{
            width: 295px;
            float:right;
            padding-right: 5px;
            box-sizing: border-box;
        }

        .footer-left{
            float:left;
            padding-left: 5px;
            box-sizing: border-box;
            width: 702px;
        }

        .footer-connect-icons p {
            clear:both;

        }

        .gen-footer-container .footer-right .footer-connect-title{
            background-color:#cdd9e8;
            color:#333;
            min-height: 20px;
            padding: 20px;
            border-right: 1px solid #afaeb1;
        }

        .gen-footer-container .footer-right .footer-connect-title span{

            margin: 0;
            padding:0;
            font-size:14px;
            font-weight: bold;
            line-height: 14px;
            font-family:Verdana, Geneva, sans-serif;
            color: #333;
        }

        .gen-footer-container .footer-right .footer-connect-icons{
            padding: 20px 20px 27px;
            display: block;

            min-height: 143px;
            background:#f9f9f9 url(${assetPath(src:'footer/stay-connected-arrow-bg.png')}) no-repeat 27px 0px;
            border-right: 1px solid #afaeb1;
            border-bottom: 1px solid #afaeb1;
        }

        .gen-footer-container .footer-left .address{
            font-size:12px;
            font-family: Verdana, Geneva, sans-serif;
        }

        .gen-footer-container .footer-left .address{
            background-color:#cdd9e8;
            color:#333;
            min-height: 20px;
            padding: 20px;
            border-left: 1px solid #afaeb1;
        }

        .gen-footer-container .footer-left .global-footer-links{
            padding: 20px;
            min-height: 150px;
            border-left: 1px solid #afaeb1;
            border-bottom: 1px solid #afaeb1;
            background-color:#f9f9f9;
        }

        .addthis_32x32_style .dummy .at300bs, .addthis_32x32_style .at300bs,
        .addthis_32x32_style .at15t {
            background: url(../img/widget007.old.32.icons.png) no-repeat left
        }

        .footer-connect-icons .addthis_toolbox a {
            padding: 0 15px 15px 0;
            float: left;
        }

        .swagger-ui-wrap h3 {
            color: black;
            font-size: 1.1em;
            padding: 10px 0 10px 0;
        }

        .swagger-section .swagger-ui-wrap ul#resources li.resource div.heading h2 {
            color: #666666;
            padding-left: 0;
            display: block;
            clear: none;
            float: left;
            font-family: "Droid Sans", sans-serif;
            font-weight: bold;
        }

        .swagger-section .swagger-ui-wrap ul#resources li.resource div.heading h2 a {
            color: #666666;
        }

        .swagger-section .swagger-ui-wrap .markdown ul {
            list-style-type: disc;
        }

        .swagger-section .swagger-ui-wrap .markdown ul li ul li {
            list-style-type: circle;
            margin-left: 20px;
            margin-bottom: 0;
        }

        .swagger-section .swagger-ui-wrap .markdown ul li {
            padding: 0;
            line-height: 1;
            color: #333333;
        }

        .swagger-section .swagger-ui-wrap a {
            color: #0F6AB4;
        }

        .swagger-section .swagger-ui-wrap a:visited {
            text-decoration: none;
            color: #025d9c;
            font-size: 12px;
            font-family: Verdana, sans-serif;
        }
    </style>
</head>

<body class="swagger-section">
    <g:render template="gtm"/>
    <div id='header'>
        <div class="header">
            <span><br/><a id="logo" href="${Holders.config.API_SERVER_URL}">HHS Media Services API</a></span>
        </div>
    </div>

    <div id="message-bar" class="swagger-ui-wrap" data-sw-translate>&nbsp;</div>
    <div id="swagger-ui-container" class="swagger-ui-wrap"></div>

    <div id="footer">
        <g:render template="footer"/>
    </div>
</body>
</html>
