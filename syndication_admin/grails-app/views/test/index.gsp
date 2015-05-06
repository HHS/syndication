<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 11/12/14
  Time: 4:23 PM
--%>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ctacorp.syndication.media.MediaItem" %>
<html>
<head>
  <title></title>
</head>
<body>
  <g:if test="${flash.message}">
    <div style="color:deeppink">!!! ${flash.message} !!!</div>
    <hr/>
  </g:if>
<div>
    <g:form>
        <label>Generate all missing thumbnails</label>
        <g:actionSubmit value="Generate Missing" action="generateThumbnailPreviewForAllMedia"/>
        <g:actionSubmit value="Regenerate All" action="regenerateThumbnailPreviewForAllMedia"/>
    </g:form>
</div>
<hr/>
<div>
  <g:link controller="mediaPreviewThumbnail" action="allThumbnails">All Thumbnails</g:link>
</div>
</body>
</html>