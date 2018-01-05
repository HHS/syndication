<form id="mediaSearchForm" action="${g.createLink(action: 'index')}">
    <label for="mediaName"><strong>Search&nbsp;</strong></label>
    <g:textField name="searchQuery" id="mediaName" value="${params.searchQuery}" class="form-control"/>
    <input type="button" name="basicSearch" id="basicSearch" value="Search" onclick="this.form.submit();"/>
</form>

<p class="more">
    <g:remoteLink controller="storefront" action="otherLookupOptions" update="searchOptions">
        <i class="more">Advanced Search > ></i>
    </g:remoteLink>
</p>