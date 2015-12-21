<g:if test="${mediaItemInstance?.id}">
    <dt class="word_wrap"><g:message code="${mediaType}.id.label" default="ID"/></dt>
    <dd class="word_wrap">${mediaItemInstance?.id}</dd>
</g:if>

<g:if test="${mediaItemInstance?.name}">
    <dt class="word_wrap"><g:message code="${mediaType}.name.label" default="Name"/></dt>
    <dd class="word_wrap"><g:fieldValue bean="${mediaItemInstance}" field="name"/></dd>
</g:if>

<g:if test="${mediaItemInstance?.source}">
    <dt id="source-label" class="word_wrap"><g:message code="${mediaType}.source.label" default="Source"/></dt>
    <dd class="word_wrap"><g:link controller="source" action="show" id="${mediaItemInstance?.source?.id}">${mediaItemInstance?.source?.encodeAsHTML()}</g:link></dd>
</g:if>

<g:if test="${mediaItemInstance?.createdBy}">
    <dt id="createdBy-label" class="word_wrap"><g:message code="${mediaType}.createdBy.label" default="Created By"/></dt>
    <dd class="word_wrap">${mediaItemInstance?.createdBy?.encodeAsHTML()}</dd>
</g:if>

<g:if test="${mediaItemInstance?.sourceUrl}">
    <dt class="word_wrap"><g:message code="${mediaType}.sourceUrl.label" default="Source Url"/></dt>
    <dd class="word_wrap"><a target="_blank" href="${mediaItemInstance.sourceUrl}"><g:fieldValue bean="${mediaItemInstance}" field="sourceUrl"/></a></dd>
</g:if>

<g:if test="${mediaItemInstance?.targetUrl}">
    <dt id="targetUrl-label" class="word_wrap"><g:message code="${mediaType}.targetUrl.label" default="Target Url"/></dt>
    <dd class="word_wrap"><a target="_blank" href="${mediaItemInstance?.targetUrl}"><g:fieldValue bean="${mediaItemInstance}" field="targetUrl"/></a></dd>
</g:if>

<g:if test="${mediaItemInstance?.active && mediaItemInstance?.visibleInStorefront}">
    <dt id="storefrontLink-label" class="word_wrap"><g:message code="${mediaType}.storefrontLink.label" default="Storefront Link"/></dt>
    <dd class="word_wrap"><a target="_blank" href="${grails.util.Holders.config.storefront.serverAddress}/storefront/showContent/${mediaItemInstance?.id}">${grails.util.Holders.config.storefront.serverAddress}/storefront/showContent/${mediaItemInstance?.id}</a></dd>
</g:if>

<g:if test="${mediaItemInstance?.customThumbnailUrl}">
    <dt id="customThumbnailUrl-label" class="word_wrap"><g:message code="${mediaType}.customThumbnailUrl.label" default="Custom Thumbnail Url"/></dt>
    <dd class="word_wrap"><g:fieldValue bean="${mediaItemInstance}" field="customThumbnailUrl"/></dd>
</g:if>

<g:if test="${mediaItemInstance?.customPreviewUrl}">
    <dt id="customPreviewUrl-label" class="word_wrap"><g:message code="${mediaType}.customPreviewUrl.label" default="Custom Preview Url"/></dt>
    <dd class="word_wrap"><g:fieldValue bean="${mediaItemInstance}" field="customPreviewUrl"/></dd>
</g:if>

<g:if test="${mediaItemInstance?.language}">
    <dt id="language-label" class="word_wrap"><g:message code="${mediaType}.language.label" default="Language"/></dt>
    <dd class="word_wrap">${mediaItemInstance?.language?.encodeAsHTML()}</dd>
</g:if>

<g:if test="${mediaItemInstance?.description}">
    <dt class="word_wrap"><g:message code="${mediaType}.description.label" default="Description"/></dt>
    <dd class="word_wrap"><g:fieldValue bean="${mediaItemInstance}" field="description"/></dd>
</g:if>
<br>
<g:if test="${mediaItemInstance?.dateSyndicationUpdated}">
    <dt id="dateSyndicationUpdated-label" class="word_wrap"><g:message code="${mediaType}.dateSyndicationUpdated.label" default="Syndication Updated"/></dt>
    <dd class="word_wrap"><g:formatDate date="${mediaItemInstance?.dateSyndicationUpdated}"/></dd>
</g:if>

<g:if test="${mediaItemInstance?.dateSyndicationCaptured}">
    <dt id="dateSyndicationCaptured-label" class="word_wrap"><g:message code="${mediaType}.dateSyndicationCaptured.label" default="Syndication Captured"/></dt>
    <dd class="word_wrap"><g:formatDate date="${mediaItemInstance?.dateSyndicationCaptured}"/></dd>
</g:if>

<g:if test="${mediaItemInstance?.dateSyndicationVisible}">
    <dt id="dateSyndicationVisible-label" class="word_wrap"><g:message code="${mediaType}.dateSyndicationVisible.label" default="Syndication Visible"/></dt>
    <dd class="word_wrap"><g:formatDate date="${mediaItemInstance?.dateSyndicationVisible}"/></dd>
</g:if>

<g:if test="${mediaItemInstance?.dateContentUpdated}">
    <dt id="dateContentUpdated-label" class="word_wrap"><g:message code="${mediaType}.dateContentUpdated.label" default="Content Updated"/></dt>
    <dd class="word_wrap"><g:formatDate date="${mediaItemInstance?.dateContentUpdated}"/></dd>
</g:if>

<g:if test="${mediaItemInstance?.dateContentAuthored}">
    <dt id="dateContentAuthored-label" class="word_wrap"><g:message code="${mediaType}.dateContentAuthored.label" default="Content Authored"/></dt>
    <dd class="word_wrap"><g:formatDate date="${mediaItemInstance?.dateContentAuthored}"/></dd>
</g:if>

<g:if test="${mediaItemInstance?.dateContentPublished}">
    <dt id="dateContentPublished-label" class="word_wrap"><g:message code="${mediaType}.dateContentPublished.label" default="Content Published"/></dt>
    <dd class="word_wrap"><g:formatDate date="${mediaItemInstance?.dateContentPublished}"/></dd>
</g:if>

<g:if test="${mediaItemInstance?.dateContentReviewed}">
    <dt id="dateContentReviewed-label" class="word_wrap"><g:message code="${mediaType}.dateContentReviewed.label" default="Content Reviewed"/></dt>
    <dd class="word_wrap"><g:formatDate date="${mediaItemInstance?.dateContentReviewed}"/></dd>
</g:if>

<dt id="active-label" class="word_wrap"><g:message code="${mediaType}.active.label" default="Active"/></dt>
<dd class="word_wrap"><g:formatBoolean boolean="${mediaItemInstance?.active}"/></dd>

<dt id="active-label" class="word_wrap"><g:message code="${mediaType}.visibleInStorefront.label" default="Visible In Storefront"/></dt>
<dd class="word_wrap"><g:formatBoolean boolean="${mediaItemInstance?.visibleInStorefront}"/></dd>

<dt id="active-label" class="word_wrap"><g:message code="${mediaType}.manuallyManaged.label" default="Manually Managed"/></dt>
<dd class="word_wrap"><g:formatBoolean boolean="${mediaItemInstance?.manuallyManaged}"/></dd>

<g:if test="${mediaItemInstance?.externalGuid}">
    <dt id="externalGuid-label" class="word_wrap"><g:message code="${mediaType}.externalGuid.label" default="External GUID"/></dt>
    <dd class="word_wrap"><g:fieldValue bean="${mediaItemInstance}" field="externalGuid"/></dd>
</g:if>

<g:if test="${mediaItemInstance?.hash}">
    <dt id="hash-label" class="word_wrap"><g:message code="${mediaType}.hash.label" default="Hash"/></dt>
    <dd class="word_wrap"><g:fieldValue bean="${mediaItemInstance}" field="hash"/></dd>
</g:if>

<g:if test="${mediaItemInstance?.campaigns}">
    <dt id="campaigns-label" class="word_wrap"><g:message code="${mediaType}.campaigns.label" default="Campaigns"/></dt>
    <g:each in="${mediaItemInstance.campaigns.sort{it.name}}" var="c">
        <dd class="word_wrap"><g:link controller="campaign" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></dd>
    </g:each>
</g:if>

<g:if test="${mediaItemInstance?.alternateImages}">
    <dt id="alternateImages-label" class="word_wrap"><g:message code="${mediaType}.alternateImages.label" default="Alternate Images"/></dt>
    <g:each in="${mediaItemInstance.alternateImages}" var="a">
        <dd class="word_wrap"><g:link controller="alternateImage" params="[mediaId:mediaItemInstance.id]" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></dd>
    </g:each>
</g:if>

<g:if test="${mediaItemInstance?.extendedAttributes}">
    <dt id="extendedAttributes-label" class="word_wrap"><g:message code="${mediaType}.extendedAttributes.label" default="Extended Attributes"/></dt>
    <g:each in="${mediaItemInstance.extendedAttributes.sort{ it.name }}" var="d">
        <dd class="word_wrap"><g:link controller="extendedAttribute" params="[mediaId:mediaItemInstance.id]" action="show" id="${d.id}">${d.name?.encodeAsHTML()}: ${d.value?.encodeAsHTML()}</g:link></dd>
    </g:each>
</g:if>
