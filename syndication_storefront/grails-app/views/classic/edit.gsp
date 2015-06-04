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

<g:render template="../microsite/topNav"/>
<g:form action="update" id="${microSite?.id}" class="form-horizontal helps-footer">
    <div class="container microsite-builder">

        <div class="row">

            <div class="microsite-builder-header col-sm-4">
                <h1>Microsite Builder<span class="beta-badge"></span></h1>

                <h2>Classic Site</h2>

                <p>You're editing '${microSite?.title ?: "a none titled microsite"}'</p>

            </div><!-- end microsite-builder-header col-sm-4 -->

            <!-- col for spacing -->
            <div class="col-sm-1"></div>

            <!-- col for content -->
            <div class="classic-builder col-xs-7 center-block">

                <div class="header">

                    <a data-toggle="modal" data-target="#modal-header" data-slide-to="0">
                        <div class="outlined">
                            <div class="section"><span>header</span></div>
                        </div>
                    </a>

                </div>

                <div class="content clearfix">

                    <a data-toggle="modal" data-target="#modal-header" data-slide-to="1">
                        <div class="outlined main">
                            <div class="section"><span>main content</span></div>
                        </div>
                    </a>

                    <a data-toggle="modal" data-target="#modal-header" data-slide-to="2">
                        <div class="outlined top">
                            <div class="section"><span>sidebar<br/>one</span></div>
                        </div>
                    <a>

                    <a  data-toggle="modal" data-target="#modal-header" data-slide-to="3">
                        <div class="outlined bottom">
                            <div class="section"><span>sidebar<br>two</span></div>
                        </div>
                    </a>

                </div>

                <div class="footer">

                    <a data-toggle="modal" data-target="#modal-header" data-slide-to="4">
                        <div class="outlined">
                            <div class="section"><span>footer</span></div>
                        </div>
                    </a>

                </div>

                <div class="builder-actions">

                    <g:actionSubmit class="btn btn-submit" action="update" value="Update"/>
                    <g:link class="btn btn-default" controller="microsite" action="index">Cancel</g:link>
                    
                </div>

            </div><!-- classic-builder col-xs-7 center-block -->

            </div><!-- end row -->

    </div><!-- end container  -->
<!-- modals -->

<!-- modal for header (with gallery) -->

    <g:render template="formModal"/>


%{--</div><!-- end container microsite-builder -->--}%
    
</g:form>

<g:render template="/microsite/micrositeFooter"/>

<g:render template="../microsite/scripts"/>
</body>
</html>