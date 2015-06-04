<g:if test="${ajaxRequest}">
    <g:if test="${listType}">
        <div class="form-group">
            <label class="col-lg-4 control-label">List Type</label>
            <div class="col-lg-8">
                <p class="form-control-static">${listType}</p>
            </div>
        </div>
    </g:if>
    <g:else>
        <div class="form-group">
            <label class="col-lg-4 control-label text-danger">List Type</label>
            <div class="col-lg-8">
                <p class="form-control-static text-danger">You never selected a Media Source</p>
            </div>
        </div>
    </g:else>
    <g:if test="${item}">
        <div class="form-group">
            <label class="col-lg-4 control-label">List name</label>
            <div class="col-lg-8">
                <p class="form-control-static">${item.name}</p>
            </div>
        </div>
    </g:if>
    <g:else>
        <div class="form-group">
            <label class="col-lg-4 control-label text-danger">List name</label>
            <div class="col-lg-8">
                <p class="form-control-static text-danger">You never selected a list name</p>
            </div>
        </div>
    </g:else>
    <g:if test="${header}">
        <div class="form-group">
            <label class="col-lg-4 control-label">Panel Header</label>
            <div class="col-lg-8">
                <p class="form-control-static">${header}</p>
            </div>
        </div>
    </g:if>
    <g:else>
        <div class="form-group">
            <label class="col-lg-4 control-label">Panel Header</label>
            <div class="col-lg-8">
                <p class="form-control-static">NONE</p>
            </div>
        </div>
    </g:else>
    <g:if test="${sortBy}">
        <div class="form-group">
            <label class="col-lg-4 control-label">SortBy</label>
            <div class="col-lg-8">
                <p class="form-control-static">${sortBy}</p>
            </div>
        </div>
    </g:if>
    <g:else>
        <div class="form-group">
            <label class="col-lg-4 control-label text-danger">SortBy</label>
            <div class="col-lg-8">
                <p class="form-control-static text-danger">You never selected a your sort value</p>
            </div>
        </div>
    </g:else>
    <g:if test="${orderBy}">
        <div class="form-group">
            <label class="col-lg-4 control-label">OrderBy</label>
            <div class="col-lg-8">
                <p class="form-control-static">${orderBy}</p>
            </div>
        </div>
    </g:if>
    <g:else>
        <div class="form-group">
            <label class="col-lg-4 control-label text-danger">OrderBy</label>
            <div class="col-lg-8">
                <p class="form-control-static text-danger">You never selected the Order</p>
            </div>
        </div>
    </g:else>
    <g:if test="${displayStyle}">
        <div class="form-group">
            <label class="col-lg-4 control-label">Display Style</label>
            <div class="col-lg-8">
                <p class="form-control-static">${displayStyle}</p>
            </div>
        </div>
    </g:if>
    <g:elseif test="${sidePanel}">
        <div class="form-group">
            <label class="col-lg-4 control-label text-danger">Display Style</label>
            <div class="col-lg-8">
                <p class="form-control-static text-danger">You never selected a Display Style</p>
            </div>
        </div>
    </g:elseif>
</g:if>


<g:else>
    Nothing Was Selected!
</g:else>