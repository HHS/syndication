<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 11/12/14
  Time: 4:08 PM
--%>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="dashboard"/>
  <title>Health Reports - Ignored Media Items</title>
</head>
<body>
<div id="page-wrapper">
  <div class="row">
    <div class="col-lg-12">
      <h1 class="page-header">Health Reports</h1>
    </div>
  </div>

  <g:if test='${flash.message}'>
      <div class="alert alert-info alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        ${flash.message}
      </div>
  </g:if>

  <div class="progress">
    <div class="progress-bar progress-bar-success" title = "${stableItems} stable items" style="width: ${percentStable}%">
      <span class="">${stableItems} Stable Items</span>
    </div>
    <div class="progress-bar progress-bar-warning" title="${ignoredCount} ignored items" style="width: ${percentIgnored}%">
      <span class="">${ignoredCount} Ignored Items</span>
    </div>
    <div class="progress-bar progress-bar-danger" title="${flaggedCount} flagged items" style="width: ${percentFlagged}%">
      <span class="">${flaggedCount} Flagged Items</span>
    </div>
  </div>

  <ul class="nav nav-pills">
    <li role="presentation"><g:link action="index">Flagged Media</g:link></li>
    <li role="presentation" class="active"><g:link action="ignored">Ignored Media</g:link></li>
      <li role="presentation"><g:link action="search">Search Media</g:link></li>
  </ul>
  <br/>

  <div class="panel panel-default">
    <div class="panel-heading">
      <span>Ignored Media Items</span>
      <span class="pull-right"><strong>Total Items: ${totalCount}</strong></span>
    </div>
    <g:render template="mediaList"/>
  </div>
  <g:if test="${totalCount > 10}">
    <div class="pagination">
      <g:paginate total="${totalCount}"/>
    </div>
  </g:if>
</div>
</body>
</html>