<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="grails.util.Holders" contentType="text/html;charset=UTF-8" %>
<head>
    <meta name="layout" content="storefront"/>
    <title>Embed code for Tag: ${tagName}</title>
</head>
<body>
<g:set var="dateFormat" value="${"EEEE, MMMM dd, yyyy 'at' hh:mm aa"}"/>
<g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
</g:if>
<a name="pageContent"></a>
<h2>Embedcode for Tag: ${tagName}</h2>
<div>
    <g:render template="livePreview"/>
</div>
<g:render template="showContentScripts"/>
</body>
</html>