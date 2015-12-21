
%{--
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<%@ page import="com.ctacorp.syndication.SyndicationRequest" %>



<div class="fieldcontain ${hasErrors(bean: syndicationRequestInstance, field: 'requestedUrl', 'error')} required">
	<label for="requestedUrl">
		<g:message code="syndicationRequest.requestedUrl.label" default="Requested Url" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="requestedUrl" required="" value="${syndicationRequestInstance?.requestedUrl}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: syndicationRequestInstance, field: 'contactEmail', 'error')} required">
	<label for="contactEmail">
		<g:message code="syndicationRequest.contactEmail.label" default="Contact Email" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="contactEmail" required="" value="${syndicationRequestInstance?.contactEmail}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: syndicationRequestInstance, field: 'requesterNote', 'error')} ">
	<label for="requesterNote">
		<g:message code="syndicationRequest.requesterNote.label" default="Requester Note" />
		
	</label>
	<g:textField name="requesterNote" value="${syndicationRequestInstance?.requesterNote}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: syndicationRequestInstance, field: 'adminNote', 'error')} ">
	<label for="adminNote">
		<g:message code="syndicationRequest.adminNote.label" default="Admin Note" />
		
	</label>
	<g:textField name="adminNote" value="${syndicationRequestInstance?.adminNote}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: syndicationRequestInstance, field: 'status', 'error')} required">
	<label for="status">
		<g:message code="syndicationRequest.status.label" default="Status" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="status" required="" value="${syndicationRequestInstance?.status}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: syndicationRequestInstance, field: 'media', 'error')} ">
	<label for="media">
		<g:message code="syndicationRequest.media.label" default="Media" />
		
	</label>
	<g:select id="media" name="media.id" from="${syndication.MediaItem.list()}" optionKey="id" value="${syndicationRequestInstance?.media?.id}" class="many-to-one" noSelection="['null': '']"/>
</div>

