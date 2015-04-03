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
                %{--===================================================================--}%
                <p>04-06-2015 Release:</p>
                <h3 id="api">API</h3>
                <ul>
                    <li>New Features<ul>
                        <li>Thumbnail generation overhaul to improve performance and reliability</li>
                        <li>Media items can now be tagged by name during a publish</li>
                        <li>MediaItems now include google tagmanger code instead of the older image beacon for metrics tracking</li>
                        <li>Project documentation has been automated and stubs for initial documentation push have been added</li>
                    </ul>
                    </li>
                    <li>Bug Fixes / Improvements<ul>
                        <li>Additional logging for collections and subscribers</li>
                        <li>Sites with misconfigured SSL headers can be problematic for publishing, added a workaround that recovers from bad SSL config.</li>
                        <li>Footer links on the API page were outdated and have been updated</li>
                        <li>Thumbnails are now backed by in-memory cache for faster performance</li>
                        <li>Expanded automated test coverage and reporting</li>
                    </ul>
                    </li>
                </ul>
                <h3 id="storefront">Storefront</h3>
                <ul>
                    <li>New Features<ul>
                        <li>Updates to Solr Search to improve search result relevance &amp; accuracy</li>
                        <li>Project documentation has been automated and stubs for initial documentation push have been added</li>
                        <li>“dateVisibleInStorefront” is now respected by storefront to allow targeted release dates for content</li>
                        <li>Initial set of docs has been included in codebase</li>
                        <li>Added tagManager tags for metrics tracking of storefront page</li>
                    </ul>
                    </li>
                    <li>Bug Fixes / Improvements<ul>
                        <li>&lt;Strong&gt; elements now display correct font</li>
                        <li>Display of collection names adjusted for clarity</li>
                        <li>Footer links were outdated and have been updated</li>
                        <li>expanded code coverage and automated tests</li>
                        <li>admin email address updated</li>
                    </ul>
                    </li>
                </ul>
                <h3 id="cms-manager">CMS Manager</h3>
                <ul>
                    <li>New Features<ul>
                        <li>Project documentation has been automated and stubs for initial documentation push have been added</li>
                        <li>User show view displays the associated subscribers</li>
                    </ul>
                    </li>
                    <li>Bug Fixes / Improvements<ul>
                        <li>expanded automated testing</li>
                        <li>user delete now cleans up associated subscribers</li>
                    </ul>
                    </li>
                </ul>
                <h3 id="admin-dashboard">Admin Dashboard</h3>
                <ul>
                    <li>New Features<ul>
                        <li>Project documentation has been automated and stubs for initial documentation push have been added</li>
                        <li>Thumbnails can be generated/regenerated adhoc now for media items</li>
                    </ul>
                    </li>
                    <li>Bugs / Improvements<ul>
                        <li>Campaign controller had a bug preventing campaign creation in certain circumstances - fixed</li>
                    </ul>
                    </li>
                </ul>
                <h3 id="tagcloud">TagCloud</h3>
                <ul>
                    <li>Project documentation has been automated and stubs for initial documentation push have been added</li>
                    <li>Code coverage expanded for automated testing</li>
                    <li>Logging improvements</li>
                </ul>
                <h3 id="tinyurl">TinyUrl</h3>
                <ul>
                    <li>Project documentation has been automated and stubs for initial documentation push have been added</li>
                    <li>Logging improvements</li>
                    <li>Known Issues</li>
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