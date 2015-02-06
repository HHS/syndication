<%@ page import="com.ctacorp.syndication.authentication.User" %>
<html>
<head>
    <meta name="layout" content="storefront"/>
    <title><g:message code="springSecurity.login.title"/></title>

</head>

<body>
<div class="forget_password">
    <h1>Forgot your password?</h1>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <sf:hasErrors/>
    <div class="register_box forgotPassword">
        <form id="passwordResetForm" class='cssform' action="${g.createLink(controller:'login', action:'passwordReset')}">

            <label for="email">Account E-mail Address:</label>
            <g:textField name="email" class="email_for_reset" value="${email}"/>
            <div id='captchaBox'>
                <recaptcha:ifEnabled>
                    <recaptcha:recaptcha theme="clean" id="recaptcha"/>
                </recaptcha:ifEnabled>
            </div>
            <br/>
            <div>
                <g:submitButton name="passwordResetButton" value="Request Password Reset"/>
            </div>
        </form>
    </div>
</div>
</body>
</html>