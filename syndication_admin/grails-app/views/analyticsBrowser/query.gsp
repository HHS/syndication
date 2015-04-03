<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 2/23/15
  Time: 2:19 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title></title>
    <style>
        table, th, tr, td{
            border: 1px solid black;
        }
    </style>
</head>
<body>
<span><g:link action="index">Accounts</g:link></span> ->
<span><g:link action="webProperties" params="[accountId:accountId, accountName:accountName]">${accountName} (${accountId})</g:link></span> ->
<span><g:link action="profiles" params="[accountId:accountId, accountName:accountName, profileId:profileId, profileName:profileName, webPropertyName:webPropertyName, webPropertyId:webPropertyId]">${webPropertyName} (${webPropertyId})</g:link></span> ->
<span>${profileName} (${profileId})</span>
<br/>
<hr/>
<br/>
<g:form action="executeQuery">
    <label for="start">Start Date</label>
    <g:datePicker name="start" value="${start ?: new Date()-7}" years="${(new GregorianCalendar().get(Calendar.YEAR))..2006}"/><br/>
    <label for="end">End Date</label>
    <g:datePicker name="end" value="${end ?: new Date()}" years="${(new GregorianCalendar().get(Calendar.YEAR))..2006}"/><br/>
    <label for="queryField">Query:</label>
    <g:select name="query" from="['pageViews','overview']"/><br/>
    <g:hiddenField name="accountId" value="${accountId}"/>
    <g:hiddenField name="accountName" value="${accountName}"/>
    <g:hiddenField name="profileId" value="${profileId}"/>
    <g:hiddenField name="profileName" value="${profileName}"/>
    <g:hiddenField name="webPropertyId" value="${webPropertyId}"/>
    <g:hiddenField name="webPropertyName" value="${webPropertyName}"/>
    <g:submitButton name="execute"/>
</g:form>

<g:if test="${results}">
    %{--${results}--}%
    <hr/>
    <table>
        <tr>
        <g:each in="${results.columnHeaders}" var="title">
            <th>${title.name-'ga:'}</th>
        </g:each>
        </tr>
        <g:each in="${results.rows}" var="row">
            <tr>
            <g:each in="${row}" var="val">
                <td>${val}</td>
            </g:each>
            </tr>
        </g:each>
    </table>
</g:if>

</body>
</html>