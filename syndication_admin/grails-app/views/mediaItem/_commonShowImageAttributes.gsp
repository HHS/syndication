<g:if test="${mediaItemInstance?.width}">
    <dt id="width-label" class="word_wrap"><g:message code="${mediaType}.width.label" default="Width"/></dt>
    <dd class="word_wrap">${mediaItemInstance.width} px</dd>
</g:if>

<g:if test="${mediaItemInstance?.height}">
    <dt id="height-label" class="word_wrap"><g:message code="${mediaType}.height.label" default="Height"/></dt>
    <dd class="word_wrap">${mediaItemInstance.height} px</dd>
</g:if>

<g:if test="${mediaItemInstance?.imageFormat}">
    <dt id="imageFormat-label" class="property-label"><g:message code="${mediaType}.imageFormat.label" default="Image Format"/></dt>
    <dd class="property-value" aria-labelledby="imageFormat-label"><g:fieldValue bean="${mediaItemInstance}" field="imageFormat"/></dd>
</g:if>

<g:if test="${mediaItemInstance?.altText}">
    <dt id="altText-label" class="word_wrap"><g:message code="${mediaType}.altText.label" default="Alt Text"/></dt>
    <dd class="word_wrap"><g:fieldValue bean="${mediaItemInstance}" field="altText"/></dd>
</g:if>