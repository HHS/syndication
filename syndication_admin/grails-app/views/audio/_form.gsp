%{--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<%@ page import="com.ctacorp.syndication.media.Audio" %>


<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="name">Name<span class="required-indicator">*</span></label>
    <div class="col-md-8">
        <input id="name" name="name" required="" value="${audioInstance?.name}" type="text" placeholder="audio name" class="form-control input-md">
    </div>
</div>

<!-- Textarea -->
<div class="form-group">
    <label class="col-md-4 control-label" for="description">Description</label>
    <div class="col-md-8">
        <textarea class="form-control" id="description" name="description" maxlength="2000">${audioInstance?.description}</textarea>
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="sourceUrl">Source Url<span class="required-indicator">*</span></label>
    <div class="col-md-8">
        <input id="sourceUrl" name="sourceUrl" maxlength="2000" required="" value="${audioInstance?.sourceUrl}" type="url" placeholder="source url" class="form-control input-md">
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="targetUrl">Target Url</label>
    <div class="col-md-8">
        <input id="targetUrl" name="targetUrl" maxlength="2000" value="${audioInstance?.targetUrl}" type="url" placeholder="target url" class="form-control input-md">
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="customThumbnailUrl">Custom Thumbnail Url</label>
    <div class="col-md-8">
        <input id="customThumbnailUrl" name="customThumbnailUrl" maxlength="2000" value="${audioInstance?.customThumbnailUrl}" type="url" placeholder="thumbnail url" class="form-control input-md">
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="customPreviewUrl">Custom Preview Url</label>
    <div class="col-md-8">
        <input id="customPreviewUrl" name="customPreviewUrl" maxlength="2000" value="${audioInstance?.customPreviewUrl}" type="url" placeholder="preview url" class="form-control input-md">
    </div>
</div>

<div class="form-group ${hasErrors(bean: audioInstance, field: 'dateSyndicationCaptured', 'error')} required">
    <label class="col-md-4 control-label" for="dateSyndicationCaptured">
        <g:message code="audio.dateSyndicationCaptured.label" default="Date Syndication Captured"/>
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateSyndicationCaptured" precision="minute" relativeYears="[-20..1]" value="${audioInstance?.dateSyndicationCaptured}"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: audioInstance, field: 'dateSyndicationUpdated', 'error')} required">
    <label class="col-md-4 control-label" for="dateSyndicationUpdated">
        <g:message code="audio.dateSyndicationUpdated.label" default="Date Syndication Updated"/>
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateSyndicationUpdated" precision="minute" relativeYears="[-20..1]" value="${audioInstance?.dateSyndicationUpdated}"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: audioInstance, field: 'dateSyndicationVisible', 'error')} required">
    <label class="col-md-4 control-label" for="dateSyndicationVisible">
        <g:message code="audio.dateSyndicationVisible.label" default="Date Syndication Visible"/>
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateSyndicationVisible" precision="minute" relativeYears="[-20..1]" value="${audioInstance?.dateSyndicationVisible}"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: audioInstance, field: 'dateContentAuthored', 'error')} ">
    <label class="col-md-4 control-label" for="dateContentAuthored">
        <g:message code="audio.dateContentAuthored.label" default="Date Content Authored"/>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateContentAuthored" precision="minute" relativeYears="[-20..1]" value="${audioInstance?.dateContentAuthored}" default="none" noSelection="['': '']"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: audioInstance, field: 'dateContentUpdated', 'error')} ">
    <label class="col-md-4 control-label" for="dateContentUpdated">
        <g:message code="audio.dateContentUpdated.label" default="Date Content Updated"/>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateContentUpdated" precision="minute" relativeYears="[-20..1]" value="${audioInstance?.dateContentUpdated}" default="none" noSelection="['': '']"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: audioInstance, field: 'dateContentPublished', 'error')} ">
    <label class="col-md-4 control-label" for="dateContentPublished">
        <g:message code="audio.dateContentPublished.label" default="Date Content Published"/>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateContentPublished" precision="minute" relativeYears="[-20..1]" value="${audioInstance?.dateContentPublished}" default="none" noSelection="['': '']"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: audioInstance, field: 'dateContentReviewed', 'error')} ">
    <label class="col-md-4 control-label" for="dateContentReviewed">
        <g:message code="audio.dateContentReviewed.label" default="Date Content Reviewed"/>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateContentReviewed" precision="minute" relativeYears="[-20..1]" value="${audioInstance?.dateContentReviewed}" default="none" noSelection="['': '']"/>
    </div>
</div>

<!-- Select Basic -->
<div class="form-group ${hasErrors(bean: audioInstance, field: 'dateContentReviewed', 'error')} ">
    <label class="col-md-4 control-label" for="language">Language<span class="required-indicator">*</span></label>
    <div class="col-md-8">
        <g:select from="${com.ctacorp.syndication.Language.findAllByIsActive(true, [sort: "name"])}" name="language.id" id="language" optionKey="id" optionValue="name" value="${audioInstance?.language?.id}" class="form-control" noSelection="${['null':'-Choose a Language-']}"/>
    </div>
</div>

<!-- Radio Buttons -->
<div class="form-group">
    <label class="col-md-4 control-label" for="active">
        <g:message code="audio.active.label" default="Active"/>

    </label>
    <div class="col-md-8">
        <g:checkBox name="active" value="${audioInstance?.active}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="visibleInStorefront">
        <g:message code="audio.visibleInStorefront.label" default="Visible In Storefront"/>

    </label>
    <div class="col-md-8">
        <g:checkBox name="visibleInStorefront" value="${audioInstance?.visibleInStorefront}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="manuallyManaged">
        <g:message code="audio.manuallyManaged.label" default="Manually Managed"/>

    </label>
    <div class="col-md-8">
        <g:checkBox name="manuallyManaged" value="${audioInstance?.manuallyManaged}"/>
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="externalGuid">External Guid</label>
    <div class="col-md-8">
        <input id="externalGuid" name="externalGuid" value="${audioInstance?.externalGuid}" placeholder="guid" class="form-control input-md">
    </div>
</div>

<!-- Select Basic -->
<div class="form-group">
    <label class="col-md-4 control-label" for="source">Source<span class="required-indicator">*</span></label>
    <div class="col-md-8">
        <g:select from="${com.ctacorp.syndication.Source.list([sort: "name"])}" name="source.id" id="source" class="form-control" optionValue="name" optionKey="id" value="${audioInstance?.source?.id}"/>
    </div>
</div>

<!-- Number input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="duration">Duration (Seconds)</label>
    <div class="col-md-4">
        <input name="duration" type="number" min="0" max="2147483646" value="${audioInstance.duration}" id="duration" class="form-control input-md">
    </div>
</div>

<g:render template="/mediaItem/owner"/>