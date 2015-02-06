
%{--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="layout" content="login"/>
    <title>Syndication Admin - Login</title>
</head>

<body>
<div class="container">
    <div class="row">
        <div class="col-md-4 col-md-offset-4">
            <div class="login-panel panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><g:message code="springSecurity.login.header"/></h3>
                </div>

                <div class="panel-body">
                    <g:if test='${flash.message}'>
                        <div class='panel-body'><span class="text-danger"><i class="fa fa-warning"></i> ${flash.message}</span></div>
                    </g:if>
                    <form action='${postUrl}' method='POST' id='loginForm' class='cssform' autocomplete='off'>
                        <fieldset>
                            <div class="form-group">
                                <input id="username" autocomplete="off" class="form-control" placeholder="Email" name="j_username" type="text" autofocus>
                            </div>

                            <div class="form-group">
                                <input class="form-control" autocomplete="off" id="password" placeholder="Password" name="j_password" type="password" value="">
                            </div>

                            <g:submitButton name="submit" autocomplete="off" class="btn btn-lg btn-success btn-block" value="Login"/>
                        </fieldset>
                    </form>
                </div>
            </div>
            <p>
                If your account is locked or you have other problems logging in, please contact <a href="mailto:syndicationadmin@hhs.gov?Subject=Please%20Unlock%20My%20Account">syndicationadmin@hhs.gov</a>.
            </p>
        </div>
    </div>
</div>

<script>
    $(document).ready(function(){
        $('username').focus()
    })
</script>
</body>
</html>