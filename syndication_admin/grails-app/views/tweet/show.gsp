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
<%@ page import="com.ctacorp.syndication.media.Tweet" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'tweet.label', default: 'Tweet')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
    <asset:javascript src="tokenInput/jquery.tokeninput.js"/>
    <asset:stylesheet src="tokenInput/token-input.css"/>
</head>

<body>
<div id="show-tweet" class="container-fluid" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>

    <div class="row">
        <div class="col-md-10 col-md-offset-2">
            <dl class="dl-horizontal">
            <g:if test="${tweetInstance?.id}">
                <dt id="id-label" class="word_wrap"><g:message code="tweet.id.label" default="Id"/></dt>
                <dd class="word_wrap">${tweetInstance?.id}</dd>
            </g:if>

            <g:if test="${tweetInstance?.name}">
                <dt id="name-label" class="word_wrap"><g:message code="tweet.name.label" default="Name"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${tweetInstance}" field="name"/></dd>
            </g:if>

            <g:if test="${tweetInstance?.source}">
                <dt id="source-label" class="property-label"><g:message code="tweet.source.label" default="Source"/></dt>
                <dd class="property-value" aria-labelledby="source-label"><g:link controller="source" action="show" id="${tweetInstance?.source?.id}">${tweetInstance?.source?.encodeAsHTML()}</g:link></dd>
            </g:if>

            <g:if test="${tweetInstance?.sourceUrl}">
                <dt id="sourceUrl-label" class="word_wrap"><g:message code="tweet.sourceUrl.label" default="Source Url"/></dt>
                <dd class="word_wrap"><a target="_blank" href="${tweetInstance?.sourceUrl}"><g:fieldValue bean="${tweetInstance}" field="sourceUrl"/></a></dd>
            </g:if>

            <g:if test="${tweetInstance?.targetUrl}">
                <dt id="targetUrl-label" class="word_wrap"><g:message code="tweet.targetUrl.label" default="Target Url"/></dt>
                <dd class="word_wrap"><a target="_blank" href="${tweetInstance?.targetUrl}"><g:fieldValue bean="${tweetInstance}" field="targetUrl"/></a></dd>
            </g:if>

            <g:if test="${tweetInstance?.active && tweetInstance?.visibleInStorefront}">
                <dt id="storefrontLink-label" class="word_wrap"><g:message code="html.storefrontLink.label" default="Storefront Link"/></dt>
                <dd class="word_wrap"><a target="_blank" href="${grails.util.Holders.config.storefront.serverAddress}/storefront/showContent/${tweetInstance?.id}">${grails.util.Holders.config.storefront.serverAddress}/storefront/showContent/${tweetInstance?.id}</a></dd>
            </g:if>

            <g:if test="${tweetInstance?.customThumbnailUrl}">
                <dt id="customThumbnailUrl-label" class="word_wrap"><g:message code="tweet.customThumbnailUrl.label" default="Custom Thumbnail Url"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${tweetInstance}" field="customThumbnailUrl"/></dd>
            </g:if>

            <g:if test="${tweetInstance?.customPreviewUrl}">
                <dt id="customPreviewUrl-label" class="word_wrap"><g:message code="tweet.customPreviewUrl.label" default="Custom Preview Url"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${tweetInstance}" field="customPreviewUrl"/></dd>
            </g:if>

            <g:if test="${tweetInstance?.language}">
                <dt id="language-label" class="word_wrap"><g:message code="tweet.language.label" default="Language"/></dt>
                <dd class="word_wrap">${tweetInstance?.language?.encodeAsHTML()}</dd>
            </g:if>

            <g:if test="${tweetInstance?.description}">
                <dt id="description-label" class="word_wrap"><g:message code="tweet.description.label" default="Description"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${tweetInstance}" field="description"/></dd>
            </g:if>
            <br>
            <g:if test="${tweetInstance?.dateSyndicationUpdated}">
                <dt id="dateSyndicationUpdated-label" class="word_wrap"><g:message code="tweet.dateSyndicationUpdated.label" default="Syndication Updated"/></dt>
                <dd class="word_wrap"><g:formatDate date="${tweetInstance?.dateSyndicationUpdated}"/></dd>
            </g:if>

            <g:if test="${tweetInstance?.dateSyndicationCaptured}">
                <dt id="dateSyndicationCaptured-label" class="word_wrap"><g:message code="tweet.dateSyndicationCaptured.label" default="Syndication Captured"/></dt>
                <dd class="word_wrap"><g:formatDate date="${tweetInstance?.dateSyndicationCaptured}"/></dd>
            </g:if>

            <g:if test="${tweetInstance?.dateSyndicationVisible}">
                <dt id="dateSyndicationVisible-label" class="word_wrap"><g:message code="tweet.dateSyndicationVisible.label" default="Syndication Visible"/></dt>
                <dd class="word_wrap"><g:formatDate date="${tweetInstance?.dateSyndicationVisible}"/></dd>
            </g:if>

            <g:if test="${tweetInstance?.dateContentUpdated}">
                <dt id="dateContentUpdated-label" class="word_wrap"><g:message code="tweet.dateContentUpdated.label" default="Content Updated"/></dt>
                <dd class="word_wrap"><g:formatDate date="${tweetInstance?.dateContentUpdated}"/></dd>
            </g:if>

            <g:if test="${tweetInstance?.dateContentAuthored}">
                <dt id="dateContentAuthored-label" class="word_wrap"><g:message code="tweet.dateContentAuthored.label" default="Content Authored"/></dt>
                <dd class="word_wrap"><g:formatDate date="${tweetInstance?.dateContentAuthored}"/></dd>
            </g:if>

            <g:if test="${tweetInstance?.dateContentPublished}">
                <dt id="dateContentPublished-label" class="word_wrap"><g:message code="tweet.dateContentPublished.label" default="Content Published"/></dt>
                <dd class="word_wrap"><g:formatDate date="${tweetInstance?.dateContentPublished}"/></dd>
            </g:if>

            <g:if test="${tweetInstance?.dateContentReviewed}">
                <dt id="dateContentReviewed-label" class="word_wrap"><g:message code="tweet.dateContentReviewed.label" default="Content Reviewed"/></dt>
                <dd class="word_wrap"><g:formatDate date="${tweetInstance?.dateContentReviewed}"/></dd>
            </g:if>

            <dt id="active-label" class="word_wrap"><g:message code="tweet.active.label" default="Active"/></dt>
            <dd class="word_wrap"><g:formatBoolean boolean="${tweetInstance?.active}"/></dd>

            <dt id="active-label" class="word_wrap"><g:message code="tweet.visibleInStorefront.label" default="visible In Storefront"/></dt>
            <dd class="word_wrap"><g:formatBoolean boolean="${tweetInstance?.visibleInStorefront}"/></dd>

            <dt id="active-label" class="word_wrap"><g:message code="tweet.manuallyManaged.label" default="Manually Managed"/></dt>
            <dd class="word_wrap"><g:formatBoolean boolean="${tweetInstance?.manuallyManaged}"/></dd>

            <g:if test="${tweetInstance?.externalGuid}">
                <dt id="externalGuid-label" class="word_wrap"><g:message code="tweet.externalGuid.label" default="External GUID"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${tweetInstance}" field="externalGuid"/></dd>
            </g:if>

            <g:if test="${tweetInstance?.hash}">
                <dt id="hash-label" class="word_wrap"><g:message code="tweet.hash.label" default="Hash"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${tweetInstance}" field="hash"/></dd>
            </g:if>

            %{--<g:if test="${tweetInstance?.tweetType}">--}%
                %{--<dt id="tweetType-label" class="word_wrap"><g:message code="tweet.tweetType.label" default="Social Media Type"/></dt>--}%
                %{--<dd class="word_wrap"><g:fieldValue bean="${tweetInstance}" field="tweetType"/></dd>--}%
            %{--</g:if>--}%

            <g:if test="${tweetInstance?.alternateImages}">
                <dt id="alternateImages-label" class="word_wrap"><g:message code="tweet.alternateImages.label" default="Alternate Images"/></dt>
                <g:each in="${tweetInstance.alternateImages}" var="a">
                    <dd class="word_wrap"><g:link controller="alternateImage" params="[mediaId:tweetInstance.id]" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></dd>
                </g:each>
            </g:if>

            <g:if test="${collections}">
                <dt id="collections-label" class="word_wrap"><g:message code="mediaItems.collections.label" default="Collections"/></dt>
                <g:each in="${collections}" var="collection">
                    <dd class="word_wrap"><g:link controller="mediaItem" action="show" id="${collection.id}">${collection.name}</g:link></dd>
                </g:each>
            </g:if>

            <g:if test="${tweetInstance?.extendedAttributes}">
                <dt id="extendedAttributes-label" class="word_wrap"><g:message code="tweet.extendedAttributes.label" default="Extended Attributes"/></dt>
                <g:each in="${tweetInstance.extendedAttributes}" var="d">
                    <dd class="word_wrap"><g:link controller="extendedAttribute" params="[mediaId:tweetInstance.id]" action="show" id="${d.id}">${d?.encodeAsHTML()}</g:link></dd>
                </g:each>
            </g:if>

            <g:if test="${tweetInstance?.campaigns}">
                <dt id="campaigns-label" class="word_wrap"><g:message code="tweet.campaigns.label" default="Campaigns"/></dt>
                <g:each in="${tweetInstance.campaigns}" var="c">
                    <dd class="word_wrap"><g:link controller="campaign" action="show" id="${c.id}">${c?.encodeAsHTML()}</g:link></dd>
                </g:each>
            </g:if>


            <g:if test="${tweetInstance?.tweetId}">
                <dt id="name-label" class="word_wrap"><g:message code="tweet.tweetId.label" default="Tweet Id"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${tweetInstance}" field="tweetId"/></dd>
            </g:if>

            <g:if test="${tweetInstance?.account}">
                <dt id="name-label" class="word_wrap"><g:message code="tweet.account.label" default="Account"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${tweetInstance}" field="account"/></dd>
            </g:if>

            <g:if test="${tweetInstance?.messageText}">
                <dt id="name-label" class="word_wrap"><g:message code="tweet.messageText.label" default="Message Text"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${tweetInstance}" field="messageText"/></dd>
            </g:if>

            <g:if test="${tweetInstance?.mediaUrl}">
                <dt id="name-label" class="word_wrap"><g:message code="tweet.mediaUrl.label" default="Media Url"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${tweetInstance}" field="mediaUrl"/></dd>
            </g:if>

            <g:if test="${tweetInstance?.tweetDate}">
                <dt id="name-label" class="word_wrap"><g:message code="tweet.tweetDate.label" default="Tweet Date"/></dt>
                <dd class="word_wrap"><g:fieldValue bean="${tweetInstance}" field="tweetDate"/></dd>
            </g:if>

            </dl>
        </div>
    </div>

    <fieldset class="buttons">
        <g:form  url="[resource:tweetInstance, action:'edit']">
            <a href="${apiBaseUrl + '/resources/media/'+ tweetInstance?.id +'/syndicate.json'}" class="btn btn-success popup-link">Preview</a>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER, ROLE_USER">
                <g:actionSubmit class="btn btn-warning" value="Edit" action="edit"/>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PUBLISHER">
                <g:actionSubmit class="btn btn-danger" onclick="return confirm('${message(code: 'default.button.delete.mediaItem.confirm', default: 'Are you sure?')}');" value="Delete" action="delete"/>
            </sec:ifAnyGranted>
            <g:link class="button" action="index">
                <button type="button" class="btn">Cancel</button>
            </g:link>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER">
                <g:link controller="featuredMedia" id="${tweetInstance?.id}" action="featureItem">
                    <button type="button" class="btn btn-success pull-right">Feature this Item</button>
                </g:link>
            </sec:ifAnyGranted>
        </g:form>
    </fieldset>
    <g:render template="/mediaItem/addToYourCampaign" model="[mediaItemInstance: tweetInstance]"/>
</div>
</body>
</html>
