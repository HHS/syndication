<%--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--%>
<td><g:link action="show" id="${instance.id}">${fieldValue(bean: instance, field: "id")}</g:link></td>
<td><g:link controller="subscription" action="show" id="${instance.subscription.id}">${fieldValue(bean: instance, field: "subscription.mediaId")}</g:link></td>
<td><g:link controller="emailSubscriber" action="show" id="${instance.emailSubscriber.id}">${fieldValue(bean: instance, field: "emailSubscriber.email")}</g:link></td>
<g:if test="${instance.deliveryFailureLogId && !instance.isPending}">
    <td class="delivery-failed danger">${message(code: 'subscription.delivery.status.failure')}</td>
    <td>${fieldValue(bean: instance, field: "deliveryFailureLogId")}</td>
</g:if>
<g:elseif test="${instance.isPending}">
    <td class="delivery-pending warning">${message(code: 'subscription.delivery.status.pending')}</td>
    <td></td>
</g:elseif>
<g:else>
    <td class="delivery-success success">${message(code: 'subscription.delivery.status.success')}</td>
    <td></td>
</g:else>
