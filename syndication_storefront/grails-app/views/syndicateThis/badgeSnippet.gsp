<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="storefront"/>
    <title>Syndicate This button code snippet</title>
</head>

<body>

<div id="home">
    <div id="home_left">
        <div id="contentListBox">
            <div class="purplebox">
                <h3>Syndicate This code snippet</h3>
                    <span>Copy and paste this snippet into your existing webpage, and when the page is viewed by a user, the Syndicate This button will be displayed</span>
                    <br/>
                    <g:textArea name="embedCode" cols="50" rows="10" style="resize:none" id="snippetCode" value="${snippet}" contenteditable="false" readonly="readonly"/>
            </div>
        </div>
    </div>
    <g:render template="/storefront/rightBoxes" model="[API_SERVER_URL:API_SERVER_URL]" />
</div>
</body>
</html>