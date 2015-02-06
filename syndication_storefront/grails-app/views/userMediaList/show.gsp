
<%@ page import="com.ctacorp.syndication.storefront.UserMediaList" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="storefront"/>

    <title>Show Media List</title>
</head>
	<body>
        <div id="home">
            <div id="home_left">
                %{--<h2><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></h2>--}%
                %{--<h2><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></h2>--}%
                <h2><g:link class="list" action="index">View Media List</g:link>&#160;&#160;&#160;
                <g:link class="create" action="create">Create Media List</g:link></h2>
                <div id="contentListBox">
                    <g:if test="${flash.message}">
                        <div class="message">${flash.message}</div>
                    </g:if>
                    <div class="purplebox">
                        <div id="show-userMediaList" class="content scaffold-show" role="main">
                            <h3>Show Media List</h3>
                            %{--<h3><g:message code="default.show.label" args="[entityName]" /></h3>--}%
                            %{--<g:if test="${flash.message}">--}%
                            %{--<div class="message" role="status">${flash.message}</div>--}%
                            %{--</g:if>--}%

                                <g:if test="${userMediaListInstance?.name}">
                                    <div class="left_side">
                                        <p class="bold"><g:message code="userMediaList.name.label" default="Name" /></p>
                                    </div>
                                    <div class="right_side">
                                        <p><g:fieldValue bean="${userMediaListInstance}" field="name"/></p>
                                    </div>
                                    <br>
                                </g:if>

                            <g:if test="${userMediaListInstance?.description}">
                                <div class="left_side">
                                    <p class="bold"><g:message code="userMediaList.description.label" default="Description" /></p>
                                </div>
                                <div class="right_side">
                                    <p><g:fieldValue bean="${userMediaListInstance}" field="description"/></p>
                                </div>
                                <br>
                            </g:if>

                            <g:if test="${userMediaListInstance?.dateCreated}">
                                <div class="left_side">
                                    <p class="bold"><g:message code="userMediaList.dateCreated.label" default="DateCreated" /></p>
                                </div>
                                <div class="right_side">
                                    <p><g:fieldValue bean="${userMediaListInstance}" field="dateCreated"/></p>
                                </div>
                                <br>
                            </g:if>
                            <g:if test="${userMediaListInstance?.lastUpdated}">
                                <div class="left_side">
                                    <p class="bold"><g:message code="userMediaList.lastUpdated.label" default="LastUpdated" /></p>
                                </div>
                                <div class="right_side">
                                    <p><g:fieldValue bean="${userMediaListInstance}" field="lastUpdated"/></p>
                                </div>
                                <br>
                            </g:if>

                                <div class="fieldcontain">
                                    <div class="left_side">
                                        <p class="bold"><g:message code="userMediaList.apiLink.label" default="API Link" /></p>
                                    </div>
                                    <div class="right_side">
                                        <p><sf:mediaListApiLink id="${userMediaListInstance?.id}"/></p>
                                    </div>
                                </div>
                            <g:if test="${userMediaListInstance?.mediaItems}">
                                <div class="fieldcontain">
                                    <div class="left_side">
                                        <p class="bold"><g:message code="userMediaList.mediaItems.label" default="Media Items" /></p>
                                    </div>
                                    <div class="right_side">
                                        <g:each in="${userMediaListInstance?.mediaItems}" var="mediaItemInstance">
                                            <p><g:link controller="storefront" action="showContent" id="${mediaItemInstance.id}">${mediaItemInstance?.encodeAsHTML()}</g:link></p>
                                        </g:each>
                                    </div>
                                </div>
                            </g:if>

                            <g:form url="[resource:userMediaListInstance, action:'update']" method="DELETE">
                                    <g:actionSubmit class="edit" action="edit" value="Edit"/>
                                    <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                                    <g:actionSubmit class="cancel" action="index" value="Cancel"/>
                            </g:form>
                        </div>
                    </div>
                </div>
            </div>
            <g:render template="/storefront/rightBoxes" model="[featuredMedia: featuredMedia]"/>
        </div>
	</body>
</html>