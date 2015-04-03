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
    <style type="text/css">
    #snippetPreview{
        border: 1px solid lightgray;
        padding:10px;
    }
    </style>
</head>
<body>
<g:set var="dateFormat" value="${"EEEE, MMMM dd, yyyy 'at' hh:mm aa"}"/>
<g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
</g:if>
<h2>${mediaItemInstance?.name}</h2>
<div class="showPreviewBox"><img class="centeredImage" src="${apiBaseUrl}/resources/media/${mediaItemInstance?.id}/thumbnail.jpg"/></div>
<div class="mediaDescription">
    <p>${mediaItemInstance?.description}</p>
</div>
<div>
    <br/>
    <strong>Media Type: ${mediaItemInstance.class.simpleName}</strong>
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
    </ul>
    <g:if test="${mediaItemInstance instanceof com.ctacorp.syndication.media.Collection}">
        <strong>Media items in this collection:</strong>
        <ul>
            <g:each in="${(mediaItemInstance as com.ctacorp.syndication.media.Collection).mediaItems.sort{it.name}}" var="collectionItem">
                <li><g:link controller="storefront" action="showContent" id="${collectionItem.id}">${collectionItem.name}</g:link></li>
            </g:each>
        </ul>
    </g:if>
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

        <div style="width: 100%; overflow: hidden;">
            <div style="width: 400px; float: left;">
                <span>Copy and paste this snippet into your existing webpage, and when the page is viewed by a user, the syndicated content will inject itself automatically!</span>

                <p>
                    <g:radioGroup name="jsOrIframe"
                                  id="jsOrIframe"
                                  labels="['Javascript', 'Iframe (no javascript)']"
                                  values="['js', 'iframe']"
                                  value="js">
                        ${it.radio} ${it.label}
                    </g:radioGroup>
                </p>
                <div style="width: 100%; overflow: hidden;">
                    <div style="width: 150px; float: left;">
                        <g:checkBox name="includeJquery"    class="extractionCheckbox" checked="true"  id="includeJquery"/><label>Include JQuery</label><br/>
                        <g:checkBox name="stripImages"      class="extractionCheckbox" checked="false" id="stripImages"/><label>Strip Images</label><br/>
                        <g:checkBox name="stripStyles"      class="extractionCheckbox" checked="false" id="stripStyles"/><label>Strip Styles</label><br/>
                    </div>
                    <div style="margin-left: 120px;">
                        <g:checkBox name="stripScripts"     class="extractionCheckbox" checked="false" id="stripScripts"/><label>Strip Scripts</label><br/>
                        <g:checkBox name="stripBreaks"      class="extractionCheckbox" checked="false" id="stripBreaks"/><label>Strip Breaks</label><br/>
                        <g:checkBox name="stripClasses"     class="extractionCheckbox" checked="false" id="stripClasses"/><label>Strip Classes</label>
                    </div>
                </div>
            </div>
            <div style="margin-left: 420px;">
                <g:textArea name="embedCode" cols="80" rows="10" id="snippetCode"/>
            </div>
        </div>
        <br/>
        <h3>Live Snippet Preview</h3>
        <p>When you embed the above snippet code on your website, the injected content will look something like the preview below. Use the supplied checkboxes to alter the delivered content. It's important to note that the final look and feel of the content is subject to your local stylesheets.</p>

        <div id="snippetPreview">

        </div>
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
    function updatePreview(){
        $.getJSON("${grails.util.Holders.config.syndication.serverUrl + grails.util.Holders.config.syndication.apiPath}/resources/media/${mediaItemInstance.id}/syndicate.json?autoplay=0&" + getQueryString(), function(data){
            $("#snippetPreview").html(data.results[0].content)
        })
    }

    function getFlavor(){
        var flavor = $('input[name=jsOrIframe]:checked').val()
        if(flavor === "iframe") {
            var flavorParam = "flavor=" + flavor + "&"
            return flavorParam
        }
        return ""
    }

    function getQueryString(){
        var excludeJquery = $('#includeJquery').prop('checked') ?   '0' : '1'
        var stripImages =   $('#stripImages').prop('checked') ?     '1' : '0'
        var stripStyles =   $('#stripStyles').prop('checked') ?     '1' : '0'
        var stripScripts =  $('#stripScripts').prop('checked') ?    '1' : '0'
        var stripBreaks =   $('#stripBreaks').prop('checked') ?     '1' : '0'
        var stripClasses =  $('#stripClasses').prop('checked') ?    '1' : '0'

        var queryParams = "excludeJquery="+     excludeJquery
        queryParams += "&stripImages="+         stripImages
        queryParams += "&stripStyles="+         stripStyles
        queryParams += "&stripScripts="+        stripScripts
        queryParams += "&stripBreaks="+         stripBreaks
        queryParams += "&stripClasses="+        stripClasses
        queryParams += "&callback=?"

        return queryParams
    }

    function updateSnippet(){
        var url = "${grails.util.Holders.config.syndication.serverUrl + grails.util.Holders.config.syndication.apiPath}/resources/media/${mediaItemInstance.id}/embed.json?"
        $.getJSON(url + getFlavor() + getQueryString(), function(data){
            function htmlDecode(input){
                var e = document.createElement('div');
                e.innerHTML = input;
                return e.childNodes.length === 0 ? "" : e.childNodes[0].nodeValue;
            }

            $('#snippetCode').val(htmlDecode(data.results[0].snippet));
        });

        updatePreview();
    }
    </sec:ifLoggedIn>

    $(document).on('click', '.extractionCheckbox', function(){
        updateSnippet()
    })

    $(document).on('click', '#jsOrIframe', function(){
        updateSnippet()
        if($('input[name=jsOrIframe]:checked').val() === "iframe") {
            $("#includeJquery").prop("checked", false);
            $("#includeJquery").prop("disabled", true);
        } else{
            $("#includeJquery").prop("checked", true);
            $("#includeJquery").prop("disabled", false);
        }
    })

    $(document).ready(function(){
        updateSnippet();
    });

</script>
</body>
</html>