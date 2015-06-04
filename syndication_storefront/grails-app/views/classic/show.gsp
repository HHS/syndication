<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="layout" content="microsite"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Syndication: Microsite Classic Template</title>
    <link rel="shortcut icon" href="">
    <!--[if IE]>
    <script src="https://cdn.jsdelivr.net/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://cdn.jsdelivr.net/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
</head>

<body>

<g:render template="/microsite/micrositeBar"/>

<div class="template container independent">

    <div class="microsite classic">

        <g:render template="/microsite/templateHeader"/>

        <div class="microsite-classic-content row">

            <div class="microsite-classic-left-col col-sm-7 col-xs-12">
                
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <g:set var="firstPanel" value="${true}"/>
                    <g:each in="${pane1MediaItems}" var="media">
                        <g:if test="${firstPanel}">
                            <div class="panel panel-default">
                                <a data-toggle="collapse" data-parent="#accordion" href="#collapse${media.id}"
                                           aria-expanded="true" aria-controls="collapse${media.id}">
                                    <div class="panel-heading microsite-article" role="tab" id="${media.id}">
                                        <h2 class="microsite-article-title panel-title">
                                            ${media.name}

                                        </h2>
                                    </div>

                                </a>

                                <div id="collapse${media.id}" class="panel-collapse collapse in" role="tabpanel"
                                     aria-labelledby="${media.id}">

                                    <div class="panel-body">
                                            <img style="width:100%" class="microsite-article-pic"
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

                                <a class="collapsed" data-toggle="collapse" data-parent="#accordion"
                                           href="#collapse${media.id}" aria-expanded="false"
                                           aria-controls="collapse${media.id}">

                                    <div class="panel-heading" role="tab" id="${media.id}">
                                        <h2 class="microsite-article-title panel-title">
                                            ${media.name}
                                        </h2>
                                    </div>
                                </a>
                                <div id="collapse${media.id}" class="panel-collapse collapse" role="tabpanel"
                                     aria-labelledby="${media.id}">

                                    <div class="panel-body">
                                        <div class="microssite-article on-white col-md-3 col-xs-4">
                                            <img style="width:100%" class="microsite-article-pic"
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

                </div>

            </div><!-- end col-sm-7 col-xs-12-->

            <div class="microsite-classic-right-col col-sm-5 col-xs-12 clearfix">
                <g:if test="${pane2MediaItems && microSite.mediaArea2}">
                    <div class="microsite-classic-right-col-top row clearfix">
                        <div class="microsite-article row">
                            <g:render template="../microsite/displayStyle"
                                      model="[mediaArea: microSite.mediaArea2, mediaItems: pane2MediaItems, panel:1]"/>
                        </div>
                    </div>
                </g:if>
                <g:if test="${pane3MediaItems && microSite.mediaArea3}">
                    <div class="microsite-classic-right-col-bottom row clearfix">
                        <g:render template="../microsite/displayStyle" model="[mediaArea: microSite.mediaArea3, mediaItems: pane3MediaItems, panel:2]"/>
                    </div>
                </g:if>
            </div>

            <g:render template="/microsite/mediaItemModals"/>

        </div><!-- end microsite-content  -->
    </div>

    <g:render template="/microsite/templateFooter"/>

</div>

</body>
</html>
