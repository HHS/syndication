<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ctacorp.syndication.media.MediaItem" %>

<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="storefront"/>

    <title>Syndication Storefront</title>
</head>

<body>
    <div id="home">
        <div id="home_left">
            <div id="newestContent">
                <a name="pageContent"></a>
                <h2>HHS Syndication Storefront</h2>

                <p>The HHS Syndication Storefront allows you to syndicate (import) content from many HHS websites directly into your own website or application.  These services are provided by HHS free of charge.</p>

                <g:if test="${flash.error}">
                    <div class="errors"><li>${flash.error}</li></div>
                </g:if>
                <g:if test="${flash.message}">
                    <div class="message">${flash.message}</div>
                </g:if>
                <g:render template="search"/>

                <div id="contentListBox">
                    <form name="chooseListForm">
                        <div class="purplebox">
                            <img class="spinner" id="spinner" src="${assetPath(src: 'ajax-loader.gif')}" alt="spinner"/>

                            <h3>${contentTitle}</h3>
                            <div id="mediaList">
                                <g:render template="mediaList" />
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <g:render template="rightBoxes" model="[featuredMedia: featuredMedia, API_SERVER_URL:API_SERVER_URL]"/>
    </div>
</body>
</html>