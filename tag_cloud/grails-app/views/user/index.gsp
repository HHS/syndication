<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 5/28/14
  Time: 4:28 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>User info</title>
    <meta name="layout" content="main"/>
    <r:require module="jquery"/>
    <g:javascript>
        $(document).ready(function(){
            $("#verifyPassword").on('input', function(){
                if(!($("#verifyPassword").val() === $("#password").val())){
                    $("#matcher").css("color", "red");
                    $("#matcher").html("Passwords do not match!");
                } else{
                    $("#matcher").css("color", "green");
                    $("#matcher").html("Passwords match!");
                }
            })
        });
    </g:javascript>
</head>

<body>
    <div class="nav" role="navigation">
        <ul>
            <li><a class="home" href="${createLink(controller:'appInfo', action: 'index')}"><g:message code="default.home.label"/></a></li>
        </ul>
    </div>

    <div style="width:900px; margin-left: auto; margin-right: auto">

        <h1>User Info</h1>
        <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
        </g:if>

        <g:hasErrors bean="${currentUser}">
            <ul class="errors" role="alert">
                <g:eachError bean="${currentUser}" var="error">
                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
        </g:hasErrors>
        <g:form action="updateUser">
            <g:hiddenField name="id" value="${currentUser.id}"/>
            <label for="name">Name:</label>
            <g:textField name="name" value="${currentUser?.name}"/>
            <br/>
            <br/>
            <label for="username">Username:</label>
            <g:textField name="username" value="${currentUser?.username}"/>
            <br/>
            <br/>
            <label for="password">Password:</label>
            <g:passwordField name="password"/>
            <br/>
            <br/>
            <label for="verifyPassword">Verify Password</label>
            <g:passwordField name="verifyPassword"/>&nbsp;<span id="matcher"></span>
            <br/>
            <br/>
            <g:submitButton name="submit" value="Save Changes"/>
        </g:form>
    </div>
</body>
</html>