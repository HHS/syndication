<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Microsite Flag</title>
    <style>
    body{
        margin:0px;
        padding:0px;
        font-family:Arial;
    }

    .banner{
        font-size:48px;
        padding-left:10px;
        color:#3b3137;
    }

    .banner span{
        display: inline;
    }

    .logo{
        display: inline;
        margin-right:15px;
    }

    .message_body{
        padding:0px 10px 0px 10px;
        background-color:#f9f9f9;
        border-top:1px solid #dddddd;
        border-bottom:1px solid #dddddd;
    }

    .footer_links{
        padding:10px;
    }

    a:link, a:visited{
        color:#0a589f;
        text-decoration: none;
    }

    a:hover, a:active{
        color:#f89840;
        text-decoration: underline;
    }

    .footer_links a:link, .footer_links a:visited{
        color:#3b3137;
        text-decoration: none;
    }

    .footer_links a:hover, .footer_links a:active{
        color:#f89840;
    }
    </style>
</head>
<body>
<div>
    <div class="clearfix"></div>
    <div class="banner"><div class="logo"><asset:image absolute="true" alt="syndication logo" src="smallSyndicationLogo.png"/></div><span>HHS Media Services</span></div>
    <div class="clearfix"></div>
    <div class="message_body">
        <p>The following user has a flagged microsite.</p>
        <ul>
            <li>name: ${userInstance?.name}</li>
            <li>username: ${userInstance?.username}</li>
            <li>micrositeId: ${microsite?.id}</li>
            <li>microsite title: ${microsite?.title ?: "No Title"}</li>
            <li>Microsite show page: <a href="${serverUrl+"/microsite/show/"+ microsite?.id}" title="flagged microsite show page">Admins show page</a></li>
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
