<%@ page import="com.ctacorp.syndication.storefront.UserMediaList" %>
<div class="fieldcontain ${hasErrors(bean: userMediaList, field: 'name', 'error')} ">
    <div class="left_side">
        <label for="name">
            <g:message code="userMediaList.name.label" default="Name" />
            <span class="required-indicator">*</span>
        </label>
    </div>
    <div class="right_side">
	    <g:textField name="name" required="" maxlength="255" value="${userMediaList?.name}"/>
    </div>
</div>

<div class="fieldcontain ${hasErrors(bean: userMediaList, field: 'description', 'error')}">
    <div class="left_side">
        <label for="description">
            Description
            <span class="required-indicator">*</span>
        </label>
    </div>
    <div class="right_side">
        <g:textArea class="mediaListDesc" maxlength="2048" rows="5" required="" cols="40" name="description" value="${userMediaList?.description}"/>
    </div>
</div>

<div class="fieldcontain ${hasErrors(bean: userMediaList, field: 'mediaItems', 'error')} ">
    <div class="left_side">
        <label for="mediaItemIds">
            <g:message code="userMediaList.mediaItems.label" default="Media Items" />
        </label>
    </div>
    <div class="right_side">
        <g:textField name="mediaItemIds" id="mediaItemIds"/>
    </div>
</div>