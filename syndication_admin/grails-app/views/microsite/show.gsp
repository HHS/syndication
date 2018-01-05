<%--
  Created by IntelliJ IDEA.
  User: nburk
  Date: 12/1/15
  Time: 2:53 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'microsite.label', default: 'Microsite')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
    <div id="show-microsite" class="container-fluid" role="main">
        <h1><g:message code="default.show.label" args="[entityName]"/></h1>
        <synd:message/>
        <synd:errors/>
        <synd:hasError/>
        <div class="row">
            <div class="col-md-10 col-md-offset-2">
                <dl class="dl-horizontal">
                    <g:if test="${micrositeInstance?.user?.name}">
                        <dt id="user-name-label" class="word_wrap"><g:message code="microsite.user.name.label" default="Owner Name"/></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${micrositeInstance}" field="user.name"/></dd>
                    </g:if>
                    <g:if test="${micrositeInstance?.user?.username}">
                        <dt id="user-username-label" class="word_wrap"><g:message code="microsite.user.username.label" default="Owner Email"/></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${micrositeInstance}" field="user.username"/></dd>
                    </g:if>
                    <g:if test="${micrositeInstance?.id}">
                        <dt id="id-label" class="word_wrap"><g:message code="html.id.label" default="Id"/></dt>
                        <dd class="word_wrap">${micrositeInstance.id}</dd>
                    </g:if>

                    <g:if test="${micrositeInstance?.title}">
                        <dt id="title-label" class="word_wrap"><g:message code="microsite.title.label" default="Title"/></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${micrositeInstance}" field="title"/></dd>
                    </g:if>
                    <g:if test="${micrositeInstance?.logoUrl}">
                        <dt id="logoUrl-label" class="word_wrap"><g:message code="microsite.logoUrl.label" default="Logo Url"/></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${micrositeInstance}" field="logoUrl"/></dd>
                    </g:if>
                    <g:if test="${micrositeInstance?.footerText}">
                        <dt id="footerText-label" class="word_wrap"><g:message code="microsite.footerText.label" default="Footer Text"/></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${micrositeInstance}" field="footerText"/></dd>
                    </g:if>
                    <g:if test="${micrositeInstance?.footerLinkName1}">
                        <dt id="footerLinkName1-label" class="word_wrap"><g:message code="microsite.footerLinkName1.label" default="FooterLinkName1"/></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${micrositeInstance}" field="footerLinkName1"/></dd>
                    </g:if>
                    <g:if test="${micrositeInstance?.footerLink1}">
                        <dt id="footerLink1-label" class="word_wrap"><g:message code="microsite.footerLink1.label" default="FooterLink1"/></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${micrositeInstance}" field="footerLink1"/></dd>
                    </g:if>
                    <g:if test="${micrositeInstance?.footerLinkName2}">
                        <dt id="footerLinkName2-label" class="word_wrap"><g:message code="microsite.footerLinkName2.label" default="FooterLinkName2"/></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${micrositeInstance}" field="footerLinkName1"/></dd>
                    </g:if>
                    <g:if test="${micrositeInstance?.footerLink2}">
                        <dt id="footerLink2-label" class="word_wrap"><g:message code="microsite.footerLink2.label" default="FooterLink2"/></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${micrositeInstance}" field="footerLink2"/></dd>
                    </g:if>
                    <g:if test="${micrositeInstance?.footerLinkName3}">
                        <dt id="footerLinkName3-label" class="word_wrap"><g:message code="microsite.footerLinkName3.label" default="FooterLinkName3"/></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${micrositeInstance}" field="footerLinkName1"/></dd>
                    </g:if>
                    <g:if test="${micrositeInstance?.footerLink3}">
                        <dt id="footerLink3-label" class="word_wrap"><g:message code="microsite.footerLink3.label" default="FooterLink3"/></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${micrositeInstance}" field="footerLink3"/></dd>
                    </g:if>
                    <g:if test="${micrositeInstance?.footerLinkName4}">
                        <dt id="footerLinkName4-label" class="word_wrap"><g:message code="microsite.footerLinkName4.label" default="FooterLinkName4"/></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${micrositeInstance}" field="footerLinkName4"/></dd>
                    </g:if>
                    <g:if test="${micrositeInstance?.footerLink4}">
                        <dt id="footerLink4-label" class="word_wrap"><g:message code="microsite.footerLink4.label" default="FooterLink4"/></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${micrositeInstance}" field="footerLink4"/></dd>
                    </g:if>


                    <g:if test="${micrositeInstance?.mediaArea1?.header}">
                        <dt id="mediaArea1-header-label" class="word_wrap"><g:message code="microsite.mediaArea1.header.label" default="Media Area 1 Header"/></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${micrositeInstance}" field="mediaArea1.header"/></dd>
                    </g:if>
                    <g:if test="${micrositeInstance?.mediaArea1?.description}">
                        <dt id="mediaArea1-description-label" class="word_wrap"><g:message code="microsite.mediaArea1.description.label" default="Media Area 1 Description"/></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${micrositeInstance}" field="mediaArea1.description"/></dd>
                    </g:if>
                    <g:if test="${micrositeInstance?.mediaArea2?.header}">
                        <dt id="mediaArea2-header-label" class="word_wrap"><g:message code="microsite.mediaArea2.header.label" default="Media Area 2 Header"/></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${micrositeInstance}" field="mediaArea2.header"/></dd>
                    </g:if>
                    <g:if test="${micrositeInstance?.mediaArea2?.description}">
                        <dt id="mediaArea2-description-label" class="word_wrap"><g:message code="microsite.mediaArea2.description.label" default="Media Area 2 Description"/></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${micrositeInstance}" field="mediaArea2.description"/></dd>
                    </g:if>
                    <g:if test="${micrositeInstance?.mediaArea3?.header}">
                        <dt id="mediaArea3-header-label" class="word_wrap"><g:message code="microsite.mediaArea3.header.label" default="Media Area 3 Header"/></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${micrositeInstance}" field="mediaArea3.header"/></dd>
                    </g:if>
                    <g:if test="${micrositeInstance?.mediaArea3?.description}">
                        <dt id="mediaArea3-description-label" class="word_wrap"><g:message code="microsite.mediaArea3.description.label" default="Media Area 3 Description"/></dt>
                        <dd class="word_wrap"><g:fieldValue bean="${micrositeInstance}" field="mediaArea3.description"/></dd>
                    </g:if>

                </dl>
            </div>
        </div>
        <fieldset class="buttons">
            <g:form action="edit" id="${micrositeInstance.id}" controller="microsite">
                %{--<sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER">--}%
                    %{--<g:actionSubmit class="btn btn-warning" value="Edit" action="edit"/>--}%
                %{--</sec:ifAnyGranted>--}%
                <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PUBLISHER">
                    <g:actionSubmit class="btn btn-danger" action="delete" onclick="return confirm('${message(code: 'default.button.delete.mediaItem.confirm', default: 'Are you sure?')}');" value="Delete"/>
                </sec:ifAnyGranted>
                <g:link class="button" controller="micrositeFilter" action="index">
                    <button type="button" class="btn">Cancel</button>
                </g:link>
            </g:form>
        </fieldset>
    </div>
</body>
</html>