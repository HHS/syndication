<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 3/13/14
  Time: 1:54 PM
--%>

<g:set var="dateFormat" value="${"EEEE, MMMM dd, yyyy 'at' hh:mm aa"}"/>

<%@ page import="grails.util.Holders" contentType="text/html;charset=UTF-8" %>
<head>
    <meta name="layout" content="storefront"/>
    <title>Show content</title>
</head>
<body>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <h2>${mediaItemInstance?.name}</h2>
    <div class="showPreviewBox"><img class="centeredImage" src="${apiBaseUrl}/resources/media/${mediaItemInstance?.id}/preview.jpg?previewSize=custom&width=400&height=300&imageFloat=left&imageMargin=0,10,0,0"/></div>
    <div class="mediaDescription">
        <p>${mediaItemInstance?.description}</p>
    </div>
    <div>
        <ul>
            <g:if test="${mediaItemInstance?.sourceUrl}"><li>SourceUrl: <a href="${mediaItemInstance.sourceUrl}">${mediaItemInstance.sourceUrl}</a></li></g:if>
            <g:if test="${mediaItemInstance?.id}"><li>Syndication ID: ${mediaItemInstance.id}</li></g:if>
            <g:if test="${mediaItemInstance?.language}"><li>Language: ${mediaItemInstance.language.name}</li></g:if>
            <g:if test="${mediaItemInstance?.source}"><li>Source: ${mediaItemInstance.source.name} (${mediaItemInstance.source.acronym})</li></g:if>
            <g:if test="${mediaItemInstance?.dateContentAuthored}"><li>Date Content Authored: ${mediaItemInstance.dateContentAuthored?.format(dateFormat)}</li></g:if>
            <g:if test="${mediaItemInstance?.dateContentUpdated}"><li>Date Content Updated: ${mediaItemInstance.dateContentUpdated?.format(dateFormat)}</li></g:if>
            <g:if test="${mediaItemInstance?.dateContentPublished}"><li>Date Content Published: ${mediaItemInstance.dateContentPublished?.format(dateFormat)}</li></g:if>
            <g:if test="${mediaItemInstance?.dateContentReviewed}"><li>Date Content Reviewed: ${mediaItemInstance.dateContentReviewed?.format(dateFormat)}</li></g:if>
            <g:if test="${mediaItemInstance?.dateSyndicationCaptured}"><li>Date Syndication Captured: ${mediaItemInstance.dateSyndicationCaptured?.format(dateFormat)}</li></g:if>
            <g:if test="${mediaItemInstance?.dateSyndicationUpdated}"><li>Date Syndication Updated: ${mediaItemInstance.dateSyndicationUpdated?.format(dateFormat)}</li></g:if>
            <g:if test="${mediaItemInstance?.dateSyndicationVisible}"><li>Date Syndication Visible: ${mediaItemInstance.dateSyndicationVisible?.format(dateFormat)}</li></g:if>
        </ul>
        <a href="${apiBaseUrl + '/resources/media/'+ mediaItemInstance?.id +'/syndicate.json'}" class="syndButton popup-link">Preview</a>
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
        <h3>Embed Code Snippet</h3>
        <br/>
        <sec:ifLoggedIn>
            <span>Copy and paste this snippet into your existing webpage, and when the page is viewed by a user, the syndicated content will inject itself automatically!</span>
            <div>
                <g:checkBox name="includeJquery" checked="true" id="includeJquery"/><label>Include JQuery in snippet</label>
            </div>
            <g:textArea name="embedCode" cols="50" rows="10" id="snippetCode"/>

        </sec:ifLoggedIn>
        <sec:ifNotLoggedIn>
            <span>To get the embed code snippet please <g:link controller="login" action="index">Login.</g:link></span>
        </sec:ifNotLoggedIn>
    </div>

<script>
    $(document).on('click', '.alreadyLiked', function () {
        var mediaId = $(this).attr('data-mediaId');
        var reference = this;
        setTimeout(function () {
            $.ajax({ // create an AJAX call...
                type: 'POST', // GET or POST
                data: {id: mediaId},
                contentType: 'json',
                url: '${g.createLink(controller: 'storefront', action: 'ajaxUndoLike')}' + '/' + mediaId, // the file to call
                success: function (response) { // on success..
                    $(reference).removeClass("alreadyLiked");
                    $(reference).addClass("notLiked");
                    $(reference).html("<i class='socialIcons fa fa-thumbs-up'></i>");  // update the DIV
                    $('#likeCount_' + mediaId).html(response);
                }
            });
        });
    });

    $(document).on('click', '.notLiked', function () {
        var mediaId = $(this).attr('data-mediaId');
        var reference = this;
        setTimeout(function () {
            $.ajax({ // create an AJAX call...
                type: 'POST', // GET or POST
                data: {id: mediaId},
                contentType: 'json',
                url: '${g.createLink(controller: 'storefront', action: 'ajaxLike')}' + '/' + mediaId, // the file to call
                success: function (response) { // on success..
                    $(reference).removeClass("notLiked");
                    $(reference).addClass("alreadyLiked");
                    $(reference).html("<i class='socialIcons fa fa-thumbs-up'></i>");  // update the DIV
                    $('#likeCount_' + mediaId).html(response);
                }
            });
        });
    });
    <sec:ifLoggedIn>
        function updateSnippet(useJquery){
            var url
            if(useJquery){
                url = "${grails.util.Holders.config.syndication.serverUrl + grails.util.Holders.config.syndication.apiPath}/resources/media/${mediaItemInstance.id}/embed.json?callback=?"
            } else{
                url = "${grails.util.Holders.config.syndication.serverUrl + grails.util.Holders.config.syndication.apiPath}/resources/media/${mediaItemInstance.id}/embed.json?excludeJquery=true&callback=?"
            }
            $.getJSON(url, function(data){
                function htmlDecode(input){
                    var e = document.createElement('div');
                    e.innerHTML = input;
                    return e.childNodes.length === 0 ? "" : e.childNodes[0].nodeValue;
                }

                $('#snippetCode').val(htmlDecode(data.results[0].snippet));
            });
        }
    </sec:ifLoggedIn>
        $(document).on('click', '#includeJquery', function(){
            if($('#includeJquery').prop('checked')){
                updateSnippet(true)
            } else{
                updateSnippet(false)
            }
        })

        $(document).ready(function(){
            updateSnippet(true);
        });

</script>
</body>
</html>