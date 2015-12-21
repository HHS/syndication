<%--
  Created by IntelliJ IDEA.
  User: nburk
  Date: 12/16/15
  Time: 2:00 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="storefront"/>
    <title>Embed code for Source: ${sourceName}</title>
</head>

<body>
<g:set var="dateFormat" value="${"EEEE, MMMM dd, yyyy 'at' hh:mm aa"}"/>
<g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
</g:if>
<a name="pageContent"></a>
<h2>Embedcode for Source: ${sourceName}</h2>
<div>
    <g:render template="livePreview"/>
</div>
<g:render template="showContentScripts"/>
</body>
</html>