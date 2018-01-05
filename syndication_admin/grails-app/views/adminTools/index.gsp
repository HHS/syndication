%{--
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<%--
  Created by IntelliJ IDEA.
  User: sgates
  Date: 10/30/15
  Time: 11:23 AM
--%>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ctacorp.syndication.media.Html" %>
<html>
<head>
    <meta name="layout" content="dashboard">
    <title>Admin Tools</title>
</head>

<body>
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Admin Tools</h1>
        </div><!-- /.col-lg-12 -->
    </div><!-- /.row -->

    <synd:message/>
    <synd:errors/>
    <synd:hasError/>

    <div class="row">
        <div class="col-md-6">
            %{--UPDATE MISSING TINY URLS ------------------------------------------------------------------------------------}%
            <div class="row">
                <div class="col-lg-12">
                    <h3>TinyUrls</h3>
                    <g:form action="updateMissingTinyUrls">
                        <g:submitButton class="btn btn-primary" name="updateMissingTinyUrls" value="Update Missing TinyUrls"/>
                    </g:form>
                </div>
            </div>
            <hr/>
            %{--RESET CONTENT HASHES ----------------------------------------------------------------------------------------}%
            <div class="row">
                <div class="col-lg-12">
                    <h3>Content Hashes</h3>
                    <g:form action="resetAllHashes">
                        <div class="check-box">
                            <label>
                                <g:checkBox name="restrictToDomain"/>   Restrict to Domain
                            </label>
                        </div>
                        <div class="form-group">
                            <lable for="domain">URL:</lable>
                            <g:textField class="form-control" name="domain"/>
                        </div>
                        <div class="form-group">
                            <g:submitButton class="btn btn-primary" name="resetAllHashes" value="Reset all content hashes"/>
                        </div>
                    </g:form>
                </div>
            </div>
            <hr/>
            %{--Download Database as JSON ------------------------------------------------------------------------------------}%
            <div class="row">
                <div class="col-lg-12">
                    <h3>Database Dump</h3>
                    <g:form action="downloadDatabaseAsJsonFile">
                        <div class="check-box">
                            <label>
                                <g:checkBox name="pretty" checked="true"/> Pretty Json?
                            </label>
                        </div>
                        <g:submitButton class="btn btn-primary" name="downloadData" value="Download Datadump"/>

                    </g:form>
                </div>
            </div>
            <hr/>
            %{--TWEET INSPECTOR ---------------------------------------------------------------------------------------------}%
            <div class="row">
                <div class="col-lg-12">
                    <h3>Tweet Inspector</h3>
                    <g:form action="inspectTweet">
                        <div class="form-group">
                            <label for="tweetId">Tweet ID</label>
                            <g:textField class="form-control" name="tweetId" value="${tweetId}"/>
                        </div>

                        <g:submitButton class="btn btn-primary" name="submit" value="Lookup"/>
                    </g:form>
                    <g:if test="${tweetData}">
                        <h4>Data for tweet id: ${tweetData.id}</h4>
                        <div>
                            <ul>
                                <g:each in="${tweetData.metaClass.properties}" var="prop">
                                    <g:if test="${prop.name == "user" || prop.name.contains('Entities')}">
                                        <li>
                                            <strong>${prop.name}</strong>
                                            <ul>
                                                <g:each in="${tweetData."${prop.name}"}" var="nestedProp" status="i">
                                                    <li>
                                                        <strong>${i}</strong>
                                                        <ul>
                                                            <g:each in="${nestedProp.metaClass.properties}" var="doubleNestedProp">
                                                                <g:if test="${doubleNestedProp.name == "videoVariants"}">
                                                                    <li><strong>videoVariants</strong>
                                                                        <ul>
                                                                            <g:each in="${nestedProp.videoVariants}" var="videoVariant" status="vi">
                                                                                <li><strong>${vi}</strong>
                                                                                    <ul>
                                                                                        <g:each in="${videoVariant.metaClass.properties}" var="videoProp">
                                                                                            <li><strong>${videoProp.name}</strong>: ${videoVariant."${videoProp.name}"}</li>
                                                                                        </g:each>
                                                                                    </ul>
                                                                                </li>
                                                                            </g:each>
                                                                        </ul>
                                                                    </li>
                                                                </g:if>
                                                                <g:else>
                                                                    <li><strong>${doubleNestedProp.name}</strong>: ${nestedProp."${doubleNestedProp.name}"}</li>
                                                                </g:else>
                                                            </g:each>
                                                        </ul>
                                                    </li>
                                                </g:each>
                                            </ul>
                                        </li>
                                    </g:if>
                                    <g:else>
                                        <li><strong>${prop.name}</strong>: ${tweetData."${prop.name}"}</li>
                                    </g:else>
                                </g:each>
                            </ul>
                        </div>
                    </g:if>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            %{--Content Associations ---------------------------------------------------------------------------------------------}%
            <div class="row">
                <div class="col-lg-12">
                    <h3>Content Associations</h3>
                    <g:link controller="ownershipCleanup" action="index" class="btn btn-warning">Bulk Media Ownership</g:link>
                </div>
            </div>
            <hr/>
            %{--Duplicate Finder ---------------------------------------------------------------------------------------------}%
            <div class="row">
                <div class="col-lg-12">
                    <h3>Duplicate Finder</h3>
                    <g:link action="duplicateFinder" class="btn btn-primary">Find Duplicates</g:link>
                </div>
            </div>
            <hr/>
            %{--Cache Management ---------------------------------------------------------------------------------------------}%
            <div class="row">
                <div class="col-lg-12">
                    <h3>Cache Flusher</h3>
                    <g:link action="flushAllCaches" class="btn btn-primary">Flush All Caches</g:link>
                </div>
            </div>
            %{--Content Cache Management ---------------------------------------------------------------------------------------------}%
            <div class="row">
                <div class="col-lg-12">
                    <h3>Content Cache Flusher</h3>
                    <g:link action="flushAllContentCaches" class="btn btn-primary">Flush Content Caches</g:link>
                </div>
            </div>
            %{--update source url md5 hash ------------------------------------------------------------------------------------}%
            <div class="row">
                <div class="col-lg-12">
                    <h3>Source Url Hashing</h3>
                    <g:form action="updateSourceUrlHash">
                        <g:submitButton class="btn btn-primary" name="sourceUrlmd5" value="update sourceUrl md5 hash"/>
                    </g:form>
                </div>
            </div>
            %{--Add initial last login ------------------------------------------------------------------------------------}%
            <div class="row">
                <div class="col-lg-12">
                    <h3>Add last-login date to users without one</h3>
                    <g:form action="addLoginDateToAllUsers">
                        <g:submitButton class="btn btn-primary" name="addLastLogin" value="Add Date"/>
                    </g:form>
                </div>
            </div>
            %{--Mass Message users ------------------------------------------------------------------------------------}%
            <div class="row">
                <div class="col-lg-12">
                    <h3>Mass Email Users</h3>
                    <g:form action="massMessage">
                        <g:submitButton class="btn btn-primary" name="massMessage" value="Send a Mass email to users"/>
                    </g:form>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
</html>
