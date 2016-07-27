<%--
  Created by IntelliJ IDEA.
  User: nburk
  Date: 6/1/16
  Time: 1:45 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="layout" content="login"/>
    <title>Syndication Admin - Forgot Password</title>
</head>

<body>

<div class="container">
    <div class="row">
        <div class="col-md-4 col-md-offset-4">
            <div class="login-panel panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Forgot Password?</h3>
                </div>

                <div class="panel-body">
                    <g:if test='${flash.error}'>
                        <div class='panel-body'><span class="text-danger"><i class="fa fa-warning"></i> ${flash.error}</span></div>
                    </g:if>
                    <g:if test='${flash.message}'>
                        <div class='panel-body'><span class="text-info"><i class="fa fa-info"></i> ${flash.message}</span></div>
                    </g:if>
                    <g:form action='resetPassword' method='POST' id='loginForm' class='cssform' autocomplete='off'>
                        <fieldset>
                            <p>
                                Have a new password sent to your email
                            </p>

                            <div class="form-group">
                                <input id="username" autocomplete="off" class="form-control" placeholder="Email/Username" name="username" type="text" autofocus>
                            </div>

                            <div class="form-group">
                                <g:submitButton name="submit" autocomplete="off" class="btn btn-lg btn-success btn-block" value="Send New Password"/>
                            </div>

                        </fieldset>
                    </g:form>


                </div>
            </div>
            <p>
                If your account is locked or you have other problems logging in, please contact <a href="mailto:syndicationadmin@hhs.gov?Subject=Please%20Unlock%20My%20Account">syndicationadmin@hhs.gov</a>.
            </p>
        </div>
    </div>
</div>

</body>
</html>