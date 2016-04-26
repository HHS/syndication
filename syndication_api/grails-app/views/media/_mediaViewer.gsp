<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="en">
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
<head>
    <meta charset="UTF-8">
    <title>HHS Storefront Media Viewer</title>
    <base target="_blank">
    <asset:stylesheet src="mediaViewer/main.css"/>
    <asset:stylesheet src="slick/slick.css"/>
    <asset:stylesheet src="slick/slick-theme.css"/>
    <asset:javascript src="mediaViewer/main.js"/>
</head>
<body>
    <div id="HHSSFMV"><!-- Parent Div Open-->
        <div class="slick-container">
            <g:each in="${mediaItemContent}" var="mediaItem">
                <div class="${mediaItem.meta.getClass().simpleName.toLowerCase()}">
                    ${mediaItem.content.encodeAsRaw()}
                </div>
            </g:each>
        </div>

        <div class="slick-navigation">
            <g:each in="${mediaItemContent}" var="mediaItem">
                <div><img src="${grailsApplication.config.syndication.serverUrl}/api/v2/resources/media/${mediaItem.meta.id}/thumbnail.jpg" alt="Thumbnail for media item ${mediaItem.meta.name}"></div>
            </g:each>
        </div>

    </div><!-- Parent Div Stop Closed-->
    <script type="application/javascript">
        $(document).ready(function () {
            HHSSFMV.setup();
        })
    </script>
</body>
</html>