<%@ page import="grails.util.Holders" %>

<g:set var="helpEmailAddress" value="${grails.util.Holders.config.CMSMANAGER_HELPEMAILADDRESS}"/>

<html>
<head>
    <meta name='layout' content='main'/>
</head>

<body>
<g:render template="/templates/header"/>

<div class="container">

    <div class="row">

        <div class="col-md-4 col-md-offset-4">

            <div style="margin-top: 25%" class="panel panel-default">
                <div class="panel-heading">
                    <h3 style="font-size: 16px; margin-top: 0; margin-bottom: 0;"><g:message code="default.login.label"/></h3>
                </div>

                <div class="panel-body">
                    <g:if test='${flash.message}'>
                        <div class="alert alert-danger" role="alert">${flash.message}</div>
                    </g:if>

                    <form id='loginForm' action="${postUrl}" method="POST" autocomplete="on" role="form">
                        <div class="form-group">
                            <input type="text" autofocus="" class="form-control" name="username" id="username"
                                   placeholder="Enter username">
                        </div>

                        <div class="form-group">
                            <input type="password" class="form-control" name="password" id="password"
                                   placeholder="Password">
                        </div>

                        <button type="submit" class="btn btn-lg btn-success btn-block">Login</button>
                    </form>
                </div>
            </div>
            <p style="padding-left: .5em;">
                If your account is locked or you have other problems logging in, please contact <a href="mailto:${helpEmailAddress}?Subject=Please%20Unlock%20My%20Account">${helpEmailAddress}</a>.
            </p>
        </div>
    </div>

    <script type='text/javascript'>
        <!--
        (function () {
            document.forms['loginForm'].elements['username'].focus();
        })();
        // -->
    </script>
</body>
</html>