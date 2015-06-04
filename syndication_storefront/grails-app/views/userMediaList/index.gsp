<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ctacorp.syndication.storefront.UserMediaList" %>
<html>
<head>
    <meta name="layout" content="storefront"/>

    <title>Lists</title>
</head>

<body>
<div id="home">
    <div id="home_left">
        <h2><g:link class="create" action="create">Create List</g:link></h2>
        <div id="list-userMediaList" class="content scaffold-list" role="main">
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <div id="contentListBox">
                    <div class="purplebox">
                        %{--<h3><g:message code="default.my.label" args="[entityName]" /></h3>--}%
                        <h3>My Lists</h3>
                        <table>
                            <thead>
                            <tr>
                                <g:sortableColumn property="name" class="col1 sortable" title="${message(code: 'userMediaList.name.label', default: 'Name')}"/>
                                <g:sortableColumn property="description" class="col2 sortable" title="${message(code: 'userMediaList.name.label', default: 'Description')}"/>
                                <g:sortableColumn property="lastUpdated" class="col4 sortable" title="${message(code: 'userMediaList.lastUpdated.label', default: 'Last Updated')}"/>
                            </tr>
                            </thead>
                            <tbody>
                            <g:each in="${userMediaListInstanceList}" status="i" var="userMediaListInstance">
                                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                                    <td class="mediaListIndex"><g:link action="show"
                                                id="${userMediaListInstance.id}">${fieldValue(bean: userMediaListInstance, field: "name")}</g:link></td>
                                    <td class="mediaListIndex">
                                        ${fieldValue(bean: userMediaListInstance, field: "description")}
                                    </td>
                                    <td>
                                        ${fieldValue(bean: userMediaListInstance, field: "lastUpdated")}
                                    </td>
                                </tr>
                            </g:each>
                            </tbody>
                        </table>
                    </div>
            </div>
            <div class="pagination">
                <g:paginate total="${userMediaListInstanceCount ?: 0}"/>
            </div>
        </div>
    </div>
    <g:render template="/storefront/rightBoxes" model="[featuredMedia: featuredMedia]"/>
</div>
</body>
</html>


