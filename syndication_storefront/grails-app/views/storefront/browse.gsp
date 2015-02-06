<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="storefront"/>
    <title>Syndication - Browse by media type</title>
</head>

<body>
<div id="home_left">
    <div>
        <g:form action="browse">
            <g:select name="selectedMediaType" from="${mediaTypes}" value="${selectedMediaType}"/>
            <g:submitButton name="Filter"/>
        </g:form>
    </div>
    <div class="purplebox">
        <h3>${selectedMediaType} Media Items</h3>
        <g:render template="mediaList"/>
    </div>
</div>

<g:render template="rightBoxes"/>
</body>
</html>