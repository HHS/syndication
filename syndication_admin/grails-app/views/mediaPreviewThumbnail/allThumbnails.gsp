<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 4/20/15
  Time: 3:39 PM
--%>

<%@ page import="com.ctacorp.syndication.media.MediaItem" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Thumbnail Overview</title>
    <meta name="layout" content="main">
    <style>
    .thumb-holder {
        width: 260px;
        height: 198px;
    }

    .thumbnail {
        max-width: 260px;
    }
    </style>
</head>

<body>
<div>
    <div class="pagination">
        <g:paginate total="${com.ctacorp.syndication.media.MediaItem.count()}" action="allThumbnails"/>
    </div>
    <br/>
    <br/>
    <g:form>
        <div class="form-group">
            <g:actionSubmit value="Flush Cache" action="flushCache" class="btn btn-info"/>
            <g:actionSubmit value="Generate Missing" action="generateMissing" class="btn btn-warning"/>
            <g:actionSubmit value="Regenerate All" action="regenerateThumbnailPreviewForAllMedia"
                            class="btn btn-danger"/>
        </div>

        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label>Pick a collection to Regenerate:</label>
                    <g:select class="form-control"
                              from="${com.ctacorp.syndication.media.Collection.list(sort: 'name', order: 'desc')}"
                              name="collectionId"
                              optionKey="id"
                              noSelection="['': 'Choose a Collection']"/>
                </div>

                <div class="form-group">
                    <label>Or enter a Collection ID</label>
                    <g:textField class="form-control" name="manualCollectionId"/>
                </div>

                <div class="form-group">
                    <g:actionSubmit value="Regenerate Collection" action="regenerateThumbnailsForCollection"
                                    class="btn btn-warning"/>
                </div>
            </div>

            <div class="col-md-6">
                <div class="form-group">
                    <label>Starting ID</label>
                    <g:textField class="form-control" name="start"/>
                </div>

                <div class="form-group">
                    <label>Ending ID</label>
                    <g:textField class="form-control" name="end"/>
                </div>

                <div class="form-group">
                    <g:actionSubmit value="Regenerate Range" action="regenerateThumbnailsForRange"
                                    class="btn btn-warning"/>
                </div>
            </div>
        </div>

        <g:hiddenField name="max" value="${params.max ?: 100}"/>
        <g:hiddenField name="offset" value="${params.offset ?: 0}"/>
    </g:form>
    <br/>

    <div>
        <g:each in="${mediaItems}" var="mi">
            <div style="display:inline-block; border:1px solid black; margin-bottom: 4px;">
                <p>
                    <span>${mi.id} -</span>
                    <span><g:link action="show" controller="mediaItem" id="${mi.id}">View</g:link></span>
                </p>

                <div id="thumb_${mi.id}" class="thumb-holder">
                    <img class="thumbnail"
                         src="${grails.util.Holders.config.syndication.serverUrl}/api/v2/resources/media/${mi.id}/thumbnail.jpg"/>
                </div>

                <div style="padding: 5px;">
                    <g:remoteLink class="btn btn-info center-block" action="regenerateThumbnailPreviewForSingleItem"
                                  id="${mi.id}" update="thumb_${mi.id}">Regenerate</g:remoteLink>
                </div>
            </div>
        </g:each>
    </div>
    <br/>
    <br/>

    <div class="pagination">
        <g:paginate class="pagination" total="${com.ctacorp.syndication.media.MediaItem.count()}"
                    action="allThumbnails"/>
    </div>
    <br/>
    <br/>
</div>
</body>
</html>