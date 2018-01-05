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
    <g:set var="entityName" value="${message(code: 'twitterAccount.label', default: 'Twitter Account')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>
<body>
<div id="list-twitterAccount" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:hasError/>

    <div class="row">
        <div class="col-lg-12">
            <!-- /.panel-heading -->
            <div class="panel panel-info">
                <div class="table-responsive">
                    <table class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>

                            <g:sortableColumn class="idTables" property="id" title="${message(code: 'twitterAccount.id.label', default: 'Id')}"/>

                            <g:sortableColumn property="accountName" title="${message(code: 'twitterAccount.accountName.label', default: 'Account Name')}"/>

                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${twitterAccountInstanceList}" status="i" var="accountInstance">
                            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                                <td>${accountInstance?.id}</td>

                                <td><g:link action="show" id="${accountInstance.id}"><span class="limited-width-md ellipse">${fieldValue(bean: accountInstance, field: "accountName")}</span></g:link></td>

                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                </div>
            </div>

            <g:if test="${total > twitterAccountInstanceList.size()}">
                <div class="pagination">
                    <g:paginate total="${total}"/>
                </div>
            </g:if>

        </div>
    </div>
</div>
</body>
</html>