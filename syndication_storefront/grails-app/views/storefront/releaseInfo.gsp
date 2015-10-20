<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 2/17/15
  Time: 2:25 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="storefront"/>
    <title>Release and Version Info</title>
</head>

<body>
<div id="home">
    <div id="home_left">
        <div id="contentListBox">
            <div class="purplebox">
                <a name="pageContent"></a>
                %{--===================================================================--}%
                <h1><a id="October192015_Release_0"></a>October-19-2015 Release:</h1>
                <h1><a id="API_2"></a>API</h1>
                <ul>
                    <li>New Features
                        <ul>
                            <li>Support for new Tweet media type</li>
                            <li>Add full suite of twitter data fields to API</li>
                            <li>Custom thumbnails &amp; previews are now cached and database-backed</li>
                            <li>Custom thumbnails and preview links now included in API results</li>
                            <li>Upgrade to Grails 2.5.1</li>
                        </ul>
                    </li>
                    <li>Bug Fixes / Improvements
                        <ul>
                            <li>Correct titles on documentation</li>
                            <li>Embedded PDFs now include content type, warning about mixed security links included</li>
                            <li>Tweet media type incorrectly displayed as “social media” - fixed</li>
                            <li>Google analytics userId fix</li>
                            <li>Better thumbnails for missing images and tweets</li>
                            <li>Update RabbitMQ Libraries</li>
                            <li>Additional Logging</li>
                            <li>Typo corrections in Swagger</li>
                        </ul>
                    </li>
                </ul>
                <h1><a id="Storefront_18"></a>Storefront</h1>
                <ul>
                    <li>New Features
                        <ul>
                            <li>Upgrade to Grails 2.5.1</li>
                        </ul>
                    </li>
                    <li>Bug Fixes / Improvements
                        <ul>
                            <li>Fixed and IE 10 &amp; 11 bug preventing embed code from displaying properly</li>
                            <li>Hidden collections are now properly hidden when building microsites</li>
                            <li>Publishers logged into storefront couldn’t change their passwords there, now they can</li>
                            <li>Correct titles on documentation</li>
                            <li>Microsites beta 508 Improvements</li>
                            <li>Organization listings are now alphabetical</li>
                        </ul>
                    </li>
                </ul>
                <h1><a id="CMS_Manager_28"></a>CMS Manager</h1>
                <ul>
                    <li>New Features
                        <ul>
                            <li>Upgrade to Grails 2.5.1</li>
                        </ul>
                    </li>
                    <li>Bug Fixes / Improvements
                        <ul>
                            <li>Better support for windows character encoding</li>
                            <li>Correct titles on documentation</li>
                        </ul>
                    </li>
                </ul>
                <h1><a id="Admin_Dashboard_34"></a>Admin Dashboard</h1>
                <ul>
                    <li>New Features
                        <ul>
                            <li>Support for Tweets (Twitter)</li>
                            <li>Additional metrics reported in admin dashboard: totals and totals graphs</li>
                            <li>Tagging is now UTF-8 aware</li>
                            <li>Upgrade to Grails 2.5.1</li>
                        </ul>
                    </li>
                    <li>Bugs / Improvements
                        <ul>
                            <li>Correct titles on documentation</li>
                            <li>Auto-tagger is now thousands of times faster due to native handling of bulk tagging</li>
                            <li>Ownership cleanup tool now supports items that are already owned</li>
                            <li>Google Analytics errors handled better - no more 500s when request is denied</li>
                            <li>Caching for metrics graph data for performance</li>
                            <li>Broken image links in docs fixed</li>
                        </ul>
                    </li>
                </ul>
                <h1><a id="Tag_Cloud_47"></a>Tag Cloud</h1>
                <ul>
                    <li>New Features
                        <ul>
                            <li>Upgrade to Grails 2.5.1</li>
                        </ul>
                    </li>
                    <li>Bug Fixes / Improvements
                        <ul>
                            <li>Correct titles on documentation</li>
                            <li>Auto-tagger is now thousands of times faster due to native handling of bulk tagging</li>
                        </ul>
                    </li>
                </ul>
                <h1><a id="Tiny_Url_53"></a>Tiny Url</h1>
                <ul>
                    <li>New Features
                        <ul>
                            <li>Upgrade to Grails 2.5.1</li>
                        </ul>
                    </li>
                    <li>Bug Fixes / Improvements
                        <ul>
                            <li>Correct titles on documentation</li>
                        </ul>
                    </li>
                </ul>
                <h1><a id="Known_Issues_58"></a>Known Issues</h1>
                <ul>
                    <li>“SocialMedia” type has been replaced with “Tweet”. All references to “SocialMedia” should be replaced</li>
                </ul>
                %{--===================================================================--}%
            </div>
        </div>
    </div>
    <g:render template="../storefront/rightBoxes"/>
</div>
</body>
</html>