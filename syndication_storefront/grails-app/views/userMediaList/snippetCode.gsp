<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ctacorp.syndication.storefront.UserMediaList" %>
<html>
<head>
    <meta name="layout" content="storefront"/>
    <title>Snippet for List</title>
</head>

<body>
<div id="home">
    <div id="home_left">
    </div>
    <g:render template="/storefront/rightBoxes" model="[featuredMedia: featuredMedia]"/>
</div>
</body>
</html>