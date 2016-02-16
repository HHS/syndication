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
    <synd:error/>

    <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER">
        <div class="row">
            <div class="col-md-8">

                <g:form class="form-horizontal" url="[resource: PDFInstance, action: 'update']" method="PUT">
                    <g:hiddenField name="version" value="${PDFInstance?.version}"/>
                    <fieldset class="form">
                        <g:render template="form"/>
                    </fieldset>
                    <fieldset class="buttons">
                        <g:actionSubmit class="btn btn-success" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                        <g:link class="button" id="${PDFInstance.id}" resource="${PDFInstance}" action="show">
                            <button type="button" class="btn">Cancel</button>
                        </g:link>
                        <g:link controller="mediaPreviewThumbnail" id="${PDFInstance?.id}" action="flush">
                            <button type="button" class="btn btn-warning pull-right">Regenerate Thumbnail & Preview</button>
                        </g:link>
                    </fieldset>
                </g:form>
            </div>
        </div>
    </sec:ifAnyGranted>
    <g:render template="/mediaItem/addAttributeOrImage" model="[mediaItemInstance: PDFInstance]"/>
</div>
</body>
</html>
