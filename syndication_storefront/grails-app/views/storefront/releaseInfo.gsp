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
                <h2 id="05-04-2015-release-">05-04-2015 Release:</h2>
                <h3 id="api">API</h3>
                <ul>
                    <li>New Features<ul>
                        <li>Added keyTest controller to allow outside parties to verify if their keys are valid or not</li>
                        <li>Thumbnail generation has been completely overhauled, and has a robust backend to allow the ad-hoc regeneration of thumbnails (for admin users)</li>
                        <li>Support for new PDF media type</li>
                        <li>Updated grails to version 2.4.5</li>
                    </ul>
                    </li>
                    <li>Bug Fixes / Improvements<ul>
                        <li>JSONP wasn’t working for some limited endpoints, this has been corrected</li>
                        <li>Better error messages for users that are unauthorized to publish content</li>
                    </ul>
                    </li>
                </ul>
                <h3 id="storefront">Storefront</h3>
                <ul>
                    <li>New Features<ul>
                        <li>Updated grails to version 2.4.5</li>
                        <li>Documentation has been expanded and bundled with the source code</li>
                        <li>Added support for new PDF type</li>
                    </ul>
                    </li>
                    <li>Bug Fixes / Improvements</li>
                </ul>
                <h3 id="cms-manager">CMS Manager</h3>
                <ul>
                    <li>New Features<ul>
                        <li>Updated grails to version 2.4.5</li>
                    </ul>
                    </li>
                    <li>Bug Fixes / Improvements<ul>
                        <li>Fixed a character encoding issue with flash notifications for subscribers</li>
                    </ul>
                    </li>
                </ul>
                <h3 id="admin-dashboard">Admin Dashboard</h3>
                <ul>
                    <li>New Features<ul>
                        <li>Updated grails to version 2.4.5</li>
                        <li>Admin GUI for managing missing thumbnails has been added.</li>
                        <li>Thumbnails generation subsystem has undergone a complete rewrite</li>
                        <li>Auto-Tagger is a little smarter than before</li>
                        <li>New user breakdown view for admins allows seeing account distribution by domain</li>
                        <li>Support for new PDF type</li>
                    </ul>
                    </li>
                    <li>Bugs / Improvements<ul>
                        <li>Date Range for metrics displayed some erroneous values, this has been corrected</li>
                        <li>Youtube links in the .com/embed/&lt;some id&gt; format are now supported</li>
                    </ul>
                    </li>
                </ul>
                <h3 id="tagcloud">TagCloud</h3>
                <ul>
                    <li>New Features<ul>
                        <li>Updated grails to version 2.4.5</li>
                        <li>Documentation has been expanded and bundled with the source code</li>
                    </ul>
                    </li>
                    <li>Bug Fixes / Improvements</li>
                </ul>
                <h3 id="tinyurl">TinyUrl</h3>
                <ul>
                    <li>New Features<ul>
                        <li>Updated grails to version 2.4.5</li>
                        <li>Documentation has been expanded and bundled with the source code</li>
                    </ul>
                    </li>
                    <li>Bug Fixes / Improvements<ul>
                        <li>Known Issues</li>
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