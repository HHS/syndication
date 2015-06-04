
%{--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<%@ page import="com.ctacorp.syndication.ExtendedAttribute" %>
<%@ page import="com.ctacorp.syndication.MediaItemSubscriber" %>


<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="name">Name<span class="required-indicator">*</span></label>
    <div class="col-md-4">
        <input name="name" type="text" value="${extendedAttributeInstance.name}" placeholder="attribute name" class="form-control input-md">
    </div>
</div>

<!-- Textarea -->
<div class="form-group">
    <label class="col-md-4 control-label" for="value">Value<span class="required-indicator">*</span></label>
    <div class="col-md-4">
        <textarea class="form-control" cols="40" rows="5" maxlength="255" placeholder="description" required="" value="" name="value">${extendedAttributeInstance?.value}</textarea>
    </div>
</div>

<!-- Select Basic -->
<div class="form-group">
    <label class="col-md-4 control-label" for="mediaItem">Media Item<span class="required-indicator">*</span></label>
    <div class="col-md-6">
        <sec:ifAnyGranted roles="ROLE_PUBLISHER">
            <g:select from="${com.ctacorp.syndication.media.MediaItem.findAllByIdInList(MediaItemSubscriber?.findAllBySubscriberId(user.subscriberId)?.mediaItem?.id, [sort: "name"])}" name="mediaItem.id" id="media" class="form-control" optionValue="name" optionKey="id" value="${extendedAttributeInstance?.mediaItem?.id}"/>
        </sec:ifAnyGranted>
        <sec:ifNotGranted roles="ROLE_PUBLISHER">
            <g:select from="${com.ctacorp.syndication.media.MediaItem.list([sort: "name"])}" name="mediaItem.id" id="media" class="form-control" optionValue="name" optionKey="id" value="${extendedAttributeInstance?.mediaItem?.id}"/>
        </sec:ifNotGranted>
    </div>
</div>


%{--<div class="fieldcontain ${hasErrors(bean: extendedAttributeInstance, field: 'name', 'error')} required">--}%
	%{--<label for="name">--}%
		%{--<g:message code="extendedAttribute.name.label" default="Name" />--}%
		%{--<span class="required-indicator">*</span>--}%
	%{--</label>--}%
	%{--<g:textArea name="name" cols="40" rows="5" maxlength="255" required="" value="${extendedAttributeInstance?.name}"/>--}%
%{--</div>--}%

%{--<div class="fieldcontain ${hasErrors(bean: extendedAttributeInstance, field: 'value', 'error')} required">--}%
	%{--<label for="value">--}%
		%{--<g:message code="extendedAttribute.value.label" default="Value" />--}%
		%{--<span class="required-indicator">*</span>--}%
	%{--</label>--}%
	%{--<g:textArea name="value" cols="40" rows="5" maxlength="255" required="" value="${extendedAttributeInstance?.value}"/>--}%
%{--</div>--}%

%{--<div class="fieldcontain ${hasErrors(bean: extendedAttributeInstance, field: 'mediaItem', 'error')} required">--}%
	%{--<label for="mediaItem">--}%
		%{--<g:message code="extendedAttribute.mediaItem.label" default="Media Item" />--}%
		%{--<span class="required-indicator">*</span>--}%
	%{--</label>--}%
	%{--<g:select id="mediaItem" name="mediaItem.id" from="${com.ctacorp.syndication.media.MediaItem.list()}" optionValue="${{it.id + ' ' + it.name}}" optionKey="id" required="" value="${extendedAttributeInstance?.mediaItem?.id}" class="many-to-one"/>--}%
%{--</div>--}%

