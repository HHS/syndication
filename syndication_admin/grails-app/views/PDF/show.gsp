<%@ page import="com.ctacorp.syndication.media.PDF" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'PDF.label', default: 'PDF')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
    <asset:javascript src="/tokenInput/jquery.tokeninput.js"/>
    <asset:stylesheet src="/tokenInput/token-input.css"/>
</head>

<body>
<div id="show-PDF" class="content scaffold-show" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <synd:message/>
    <synd:errors/>
    <synd:error/>

    <div class="row">
        <div class="col-md-10 col-md-offset-2">
            <dl class="dl-horizontal">

                <g:if test="${PDFInstance?.id}">
                    <dt id="id-label" class="word_wrap"><g:message code="pdf.id.label" default="Id"/></dt>
                    <dd class="word_wrap">${PDFInstance?.id}</dd>
                </g:if>

                <g:if test="${PDFInstance?.name}">
                    <dt id="name-label" class="word_wrap"><g:message code="PDF.name.label" default="Name"/></dt>
                    <dd class="word_wrap" aria-labelledby="name-label"><g:fieldValue bean="${PDFInstance}" field="name"/></dd>
                </g:if>

                <g:if test="${PDFInstance?.source}">
                    <dt id="source-label" class="word_wrap"><g:message code="PDF.source.label"
                                                                       default="Source"/></dt>
                    <dd class="word_wrap" aria-labelledby="source-label"><g:link controller="source" action="show"
                                                                                 id="${PDFInstance?.source?.id}">${PDFInstance?.source?.encodeAsHTML()}</g:link></dd>
                </g:if>

                <g:if test="${PDFInstance?.description}">
                    <dt id="description-label" class="word_wrap"><g:message code="PDF.description.label"
                                                                                   default="Description"/></dt>
                    <dd class="word_wrap" aria-labelledby="description-label"><g:fieldValue bean="${PDFInstance}"
                                                                                                   field="description"/></dd>
                </g:if>

                <g:if test="${PDFInstance?.sourceUrl}">
                    <dt id="sourceUrl-label" class="word_wrap"><g:message code="PDF.sourceUrl.label"
                                                                                 default="Source Url"/></dt>
                    <dd class="word_wrap" aria-labelledby="sourceUrl-label"><g:fieldValue bean="${PDFInstance}"
                                                                                                 field="sourceUrl"/></dd>
                </g:if>

                <g:if test="${PDFInstance?.active && PDFInstance?.visibleInStorefront}">
                    <dt id="storefrontLink-label" class="word_wrap"><g:message code="html.storefrontLink.label"
                                                                               default="Storefront Link"/></dt>
                    <dd class="word_wrap"><a target="_blank" href="${grails.util.Holders.config.storefront.serverAddress}/storefront/showContent/${PDFInstance?.id}">${grails.util.Holders.config.storefront.serverAddress}/storefront/showContent/${PDFInstance?.id}</a></dd>
                </g:if>

                <g:if test="${PDFInstance?.targetUrl}">
                    <dt id="targetUrl-label" class="word_wrap"><g:message code="PDF.targetUrl.label"
                                                                                 default="Target Url"/></dt>
                    <dd class="word_wrap" aria-labelledby="targetUrl-label"><g:fieldValue bean="${PDFInstance}"
                                                                                                 field="targetUrl"/></dd>
                </g:if>

                <g:if test="${PDFInstance?.customThumbnailUrl}">
                    <dt id="customThumbnailUrl-label" class="word_wrap"><g:message
                            code="PDF.customThumbnailUrl.label" default="Custom Thumbnail Url"/></dt>
                    <dd class="word_wrap" aria-labelledby="customThumbnailUrl-label"><g:fieldValue
                            bean="${PDFInstance}" field="customThumbnailUrl"/></dd>
                </g:if>

                <g:if test="${PDFInstance?.customPreviewUrl}">
                    <span id="customPreviewUrl-label" class="word_wrap"><g:message code="PDF.customPreviewUrl.label"
                                                                                        default="Custom Preview Url"/></span>
                    <span class="word_wrap" aria-labelledby="customPreviewUrl-label"><g:fieldValue
                            bean="${PDFInstance}" field="customPreviewUrl"/></span>
                </g:if>

                <g:if test="${PDFInstance?.dateSyndicationUpdated}">
                    <dt id="dateSyndicationUpdated-label" class="word_wrap"><g:message
                            code="PDF.dateSyndicationUpdated.label" default="Syndication Updated"/></dt>
                    <dd class="word_wrap" aria-labelledby="dateSyndicationUpdated-label"><g:formatDate
                            date="${PDFInstance?.dateSyndicationUpdated}"/></dd>
                </g:if>
                <g:if test="${PDFInstance?.dateSyndicationCaptured}">
                    <dt id="dateSyndicationCaptured-label" class="word_wrap"><g:message
                            code="PDF.dateSyndicationCaptured.label" default="Syndication Captured"/></dt>
                    <dd class="word_wrap" aria-labelledby="dateSyndicationCaptured-label"><g:formatDate
                            date="${PDFInstance?.dateSyndicationCaptured}"/></dd>
                </g:if>

                <g:if test="${PDFInstance?.dateSyndicationVisible}">
                    <dt id="dateSyndicationVisible-label" class="word_wrap"><g:message
                            code="PDF.dateSyndicationVisible.label" default="Syndication Visible"/></dt>
                    <dd class="word_wrap" aria-labelledby="dateSyndicationVisible-label"><g:formatDate
                            date="${PDFInstance?.dateSyndicationVisible}"/></dd>
                </g:if>

                <g:if test="${PDFInstance?.dateContentAuthored}">
                    <dt id="dateContentAuthored-label" class="word_wrap"><g:message
                            code="PDF.dateContentAuthored.label" default="Content Authored"/></dt>
                    <dd class="word_wrap" aria-labelledby="dateContentAuthored-label"><g:formatDate
                            date="${PDFInstance?.dateContentAuthored}"/></dd>
                </g:if>

                <g:if test="${PDFInstance?.dateContentUpdated}">
                    <dt id="dateContentUpdated-label" class="word_wrap"><g:message
                            code="PDF.dateContentUpdated.label" default="Content Updated"/></dt>
                    <dd class="word_wrap" aria-labelledby="dateContentUpdated-label"><g:formatDate
                            date="${PDFInstance?.dateContentUpdated}"/></dd>
                </g:if>

                <g:if test="${PDFInstance?.dateContentPublished}">
                    <dt id="dateContentPublished-label" class="word_wrap"><g:message
                            code="PDF.dateContentPublished.label" default="Content Published"/></dt>
                    <dd class="word_wrap" aria-labelledby="dateContentPublished-label"><g:formatDate
                            date="${PDFInstance?.dateContentPublished}"/></dd>
                </g:if>

                <g:if test="${PDFInstance?.dateContentReviewed}">
                    <dt id="dateContentReviewed-label" class="word_wrap"><g:message
                            code="PDF.dateContentReviewed.label" default="Content Reviewed"/></dt>
                    <dd class="word_wrap" aria-labelledby="dateContentReviewed-label"><g:formatDate
                            date="${PDFInstance?.dateContentReviewed}"/></dd>
                </g:if>

                <g:if test="${PDFInstance?.language}">
                    <dt id="language-label" class="word_wrap"><g:message code="PDF.language.label"
                                                                                default="Language"/></dt>
                    <dd class="word_wrap" aria-labelledby="language-label"><g:link controller="language"
                                                                                          action="show"
                                                                                          id="${PDFInstance?.language?.id}">${PDFInstance?.language?.encodeAsHTML()}</g:link></dd>
                </g:if>

                <dt id="active-label" class="word_wrap"><g:message code="PDF.active.label"
                                                                          default="Active"/></dt>
                <dd class="word_wrap" aria-labelledby="active-label"><g:formatBoolean
                        boolean="${PDFInstance?.active}"/></dd>

                <dt id="visibleInStorefront-label" class="word_wrap"><g:message
                        code="PDF.visibleInStorefront.label" default="Visible In Storefront"/></dt>
                <dd class="word_wrap" aria-labelledby="visibleInStorefront-label"><g:formatBoolean
                        boolean="${PDFInstance?.visibleInStorefront}"/></dd>

                <dt id="manuallyManaged-label" class="word_wrap"><g:message code="PDF.manuallyManaged.label"
                                                                                   default="Manually Managed"/></dt>
                <dd class="word_wrap" aria-labelledby="manuallyManaged-label"><g:formatBoolean
                        boolean="${PDFInstance?.manuallyManaged}"/></dd>

                <g:if test="${PDFInstance?.externalGuid}">
                    <dt id="externalGuid-label" class="word_wrap"><g:message code="PDF.externalGuid.label"
                                                                                    default="External Guid"/></dt>
                    <dd class="word_wrap" aria-labelledby="externalGuid-label"><g:fieldValue bean="${PDFInstance}"
                                                                                                    field="externalGuid"/></dd>
                </g:if>

                <g:if test="${PDFInstance?.hash}">
                    <dt id="hash-label" class="word_wrap"><g:message code="PDF.hash.label" default="Hash"/></dt>
                    <dd class="word_wrap" aria-labelledby="hash-label"><g:fieldValue bean="${PDFInstance}"
                                                                                            field="hash"/></dd>
                </g:if>

                <g:if test="${PDFInstance?.alternateImages}">
                    <dt id="alternateImages-label" class="word_wrap"><g:message code="PDF.alternateImages.label"
                                                                                       default="Alternate Images"/></dt>
                    <g:each in="${PDFInstance.alternateImages}" var="a">
                        <dd class="word_wrap" aria-labelledby="alternateImages-label"><g:link
                                controller="alternateImage" action="show" id="${a.id}">${a?.encodeAsHTML()}</g:link></dd>
                    </g:each>
                </g:if>

                <g:if test="${collections}">
                    <dt id="collections-label" class="word_wrap"><g:message code="mediaItems.collections.label" default="Collections"/></dt>
                    <g:each in="${collections}" var="collection">
                        <dd class="word_wrap"><g:link controller="mediaItem" action="show" id="${collection.id}">${collection.name}</g:link></dd>
                    </g:each>
                </g:if>

                <g:if test="${PDFInstance?.campaigns}">
                    <dt id="campaigns-label" class="word_wrap"><g:message code="PDF.campaigns.label"
                                                                                 default="Campaigns"/></dt>
                    <g:each in="${PDFInstance.campaigns}" var="c">
                        <dd class="word_wrap" aria-labelledby="campaigns-label"><g:link controller="campaign" action="show"
                                                                                               id="${c.id}">${c?.encodeAsHTML()}</g:link></dd>
                    </g:each>
                </g:if>

                <g:if test="${PDFInstance?.extendedAttributes}">
                    <dt id="extendedAttributes-label" class="word_wrap"><g:message
                            code="PDF.extendedAttributes.label" default="Extended Attributes"/></dt>
                    <g:each in="${PDFInstance.extendedAttributes}" var="e">
                        <dd class="word_wrap" aria-labelledby="extendedAttributes-label"><g:link
                                controller="extendedAttribute" action="show"
                                id="${e.id}">${e?.encodeAsHTML()}</g:link></dd>
                    </g:each>
                </g:if>

            </dl>
        </div>
    </div>

    <fieldset class="buttons">
        <g:form  url="[resource:PDFInstance, action:'edit']">
            <a href="${apiBaseUrl + '/resources/media/'+ PDFInstance?.id +'/syndicate.json'}" class="btn btn-success popup-link">Preview</a>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER, ROLE_PUBLISHER, ROLE_USER">
                <g:actionSubmit class="btn btn-warning" value="Edit" action="edit"/>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_PUBLISHER">
                <g:actionSubmit class="btn btn-danger" value="Delete" onclick="return confirm('${message(code: 'default.button.delete.mediaItem.confirm', default: 'Are you sure?')}');" action="delete"/>
            </sec:ifAnyGranted>
            <g:link class="button" action="index">
                <button type="button" class="btn">Cancel</button>
            </g:link>
            <sec:ifAnyGranted roles="ROLE_ADMIN, ROLE_MANAGER">
                <g:link controller="featuredMedia" id="${PDFInstance?.id}" action="featureItem">
                    <button type="button" class="btn btn-success pull-right">Feature this Item</button>
                </g:link>
            </sec:ifAnyGranted>
        </g:form>
    </fieldset>
    <g:render template="/mediaItem/addToYourCampaign" model="[mediaItemInstance: PDFInstance]"/>

</div>
</body>
</html>
