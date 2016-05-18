<div style="width: 400px; float: left;">
    <g:form action="showContent" id="${mediaItemInstance.id}">
        <span>Copy and paste this snippet into your existing web-page, and when the page is viewed by a user, the syndicated content will inject itself automatically!</span>
        <p></p>
        <div style="width: 100%; overflow: hidden;">
            <div style="width: 200px; float: left;">
                %{--<g:checkBox name="includeJquery"    class="extractionCheckbox" checked="true"  id="includeJquery"/><label>Include JQuery</label><br/>--}%
                %{--<g:checkBox name="stripClasses"     class="extractionCheckbox" checked="true" id="stripClasses"/><label>Strip Classes</label><br/>--}%
                <g:checkBox name="stripStyles"  checked="${stripStyles}" id="stripStyles"/><label>Strip Styles</label><br/>
                <g:checkBox name="stripScripts" checked="${stripScripts}" id="stripScripts"/><label>Strip Scripts</label><br/>
                <g:checkBox name="stripImages"  checked="${stripImages}" id="stripImages"/><label>Strip Images</label><br/>
                %{--<label for="width">Width</label>--}%
                %{--<g:textField name="width" type="number" style="width: 50px" value="775" disabled="disabled" id="iframeWidth" class="iframe_size_control"/>--}%
                %{--<br>--}%
                %{--<label for="height">Height</label>--}%
                %{--<g:textField name="height" type="number" style="width: 50px" value="650" disabled="disabled" id="iframeHeight" class="iframe_size_control"/>--}%
                <input type="submit" name="submitChangesButton" value="Submit Changes" id="submitChangesButton">
            </div>
            <div style="margin-left: 120px;">
                %{--<g:checkBox name="stripIds"         class="extractionCheckbox" checked="true" id="stripIds"/><label>Strip Ids</label><br/>--}%
                %{--<g:checkBox name="stripBreaks"      class="extractionCheckbox" checked="false" id="stripBreaks"/><label>Strip Breaks</label><br/>--}%
            </div>
        </div>
    </g:form>
</div>