<h3>
    Media Item Lookup
</h3>

<form id="otherSearchForm" action="${g.createLink(action: 'index')}">
    <table class="advanceSearch" summary="This table is used for layout purposes.">
        <tbody>
        <tr>
            <td class="col1">
                <label for="title">
                    Title:
                </label>
            </td>
            <td class="col2">
                <input id="title" type="text" value="${title}" name="title"/>
            </td>
            <td class="col3">
                <label>
                    Media Type:
                </label>
            </td>
            <td class="col4">
                <g:select name="mediaType"
                          from="${mediaTypes*.name}"
                          value="${mediaType}"
                          keys="${mediaTypes*.id}"
                          noSelection="['': 'All Media Types']"/>
            </td>
        </tr>
        <tr>
            <td class="col1">
                <label>
                    Source:
                </label>
            </td>
            <td class="col2">
                <g:select name="source" from="${sourceList}" value="${source}" optionKey="name"
                          noSelection="['': 'All Sources']"/>
            </td>
        </tr>
        <tr>
            <td class="col1">
                <label for="domain">
                    URL:
                </label>
            </td>
            <td class="col2">
                <input id="domain" type="text" value="${domain}" name="domain"/>
            </td>
            <td class="col3">
                <label>
                    Language:
                </label>
            </td>
            <td class="col4">
                <g:select name="language" from="${languageList}" value="${language}" optionKey="name"
                          noSelection="['': 'All Languages']"/>
            </td>
        </tr>
        </tbody>
    </table>
    <button name="advancedSearch" value="Search" type="submit">Search</button>
</form>

<p class="more">
    <g:remoteLink controller="storefront" action="basicSearch" update="searchOptions">
        <i class="more">Basic Search > ></i>
    </g:remoteLink>
</p>
