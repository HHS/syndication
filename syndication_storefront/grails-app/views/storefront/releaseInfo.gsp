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
                <h1><a id="Dec212015_Release_0"></a>Dec-21-2015 Release:</h1>
                <h2><a id="API_1"></a>API</h2>
                <ul>
                    <li>New Features
                        <ul>
                            <li>Allow flat listing of nested collections in /syndicate view</li>
                            <li>Preliminary support for new structured content types</li>
                            <li>Structured types now appear in /mediaTypes</li>
                            <li>You can now embed campaigns and sources (instead of just collections, and user media lists)</li>
                        </ul>
                    </li>
                    <li>Bug Fixes / Improvements
                        <ul>
                            <li>PDF now displays proper media type in API</li>
                        </ul>
                    </li>
                </ul>
                <h2><a id="Storefront_10"></a>Storefront</h2>
                <ul>
                    <li>New Features
                        <ul>
                            <li>Preview views now have a specific stylesheet which should improve consistency if displayed media</li>
                            <li>CSS Classes and IDs are now stripped by default, users can override as needed (old behavior: only styles were stripped leading to namespace collisions on some end user pages)</li>
                            <li>Initial structured content type support, sorting, searching, listing now supported for new types</li>
                        </ul>
                    </li>
                    <li>Bug Fixes / Improvements
                        <ul>
                            <li>Stylesheet improvements</li>
                            <li>Advance search has improved pagination on search results</li>
                            <li>Storefront search performance increased by 50%</li>
                        </ul>
                    </li>
                </ul>
                <h2><a id="CMS_Manager_20"></a>CMS Manager</h2>
                <ul>
                    <li>New Features
                        <ul>
                            <li>Nothing at this time</li>
                        </ul>
                    </li>
                    <li>Bug Fixes / Improvements
                        <ul>
                            <li>Nothing at this time</li>
                        </ul>
                    </li>
                </ul>
                <h2><a id="Admin_Dashboard_26"></a>Admin Dashboard</h2>
                <ul>
                    <li>New Features
                        <ul>
                            <li>Better handling of twitter rate limit error messages for users of the admin panel</li>
                            <li>Clarified several Metrics views labels and buttons</li>
                            <li>Manually created media items now default to english on the create form instead of chinese</li>
                            <li>Initial structured content support (new fields and types)</li>
                            <li>Preview views now have a specific stylesheet which should improve consistency if displayed media</li>
                        </ul>
                    </li>
                    <li>Bugs / Improvements
                        <ul>
                            <li>Create Collection views no longer require the user to add an arbitrary sourceUrl, one is automatically generated in the back end and hidden from the user.</li>
                            <li>Microsite profanity filter enhanced</li>
                            <li>When collection creation form fails, user’s previously selected media items do not disappear anymore</li>
                            <li>Video media type now more clearly states youtube import capabilities</li>
                            <li>Google analytics no longer self reports the syndication storefront mixed in with other “syndication destinations”</li>
                            <li>UI for metrics improved for large lists of sources</li>
                            <li>Improve performance of UI Metric Mapping views, fix a bug that truncated large date-range data</li>
                            <li>Better error handling for PDF creation</li>
                            <li>Improved error messages system-wide</li>
                        </ul>
                    </li>
                </ul>
                <h2><a id="Tag_Cloud_44"></a>Tag Cloud</h2>
                <ul>
                    <li>New Features
                        <ul>
                            <li>Nothing at this time</li>
                        </ul>
                    </li>
                    <li>Bug Fixes / Improvements
                        <ul>
                            <li>Nothing at this time</li>
                        </ul>
                    </li>
                </ul>
                <h2><a id="Tiny_Url_50"></a>Tiny Url</h2>
                <ul>
                    <li>New Features
                        <ul>
                            <li>Nothing at this time</li>
                        </ul>
                    </li>
                    <li>Bug Fixes / Improvements
                        <ul>
                            <li>Nothing at this time</li>
                        </ul>
                    </li>
                </ul>
                <h2><a id="Known_Issues_56"></a>Known Issues</h2>
                <ul>
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