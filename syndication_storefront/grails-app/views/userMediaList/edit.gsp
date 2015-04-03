<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ctacorp.syndication.storefront.UserMediaList" %>
<html>
<head>
    <meta name="layout" content="storefront"/>

    <title>Edit Media List</title>
    <asset:stylesheet src="tokenInput.css"/>
    <asset:javascript src="/tokenInput/jquery.tokeninput.js"/>
    <g:javascript>
            $(document).ready(function () {
                $("#mediaItemIds").tokenInput("${g.createLink(controller: 'userMediaList', action: 'mediaSearch')}", {
                    prePopulate:${raw(mediaItemList ?: "[]")},
                    preventDuplicates:true
                });
            });
    </g:javascript>
</head>

<body>
<g:if test="${flash.message}">
    <div class="message" role="status">${flash.message}</div>
</g:if>
<g:hasErrors bean="${userMediaListInstance}">
    <div class="errors">
        <g:renderErrors bean="${userMediaListInstance}" as="list" />
    </div>
</g:hasErrors>
<div id="home">
    <div id="home_left">
        %{--<h2><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]"/></g:link></h2>--}%
        %{--<h2><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]"/></g:link></h2>--}%
        <h2><g:link class="list" action="index">View Media List</g:link>&#160;&#160;&#160;
        <g:link class="create" action="create">Create Media List</g:link></h2>
        <div id="contentListBox">
            <div class="purplebox">
                <div id="edit-userMediaList" class="content scaffold-edit" role="main">
                    <h3>Edit Media List</h3>
                %{--<h3><g:message code="default.edit.label" args="[entityName]"/></h3>--}%

                    <g:form url="[resource: userMediaListInstance, action: 'update']" method="PUT">
                        <g:hiddenField name="version" value="${userMediaListInstance?.version}"/>
                        <g:render template="form"/>
                        <g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}"/>
                        <g:actionSubmit class="cancel" action="show" value="Cancel"/>
                    </g:form>
                </div>
            </div>
        </div>
    </div>
    <g:render template="/storefront/rightBoxes" model="[featuredMedia: featuredMedia]"/>
</div>
</body>
</html>
