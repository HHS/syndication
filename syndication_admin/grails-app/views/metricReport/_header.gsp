<div class="row">
    <div class="col-lg-12">
        <div class="pull-right">
            <ul class="nav nav-pills" role="tablist">
                <li role="presentation" class="<g:if test="${params.action == "overview"}">active</g:if>">
                    <a href="${createLink(action:"overview")}">Overview</a>
                </li>

                <li role="presentation" class="<g:if test="${params.action == "mediaViewMetrics"}">active</g:if>">
                    <a href="${createLink(action:"mediaViewMetrics")}">Date</a>
                </li>

                <li role="presentation" class="<g:if test="${params.action == "mediaRangeViewMetrics"}">active</g:if>">
                    <a href="${createLink(action:"mediaRangeViewMetrics")}">Range</a>
                </li>

                <li role="presentation" class="<g:if test="${params.action == "totalViews"}">active</g:if>">
                    <a href="${createLink(action:"totalViews")}">Totals</a>
                </li>

                <li role="presentation" class="<g:if test="${params.action == "viewGraphs"}">active</g:if>">
                    <a href="${createLink(action:"viewGraphs")}">Graphs</a>
                </li>
            </ul>
        </div>
        <h1 class="page-header">${entityName}</h1>
    </div>
</div>