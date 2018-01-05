<%--
  Created by IntelliJ IDEA.
  User: nburk
  Date: 3/16/16
  Time: 10:33 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'cacheBuster.label', default: 'Cache Buster')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
    <div class="content scaffold-list" role="main">
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
                                <g:sortableColumn class="idTables" property="id" title="${message(code: 'cacheBuster.id.label', default: 'Id')}"/>

                                <g:sortableColumn property="domainName" title="${message(code: 'cacheBuster.name.label', default: 'Domain Name')}"/>

                            </tr>
                            </thead>
                            <tbody>
                            <g:each in="${cacheBusterList}" status="i" var="cacheBusterInstance">
                                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                                    <td>${cacheBusterInstance?.id}</td>

                                    <td><g:link action="show" id="${cacheBusterInstance.id}"><span class="limited-width-md ellipse">${fieldValue(bean: cacheBusterInstance, field: "domainName")}</span></g:link></td>

                                </tr>
                            </g:each>
                            </tbody>
                        </table>
                    </div>
                </div>

                <g:if test="${cacheBusterInstanceCount > params.max}">
                    <div class="pagination">
                        <g:paginate total="${cacheBusterInstanceCount ?: 0}"/>
                    </div>
                </g:if>

            </div>
        </div>

    </div>

</body>
</html>