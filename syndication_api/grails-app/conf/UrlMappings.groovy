/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

class UrlMappings {
	static mappings = {
        String v2 = "/api/v2"
        String v3 = "/api/v3"

// =================================================================================================================================
// |     ######   ##        #######  ########     ###    ##           ######  ########    ###    ########   ######  ##     ##      |
// |    ##    ##  ##       ##     ## ##     ##   ## ##   ##          ##    ## ##         ## ##   ##     ## ##    ## ##     ##      |
// |    ##        ##       ##     ## ##     ##  ##   ##  ##          ##       ##        ##   ##  ##     ## ##       ##     ##      |
// |    ##   #### ##       ##     ## ########  ##     ## ##           ######  ######   ##     ## ########  ##       #########      |
// |    ##    ##  ##       ##     ## ##     ## ######### ##                ## ##       ######### ##   ##   ##       ##     ##      |
// |    ##    ##  ##       ##     ## ##     ## ##     ## ##          ##    ## ##       ##     ## ##    ##  ##    ## ##     ##      |
// |     ######   ########  #######  ########  ##     ## ########     ######  ######## ##     ## ##     ##  ######  ##     ##      |
// =================================================================================================================================
        "$v2/resources(.$format)?"(controller: "resources", parseRequest: true) {
            action = [GET: "index"]
        }

// ========================================================
// |    ##     ## ######## ########  ####    ###          |
// |    ###   ### ##       ##     ##  ##    ## ##         |
// |    #### #### ##       ##     ##  ##   ##   ##        |
// |    ## ### ## ######   ##     ##  ##  ##     ##       |
// |    ##     ## ##       ##     ##  ##  #########       |
// |    ##     ## ##       ##     ##  ##  ##     ##       |
// |    ##     ## ######## ########  #### ##     ##       |
// ========================================================

        "$v2/resources/media(.$format)?"(controller: "media", parseRequest: true) {
            action = [GET: "list"]
        }

        "$v2/resources/media/mostPopularMedia(.$format)?"(controller: "media", parseRequest: true) {
            action = [GET: "mostPopularMedia"]
        }

        "$v2/resources/media/$id(.$format)?"(controller: "media", parseRequest: true) {
            action = [GET: "show"]
        }

        "$v2/resources/media/$id/preview.jpg"(controller: "media", parseRequest: true) {
            action = [GET: "preview"]
        }

        "$v2/resources/media/$id/thumbnail.jpg"(controller: "media", parseRequest: true) {
            action = [GET: "thumbnail"]
        }

        "$v2/resources/media/searchResults(.$format)?"(controller: "media", parseRequest: true) {
            action = [GET: "search"]
        }

        "$v2/resources/media/$id/embed(.$format)?"(controller: "media", parseRequest: true) {
            action = [GET: "embed"]
        }

        "$v2/resources/media/$id/javascriptContent(.$format)?"(controller: "media", parseRequest: true) {
            action = [GET: "javascriptContent"]
        }

        "$v2/resources/media/$id/tinyUrl(.$format)?"(controller: "media", parseRequest: true) {
            action = [GET: "show"]
        }

        "$v2/resources/media/$id/content(.$format)?"(controller: "media", parseRequest: true) {
            action = [GET: "content"]
        }

        "$v2/resources/media/$id/syndicate(.$format)?"(controller: "media", parseRequest: true) {
            action = [GET: "syndicate"]
        }

        "$v2/resources/media/$id/ratings(.$format)?"(controller: "media", parseRequest: true) {
            action = [GET: "likes", POST: "like", DELETE: "unlike"]
        }

        "$v2/resources/media/metrics(.$format)?"(controller: "media", parseRequest: true) {
            action = [GET: "metrics"]
        }

        "$v2/resources/media/$id/tags(.$format)?"(controller: "tags", parseRequest: true) {
            action = [GET: "listForMedia"]
        }

        "$v2/resources/media/$id/relatedMedia(.$format)?"(controller: "media", parseRequest: true) {
            action = [GET: "relatedMedia"]
        }

        "$v2/resources/media/$id/youtubeMetaData(.$format)?"(controller: "media", parseRequest: true) {
            action = [GET: "youtubeMetaData"]
        }

        "$v2/resources/media/featured(.$format)?"(controller: "media", parseRequest: true){
            action = [GET: "featured"]
        }

// =====================================================
// |    ##     ## ######## ########  ####    ###       |
// |    ###   ### ##       ##     ##  ##    ## ##      |
// |    #### #### ##       ##     ##  ##   ##   ##     |
// |    ## ### ## ######   ##     ##  ##  ##     ##    |
// |    ##     ## ##       ##     ##  ##  #########    |
// |    ##     ## ##       ##     ##  ##  ##     ##    |
// |    ##     ## ######## ########  #### ##     ##    |
// =====================================================

        "$v2/resources/media/collections"(controller: "media", parseRequest: true) {
            action = [POST: "saveCollection"]
        }

        "$v2/resources/media/faqs"(controller: "media", parseRequest: true) {
            action = [POST: "saveFAQ"]
        }

        "$v2/resources/media/htmls(.$format)?"(controller: "media", parseRequest: true) {
            action = [POST: "saveHtml"]
        }

        "$v2/resources/media/images"(controller: "media", parseRequest: true) {
            action = [POST: "saveImage"]
        }

        "$v2/resources/media/infographics"(controller: "media", parseRequest: true) {
            action = [POST: "saveInfographic"]
        }

        "$v2/resources/media/pdfs"(controller: "media", parseRequest: true) {
            action = [POST: "savePDF"]
        }

        "$v2/resources/media/questionAndAnswers"(controller: "media", parseRequest: true) {
            action = [POST: "saveQuestionAndAnswer"]
        }

        "$v2/resources/media/tweets"(controller: "media", parseRequest: true) {
            action = [POST: "saveTweet"]
        }

        "$v2/resources/media/videos"(controller: "media", parseRequest: true) {
            action = [POST: "saveVideo"]
        }

// =================================================================================================
// |       ###    ##       ########       #### ##     ##    ###     ######   ########  ######      |
// |      ## ##   ##          ##           ##  ###   ###   ## ##   ##    ##  ##       ##    ##     |
// |     ##   ##  ##          ##           ##  #### ####  ##   ##  ##        ##       ##           |
// |    ##     ## ##          ##           ##  ## ### ## ##     ## ##   #### ######    ######      |
// |    ######### ##          ##           ##  ##     ## ######### ##    ##  ##             ##     |
// |    ##     ## ##          ##           ##  ##     ## ##     ## ##    ##  ##       ##    ##     |
// |    ##     ## ########    ##          #### ##     ## ##     ##  ######   ########  ######      |
// =================================================================================================

        "$v2/resources/media/alternateImages"(controller: "alternateImages", parseRequest: true) {
            action = [POST: 'saveAlternateImage']
        }


// ==============================================================================================
// |     ######     ###    ##     ## ########     ###    ####  ######   ##    ##  ######        |
// |    ##    ##   ## ##   ###   ### ##     ##   ## ##    ##  ##    ##  ###   ## ##    ##       |
// |    ##        ##   ##  #### #### ##     ##  ##   ##   ##  ##        ####  ## ##             |
// |    ##       ##     ## ## ### ## ########  ##     ##  ##  ##   #### ## ## ##  ######        |
// |    ##       ######### ##     ## ##        #########  ##  ##    ##  ##  ####       ##       |
// |    ##    ## ##     ## ##     ## ##        ##     ##  ##  ##    ##  ##   ### ##    ##       |
// |     ######  ##     ## ##     ## ##        ##     ## ####  ######   ##    ##  ######        |
// ==============================================================================================

        "$v2/resources/campaigns(.$format)?"(controller: "campaigns", parseRequest: true) {
            action = [GET: "list", POST: "save"]
        }

        "$v2/resources/campaigns/$id(.$format)?"(controller: "campaigns", parseRequest: true) {
            action = [GET: "show"]
        }

        "$v2/resources/campaigns/$id/media(.$format)?"(controller: "campaigns", parseRequest: true) {
            action = [GET: "listMediaForCampaign"]
        }

        "$v2/resources/campaigns/$id/syndicate(.$format)?"(controller: "campaigns", parseRequest: true) {
            action = [GET: "syndicate"]
        }

        "$v2/resources/campaigns/$id/embed(.$format)?"(controller: "campaigns", parseRequest: true) {
            action = [GET: "embed"]
        }


// ===================================================================================================
// |    ##          ###    ##    ##  ######   ##     ##    ###     ######   ########  ######         |
// |    ##         ## ##   ###   ## ##    ##  ##     ##   ## ##   ##    ##  ##       ##    ##        |
// |    ##        ##   ##  ####  ## ##        ##     ##  ##   ##  ##        ##       ##              |
// |    ##       ##     ## ## ## ## ##   #### ##     ## ##     ## ##   #### ######    ######         |
// |    ##       ######### ##  #### ##    ##  ##     ## ######### ##    ##  ##             ##        |
// |    ##       ##     ## ##   ### ##    ##  ##     ## ##     ## ##    ##  ##       ##    ##        |
// |    ######## ##     ## ##    ##  ######    #######  ##     ##  ######   ########  ######         |
// ===================================================================================================

        "$v2/resources/languages(.$format)?"(controller: "languages", parseRequest: true) {
            action = [GET: "list", POST: "save"]
        }

        "$v2/resources/languages/$id(.$format)?"(controller: "languages", parseRequest: true) {
            action = [GET: "show"]
        }

// =========================================================================================================
// |    ##     ## ######## ########  ####    ###       ######## ##    ## ########  ########  ######        |
// |    ###   ### ##       ##     ##  ##    ## ##         ##     ##  ##  ##     ## ##       ##    ##       |
// |    #### #### ##       ##     ##  ##   ##   ##        ##      ####   ##     ## ##       ##             |
// |    ## ### ## ######   ##     ##  ##  ##     ##       ##       ##    ########  ######    ######        |
// |    ##     ## ##       ##     ##  ##  #########       ##       ##    ##        ##             ##       |
// |    ##     ## ##       ##     ##  ##  ##     ##       ##       ##    ##        ##       ##    ##       |
// |    ##     ## ######## ########  #### ##     ##       ##       ##    ##        ########  ######        |
// =========================================================================================================

        "$v2/resources/mediaTypes(.$format)?"(controller: "mediaTypes", parseRequest: true) {
            action = [GET: "list", POST: "save"]
        }

// ===================================================
// |    ########    ###     ######    ######         |
// |       ##      ## ##   ##    ##  ##    ##        |
// |       ##     ##   ##  ##        ##              |
// |       ##    ##     ## ##   ####  ######         |
// |       ##    ######### ##    ##        ##        |
// |       ##    ##     ## ##    ##  ##    ##        |
// |       ##    ##     ##  ######    ######         |
// ===================================================

        "$v2/resources/tags(.$format)?"(controller: "tags", parseRequest: true) {
            action = [GET: "list", POST: "tagSave"]
        }

        "$v2/resources/tags/$id(.$format)?"(controller: "tags", parseRequest: true) {
            action = [GET: "show", PUT: "update"]
        }

        "$v2/resources/tags/$id/related(.$format)?"(controller: "tags", parseRequest: true) {
            action = [GET: "relatedTags"]
        }

        "$v2/resources/tags/$id/media(.$format)?"(controller: "tags", parseRequest: true) {
            action = [GET: "listMediaForTagId"]
        }

        "$v2/resources/tags/$id/syndicate(.$format)?"(controller: "tags", parseRequest: true) {
            action = [GET: "syndicate"]
        }

        "$v2/resources/tags/$id/embed(.$format)?"(controller: "tags", parseRequest: true) {
            action = [GET: "embed"]
        }

        "$v2/resources/tags/tagLanguages(.$format)?"(controller: "tags", parseRequest: true) {
            action = [GET: "listLanguages"]
        }

        "$v2/resources/tags/tagTypes(.$format)?"(controller: "tags", parseRequest: true) {
            action = [GET: "listTypes"]
        }

        //Undocumented API calls
        "$v2/resources/tags/query(.$format)?"(controller: "tags", parseRequest: true){
            action = [GET: "query"]
        }

// ============================================================================
// |    ######## #### ##    ## ##    ##    ##     ## ########  ##             |
// |       ##     ##  ###   ##  ##  ##     ##     ## ##     ## ##             |
// |       ##     ##  ####  ##   ####      ##     ## ##     ## ##             |
// |       ##     ##  ## ## ##    ##       ##     ## ########  ##             |
// |       ##     ##  ##  ####    ##       ##     ## ##   ##   ##             |
// |       ##     ##  ##   ###    ##       ##     ## ##    ##  ##             |
// |       ##    #### ##    ##    ##        #######  ##     ## ########       |
// ============================================================================

        "$v2/resources/tinyurls/$id/sourceUrl"(controller: "tinyUrls", parseRequest: true) {
            action = [GET: "sourceUrl"]
        }

// ================================================================
// |    ########  #######  ########  ####  ######   ######        |
// |       ##    ##     ## ##     ##  ##  ##    ## ##    ##       |
// |       ##    ##     ## ##     ##  ##  ##       ##             |
// |       ##    ##     ## ########   ##  ##        ######        |
// |       ##    ##     ## ##         ##  ##             ##       |
// |       ##    ##     ## ##         ##  ##    ## ##    ##       |
// |       ##     #######  ##        ####  ######   ######        |
// ================================================================

        "$v2/resources/topics(.$format)?"(controller: "topics", parseRequest: true){
            action = [GET: "list"]
        }

// ===========================================================
// |    ##     ##  ######  ######## ########   ######        |
// |    ##     ## ##    ## ##       ##     ## ##    ##       |
// |    ##     ## ##       ##       ##     ## ##             |
// |    ##     ##  ######  ######   ########   ######        |
// |    ##     ##       ## ##       ##   ##         ##       |
// |    ##     ## ##    ## ##       ##    ##  ##    ##       |
// |     #######   ######  ######## ##     ##  ######        |
// ===========================================================

        "$v2/resources/users/subscriptions"(controller: "users", parseRequest: true) {
            action = [POST: "saveSubscription"]
        }

        "$v2/resources/users/$id/subscriptions(.$format)?"(controller: "users", parseRequest: true) {
            action = [GET: "listSubs"]
        }

        "$v2/resources/users/$id/subscriptions/$subId"(controller: "users", parseRequest: true) {
            action = [GET: "subscribe", DELETE: "unsubscribe"]
        }

        "$v2/resources/users/$id/ratings(.$format)?"(controller: "users", parseRequest: true) {
            action = [GET: "getLikes"]
        }

// ===================================================================================================================================
// |    ##     ##  ######  ######## ########        ##     ## ######## ########  ####    ###    ##       ####  ######  ########      |
// |    ##     ## ##    ## ##       ##     ##       ###   ### ##       ##     ##  ##    ## ##   ##        ##  ##    ##    ##         |
// |    ##     ## ##       ##       ##     ##       #### #### ##       ##     ##  ##   ##   ##  ##        ##  ##          ##         |
// |    ##     ##  ######  ######   ########        ## ### ## ######   ##     ##  ##  ##     ## ##        ##   ######     ##         |
// |    ##     ##       ## ##       ##   ##         ##     ## ##       ##     ##  ##  ######### ##        ##        ##    ##         |
// |    ##     ## ##    ## ##       ##    ##        ##     ## ##       ##     ##  ##  ##     ## ##        ##  ##    ##    ##         |
// |     #######   ######  ######## ##     ##       ##     ## ######## ########  #### ##     ## ######## ####  ######     ##         |
// ===================================================================================================================================

        "$v2/resources/userMediaLists/$id(.$format)?"(controller:"userMediaLists", parseRequest: true){
            action = [GET: "show"]
        }

        "$v2/resources/userMediaLists/$id/syndicate(.$format)?"(controller:"userMediaLists", parseRequest: true){
            action = [GET: "syndicate"]
        }

        "$v2/resources/userMediaLists/$id/embed(.$format)?"(controller:"userMediaLists", parseRequest: true){
            action = [GET: "embed"]
        }


// ==============================================================================
// |     ######   #######  ##     ## ########   ######  ########  ######        |
// |    ##    ## ##     ## ##     ## ##     ## ##    ## ##       ##    ##       |
// |    ##       ##     ## ##     ## ##     ## ##       ##       ##             |
// |     ######  ##     ## ##     ## ########  ##       ######    ######        |
// |          ## ##     ## ##     ## ##   ##   ##       ##             ##       |
// |    ##    ## ##     ## ##     ## ##    ##  ##    ## ##       ##    ##       |
// |     ######   #######   #######  ##     ##  ######  ########  ######        |
// ==============================================================================

        "$v2/resources/sources(.$format)?"(controller: "sources", parseRequest: true){
            action = [GET: "list"]
        }

        "$v2/resources/sources/$id(.$format)?"(controller: "sources", parseRequest: true){
            action = [GET: "show"]
        }

        "$v2/resources/sources/$id/syndicate(.$format)?"(controller: "sources", parseRequest: true){
            action = [GET: "syndicate"]
        }

        "$v2/resources/sources/$id/embed(.$format)?"(controller: "sources", parseRequest: true) {
            action = [GET: "embed"]
        }

// ====================================================
// |       ###    ##     ## ######## ##     ##        |
// |      ## ##   ##     ##    ##    ##     ##        |
// |     ##   ##  ##     ##    ##    ##     ##        |
// |    ##     ## ##     ##    ##    #########        |
// |    ######### ##     ##    ##    ##     ##        |
// |    ##     ## ##     ##    ##    ##     ##        |
// |    ##     ##  #######     ##    ##     ##        |
// ====================================================

        "/login/${action}"(controller: "login")
        "/logout"(controller:"logout", action:"index")
        "/keyTest"(controller:"keyTest", action:"index")

// =======================================================================================================
// |       ###    ########  ########         ######  ########    ###    ######## ##     ##  ######       |
// |      ## ##   ##     ## ##     ##       ##    ##    ##      ## ##      ##    ##     ## ##    ##      |
// |     ##   ##  ##     ## ##     ##       ##          ##     ##   ##     ##    ##     ## ##            |
// |    ##     ## ########  ########         ######     ##    ##     ##    ##    ##     ##  ######       |
// |    ######### ##        ##                    ##    ##    #########    ##    ##     ##       ##      |
// |    ##     ## ##        ##              ##    ##    ##    ##     ##    ##    ##     ## ##    ##      |
// |    ##     ## ##        ##               ######     ##    ##     ##    ##     #######   ######       |
// =======================================================================================================

        "/statusCheck/$action?"(controller:"statusCheck")

// ===============================================
// |    ######## ########  ######  ########      |
// |       ##    ##       ##    ##    ##         |
// |       ##    ##       ##          ##         |
// |       ##    ######    ######     ##         |
// |       ##    ##             ##    ##         |
// |       ##    ##       ##    ##    ##         |
// |       ##    ########  ######     ##         |
// ===============================================

        "/init/$action?"(controller:'init')
        "/test/$action?"(controller:'test')
        "/micrositeTestData/$action?/$id?"(controller: "micrositeTestData", parseRequest: true)

// ==========================================================
// |     ######     ###     ######  ##     ## ########      |
// |    ##    ##   ## ##   ##    ## ##     ## ##            |
// |    ##        ##   ##  ##       ##     ## ##            |
// |    ##       ##     ## ##       ######### ######        |
// |    ##       ######### ##       ##     ## ##            |
// |    ##    ## ##     ## ##    ## ##     ## ##            |
// |     ######  ##     ##  ######  ##     ## ########      |
// ==========================================================

        "/cacheAccess/$action?"(controller:'cacheAccess')

// =================================================================================================================
// |    ##     ## ######## ########  ####    ###       ##     ## #### ######## ##      ## ######## ########        |
// |    ###   ### ##       ##     ##  ##    ## ##      ##     ##  ##  ##       ##  ##  ## ##       ##     ##       |
// |    #### #### ##       ##     ##  ##   ##   ##     ##     ##  ##  ##       ##  ##  ## ##       ##     ##       |
// |    ## ### ## ######   ##     ##  ##  ##     ##    ##     ##  ##  ######   ##  ##  ## ######   ########        |
// |    ##     ## ##       ##     ##  ##  #########     ##   ##   ##  ##       ##  ##  ## ##       ##   ##         |
// |    ##     ## ##       ##     ##  ##  ##     ##      ## ##    ##  ##       ##  ##  ## ##       ##    ##        |
// |    ##     ## ######## ########  #### ##     ##       ###    #### ########  ###  ###  ######## ##     ##       |
// =================================================================================================================

        "/mediaViewer/$action?/$id?"(controller:'mediaViewer', parseRequest: true)

// ================================================================================================
// |       ###    ########  ##     ## #### ##    ##    ######## ##     ## ##    ##  ######        |
// |      ## ##   ##     ## ###   ###  ##  ###   ##    ##       ##     ## ###   ## ##    ##       |
// |     ##   ##  ##     ## #### ####  ##  ####  ##    ##       ##     ## ####  ## ##             |
// |    ##     ## ##     ## ## ### ##  ##  ## ## ##    ######   ##     ## ## ## ## ##             |
// |    ######### ##     ## ##     ##  ##  ##  ####    ##       ##     ## ##  #### ##             |
// |    ##     ## ##     ## ##     ##  ##  ##   ###    ##       ##     ## ##   ### ##    ##       |
// |    ##     ## ########  ##     ## #### ##    ##    ##        #######  ##    ##  ######        |
// ================================================================================================

        "$v2/adminControls/media/delete/$id"(controller: "admin", parseRequest: true) {
            action = [DELETE: "deleteMedia"]
        }

        "$v2/adminControls/media/archive/$id"(controller: "admin", parseRequest: true) {
            action = [POST: "archiveMedia"]
        }

// ==================================================================================================================
// |       ###    ########  ########         ######   #######  ##    ## ######## ########   #######  ##             |
// |      ## ##   ##     ## ##     ##       ##    ## ##     ## ###   ##    ##    ##     ## ##     ## ##             |
// |     ##   ##  ##     ## ##     ##       ##       ##     ## ####  ##    ##    ##     ## ##     ## ##             |
// |    ##     ## ########  ########        ##       ##     ## ## ## ##    ##    ########  ##     ## ##             |
// |    ######### ##        ##              ##       ##     ## ##  ####    ##    ##   ##   ##     ## ##             |
// |    ##     ## ##        ##              ##    ## ##     ## ##   ###    ##    ##    ##  ##     ## ##             |
// |    ##     ## ##        ##               ######   #######  ##    ##    ##    ##     ##  #######  ########       |
// ==================================================================================================================

        "/"(controller:'swagger', action:'index')
        "/appInfo"(controller:'appInfo', action: 'index')
        "500"(controller:'error', action: 'fivehundred')
        "/error/unauthorized"(controller:'error', action:'unauthorized')
	}
}

// =========================================================
// |    ##    ##  #######  ######## ########  ######       |
// |    ###   ## ##     ##    ##    ##       ##    ##      |
// |    ####  ## ##     ##    ##    ##       ##            |
// |    ## ## ## ##     ##    ##    ######    ######       |
// |    ##  #### ##     ##    ##    ##             ##      |
// |    ##   ### ##     ##    ##    ##       ##    ##      |
// |    ##    ##  #######     ##    ########  ######       |
// =========================================================

// ASCII labels created with http://patorjk.com/software/taag/#p=display&f=Banner3&t=Ascii%20%20banners
// Font is Banner3