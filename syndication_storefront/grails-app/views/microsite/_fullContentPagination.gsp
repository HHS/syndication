<div id="full-content-data${panel}" data-offset="${offset}" data-mediacount="${count}">
    <g:if test="${syndicateResponse}">
        ${raw(syndicateResponse?.json?.results?.content[0])}
    </g:if>
</div>