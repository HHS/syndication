<div class="syndicated_media_feed">
    <g:each in="${mediaItemContent}" var="mediaContent">
        <div class="syndicated_media_feed_item">${mediaContent.content.encodeAsRaw()}</div>
    </g:each>
</div>