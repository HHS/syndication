<div class="grids-${gridOffset}">

    <g:each in="${pane1MediaItems}" var="mediaItemInstance">
        <div class="microsite-article on-white panel">
            <div class="pin">
                <h2 class="microsite-article-title">${mediaItemInstance?.name}</h2>
                <img class="microsite-article-pic" src="${apiBaseUrl}/resources/media/${mediaItemInstance?.id}/thumbnail.jpg" alt="thumbnail for ${mediaItemInstance.name}"/>

                <p>${mediaItemInstance?.description}</p>
                <br/>

                <p>
                    <button type="button" class="btn btn-default center-block modal_popup_button"
                            data-toggle="modal" data-target="#preview_popup"
                            data-media_id="${mediaItemInstance.id}">View</button>
                </p>

            </div>

        </div>

    </g:each>

</div>

<asset:javascript src="microsite.js"/>