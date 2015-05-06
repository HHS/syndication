%{--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ctacorp.syndication.media.Html" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'html.label', default: 'Html')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
    <asset:javascript src="/tokenInput/jquery.tokeninput.js"/>
    <asset:stylesheet src="/tokenInput/token-input.css"/>
</head>

<body>
<div id="show-html" class="container-fluid" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>
    <div class="row">
        <div class="col-md-10 col-md-offset-2">
                <dl class="dl-horizontal">
                <g:if test="${htmlInstance?.id}">
                    <dt id="id-label" class="word_wrap"><g:message code="html.id.label" default="Id"/></dt>
                    <dd class="word_wrap">${htmlInstance.id}</dd>
                </g:if>

                <g:if test="${htmlInstance?.name}">
                    <dt id="name-label" class="word_wrap"><g:message code="html.name.label" default="Name"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${htmlInstance}" field="name"/></dd>
                </g:if>

                <g:if test="${htmlInstance?.source}">
                    <dt id="source-label" class="word_wrap"><g:message code="html.source.label" default="Source"/></dt>
                    <dd class="word_wrap"><g:link controller="source" action="show" id="${htmlInstance?.source?.id}">${htmlInstance?.source?.encodeAsHTML()}</g:link></dd>
                </g:if>

                <g:if test="${htmlInstance?.sourceUrl}">
                    <dt id="sourceUrl-label" class="word_wrap"><g:message code="html.sourceUrl.label" default="Source Url"/></dt>
                    <dd class="word_wrap"><a target="_blank" href="${htmlInstance?.sourceUrl}"><g:fieldValue bean="${htmlInstance}" field="sourceUrl"/></a></dd>
                </g:if>

                <g:if test="${htmlInstance?.active && htmlInstance?.visibleInStorefront}">
                    <dt id="storefrontLink-label" class="word_wrap"><g:message code="html.storefrontLink.label" default="Storefront Link"/></dt>
                    <dd class="word_wrap"><a target="_blank" href="${grails.util.Holders.config.storefront.serverAddress}/storefront/showContent/${htmlInstance?.id}">${grails.util.Holders.config.storefront.serverAddress}/storefront/showContent/${htmlInstance?.id}</a></dd>
                </g:if>

                <g:if test="${htmlInstance?.targetUrl}">
                    <dt id="targetUrl-label" class="word_wrap"><g:message code="html.targetUrl.label" default="Target Url"/></dt>
                    <dd class="word_wrap"><a target="_blank" href="${htmlInstance?.targetUrl}"><g:fieldValue bean="${htmlInstance}" field="targetUrl"/></a></dd>
                </g:if>

                <g:if test="${htmlInstance?.customThumbnailUrl}">
                    <dt id="customThumbnailUrl-label" class="word_wrap"><g:message code="html.customThumbnailUrl.label" default="Custom Thumbnail Url"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${htmlInstance}" field="customThumbnailUrl"/></dd>
                </g:if>

                <g:if test="${htmlInstance?.customPreviewUrl}">
                    <dt id="customPreviewUrl-label" class="word_wrap"><g:message code="html.customPreviewUrl.label" default="Custom Preview Url"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${htmlInstance}" field="customPreviewUrl"/></dd>
                </g:if>

                <g:if test="${htmlInstance?.language}">
                    <dt id="language-label" class="word_wrap"><g:message code="html.language.label" default="Language"/></dt>
                    <dd class="word_wrap">${htmlInstance?.language?.encodeAsHTML()}</dd>
                </g:if>

                <g:if test="${htmlInstance?.description}">
                    <dt id="description-label" class="word_wrap"><g:message code="html.description.label" default="Description"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${htmlInstance}" field="description"/></dd>
                </g:if>
                <br>
                <g:if test="${htmlInstance?.dateSyndicationUpdated}">
                    <dt id="dateSyndicationUpdated-label" class="word_wrap"><g:message code="html.dateSyndicationUpdated.label" default="Syndication Updated"/></dt>
                    <dd class="property-value" class="word_wrap"><g:formatDate date="${htmlInstance?.dateSyndicationUpdated}"/></dd>
                </g:if>

                <g:if test="${htmlInstance?.dateSyndicationCaptured}">
                    <dt id="dateSyndicationCaptured-label" class="word_wrap"><g:message code="html.dateSyndicationCaptured.label" default="Syndication Captured"/></dt>
                    <dd class="word_wrap"><g:formatDate date="${htmlInstance?.dateSyndicationCaptured}"/></dd>
                </g:if>

                <g:if test="${htmlInstance?.dateSyndicationVisible}">
                    <dt id="dateSyndicationVisible-label" class="word_wrap"><g:message code="html.dateSyndicationVisible.label" default="Syndication Visible"/></dt>
                    <dd class="word_wrap"><g:formatDate date="${htmlInstance?.dateSyndicationVisible}"/></dd>
                </g:if>

                <g:if test="${htmlInstance?.dateContentUpdated}">
                    <dt id="dateContentUpdated-label" class="word_wrap"><g:message code="html.dateContentUpdated.label" default="Content Updated"/></dt>
                    <dd class="word_wrap"><g:formatDate date="${htmlInstance?.dateContentUpdated}"/></dd>
                </g:if>

                <g:if test="${htmlInstance?.dateContentAuthored}">
                    <dt id="dateContentAuthored-label" class="word_wrap"><g:message code="html.dateContentAuthored.label" default="Content Authored"/></dt>
                    <dd class="word_wrap"><g:formatDate date="${htmlInstance?.dateContentAuthored}"/></dd>
                </g:if>

                <g:if test="${htmlInstance?.dateContentPublished}">
                    <dt id="dateContentPublished-label" class="property-label"><g:message code="html.dateContentPublished.label" default="Content Published"/></dt>
                    <dd class="property-value" aria-labelledby="dateContentPublished-label"><g:formatDate date="${htmlInstance?.dateContentPublished}"/></dd>
                </g:if>

                <g:if test="${htmlInstance?.dateContentReviewed}">
                    <dt id="dateContentReviewed-label" class="word_wrap"><g:message code="html.dateContentReviewed.label" default="Content Reviewed"/></dt>
                    <dd class="word_wrap"><g:formatDate date="${htmlInstance?.dateContentReviewed}"/></dd>
                </g:if>

                <dt id="active-label" class="word_wrap"><g:message code="html.active.label" default="Active"/></dt>
                <dd class="word_wrap"><g:formatBoolean boolean="${htmlInstance?.active}"/></dd>

                <dt id="active-label" class="word_wrap"><g:message code="html.visibleInStorefront.label" default="Visible In Storefront"/></dt>
                <dd class="word_wrap"><g:formatBoolean boolean="${htmlInstance?.visibleInStorefront}"/></dd>

                <dt id="active-label" class="word_wrap"><g:message code="html.manuallyManaged.label" default="Manually Managed"/></dt>
                <dd class="word_wrap"><g:formatBoolean boolean="${htmlInstance?.manuallyManaged}"/></dd>

                <g:if test="${htmlInstance?.externalGuid}">
                    <dt id="externalGuid-label" class="word_wrap"><g:message code="html.externalGuid.label" default="External Guide"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${htmlInstance}" field="externalGuid"/></dd>
                </g:if>

                <g:if test="${htmlInstance?.hash}">
                    <dt id="hash-label" class="word_wrap"><g:message code="html.hash.label" default="Hash"/></dt>
                    <dd class="word_wrap"><g:fieldValue bean="${htmlInstance}" field="hash"/></dd>
                </g:if>

                <g:if test="${htmlInstance?.extractionOptions}">
                    <dt id="extractionOptions-label" class="word_wrap"><g:message code="html.extractionOptions.label" default="Extraction Options"/></dt>
                    <dd class="word_wrap"><g:link controller="extractionOptions" action="show" id="${htmlInstance?.extractionOptions?.id}">${htmlInstance?.extractionOptions?.encodeAsHTML()}</g:link></dd>
                </g:if>

                <g:if test="${htmlInstance?.alternateImages}">
                    <dt id="alternateImages-label" class="word_wrap"><g:message code="html.alternateImages.label" default="Alternate Images"/></dt>
                    <g:each in="${htmlInstance?.alternateImages?.sort { it.name }}" var="altImage">
                        <dd class="word_wrap"><g:link controller="alternateImage" params="[mediaId:htmlInstance.id]" action="show" id="${altImage.id}">${altImage?.encodeAsHTML()}</g:link></dd>
                    </g:each>
                </g:if>

                <g:if test="${collections}">
                    <dt id="collections-label" class="word_wrap"><g:message code="mediaItems.collections.label" default="Collections"/></dt>
                    <g:each in="${collections}" var="collection">
                        <dd class="word_wrap"><g:link controller="mediaItem" action="show" id="${collection.id}">${collection.name}</g:link></dd>
                    </g:each>
                </g:if>
                    
                <g:if test="${htmlInstance?.campaigns}">
                    <dt id="campaigns-label" class="word_wrap"><g:message code="html.campaigns.label" default="Campaigns"/></dt>
                    <g:each in="${htmlInstance?.campaigns?.sort { it.name }}" var="c">
                        <dd class="word_wrap"><g:link controller="campaign" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></dd>
                    </g:each>
                </g:if>

                 <g:if test="${htmlInstance?.extendedAttributes}">
                    <dt id="extendedAttributes-label" class="word_wrap"><g:message code="html.extendedAttributes.label" default="Extended Attributes"/></dt>
                    <dd>Attribute : Value</dd>
                    <g:each in="${htmlInstance.extendedAttributes.sort { it.name }}" var="d">
                        <dd class="word_wrap"><g:link controller="extendedAttribute" params="[mediaId:htmlInstance.id]" action="show" id="${d.id}">${d?.encodeAsHTML()} : ${d.value}</g:link></dd>
                    </g:each>
                </g:if>
            </dl>
        </div>
    </div>

    <fieldset class="buttons">
        <g:form url="[resource:htmlInstance, action:'edit']">
            <a href="${apiBaseUrl + '/resources/media/'+ htmlInstance?.id +'/syndicate.json'}" class="btn btn-success popup-link">Preview</a>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER, ROLE_USER">
                <g:actionSubmit class="btn btn-warning" value="Edit" action="edit"/>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PUBLISHER">
                <g:actionSubmit class="btn btn-danger" action="delete" onclick="return confirm('${message(code: 'default.button.delete.mediaItem.confirm', default: 'Are you sure?')}');" value="Delete"/>
            </sec:ifAnyGranted>
            <g:link class="button" action="index">
                <button type="button" class="btn">Cancel</button>
            </g:link>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_USER">
                <g:link controller="featuredMedia" id="${htmlInstance?.id}" action="featureItem">
                    <button type="button" class="btn btn-success pull-right">Feature this Item</button>
                </g:link>
            </sec:ifAnyGranted>
        </g:form>
    </fieldset>
    <g:render template="/mediaItem/addToYourCampaign" model="[mediaItemInstance: htmlInstance]"/>
</div>
</body>
</html>
