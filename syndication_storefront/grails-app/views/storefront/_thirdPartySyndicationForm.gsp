<div style="width: 400px; float: left;">
    <g:form action="showContent" id="${mediaItemInstance.id}">
        <span>Copy and paste this snippet into your existing web-page, and when the page is viewed by a user, the syndicated content will inject itself automatically!</span>
        <p></p>
        <div style="width: 100%; overflow: hidden;">
            <div style="width: 200px; float: left;">
                <g:checkBox name="stripStyles"  checked="${stripStyles}" id="stripStyles"/><label>Strip Styles</label><br/>
                <g:checkBox name="stripScripts" checked="${stripScripts}" id="stripScripts"/><label>Strip Scripts</label><br/>
                <g:checkBox name="stripImages"  checked="${stripImages}" id="stripImages"/><label>Strip Images</label><br/>
                <input type="submit" name="submitChangesButton" value="Apply" id="submitChangesButton">
            </div>
            <div style="margin-left: 120px;">
            </div>
        </div>
    </g:form>
</div>