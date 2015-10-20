<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="layout" content="microsite"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <meta name="description" content=""/>
  <meta name="author" content=""/>
  <title>Syndication Style Guide by CTAC : Microsite</title>
  <link rel="shortcut icon" href="">

  <!--[if IE]>
    <script src="https://cdn.jsdelivr.net/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://cdn.jsdelivr.net/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
</head>

<body>

<g:render template="/microsite/topNav"/>
<g:form action="update" id="${microSite?.id}" name="micrositeForm" method="post" class="form-horizontal helps-footer">
  <div class="container microsite-builder">

    <div class="row">

      <div class="microsite-builder-header col-sm-4">
        <a id="pageContent"></a>
        <h1>Microsite Builder<span class="beta-badge"><asset:image src="microsite/beta-badge.png" alt="Currently in beta"/></span></h1>

        <h2>Blog Site</h2>

        <p>You're editing '${microSite?.title ?: "a none titled microsite"}'</p>

          <g:render template="../microsite/formDirections"/>

      </div>

      <!-- col for spacing -->
      <div class="col-sm-1"></div>

      <!-- col for content -->
      <div class="blog-builder col-sm-7 center-block">

          <div class="header">

              <a id="head-modal" tabIndex="-1" data-toggle="modal" data-target="#modal-header" data-slide-to="0"></a>
              <div onkeypress="if(event.keyCode==13){$('#head-modal').click();return false;}" class="outlined" data-toggle="modal" data-target="#modal-header" data-slide-to="0" tabindex="0" role="link">
                  <div class="section"><span>header</span></div>
              </div>

          </div>

          <div class="content clearfix">

              <a id="main-modal" tabindex="-1" data-toggle="modal" data-target="#modal-header" data-slide-to="1"></a>
              <div onkeypress="if(event.keyCode==13){$('#main-modal').click();return false;}" class="outlined main" tabindex="0" data-toggle="modal" data-target="#modal-header" data-slide-to="1" role="link">
                  <div class="section"><span>main content</span></div>
              </div>

              <a id="right-top-modal" tabIndex="-1" data-toggle="modal" data-target="#modal-header" data-slide-to="2"></a>
              <div onkeypress="if(event.keyCode==13){$('#right-top-modal').click();return false;}" tabindex="0" class="outlined top" data-toggle="modal" data-target="#modal-header" data-slide-to="2" role="link">
                  <div class="section"><span>sidebar<br/>one</span></div>
              </div>

              <a id="right-bottom-modal" tabIndex="-1" data-toggle="modal" data-target="#modal-header" data-slide-to="3"></a>
              <div onkeypress="if(event.keyCode==13){$('#right-bottom-modal').click();return false;}" tabindex="0" class="outlined bottom" data-toggle="modal" data-target="#modal-header" data-slide-to="3" role="link">
                  <div class="section"><span>sidebar<br>two</span></div>
              </div>

          </div>

          <div class="footer">

              <a id="foot-modal" tabIndex="-1" data-toggle="modal" data-target="#modal-header" data-slide-to="4"></a>
              <div onkeypress="if(event.keyCode==13){$('#foot-modal').click();return false;}" tabindex="0" class="outlined" data-toggle="modal" data-target="#modal-header" data-slide-to="4" role="link">
                  <div class="section"><span>footer</span></div>
              </div>

          </div>

          <div class="builder-actions">

            <input type="button" class="btn btn-submit" onclick="submitClick()" value="Update"/>
            <g:link class="btn btn-default" controller="microsite" action="index">Cancel</g:link>

          </div>

      </div>

  </div><!-- end row -->

</div><!-- end container -->
      

<g:render template="formModal"/>

%{--</div><!-- end container microsite-builder -->--}%
  
</g:form>

<g:render template="/microsite/micrositeFooter"/>

<g:render template="../microsite/scripts"/>

</body>
</html>