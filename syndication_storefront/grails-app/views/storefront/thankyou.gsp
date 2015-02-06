<%--
  Created by IntelliJ IDEA.
  User: Steffen Gates
  Date: 12/6/12
  Time: 10:58 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta content="storefront" name="layout"/>
    <title>Request Content Syndication - Thank You!</title>
</head>

<body>
<div id="home_left">
    <g:if test='${flash.message}'>
        <div class='message'>${flash.message}</div>
    </g:if>
    <div class="bluebox">
        <h3>Thank You!</h3>
        <br/>
        <p>An administrator has been notified of your request. An email will be sent to the address you provided with any updates.</p>
    </div>
</div>
<g:render template="rightBoxes"/>
</body>
</html>