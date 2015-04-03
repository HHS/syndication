<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 2/23/15
  Time: 1:44 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>

<body>
    <span><g:link action="index">Accounts</g:link></span> -> <span>${accountName} (${accountId})</span>
    <ul>
        <g:each in="${webProperties}" var="webProperty">
            <li>Name: ${webProperty.name}
                <ul>
                    <li>ID: <g:link action="profiles" params="[accountId:accountId, accountName:accountName, webPropertyName:webProperty.name, webPropertyId:webProperty.id]">${webProperty.id}</g:link></li>
                </ul>
            </li>
        </g:each>
</body>
</html>