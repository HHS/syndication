<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="layout" content="microsite"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Syndication Microsite Builder</title>
    <link rel="shortcut icon" href="">

    <!--[if IE]>
    <script src="https://cdn.jsdelivr.net/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://cdn.jsdelivr.net/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
</head>

<body>

<g:render template="/microsite/topNav"/>
<g:form action="save" class="form-horizontal helps-footer">
<div class="container microsite-builder">

    <div class="row">

        <div class="microsite-builder-header col-sm-4">
            <h1>Microsite Builder<span class="beta-badge"></span></h1>

            <h2>Grid Site</h2>

            <p>You've chosen the grid template.<br/>Click on any section to get started.</p>

        </div><!-- end microsite-builder-header col-sm-4 -->

        <!-- col for spacing -->
        <div class="col-sm-1"></div>

        <!-- col for content -->
        <div class="grid-builder col-xs-6 center-block">

            <div class="header">

                <a data-toggle="modal" data-target="#modal-header" data-slide-to="0">
                    <div class="outlined">
                        <div class="section"><span>header</span>
                            <img id="check-title" hidden src="${assetPath(src: '/microsite/check.png')}" alt="This section contains content" class="checked"/>
                        </div>
                    </div>
                </a>

            </div>

            <div class="content clearfix">

                <a data-toggle="modal" data-target="#modal-header" data-slide-to="1">
                    <div class="outlined main">
                        <div class="section"><span>main content</span>
                            <img id="check-area1" hidden src="${assetPath(src: '/microsite/check.png')}" alt="This section contains content" class="checked"/>
                        </div>
                    </div>
                </a>

                <div>
                    <a data-toggle="modal" data-target="#modal-header" data-slide-to="2">
                        <div class="outlined top">
                            <div class="section"><span>sidebar<br/>one</span>
                                <img id="check-area2" hidden src="${assetPath(src: '/microsite/check.png')}" alt="This section contains content" class="checked"/>
                            </div>
                        </div>
                    <a>

                    <a data-toggle="modal" data-target="#modal-header" data-slide-to="3">
                        <div class="outlined bottom">
                            <div class="section"><span>sidebar<br>two</span>
                                <img id="check-area3" hidden src="${assetPath(src: '/microsite/check.png')}" alt="This section contains content" class="checked"/>
                            </div>
                        </div>
                    </a>

                </div>
                
            </div>

            <div class="footer">

                    <a data-toggle="modal" data-target="#modal-header" data-slide-to="4">
                        <div class="outlined">
                            <div class="section"><span>footer</span>
                                <img id="check-footer" hidden src="${assetPath(src: '/microsite/check.png')}" alt="This section contains content" class="checked"/>
                            </div>
                        </div>
                    </a>

            </div>

            <div class="builder-actions">
       
                <g:actionSubmit class="btn btn-submit" action="save" value="Save"/>
                <g:link class="btn btn-default" controller="microsite" action="microsite">Cancel</g:link>         
             
            </div>

        </div>

    </div><!-- end row -->

</div><!-- end container -->

<!-- modals -->
    <g:render template="formModal"/>

%{--</div><!-- end container microsite-builder -->--}%
    
</g:form>

<g:render template="/microsite/micrositeFooter"/>

<g:render template="../microsite/scripts"/>

</body>
</html>