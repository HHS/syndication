<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="layout" content="microsite"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Syndication: Microsite Blog Template</title>
    <link rel="shortcut icon" href="">
    <asset:stylesheet src="microsite/microsite_bar.css"/>
    <!--[if IE]>
    <script src="https://cdn.jsdelivr.net/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://cdn.jsdelivr.net/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
</head>

<body>
<g:render template="/microsite/micrositeBar"/>

<div class="template container">

    <div class="microsite blog">
        <a id="pageContent"></a>
        <g:render template="/microsite/templateHeader"/>

        <div class="microsite-blog-content row">

            <div class="microsite-blog-left-col col-sm-8 col-xs-12 clearfix">

                <g:each in="${blogs}" var="blog">

                    <div class="microsite-article on-white">

                        ${raw(blog?.json?.results?.content[0])}

                    </div><!-- end microsite-article  -->

                </g:each>

            </div><!-- end microsite-blog-left-col col-sm-8 col-xs-12 -->

        
            <div class="microsite-blog-right-col col-sm-4 col-xs-12 clearfix">
                <g:if test="${pane2MediaItems && microSite.mediaArea2}">
                    <div class="microsite-blog-right-col-top clearfix">

                        <div class="microsite-article on-color">

                            <g:render template="../microsite/displayStyle"
                                      model="[mediaArea: microSite.mediaArea2, mediaItems: pane2MediaItems, panel:1]"/>

                        </div><!-- end microsite-article  -->

                    </div><!-- end microsite-blog-right-col-top" -->
                </g:if>
                <g:if test="${pane3MediaItems && microSite.mediaArea3}">

                    <div class="microsite-blog-right-col-bottom clearfix">

                        <div class="microsite-article on-color">

                            <g:render template="../microsite/displayStyle" model="[mediaArea: microSite.mediaArea3, mediaItems: pane3MediaItems, panel:2]"/>

                        </div>

                    </div><!-- end microsite-blog-right-col-bottom -->
                </g:if>
            </div><!-- end microsite-blog-right-col col-xs-4 -->

        <g:render template="/microsite/mediaItemModals"/>

        </div><!-- end microsite-content row  -->

    <g:render template="/microsite/templateFooter"/>

    </div><!-- end microsite blog -->
    
</div>

</body>
</html>
