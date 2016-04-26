<h3>Embed Code Snippet</h3>
<br/>
<sec:ifLoggedIn>
    <g:render template="/storefront/snippetControls"/>

    <br/>
    <h3>Live Snippet Preview</h3>
    <p>When you embed the above snippet code on your website, the injected content will look something like the preview below. Use the supplied checkboxes to alter the delivered content. It's important to note that the final look and feel of the content is subject to your local stylesheets.</p>

    <div id="snippetPreview">

    </div>
</sec:ifLoggedIn>
<sec:ifNotLoggedIn>
    <span>To get the embed code snippet please <g:link controller="login" action="index" params="['spring-security-redirect':request.forwardURI - request.contextPath]">Login.</g:link></span>
</sec:ifNotLoggedIn>