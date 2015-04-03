<!-- Modal -->
<div class="modal fade" id="urlModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
     xmlns="http://www.w3.org/1999/html">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="urlModallLabel">Import Video Metadata</h4>
            </div>
            <g:form url="[action:'importVideo',controller:'video']">
                <div class="modal-body">
                    <div class="form-group">
                        <label class="sr-only" for="videoUrl">Video Url</label>
                        <input type="text" class="form-control" id="videoUrl" name="videoUrl" placeholder="Youtube video URL">
                    </div>
                    <br>
                    <div class="panel panel-info">
                        <div class="panel-heading">
                            <h3 class="panel-title">How to use Import Video Metadata</h3>
                        </div>

                        <div class="panel-body">
                            <ul>
                                <li>Enter a fully qualified YouTube player URL in the text field.</li>
                                <li>Enter the URL of a single video only since playlists are not supported yet.</li>
                                <li>Once 'Import' is clicked, metadata information like video title, description etc will be pulled into the Create Video form.</li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn" data-dismiss="modal">Cancel</button>
                    <button type="submit" id="create" class="btn btn-warning" name="create">
                    <i class="fa fa-arrow-circle-down"></i> Import
                    </button>
                </div>
            </g:form>
        </div>
    </div>
</div>