<%--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--%>
<g:if test="${instance?.systemTitle}">
    <dt><g:message code="rhythmyxSubscription.systemTitle.label" /></dt>
    <dd><g:fieldValue bean="${instance}" field="systemTitle"/></dd>
</g:if>

<g:if test="${instance?.targetFolder}">
    <dt><g:message code="rhythmyxSubscription.targetFolder.label" /></dt>
    <dd>${fieldValue(bean: instance, field: "targetFolder").replaceAll(',','')}</dd>
</g:if>

<g:if test="${instance?.contentType}">
    <dt><g:message code="rhythmyxSubscription.contentType.label" /></dt>
    <dd><g:fieldValue bean="${instance}" field="contentType"/></dd>
</g:if>

<g:if test="${instance?.contentId}">
    <dt><g:message code="rhythmyxSubscription.contentId.label" /></dt>
    <dd><g:fieldValue bean="${instance}" field="contentId"/></dd>
</g:if>

<g:if test="${instance?.rhythmyxSubscriber}">
    <dt><g:message code="rhythmyxSubscription.rhythmyxSubscriber.label" /></dt>
    <dd><g:link controller="rhythmyxSubscriber" action="show" id="${instance?.rhythmyxSubscriber?.id}">${instance?.rhythmyxSubscriber?.instanceName}</g:link></dd>
</g:if>

<g:if test="${instance?.subscription}">
    <dt><g:message code="rhythmyxSubscription.subscription.mediaId.label" /></dt>
    <dd><g:link controller="subscription" action="show" id="${instance?.subscription?.id}">${instance?.subscription?.mediaId}</g:link></dd>
</g:if>

<g:if test="${instance?.subscription}">
    <dt><g:message code="rhythmyxSubscription.subscription.label" /></dt>
    <dd><a target="_blank" href="${instance?.subscription?.mediaUri}">${instance?.subscription?.mediaUri}</a></dd>
</g:if>

<g:if test="${instance?.sourceUrl}">
    <dt><g:message code="rhythmyxSubscription.sourceUrl.label" /></dt>
    <dd><a target="_blank" href="${instance?.sourceUrl}">${instance?.sourceUrl}</a></dd>
</g:if>

<g:if test="${instance?.rhythmyxWorkflow}">

    <dt><g:message code="rhythmyxWorkflow.autoPublish.label" /></dt>
    <dd><g:fieldValue bean="${instance?.rhythmyxWorkflow}" field="autoPublish"/></dd>

    <dt><g:message code="rhythmyxWorkflow.importTransitions.label" /></dt>
    <dd><g:fieldValue bean="${instance?.rhythmyxWorkflow}" field="importTransitions"/></dd>

    <dt><g:message code="rhythmyxWorkflow.updateTransitions.label" /></dt>
    <dd><g:fieldValue bean="${instance?.rhythmyxWorkflow}" field="updateTransitions"/></dd>

    <dt><g:message code="rhythmyxWorkflow.updateAutoPublishTransitions.label" /></dt>
    <dd><g:fieldValue bean="${instance?.rhythmyxWorkflow}" field="updateAutoPublishTransitions"/></dd>

    <dt><g:message code="rhythmyxWorkflow.deleteTransitions.label" /></dt>
    <dd><g:fieldValue bean="${instance?.rhythmyxWorkflow}" field="deleteTransitions"/></dd>

    <dt><g:message code="rhythmyxWorkflow.deleteAutoPublishTransitions.label" /></dt>
    <dd><g:fieldValue bean="${instance?.rhythmyxWorkflow}" field="deleteAutoPublishTransitions"/></dd>

</g:if>

<dt>${message(code: 'subscription.delivery.status.label')}</dt>
<g:if test="${instance.deliveryFailureLogId}">
    <dd>${message(code: 'subscription.delivery.status.failure')}</dd>
</g:if>
<g:elseif test="${!instance.deliveryFailureLogId && !instance.subscription}">
    <dd>${message(code: 'subscription.delivery.status.pending')}</dd>
</g:elseif>
<g:else>
    <dd>${message(code: 'subscription.delivery.status.success')}</dd>
</g:else>

<g:if test="${instance.deliveryFailureLogId}">
    <dt>${message(code: 'subscription.delivery.error.id.label')}</dt>
    <dd><g:fieldValue bean="${instance}" field="deliveryFailureLogId"/></dd>
</g:if>

<g:if test="${instance?.dateCreated}">
    <dt><g:message code="rhythmyxSubscription.dateCreated.label" /></dt>
    <dd><g:fieldValue bean="${instance}" field="dateCreated"/></dd>
</g:if>

<g:if test="${instance?.lastUpdated}">
    <dt><g:message code="rhythmyxSubscription.lastUpdated.label" /></dt>
    <dd><g:fieldValue bean="${instance}" field="lastUpdated"/></dd>
</g:if>