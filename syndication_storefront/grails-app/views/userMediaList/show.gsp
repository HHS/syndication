<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ctacorp.syndication.storefront.UserMediaList" %>
<html>
<head>
    <meta name="layout" content="storefront"/>
    <title>Show List</title>
</head>
	<body>
        <div id="home">
            <div id="home_left">
                <h2><g:link class="list" action="index">View List</g:link>&#160;&#160;&#160;
                <g:link class="create" action="create">Create List</g:link></h2>
                <div id="contentListBox">
                    <g:if test="${flash.message}">
                        <div class="message">${flash.message}</div>
                    </g:if>
                    <g:if test="${flash.error}">
                        <div class="errors" role="status"><li>${flash.error}</li></div>
                    </g:if>
                    <div class="purplebox">
                        <div id="show-userMediaList" class="content scaffold-show" role="main">
                            <h3>Show List</h3>
                                <g:if test="${userMediaList?.name}">
                                    <div class="left_side">
                                        <p class="bold"><g:message code="userMediaList.name.label" default="Name" /></p>
                                    </div>
                                    <div class="right_side">
                                        <p><g:fieldValue bean="${userMediaList}" field="name"/></p>
                                    </div>
                                    <br>
                                </g:if>

                            <g:if test="${userMediaList?.description}">
                                <div class="left_side">
                                    <p class="bold"><g:message code="userMediaList.description.label" default="Description" /></p>
                                </div>
                                <div class="right_side">
                                    <p><g:fieldValue bean="${userMediaList}" field="description"/></p>
                                </div>
                                <br>
                            </g:if>

                            <g:if test="${userMediaList?.id}">
                                <div class="left_side">
                                    <p class="bold"><g:message code="userMediaList.description.label" default="ID" /></p>
                                </div>
                                <div class="right_side">
                                    <p><g:fieldValue bean="${userMediaList}" field="id"/></p>
                                </div>
                                <br>
                            </g:if>

                            <g:if test="${userMediaList?.dateCreated}">
                                <div class="left_side">
                                    <p class="bold"><g:message code="userMediaList.dateCreated.label" default="DateCreated" /></p>
                                </div>
                                <div class="right_side">
                                    <p><g:fieldValue bean="${userMediaList}" field="dateCreated"/></p>
                                </div>
                                <br>
                            </g:if>
                            <g:if test="${userMediaList?.lastUpdated}">
                                <div class="left_side">
                                    <p class="bold"><g:message code="userMediaList.lastUpdated.label" default="LastUpdated" /></p>
                                </div>
                                <div class="right_side">
                                    <p><g:fieldValue bean="${userMediaList}" field="lastUpdated"/></p>
                                </div>
                                <br>
                            </g:if>

                            <div class="fieldcontain">
                                <div class="left_side">
                                    <p class="bold"><g:message code="userMediaList.apiLink.label" default="API Link" /></p>
                                </div>
                                <div class="right_side">
                                    <p><sf:mediaListApiLink id="${userMediaList?.id}"/></p>
                                </div>
                            </div>

                            <g:if test="${userMediaList?.mediaItems}">
                                <div class="fieldcontain">
                                    <div class="left_side">
                                        <p class="bold"><g:message code="userMediaList.mediaItems.label" default="Media Items" /></p>
                                    </div>
                                    <div class="right_side">
                                        <g:each in="${userMediaList?.mediaItems?.sort{it.name}}" var="mediaItemInstance">
                                            <p><g:link controller="storefront" action="showContent" id="${mediaItemInstance.id}">${mediaItemInstance?.encodeAsHTML()}</g:link></p>
                                        </g:each>
                                    </div>
                                </div>
                            </g:if>

                            <g:form url="[resource:userMediaList, action:'update']" method="DELETE">
                                    <g:actionSubmit class="edit" action="edit" value="Edit"/>
                                    <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('Are you sure you want to delete this list?');" />
                                    <g:actionSubmit class="cancel" action="index" value="Cancel"/>
                            </g:form>
                        </div>
                    </div>

                </div>
            </div>
            <g:render template="/storefront/rightBoxes" model="[featuredMedia: featuredMedia]"/>
            <g:render template="/storefront/livePreview"/>
        </div>
        <g:render template="/storefront/showContentScripts"/>
	</body>
</html>