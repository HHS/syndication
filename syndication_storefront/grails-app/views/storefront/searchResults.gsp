<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 3/13/14
  Time: 2:48 PM
--%>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="storefront"/>
    <title>Syndication</title>
</head>

<body>
<div id="home_left">
    <div id="contentListBox">
        <form name="chooseListForm">
            <div class="purplebox">
                <img class="spinner" id="spinner" src="${assetPath(src: 'ajax-loader.gif')}" alt="spinner"/>

                <h3><s:pageContentAnchor/>Not fully implemented yet</h3>
                    Not Implemented yet
                %{--<g:render template="mediaList"/>--}%
            </div>
        </form>
    </div>

</div>

<g:render template="rightBoxes"/>
</body>
</html>