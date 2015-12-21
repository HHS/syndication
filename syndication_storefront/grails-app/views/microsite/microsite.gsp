<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="layout" content="microsite"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Syndication Style Guide: Microsite</title>
    <link rel="shortcut icon" href="">
     <asset:stylesheet src="font-awesome.min.css"/>
    <!--[if IE]>
    <script src="https://cdn.jsdelivr.net/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://cdn.jsdelivr.net/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
</head>

<body>
<g:render template="/microsite/topNav"/>

<div class="landing container">

    <div class="heading">
        <a id="pageContent"></a>
        <h1>Microsite Builder<span class="beta-badge"><asset:image src="microsite/beta-badge.png" alt="Currently in beta"/></span></h1>

        <p class="instructions">Build your own personalized site populated with rich media from the HHS Syndication Network.<br/> Choose a template below to start.
        </p>
    </div>

    <div class="template-samples row">

        <div class="pick col-sm-3">
            <p>Classic</p>
            <a id="pick-classic" class="template-thumb" href="${createLink(action:"create", controller: "classic")}">Classic Template: A basic way of displaying easily accessed content.</a>

            <div class="hide-description"><p>A traditional layout with a modern accordion style media view.</p></div>
        </div>

        <div class="pick col-sm-3">
            <p>Blog</p>
            <a id="pick-blog" class="template-thumb" href="${createLink(action:"create", controller: "blog")}">Blog Template: A recognizable display of aggragated content.</a>

            <div class="hide-description"><p>An endless-list style media view with right side call-out boxes.</p></div>
        </div>

        <div class="pick col-sm-3">
            <p>Carousel</p>
            <a id="pick-carousel" class="template-thumb" href="${createLink(action:"create", controller: "carousel")}">Carousel Template: Shows images in an animated gallery.</a>

            <div class="hide-description"><p>A flashy image carousel with a dual column endless-list layout.</p></div>
        </div>

        <div class="pick col-sm-3">
            <p>Grid</p>
            <a id="pick-grid" class="template-thumb" href="${createLink(action:"create", controller: "grid")}">Grid Template: See content in a tiled or paneled format.</a>

            <div class="hide-description"><p>Media is presented in a multi-column social media reader format.</p></div>
        </div>

    </div><!-- end of container -->

</div>

<g:render template="micrositeFooter"/>

</body>
</html>
