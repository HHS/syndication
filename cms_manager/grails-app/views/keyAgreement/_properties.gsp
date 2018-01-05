<%--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--%>
<%@ page import="com.ctacorp.syndication.manager.cms.KeyAgreement; com.ctacorp.syndication.manager.cms.Subscriber" %>

<g:set var="subscriber" value="${Subscriber.findByKeyAgreement(instance as KeyAgreement)}"/>

<dt><g:message code="emailSubscriber.subscriber.label"/></dt>
<g:if test="${subscriber}">
    <dd>
        <g:link controller="subscriber" action="show" id="${subscriber.id}">${fieldValue(bean: instance, field: "entity2")}</g:link>
    </dd>
</g:if>
<g:else>
    <dd><g:fieldValue bean="${instance}" field="entity2"/></dd>
</g:else>

<dt><g:message code="keyAgreement.dateCreated.label"/></dt>
<dd><g:formatDate date="${instance?.dateCreated}"/></dd>

<div id="keyAgreement-panel" class="panel panel-default">
    <div class="panel-heading">
        <b><g:message code="keyAgreement.jsonExport.label"/></b>
    </div>
    <div class="panel-body">
        <p class="diffie-hellman-key">${instance?.jsonExport}</p>
    </div>
</div>

