<%@ page import="com.ctacorp.syndication.storefront.ReleaseNote" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="storefront">
		<g:set var="entityName" value="${message(code: 'releaseNote.label', default: 'ReleaseNote')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>
		<div class="nav" role="navigation">
			<ul>
                <li><g:link controller="storefront" action="releaseInfo"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="edit-releaseNote" class="content scaffold-edit" role="main">
			<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${releaseNoteInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${releaseNoteInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form url="[resource:releaseNoteInstance, action:'update']" method="PUT" >
				<g:hiddenField name="version" value="${releaseNoteInstance?.version}" />
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
					<button type="button" id="updatePreviewButton">Preview Markdown</button>
				</fieldset>
			</g:form>
            <div id="markdownPreview" style="border: 1px dashed black; padding:10px; margin:10px 0px 0px 0px;">
                <h1>Click "Preview Markdown" before saving!</h1>
                <g:if test="${htmlMarkdown}">
                    ${htmlMarkdown.encodeAsRaw()}
                </g:if>
            </div>
        </div>

    <script>
        $(document).ready(function(){
            $('#updatePreviewButton').click(function(){
                $.post('<g:createLink action="previewMarkdown"/>', {markdownContentForPreview:$('#releaseNoteText').val()}, function(renderedHtml){
                    $('#markdownPreview').html(renderedHtml)
                });
            });
        });
    </script>
	</body>
</html>
