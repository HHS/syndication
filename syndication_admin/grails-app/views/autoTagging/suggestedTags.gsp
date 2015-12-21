<%--
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
--%>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.ctacorp.syndication.Language; com.ctacorp.syndication.media.MediaItem" %>
<html>
<head>
    <title>Auto Tagging</title>
    <meta name="layout" content="dashboard"/>
    <style type="text/css">
        .top-buffer {
            margin-top: 5px;
        }

        .tag-btn{
            margin-top: 5px;
        }

        .tag-btn:hover i{
            display: inline-block;
            visibility:visible;
            padding-left: 5px;
            width:1em;
        }

        .tag-btn:hover{
            background-color: #d2322d;
            border-color: #ac2925;
        }

        i.closeButton{
            width:0px;
            display: none;
            visibility: hidden;
        }

        div.tagSingleButton{
            min-height:4em;
        }
    </style>
    <script type="application/javascript">
        $(document).ready(function(){
            $(".tag-btn").click(function(){
                var btn = $(this)
                var mediaId = btn.find("input").attr("data-mediaId")
                var tag = btn.attr("data-tagName")
                var hiddenInputName = "media_tag_"+mediaId+"_"+tag
                $("#"+hiddenInputName).remove()
                btn.remove()
            })
        })
    </script>
</head>
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Auto Tagging</h1>
        </div>
    </div>
    <g:if test='${flash.message}'>
        <div class="alert alert-info alert-dismissable">
            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
            ${flash.message}
        </div>
    </g:if>

    <div class="row">
        %{-- Select Media Language --}%
        <div class="col-sm-10">
            <g:form action="suggestedTags">
                <g:hiddenField name="lastIndex" value="0"/>
                <label for="languageId">Select Media Language: </label>
                <g:select name="languageId" from="${languages}" optionKey="id" optionValue="name" value="${language.id}"/>
                <g:submitButton name="applyLanguage" value="Apply" class="btn btn-xs btn-success"/>
            </g:form>
        </div>
        <g:if test="${untaggedMedia}">
            <div class="col-sm-2">
                %{--Tag all button--}%
                <g:form action="tagAll">
                    <g:each in="${untaggedMedia}" var="mediaItemInstance">
                        <g:each in="${suggestedTags[mediaItemInstance.id]}" var="tag">
                            <g:hiddenField name="media_tag_${mediaItemInstance.id}" id="media_tag_${mediaItemInstance.id}_${tag}" value="${tag}"/>
                        </g:each>
                    </g:each>
                    <g:hiddenField name="languageId" value="${language.id}"/>
                    <g:submitButton class="btn btn-lg btn-warning"
                                    name="tagAllButton"
                                    value="Tag All"
                                    onclick="return confirm('Are you sure? Will apply all suggested tags on this page, and may take several seconds to complete.');"/>
                </g:form>
            </div>
        </g:if>
    </div>

    <div class="row">
        <div class="col-lg-12">
            <g:if test="${untaggedMedia}">
            <div class="row">
                <div class="col-sm-1"></div>
                <div class="col-sm-4">
                    <h3>Media Item</h3>
                </div>

                <div class="col-sm-7">
                    <h3>Suggested Tags</h3>
                </div>
            </div>
            <g:each in="${untaggedMedia}" var="mediaItemInstance">
                <div class="row top-buffer">
                    <g:form action="tagSingle">
                    %{--Tag single item submit button--}%
                        <div class="col-sm-1 tagSingleButton">
                            <g:hiddenField name="firstIndex" value="${firstIndex}"/>
                            <g:hiddenField name="languageId" value="${language.id}"/>
                            <g:hiddenField name="mediaId" value="${mediaItemInstance.id}"/>
                            <g:submitButton name="submit" class="btn btn-sm btn-success" value="Tag"/>
                        </div>

                        <div class="col-sm-4">
                            ${mediaItemInstance?.id}.&nbsp;
                            <g:link controller="mediaItem" action="show" target="_blank" id="${mediaItemInstance.id}">
                                <g:fieldValue bean="${mediaItemInstance}" field="name"/>
                            </g:link>
                        </div>
                        <div class="col-sm-7">
                        %{--Suggested Tags--}%
                            <g:if test="${suggestedTags[mediaItemInstance.id]}">
                                <g:each in="${suggestedTags[mediaItemInstance.id]}" var="tag">
                                    <button type="button" class="btn btn-info btn-xs tag-btn" data-tagName="${tag}">
                                        ${tag}<i class="fa fa-times-circle closeButton"></i>
                                        <g:hiddenField name="tags" value="${tag}" data-mediaId="${mediaItemInstance.id}"/>
                                    </button>
                                </g:each>
                            </g:if>
                            <g:else>
                                <button type="button" class="btn btn-warning btn-xs">No Tag Suggestions</button>
                            </g:else>
                        </div>
                    </g:form>
                </div>
            </g:each>
            </g:if>
            <g:else>
                <h1>There are no media items without tags</h1>
            </g:else>
        </div>
    </div>
    <br/>
    <g:if test="${untaggedMedia}">
        <div class="row">
            <div class="col-md-10">
                <g:if test="${total > untaggedMedia.size()}">
                    <div class="pagination">
                        <g:paginate total="${total}" max="${max ?: 50}"/>
                    </div>
                </g:if>
            </div>
            <div class="col-md-2">
                <g:form action="tagAll">
                    <g:each in="${untaggedMedia}" var="mediaItemInstance">
                        <g:each in="${suggestedTags[mediaItemInstance.id]}" var="tag">
                            <g:hiddenField name="media_tag_${mediaItemInstance.id}" id="media_tag_${mediaItemInstance.id}_${tag}" value="${tag}"/>
                        </g:each>
                    </g:each>
                    <g:hiddenField name="languageId" value="${language.id}"/>
                    <g:submitButton class="btn btn-lg btn-warning"
                                    name="tagAllButton"
                                    value="Tag All"
                                    onclick="return confirm('Are you sure? Will apply all suggested tags on this page, and may take several seconds to complete.');"/>
                </g:form>
            </div>
        </div>
    </g:if>
    <br/>
    <br/>
    <br/>
</div>
</body>
</html>