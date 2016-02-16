<g:if test="${thumbnailGeneration == true || previewGeneration == true}">
    <g:img style="height:100%;width:100%;" uri="${grailsApplication.config.grails.serverURL}/assets/defaultIcons/thumbnail/questionAndAnswer.jpg"/>
</g:if>
<g:else>
    <div style="margin-bottom: 1em; font-family: sans-serif;">
        <div><strong>${questionAndAnswer.name}</strong></div>
        <div style="font-style: italic; margin-left: 1.5em;">${questionAndAnswer.answer}</div>
    </div>
</g:else>