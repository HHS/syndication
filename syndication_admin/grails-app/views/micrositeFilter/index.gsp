<%--
  Created by IntelliJ IDEA.
  User: nburk
  Date: 11/25/15
  Time: 3:12 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="dashboard"/>
    <title>Health Reports - Flagged Microsites</title>
</head>

<body>
    <div id="page-wrapper">
        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">Microsite Reports</h1>
            </div>
        </div>

        <g:if test='${flash.message}'>
            <div class="col alert alert-info alert-dismissable">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                ${flash.message}
            </div>
        </g:if>

        <g:link action="checkAllMicrosites" onclick="return confirm('Are you sure? This can take several hours to run.');"><button class="btn btn-danger pull-right">Check All Microsites</button></g:link>

        <ul class="nav nav-pills">
            <li role="presentation" class="active"><g:link action="index">Flagged Microsites</g:link></li>
            <li role="presentation"><g:link action="ignored">Ignored Microsites</g:link></li>
        </ul>

        <br/>
        <div class="panel panel-danger">
            <div class="panel-heading">
                <span>Flagged Microsites</span>
                <span class="pull-right"><strong>Total Items: ${totalCount}</strong></span>
            </div>
            <g:render template="micrositeList"/>
        </div>
        <g:if test="${totalCount > 10}">
            <div class="pagination">
                <g:paginate total="${totalCount}"/>
            </div>
        </g:if>
    </div>
</body>
</html>