<!-- Modal -->
<div class="modal fade" id="playlistModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
     xmlns="http://www.w3.org/1999/html">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="playlistModallLabel">Import Playlist Metadata</h4>
            </div>
            <g:form url="[action:'importPlaylist',controller:'video']" class="form-horizontal">
                <div class="modal-body">
                    <synd:message/>
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
                                <li>The link provided is a youtube playlist.</li>
                                <li>To import all of the videos from the playlist select a source and language then click Import. </li>
                            </ul>
                        </div>
                    </div>

                    %{--Select Source--}%
                    <div class="form-group ${hasErrors(bean:mediaItemInstance, field:'source', 'errors')}">
                        <label class="col-md-2 control-label" for="source">Source<span class="required-indicator">*</span></label>
                        <div class="col-md-8">
                            <g:select from="${com.ctacorp.syndication.Source.list([sort: "name"])}" name="source.id" id="source" class="form-control" optionValue="name" optionKey="id" value="${mediaItemInstance?.source?.id}" noSelection="${['null':'-Choose a Source-']}"/>
                        </div>
                    </div>
                    <!-- Select Language -->
                    <div class="form-group ${hasErrors(bean:mediaItemInstance, field:'language', 'errors')}">
                        <label class="col-md-2 control-label" for="language">Language<span class="required-indicator">*</span></label>
                        <div class="col-md-8">
                            <g:select from="${com.ctacorp.syndication.Language.findAllByIsActive(true, [sort: "name"])}" name="language.id" id="language" optionKey="id" optionValue="name" value="${mediaItemInstance?.language?.id}" class="form-control" />
                        </div>
                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn" data-dismiss="modal">Cancel</button>
                    <button type="submit" id="create" class="btn btn-warning" name="create">
                        <i class="fa fa-arrow-circle-down"></i> Import Entire Playlist
                    </button>
                </div>
            </g:form>
        </div>
    </div>
</div>
<script>
    $(document).ready(function(){
        if(${playlist == true}){
            $('#playlistModal').modal('show');
        }
    })
</script>