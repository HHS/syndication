<g:if test="${extractedContent}">
    <div class="row">
        <div class="col-lg-8 col-lg-offset-2">
            <ul class="nav nav-pills" role="tablist">
                <li role="presentation" class="active"><a href="#previewTab" role="tab" data-toggle="tab">Browser Preview</a></li>
                <li role="presentation"><a href="#previewMobileTab" role="tab" data-toggle="tab">Mobile Preview</a></li>
                <li role="presentation"><a href="#sourceTab" role="tab" data-toggle="tab">Raw Source</a></li>
            </ul>
            <br/>
            <div class="tab-content">
                <div class="row tab-pane active" id="previewTab">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h1 class="panel-title">Extracted Content</h1>
                        </div>
                        <div class="panel-body">
                            ${extractedContent.encodeAsRaw()}
                        </div>
                    </div>
                </div>
                <div class="row tab-pane" id="previewMobileTab">
                    <div class="alert alert-info">Previews are constrained to mobile phone resolution in order of popularity.
                    5 pixel padding has been added to all previews, and images are constrained to 100% maximum to prevent cropping.
                    Exact look & feel will depend on your style sheets.</div>
                    <h3>320x568</h3>
                    <div class="mobile-preview mobile-size-320x568">${extractedContent.encodeAsRaw()}</div>
                    <h3>768x1024</h3>
                    <div class="mobile-preview mobile-size-768x1024">${extractedContent.encodeAsRaw()}</div>
                    <h3>320x480</h3>
                    <div class="mobile-preview mobile-size-320x480">${extractedContent.encodeAsRaw()}</div>
                    <h3>360x640</h3>
                    <div class="mobile-preview mobile-size-360x640">${extractedContent.encodeAsRaw()}</div>
                </div>
                <div class="row tab-pane" id="sourceTab">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <h1 class="panel-title">Extracted Content</h1>
                        </div>
                        <div class="panel-body">
                            <pre class="prettyprint linenums">
                                <code class="lang-html">
                                    ${extractedContent}
                                </code>
                            </pre>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</g:if>
<g:else>
    <div class="alert alert-danger alert-dismissable">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
        No content was able to be extracted from the URL: ${sourceUrl ?: "null"}
    </div>
</g:else>