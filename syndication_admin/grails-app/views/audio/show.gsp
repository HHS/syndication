%{--
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'audio.label', default: 'Audio')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
    <asset:javascript src="tokenInput/jquery.tokeninput.js"/>
    <asset:stylesheet src="tokenInput/token-input.css"/>
</head>

<body>
<div id="show-audio" class="container-fluid" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>

    <div class="row">
        <div class="col-md-10 col-md-offset-2">
            <dl class="dl-horizontal">

            <g:if test="${audioInstance?.id}">
                <dt id="id-label" class="word_wrap"><g:message code="audio.id.label" default="Id"/></dt>
                <dd class="word_wrap">${audioInstance?.id}</dd>
            </g:if>

            <g:if test="${audioInstance?.name}">
                <dt id="name-label" class="word_wrap"><g:message code="audio.name.label" default="Name"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${audioInstance}" field="name"/></dd>
            </g:if>

            <g:if test="${audioInstance?.source}">
                <dt id="source-label" class="word_wrap"><g:message code="audio.source.label" default="Source"/></dt>
                <dd class="word_wrap"><g:link controller="source" action="show" id="${audioInstance?.source?.id}">${audioInstance?.source?.encodeAsHTML()}</g:link></dd>
            </g:if>

            <g:if test="${audioInstance?.sourceUrl}">
                <dt id="sourceUrl-label" class="word_wrap"><g:message code="audio.sourceUrl.label" default="Source Url"/></dt>
                <dd class="word_wrap"><a target="_blank" href="${audioInstance?.sourceUrl}"><g:fieldValue bean="${audioInstance}" field="sourceUrl"/></a></dd>
            </g:if>

            <g:if test="${audioInstance?.targetUrl}">
                <dt id="targetUrl-label" class="word_wrap"><g:message code="audio.targetUrl.label" default="Target Url"/></dt>
                <dd class="word_wrap"><a target="_blank" href="${audioInstance?.targetUrl}"><g:fieldValue bean="${audioInstance}" field="targetUrl"/></a></dd>
            </g:if>

            <g:if test="${audioInstance?.customThumbnailUrl}">
                <dt id="customThumbnailUrl-label" class="word_wrap"><g:message code="audio.customThumbnailUrl.label" default="Custom Thumbnail Url"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${audioInstance}" field="customThumbnailUrl"/></dd>
            </g:if>

            <g:if test="${audioInstance?.customPreviewUrl}">
                <dt id="customPreviewUrl-label" class="word_wrap"><g:message code="audio.customPreviewUrl.label" default="Custom Preview Url"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${audioInstance}" field="customPreviewUrl"/></dd>
            </g:if>

            <g:if test="${audioInstance?.language}">
                <dt id="language-label" class="word_wrap"><g:message code="audio.language.label" default="Language"/></dt>
                <dd class="word_wrap">${audioInstance?.language?.encodeAsHTML()}</dd>
            </g:if>

            <g:if test="${audioInstance?.description}">
                <dt id="description-label" class="word_wrap"><g:message code="audio.description.label" default="Description"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${audioInstance}" field="description"/></dd>
            </g:if>
            <br>
            <g:if test="${audioInstance?.dateSyndicationUpdated}">
                <dt id="dateSyndicationUpdated-label" class="word_wrap"><g:message code="audio.dateSyndicationUpdated.label" default="Syndication Updated"/></dt>
                <dd class="word_wrap"><g:formatDate date="${audioInstance?.dateSyndicationUpdated}"/></dd>
            </g:if>

            <g:if test="${audioInstance?.dateSyndicationCaptured}">
                <dt id="dateSyndicationCaptured-label" class="word_wrap"><g:message code="audio.dateSyndicationCaptured.label" default="Syndication Captured"/></dt>
                <dd class="word_wrap"><g:formatDate date="${audioInstance?.dateSyndicationCaptured}"/></dd>
            </g:if>

            <g:if test="${audioInstance?.dateSyndicationVisible}">
                <dt id="dateSyndicationVisible-label" class="word_wrap"><g:message code="audio.dateSyndicationVisible.label" default="Syndication Visible"/></dt>
                <dd class="word_wrap"><g:formatDate date="${audioInstance?.dateSyndicationVisible}"/></dd>
            </g:if>

            <g:if test="${audioInstance?.dateContentUpdated}">
                <dt id="dateContentUpdated-label" class="word_wrap"><g:message code="audio.dateContentUpdated.label" default="Content Updated"/></dt>
                <dd class="property-value" aria-labelledby="dateContentUpdated-label"><g:formatDate date="${audioInstance?.dateContentUpdated}"/></dd>
            </g:if>

            <g:if test="${audioInstance?.dateContentAuthored}">
                <dt id="dateContentAuthored-label" class="word_wrap"><g:message code="audio.dateContentAuthored.label" default="Content Authored"/></dt>
                <dd class="word_wrap"><g:formatDate date="${audioInstance?.dateContentAuthored}"/></dd>
            </g:if>

            <g:if test="${audioInstance?.dateContentPublished}">
                <dt id="dateContentPublished-label" class="word_wrap"><g:message code="audio.dateContentPublished.label" default="Content Published"/></dt>
                <dd class="word_wrap"><g:formatDate date="${audioInstance?.dateContentPublished}"/></dd>
            </g:if>

            <g:if test="${audioInstance?.dateContentReviewed}">
                <dt id="dateContentReviewed-label" class="word_wrap"><g:message code="audio.dateContentReviewed.label" default="Content Reviewed"/></dt>
                <dd class="property-value" aria-labelledby="dateContentReviewed-label"><g:formatDate date="${audioInstance?.dateContentReviewed}"/></dd>
            </g:if>

            <dt id="active-label" class="word_wrap"><g:message code="audio.active.label" default="Active"/></dt>
            <dd class="word_wrap"><g:formatBoolean boolean="${audioInstance?.active}"/></dd>

            <dt id="active-label" class="word_wrap"><g:message code="audio.visibleInStorefront.label" default="Visible In Storefront"/></dt>
            <dd class="word_wrap"><g:formatBoolean boolean="${audioInstance?.visibleInStorefront}"/></dd>

            <dt id="active-label" class="word_wrap"><g:message code="audio.manuallyManged.label" default="Manually Managed"/></dt>
            <dd class="word_wrap"><g:formatBoolean boolean="${audioInstance?.manuallyManaged}"/></dd>

            <g:if test="${audioInstance?.externalGuid}">
                <dt id="externalGuid-label" class="word_wrap"><g:message code="audio.externalGuid.label" default="External GUID"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${audioInstance}" field="externalGuid"/></dd>
            </g:if>

            <g:if test="${audioInstance?.hash}">
                <dt id="hash-label" class="word_wrap"><g:message code="audio.hash.label" default="Hash"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${audioInstance}" field="hash"/></dd>
            </g:if>

            <g:if test="${audioInstance?.duration}">
                <dt id="duration-label" class="word_wrap"><g:message code="audio.duration.label" default="Duration"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${audioInstance}" field="duration"/></dd>
            </g:if>

            <g:if test="${audioInstance?.alternateImages}">
                <dt id="alternateImages-label" class="word_wrap"><g:message code="audio.alternateImages.label" default="Alternate Images"/></dt>
                <g:each in="${audioInstance.alternateImages}" var="alternateInstance">
                    <dd class="word_wrap"><g:link controller="alternateImage" params="[mediaId:audioInstance.id]" action="show" id="${alternateInstance.id}">${alternateInstance?.encodeAsHTML()}</g:link></dd>
                </g:each>
            </g:if>

            <g:if test="${collections}">
                <dt id="collections-label" class="word_wrap"><g:message code="mediaItems.collections.label" default="Collections"/></dt>
                <g:each in="${collections}" var="collection">
                    <dd class="word_wrap"><g:link controller="mediaItem" action="show" id="${collection.id}">${collection.name}</g:link></dd>
                </g:each>
            </g:if>

            <g:if test="${audioInstance?.campaigns}">
                <dt id="campaigns-label" class="word_wrap"><g:message code="audio.campaigns.label" default="Campaigns"/></dt>
                <g:each in="${audioInstance.campaigns}" var="campaignInstance">
                    <dd class="word_wrap"><g:link controller="campaign" action="show" id="${campaignInstance.id}">${campaignInstance?.encodeAsHTML()}</g:link></dd>
                </g:each>
            </g:if>

            <g:if test="${audioInstance?.extendedAttributes}">
                <dt id="extendedAttributes-label" class="word_wrap"><g:message code="audio.extendedAttributes.label" default="Extended Attributes"/></dt>
                <g:each in="${audioInstance.extendedAttributes}" var="attributeInstance">
                    <dd class="word_wrap"><g:link controller="extendedAttribute" params="[mediaId:audioInstance.id]" action="show" id="${attributeInstance.id}">${attributeInstance?.encodeAsHTML()}</g:link></dd>
                </g:each>
            </g:if>
            </dl>
        </div>
    </div>

        <fieldset class="buttons">
            <g:form  url="[resource:audioInstance, action:'edit']">
                <a href="${apiBaseUrl + '/resources/media/'+ audioInstance?.id +'/syndicate.json'}" class="btn btn-success popup-link">Preview</a>
                <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER">
                    <g:link class="edit" action="edit" resource="${audioInstance}">
                        <g:actionSubmit class="btn btn-warning" value="Edit" action="edit"/>
                    </g:link>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PUBLISHER">
                    <g:actionSubmit class="btn btn-danger" action="delete" onclick="return confirm('${message(code: 'default.button.delete.mediaItem.confirm', default: 'Are you sure?')}');" value="Delete"/>
                </sec:ifAnyGranted>

                <g:link class="button" action="index">
                    <button type="button" class="btn">Cancel</button>
                </g:link>
                <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER">
                    <g:link controller="featuredMedia" id="${audioInstance?.id}" action="featureItem">
                        <button type="button" class="btn btn-success pull-right">Feature this Item</button>
                    </g:link>
                </sec:ifAnyGranted>
            </g:form>
        </fieldset>

    <g:render template="/mediaItem/addToYourCampaign" model="[mediaItemInstance: audioInstance]"/>
</div>
</body>
</html>
