
<!--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

-->
<%@ page import="com.ctacorp.syndication.Language; tagcloud.domain.TagType; tagcloud.domain.Tag" %>

<div class="fieldcontain ${hasErrors(bean: tagInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="tag.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${tagInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: tagInstance, field: 'type', 'error')} required">
	<label for="type">
		<g:message code="tag.tagType.label" default="Tag Type" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="type" name="type.id" from="${TagType.list()}" optionKey="id" required="" value="${tagInstance?.type?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: tagInstance, field: 'language', 'error')} required">
	<label for="language">
		<g:message code="tag.language.label" default="Language" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="language" name="language.id" from="${Language.list()}" optionKey="id" required="" value="${tagInstance?.language?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: tagInstance, field: 'contentItems', 'error')} ">
	<label for="contentItems">
		<g:message code="tag.contentItems.label" default="Content Items" />
		
	</label>
	<g:select name="contentItems" from="${tagcloud.domain.ContentItem.list()}" multiple="multiple" optionKey="id" size="5" value="${tagInstance?.contentItems*.id}" class="many-to-many"/>
</div>

