<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="storefront"/>
	<title><g:message code="springSecurity.login.title"/></title>

</head>

<body>
<div id='login_pages'>
	<div class='inner'>
        <h1>Sign In</h1>

        <p>Please Sign In below to get the syndicated code for any pages you’ve added to Your List or to view your account.</p>

		<g:if test='${flash.message}'>
			<div class='message'>${flash.message}</div>
		</g:if>
        <sf:hasErrors/>

        <form action='${postUrl}' method='POST' id='loginForm' class='cssform' autocomplete='off'>
            <div class="login_box auth">
                <p>Enter the e-mail address and password you used to register on this site.</p>

                <div class="group">
                    <div class="left_side">
                        <label for="username">E-mail:</label>
                    </div>

                    <div class="right_side">
                        <input type='text' class='text_' name='j_username' id='username' />
                    </div>
                </div>

                <div class="group">
                    <div class="left_side">
                        <label for="password">Password:</label>
                    </div>

                    <div class="right_side">
                        <input type='password' class='text_' name='j_password' id='password'/>
                    </div>
                </div>

                <div class="group">
                    <div class="left_side"></div>
                    <div class="left_side_remember">
                        <input type='checkbox' class='chk' name='${rememberMeParameter}' id='remember_me' <g:if test='${hasCookie}'>checked='checked'</g:if>/>
                        <label for='remember_me'>Remember me</label>
                    </div>

                    <div class="right_side_signin">
                        <g:submitButton name="signInButton" value="Sign In"/>
                    </div>
                </div>

                <div class="group">
                    %{--<div class="left_side"></div>--}%

                    <div class="right_side_link">
                        <g:link action="register" controller="login">New User?</g:link>
                    </div>

                    <div class="right_side_link">
                        <g:link action="forgotPassword" update="forgotPasswordContent">Forgot Password?</g:link>
                    </div>
                    <div class="right_side_link">
                        <g:link action="accountLocked" update="forgotPasswordContent">Unlock Account?</g:link>
                    </div>
                </div>
            </div>
            <p>
                If your account is locked or you have other problems logging in, please contact <a href="mailto:syndicationadmin@hhs.gov?Subject=Please%20Unlock%20My%20Account">syndicationadmin@hhs.gov</a>.
            </p>
        </form>
	</div>
</div>
<script type='text/javascript'>
	<!--
	(function() {
		document.forms['loginForm'].elements['j_username'].focus();
	})();
	// -->
</script>
</body>
</html>
