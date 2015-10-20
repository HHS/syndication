<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 4/6/15
  Time: 4:09 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>

<body>
    <strong>Tag Media</strong>
    <g:form action="tagMedia">
        <g:submitButton name="tagMedia" value="Tag Media"/>
    </g:form>
    <hr/>
    <g:if test="${message}">
        <strong>${message}</strong>
    </g:if>
</body>
</html>