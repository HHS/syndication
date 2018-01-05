<style type="text/css">
    .col-1-8 {
        width: 12.5%;
    }
    .col-2-8 {
        width: 25%;
    }
    .col-4-8 {
        width: 50%;
    }
    .col-6-8 {
        width: 75%;
    }
    .col-7-8 {
        width: 87.5%;
    }
    .col-8-8 {
        width: 100%;
    }
    .grid:after {
        content: "";
        display: table;
        clear: both;
    }
    .grid [class*='col-'] {
        float: left;
        padding: 0;
        margin: 0;
    }
    .search-form {
        padding-bottom: 10px;
    }
    .search-form h1 {
        margin-top: 0;
    }
    .search-form h5 {
        padding: 0;
        margin: 0 0 10px 0;
        background-color: inherit;
    }
    .search-form input {
        width: 100%;
    }
    .search-form label {
        margin-bottom: 10px;
    }
    .filters input, .filters select {
        width: 90%;
        margin-bottom: 10px;
    }
</style>

<form class="search-form bluebox">
    <h1>Search Media Items</h1>
    <div class="grid">
        <div class="col-1-8">
            <label class="" for="fullText">Full Text</label>
        </div>
        <div class="col-7-8">
            <input class="" value="${params.fullText}" id="fullText" name="fullText" type="text" placeholder="Search for text in several fields (e.g title, description, content)"/>
        </div>
    </div>
    <hr/>
    <h5>Filter results by:</h5>
    <div class="grid filters">
        <div class="col-4-8">
            <div class="col-8-8">
                <div class="col-2-8">
                    <label for="title">Title</label>
                </div>
                <div class="col-6-8">
                    <input class="" id="title" name="title" type="text" placeholder="Title must contain these words" value="${params.title}"/>
                </div>
            </div>
            <div class="col-8-8">
                <div class="col-2-8">
                    <label for="createdBy">Created By</label>
                </div>
                <div class="col-6-8">
                    <input class="" id="createdBy" name="createdBy" value="${params.createdBy}" type="text" placeholder="Item was created by"/>
                </div>
            </div>
            <div class="col-8-8">
                <div class="col-2-8">
                    <label for="sourceUrl">URL</label>
                </div>
                <div class="col-6-8">
                    <input class="" id="sourceUrl" name="sourceUrl" value="${params.sourceUrl}" type="text" placeholder="The source url of the item"/>
                </div>
            </div>
        </div>
        <div class="col-4-8">
            <div class="col-8-8">
                <div class="col-2-8">
                    <label for="mediaType">Media Type</label>
                </div>
                <div class="col-6-8">
                    <g:select name="mediaType"
                              from="${mediaTypes*.name}"
                              value="${params.mediaType}"
                              keys="${mediaTypes*.id}"
                              noSelection="['': 'All Media Types']"/>
                </div>
            </div>
            <div class="col-8-8">
                <div class="col-2-8">
                    <label for="mediaType">Source</label>
                </div>
                <div class="col-6-8">
                    <g:select name="sourceId" from="${sourceList}" optionValue="name" value="${params.sourceId}" optionKey="id" noSelection="['': 'All Sources']"/>
                </div>
            </div>
            <div class="col-8-8">
                <div class="col-2-8">
                    <label for="mediaType">Language</label>
                </div>
                <div class="col-6-8">
                    <g:select name="languageId" from="${languageList}" optionValue="name" value="${params.languageId}" optionKey="id" noSelection="['': 'All Languages']"/>
                </div>
            </div>
            <input type="hidden" value="${params.tagId}" name="tagId" id="tagId" />
        </div>
    </div>
    <div class="grid controls">
        <div class="col-8-8">
            <button name="search" value="Search" type="submit">Search</button>
        </div>
    </div>
</form>

