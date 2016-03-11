<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'PDF.label', default: 'PDF')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>

<div id="create-PDF" class="content scaffold-create" role="main">
    <h1><g:message code="default.create.label" args="[entityName]" /></h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>

    <div class="row">
        <div class="col-md-10">
            <g:form class="form-horizontal" url="[resource: PDFInstance, action: 'save']">
                <fieldset class="form">
                    <g:render template="form"/>
                </fieldset>
                <fieldset class="buttons">
                    <i id="creationSpinner" class="fa fa-refresh fa-spin" style="display: none;"></i>
                    <g:submitButton name="create" class="btn btn-success"
                                    value="${message(code: 'default.button.create.label', default: 'Create')}"/>
                    <g:link class="btn btn-default" action="index">
                        Cancel
                    </g:link>
                </fieldset>
            </g:form>
        </div>
    </div>
</div>
</body>
</html>
