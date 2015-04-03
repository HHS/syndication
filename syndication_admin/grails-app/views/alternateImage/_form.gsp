
%{--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%
<%@ page import="com.ctacorp.syndication.AlternateImage" %>
<%@ page import="com.ctacorp.syndication.MediaItemSubscriber" %>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="width"><g:message code="alternateImage.width.label" default="Width" /></label>
    <div class="col-md-4">
        <input name="width" type="number" placeholder="image width" class="form-control input-md" value="${alternateImageInstance.width}">
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="height"><g:message code="alternateImage.height.label" default="Height" /></label>
    <div class="col-md-4">
        <input name="height" type="number" placeholder="image height" class="form-control input-md" value="${alternateImageInstance.height}">
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="name"><g:message code="alternateImage.name.label" default="Name" /></label>
    <div class="col-md-4">
        <input name="name" type="text" placeholder="image name" class="form-control input-md" value="${alternateImageInstance?.name}">
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="url">
        <g:message code="alternateImage.url.label" default="Url" />
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-4">
        <input name="url" type="url" placeholder="location" class="form-control input-md" value="${alternateImageInstance?.url}">
    </div>
</div>

<!-- Select Basic -->
<div class="form-group">
    <label class="col-md-4 control-label" for="mediaItem">
        <g:message code="alternateImage.mediaItem.label" default="Media Item" />
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-4">
        <sec:ifAnyGranted roles="ROLE_PUBLISHER">
            <g:select from="${com.ctacorp.syndication.media.MediaItem.findAllByIdInList(MediaItemSubscriber?.findAllBySubscriberId(user.subscriberId)?.mediaItem?.id)}" name="mediaItem.id" id="media" class="form-control" optionValue="name" optionKey="id" value="${alternateImageInstance?.mediaItem?.id}"/>
        </sec:ifAnyGranted>
        <sec:ifNotGranted roles="ROLE_PUBLISHER">
            <g:select from="${com.ctacorp.syndication.media.MediaItem.list()}" name="mediaItem.id" id="media" class="form-control" optionValue="name" optionKey="id" value="${alternateImageInstance?.mediaItem?.id}"/>
        </sec:ifNotGranted>
        
    </div>
</div>