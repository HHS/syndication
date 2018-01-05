<g:form controller="storefront" action="sendSyndicationRequest">
    <label for="requesterEmailAddressField">Please provide your email address</label><br/>
    <g:textField class="popupTextField" name="requesterEmailAddress" id="requesterEmailAddressField"/><br/>
    <label for="contentSourceURL">Content URL</label><br/>
    <g:textField class="popupTextField" name="contentSourceURL" id="contentSourceURLTextField" value="${params.contentURL}"/><br/>
    <label for="commentsTextArea">Please include any specific details about the content you would like to see syndicated</label><br/>
    <g:textArea id="commentsTextArea" name="comments" class="popupTextAreaField"/><br/>
    <g:submitButton name="submitButton" value="Submit"/>
</g:form>