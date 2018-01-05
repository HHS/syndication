<%--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--%>
<g:if test="${instance?.instanceName}">
        <dt><g:message code="rhythmyxSubscriber.instanceName.label" /></dt>
        <dd>${instance?.instanceName}</dd>
</g:if>

<g:if test="${instance?.rhythmyxHost}">
    <dt><g:message code="rhythmyxSubscriber.rhythmyxHost.label" /></dt>
    <dd><g:fieldValue bean="${instance}" field="rhythmyxHost"/></dd>
</g:if>

<g:if test="${instance?.rhythmyxPort}">
    <dt><g:message code="rhythmyxSubscriber.rhythmyxPort.label" /></dt>
    <dd><g:fieldValue bean="${instance}" field="rhythmyxPort"/></dd>
</g:if>

<g:if test="${instance?.rhythmyxCommunity}">
    <dt><g:message code="rhythmyxSubscriber.rhythmyxCommunity.label" /></dt>
    <dd><g:fieldValue bean="${instance}" field="rhythmyxCommunity"/></dd>
</g:if>

<g:if test="${instance?.subscriber}">
    <dt><g:message code="rhythmyxSubscriber.subscriber.label" /></dt>
    <dd><g:link controller="subscriber" action="show" id="${instance?.subscriber?.id}">${instance?.subscriber?.name}</g:link></dd>
</g:if>

<g:if test="${instance?.rhythmyxWorkflow}">

    <dt><g:message code="default.rhythmyxWorkflow.autoPublish.label" /></dt>
    <dd><g:fieldValue bean="${instance?.rhythmyxWorkflow}" field="autoPublish"/></dd>

    <dt><g:message code="default.rhythmyxWorkflow.importTransitions.label" /></dt>
    <dd><g:fieldValue bean="${instance?.rhythmyxWorkflow}" field="importTransitions"/></dd>

    <dt><g:message code="default.rhythmyxWorkflow.updateTransitions.label" /></dt>
    <dd><g:fieldValue bean="${instance?.rhythmyxWorkflow}" field="updateTransitions"/></dd>

    <dt><g:message code="default.rhythmyxWorkflow.updateAutoPublishTransitions.label" /></dt>
    <dd><g:fieldValue bean="${instance?.rhythmyxWorkflow}" field="updateAutoPublishTransitions"/></dd>

    <dt><g:message code="default.rhythmyxWorkflow.deleteTransitions.label" /></dt>
    <dd><g:fieldValue bean="${instance?.rhythmyxWorkflow}" field="deleteTransitions"/></dd>

    <dt><g:message code="default.rhythmyxWorkflow.deleteAutoPublishTransitions.label" /></dt>
    <dd><g:fieldValue bean="${instance?.rhythmyxWorkflow}" field="deleteAutoPublishTransitions"/></dd>

</g:if>

<g:if test="${instance?.dateCreated}">
    <dt><g:message code="rhythmyxSubscriber.dateCreated.label" /></dt>
    <dd><g:fieldValue bean="${instance}" field="dateCreated"/></dd>
</g:if>

<g:if test="${instance?.lastUpdated}">
    <dt><g:message code="rhythmyxSubscriber.lastUpdated.label" /></dt>
    <dd><g:fieldValue bean="${instance}" field="lastUpdated"/></dd>
</g:if>
