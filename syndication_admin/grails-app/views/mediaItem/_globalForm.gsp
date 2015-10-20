<!-- Text input-->
<div class="form-group ${hasErrors(bean:mediaItemInstance, field:'name', 'errors')}" xmlns="http://www.w3.org/1999/html">
    <label class="col-md-4 control-label" for="name">Name<span class="required-indicator">*</span></label>
    <div class="col-md-8">
        <input id="name" name="name" value="${mediaItemInstance?.name}" required="true" type="text" placeholder="Media Item Name" class="form-control input-md">
    </div>
</div>

<!-- Text input-->
<div class="form-group ${hasErrors(bean:mediaItemInstance, field:'sourceUrl', 'errors')}">
    <label class="col-md-4 control-label" for="sourceUrl">Source Url<span class="required-indicator">*</span></label>

    <g:if test="${mediaItemInstance.getClass().simpleName == 'Html' || mediaItemInstance.getClass().simpleName == 'Periodical'}">
        <div class="col-md-6">
            <input id="sourceUrl" name="sourceUrl" maxlength="2000" required="true" value="${mediaItemInstance?.sourceUrl}" type="url" placeholder="Source URL" class="form-control input-md">
        </div>
        <div class="col-md-2">
            <button type="button" id="urlModal" class="btn btn-primary btn-sm pull-right" data-toggle="modal" data-target="#myModal">
                Verify URL
            </button>
            <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <g:render template="../mediaTestPreview/testModal"/>
            </div>
        </div>
    </g:if>
    <g:else>
        <div class="col-md-8">
            <input id="sourceUrl" name="sourceUrl" maxlength="2000" required="true" value="${mediaItemInstance?.sourceUrl}" type="url" placeholder="Source URL" class="form-control input-md">
        </div>
    </g:else>
</div>

<!-- Select Basic -->
<div class="form-group ${hasErrors(bean:mediaItemInstance, field:'language', 'errors')}">
    <label class="col-md-4 control-label" for="language">Language<span class="required-indicator">*</span></label>
    <div class="col-md-8">
        <g:select from="${com.ctacorp.syndication.Language.findAllByIsActive(true, [sort: "name"])}" name="language.id" id="language" optionKey="id" optionValue="name" value="${mediaItemInstance?.language?.id}" class="form-control" />
    </div>
</div>

<!-- Select Basic -->
<div class="form-group ${hasErrors(bean:mediaItemInstance, field:'source', 'errors')}">
    <label class="col-md-4 control-label" for="source">Source<span class="required-indicator">*</span></label>
    <div class="col-md-8">
        <g:select from="${com.ctacorp.syndication.Source.list([sort: "name"])}" name="source.id" id="source" class="form-control" optionValue="name" optionKey="id" value="${mediaItemInstance?.source?.id}" noSelection="${['null':'-Choose a Source-']}"/>
    </div>
</div>

<g:render template="/mediaItem/owner" model="[subscribers:subscribers, currentSubscriber:currentSubscriber]"/>

<!-- Textarea -->
<div class="form-group ${hasErrors(bean:mediaItemInstance, field:'description', 'errors')}">
    <label class="col-md-4 control-label" for="description">Description</label>
    <div class="col-md-8">
        <textArea class="form-control" id="description" name="description" placeholder="Write a summary or detailed description of your content here" maxlength="2000">${mediaItemInstance?.description}</textarea>
    </div>
</div>

<!-- Text input-->
<div class="form-group ${hasErrors(bean:mediaItemInstance, field:'targetUrl', 'errors')}">
    <label class="col-md-4 control-label" for="targetUrl">Target Url</label>
    <div class="col-md-8">
        <input id="targetUrl" name="targetUrl" maxlength="2000" value="${mediaItemInstance?.targetUrl}" type="url" placeholder="Target URL" class="form-control input-md">
    </div>
</div>

<!-- Text input-->
<div class="form-group ${hasErrors(bean:mediaItemInstance, field:'customThumbnailUrl', 'errors')}">
    <label class="col-md-4 control-label" for="customThumbnailUrl">Custom Thumbnail Url</label>
    <div class="col-md-8">
        <input id="customThumbnailUrl" name="customThumbnailUrl" maxlength="2000" value="${mediaItemInstance?.customThumbnailUrl}" type="url" placeholder="Custom Thumbnail URL" class="form-control input-md">
    </div>
</div>

<!-- Text input-->
<div class="form-group ${hasErrors(bean:mediaItemInstance, field:'customPreviewUrl', 'errors')}">
    <label class="col-md-4 control-label" for="customPreviewUrl">Custom Preview Url</label>
    <div class="col-md-8">
        <input id="customPreviewUrl" name="customPreviewUrl" maxlength="2000" value="${mediaItemInstance?.customPreviewUrl}" type="url" placeholder="Custom Preview URL" class="form-control input-md">
    </div>
</div>

<div class="form-group ${hasErrors(bean: mediaItemInstance, field: 'dateSyndicationCaptured', 'error')} required">
    <label class="col-md-4 control-label" for="dateSyndicationCaptured">
        <g:message code="html.dateSyndicationCaptured.label" default="Date Syndication Captured"/>
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateSyndicationCaptured" precision="minute" relativeYears="[-20..1]" value="${mediaItemInstance?.dateSyndicationCaptured}"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: mediaItemInstance, field: 'dateSyndicationUpdated', 'error')} required">
    <label class="col-md-4 control-label" for="dateSyndicationUpdated">
        <g:message code="html.dateSyndicationUpdated.label" default="Date Syndication Updated"/>
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateSyndicationUpdated" precision="minute" relativeYears="[-20..1]" value="${mediaItemInstance?.dateSyndicationUpdated}"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: mediaItemInstance, field: 'dateSyndicationVisible', 'error')} required">
    <label class="col-md-4 control-label" for="dateSyndicationVisible">
        <g:message code="html.dateSyndicationVisible.label" default="Date Syndication Visible"/>
        <span class="required-indicator">*</span>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateSyndicationVisible" precision="minute" relativeYears="[-20..1]" value="${mediaItemInstance?.dateSyndicationVisible}"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: mediaItemInstance, field: 'dateContentAuthored', 'error')} ">
    <label class="col-md-4 control-label" for="dateContentAuthored">
        <g:message code="html.dateContentAuthored.label" default="Date Content Authored"/>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateContentAuthored" precision="minute" relativeYears="[-20..1]" value="${mediaItemInstance?.dateContentAuthored}" default="none" noSelection="['': '']"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: mediaItemInstance, field: 'dateContentUpdated', 'error')} ">
    <label class="col-md-4 control-label" for="dateContentUpdated">
        <g:message code="html.dateContentUpdated.label" default="Date Content Updated"/>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateContentUpdated" precision="minute" relativeYears="[-20..1]" value="${mediaItemInstance?.dateContentUpdated}" default="none" noSelection="['': '']"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: mediaItemInstance, field: 'dateContentPublished', 'error')} ">
    <label class="col-md-4 control-label" for="dateContentPublished">
        <g:message code="html.dateContentPublished.label" default="Date Content Published"/>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateContentPublished" precision="minute" relativeYears="[-20..1]" value="${mediaItemInstance?.dateContentPublished}" default="none" noSelection="['': '']"/>
    </div>
</div>

<div class="form-group ${hasErrors(bean: mediaItemInstance, field: 'dateContentReviewed', 'error')} ">
    <label class="col-md-4 control-label" for="dateContentReviewed">
        <g:message code="html.dateContentReviewed.label" default="Date Content Reviewed"/>
    </label>
    <div class="col-md-8">
        <g:datePicker name="dateContentReviewed" precision="minute" relativeYears="[-20..1]" value="${mediaItemInstance?.dateContentReviewed}" default="none" noSelection="['': '']"/>
    </div>
</div>

<!-- Radio Buttons -->
<div class="form-group">
    <label class="col-md-4 control-label" for="active">
        <g:message code="html.active.label" default="Active"/>

    </label>
    <div class="col-md-8">
        <g:checkBox name="active" value="${mediaItemInstance?.active}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="visibleInStorefront">
        <g:message code="html.visibleInStorefront.label" default="Visible In Storefront"/>

    </label>
    <div class="col-md-8">
        <g:checkBox name="visibleInStorefront" value="${mediaItemInstance?.visibleInStorefront}"/>
    </div>
</div>

<div class="form-group">
    <label class="col-md-4 control-label" for="manuallyManaged">
        <g:message code="html.manuallyManaged.label" default="Manually Managed"/>

    </label>
    <div class="col-md-8">
        <g:checkBox name="manuallyManaged" value="${mediaItemInstance?.manuallyManaged}"/>
    </div>
</div>

<!-- Text input-->
<div class="form-group ${hasErrors(bean:mediaItemInstance, field:'externalGuid', 'errors')}">
    <label class="col-md-4 control-label" for="externalGuid">External Guid</label>
    <div class="col-md-8">
        <input id="externalGuid" name="externalGuid" value="${mediaItemInstance?.externalGuid}" placeholder="ID of Media Item in the Source CMS" class="form-control input-md">
    </div>
</div>