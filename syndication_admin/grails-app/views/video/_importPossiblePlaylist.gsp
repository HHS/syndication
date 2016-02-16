<!-- Modal -->
<div class="modal fade" id="possiblePlaylistModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
     xmlns="http://www.w3.org/1999/html">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="possiblePlaylistModallLabel">Import Playlist Metadata</h4>
            </div>
            <g:form action="importVideo" controller="video" class="form-horizontal" params="[videoAndPlaylist:true]">
                <div class="modal-body">
                    <synd:errors/>
                    <synd:error/>

                    <div class="form-group">
                        <label class="sr-only" for="videoUrl">Playlist Url</label>
                        <div class="col-md-12">
                            <input type="text" class="form-control" id="videoUrl" name="videoUrl"  placeholder="Youtube playlist URL" value="${sourceUrl}">
                        </div>
                    </div>

                    <div class="panel panel-info">
                        <div class="panel-heading">
                            <h3 class="panel-title">Youtube Playlist</h3>
                        </div>

                        <div class="panel-body">
                            <ul>
                                <li>The link provided is a video that is contained within a playlist.</li>
                                <li>Would you like to only import the single video or all of the video within the playlist?</li>
                            </ul>
                        </div>
                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn" data-dismiss="modal">Cancel</button>
                    <g:actionSubmit type="submit" id="createSingleVideo" action="importVideo" class="btn btn-warning" name="createSingleVideo" value="Import Single Video">
                        <i class="fa fa-arrow-circle-down"></i> Import Single Video
                    </g:actionSubmit>
                    <g:actionSubmit type="submit" id="createPlaylist" action="importPlaylist" class="btn btn-warning" name="createPlaylist" value="Import Entire Playlist">
                        <i class="fa fa-arrow-circle-down"></i> Import Entire Playlist
                    </g:actionSubmit>
                </div>
            </g:form>
        </div>
    </div>
</div>
<script>
    $(document).ready(function(){
        if(${possiblePlaylist == true}){
            $('#possiblePlaylistModal').modal('show');
        }
    })
</script>