
<!--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

-->
<%@ page import="tagcloud.domain.ContentItem" %>



<div class="fieldcontain ${hasErrors(bean: contentItemInstance, field: 'url', 'error')} required">
	<label for="url">
		<g:message code="contentItem.url.label" default="Url" />
		<span class="required-indicator">*</span>
	</label>
	<g:textArea name="url" cols="40" rows="5" maxlength="5000" required="" value="${contentItemInstance?.url}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: contentItemInstance, field: 'externalUID', 'error')} ">
	<label for="externalUID">
		<g:message code="contentItem.externalUID.label" default="External UID" />
		
	</label>
	<g:textField name="externalUID" value="${contentItemInstance?.externalUID}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: contentItemInstance, field: 'syndicationId', 'error')} ">
	<label for="syndicationId">
		<g:message code="contentItem.syndicationId.label" default="Syndication Id" />
		
	</label>
	<g:field name="syndicationId" type="number" min="0" max="9223372036854775806" value="${contentItemInstance.syndicationId}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: contentItemInstance, field: 'tags', 'error')} ">
	<label for="tags">
		<g:message code="contentItem.tags.label" default="Tags" />
		
	</label>
	
</div>

