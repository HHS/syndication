<%@ page contentType="text/html" %>
<%@ page import="grails.util.Holders; com.ctacorp.syndication.health.FlaggedMedia" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Syndication Health Report</title>
    <style>
    body {
        margin: 0px;
        padding: 0px;
        font-family: Arial;
    }

    @media (min-width: 725px) {
        .banner {
            font-size: 48px;
            padding-left: 10px;
            color: #3b3137;
        }
    }

    @media (max-width: 724px) {
        .banner {
            font-size: 33px;
            padding-left: 10px;
            color: #3b3137;
        }

        h1 {
            font-size: 1.1em;
        }
    }

    .banner span {
        display: inline;
    }

    .logo {
        display: inline;
        margin-right: 15px;
    }

    .message_body {
        padding: 0px 10px 0px 10px;
        background-color: #f9f9f9;
        border-top: 1px solid #dddddd;
        border-bottom: 1px solid #dddddd;
    }

    .footer_links {
        padding: 10px;
    }

    a:link, a:visited {
        color: #0a589f;
        text-decoration: none;
    }

    a:hover, a:active {
        color: #f89840;
        text-decoration: underline;
    }

    .footer_links a:link, .footer_links a:visited {
        color: #3b3137;
        text-decoration: none;
    }

    .footer_links a:hover, .footer_links a:active {
        color: #f89840;
    }
    </style>
</head>

<body>
<div>
    <div class="clearfix"></div>

    <div class="banner"><div class="logo"><img
            src="${grails.util.Holders.config.grails.serverURL}/assets/smallSyndicationLogo.png"/>
    </div><span>HHS Media Services</span></div>

    <div class="clearfix"></div>

    <div class="message_body">
        <h1>The following media items were identified as problematic in the Syndication System</h1>

        <p>Please verify these items, and correct as needed. You can validate the items in the <g:link
                controller="healthReport" action="index" absolute="true">Syndication Admin Dashboard</g:link></p>

        <p>Flagged Media Items:</p>
        <ul>
            <g:each in="${flaggedMediaItems}" var="flaggedMediaInstance">
                <li><strong><g:link controller="mediaItem" action="show" id="${flaggedMediaInstance.mediaItem.id}"
                                    absolute="true">${flaggedMediaInstance.mediaItem.name}</g:link></strong> (${flaggedMediaInstance.mediaItem.id})
                    <ul>
                        <li><Strong>Error:</Strong> ${flaggedMediaInstance.failureType.prettyName}</li>
                        <li><Strong>Description:</Strong> <g:message
                                code="healthReport.failure.${flaggedMediaInstance.failureType}"/></li>
                    </ul>
                </li>
            </g:each>
        </ul>
    </div>

    <div class="footer_links">
        <a href="http://www.hhs.gov" title="HHS" style="">HHS.gov</a> |
        <a href="http://www.hhs.gov/contactus.html" title="HHS Contact Us">Contact Us</a> |
        <a href="http://www.hhs.gov/Accessibility.html" title="HHS Accessibility">Accessibility</a> |
        <a href="http://www.hhs.gov/Privacy.html" title="HHS Privacy">Privacy Policy</a> |
        <a href="http://www.hhs.gov/foia/" title="HHS Privacy">FOIA</a> |
        <a href="http://www.hhs.gov/Disclaimer.html" title="HHS Disclaimers">Disclaimers</a> |
        <a href="http://oig.hhs.gov/" title="Inspector General">Inspector General</a> |
        <a href="http://www.hhs.gov/asa/eeo/no_fear_bullet_3.html" title="No FEAR Act">No FEAR Act</a> |
        <a href="http://www.hhs.gov/web/tools/plugins.html" title="Views &amp; Players">Viewers &amp; Players</a> |
        <a href="http://www.whitehouse.gov/" title="The White House">The White House</a> |
        <a href="http://www.USA.gov" title="USA.Gov">USA.gov</a>
    </div>
</div>
</body>
