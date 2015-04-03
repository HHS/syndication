<%@ page import="com.ctacorp.syndication.manager.cms.UserSubscriber; com.ctacorp.syndication.manager.cms.UserRole; org.apache.commons.lang3.RandomStringUtils" %>
<%--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--%>
<g:if test="${instance?.username}">
    <dt><g:message code="user.username.label" default="Username" /></dt>
    <dd><g:fieldValue bean="${instance}" field="username"/></dd>
</g:if>

<g:if test="${instance?.password}">
    <dt><g:message code="user.password.label" default="Password" /></dt>
    <dd>${RandomStringUtils.random(20,'•')}</dd>
</g:if>

<dt><g:message code="role.label" /></dt>
<dd>
    <g:set var="role" value="${UserRole.findByUser(instance)?.role}"/>
    <g:link controller="role" action="show" id="${role?.id}">${role?.authority}</g:link>
</dd>

<dt><g:message code="user.subscribers.label" /></dt>
<dd>
    <g:set var="subscribers" value="${UserSubscriber.findAllByUser(instance)*.subscriber}"/>
    <g:set var="subscriberIndex" value="${0}"/>
    <div>
        <g:each in="${subscribers}" var="subscriber">
            <g:link controller="subscriber" action="show" id="${subscriber.id}">${subscriber.name}</g:link><g:if test="${++subscriberIndex < subscribers.size()}">,</g:if>
        </g:each>
    </div>
</dd>

<dt><g:message code="user.accountExpired.label" default="Account Expired" /></dt>
<dd><g:formatBoolean boolean="${instance?.accountExpired}" /></dd>

<dt><g:message code="user.accountLocked.label" default="Account Locked" /></dt>
<dd><g:formatBoolean boolean="${instance?.accountLocked}" /></dd>

<dt><g:message code="user.enabled.label" default="Enabled" /></dt>
<dd><g:formatBoolean boolean="${instance?.enabled}" /></dd>

<dt><g:message code="user.passwordExpired.label" default="Password Expired" /></dt>
<dd><g:formatBoolean boolean="${instance?.passwordExpired}" /></dd>

<g:if test="${instance?.dateCreated}">
    <dt><g:message code="user.dateCreated.label" default="Date Created" /></dt>
    <dd><g:fieldValue bean="${instance}" field="dateCreated"/></dd>
</g:if>

<g:if test="${instance?.lastUpdated}">
    <dt><g:message code="user.lastUpdated.label" default="Last Updated" /></dt>
    <dd><g:fieldValue bean="${instance}" field="lastUpdated"/></dd>
</g:if>
