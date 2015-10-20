<style>
.item-tweet {
    display: inline-block;
    padding:  .25rem;
    width:  100%;
}

.well-tweet {
    position:relative;
    display: block;
}
</style>

<g:if test="${thumbnailGeneration == true || previewGeneration == true}">
    <g:if test="${tweet.mediaUrl}">
        <img style="height:100%;width:100%;" class="clickable_thumbnail" src="${tweet.mediaUrl}:thumb" alt="Image of twitter post"/>
    </g:if>
    <g:else>
        <g:img style="height:100%;width:100%;" uri="${grailsApplication.config.grails.serverURL}/assets/defaultIcons/thumbnail/twitter.jpg"/>
    </g:else>
</g:if>
<g:else>
    <div class="item-tweet">
        <div class="well-tweet">
            <div>
                <g:if test="${tweet.mediaUrl}">
                    <img class="clickable_thumbnail" height="100%" width="100%" src="${tweet.mediaUrl}:large"/>
                </g:if>
            </div>
            <div class="${hasMedia ? 'tweet_text' : 'tweet_text_large'}">
                <i class="fa fa-twitter"></i>
                ${tweet.messageText.encodeAsRaw()}
            </div>
        </div>
    </div>
</g:else>