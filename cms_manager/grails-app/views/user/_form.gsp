<%--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--%>
<%@ page import="com.ctacorp.syndication.manager.cms.UserSubscriber; com.ctacorp.syndication.manager.cms.Subscriber; com.ctacorp.syndication.manager.cms.UserRole; com.ctacorp.syndication.manager.cms.Role; com.ctacorp.syndication.manager.cms.User" %>

<div class="form-group ${hasErrors(bean: userInstance, field: 'username', 'has-error')} ">
    <label class="control-label col-sm-3" for="username">
        <g:message code="user.username.label" default="Username" />
        <i class="fa fa-asterisk"></i>
    </label>
    <div class="col-sm-6">
        <g:if test="${actionName != 'edit'}">
            <g:textField class="form-control" name="username" value="${userInstance?.username}" />
        </g:if>
        <g:else>
            ${userInstance?.username}
        </g:else>
    </div>
</div>

<div class="form-group ${hasErrors(bean: userInstance, field: 'password', 'has-error')} ">
    <label class="control-label col-sm-3" for="username">
        <g:message code="user.password.label" default="Password" />
        <i class="fa fa-asterisk"></i>
    </label>
    <div class="col-sm-6">
        <g:passwordField class="form-control" name="password" required="" value="${instance?.password}" />
    </div>
</div>

<g:if test="${actionName != 'edit'}">

    <div class="form-group">
        <label class="control-label col-sm-3" for="role">
            <g:message code="role.label" />
            <i class="fa fa-asterisk"></i>
        </label>
        <div class="controls col-sm-6">
            <g:select class="form-control" id="role" name="role" from="${Role.list()}" optionKey="id" required="required" optionValue="authority" />
        </div>
    </div>

    <div class="form-group">
        <label class="control-label col-sm-3" for="subscribers">
            <g:message code="subscriber.label" />
        </label>
        <div class="controls col-sm-6">
            <g:select class="form-control" name="subscribers" from="${Subscriber.list()}" optionValue="name" optionKey="id" multiple="true" />
        </div>
    </div>

</g:if>

<g:if test="${actionName == 'edit'}">

<div class="form-group">
    <label class="control-label col-sm-3" for="role">
        <g:message code="role.label" />
        <i class="fa fa-asterisk"></i>
    </label>
    <div class="controls col-sm-6">
        <g:select class="form-control" id="role" name="role" from="${Role.list()}" optionKey="id" required="required" optionValue="authority" value="${UserRole.findByUser(instance)?.role?.id}" />
    </div>
</div>

<div class="form-group">
    <label class="control-label col-sm-3" for="subscribers">
        <g:message code="subscriber.label" />
    </label>
    <div class="controls col-sm-6">
        <g:select class="form-control" id="subscribers" name="subscribers" from="${Subscriber.list()}" optionValue="name" optionKey="id" value="${UserSubscriber.findAllByUser(instance).subscriber}" multiple="true" />
    </div>
</div>

<div class="form-group">
    <label class="control-label col-sm-3" for="accountExpired">
        <g:message code="user.accountExpired.label" default="Account Expired" />
    </label>
    <div class="col-sm-6" style="padding-top: .5em;">
        <g:checkBox name="accountExpired" id="accountExpired" value="${instance?.accountExpired}" />
    </div>
</div>

<div class="form-group">
    <label class="control-label col-sm-3" for="accountLocked">
        <g:message code="user.accountLocked.label" default="Account Locked" />
    </label>
    <div class="col-sm-6" style="padding-top: .5em;">
        <g:checkBox name="accountLocked" id="accountLocked" value="${instance?.accountLocked}" />
    </div>
</div>

<div class="form-group">
    <label class="control-label col-sm-3" for="enabled">
        <g:message code="user.enabled.label" default="Enabled" />
    </label>
    <div class="col-sm-6" style="padding-top: .5em;">
        <g:checkBox name="enabled" id="enabled" value="${instance?.enabled}" />
    </div>
</div>

<div class="form-group">
    <label class="control-label col-sm-3" for="passwordExpired">
        <g:message code="user.passwordExpired.label" default="Password Expired" />
    </label>
    <div class="col-sm-6" style="padding-top: .5em;">
        <g:checkBox name="passwordExpired" id="passwordExpired" value="${instance?.passwordExpired}" />
    </div>
</div>

</g:if>


