<div class="builder-form-media-area col-xs-12">
    <div class="row">
        <ul class="nav nav-pills pane${area}-tabs" role="tablist">
            <g:if test="${!microSite}">
                <li role="presentation" id="pane${area}tab1" class="active"><a role="tab" data-toggle="tab" href="#pane${area}Type">Media Source</a></li>
                <li role="presentation" id="pane${area}tab2"><a role="tab" data-toggle="tab" href="#pane${area}Content">Content</a></li>
                <li role="presentation" id="pane${area}tab3"><a role="tab" data-toggle="tab" href="#pane${area}Details">Details</a></li>
                <li role="presentation" id="pane${area}tab4"><a role="tab" data-toggle="tab" href="#pane${area}Summary">Summary</a></li>
            </g:if>
            <g:else>
                <li role="presentation" id="pane${area}tab1"><a role="tab" data-toggle="tab" href="#pane${area}Type">Media Source</a></li>
                <li role="presentation" id="pane${area}tab2"><a role="tab" data-toggle="tab" href="#pane${area}Content">Content</a></li>
                <li role="presentation" id="pane${area}tab3"><a role="tab" data-toggle="tab" href="#pane${area}Details">Details</a></li>
                <li role="presentation" id="pane${area}tab4" class="active"><a role="tab" data-toggle="tab" href="#pane${area}Summary">Summary</a></li>
            </g:else>
        </ul>
        <br>

        <div class="tab-content"> %{--Type tab--}%
            <div class="row tab-pane${!microSite ? ' active':''}" id="pane${area}Type">
            <i tabindex="0" role="note" aria-label="Choose one media source"> Choose one Media Source </i>
                <div class="choose-media-source">

                    <a tabindex="0" href="#pane${area}Content" class="choose-user-media-list pane${area}Type source_selector_button list-type" data-microSiteId="${microSite?.id}" data-mediaArea="${area}" data-listType="USER_MEDIA_LIST">
                        <div></div>
                        <p>My List <i class='fa fa-arrow-right'></i></p>
                    </a>

                    <a tabindex="0" href="#pane${area}Content" class="choose-collection pane${area}Type source_selector_button list-type" data-microSiteId="${microSite?.id}" data-mediaArea="${area}" data-listType="COLLECTION">
                        <div></div>
                        <p>Collection <i class='fa fa-arrow-right'></i></p>
                    </a>

                    <a tabindex="0" href="#pane${area}Content" class="choose-tag pane${area}Type source_selector_button list-type" data-microSiteId="${microSite?.id}" data-mediaArea="${area}" data-listType="TAG">
                        <div></div>
                        <p>Tag <i class='fa fa-arrow-right'></i></p>
                    </a>

                    <a tabindex="0" href="#pane${area}Content" class="choose-source pane${area}Type source_selector_button list-type" data-microSiteId="${microSite?.id}" data-mediaArea="${area}" data-listType="SOURCE">
                        <div></div>
                        <p>Source <i class='fa fa-arrow-right'></i></p>
                    </a>

                    <a tabindex="0" href="#pane${area}Content" class="choose-campaign pane${area}Type source_selector_button list-type" data-microSiteId="${microSite?.id}" data-mediaArea="${area}" data-listType="CAMPAIGN">
                        <div></div>
                        <p>Campaign <i class='fa fa-arrow-right'></i></p>
                    </a>
                </div>
            </div>

            %{--Content Tab--}%
            <div class="row tab-pane" id="pane${area}Content">
                <div class="container-fluid">

                    <div id="pane${area}ListBody">
                        Please select a Media Source first.
                    </div>

                    <a tabindex="0" href="#pane${area}Type" class=" nav-bottom-links btn pull-left" id="pane${area}SpecificLeft"><i class='socialIcons fa fa-arrow-left'></i>Change Media Source</a>
                    <a tabindex="0" href="#pane${area}Details" class="nav-bottom-links btn pull-right" id="pane${area}SpecificRight">Select Content Details <i class='socialIcons fa fa-arrow-right'></i></a>

                </div>
            </div>

            %{--Details Tab--}%
            <div class="row tab-pane" id="pane${area}Details">
                <div class="container-fluid">
                    <g:if test="${sidePanel}">
                        <div class="form-group">
                            <label class="col-lg-4 control-label" for="pane${area}Header">Panel Header</label>
                            <div class="col-lg-8">
                                <g:textField class="form-control" name="pane${area}Header" value="${mediaArea?.header}" placeholder="optional title"/>
                            </div>
                        </div>
                    </g:if>

                    <div class="form-group">
                        <label class="col-lg-4 control-label" for="pane${area}Sort">Sort By</label>
                        <div class="col-lg-8">
                            <g:select class="form-control" name="pane${area}Sort" from="${sort}" optionValue="name" optionKey="value" value="${mediaArea?.sortBy}" aria-required="true"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-lg-4 control-label" for="pane${area}Order">Order</label>
                        <div class="col-lg-8">
                            <g:select class="form-control" name="pane${area}Order" from="${order}" optionValue="name" optionKey="value" value="${mediaArea?.orderBy}" aria-required="true"/>
                        </div>
                    </div>
                    <g:if test="${sidePanel}">
                        <div class="form-group">
                            <label class="col-lg-4 control-label" for="pane${area}DisplayStyle">Display Style</label>
                            <div class="col-lg-8">
                                <g:select class="form-control" name="pane${area}DisplayStyle" from="${displayStyle}" optionValue="name" value="${mediaArea?.displayStyle}" aria-required="true"/>
                            </div>
                        </div>
                    </g:if>
                    <a tabindex="0" href="#pane${area}Content" class="nav-bottom-links btn pull-left pane${area}-sortingLeft"><i class='socialIcons fa fa-arrow-left'></i> Change Content List</a>
                    <a tabindex="0" href="#pane${area}Details" class="nav-bottom-links btn pull-right pane${area}-sortingRight">View Summary <i class='socialIcons fa fa-arrow-right'></i></a>
                </div>
            </div>

            %{--Summary--}%
            <div class="row tab-pane${microSite ? ' active':''}" id="pane${area}Summary">
                <div class="container-fluid">
                    <div id="summary${area}">
                        <g:render template="/microsite/summary"/>
                    </div>
                    <a tabindex="0" href="#pane${area}Details" class="nav-bottom-links btn pull-left pane${area}-sortingLeft"><i class='socialIcons fa fa-arrow-left'></i> Change Content Details</a>
                    <a tabindex="0" href="#pane${area}Type" class="nav-bottom-links btn pull-right pane${area}-newList">Start Over <i class='socialIcons fa fa-undo'></i></a>
                </div>
            </div>
        </div> %{--end Type tab--}%
    </div><!-- end row -->
</div><!-- end builder-form-media-area col-xs-12 -->

<script>
    $(document).ready(function(){
        getSummary({
                selectorType:'${mediaArea?.selectorType}',
                selectionId:'${mediaArea?.selectionId}',
                sortBy:'${mediaArea?.sortBy}',
                orderBy:'${mediaArea?.orderBy}',
                panelHeader:'${mediaArea?.header}',
                displayStyle:'${mediaArea?.displayStyle}'
            },
            ${area}
        );
        getList({
                selectorType:'${mediaArea?.selectorType}',
                selectionId:'${mediaArea?.selectionId}',
                sortBy:'${mediaArea?.sortBy}',
                orderBy:'${mediaArea?.orderBy}',
                id:'${mediaArea?.id}',
                areaValue:'${area}'
            }
        );
    });
</script>