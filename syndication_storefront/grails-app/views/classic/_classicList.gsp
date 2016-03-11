<g:each in="${pane1MediaItems}" var="media">
    <g:if test="${firstPanel}">
        <div class="panel panel-default">
            <a class="collapsable-link" data-toggle="collapse" data-parent="#accordion" href="#collapse${media.id}"
               aria-expanded="true" aria-controls="collapse${media.id}">
                <div class="panel-heading microsite-article" role="button" id="${media.id}">
                    <h2 class="microsite-article-title panel-title">
                        ${media.name}

                    </h2>
                </div>

            </a>

            <div id="collapse${media.id}" class="panel-collapse collapse in" role="tabpanel"
                 aria-labelledby="${media.id}">

                <div class="panel-body">
                    <img style="width:100%" alt="Thumbnail for ${media.name}" class="microsite-article-pic"
                         src="${apiBaseUrl}/resources/media/${media?.id}/preview.jpg"/>

                    <p>${media.description}</p>
                    <br>
                    <g:link type="button" url="${media.sourceUrl}"
                            class="btn btn-default pull-right" target="_blank">View</g:link>
                </div>

            </div>

        </div>
        <g:set var="firstPanel" value="${false}"/>
    </g:if>
    <g:else>
        <div class="panel panel-default">

            <a class="collapsed collapsable-link" data-toggle="collapse" data-parent="#accordion"
               href="#collapse${media.id}" aria-expanded="false"
               aria-controls="collapse${media.id}">

                <div class="panel-heading" role="button" id="${media.id}">
                    <h2 class="microsite-article-title panel-title">
                        ${media.name}
                    </h2>
                </div>
            </a>
            <div id="collapse${media.id}" class="panel-collapse collapse" role="tabpanel"
                 aria-labelledby="${media.id}">

                <div class="panel-body">
                    <div class="microssite-article on-white col-md-3 col-xs-4">
                        <img style="width:100%" alt="thumbnail for ${media.name}" class="microsite-article-pic"
                             src="${apiBaseUrl}/resources/media/${media?.id}/thumbnail.jpg"/>
                    </div>

                    <p>${media.description}</p>
                    <br>
                    <g:link type="button" url="${media.sourceUrl}"
                            class="btn btn-default pull-right" target="_blank">View</g:link>
                </div>
            </div>
        </div>
    </g:else>
</g:each>