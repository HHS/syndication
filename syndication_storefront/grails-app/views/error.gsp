<!DOCTYPE html>
<%@ page import="grails.util.Holders" contentType="text/html;charset=UTF-8" %>
<html>
	<head>
		<title><g:if env="development">Grails Runtime Exception</g:if><g:else>Error</g:else></title>
        <meta name="layout" content="storefront"/>
		<g:if env="development"><link rel="stylesheet" href="${resource(dir: 'css', file: 'errors.css')}" type="text/css"></g:if>
        <g:if env="production" test="${grails.util.Holders.config.SHOW_DETAILED_ERROR_MESSAGES_IN_PRODUCTION}">Grails Runtime Exception</g:if>
    </head>
	<body>
		<g:if env="development">
			<g:renderException exception="${exception}" />
		</g:if>
        <g:if env="production" test="${Holders.config.SHOW_DETAILED_ERROR_MESSAGES_IN_PRODUCTION}">
            <g:renderException exception="${exception}" />
        </g:if>
		<g:else>
			<ul class="errors">
				<li>An error has occurred</li>
			</ul>
		</g:else>
	</body>
</html>
