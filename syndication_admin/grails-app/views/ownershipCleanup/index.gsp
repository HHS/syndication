<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 2/13/15
  Time: 3:10 PM
--%>

<%@ page import="com.ctacorp.syndication.media.MediaItem" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>

<body>
<g:form>
    <g:textField name="query" value="${query}"/>
    <g:actionSubmit value="search" action="search"/>
    <g:actionSubmit value="listAll" action="list"/>
    <g:select name="subscriber" from="${subscribers}" optionValue="name" optionKey="id"/>
    <g:actionSubmit value="associateOwner" action="associate"/>
</g:form>
<hr/>
<p>Items found: ${mediaItems?.size()} of ${com.ctacorp.syndication.media.MediaItem.count()}</p>
<g:each in="${mediaItems}" var="mi">
    <span style="display: block;">${mi.id} - ${mi.sourceUrl}</span>
</g:each>
<g:if test="${!mediaItems}">
    No media items found that are not already owned.
</g:if>
</body>
</html>