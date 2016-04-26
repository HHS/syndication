<%--
  Created by IntelliJ IDEA.
  User: nburk
  Date: 3/16/16
  Time: 12:37 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'cacheBuster.label', default: 'Cache Buster')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>

<body>

<div class="content scaffold-edit" role="main">
    <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>
    <div class="row">
        <div class="col-md-8">
            <g:form class="form-horizontal" url="[resource:cacheBusterInstance, action:'update']" method="PUT" >
                <g:hiddenField name="version" value="${cacheBusterInstance?.version}" />
                <fieldset class="form">
                    <g:render template="form"/>
                </fieldset>
                <fieldset class="buttons">
                    <div class="form-group">
                        <label class="col-md-5 control-label" for="create"></label>
                        <div class="col-md-7">
                            <g:actionSubmit class="btn btn-success" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                            <g:link class="btn btn-default" resource="${cacheBusterInstance}" action="show">
                                Cancel
                            </g:link>
                        </div>
                    </div>
                </fieldset>
            </g:form>
        </div>
    </div>
</div>

</body>
</html>