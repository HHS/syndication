<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 4/8/15
  Time: 2:03 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>

<body>
    <g:form action="flushCache">
        <label>Flush All Caches:</label><g:submitButton name="Flush"/>
    </g:form>
    <hr/>
    <g:form action="flushCacheByName">
        <g:select name="cacheName" from="${['apiResponseCache','extractedContentCache','imageCache']}"/>
        <label>Flush Specific Cache:</label><g:submitButton name="Flush"/>
    </g:form>
</body>
</html>