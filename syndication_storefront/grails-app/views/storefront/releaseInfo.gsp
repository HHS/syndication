<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 2/17/15
  Time: 2:25 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="storefront"/>
    <title>Release and Version Info</title>
</head>

<body>
<div id="home">
    <div id="home_left">
        <div id="contentListBox">
            <a name="pageContent"></a>
        %{--===================================================================--}%
            <sec:ifAnyGranted roles="ROLE_ADMIN">
                <g:link controller="releaseNote" action="create">New Release Note...</g:link>
            </sec:ifAnyGranted>
            <g:each in="${releaseNotes}" var="releaseNote">
                <div class="purplebox">
                    <h1>Release on ${releaseNote.releaseDate.format("MMM dd, yyyy")}</h1>
                    <sec:ifAnyGranted roles="ROLE_ADMIN">
                        <g:link controller="releaseNote" action="edit" id="${releaseNote.id}">Edit...</g:link>
                    </sec:ifAnyGranted>

                    <div>
                        <sf:markdown>
                            ${releaseNote.releaseNoteText}
                        </sf:markdown>
                    </div>
                </div>
            </g:each>
        %{--===================================================================--}%
        </div>
    </div>
    <g:render template="../storefront/rightBoxes"/>
</div>
</body>
</html>