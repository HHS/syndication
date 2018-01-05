<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 11/12/14
  Time: 12:41 PM
--%>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="dashboard"/>
    <title>Health Reports - Flagged Media Items</title>
</head>

<body>
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Health Reports</h1>
        </div>
    </div>

    <g:if test='${flash.message}'>
        <div class="col alert alert-info alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
            ${flash.message}
        </div>
    </g:if>
    <g:form class="deleteAllForm form-horizontal" controller="healthReport" action="search">
        <g:render template="searchOptions"/>

        <ul class="nav nav-pills">
            <li role="presentation"><g:link action="index">Flagged Media</g:link></li>
            <li role="presentation"><g:link action="ignored">Ignored Media</g:link></li>
            <li role="presentation" class="active"><g:link action="search">Search Media</g:link></li>
        </ul>

        <br/>
        <div class="panel panel-danger">
            <div class="panel-heading">
                <span>Flagged Media Items</span>
                <span class="pull-right"><strong>Total Items: ${totalCount}</strong></span>
            </div>
            <g:render template="searchMediaList"/>
        </div>
        <g:if test="${totalCount > 10}">
            <div class="pagination">
                <g:paginate total="${totalCount}" params="[url:params.url,
                                                          name:params.name,
                                                          id:params.id,
                                                          mediaType:params.mediaType,
                                                          subscriberId:params.subscriberId,
                                                          sourceId:params.sourceId,
                                                          max: params.max]"/>
            </div>
        </g:if>
    </g:form>
</div>
</body>
</html>