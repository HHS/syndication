<%--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--%>
<g:if test="${instance?.subscriberId}">
    <dt><g:message code="subscriber.subscriberId.label"/></dt>
    <dd><g:fieldValue bean="${instance}" field="subscriberId"/></dd>
</g:if>

<g:if test="${instance?.name}">
    <dt><g:message code="subscriber.name.label"/></dt>
    <dd><g:fieldValue bean="${instance}" field="name"/></dd>
</g:if>

<g:if test="${instance?.email}">
    <dt><g:message code="subscriber.email.label"/></dt>
    <dd><g:fieldValue bean="${instance}" field="email"/></dd>
</g:if>

<g:if test="${instance?.dateCreated}">
    <dt><g:message code="subscriber.dateCreated.label"/></dt>
    <dd><g:formatDate date="${instance?.dateCreated}"/></dd>
</g:if>

<g:if test="${instance?.lastUpdated}">
    <dt><g:message code="subscriber.lastUpdated.label" /></dt>
    <dd><g:formatDate date="${instance?.lastUpdated}" /></dd>
</g:if>

<div id="keyAgreement-panel" class="panel panel-default">
    <div class="panel-heading">
        <b><g:message code="keyAgreement.jsonExport.label"/></b>
    </div>
    <div class="panel-body">
        <g:if test="${instance?.keyAgreement?.jsonExport}">
            <p class="diffie-hellman-key">${instance?.keyAgreement?.jsonExport}</p>
        </g:if>
        <g:else>
            <p><g:link controller="keyAgreement" params="[entity2:instance?.name]" action="create" id="${instance?.keyAgreement?.id}"><g:message code="keyAgreement.create.label"/></g:link></p>
        </g:else>
    </div>
</div>