<div class="panel panel-default">
    <div class="panel-heading">
        <i class="fa fa-tags"></i> Tag this MediaItem
    </div>

    <div class="panel-body">
        <g:form controller="tag" action="setTags">
            <div class="row">
                <div class="col-md-6 col-lg-6">
                    <div class="form-group">
                        <label class="col-md-4" for="languageLabel">Language</label>
                        <div class="col-md-12">
                            <g:select name="languageId" value="${languageId}" from="${languages}" optionKey="id" optionValue="name" class="form-control tagSpecifics"/>
                        </div>
                    </div>
                </div>
                <div class="col-md-6 col-lg-6">
                    <div class="form-group">
                        <label class="col-md-4" for="typeLabel">Type</label>
                        <div class="col-md-12">
                            <g:select name="tagTypeId" value="${tagTypeId}" from="${tagTypes}" optionKey="id" optionValue="name" class="form-control tagSpecifics"/>
                        </div>
                    </div>
                </div>
            </div>
            <br>
            <label for="tagIds">Select tags</label>
            <div id="tokenHolder">
                <g:textField name="tagIds"/>
            </div>
            <g:hiddenField name="mediaId" value="${mediaItemInstance?.id}"/>
            <br/>
            <div class="pull-right">
                <g:submitButton name="Save Tags" id="tagButton" class="btn btn-default btn-md btn-success"/>
            </div>
        </g:form>
    </div>
</div>

<g:javascript>
    $(document).ready(function () {
        $("#tagIds").tokenInput("${g.createLink(controller: 'tag', action: 'tagSearch')}.json" + "?syndicationId=${mediaItemInstance?.id}&tagTypeId=${tagTypeId}&languageId=${languageId}", {
            prePopulate:${tags.encodeAsRaw()}
        });

        $(".tagSpecifics").change(function () {
            var tagLangId = $("#languageId").val();
            var tagTypeId = $("#tagTypeId").val();
            updateTokenInput(tagTypeId, tagLangId);
        })

        function updateTokenInput(tagTypeId, languageId){
            $("#tokenHolder").html('<g:textField name="tagIds"/>')

            $.getJSON("${g.createLink(controller: 'tag', action: 'getTagsForSyndicationId')}.json" + "?syndicationId=${mediaItemInstance?.id}&tagTypeId="+tagTypeId+"&languageId="+languageId, function(data){
                console.log(data);
                $("#tagIds").tokenInput("${g.createLink(controller: 'tag', action: 'tagSearch')}.json" + "?syndicationId=${mediaItemInstance?.id}&tagTypeId="+tagTypeId+"&languageId="+languageId, {
                    prePopulate:data
                });
            });
        }
    }
);
</g:javascript>