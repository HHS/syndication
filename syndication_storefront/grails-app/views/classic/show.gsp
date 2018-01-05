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
        <a id="pageContent"></a>
        <g:render template="/microsite/templateHeader"/>

        <div class="microsite-classic-content row">

            <div class="microsite-classic-left-col col-sm-7 col-xs-12 clearfix">

                <div class="panel-group classics" id="accordion" role="tablist" aria-multiselectable="true">

                    <g:set var="firstPanel" value="${true}"/>

                    <g:render template="classicList"/>

                </div>
                <br>
                <div class='row' id="noMoreContent" style="display: none;">
                    <div class='col-md-12'>
                        <div class="alert alert-info alert-dismissable break-word">No More Content
                            <button type="button" class="close" style='top:-8px;' data-dismiss="alert" aria-hidden="true">&times;</button>
                        </div>
                    </div>
                </div>

                <div class="row load-more">
                    <div class="col-md-6">
                        <input type="button" class="form-group btn btn-default text-center" id="loadClassicsButton" value="Load More"/>
                        <div id="spinnerDiv" style="width:50px;display: none;" class="text-center"><i class="fa fa-refresh fa-spin fa-lg"></i></div>
                    </div>
                    <div class="col-md-6">
                        <button class="form-group btn btn-default pull-right" id="returnTop">Back To Top <i class='fa fa-arrow-up'></i></button>
                    </div>

                </div>


            </div><!-- end col-sm-7 col-xs-12-->

            <div class="microsite-classic-right-col col-sm-5 col-xs-12 clearfix">
                <g:if test="${pane2MediaItems && microSite.mediaArea2}">
                    <div class="microsite-classic-right-col-top row clearfix">
                        <div class="microsite-article row">
                            <g:render template="/microsite/displayStyle"
                                      model="[mediaArea: microSite.mediaArea2, mediaItems: pane2MediaItems, panel:1]"/>
                        </div>
                    </div>
                </g:if>
                <g:if test="${pane3MediaItems && microSite.mediaArea3}">
                    <div class="microsite-classic-right-col-bottom row clearfix">
                        <g:render template="/microsite/displayStyle" model="[mediaArea: microSite.mediaArea3, mediaItems: pane3MediaItems, panel:2]"/>
                    </div>
                </g:if>
            </div>

            <g:render template="/microsite/mediaItemModals" model="[apiBaseUrl:apiBaseUrl]"/>

        </div><!-- end microsite-content  -->
    </div>

    <g:render template="/microsite/templateFooter"/>

</div>
<script>
    $(document).ready(function(){
        $(".collapsable-link").on("click", function(){
            if(this.getAttribute("aria-expanded") == "true") {
                this.setAttribute("aria-expanded", false);
            } else {
                var tabs
                if(!document.getElementsByClassName) {//for IE browsers
                    document.getElementsByClassName = function(className) {
                        return this.querySelectorAll("." + className);
                    };
                    tabs = document.getElementsByClassName("collapsable-link");
                } else {//if not IE than getElementsBuClassName already works
                    tabs = document.getElementsByClassName("collapsable-link");
                }
                for(var index = 0;index < tabs.length;index++) {
                    tabs[index].setAttribute("aria-expanded", false)
                }
                this.setAttribute("aria-expanded", true);
            }
        });


        var classicOffset = ${classicOffset};
        var currentlyProcessed = false;

        $("#loadClassicsButton").on("click", function(){
            $("#loadClassicsButton").hide();
            $("#spinnerDiv").show();

            classicOffset = classicOffset + ${maxClassics};
            $.ajax({ // create an AJAX call...
                data: {classicOffset:classicOffset, maxClassics:"${maxClassics}", id:"${microSite.id}"}, // get the form data
                type: 'POST', // GET or POST
                url: '${g.createLink(controller: 'classic', action: 'getMoreClassics')}', // the file to call
                success: function (response) { // on success..
                    $("#spinnerDiv").fadeOut("fast",function(){
                        if(response.constructor.name == "String"){
                            $('.classics').append(response); // update the DIV
                            $("#loadClassicsButton").show();
                            currentlyProcessed = false;
                        } else {
                            $("#loadClassicsButton").show();
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
                $("#loadClassicsButton").trigger("click");
            }
        });

    })


</script>
</body>
</html>
