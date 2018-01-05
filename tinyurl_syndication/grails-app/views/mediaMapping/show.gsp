
<%@ page import="com.ctacorp.tinyurl.MediaMapping" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'mediaMapping.label', default: 'MediaMapping')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-mediaMapping" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-mediaMapping" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list mediaMapping">
			
				<g:if test="${mediaMapping?.targetUrl}">
				<li class="fieldcontain">
					<span id="targetUrl-label" class="property-label"><g:message code="mediaMapping.targetUrl.label" default="Target Url" /></span>
					
						<span class="property-value" aria-labelledby="targetUrl-label"><a href="${mediaMapping.targetUrl}"><g:fieldValue bean="${mediaMapping}" field="targetUrl"/></a></span>
					
				</li>
				</g:if>
			
				<g:if test="${mediaMapping?.syndicationId}">
				<li class="fieldcontain">
					<span id="syndicationId-label" class="property-label"><g:message code="mediaMapping.syndicationId.label" default="Syndication Id" /></span>
					
						<span class="property-value" aria-labelledby="syndicationId-label"><g:fieldValue bean="${mediaMapping}" field="syndicationId"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${mediaMapping?.guid}">
				<li class="fieldcontain">
					<span id="guid-label" class="property-label"><g:message code="mediaMapping.guid.label" default="Guid" /></span>
					
						<span class="property-value" aria-labelledby="guid-label"><g:fieldValue bean="${mediaMapping}" field="guid"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:mediaMapping, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${mediaMapping}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
