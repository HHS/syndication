<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 2/23/15
  Time: 1:30 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
</head>

<body>
<span>Accounts</span>
<ul>
    <g:each in="${accounts.items}" var="account">
        <li>${account.name}
            <ul><li><g:link action="webProperties" params="[accountId:account.id, accountName:account.name]">${account.id}</g:link></li>
                <g:each in="${account.permissions?.effective}" var="permission">
                    <li>${permission}</li>
                </g:each>
            </ul>
        </li>
    </g:each>
</ul>
</body>
</html>