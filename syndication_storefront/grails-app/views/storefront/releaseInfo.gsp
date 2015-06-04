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
                <h1 id="june-01-2015-release-">June-01-2015 Release:</h1>
                <h2 id="api">API</h2>
                <ul>
                    <li>New Features<ul>
                        <li>Media Viewer Support</li>
                        <li>View aggregate types as a “feed” (userMediaList, collection, tag, source, etc...)</li>
                        <li>Embed code for userMediaLists, tags, collections, etc...</li>
                    </ul>
                    </li>
                    <li>Bug Fixes / Improvements<ul>
                        <li>Some bad requests now correctly return a 400 instead of 500 error</li>
                        <li>Whitespace in thumbnails/previews removed by centering/cropping long or wide content</li>
                        <li>Collections no longer show hidden content</li>
                    </ul>
                    </li>
                </ul>
                <h2 id="storefront">Storefront</h2>
                <ul>
                    <li>New Features<ul>
                        <li>Do-it-yourself microsites are now available to publishers (beta release)</li>
                        <li>Embeddable media viewers now available for aggregate types</li>
                        <li>Media Viewers &amp; Feed view available for all aggregate types (tags, lists, etc)</li>
                        <li>FAQ (Q&amp;A), roadmap, and other static pages have been updated to be relevant</li>
                    </ul>
                    </li>
                    <li>Bug Fixes / Improvements<ul>
                        <li>Fixed an http vs https mixed mode error for previewing youtube videos in chrome</li>
                        <li>Added some missing “skip to content” page anchors for 508</li>
                        <li>Hidden media items are no longer visible in collections</li>
                    </ul>
                    </li>
                </ul>
                <h2 id="cms-manager">CMS Manager</h2>
                <ul>
                    <li>New Features<ul>
                        <li>Field error checking on rhythmyx subscriber creations</li>
                    </ul>
                    </li>
                    <li>Bug Fixes / Improvements<ul>
                        <li>User passwords no longer appear as a string of question marks in user show page</li>
                    </ul>
                    </li>
                </ul>
                <h2 id="admin-dashboard">Admin Dashboard</h2>
                <ul>
                    <li>New Features<ul>
                        <li>Dashboard timeline now shows thumbnails and links to storefront</li>
                        <li>User breakdown has been made available to all users, and includes additional breakdown information for other domains besides .gov .edu and .org.</li>
                        <li>You can now specify a list size for media metrics views</li>
                        <li>Class stripping enabled by default for media</li>
                    </ul>
                    </li>
                    <li>Bugs / Improvements<ul>
                        <li>Expanded auto-tagger exclusion wordlist</li>
                        <li>Improved query performance for certain media metric views</li>
                        <li>Better error messages for private/disabled youtube videos</li>
                        <li>Fixed a bug where the syndicate class got stripped from media items</li>
                        <li>Source/Subscriber lists are now sorted alphabetically</li>
                    </ul>
                    </li>
                </ul>
                <h2 id="tagcloud">TagCloud</h2>
                <ul>
                    <li>New Features<ul>
                        <li>Nothing significant</li>
                    </ul>
                    </li>
                    <li>Bug Fixes / Improvements<ul>
                        <li>Nothing significant</li>
                    </ul>
                    </li>
                </ul>
                <h2 id="tinyurl">TinyUrl</h2>
                <ul>
                    <li>New Features<ul>
                        <li>Nothing significant</li>
                    </ul>
                    </li>
                    <li>Bug Fixes / Improvements<ul>
                        <li>Nothing significant
                        Known Issues</li>
                    </ul>
                    </li>
                    <li>No significant issues at this time</li>
                </ul>
                %{--===================================================================--}%
            </div>
        </div>
    </div>
    <g:render template="../storefront/rightBoxes"/>
</div>
</body>
</html>