<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 2/23/15
  Time: 1:55 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title></title>
</head>
<body>
<span><g:link action="index">Accounts</g:link></span> -> <span><g:link action="webProperties" params="[accountId:accountId, accountName:accountName]">${accountName} (${accountId})</g:link></span> -> <span>${webPropertyName} (${webPropertyId})</span>
<ul>
    <g:each in="${profiles.items}" var="profile">
        <li>${profile.name}
            <ul>
                <li>id: <g:link action="query" params="[accountId:accountId, accountName:accountName, webPropertyName:webPropertyName, webPropertyId:webPropertyId, profileId:profile.id, profileName:profile.name]">${profile.id}</g:link></li>
            </ul>
        </li>
    </g:each>
</ul>
</body>
</html>