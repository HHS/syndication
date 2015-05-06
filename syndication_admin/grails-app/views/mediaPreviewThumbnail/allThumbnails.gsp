<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 4/20/15
  Time: 3:39 PM
--%>

<%@ page import="com.ctacorp.syndication.media.MediaItem" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Thumbnail Overview</title>
    <meta name="layout" content="main">
</head>

<body>
    <div class="pagination">
        <g:paginate total="${com.ctacorp.syndication.media.MediaItem.count()}" action="allThumbnails"/>
    </div>
    <br/>
    <br/>
    <g:form>
        <g:actionSubmit value="Flush Cache" action="flushCache" class="btn btn-info"/>
        <g:actionSubmit value="Generate Missing" action="generateMissing" class="btn btn-warning"/>
        <g:actionSubmit value="Regenerate All" action="regenerateThumbnailPreviewForAllMedia" class="btn btn-danger"/>
        <g:hiddenField name="max" value="${params.max ?: 100}"/>
        <g:hiddenField name="offset" value="${params.offset ?: 0}"/>
    </g:form>
    <br/>
    <div>
        <g:each in="${mediaItems}" var="mi">
            <div style="display:inline-block; border:1px solid black; margin-bottom: 4px;">
                <p>${mi.id}</p>
                <div id="thumb_${mi.id}">
                    <img src="${grails.util.Holders.config.syndication.serverUrl}/api/v2/resources/media/${mi.id}/thumbnail.jpg"/>
                </div>
                <div style="padding: 5px;">
                    <g:remoteLink class="btn btn-info center-block" action="regenerateThumbnailPreviewForSingleItem" id="${mi.id}" update="thumb_${mi.id}">Regenerate</g:remoteLink>
                </div>
            </div>
        </g:each>
    </div>
    <br/>
    <br/>
    <div class="pagination">
        <g:paginate class="pagination" total="${com.ctacorp.syndication.media.MediaItem.count()}" action="allThumbnails"/>
    </div>

</body>
</html>