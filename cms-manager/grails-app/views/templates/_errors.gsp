<%--
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

--%>
<%@ page import="org.springframework.validation.FieldError" %>

<g:if test="${flash.errors}">
    %{--<div class="alert alert-error" style="display: block">${flash.errors}</div>--}%
    <div class="container-fluid">
        <div class="row"><div class="alert alert-danger alert-dismissable break-word">
        <g:each in="${flash.errors}" var="error">
            <div class="" role="alert">${error.message}</div>
        </g:each>
        </ul></div></div>
    </div>
</g:if>
<g:else>

    <g:hasErrors bean="${instance}">
        <div class="container-fluid">
            <g:eachError bean="${instance}" var="error">
                <g:if test="${!((
                        error.field == 'title' ||
                                error.field == 'subscription'
                ) && (
                        controllerName == 'emailSubscription' ||
                                controllerName == 'restSubscription'
                ))}">
                    <div class="alert alert-danger" role="alert">
                        <g:if test="${error in FieldError}">
                            <g:message error="${error}"/>
                        </g:if>
                    </div>
                </g:if>
            </g:eachError>
        </div>
    </g:hasErrors>
</g:else>
