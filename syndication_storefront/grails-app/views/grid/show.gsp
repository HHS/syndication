<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="layout" content="microsite"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Syndication: Microsite Grid Template</title>
    <link rel="shortcut icon" href="">
     <asset:stylesheet src="microsite/template-grid.css"/>
    <!--[if IE]>
    <script src="https://cdn.jsdelivr.net/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://cdn.jsdelivr.net/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
</head>

<body>
<g:render template="/microsite/micrositeBar"/>

<div class="template container-fluid">

    <div class="microsite grid">

        <g:render template="/microsite/templateHeader"/>

        <div class="microsite-grid-content row">

            <div class="microsite-grid-left-col col-xs-12 col-sm-6 col-md-5 col-lg-3 clearfix">
                <g:if test="${pane2MediaItems && microSite.mediaArea2}">
                    <div class="microsite-grid-left-col-top">

                        <div class="microsite-article on-color clearfix">
                            <g:render template="../microsite/displayStyle"
                                  model="[mediaArea: microSite.mediaArea2, mediaItems: pane2MediaItems, panel:1]"/>
                        </div>

                    </div><!-- end microsite-grid-left-col-top-->
                </g:if>
                <g:if test="${pane3MediaItems && microSite.mediaArea3}">
                    <div class="microsite-grid-left-col-bottom clearfix">

                        <div class="microsite-article on-color clearfix">
                            <g:render template="../microsite/displayStyle"
                                  model="[mediaArea: microSite.mediaArea3, mediaItems: pane3MediaItems, panel:2]"/>
                        </div>

                    </div><!-- end microsite-grid-left-col-bottom -->
                </g:if>
            </div><!-- end col-xs-12 col-md-4  -->

        <div class="microsite-grid-right-col col-xs-12 col-sm-6 col-md-7 col-lg-9">

                    <div id="js-masonry">

                        <g:each in="${pane1MediaItems}" var="mediaItemInstance">
                                <div class="microsite-article on-white panel">
                                        <div class="pin">
                                            <h2 class="microsite-article-title">${mediaItemInstance?.name}</h2>
                                            <img class="microsite-article-pic" src="${apiBaseUrl}/resources/media/${mediaItemInstance?.id}/thumbnail.jpg"/>

                                            <p>${mediaItemInstance?.description}</p>
                                            <br/>

                                            <p>
                                                <button type="button" class="btn btn-default center-block modal_popup_button"
                                                        data-toggle="modal" data-target="#preview_popup"
                                                        data-media_id="${mediaItemInstance.id}">View</button>
                                            </p>

                                        </div>

                                </div>

                        </g:each>

                    </div> <!--end js-masonry -->

            </div><!-- end col-sm-7 col-xs-1 js-masonry-->

            <g:render template="/microsite/mediaItemModals"/>

        </div><!-- end microsite-grid-content row -->
    
    </div><!-- end microsite grid -->

<g:render template="/microsite/templateFooter"/>

</div><!-- end template container -->

</body>
</html>
