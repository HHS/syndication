<div class="microsite-header row">

    <div class="header-left col-xs-6">

        <h1 class="microsite-phrase">${microSite?.title ?: "HHS Microsite Service"}</h1>
                
    </div>

    <g:if test="${microSite?.logoUrl}">
        <div class="header-right col-xs-6">

            <div class=""><img class="microsite-logo" src="${microSite?.logoUrl}" alt="Custom microsite logo"/></div>

        </div>
    </g:if>


    <div class="header-right col-xs-6">
        <div class=""></div>
    </div>

</div><!-- end microsite-header  -->
