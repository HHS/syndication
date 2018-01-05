<%@ page import="com.ctacorp.syndication.media.PDF" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'PDF.label', default: 'PDF')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>

<body>
<div id="edit-image" class="content scaffold-edit" role="main">
    <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
    <synd:message/>
    <synd:errors/>
    <synd:hasError/>
    <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER">
        <div class="row">
            <div class="col-md-8">
                <g:form class="form-horizontal" url="[resource: pdfInstance, action: 'update']" method="PUT" id="updateMediaItem">
                    <g:hiddenField name="version" value="${pdfInstance?.version}"/>
                    <fieldset class="form">
                        <g:render template="form" model="[pdfInstance: pdfInstance]" />
                    </fieldset>
                    <fieldset class="buttons" id="mediaItemSubmitButton">
                        <g:actionSubmit class="btn btn-success" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                        <g:link class="btn btn-dfault" id="${pdfInstance?.id}" resource="${pdfInstance}" action="show">
                            Cancel
                        </g:link>
                    </fieldset>
                </g:form>
            </div>
        </div>
    </sec:ifAnyGranted>
    <g:render template="/mediaItem/addAttributeOrImage" model="[mediaItemInstance: pdfInstance]"/>
</div>
</body>
</html>
