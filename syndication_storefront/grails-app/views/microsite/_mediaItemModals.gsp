<div class="modal fade" id="preview_popup" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Loading...</h4>
            </div>
            <div class="modal-body" id="modal_popup_body">
                Loading...
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<script>
    function loadModal(element){
        $("#myModalLabel").html("Loading...")
        $("#modal_popup_body").html("<i class='fa fa-spinner fa-spin fa-lg fa-fw'></i> &nbsp;Loading...")

        var mediaId = element.attr("data-media_id");
        $.getJSON("${grailsApplication.config.syndication.serverUrl}/api/v2/resources/media/"+mediaId+"/syndicate.json?callback=?", function(data){
            $("#modal_popup_body").html(data.results[0].content)
            $("#myModalLabel").html(data.results[0].name)
        })
    }

    $(document).ready(function(){
        $(document).on("click",".modal_popup_button",function(){
            loadModal($(this))
        });

        $(document).on("click", ".list-group-item", function(){
            loadModal($(this))
        });
    })
</script>