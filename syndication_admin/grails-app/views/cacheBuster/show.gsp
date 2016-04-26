<%--
  Created by IntelliJ IDEA.
  User: nburk
  Date: 3/16/16
  Time: 11:52 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'cacheBuster.label', default: 'Cache Buster')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
<div class="container-fluid" role="main">
    <h1><g:message code="default.show.label" args="[entityName]" /></h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>
    <div class="row">
        <div class="col-sm-9 col-sm-offset-1">
            <dl class="dl-horizontal">

                <g:if test="${cacheBusterInstance?.id}">
                    <dt id="id-label" class="word_wrap"><g:message code="cacheBuster.id.label" default="Id" /></dt>
                    <dd class="word_wrap">${cacheBusterInstance?.id}</dd>
                </g:if>
                <g:if test="${cacheBusterInstance?.domainName}">
                    <dt id="name-label" class="word_wrap"><g:message code="cacheBuster.domainName.label" default="Domain Name" /></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${cacheBusterInstance}" field="domainName"/></dd>
                </g:if>
            </dl>
        </div>
    </div>
    <div class="col-sm-8">
        <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER">
            <g:form url="[resource:cacheBusterInstance, action:'delete']" method="DELETE">
                <fieldset class="buttons">
                    <g:link class="btn btn-success" action="edit" resource="${cacheBusterInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                    <g:actionSubmit class="btn btn-danger" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                    <g:link class="btn btn-default" action="index">
                        Cancel
                    </g:link>
                </fieldset>
            </g:form>
        </sec:ifAnyGranted>
    </div>
</div>
</body>
</html>