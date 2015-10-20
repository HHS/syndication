<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">

            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>

        </div>


        <div class="collapse navbar-collapse">

            <ul class="nav navbar-nav">

                <g:if test="${params.controller.equals("microsite")}">

                    <a href="#pageContent" class="skip-in-page-top-nav">Skip to Main Content</a>
                    <li><g:link controller="microsite" action="microsite"
                                class="microsite-brand ${params.action == "microsite" ? " active" : ""}"><i
                                class="fa fa-wrench"></i> Microsite Builder</g:link></li>
                    <li><g:link controller="microsite" action="templateClassic"
                                class="${params.action == "templateClassic" ? " active" : ""}">Classic Example</g:link></li>
                    <li><g:link controller="microsite" action="templateBlog"
                                class="${params.action == "templateBlog" ? " active" : ""}">Blog Example</g:link></li>
                    <li><g:link controller="microsite" action="templateCarousel"
                                class="${params.action == "templateCarousel" ? " active" : ""}">Carousel Example</g:link></li>
                    <li><g:link controller="microsite" action="templateGrid"
                                class="${params.action == "templateGrid" ? " active" : ""}">Grid Example</g:link></li>

                    <li class="last"><g:link controller="microsite" action="index"
                    class="back-user-ms-list${params.action == "templateGrid" ? " active" : ""}"><i
                    class="fa fa-sitemap"></i> My Microsites</g:link></li>

                </g:if>
                <g:else>

                    <a href="#pageContent" class="skip-in-page-top-nav">Skip to Main Content</a>

                    <li><g:link controller="microsite" action="microsite"
                                class="microsite-brand  ${params.action == "microsite" ? " active" : ""}"><i
                                class="fa fa-wrench"></i> Microsite Template Builder</g:link></li>

                    <li class="last"><g:link controller="microsite" action="index"
                    class="back-user-ms-list${params.action == "templateGrid" ? " active" : ""}"><i
                    class="fa fa-sitemap"></i> My Microsites</g:link></li>

                </g:else>

            </ul>

            

        </div><!--.nav-collapse -->
    </div>
</nav>