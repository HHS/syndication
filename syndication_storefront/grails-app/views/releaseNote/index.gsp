
<%@ page import="com.ctacorp.syndication.storefront.ReleaseNote" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="storefront">
		<g:set var="entityName" value="${message(code: 'releaseNote.label', default: 'ReleaseNote')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<div class="nav" role="navigation">
			<ul>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-releaseNote" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
						<g:sortableColumn property="releaseDate" title="${message(code: 'releaseNote.releaseDate.label', default: 'Release Date')}" />
					
						<g:sortableColumn property="dateCreated" title="${message(code: 'releaseNote.dateCreated.label', default: 'Date Created')}" />
					
						<g:sortableColumn property="lastUpdated" title="${message(code: 'releaseNote.lastUpdated.label', default: 'Last Updated')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${releaseNoteInstanceList}" status="i" var="releaseNoteInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

						<td><g:link action="show" id="${releaseNoteInstance.id}"><g:formatDate date="${releaseNoteInstance.releaseDate}" /></g:link></td>
					
						<td><g:formatDate date="${releaseNoteInstance.dateCreated}" /></td>
					
						<td><g:formatDate date="${releaseNoteInstance.lastUpdated}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${releaseNoteInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
