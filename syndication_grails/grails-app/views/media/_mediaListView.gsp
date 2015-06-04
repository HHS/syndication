<ul>
<g:each in="${mediaItems}" var="mediaItem">
    <li><a href='${mediaItem.sourceUrl}'>${mediaItem.name}</a> [${mediaItem.id}]</li>
</g:each>
</ul>