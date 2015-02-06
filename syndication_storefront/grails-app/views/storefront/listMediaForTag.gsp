<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 3/13/14
  Time: 2:48 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="storefront"/>
    <title>Syndication</title>
</head>

<body>
    <div id="home_left">

        <h2>HHS Syndication Storefront</h2>

        <p>The HHS Syndication Storefront allows you to syndicate (import) content from many HHS websites directly into your own website or application.  These services are provided by HHS free of charge.</p>


        <div id="searchBox">
            <div class="bluebox">
                <div id="searchOptions">
                    <g:render template="basicSearch"/>
                </div>
            </div>
        </div>

        <div id="contentListBox">
            <form name="chooseListForm">
                <div class="purplebox">
                    <img class="spinner" id="spinner" src="${assetPath(src: 'ajax-loader.gif')}" alt="spinner"/>

                    <h3><s:pageContentAnchor/>Media tagged with "${tagName}"</h3>

                    <g:render template="mediaList"/>
                </div>
            </form>
        </div>

    </div>

    <g:render template="rightBoxes"/>
</body>
</html>