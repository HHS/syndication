<div class="row">
    <div class="col-lg-12">
        <div class="pull-right">
            <ul class="nav nav-pills" role="tablist">
                <li role="presentation" class="<g:if test="${params.action == "generalViews"}">active</g:if>">
                    <a href="${createLink(action:"generalViews")}">General Views</a>
                </li>
                <li role="presentation" class="<g:if test="${params.action == "consumers"}">active</g:if>">
                    <a href="${createLink(action:"consumers")}">Who is Embedding</a>
                </li>
                <li role="presentation" class="<g:if test="${params.action == "viewLocation"}">active</g:if>">
                    <a href="${createLink(action:"viewLocation")}">Viewing Location</a>
                </li>
            </ul>
        </div>
        <h1 class="page-header">${header}</h1>
    </div>
</div>