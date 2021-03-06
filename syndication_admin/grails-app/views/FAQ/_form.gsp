%{--
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--}%

<%@ page import="com.ctacorp.syndication.media.FAQ" %>

<g:render template="/mediaItem/globalForm" model="[mediaItemInstance:faq]"/>

<!-- Select Multiple -->
<div class="form-group">
    <label class="col-md-4 control-label" for="questionAndAnswers">Question and Answers</label>
    <div class="col-md-8">
        <select multiple="multiple" id="questionAndAnswers" name="questionAndAnswers">
            <g:each in="${questionAndAnswerList}" var="qAndA">
                <g:if test="${qAndA.id in faq?.questionAndAnswers*.id}">
                    <option value="${qAndA.id}" selected>[${qAndA.id}] ${qAndA.name}</option>
                </g:if>
                <g:else>
                    <option value="${qAndA.id}">[${qAndA.id}] ${qAndA.name}</option>
                </g:else>
            </g:each>
        </select>
    </div>
</div>
