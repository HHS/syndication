<%--
  Created by IntelliJ IDEA.
  User: nburk
  Date: 3/4/15
  Time: 8:55 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="storefront"/>
    <title>Microsite Builder</title>
    <style type="text/css">
        .full-width{
            width: 100%
        }
    </style>
</head>

<body>
<div id="home">
    <div id="home_left">
        <div id="contentListBox">
            <g:if test="${flash.message}">
                <div class="message">${flash.message}</div>
            </g:if>
            <g:if test="${flash.error}">
                <div class="errors">${flash.error}</div>
            </g:if>
            <g:else>
                <a id="pageContent"></a>
                <g:render template="micrositeNav"/>
                <div class="purplebox">
                    <div id="show-userMediaList" class="content scaffold-show" role="main">
                        <h3>Microsites</h3>
                        <table class="full-width">
                            <thead>
                            <tr>
                                <th>Site Name</th>
                                <th>Template</th>
                                <th>Edit</th>
                                <th>Delete</th>
                            </tr>
                            </thead>
                                <g:each in="${userMicrosites}" var="site">
                                    <tr>
                                        <td><g:link controller="microsite" id="${site.id}" action="show" params="[showAdminControls:1]">${site.title ?: "No Title"}</g:link></td>
                                        <td>${site.templateType.name}</td>
                                        <td><g:link controller="microsite" id="${site.id}" action="edit">Edit</g:link></td>
                                        <g:form method="delete" action="delete" id="${site?.id}">
                                            <td><g:submitButton name="delete-microsite" value="Delete" onclick="return confirm('Are you sure you want to delete this microsite?')"/></td>
                                        </g:form>
                                    </tr>

                                </g:each>
                        </table>
                    </div>
                </div>
            </g:else>
        </div>
    </div>
    <g:render template="/storefront/rightBoxes" model="[featuredMedia: featuredMedia]"/>
</div>

</body>
</html>