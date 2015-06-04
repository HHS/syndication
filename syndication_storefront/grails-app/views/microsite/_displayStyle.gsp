%{--panel header--}%
<g:if test="${mediaArea.header}">
    <h2>
        ${mediaArea.header}
    </h2>

</g:if>

%{--thumbnail grid display style--}%
<g:if test="${mediaArea.displayStyle.name == "Thumbnail Grid"}">
    <div class="microsite-article on-color">
    <g:each in="${mediaItems}" var="mediaItemInstance">
        <div class="row-fluid">
            <div class="col-md-6" style="padding-left: 10px;padding-right: 0px;padding-bottom: 10px;padding-top: 0;">
                %{--<div class="thumbnail" style="margin: 0 0 0 0">--}%
                    <a href="#" class="list-group-item display-thumbnail" data-media_id="${mediaItemInstance.id}" data-toggle="modal" data-target="#preview_popup">
                        <img style="width:100%" class="img-responsive" src="${apiBaseUrl}/resources/media/${mediaItemInstance?.id}/thumbnail.jpg"/>
                    </a>
                %{--</div>--}%
            </div>
        </div>
    </g:each>
    </div>
</g:if>

%{--list display style--}%

<g:if test="${mediaArea.displayStyle.name == "List"}">
    <div class="microsite-article on-color">
    <div class="list-group">
        <g:each in="${mediaItems}" var="mediaItemInstance">
            <a href="#" class="list-group-item" data-media_id="${mediaItemInstance.id}" data-toggle="modal" data-target="#preview_popup">
                ${mediaItemInstance.name}
            </a>
        </g:each>
    </div>
    </div>
</g:if>


%{--full content/one at a time--}%

<g:if test="${mediaArea.displayStyle.name == "Full Content"}">
    <div class="microsite-article on-color">
    <a id="prevFullContent${panel}"><i class="fa fa-arrow-left fa-3x"></i></a> <a id="nextFullContent${panel}" class="pull-right" ><i class="fa fa-arrow-right fa-3x"></i></a>
    <div id="fullContentView${panel}">
        %{--loads fullContentPagination with ajax call--}%
    </div>
    </div>
<script>
    $(document).ready(function() {
        loadContent(0, ${panel})
    });

    $('#nextFullContent' + ${panel}).click(function(){
        var mediaSize = parseInt(document.getElementById("full-content-data${panel}").dataset.mediacount,10);
        var offset = parseInt(document.getElementById("full-content-data${panel}").dataset.offset,10);
        if(offset < (mediaSize - 1)) {
            loadContent(offset + 1, ${panel});
        } else {
            loadContent(0, ${panel});
        }
    });

    $('#prevFullContent' + ${panel}).click(function(){
        if(parseInt(document.getElementById("full-content-data${panel}").dataset.offset,10) > 0) {
            loadContent(parseInt(document.getElementById("full-content-data${panel}").dataset.offset,10) - 1, ${panel});
        } else {
            loadContent(parseInt(document.getElementById("full-content-data${panel}").dataset.mediacount,10) - 1, ${panel});
        }
    });

    function loadContent(offset, panel)
    {
        var mediaItemIds = ${mediaItems.id};

        $.ajax({
            method: "POST",
            url:"${g.createLink(controller:'microsite',action:'displayStyle')}",
            data: {
                mediaItems:mediaItemIds.toString(), offset:offset, panel:panel
            },
            success: function(data) {
                document.getElementById("fullContentView" + panel).innerHTML = data;
            },
            error: function(request, status, error) {
                alert(error)
            },
            complete: function() {
            }
        });
    }

</script>
</g:if>
