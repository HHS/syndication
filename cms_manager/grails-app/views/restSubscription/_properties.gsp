<%--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--%>
<g:if test="${instance?.sourceUrl}">
    <dt><g:message code="restSubscription.sourceUrl.label" /></dt>
    <dd><a target="_blank" href="${instance?.sourceUrl}">${instance?.sourceUrl}</a></dd>
</g:if>

<g:if test="${instance?.title}">
    <dt><g:message code="restSubscription.title.label" /></dt>
    <dd>${instance?.title}</dd>
</g:if>

<g:if test="${instance?.restSubscriber}">
    <dt><g:message code="restSubscription.restSubscriber.deliveryEndpoint.label" /></dt>
    <dd><g:link controller="restSubscriber" action="show" id="${instance?.restSubscriber?.id}">${instance?.restSubscriber?.deliveryEndpoint}</g:link></dd>
</g:if>

<g:if test="${instance?.subscription}">
    <dt><g:message code="restSubscription.subscription.mediaId.label" /></dt>
    <dd><g:link controller="subscription" action="show" id="${instance?.subscription?.id}">${instance?.subscription?.mediaId}</g:link></dd>
</g:if>

<g:if test="${instance?.subscription}">
    <dt><g:message code="restSubscription.subscription.mediaUri.label" /></dt>
    <dd><a href="${instance?.subscription?.mediaUri}">${instance?.subscription?.mediaUri}</a></dd>
</g:if>

<dt>${message(code: 'subscription.delivery.status.label')}</dt>
<g:if test="${instance.deliveryFailureLogId && !instance.isPending}">
    <dd class="property-value delivery-failed">${message(code: 'subscription.delivery.status.updateFailed')}</dd>
</g:if>
<g:elseif test="${instance.deliveryFailureLogId && instance.isPending}">
    <dd class="property-value delivery-failed">${message(code: 'subscription.delivery.status.failed')}</dd>
</g:elseif>
<g:elseif test="${!instance.deliveryFailureLogId && instance.isPending}">
    <dd class="property-value delivery-pending">${message(code: 'subscription.delivery.status.pending')}</dd>
</g:elseif>
<g:else>
    <dd class="property-value delivery-success">${message(code: 'subscription.delivery.status.updated')}</dd>
</g:else>

<g:if test="${instance?.deliveryFailureLogId}">
    <dt>${message(code: 'subscription.delivery.error.id.label')}</dt>
    <dd><g:fieldValue bean="${instance}" field="deliveryFailureLogId"/></dd>
</g:if>

<g:if test="${instance?.dateCreated}">
    <dt><g:message code="restSubscription.dateCreated.label" /></dt>
    <dd><g:fieldValue bean="${instance}" field="dateCreated"/></dd>
</g:if>
