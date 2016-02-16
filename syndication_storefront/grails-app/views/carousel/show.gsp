<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="layout" content="microsite"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Syndication: Microsite Carousel Template</title>
    <link rel="shortcut icon" href="">
    <asset:javascript src="resize/jquery.resize.js"/>
    <asset:javascript src="waitForImages/jquery.waitforimages.js"/>
    <asset:javascript src="modernizr/modernizr.js"/>
    <asset:javascript src="carousel3d/jquery.carousel-3d.js"/>
    <asset:stylesheet src="carousel3d/jquery.carousel-3d.default.css"/>
    <asset:stylesheet src="microsite/microsite_bar.css"/>
    <!--[if IE]>
    <script src="https://cdn.jsdelivr.net/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://cdn.jsdelivr.net/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
</head>

<body>

<g:render template="/microsite/micrositeBar"/>

<div class="template container">

    <div class="microsite carousel">
        <a id="pageContent"></a>
        <g:render template="/microsite/templateHeader"/>

        <div class="microsite-carousel-content row">

            <div class="carousel-microsite-carousel col-xs-12">

                <div data-carousel-3d>

                    <g:if test="${pane1MediaItems}">
                        <g:each in="${pane1MediaItems}" var="media">
                            <img style="width:100%" class="microsite-article-pic"
                                 src="${apiBaseUrl}/resources/media/${media?.id}/preview.jpg" alt="thumbnail for ${media.name}"/>
                        </g:each>
                    </g:if>
                    <g:else>
                        <asset:image src="bad.jpg" style="width:100%" class="microsite-article-pic" alt="thumbnail for empty list"/>
                    </g:else>

                </div>

            </div>
            <g:if test="${pane2MediaItems && microSite.mediaArea2}">
            
                <div class="microsite-carousel-left-col col-sm-6 col-xs-12 clearfix">

                    <div>

                        <g:each in="${pane2MediaItemContents}" var="mediaItemContent">

                            <div class="microsite-article on-color">

                                ${raw(mediaItemContent?.json?.results?.content[0])}

                            </div>

                        </g:each>

                    </div>

                </div><!-- end microsite-carousel-left-col col-sm-6 col-xs-12 -->
            </g:if>
            <g:if test="${pane3MediaItems && microSite.mediaArea3}">

                <div class="microsite-carousel-right-col col-sm-6 col-xs-12 clearfix">

                    <div>

                        <g:each in="${pane3MediaItemContents}" var="mediaItemContent">

                        <div class="microsite-article on-color">

                            ${raw(mediaItemContent?.json?.results?.content[0])}
                        </div>

                        </g:each>

                    </div>

                </div><!-- end microsite-carousel-right-col col-sm-6 col-xs-12 -->
            </g:if>
        </div><!-- end row -->

    <g:render template="/microsite/mediaItemModals"/>

    <g:render template="/microsite/templateFooter"/>

    </div><!-- end microsite carousel -->

</div><!-- end template container -->

<script>

    $(document).ready(function () {
//        $('#carousel').Carousel3d(
//                {
//                    itemWidth: 110,
//                    itemHeight: 62,
//                    itemMinWidth: 50,
//                    items: 'a',
//                    reflections: .5,
//                    rotationSpeed: 1.8
//                }
//        );
    });

</script>
</body>
</html>
