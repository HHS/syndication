<%--
  Created by IntelliJ IDEA.
  User: nburk
  Date: 11/23/15
  Time: 3:22 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="storefront"/>
    <title>Syndication: Microsite Registration</title>
</head>
<body>
<div id='login_pages'>
    <div class='inner'>
        <h1>Register for Microsites</h1>
        <p>Microsites are personalized single page websites that enable you to carefully display Media Items to
        promote a general idea or message.</p>

        <g:if test='${flash.message}'>
            <div class='message'>${flash.message}</div>
        </g:if>
        <g:if test='${flash.error}'>
            <div class='errors'>${flash.error}</div>
        </g:if>

        <g:hasErrors bean="${registration}">
            <div class="errors">
                <g:renderErrors bean="${registration}" as="list" />
            </div>
        </g:hasErrors>
        <g:form url="[resource:registration, action:'updateRegistration']" >
            <div class="register_box">
                <div>
                    <p>Note: Your request has already been sent. You may still update your request information.</p>

                    <div class="left_side">
                        <p>Organization <span class="red">*</span></p>
                    </div>
                    <label for="organization" class="obscure">Organization</label>
                    <div class="right_side_name ${hasErrors(bean: registration, field: 'organization', 'errors')}">
                        <g:textField id="organization" name="organization" value="${registration?.organization}" autocomplete="off"/>
                    </div>
                    <div class="below_text">
                        <p class="italic">Enter your name (First Name, Middle Initial, Last Name).</p>
                    </div>
                </div>
                <div style="vertical-align: top;">
                    <div class="left_side">
                        <p>Description<span class="red">*</span></p>
                    </div>

                    <div style="vertical-align: top;" class="right_side ${hasErrors(bean: registration, field: 'description', 'errors')}">
                        <g:textArea width="380px" style="width:380px;" id="description" name="description" value="${registration?.description}" autocomplete="off"/>
                    </div>
                    <div class="below_text">
                        <p class="italic">Brief description of intended use.</p>
                    </div>
                </div>
                <br />
                <div class="input_row">
                    <g:submitButton name="updateChangesButton" value="Update"/>
                    <g:link name="cancelButton" controller="storefront" action="index">
                        <input type="button" value="Cancel"/>
                    </g:link>
                </div>
            </div>
        </g:form>
    </div>
</div>
</body>
</html>