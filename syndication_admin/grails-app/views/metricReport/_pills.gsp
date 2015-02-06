<ul class="nav nav-pills" role="tablist">

    <li role="presentation" class="<g:if test="${active == "date"}">active</g:if>">
        <a href="${createLink(action:"mediaViewMetrics")}">Date</a>
    </li>

    <li role="presentation" class="<g:if test="${active == "range"}">active</g:if>">
        <a href="${createLink(action:"mediaRangeViewMetrics")}">Range</a>
    </li>

    <li role="presentation" class="<g:if test="${active == "total"}">active</g:if>">
        <a href="${createLink(action:"totalViews")}">Totals</a>
    </li>

    <li role="presentation" class="<g:if test="${active == "graph"}">active</g:if>">
            <a href="${createLink(action:"viewGraphs")}">Graphs</a>
    </li>

</ul>