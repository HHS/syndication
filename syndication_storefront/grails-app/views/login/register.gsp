<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name='layout' content='storefront'/>
    <title>Storefront Register</title>
</head>
<body>
<div id="login_pages">
    <h1>Register</h1>
    <p>Registering enables you to syndicate content from the HHS. You’ll be asked to provide your organization and contact information. Your information will only be used to deliver the requested information or to give you access to your account. Upon registering, HHS will provide you with a unique Registration ID that you’ll use to syndicate content with.</p>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <g:if test="${flash.error}">
        <div class="errors">${flash.error}</div>
    </g:if>
    <g:hasErrors bean="${userInstance}">
        <div class="errors">
            <g:renderErrors bean="${userInstance}" as="list" />
        </div>
    </g:hasErrors>


    <sf:hasErrors/>
    <g:form id="registerForm"  action="createUserAccount">
        <div class="register_box">
            <h3>Contact Information</h3>

            <p class="italic"><span class="red">*</span> Indicates required fields</p>

            <div>
                <div class="left_side">
                    <p>Full Name <span class="red">*</span></p>
                </div>
                <label for="name" class="obscure">Full Name</label>
                <div class="right_side_name ${hasErrors(bean: userInstance, field: 'name', 'errors')}">
                    <g:textField id="name" name="name" value="${userInstance?.name}"/>
                </div>
                <div class="below_text">
                    <p class="italic">Enter your name (First Name, Middle Initial, Last Name).</p>
                </div>
            </div>
        </div>

        <div class="register_box">
            <h3>Sign In Information</h3>

            <p>Please enter your e-mail address, select a password for your account, and confirm the password.<br/>Note: Your password is case sensitive and must conform to these rules:</p>

            <ul class="register_set">
                <li>Be at least 8 characters</li>
                <li>Contain at least one upper case letter</li>
                <li>Contain at least one lower case letter</li>
                <li>Contain at least one number</li>
            </ul>
            <div>
                <div class="left_side">
                    <p><label for="email" class="registerLabel">E-mail </label><span class="red">*</span></p>
                </div>

                <div class="right_side ${hasErrors(bean: userInstance, field: 'username', 'errors')}">
                    <g:textField id="email" name="username" value="${userInstance?.username}"/>
                </div>
            </div>

            <div>
                <div class="left_side">
                    <p><label for="password" class="registerLabel">Password </label> <span class="red">*</span></p>
                </div>

                <div class="right_side ${hasErrors(bean: userInstance, field: 'password', 'errors')}">
                    <g:passwordField id="password" name="password"/>
                </div>
            </div>

            <div>
                <div class="left_side">
                    <p><label for="passwordRepeat" class="registerLabel">Re-enter Password </label><span class="red">*</span></p>
                </div>

                <div class="right_side ${hasErrors(bean: userInstance, field: 'passwordRepeat', 'errors')}">
                    <g:passwordField id="passwordRepeat" name="passwordRepeat"/>
                </div>
            </div>
        </div>
        <recaptcha:ifEnabled>
            <div class="register_box">
                <p class="recaptcha"><label class="registerLabel" for="recaptcha">Please verify the recaptcha below </label><span class="red">*</span></p>
                <recaptcha:recaptcha theme="clean" id="recaptcha"/>
            </div>
        </recaptcha:ifEnabled>
       <div class="input_row">
            <g:submitButton name="registerButton" value="Register"/>
            <g:actionSubmit name="cancelButton" action="cancel" value="Cancel"/>
        </div>
    </g:form>
</div>
</body>
</html>