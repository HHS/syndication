<div class="modal fade" id="modal-header" tabindex="-1" role="form" aria-labelledby="" aria-hidden="true">
    <div class="modal-dialog modal-lg">

        <div class="modal-content">

            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                    aria-hidden="true">&times;</span></button>

            <div id="carousel-example-generic" class="carousel slide" data-ride="carousel" data-interval="false">
              

                <!-- Wrapper for slides -->
                <div class="carousel-inner">
                    <div class="item active">
                        <div tabindex="-1" id="step-one" aria-label="Step One Header"></div>
                        <h1 class="modal-title">Step One<br/><span class="modal-section-title">Header</span></h1>
                        <g:render template="../microsite/micrositeHeader"/>
                    </div>

                    <div class="item">
                        <div tabindex="-1" id="step-two" aria-label="Step Two Carousel Section"></div>
                        <h1 class="modal-title">Step Two<br/><span class="modal-section-title">Carousel Section</span></h1>
                        <div class="panel-body">
                            <g:render template="../microsite/mediaArea" model="[area:1, mediaArea: microSite?.mediaArea1]"/>
                        </div>
                    </div>

                    <div class="item">
                        <div tabindex="-1" id="step-three" aria-label="Step Three Left Column Bottom Section"></div>
                        <h1 class="modal-title">Step Three<br/><span class="modal-section-title">Left Column Bottom Section</span></h1>
                        <g:render template="../microsite/mediaArea" model="[area:2, mediaArea: microSite?.mediaArea2]"/>

                    </div>

                    <div class="item">
                        <div tabindex="-1" id="step-four" aria-label="Step Four Right column bottom section"></div>
                        <h1 class="modal-title">Step Four<br/><span class="modal-section-title">Right Column Bottom Section</span></h1>
                        <g:render template="../microsite/mediaArea" model="[area:3, mediaArea: microSite?.mediaArea3]"/>
                    </div>

                    <div class="item">
                        <div tabindex="-1" id="step-five" aria-label="Step Five Footer"></div>
                        <h1 class="modal-title">Step Five<br/><span class="modal-section-title">Footer</span></h1>
                        <g:render template="../microsite/formFooter"/>
                    </div>

                </div>

                <!-- Controls -->
                %{--<a id="left-carousel-button" class="left carousel-control" href="#carousel-example-generic" role="button" data-slide="prev">--}%
                    %{--<span class="glyphicon glyphicon-chevron-left builder-direction" aria-hidden="true"><p>previous section</p></span>--}%
                    %{--<span class="sr-only">Previous</span>--}%
                %{--</a>--}%
                %{--<a id="right-carousel-button" class="right carousel-control" href="#carousel-example-generic" role="button" data-slide="next">--}%
                    %{--<span class="glyphicon glyphicon-chevron-right builder-direction" aria-hidden="true"><p>next section</p></span>--}%
                    %{--<span class="sr-only">Next</span>--}%
                %{--</a>--}%

                <button type="button" id="modal-done-button" class="pull-right btn btn-submit" hidden data-target="#carousel-example-generic" data-dismiss="modal">
                    Done
                </button>
            </div>

        </div><!-- end modal-content -->
    </div><!-- end modal-dialog modal-lg-->
</div><!-- end modal-header -->