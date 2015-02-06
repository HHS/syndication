%{--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%
<%@ page import="com.ctacorp.syndication.authentication.Role; com.ctacorp.syndication.authentication.User" %>

    <div class="form-group ${hasErrors(bean: userInstance, field: 'authority', 'error')}">
        <label for="name" class="col-sm-4 control-label">
            <g:message code="role.authority.label" default="Authority"/>
        </label>
        
        <div class="col-sm-8">
            <g:select id="authority" autocomplete="off" name="authority" optionKey="id" value="${currentRoleId}" optionValue="" from="${roles}" class="form-control"/>
        </div>
    </div>

    <div class="form-group ${hasErrors(bean: userInstance, field: 'name', 'error')} ">
        <label for="name" class="col-sm-4 control-label">
            <g:message code="user.name.label" default="Name"/>
        </label>

        <div class="col-sm-8">
            <g:textField autocomplete="off" placeholder="display name" name="name" value="${userInstance?.name}" class="form-control"/>
        </div>
    </div>

    <div class="form-group ${hasErrors(bean: userInstance, field: 'username', 'error')} required">
        <label for="username" class="col-sm-4 control-label">
            <g:message code="user.username.label" default="Email"/><span class="required-indicator">*</span>
        </label>

        <div class="col-sm-8">
            <g:textField autocomplete="off" name="username" placeholder="username" required="" value="${userInstance?.username}" class="form-control"/>
        </div>
    </div>

    <div class="form-group ${hasErrors(bean: userInstance, field: 'password', 'error')} required">
        <label for="password" class="col-sm-4 control-label">
            <g:message code="user.password.label" default="Password"/><span class="required-indicator">*</span>
        </label>

        <div class="col-sm-8">
            <g:passwordField autocomplete="off" name="password" placeholder="password" id="pass1" onkeyup="checkPass(); return false;" required="" value="${userInstance?.password}" class="form-control"/>
        </div>
    </div>

    <div class="form-group required">
        <label for="password" class="col-sm-4 control-label">
            Re-enter Password<span class="required-indicator">*</span>
        </label>

        <div class="col-sm-8">
            <g:passwordField autocomplete="off" name="passwordVerify" placeholder="re-type password" id="pass2" onkeyup="checkPass(); return false;" required="" value="${userInstance?.password}" class="form-control"/>
            <span id="confirmMessage" class="confirmMessage"></span>
        </div>
    </div>

    <sec:ifAnyGranted roles="ROLE_ADMIN">
        <div class="form-group">
            <label class="col-sm-4 control-label" for="subscriberId">Subscriber<span class="required-indicator">*</span></label>
            <div class="col-sm-8">
                <g:select from="${subscribers}" name="subscriberId" optionKey="id" optionValue="name" value="${currentSubscriber}" noSelection="['':'-Choose an Key-']" class="form-control"/>
            </div>
        </div>
    </sec:ifAnyGranted>

    <div class="form-group ${hasErrors(bean: userInstance, field: 'enabled', 'error')} ">
        <div class="col-sm-offset-4 col-sm-9">
            <label for="enabled">
                <g:checkBox name="enabled" value="${userInstance?.enabled}"/> &nbsp;<g:message code="user.enabled.label" default="Enabled"/>
            </label>
        </div>
    </div>

    <div class="form-group ${hasErrors(bean: userInstance, field: 'accountExpired', 'error')} ">
        <div class="col-sm-offset-4 col-sm-9">
            <label for="accountLocked">
                <g:checkBox name="accountExpired" value="${userInstance?.accountExpired}"/> &nbsp;<g:message code="user.accountExpired.label" default="Account Expired"/>
            </label>
        </div>
    </div>

    <div class="form-group ${hasErrors(bean: userInstance, field: 'accountLocked', 'error')} ">
        <div class="col-sm-offset-4 col-sm-9">
            <label for="accountLocked">
                <g:checkBox name="accountLocked" value="${userInstance?.accountLocked}"/> &nbsp;<g:message code="user.accountLocked.label" default="Account Locked"/>
            </label>
        </div>
    </div>

    <div class="form-group ${hasErrors(bean: userInstance, field: 'passwordExpired', 'error')} ">
        <div class="col-sm-offset-4 col-sm-9">
            <label for="passwordExpired">
                <g:checkBox name="passwordExpired" value="${userInstance?.passwordExpired}"/> &nbsp;<g:message code="user.passwordExpired.label" default="Password Expired"/>
            </label>
        </div>
    </div>

<script>
    $(document).ready(function(){
        var e = document.getElementById("authority");
        if(e.options[e.selectedIndex].innerHTML != "ROLE_PUBLISHER"){
            document.getElementById("subscriberId").value = "";
            document.getElementById("subscriberId").disabled = true;
        }
    });
    $(document).on('change','#authority', function(e){
        if(this.options[e.target.selectedIndex].text != "ROLE_PUBLISHER"){
            document.getElementById("subscriberId").value = "";
            document.getElementById("subscriberId").disabled = true;
        } else{
            document.getElementById("subscriberId").value = "";
            document.getElementById("subscriberId").disabled = false;
        }
    })
</script>