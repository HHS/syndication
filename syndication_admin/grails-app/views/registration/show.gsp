<%--
  Created by IntelliJ IDEA.
  User: nburk
  Date: 11/24/15
  Time: 10:33 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'registration.label', default: 'Microsite Registration')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<div id="show-registration" class="container-fluid" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>

    <div class="row">
        <div class="col-md-10 col-md-offset-2">
            <dl class="dl-horizontal">
                <g:if test="${registrationInstance?.id}">
                    <dt id="id-label" class="word_wrap"><g:message code="registration.id.label" default="Id"/></dt>
                    <dd class="word_wrap">${registrationInstance?.id}</dd>
                </g:if>

                <g:if test="${registrationInstance?.organization}">
                    <dt id="organization-label" class="word_wrap"><g:message code="registration.organization.label" default="organization"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${registrationInstance}" field="organization"/></dd>
                </g:if>

                <g:if test="${registrationInstance?.description}">
                    <dt id="description-label" class="word_wrap"><g:message code="registration.description.label" default="description"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${registrationInstance}" field="description"/></dd>
                </g:if>

                    <dt id="verified-label" class="word_wrap"><g:message code="registration.verified.label" default="verified"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${registrationInstance}" field="verified"/></dd>

            </dl>
        </div>
    </div>
    <fieldset class="buttons">
        <g:form  url="[id:registrationInstance?.id, controller:'registration',action:'update']">
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER">
                <g:if test="${registrationInstance?.verified}">
                    <g:submitButton name="changeAccess" class="btn btn-warning" value="Revoke Access" action="update"/>
                </g:if>
                <g:else>
                    <g:submitButton name="changeAccess" class="btn btn-warning" value="Give Access"/>
                </g:else>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER">
                <g:actionSubmit class="btn btn-danger" value="Delete" onclick="return confirm('${message(code: 'default.button.delete.micrositeRegistration.confirm', default: 'Are you sure you want to delete this Microsite Registration Form?')}');" action="delete"/>
            </sec:ifAnyGranted>
            <g:link class="button" action="index">
                <button type="button" class="btn">Cancel</button>
            </g:link>
        </g:form>
    </fieldset>
</div>
</body>
</html>