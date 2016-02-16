<%--
  Created by IntelliJ IDEA.
  User: nburk
  Date: 9/4/15
  Time: 7:40 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'twitterStatusCollector.label', default: 'Tweet Auto Importer')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>
<body>
<div id="list-twitterStatusCollector" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>

    <div class="row">
        <div class="col-lg-12">
            <!-- /.panel-heading -->
            <div class="panel panel-info">
                <div class="table-responsive">
                    <table class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>

                            <g:sortableColumn class="idTables" property="id" title="${message(code: 'twitterStatusCollector.id.label', default: 'Id')}"/>

                            <g:sortableColumn property="hashTags" title="${message(code: 'twitterStatusCollector.query.label', default: 'Hash Tags')}"/>

                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${twitterStatusCollectorInstanceList}" status="i" var="statusCollectorInstance">
                            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                                <td>${statusCollectorInstance?.id}</td>

                                <td><g:link action="show" id="${statusCollectorInstance.id}"><span class="limited-width-md ellipse">${fieldValue(bean: statusCollectorInstance, field: "hashTags")}</span></g:link></td>

                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                </div>
            </div>

            <g:if test="${total > twitterStatusCollectorInstanceList?.size()}">
                <div class="pagination">
                    <g:paginate total="${total}"/>
                </div>
            </g:if>
        </div>
        </div>
    </div>
</div>
</body>
</html>