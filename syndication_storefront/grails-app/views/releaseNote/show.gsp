
<%@ page import="com.ctacorp.syndication.storefront.ReleaseNote" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="storefront">
		<g:set var="entityName" value="${message(code: 'releaseNote.label', default: 'ReleaseNote')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<div class="nav" role="navigation">
			<ul>
                <li><g:link controller="storefront" action="releaseInfo"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-releaseNote" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list releaseNote">
			
				<g:if test="${releaseNoteInstance?.releaseNoteText}">
				<li class="fieldcontain">
					<span id="releaseNoteText-label" class="property-label"><g:message code="releaseNote.releaseNoteText.label" default="Release Note Text" /></span>
					
						<span class="property-value" aria-labelledby="releaseNoteText-label">
							<sf:markdown>
								<g:fieldValue bean="${releaseNoteInstance}" field="releaseNoteText"/>
							</sf:markdown>
						</span>
					
				</li>
				</g:if>
			
				<g:if test="${releaseNoteInstance?.releaseDate}">
				<li class="fieldcontain">
					<span id="releaseDate-label" class="property-label"><g:message code="releaseNote.releaseDate.label" default="Release Date" /></span>
					
						<span class="property-value" aria-labelledby="releaseDate-label"><g:formatDate date="${releaseNoteInstance?.releaseDate}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${releaseNoteInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="releaseNote.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${releaseNoteInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${releaseNoteInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="releaseNote.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${releaseNoteInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:releaseNoteInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${releaseNoteInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
