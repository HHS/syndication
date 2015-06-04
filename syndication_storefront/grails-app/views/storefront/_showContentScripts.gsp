<sec:ifLoggedIn>
    <script type="application/javascript">
        $(document).ready(function () {

            // Update Functions ----------------------------------------------------------------------------------------
            function updatePreview() {
                var url;
                if("${mediaItemInstance}"){
                    url = "${grails.util.Holders.config.syndication.serverUrl + grails.util.Holders.config.syndication.apiPath}/resources/media/${mediaItemInstance?.id}/embed.json?autoplay=0&" + getQueryString()
                } else if("${userMediaListInstance}"){
                    url = "${grails.util.Holders.config.syndication.serverUrl + grails.util.Holders.config.syndication.apiPath}/resources/userMediaLists/${userMediaListInstance?.id}/embed.json?autoplay=0&" + getQueryString()
                } else if("${renderTagList}"){
                    url = "${grails.util.Holders.config.syndication.serverUrl + grails.util.Holders.config.syndication.apiPath}/resources/tags/${id}/embed.json?autoplay=0&" + getQueryString()
                } else{
                    console.error("Could not find an instance to work with!")
                }
                $.getJSON(url, function (data) {
                    var decoded = $('<div/>').html(data.results[0].snippet).text();
                    $("#snippetPreview").html(decoded)
                })
            }

            function updateSnippet() {
                var url;
                if("${mediaItemInstance}"){
                    url = "${grails.util.Holders.config.syndication.serverUrl + grails.util.Holders.config.syndication.apiPath}/resources/media/${mediaItemInstance?.id}/embed.json?autoplay=0&" + getQueryString()
                } else if("${userMediaListInstance}"){
                    url = "${grails.util.Holders.config.syndication.serverUrl + grails.util.Holders.config.syndication.apiPath}/resources/userMediaLists/${userMediaListInstance?.id}/embed.json?autoplay=0&" + getQueryString()
                } else if("${renderTagList}"){
                    url = "${grails.util.Holders.config.syndication.serverUrl + grails.util.Holders.config.syndication.apiPath}/resources/tags/${id}/embed.json?autoplay=0&" + getQueryString()
                } else{
                    console.error("Could not find an instance to work with!")
                }

                $.getJSON(url, function (data) {
                    function htmlDecode(input) {
                        var e = document.createElement('div');
                        e.innerHTML = input;
                        return e.childNodes.length === 0 ? "" : e.childNodes[0].nodeValue;
                    }

                    $('#snippetCode').val(htmlDecode(data.results[0].snippet));
                });

                updatePreview();
            }

            // Data Getters --------------------------------------------------------------------------------------------

            function getFlavor() {
                return $('input[name=jsOrIframe]:checked').val()
            }

            function getDisplayMethod() {
                return $('input[name=displayMethod]:checked').val()
            }

            function getCollectionType() {
                var collectionType = $('input[name=listOrMediaViewer]:checked').val()
                if (collectionType) {
                    return "listOrMediaViewer=" + collectionType + "&"
                }
                return ""
            }

            function getQueryString() {
                var excludeJquery = $('#includeJquery').prop('checked') ? '0' : '1';
                var stripImages = $('#stripImages').prop('checked') ? '1' : '0';
                var stripStyles = $('#stripStyles').prop('checked') ? '1' : '0';
                var stripScripts = $('#stripScripts').prop('checked') ? '1' : '0';
                var stripBreaks = $('#stripBreaks').prop('checked') ? '1' : '0';
                var stripClasses = $('#stripClasses').prop('checked') ? '1' : '0';
                var width = $('#iframeWidth').val();
                var height = $('#iframeHeight').val();
                var flavor = getFlavor();
                var displayMethod = getDisplayMethod();

                var queryParams = "excludeJquery=" + excludeJquery;
                queryParams += "&stripImages=" + stripImages;
                queryParams += "&stripStyles=" + stripStyles;
                queryParams += "&stripScripts=" + stripScripts;
                queryParams += "&stripBreaks=" + stripBreaks;
                queryParams += "&stripClasses=" + stripClasses;
                queryParams += "&width=" + width;
                queryParams += "&height=" + height;
                queryParams += "&flavor=" + flavor;
                queryParams += "&displayMethod=" + displayMethod;
                queryParams += "&callback=?";

                return getCollectionType() + queryParams
            }

            // Event Listeners ----------------------------------------------------------------------------

            $(document).on('click', '.extractionCheckbox', function () {
                updateSnippet()
            })

            $(".iframe_size_control").change(function (e) {
                updateSnippet()
            });

            $(document).on('click', '#jsOrIframe', function () {
                updateSnippet()
                if ($('input[name=jsOrIframe]:checked').val() === "iframe") {
                    $("#includeJquery").prop("checked", false);
                    $("#includeJquery").prop("disabled", true);
                    $("#iframeWidth").prop("disabled", false);
                    $("#iframeHeight").prop("disabled", false);
                } else {
                    $("#includeJquery").prop("checked", true);
                    $("#includeJquery").prop("disabled", false);
                    $("#iframeWidth").prop("disabled", true);
                    $("#iframeHeight").prop("disabled", true);
                }
            })

            $(document).on('click', '#displayMethod', function () {
                if(getDisplayMethod() === "mv"){
                    $('input[type="radio"][name="jsOrIframe"][value="iframe"]').prop('checked', true).trigger("click");
                    $('input[type="radio"][name="jsOrIframe"][value="javascript"]').prop('disabled', true);
                    $('#iframeWidth').val('775');
                    $('#iframeHeight').val('650');
                } else{
                    $('input[type="radio"][name="jsOrIframe"][value="javascript"]').prop('disabled', false);
                }
                updateSnippet()
            })

            $(document).on('click', '#listOrMediaViewer', function () {
                updateSnippet()
            })

            $(document).on('click', '.alreadyLiked', function () {
                var mediaId = $(this).attr('data-mediaId');
                var reference = this;
                setTimeout(function () {
                    $.ajax({ // create an AJAX call...
                        type: 'POST', // GET or POST
                        data: {id: mediaId},
                        contentType: 'json',
                        url: '${g.createLink(controller: 'storefront', action: 'ajaxUndoLike')}' + '/' + mediaId, // the file to call
                        success: function (response) { // on success..
                            $(reference).removeClass("alreadyLiked");
                            $(reference).addClass("notLiked");
                            $(reference).html("<i class='socialIcons fa fa-thumbs-up'></i>");  // update the DIV
                            $('#likeCount_' + mediaId).html(response);
                        }
                    });
                });
            });

            $(document).on('click', '.notLiked', function () {
                var mediaId = $(this).attr('data-mediaId');
                var reference = this;
                setTimeout(function () {
                    $.ajax({ // create an AJAX call...
                        type: 'POST', // GET or POST
                        data: {id: mediaId},
                        contentType: 'json',
                        url: '${g.createLink(controller: 'storefront', action: 'ajaxLike')}' + '/' + mediaId, // the file to call
                        success: function (response) { // on success..
                            $(reference).removeClass("notLiked");
                            $(reference).addClass("alreadyLiked");
                            $(reference).html("<i class='socialIcons fa fa-thumbs-up'></i>");  // update the DIV
                            $('#likeCount_' + mediaId).html(response);
                        }
                    });
                });
            });

            // Init Tasks ----------------------------------------------------------------------------------------------

            updateSnippet();
        });
    </script>
</sec:ifLoggedIn>