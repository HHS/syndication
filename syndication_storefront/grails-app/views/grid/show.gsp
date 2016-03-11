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
        <a id="pageContent"></a>
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

                    <g:render template="gridList"/>

                </div> <!--end js-masonry -->

                <div class='row' id="noMoreContent" style="display: none;">
                    <div class='col-md-12'>
                        <div class="alert alert-info alert-dismissable break-word">No More Content
                            <button type="button" class="close" style='top:-8px;' data-dismiss="alert" aria-hidden="true">&times;</button>
                        </div>
                    </div>
                </div>

                <div class="load-more-buttons">
                    <div class="row">
                        <div class="col-md-4">
                            <input type="button" class="form-group btn btn-default text-center" id="loadGridsButton" value="Load More"/>
                            <div id="spinnerDiv" style="width:50px;display: none;" class="text-center"><i class="fa fa-refresh fa-spin fa-lg"></i></div>
                        </div>
                        <div class="col-md-4">
                            <button class="form-group btn btn-default" id="returnTop">Back To Top <i class='fa fa-arrow-up'></i></button>
                        </div>

                    </div>

                </div>

            </div><!-- end col-sm-7 col-xs-1 js-masonry-->

            <g:render template="/microsite/mediaItemModals"/>

        </div><!-- end microsite-grid-content row -->

        <g:render template="/microsite/templateFooter"/>

    </div><!-- end microsite grid -->



</div><!-- end template container -->
<script>

    var gridOffset = ${gridOffset};
    var currentlyProcessed = false;

    $("#loadGridsButton").on("click", function(){
        $("#loadGridsButton").hide();
        $("#spinnerDiv").show();

        gridOffset = gridOffset + ${maxGrids};
        $.ajax({ // create an AJAX call...
            data: {gridOffset:gridOffset, maxGrids:"${maxGrids}", id:"${microSite.id}"}, // get the form data
            type: 'POST', // GET or POST
            url: '${g.createLink(controller: 'grid', action: 'getMoreGrids')}', // the file to call
            success: function (response) { // on success..
                $("#spinnerDiv").fadeOut("fast",function(){
                    if(response.constructor.name == "String"){
                        var temp = $('#js-masonry');
                        temp.append(response).masonry('appended', temp,true ); // update the DIV
                        temp.masonry('reloadItems');
//                        temp.masonry('layout')
                        $("#loadGridsButton").show();
                        currentlyProcessed = false;
                    } else {
                        $("#loadGridsButton").show();
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
            $("#loadGridsButton").trigger("click");
        }
    });
</script>
</body>
</html>
