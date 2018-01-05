<%--
  Created by IntelliJ IDEA.
  User: nburk
  Date: 11/24/15
  Time: 9:21 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'registration.label', default: 'Microsite Registration')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<div id="list-registration" class="content scaffold-list" role="main">
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
                            <g:sortableColumn class="idTables" property="id" title="${message(code: 'registration.id.label', default: 'Id')}"/>

                            <g:sortableColumn property="organization" title="${message(code: 'registration.organization.label', default: 'organization')}"/>

                            <g:sortableColumn property="description" title="${message(code: 'registration.description.label', default: 'Description')}"/>

                            <g:sortableColumn property="verified" title="${message(code: 'registration.verified.label', default: 'Verified')}"/>

                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${registrationList}" status="i" var="registrationInstance">
                            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                                <td>${registrationInstance?.id}</td>

                                <td><g:link action="show" id="${registrationInstance.id}"><span class="limited-width-md ellipse">${fieldValue(bean: registrationInstance, field: "organization")}</span></g:link></td>

                                <td><span class="limited-width-lg ellipse abv60">${fieldValue(bean: registrationInstance, field: "description")}</span></td>

                                <td><span class="limited-width-lg ellipse abv60">${fieldValue(bean: registrationInstance, field: "verified")}</span></td>

                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                </div>
            </div>
            <g:if test="${registrationCount > max}">
                <div class="pagination">
                    <g:paginate total="${registrationCount ?: 0}"/>
                </div>
            </g:if>

        </div>
    </div>
</div>
</body>
</html>