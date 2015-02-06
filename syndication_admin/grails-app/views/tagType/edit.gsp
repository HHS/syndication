<%--
  Created by IntelliJ IDEA.
  User: esommers
  Date: 7/1/14
  Time: 12:18 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="dashboard">
    <g:set var="entityName" value="${message(code: 'tagType.label', default: 'Tag Type')}" />
  <title></title>
</head>
<body>
<div id="page-wrapper">
    <div class="row">
        <div class="col-md-12">
            <h1 class="page-header">Edit Tag Type</h1>
        </div>
    </div>

        <synd:message/>
        <synd:errors/>
        <g:form class="form-horizontal" action="update">
            <div class="row">
                <div class="col-md-8">
                 <g:render template="form" model="[tagTypeInstance:tagTypeInstance]"/>
                </div>
            </div>
            <!-- Button -->
            <div class="row">
                <div class="col-xs-12">
                     <div class="form-group">
                        <g:submitButton id="update" name="Update" class="btn btn-success" onclick="return confirm('Are you Sure?');">Update</g:submitButton>
                        <g:if test="${params.action == 'edit'}">
                            <g:actionSubmit value="Delete" action="delete" class="btn btn-danger" onclick="return confirm('Are you Sure?');"/>
                        </g:if>
                        <g:link class="button" action="index">
                            <button type="button" class="btn">Cancel</button>
                        </g:link>
                    </div>
                </div>
            </div>
        </g:form>


</div>

</body>
</html>