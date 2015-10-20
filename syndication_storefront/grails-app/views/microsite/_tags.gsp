<%@ page import="com.ctacorp.syndication.microsite.MediaSelector" %>
<style>
.tt-query,
.tt-hint {
    width: 396px;
    height: 30px;
    padding: 8px 12px;
    font-size: 24px;
    line-height: 30px;
    border: 2px solid #ccc;
    border-radius: 8px;
    outline: none;
}

.tt-input { /* UPDATE: newer versions use tt-input instead of tt-query */
    box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
}

.tt-hint {
    color: #999
}

.tt-menu {
    width: 422px;
    margin-top: 12px;
    padding: 8px 0;
    background-color: #fff;
    border: 1px solid #ccc;
    border: 1px solid rgba(0, 0, 0, 0.2);
    border-radius: 8px;
    box-shadow: 0 5px 10px rgba(0,0,0,.2);
    word-break: break-all;
    cursor: pointer;
}

.tt-suggestion {
    padding: 3px 20px;
    font-size: 18px;
    line-height: 24px;
}

.tt-suggestion.tt-cursor, .tt-suggestion:hover { /* UPDATE: newer versions use .tt-suggestion.tt-cursor */
    color: #fff;
    background-color: #4c8bb8;

}

.tt-suggestion p {
    margin: 0;
}
</style>
<div class="col-md-6 col-lg-6">
    <div class="form-group">
        <label class="col-md-4" for="pane${area}TagLanguageId">Language</label>
        <div class="col-md-12">
            <g:select name="pane${area}TagLanguageId" data-microSiteId="${microSite?.id}" data-mediaArea="${area}" value="${selectedLanguage}" from="${languages}" optionKey="id" optionValue="name" class="form-control tagSpecifics" aria-label="choose tag language"/>
        </div>
    </div>
</div>
<div class="col-md-6 col-lg-6">
    <div class="form-group">
        <label class="col-md-4" for="pane${area}TypeId">Type</label>
        <div class="col-md-12">
            <g:select name="pane${area}TypeId" data-microSiteId="${microSite?.id}" data-mediaArea="${area}" value="${selectedTagType}" from="${tagTypes}" optionKey="id" optionValue="name" class="form-control tagSpecifics" aria-label="choose tag type"/>
        </div>
    </div>
</div>

<div class="form-group">
    <label class="col-sm-4 control-label" for="pane${area}ListId">Tag</label>
    <div class="col-sm-8">
        <span class="typeahead-query">
        <input id="typeahead${area}" type="search" data-provide="typeahead" value="${currentTag?.name ?: ''}" name="searchTag" placeholder="Search" class="form-control" autocomplete="off" aria-label="type tag name"/>
        <input id="pane${area}ListId" type="search" hidden name="pane${area}ListId" value="${currentTag?.id}"/>
        </span>
    </div>
</div>

<div class="form-group" hidden="hidden">
    <label class="col-sm-2 control-label" for="pane${area}selectorType">Collection</label>
    <input type="hidden" id="pane${area}selectorType" name="pane${area}selectorType" hidden="" value="${selectorType}"/>
</div>

<asset:javascript src="typeahead/typeahead.bundle.min.js"/>
<script>
    $(document).ready(function(){
        updateFormCompletion(${area});
    });

    $('#pane' + ${area} + 'TagLanguageId').on('change', function(){
        updateTags(this);
    });

    $('#pane' + ${area} + 'TypeId').on('change', function(){
        updateTags(this);
    });


    // typeahead caching code
    var bestPictures = new Bloodhound({
        datumTokenizer: Bloodhound.tokenizers.obj.whitespace("name"),
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        remote: {
            url: '${currentServerUrl}/microsite/searchTags.json?name=%QUERY&typeId=${selectedTagType}&languageId=${selectedLanguage}',
            wildcard: '%QUERY',
            cache:false,
            filter:function(response){
                updateTagListId(response);
                return response;
            }
        }
    });

    // typeahead display and its options
    $('#typeahead' + ${area}).typeahead({
        minLength: 0,
        maxItem: 15,
        order: "asc",
        hint: false,
        searchOnFocus: true
    }, {
        limit:10,
        display: 'name',
        source: bestPictures
    });
    //updates listId when a tag is selected
    $('#typeahead' + ${area}).bind('typeahead:select', function(ev, suggestion) {
        document.getElementById("pane${area}ListId").value = suggestion.id;
            updateFormCompletion(${area});
    });

    //updates a listId when a tag is typed out correctly but not selected
    function updateTagListId(response){
        var noMatch = 0;
        var i = 0;
        while(response[i]){
            if(response[i].name == document.getElementById("typeahead${area}").value){
                document.getElementById("pane${area}ListId").value = response[i].id
            } else {
                noMatch ++;
            }
            i++
        }
        if(noMatch == response.length){
            document.getElementById("pane${area}ListId").value = ""
        }
        updateFormCompletion(${area});
    }

    //updates available tags when their language or type is changed
    function updateTags(htmlTag){
        var listType = "TAG";
        var mediaArea = $(htmlTag).attr("data-mediaArea");
        var microSiteId = $(htmlTag).attr("data-microSiteId");
        var language = $('#pane' + ${area} + 'TagLanguageId').val();
        var type = $('#pane' + ${area} + 'TypeId').val();

        $.ajax({ // create an AJAX call...
            data: {listType:listType, mediaAreaValue:mediaArea, microSiteId:microSiteId,languageId:language, typeId:type}, // get the form data
            type: 'POST', // GET or POST
            url: '${g.createLink(controller: 'microsite', action: 'specificList')}', // the file to call
            success: function (response) { // on success..
                $('#pane' + mediaArea + 'ListBody').html(response); // update the DIV
                document.getElementById(htmlTag.id).focus();
            }
        });
    }
</script>