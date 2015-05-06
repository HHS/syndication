%{--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<%@ page import="com.ctacorp.syndication.media.Periodical" %>

<!-- Text input-->
<div class="form-group" xmlns="http://www.w3.org/1999/html">
    <label class="col-md-4 control-label" for="name">Name<span class="required-indicator">*</span></label>
    <div class="col-md-8">
        <input id="name" name="name" required="" value="${periodicalInstance?.name}" type="text" placeholder="periodical name" class="form-control input-md">
    </div>
</div>

<!-- Textarea -->
<div class="form-group">
    <label class="col-md-4 control-label" for="description">Description</label>
    <div class="col-md-8">
        <textArea class="form-control" id="description" name="description" maxlength="2000">${periodicalInstance?.description}</textarea>
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="sourceUrl">Source Url<span class="required-indicator">*</span></label>
    <div class="col-md-6">
        <input id="sourceUrl" name="sourceUrl" maxlength="2000" required="" value="${periodicalInstance?.sourceUrl}" type="url" placeholder="source url" class="form-control input-md">
    </div>
    <div class="col-md-2">
        <button type="button" id="urlModal" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#myModal">
            Verify URL
        </button>
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <g:render template="../mediaTestPreview/testModal"/>
        </div>
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="targetUrl">targetUrl</label>
    <div class="col-md-8">
        <input id="targetUrl" name="targetUrl" maxlength="2000" value="${periodicalInstance?.targetUrl}" type="url" placeholder="target url" class="form-control input-md">
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="customThumbnailUrl">Custom Thumbnail Url</label>
    <div class="col-md-8">
        <input id="customThumbnailUrl" name="customThumbnailUrl" maxlength="2000" value="${periodicalInstance?.customThumbnailUrl}" type="url" placeholder="thumbnail url" class="form-control input-md">
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="customPreviewUrl">Custom Preview Url</label>
    <div class="col-md-8">
        <input id="customPreviewUrl" name="customPreviewUrl" maxlength="2000" value="${periodicalInstance?.customPreviewUrl}" type="url" placeholder="preview url" class="form-control input-md">
    </div>
</div>

<!-- Select Basic -->
<div class="form-group">
    <label class="col-md-4 control-label" for="period">Period<span class="required-indicator">*</span></label>
    <div class="col-md-8">
        <g:select from="${com.ctacorp.syndication.media.Periodical.Period}" name="period" id="period" optionKey="${{period -> "${period.name()}"}}" value="${periodicalInstance?.period}" class="form-control"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: periodicalInstance, field: 'dateSyndicationCaptured', 'error')} required">
    <label class="col-md-4 control-label" for="dateSyndicationCaptured">
        <g:message code="periodical.dateSyndicationCaptured.label" default="Date Syndication Captured"/>
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateSyndicationCaptured" precision="minute" relativeYears="[-20..1]" value="${periodicalInstance?.dateSyndicationCaptured}"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: periodicalInstance, field: 'dateSyndicationUpdated', 'error')} required">
    <label class="col-md-4 control-label" for="dateSyndicationUpdated">
        <g:message code="periodical.dateSyndicationUpdated.label" default="Date Syndication Updated"/>
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateSyndicationUpdated" precision="minute" relativeYears="[-20..1]" value="${periodicalInstance?.dateSyndicationUpdated}"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: periodicalInstance, field: 'dateSyndicationVisible', 'error')} required">
    <label class="col-md-4 control-label" for="dateSyndicationVisible">
        <g:message code="periodical.dateSyndicationVisible.label" default="Date Syndication Visible"/>
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateSyndicationVisible" precision="minute" relativeYears="[-20..1]" value="${periodicalInstance?.dateSyndicationVisible}"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: periodicalInstance, field: 'dateContentAuthored', 'error')} ">
    <label class="col-md-4 control-label" for="dateContentAuthored">
        <g:message code="periodical.dateContentAuthored.label" default="Date Content Authored"/>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateContentAuthored" precision="minute" relativeYears="[-20..1]" value="${periodicalInstance?.dateContentAuthored}" default="none" noSelection="['': '']"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: periodicalInstance, field: 'dateContentUpdated', 'error')} ">
    <label class="col-md-4 control-label" for="dateContentUpdated">
        <g:message code="periodical.dateContentUpdated.label" default="Date Content Updated"/>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateContentUpdated" precision="minute" relativeYears="[-20..1]" value="${periodicalInstance?.dateContentUpdated}" default="none" noSelection="['': '']"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: periodicalInstance, field: 'dateContentPublished', 'error')} ">
    <label class="col-md-4 control-label" for="dateContentPublished">
        <g:message code="periodical.dateContentPublished.label" default="Date Content Published"/>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateContentPublished" precision="minute" relativeYears="[-20..1]" value="${periodicalInstance?.dateContentPublished}" default="none" noSelection="['': '']"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: periodicalInstance, field: 'dateContentReviewed', 'error')} ">
    <label class="col-md-4 control-label" for="dateContentReviewed">
        <g:message code="periodical.dateContentReviewed.label" default="Date Content Reviewed"/>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateContentReviewed" precision="minute" relativeYears="[-20..1]" value="${periodicalInstance?.dateContentReviewed}" default="none" noSelection="['': '']"/>
    </div>
</div>

<!-- Select Basic -->
<div class="form-group">
    <label class="col-md-4 control-label" for="language">Language<span class="required-indicator">*</span></label>
    <div class="col-md-8">
        <g:select from="${com.ctacorp.syndication.Language.findAllByIsActive(true, [sort: "name"])}" name="language.id" id="language" optionKey="id" optionValue="name" value="${periodicalInstance?.language?.id}" class="form-control" noSelection="${['null':'-Choose a Language-']}"/>
    </div>
</div>

<!-- Radio Buttons -->
<div class="form-group">
    <label class="col-md-4 control-label" for="active">
        <g:message code="periodical.active.label" default="Active"/>

    </label>
    <div class="col-md-8">
        <g:checkBox name="active" value="${periodicalInstance?.active}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="visibleInStorefront">
        <g:message code="periodical.visibleInStorefront.label" default="Visible In Storefront"/>

    </label>
    <div class="col-md-8">
        <g:checkBox name="visibleInStorefront" value="${periodicalInstance?.visibleInStorefront}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="manuallyManaged">
        <g:message code="periodical.manuallyManaged.label" default="Manually Managed"/>

    </label>
    <div class="col-md-8">
        <g:checkBox name="manuallyManaged" value="${periodicalInstance?.manuallyManaged}"/>
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="externalGuid">External Guid</label>
    <div class="col-md-8">
        <input id="externalGuid" name="externalGuid" value="${periodicalInstance?.externalGuid}" placeholder="guid" class="form-control input-md">
    </div>
</div>

<!-- Select Basic -->
<div class="form-group">
    <label class="col-md-4 control-label" for="source">Source<span class="required-indicator">*</span></label>
    <div class="col-md-8">
        <g:select from="${com.ctacorp.syndication.Source.list()}" name="source.id" id="source" class="form-control" optionValue="name" optionKey="id" value="${periodicalInstance?.source?.id}"/>
    </div>
</div>

<g:render template="/mediaItem/owner" model="[subscribers:subscribers, currentSubscriber:currentSubscriber]"/>