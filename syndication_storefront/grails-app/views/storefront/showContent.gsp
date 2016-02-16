<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 3/13/14
  Time: 1:54 PM
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="grails.util.Holders" contentType="text/html;charset=UTF-8" %>
<head>
    <meta name="layout" content="storefront"/>
    <title>Show content</title>
</head>
<body>
<g:set var="dateFormat" value="${"EEEE, MMMM dd, yyyy 'at' hh:mm aa"}"/>
<g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
</g:if>
<a name="pageContent"></a>
<h2>${mediaItemInstance?.name}</h2>
<div class="showPreviewBox"><img class="centeredImage" src="${apiBaseUrl}/resources/media/${mediaItemInstance?.id}/thumbnail.jpg"/></div>
<div class="mediaDescription">
    <p>${mediaItemInstance?.description}</p>
</div>
<div>
    <br/>
    <g:if test="${mediaItemInstance.structuredContentType}">
        <strong>${mediaItemInstance.structuredContentType}</strong>
    </g:if>
    <g:else>
        <strong>Media Type: ${mediaItemInstance.class.simpleName}</strong>
    </g:else>
    <g:if test="${mediaItemInstance.getClass().simpleName == 'QuestionAndAnswer'}">
        <div style="margin: 10px 0px 10px 10px">Question: ${mediaItemInstance?.name}</div>
        <div style="margin: 0px 0px 10px 20px; font-style: italic">Answer: ${mediaItemInstance?.answer}</div>
    </g:if>
    <ul>
        <g:if test="${mediaItemInstance?.sourceUrl}"><li>SourceUrl: <a href="${mediaItemInstance.sourceUrl}" style="word-break: break-all;">${mediaItemInstance.sourceUrl}</a></li></g:if>
        <g:if test="${mediaItemInstance?.id}"><li>Syndication ID: ${mediaItemInstance.id}</li></g:if>
        <g:if test="${mediaItemInstance?.language}"><li>Language: ${mediaItemInstance.language.name}</li></g:if>
        <g:if test="${mediaItemInstance?.source}"><li>Source: ${mediaItemInstance.source.name} (${mediaItemInstance.source.acronym})</li></g:if>
        <g:if test="${mediaItemInstance?.createdBy}"><li>Created By: ${mediaItemInstance.createdBy} </li></g:if>
        <g:if test="${mediaItemInstance?.dateContentAuthored}"><li>Date Content Authored: ${mediaItemInstance.dateContentAuthored?.format(dateFormat)}</li></g:if>
        <g:if test="${mediaItemInstance?.dateContentUpdated}"><li>Date Content Updated: ${mediaItemInstance.dateContentUpdated?.format(dateFormat)}</li></g:if>
        <g:if test="${mediaItemInstance?.dateContentPublished}"><li>Date Content Published: ${mediaItemInstance.dateContentPublished?.format(dateFormat)}</li></g:if>
        <g:if test="${mediaItemInstance?.dateContentReviewed}"><li>Date Content Reviewed: ${mediaItemInstance.dateContentReviewed?.format(dateFormat)}</li></g:if>
        <g:if test="${mediaItemInstance?.dateSyndicationCaptured}"><li>Date Syndication Captured: ${mediaItemInstance.dateSyndicationCaptured?.format(dateFormat)}</li></g:if>
        <g:if test="${mediaItemInstance?.dateSyndicationUpdated}"><li>Date Syndication Updated: ${mediaItemInstance.dateSyndicationUpdated?.format(dateFormat)}</li></g:if>
        <g:if test="${mediaItemInstance.class.simpleName == "Tweet"}">
            <g:if test="${mediaItemInstance?.tweetId}"><li>Tweet ID: ${mediaItemInstance.tweetId}</li></g:if>
            <g:if test="${mediaItemInstance?.account?.accountName}"><li>Account: ${mediaItemInstance.account.accountName}</li></g:if>
            <g:if test="${mediaItemInstance?.messageText}"><li>Tweet text: ${mediaItemInstance.messageText}</li></g:if>
            <g:if test="${mediaItemInstance?.mediaUrl}"><li>mediaUrl: ${mediaItemInstance.mediaUrl}</li></g:if>
            <g:if test="${mediaItemInstance?.videoVariantUrl}"><li>VideoUrl: ${mediaItemInstance.videoVariantUrl}</li></g:if>
            <g:if test="${mediaItemInstance?.tweetDate}"><li>Tweet Post Date: ${mediaItemInstance.tweetDate}</li></g:if>
        </g:if>
    </ul>
    <g:if test="${tags.eng}">
        <h3>Tags</h3>
        <p>
            <g:set var="tagsByType" value="${tags.eng.groupBy{ it.type.name }}"/>
            <g:each in="${tagsByType}" var="tagType">
                <strong>${tagType.key}: </strong>
                <g:each in="${tagsByType[tagType.key]}" var="tag">
                    <span class="mediaTagListing"><a href="${createLink(action:'listMediaForTag', id:tag.id, params:[tagName:tag.name])}" class="tagLink">${tag.name}</a></span>
                </g:each>
                <br/>
            </g:each>
        </p>
    </g:if>
    <g:if test="${mediaItemInstance instanceof com.ctacorp.syndication.media.Collection}">
        <strong>Media items in this collection:</strong>
        <ul>
            <g:set var="visibleMediaItems" value="${(mediaItemInstance as com.ctacorp.syndication.media.Collection).mediaItems.sort{it.name}}"/>
            <div hidden>${visibleMediaItems.removeAll{!it.visibleInStorefront}}</div>
            <g:each in="${visibleMediaItems}" var="collectionItem">
                <li><g:link controller="storefront" action="showContent" id="${collectionItem.id}">${collectionItem.name}</g:link></li>
            </g:each>
        </ul>
    </g:if>
    <a href="${apiBaseUrl + '/resources/media/'+ mediaItemInstance?.id +'/syndicate.json?ignoreHiddenMedia=1'}" class="syndButton popup-link">Preview</a>
    <sec:ifLoggedIn>
        <g:if test="${!alreadyLiked}">
            <span data-mediaid="${mediaItemInstance?.id}" class="notLiked">
                <i class="socialIcons fa fa-thumbs-up"></i>
            </span>
        </g:if>
        <g:else>
            <span data-mediaid="${mediaItemInstance?.id}" class="alreadyLiked">
                <i class="socialIcons fa fa-thumbs-up"></i>
            </span>
        </g:else>
    </sec:ifLoggedIn>
    <span id="likeCount_${mediaItemInstance?.id}">&nbsp; ${likeCount} Users liked this content.</span>
    <sec:ifLoggedIn>
        <br/>
        <br/>
        <div>
            <g:form action="addMediaToUserMediaList">
                <g:hiddenField name="mediaId" value="${mediaItemInstance.id}"/>
                <g:submitButton class="syndButton" name="Add To List"/>
                <g:select name="mediaList" from="${userMediaLists}" optionValue="name" optionKey="id"/>
            </g:form>
        </div>
    </sec:ifLoggedIn>
    <br/>
    <br/>
    <g:render template="livePreview"/>
</div>
%{--javascripts with url injections--}%
<g:render template="showContentScripts"/>
</body>
</html>