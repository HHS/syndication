<html>
<head>
    <meta name='layout' content='storefront'/>
    <title><g:message code="springSecurity.login.title"/></title>
</head>
<body>
<div id='login_pages'>
    <div class='inner'>
        <h1>User Account Information</h1>

        <g:if test='${flash.message}'>
            <div class='message'>${flash.message}</div>
        </g:if>
        <g:if test='${flash.error}'>
            <div class='errors'>${flash.error}</div>
        </g:if>

        <g:hasErrors bean="${userInstance}">
            <div class="errors">
                <g:renderErrors bean="${userInstance}" as="list" />
            </div>
        </g:hasErrors>
        <g:render template="storefrontUserForm"/>
    </div>
</div>
</body>
</html>
