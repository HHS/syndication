%{--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<%@ page import="com.ctacorp.syndication.Collection" %>


<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="name">Name<span class="required-indicator">*</span></label>
    <div class="col-md-8">
        <input id="name" name="name" required="" value="${collectionInstance?.name}" type="text" placeholder="collection name" class="form-control input-md">
    </div>
</div>

<!-- Textarea -->
<div class="form-group">
    <label class="col-md-4 control-label" for="description">Description</label>
    <div class="col-md-8">
        <textarea class="form-control" id="description" name="description" maxlength="2000">${collectionInstance?.description}</textarea>
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="sourceUrl">Source Url<span class="required-indicator">*</span></label>
    <div class="col-md-8">
        <input id="sourceUrl" name="sourceUrl" maxlength="2000" required="" value="${collectionInstance?.sourceUrl}" type="url" placeholder="source url" class="form-control input-md">
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="targetUrl">targetUrl</label>
    <div class="col-md-8">
        <input id="targetUrl" name="targetUrl" maxlength="2000" value="${collectionInstance?.targetUrl}" type="url" placeholder="target url" class="form-control input-md">
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="customThumbnailUrl">Custom Thumbnail Url</label>
    <div class="col-md-8">
        <input id="customThumbnailUrl" name="customThumbnailUrl" maxlength="2000" value="${collectionInstance?.customThumbnailUrl}" type="url" placeholder="thumbnail url" class="form-control input-md">
    </div>
</div>

<div class="form-group ${hasErrors(bean: collectionInstance, field: 'dateSyndicationCaptured', 'error')} required">
    <label class="col-md-4 control-label" for="dateSyndicationCaptured">
        <g:message code="collection.dateSyndicationCaptured.label" default="Date Syndication Captured"/>
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateSyndicationCaptured" precision="minute" relativeYears="[-20..1]" value="${collectionInstance?.dateSyndicationCaptured}"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: collectionInstance, field: 'dateSyndicationUpdated', 'error')} required">
    <label class="col-md-4 control-label" for="dateSyndicationUpdated">
        <g:message code="collection.dateSyndicationUpdated.label" default="Date Syndication Updated"/>
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateSyndicationUpdated" precision="minute" relativeYears="[-20..1]" value="${collectionInstance?.dateSyndicationUpdated}"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: collectionInstance, field: 'dateSyndicationVisible', 'error')} required">
    <label class="col-md-4 control-label" for="dateSyndicationVisible">
        <g:message code="collection.dateSyndicationVisible.label" default="Date Syndication Visible"/>
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateSyndicationVisible" precision="minute" relativeYears="[-20..1]" value="${collectionInstance?.dateSyndicationVisible}"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: collectionInstance, field: 'dateContentAuthored', 'error')} ">
    <label class="col-md-4 control-label" for="dateContentAuthored">
        <g:message code="collection.dateContentAuthored.label" default="Date Content Authored"/>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateContentAuthored" precision="minute" relativeYears="[-20..1]" value="${collectionInstance?.dateContentAuthored}" default="none" noSelection="['': '']"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: collectionInstance, field: 'dateContentUpdated', 'error')} ">
    <label class="col-md-4 control-label" for="dateContentUpdated">
        <g:message code="collection.dateContentUpdated.label" default="Date Content Updated"/>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateContentUpdated" precision="minute" relativeYears="[-20..1]" value="${collectionInstance?.dateContentUpdated}" default="none" noSelection="['': '']"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: collectionInstance, field: 'dateContentPublished', 'error')} ">
    <label class="col-md-4 control-label" for="dateContentPublished">
        <g:message code="collection.dateContentPublished.label" default="Date Content Published"/>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateContentPublished" precision="minute" relativeYears="[-20..1]" value="${collectionInstance?.dateContentPublished}" default="none" noSelection="['': '']"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: collectionInstance, field: 'dateContentReviewed', 'error')} ">
    <label class="col-md-4 control-label" for="dateContentReviewed">
        <g:message code="collection.dateContentReviewed.label" default="Date Content Reviewed"/>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateContentReviewed" precision="minute" relativeYears="[-20..1]" value="${collectionInstance?.dateContentReviewed}" default="none" noSelection="['': '']"/>
    </div>
</div>

<!-- Select Basic -->
<div class="form-group">
    <label class="col-md-4 control-label" for="language">Language<span class="required-indicator">*</span></label>
    <div class="col-md-8">
        <g:select from="${com.ctacorp.syndication.Language.findAllByIsActive(true, [sort: "name"])}" name="language.id" id="language" optionKey="id" optionValue="name" value="${collectionInstance?.language?.id}" class="form-control"/>
    </div>
</div>

<!-- Multiple Radios -->
<div class="form-group">
    <label class="col-md-4 control-label" for="radios">Active</label>
    <div class="col-md-8">
        <g:if test="${collectionInstance?.active == true}">
            <label class="radio" for="active">
                <input name="active" id="active" value="true" checked="checked" type="radio">
                Active
            </label>
            <label class="radio" for="unactive">
                <input name="active" id="unactive" value="false" type="radio">
                Inactive
            </label>
        </g:if>
        <g:else>
            <label class="radio" for="active">
                <input name="active" id="active" value="true" type="radio">
                Active
            </label>
            <label class="radio" for="unactive">
                <input name="active" id="unactive" checked="checked" value="false" type="radio">
                Inactive
            </label>
        </g:else>
    </div>
</div>

<!-- Multiple Radios -->
<div class="form-group">
    <label class="col-md-4 control-label" for="radios">Visible In Storefront</label>
    <div class="col-md-8">
        <g:if test="${collectionInstance?.visibleInStorefront == true}">
            <label class="radio" for="visibleInStorefront">
                <input name="visibleInStorefront" id="visibleInStorefront" value="true" checked="checked" type="radio">
                Visible in Storefront
            </label>
            <label class="radio" for="notVisibleInStorefront">
                <input name="visibleInStorefront" id="notVisibleInStorefront" value="false" type="radio">
                Not Visible in Storefront
            </label>
        </g:if>
        <g:else>
            <label class="radio" for="visibleInStorefront">
                <input name="visibleInStorefront" id="visibleInStorefront" value="true" type="radio">
                Visible in Storefront
            </label>
            <label class="radio" for="notVisibleInStorefront">
                <input name="visibleInStorefront" id="notVisibleInStorefront" checked="checked" value="false" type="radio">
                Not Visible in Storefront
            </label>
        </g:else>
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="externalGuid">External Guid</label>
    <div class="col-md-8">
        <input id="externalGuid" name="externalGuid" value="${collectionInstance?.externalGuid}" placeholder="guid" class="form-control input-md">
    </div>
</div>

<!-- Text input-->
<div class="form-group">
    <label class="col-md-4 control-label" for="hash">Hash</label>
    <div class="col-md-8">
        <input id="hash" name="hash" value="${collectionInstance?.hash}" class="form-control input-md">
    </div>
</div>

<!-- Select Basic -->
<div class="form-group">
    <label class="col-md-4 control-label" for="source">Source<span class="required-indicator">*</span></label>
    <div class="col-md-8">
        <g:select from="${com.ctacorp.syndication.Source.list()}" name="source.id" id="source" class="form-control" optionValue="name" optionKey="id" value="${collectionInstance?.source?.id}"/>
    </div>
</div>

<!-- Select Multiple -->
<div class="form-group">
    <label class="col-md-4 control-label" for="allMediaItems">Media Items</label>
    <div class="col-md-8">
        <g:textField name="allMediaItems" id="allMediaItems"/>
    </div>
</div>

<g:render template="/mediaItem/owner"/>