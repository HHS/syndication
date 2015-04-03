<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 11/12/14
  Time: 4:23 PM
--%>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ctacorp.syndication.media.MediaItem" %>
<html>
<head>
  <title></title>
</head>
<body>
  <g:if test="${flash.message}">
    <div style="color:deeppink">!!! ${flash.message} !!!</div>
    <hr/>
  </g:if>
  <div>
  <g:form action="flagItems">
    <label>Flag some media items</label>
    <g:submitButton name="Flag!"/>
  </g:form>
  </div>
  <hr/>
  <div>
    <g:form action="testHealth">
      <label>Source URL:</label>
      <g:select from="${com.ctacorp.syndication.media.MediaItem.list()}" name="id" optionKey="id"/>
      <g:submitButton name="submit"/>
    </g:form>
  </div>
  <g:if test="${healthReport}">
    <div>
      <ul>
        <li>Valid: ${healthReport.valid}</li>
        <li>Failure type: ${healthReport.failureType}</li>
        <li>Status Code: ${healthReport.statusCode}</li>
        <li>Details: ${healthReport.details}</li>
      </ul>
    </div>
  </g:if>
  <div>
    <g:form action="allHealthReports">
      <g:submitButton name="Get All Health Reports"/>
    </g:form>
  </div>
  <g:if test="${healthReports}">
    <g:each in="${healthReports}" var="healthReport">
      <div>
        <ul>
          <li style="color:${healthReport.valid ? 'green' : 'red'}">Media Id: ${healthReport.mediaId}</li>
          <li style="color:${healthReport.valid ? 'green' : 'red'}">SourceURL: ${MediaItem.get(healthReport.mediaId).sourceUrl}</li>
          <li style="color:${healthReport.valid ? 'green' : 'red'}">Valid: ${healthReport.valid}</li>
          <li style="color:${healthReport.valid ? 'green' : 'red'}">Failure type: ${healthReport.failureType}</li>
          <li style="color:${healthReport.valid ? 'green' : 'red'}">Status Code: ${healthReport.statusCode}</li>
          <li style="color:${healthReport.valid ? 'green' : 'red'}">Details: ${healthReport.details}</li>
        </ul>
      </div>
      <hr/>
    </g:each>
  </g:if>
</body>
</html>