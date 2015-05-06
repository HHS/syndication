%{--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<nav class="navbar-default navbar-static-side" role="navigation">
    <div class="sidebar-collapse">
        <ul class="nav" id="side-menu">
            <li>
                <g:link controller="dashboard" action="syndDash"><i
                        class="fa fa-dashboard fa-fw"></i> Dashboard</g:link>
            </li>

            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_MANAGER,ROLE_USER,ROLE_BASIC,ROLE_STATS,ROLE_PUBLISHER">
                <li>
                    <a href="#"><i class="fa fa-sitemap fa-fw"></i> Media<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li>
                            <g:link controller="mediaItem" action="search"><i class="fa fa-search fa-fw"></i> Search Media Items</g:link>
                        </li>
                        <li>
                            <g:link controller="audio"><i class="fa fa-microphone fa-fw"></i> Audio</g:link>
                        </li>
                        <li>
                            <g:link controller="collection"><i class="fa fa-folder-open-o fa-fw"></i> Collections</g:link>
                        </li>
                        <li>
                            <g:link controller="html"><i class="fa fa-file-text-o fa-fw"></i> Htmls</g:link>
                        </li>
                        <li>
                            <g:link controller="image"><i class="fa fa-picture-o fa-fw"></i> Images</g:link>
                        </li>
                        <li>
                            <g:link controller="infographic"><i class="fa fa-info fa-fw"></i> Infographics</g:link>
                        </li>
                        <li>
                            <g:link controller="PDF"><i class="fa fa-file-pdf-o fa-fw"></i> PDFs</g:link>
                        </li>
                        <li>
                            <g:link controller="periodical"><i class="fa fa-clock-o fa-fw"></i> Periodicals</g:link>
                        </li>
                        <li>
                            <g:link controller="socialMedia"><i class="fa fa-facebook fa-fw"></i> Social Media</g:link>
                        </li>
                        <li>
                            <g:link controller="video"><i class="fa fa-video-camera fa-fw"></i> Videos</g:link>
                        </li>
                        <li>
                            <g:link controller="widget"><i class="fa fa-gear fa-fw"></i> Widgets</g:link>
                        </li>
                    </ul>
                    <!-- /.nav-second-level -->
                </li>
            </sec:ifAnyGranted>

            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_MANAGER, ROLE_PUBLISHER">
                <li>
                    <a href="#"><i class="fa fa-tasks fa-fw"></i> Metrics<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li>
                            <g:link controller="metricReport" action="overview"><i class="fa fa-tasks fa-fw"></i> Metrics Report</g:link>
                        </li>
                        <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_MANAGER,ROLE_USER">
                            <li>
                                <g:link controller="mediaMetric"><i class="fa fa-tasks fa-fw"></i> Media Metrics</g:link>
                            </li>
                        </sec:ifAnyGranted>
                    </ul>
                    <!-- /.nav-second-level -->
                </li>
            </sec:ifAnyGranted>

            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_MANAGER,ROLE_USER,ROLE_BASIC, ROLE_PUBLISHER">
                <li>
                    <g:link controller="campaign"><i class="fa fa-flag fa-fw"></i> Campaigns</g:link>
                </li>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_MANAGER,ROLE_USER,ROLE_BASIC">
                <li>
                    <a href="#"><i class="fa fa-code-fork fa-fw"></i> Other<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_MANAGER">
                            <li>
                                <g:link controller="alternateImage"><i
                                        class="fa fa-picture-o fa-fw"></i> Alternate Images</g:link>
                            </li>
                            <li>
                                <g:link controller="language"><i class="fa fa-language fa-fw"></i> Media Languages</g:link>
                            </li>
                            <li>
                                <g:link controller="extendedAttribute"><i
                                        class="fa fa-subscript fa-fw"></i> Extended Attributes</g:link>
                            </li>
                        </sec:ifAnyGranted>
                        <li>
                            <g:link controller="source"><i class="fa fa-map-marker fa-fw"></i> Sources</g:link>
                        </li>
                    </ul>
                    <!-- /.nav-second-level -->
                </li>
            </sec:ifAnyGranted>

            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_MANAGER,ROLE_USER,ROLE_BASIC,ROLE_PUBLISHER">
                <li>
                    <a href="#"><i class="fa fa-tags fa-fw"></i> Tags<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li>
                            <g:link controller="tag" action="index"><i
                                    class="fa fa-tags fa-fw"></i> Browse Tags</g:link>
                        </li>
                        <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_MANAGER,ROLE_USER">
                            <li>
                                <g:link controller="tag" action="tagger"><i class="fa fa-tag fa-fw"></i> Bulk Tag Media</g:link>
                            </li>
                            <li>
                                <g:link controller="tagType" action="index"><i class="fa fa-arrows-alt fa-fw"></i> Tag Types</g:link>
                            </li>
                            <li>
                                <g:link controller="tagLanguages" action="index"><i class="fa fa-language fa-fw"></i> Tag Languages</g:link>
                            </li>
                        </sec:ifAnyGranted>
                        <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_MANAGER,ROLE_PUBLISHER">
                            <li>
                                <g:link controller="autoTagging"><i class="fa fa-tags fa-fw"></i> Auto-Tagging</g:link>
                            </li>
                        </sec:ifAnyGranted>
                    </ul><!-- /.nav-second-level -->
                </li>
                <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_MANAGER,ROLE_USER">
                    <li>
                        <g:link controller="featuredMedia" action="index"><i class="fa fa-star fa-fw"></i> Featured</g:link>
                    </li>
                </sec:ifAnyGranted>
            </sec:ifAnyGranted>

            <li>
                <a href="#"><i class="fa fa-wrench fa-fw"></i> Tools<span class="fa arrow"></span></a>
                <ul class="nav nav-second-level">
                    <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER">
                        <li>
                            <g:link controller="healthReport"><i class="fa fa-heart fa-fw"></i> Health Reports</g:link>
                        </li>
                        <li>
                            <g:link controller="contentIndex"><i class="fa fa-th-list fa-fw"></i> Media Indexes</g:link>
                        </li>
                    </sec:ifAnyGranted>
                    <sec:ifAnyGranted roles="ROLE_ADMIN">
                        <li>
                            <g:link controller="log"><i class="fa fa-file-text-o fa-fw"></i> Logs</g:link>
                        </li>
                        <li>
                            <g:link controller="mediaPreviewThumbnail" action="allThumbnails"><i class="fa fa-picture-o fa-fw"></i> Thumbnail Overview</g:link>
                        </li>
                    </sec:ifAnyGranted>
                    <li>
                        <g:link controller="mediaTestPreview"><i class="fa fa-eye fa-fw"></i> Media Preview & Test</g:link>
                    </li>
                </ul><!-- /.nav-second-level -->
            </li>

            <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_MANAGER,ROLE_STATS,ROLE_USER,ROLE_BASIC, ROLE_PUBLISHER">
                <li>
                    <a href="#"><i class="fa fa-users fa-fw"></i> Users<span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER">
                            <li>
                                <g:link controller="user" action="index"><i
                                        class="fa fa-users fa-fw"></i> List Users</g:link>
                            </li>
                            <li>
                                <g:link controller="user" action="breakdown"><i class="fa fa-map-marker fa-fw"></i> User Breakdown</g:link>
                            </li>
                        </sec:ifAnyGranted>
                        <li>
                            <g:link controller="user" action="editMyAccount">
                                <i class="fa fa-edit fa-fw"></i> My Account
                            </g:link>
                        </li>
                    </ul>
                    <!-- /.nav-second-level -->
                </li>
            </sec:ifAnyGranted>
    </div>
</nav>