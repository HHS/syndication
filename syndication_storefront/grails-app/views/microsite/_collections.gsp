<%@ page import="com.ctacorp.syndication.microsite.MediaSelector" %>
<div class="form-group">
    <label class="col-lg-4  control-label" for="pane${area}LanguageId">Language</label>
    <div class="col-lg-8">
        <g:select name="pane${area}LanguageId" data-microSiteId="${microSite?.id}" data-mediaArea="${area}" value="${selectedLanguage}" from="${languages}" optionKey="id" optionValue="name" class="form-control collection-languages"/>
    </div>
</div>

<div class="form-group">
    <label class="col-lg-4 control-label" for="pane${area}ListId">Collection</label>
    <div class="col-lg-8">
        <g:select class="form-control" name="pane${area}ListId" from="${collections}" value="${currentCollection}" optionKey="id"/>
    </div>
</div>
<div class="form-group" hidden="hidden">
    <label class="col-sm-2 control-label" for="pane${area}selectorType">Collection</label>
    <input type="hidden" id="pane${area}selectorType" name="pane${area}selectorType" hidden="" value="${selectorType}"/>
</div>


<script>
    $('.collection-languages').on("change",function(){
        var listType = "COLLECTION";
        var mediaArea = $(this).attr("data-mediaArea");
        var microSiteId = $(this).attr("data-microSiteId");
        var language = $(this).val();

        $.ajax({ // create an AJAX call...
            data: {listType:listType, mediaAreaValue:mediaArea, microSiteId:microSiteId,language:language}, // get the form data
            type: 'POST', // GET or POST
            url: '${g.createLink(controller: 'microsite', action: 'specificList')}', // the file to call
            success: function (response) { // on success..
                $('#pane' + mediaArea + 'ListBody').html(response); // update the DIV
                updateFormCompletion(mediaArea);
            }
        });
    });

</script>