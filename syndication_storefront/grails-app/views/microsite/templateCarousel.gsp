<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="layout" content="microsite"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Syndication: Microsite Carousel Template</title>
    <link rel="shortcut icon" href="">
    <asset:javascript src="jquery"/>
  <asset:javascript src="resize/jquery.resize.js"/>
  <asset:javascript src="waitForImages/jquery.waitforimages.js"/>
  <asset:javascript src="modernizr/modernizr.js"/>
  <asset:javascript src="carousel3d/jquery.carousel-3d.min.js"/>
  <asset:stylesheet src="carousel3d/jquery.carousel-3d.default.css"/>
  <asset:stylesheet src="font-awesome.min.css"/>
    <!--[if IE]>
    <script src="https://cdn.jsdelivr.net/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://cdn.jsdelivr.net/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
</head>

<body>

<g:render template="/microsite/topNav"/>

<div class="template container">    
   
       <div class="microsite carousel">

        <g:render template="/microsite/templateHeader"/>

        <div class="microsite-carousel-content row">

            <div class="carousel-microsite-carousel col-xs-12">

               <div data-carousel-3d>
                    <asset:image src="microsite/fpo.png"/>
                    <asset:image src="microsite/fpo.png"/>
                    <asset:image src="microsite/fpo.png"/>
                    <asset:image src="microsite/fpo.png"/>
                    <asset:image src="microsite/fpo.png"/>
                    <asset:image src="microsite/fpo.png"/>
                    <asset:image src="microsite/fpo.png"/>
                </div>

            </div>

            <div class="microsite-carousel-left-col col-sm-6 col-xs-12">

                <div class="microsite-article on-color">

                    <h2 class="microsite-article-title">Your highlighted Syndication content article here.</h2>

                    <asset:image src="microsite/fpo.png" class="microsite-article-pic" alt="pic"/>

                    <p>Lorem ipsum dolor sit amet, causae senserit urbanitas te vim, te mei zril mucius singulis. An est propriae quaerendum, no sed appareat rationibus. Ne novum nullam detraxit per. Fugit partem cu mel, has labore hendrerit instructior ea, aliquid moderatius ne sit.</p>

                    <p>Quot aeque quaeque ne nec. In quod dignissim instructior eum, qui at dolorem constituto. Tollit graeci quodsi eam ei, ad usu diam stet alienum. At est ferri facilis mediocrem, ex sit iusto fabulas saperet, errem populo.</p>

                    <p>Eos no magna autem graecis, iusto ubique voluptatum ea his. Pri feugiat nonumes platonem an, autem menandri in sed.</p>

                    <a>Link to Source </a>

                </div>

            </div><!-- end microsite-carousel-left-col col-sm-6 col-xs-12 -->           

            <div class="microsite-carousel-right-col col-sm-6 col-xs-12">

                <div class="microsite-article on-color">

                    <h2 class="microsite-article-title">A Syndication content list here.</h2>

                    <ul>
                        <li><a>A link to a Syndication article here</a></li>
                        <li><a>A link to a Syndication article here</a></li>
                        <li><a>A link to a Syndication article here</a></li>
                        <li><a>A link to a Syndication article here</a></li>
                        <li><a>A link to a Syndication article here</a></li>
                        <li><a>A link to a Syndication article here</a></li>
                        <li><a>A link to a Syndication article here</a></li>
                        <li><a>A link to a Syndication article here</a></li>
                        <li><a>A link to a Syndication article here</a></li>
                        <li><a>A link to a Syndication article here</a></li>
                    </ul>

                </div>

            </div><!-- end microsite-carousel-right-col col-sm-6 col-xs-12 -->

        </div><!-- end row -->

        <g:render template="/microsite/templateFooter"/>

    </div><!-- end microsite carousel -->

</div><!-- end template container -->

<script>

$(document).ready(function(){
    $('#carousel').Carousel(
        {
            itemWidth: 110,
            itemHeight: 62,
            itemMinWidth: 50,
            items: 'a',
            reflections: .5,
            rotationSpeed: 1.8
        }
    );
});

</script>
</body>
</html>
