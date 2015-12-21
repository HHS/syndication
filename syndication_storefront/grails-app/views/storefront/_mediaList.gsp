<div id="contentList" class="content_box">
    <g:if test="${!mediaItemInstanceList || mediaItemInstanceList.isEmpty()}">
        <div>No Results</div>
    </g:if>
    <g:each in="${mediaItemInstanceList}" var="mediaItemInstance">
        <div class="content_row">
            <p class="mediaRow">
                <span class="expando" mediaid="${mediaItemInstance.id}"><i class="fa fa-plus-circle icons"></i></span>
                <span class="mediaName"><a href="${createLink(action: 'showContent', id:mediaItemInstance.id)}">${mediaItemInstance.name}</a></span>
                <g:link class="sourceLink" action="listMediaForSource" id="${mediaItemInstance.source.id}">${mediaItemInstance.source.acronym}</g:link>
                <g:if test="${tagsForMedia}">
                    <span class="mediaTagListing">
                        <g:each in="${tagsForMedia[mediaItemInstance.id]}" var="tag">
                            <a href="${createLink(action:'listMediaForTag', id:tag.id, params:[tagName:tag.name])}" class="tagLink">${tag.name}</a>&nbsp;
                        </g:each>
                    </span>
                </g:if>
            </p>

            <div class="mediaDescription collapsedMediaDescription" id="desc_${mediaItemInstance.id}">
                <p>${mediaItemInstance.description}</p>
                <div class="mediaLinkButtons" id="medialinkbuttons_${mediaItemInstance.id}">
                    <div class="mediaPreview">
                        <a href="${grailsApplication.config.syndication.serverUrl + grailsApplication.config.syndication.apiPath + '/resources/media/'+ mediaItemInstance.id +'/syndicate.json?ignoreHiddenMedia=1'}" onclick="addStorefrontPreviewHit(${mediaItemInstance.id})" class="syndButton popup-link">Preview</a>
                        <span>
                            <sec:ifLoggedIn>
                                <g:if test="${!likeInfo."${mediaItemInstance.id}".alreadyLiked}">
                                    <span data-mediaid="${mediaItemInstance.id}" class="notLiked">
                                        <i class="socialIcons fa fa-thumbs-up"></i>
                                    </span>
                                </g:if>
                                <g:else>
                                    <span data-mediaid="${mediaItemInstance?.id}" class="alreadyLiked">
                                        <i class="socialIcons fa fa-thumbs-up"></i>
                                    </span>
                                </g:else>
                            </sec:ifLoggedIn>
                            <span id="likeCount_${mediaItemInstance.id}">&nbsp; ${likeInfo."${mediaItemInstance.id}".likeCount} Users liked this content.</span>
                        </span>
                    </div>
                    <div class="mediaSocialLinks">
                        <a class="socialLink" href="#" onclick="javascript:window.open('https://www.facebook.com/sharer/sharer.php?u=${mediaItemInstance.sourceUrl}', 'Syndication', 'width=600,height=450')"><i title="share on facebook" class="socialIcons fa fa-facebook-square"></i></a>
                        <a class="socialLink" href="#" onclick="javascript:window.open('https://plus.google.com/share?url={${mediaItemInstance.sourceUrl}}', 'Syndication', 'width=800,height=600')"><i title="share on google plus" class="socialIcons fa fa-google-plus-square"></i></a>
                        <a class="socialLink" href="#" onclick="javascript:window.open('http://www.linkedin.com/shareArticle?mini=true&url=${mediaItemInstance.sourceUrl}', 'Syndication', 'width=600,height=450')"><i title="share on linkedin" class="socialIcons fa fa-linkedin-square"></i></a>
                    </div>
                    <div class="mediaItemRowClear"></div>
                </div>
            </div>
        </div>
    </g:each>
</div>
<g:if test="${total}">
    <div class="centeredText">
        <g:paginate action="index" params="[searchQuery: searchQuery, title: params.title,
                                                language: params.language,
                                                domain: params.domain, advancedSearch: params.advancedSearch,
                                                mediaType:params.mediaType,
                                                topic:params.topic,
                                                sourceId: sourceId,
                                                source: source,
                                                tag:tag]" total="${total ?: 0}"/>
    </div>
</g:if>

<script>
    $(document).on('click','.alreadyLiked', function () {
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

    $(document).on('click','.notLiked', function () {
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
    
    function addStorefrontPreviewHit(mediaId){
        setTimeout(function () {
            $.ajax({
                type: 'POST',
                data: {id:mediaId},
                url: '${g.createLink(controller: 'storefront', action: 'storefrontPreviewMetricHit')}',
                success: function(response){
                }
            });
        });
    }
</script>