<div class="pop">
    <div class="popAdditional">
        <a id="reportAProblemPopupAnchor">&nbsp;</a>
        <h2 class="inlineHeader">Report a problem with the Syndication System</h2><br/><br/>
        <g:if test="${flash.error}">
            <div class="errors" style="display: block">${flash.error}</div>
        </g:if>
        <g:form action="sendProblemReport">
            <label for="reporterEmailAddressField">Please provide your email address</label><br/>
            <g:textField class="popupTextField" name="reporterEmailAddress" id="reporterEmailAddressField"/><br/>
            <label for="badURLTextField">Enter the URL of the page in error (Optional)</label><br/>
            <g:textField class="popupTextField" name="badURL" id="badURLTextField"/><br/>
            <label for="problemDescriptionTextArea">Please enter a description of the problem.</label><br/>
            <g:textArea id="problemDescriptionTextArea" name="problemDescription" class="popupTextAreaField"/><br/>
            <recaptcha:ifEnabled>
                <recaptcha:recaptcha theme="clean" id="recaptcha"/>
            </recaptcha:ifEnabled><br/>
            <g:submitButton name="submitButton" value="Submit"/>
        </g:form>
    </div>
</div>