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

                <div class="blogs">

                    <g:render template="blogList"/>

                </div>

                <div class='row' id="noMoreContent" style="display: none;">
                    <div class='col-md-12'>
                        <div class="alert alert-info alert-dismissable break-word">No More Content
                            <button type="button" class="close" style='top:-8px;' data-dismiss="alert" aria-hidden="true">&times;</button>
                        </div>
                    </div>
                </div>

                <div class="load-more-buttons">
                    <div class="row">
                        <div class="col-md-6">
                            <input type="button" class="form-group btn btn-default text-center" id="loadBlogsButton" value="Load More"/>
                            <div id="spinnerDiv" style="width:50px;display: none;" class="text-center"><i class="fa fa-refresh fa-spin fa-lg"></i></div>
                        </div>
                        <div class="col-md-6">
                            <button class="form-group btn btn-default pull-right" id="returnTop">Back To Top <i class='fa fa-arrow-up'></i></button>
                        </div>

                    </div>

                </div>

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
<script>

    var blogOffset = ${blogOffset};
    var currentlyProcessed = false;

    $("#loadBlogsButton").on("click", function(){
        $("#loadBlogsButton").hide();
        $("#spinnerDiv").show();

        blogOffset = blogOffset + ${maxBlogs};
        $.ajax({ // create an AJAX call...
            data: {blogOffset:blogOffset, maxBlogs:"${maxBlogs}", id:"${microSite.id}"}, // get the form data
            type: 'POST', // GET or POST
            url: '${g.createLink(controller: 'blog', action: 'getMoreBlogs')}', // the file to call
            success: function (response) { // on success..
                $("#spinnerDiv").fadeOut("fast",function(){
                    if(response.constructor.name == "String"){
                        $('.blogs').append(response); // update the DIV
                        $("#loadBlogsButton").show();
                        currentlyProcessed = false;
                    } else {
                        $("#loadBlogsButton").show();
                        $('#noMoreContent').show();
                        currentlyProcessed = false;
                    }
                });
            }
        });
    });

    $("#returnTop").on("click", function(){
        $('html, body').animate({ scrollTop: 0 }, 'slow');
    });

    $(window).scroll(function() {
        if($(window).scrollTop() + $(window).height() == $(document).height() && !currentlyProcessed) {
            currentlyProcessed = true;
            $("#loadBlogsButton").trigger("click");
        }
    });
</script>
</body>
</html>
