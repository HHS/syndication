%{--
Copyright (c) 2014,Â Health and Human Services - Web Communications (ASPA)
â€¨All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
--}%

<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="dashboard">
    <title>Tag Languages</title>
    <g:javascript>
        $(document).ready(function() {
            $(".on-off").click(function (e) {
                var $langItem = $(this)
                var languageId = $langItem.attr('id');
                var languageName = $("#"+languageId+"-name").html().trim();
                if($(this).hasClass("disabled")) {
                    var proceedToEnable = confirm('Are you sure you want to activate ' + languageName + '?')
                    if(proceedToEnable) {
                        location.href="${g.createLink(controller: 'tagLanguages', action: 'activateLanguage')}" + "?id="+ languageId
                    }
                }
                else {
                    var proceedToDisable = confirm('Are you sure you want to deactivate ' + languageName + '?')
                    if(proceedToDisable) {
                        location.href="${g.createLink(controller: 'tagLanguages', action: 'deactivateLanguage')}" + "?id="+ languageId
                    }
                }
            });
        });

    </g:javascript>
</head>

<body>
<div id="page-wrapper">
    <div class="row">
        <div class="col-md-12">
            <h1 class="page-header">Tag Languages</h1>
        </div>
    </div>

    <synd:message/>
    <synd:error/>
    <synd:errors/>

    <div class="row">
        <sec:ifAnyGranted roles="ROLE_ADMIN,ROLE_MANAGER">
            <div class="col-lg-8">
                <div class="row tab-pane">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <div class="row">

                                <div class="col-xs-3 col-sm-4 col-md-4"><strong>Name</strong></div>
                                <div class="col-xs-3 col-sm-3 col-md-3"><strong>Iso Code</strong></div>
                                <div class="col-xs-3 col-sm-3 col-md-3 pull-right"><strong>Active</strong></div>
                            </div>
                        </div>

                        <div class="list-group">

                            <g:each in="${tagLanguageInstanceList}" status="i" var="languageInstance">
                                <a href="#" id="${languageInstance.id}" class="on-off list-group-item <g:if test="${languageInstance.isActive}">list-group-item-success</g:if><g:else>disabled</g:else>">
                                    <div class="row">
                                        <div id="${languageInstance.id}-name" class="col-xs-3  col-sm-4 col-md-4">
                                            ${languageInstance.name}
                                        </div>
                                        <div class="col-xs-3 col-sm-3 col-md-3">
                                            ${languageInstance.isoCode}
                                        </div>
                                        <div class="col-xs-3 col-sm-3 col-md-3 pull-right">
                                            <i class="fa
                                            <g:if test="${languageInstance.isActive}">languageActive</g:if>
                                            <g:else>languageInActive</g:else>
                                            <g:if test="${languageInstance.isActive}">fa-check</g:if><g:else>fa-times</g:else>">
                                            </i>
                                        </div>
                                    </div>
                                </a>
                            </g:each>

                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-4">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h1 class="panel-title">How to select languages</h1>
                    </div>
                    <div class="panel-body">
                        <ul>
                            <li>Pick a language and click on <i class="fa fa-times languageInActive"></i> to activate.</li>
                            <li>Pick a language and click on <i class="fa fa-check languageActive"></i> to deactivate.</li>
                        </ul>
                    </div>
                </div>
            </div>
        </sec:ifAnyGranted>
        <div class="col-lg-4">
            <div class="panel panel-success">
                <div class="panel-heading">
                    <h1 class="panel-title">Enabled Languages</h1>
                </div>
                <ul class="list-group">
                    <g:each in= "${activeTagLanguageInstanceList}" status = "i" var="activeInstance">
                        <li class="list-group-item">
                            ${activeInstance.name}
                        </li>
                    </g:each>
                </ul>
            </div>
        </div>
    </div>


</div>

</body>
</html>