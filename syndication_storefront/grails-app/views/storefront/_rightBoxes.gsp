<div id="home_right">
    <div class="sidebar_box_white">
        <h4>What is Content Syndication?</h4>

        <p class="purple">
            Want to place content from HHS websites onto your own site? Syndication allows you to offer high-quality HHS content in the look and feel of your site, without the need to manually update it.
        </p>
        <p class="all">
            <g:link controller="storefront" action="qa">Learn more >></g:link>
        </p>
    </div>

    <g:if test="${featuredMedia}">
        <div class="sidebar_box_white_bottom"></div>
        <div class="sidebar_box_white">
            <div id="topFive">
                <h4>Featured Media</h4>
                <ul>
                    <g:each in="${featuredMedia}" var="mediaItemInstance">
                        <li>
                            <g:link controller="storefront" action="showContent" id="${mediaItemInstance.id}">
                                ${mediaItemInstance.name}
                            </g:link>
                        </li>
                    </g:each>
                </ul>
            </div>
        </div>
    </g:if>

    <div class="sidebar_box_white_bottom"></div>

    <div class="sidebar_box_blue">
        <h4>Syndication API</h4>

        <p class="purple">With the HHS Syndication API, you can retrieve HHS's vast repository of public health content.</p>

        <img src="${assetPath(src: 'gear.png')}" alt="decorativeGearLogoIcon" class="float_image_right"/>

        <p class="purple">An API, or Application Programming Interface, is a way for two computer applications to talk to each other in a common language that they both understand. HHS's API provides a structured way to get HHS content in a predictable, flexible and powerful format.</p>

        <p class="all"><a href="${grailsApplication.config.syndication.swaggerAddress}">API Documentation >></a></p>
    </div>

    <div class="sidebar_box_blue_bottom"></div>

    <div class="sidebar_box_blue">
        <h4>Additional Information</h4>
        <ul>
            <li><g:link controller="workgroup" action="index">Syndication Workgroup Page</g:link></li>
            <li><g:link controller="storefront" action="qa">Questions and Answers</g:link></li>
            <li><g:link controller="storefront" action="usageGuidelines">Usage Guidelines</g:link></li>
            <li><g:link controller="storefront" action="roadMap">Syndication Roadmap</g:link></li>
            <li><g:link controller="storefront" action="reportAProblem" class="popup-form">Report a Problem with a Syndicated Page</g:link></li>
            <li><g:link controller="storefront" action="requestSyndication" class="popup-form">Request a Page to be Syndicated</g:link></li>
            <li><g:link controller="syndicateThis" action="badgeSnippet">Get the Syndicate This button</g:link></li>
            <li><a href="https://github.com/HHS/syndication">Open Source</a></li>
            <li><g:link controller="storefront" action="fiveOhEight">508 Disclaimer</g:link></li>
            <li><g:link controller="storefront" action="releaseInfo">Release Notes</g:link></li>
        </ul>
    </div>

    <div class="sidebar_box_blue_bottom"></div>
</div>