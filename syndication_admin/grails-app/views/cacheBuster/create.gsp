<%--
  Created by IntelliJ IDEA.
  User: nburk
  Date: 3/16/16
  Time: 10:54 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'cacheBuster.label', default: 'Cache Buster')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>

<body>
    <div class="content scaffold-create" role="main">
        <h1><g:message code="default.create.label" args="[entityName]" /></h1>
        <synd:message/>
        <synd:errors/>
        <synd:hasError/>
        <div class="row">

            <div class="col-md-8">
                <g:form class="form-horizontal" url="[resource:cacheBuster, action:'save']" >
                    <fieldset class="form">
                        <g:render template="form"/>
                    </fieldset>
                    <fieldset class="buttons">
                        <div class="form-group">
                            <label class="col-md-5 control-label" for="create"></label>
                            <div class="col-md-7">
                                <g:submitButton name="create" class="btn btn-success" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                                <g:link class="btn btn-default" action="index">
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