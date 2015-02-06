<%--
  Created by IntelliJ IDEA.
  User: esommers
  Date: 7/1/14
  Time: 12:18 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="dashboard"/>
    <g:set var="entityName" value="${message(code: 'tagType.label', default: 'Tag Type')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>
<div id="page-wrapper">
    <div class="row">
        <div class="col-md-12">
            <h1 class="page-header">Create Tag Type</h1>
        </div>
    </div>

    <div class="col-lg-8">
        <synd:message/>
        <synd:errors/>
    </div>

    <div class="row">
        <div class="col-lg-8">
            <g:form class="form-horizontal">
                <g:render template="form" model="[tagTypeInstance:tagTypeInstance]"/>
                <!-- Button -->
                <div class="form-group">
                    <div class="col-md-4">
                        <g:actionSubmit action="save" value="Save" class="btn btn-success"/>
                        <g:if test="${params.action == 'edit'}">
                            <g:actionSubmit value="Delete" action="delete" class="btn btn-danger" onclick="return confirm('Are you Sure?');"/>
                        </g:if>
                        <g:link class="button" action="index">
                            <button type="button" class="btn">Cancel</button>
                        </g:link>
                    </div>
                </div>
            </g:form>
        </div>
    </div>
</div>
</body>
</html>