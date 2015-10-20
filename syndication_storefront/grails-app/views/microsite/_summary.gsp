<g:if test="${ajaxRequest}">
    <g:if test="${listType}">
        <div class="form-group">
            <label class="col-md-4 control-label" for="mediaSourceSummary${area}">Media Source</label>
            <div class="col-md-8">
                <p class="form-control-static" id="mediaSourceSummary${area}">${listType.name}</p>
            </div>
        </div>
    </g:if>
    <g:else>
        <div class="form-group">
            <label class="col-md-4 control-label text-danger" for="mediaSourceSummary${area}">Media Source</label>
            <div class="col-md-8">
                <p class="form-control-static text-danger" id="mediaSourceSummary${area}">No Media Source Selected</p>
            </div>
        </div>
    </g:else>
    <g:if test="${item}">
        <div class="form-group">
            <label class="col-md-4 control-label" for="listTypeSummary${area}">${listType ? listType?.name : 'List'}</label>
            <div class="col-md-8">
                <p class="form-control-static" id="listTypeSummary${area}">${item.name}</p>
            </div>
        </div>
    </g:if>
    <g:else>
        <div class="form-group">
            <label class="col-md-4 control-label text-danger" for="listTypeSummary${area}">${listType?.name ?: 'List'}</label>
            <div class="col-md-8">
                <p class="form-control-static text-danger" id="listTypeSummary${area}">No ${listType?.name ?: 'List'} Selected</p>
            </div>
        </div>
    </g:else>
    <g:if test="${sidePanel}">
        <g:if test="${header}">
            <div class="form-group">
                <label class="col-md-4 control-label" for="panelHeaderSummary${area}">Panel Header</label>
                <div class="col-md-8">
                    <p class="form-control-static" id="panelHeaderSummary${area}">${header}</p>
                </div>
            </div>
        </g:if>
        <g:else>
            <div class="form-group">
                <label class="col-md-4 control-label" for="panelHeaderSummary${area}">Panel Header</label>
                <div class="col-md-8">
                    <p class="form-control-static" id="panelHeaderSummary${area}">NONE</p>
                </div>
            </div>
        </g:else>
    </g:if>

    <g:if test="${sortBy}">
        <div class="form-group">
            <label class="col-md-4 control-label" for="sortSummary${area}">Sorted By</label>
            <div class="col-md-8">
                <p class="form-control-static" id="sortSummary${area}">${sortBy}</p>
            </div>
        </div>
    </g:if>
    <g:else>
        <div class="form-group">
            <label class="col-md-4 control-label text-danger" for="sortSummary${area}">Sorted By</label>
            <div class="col-md-8">
                <p class="form-control-static text-danger" id="sortSummary${area}">You never selected a your sort value</p>
            </div>
        </div>
    </g:else>
    <g:if test="${orderBy}">
        <div class="form-group">
            <label class="col-md-4 control-label" for="orderSummary${area}">Ordered By</label>
            <div class="col-md-8">
                <p class="form-control-static" id="orderSummary${area}">${orderBy}</p>
            </div>
        </div>
    </g:if>
    <g:else>
        <div class="form-group">
            <label class="col-md-4 control-label text-danger" for="orderSummary${area}">Ordered By</label>
            <div class="col-md-8">
                <p class="form-control-static text-danger" id="orderSummary${area}">You never selected the Order</p>
            </div>
        </div>
    </g:else>
    <g:if test="${displayStyle}">
        <div class="form-group">
            <label class="col-md-4 control-label" for="displayStyleSummary${area}">Display Style</label>
            <div class="col-md-8">
                <p class="form-control-static" id="displayStyleSummary${area}">${displayStyle}</p>
            </div>
        </div>
    </g:if>
    <g:elseif test="${sidePanel}">
        <div class="form-group">
            <label class="col-md-4 control-label text-danger" for="displayStyleSummary${area}">Display Style</label>
            <div class="col-md-8">
                <p class="form-control-static text-danger" id="displayStyleSummary${area}">You never selected a Display Style</p>
            </div>
        </div>
    </g:elseif>
</g:if>


<g:else>
    Nothing was selected.
</g:else>
