<script>
    function updateSummary(area, sidePanel){
            var listType = document.getElementById("pane" + area + "selectorType");
            if(listType != null) listType = listType.value;
            var panelHeader = document.getElementById("pane" + area + "Header");
            if(panelHeader != null) panelHeader = panelHeader.value;
            var listId = document.getElementById("pane" + area + "ListId");
            if(listId != null) listId = listId.value;
            var sortBy = document.getElementById("pane" + area + "Sort").value;
            var orderBy = document.getElementById("pane" + area + "Order").value;
            var displayStyle = document.getElementById("pane" + area + "DisplayStyle");
            if(displayStyle != null) displayStyle = displayStyle.value;

            $.ajax({ // create an AJAX call...
                data: {listType: listType, listId: listId, sortBy: sortBy, orderBy: orderBy, panelHeader: panelHeader, displayStyle: displayStyle, sidePanel: sidePanel, area:area}, // get the form data
                type: 'POST', // GET or POST
                url: '${g.createLink(controller: 'microsite', action: 'summary')}', // the file to call
                success: function (response) { // on success..
                    $('#summary' + area).html(response);
                }
            });
    }
    
//    called from _mediaArea
    function getSummary(mediaArea, area){
        if(mediaArea){
            var listType = mediaArea.selectorType;
            var listId = mediaArea.selectionId;
            var panelHeader = mediaArea.panelHeader;
            var sortBy = mediaArea.sortBy;
            var orderBy = mediaArea.orderBy;
            var displayStyle = mediaArea.displayStyle;

            $.ajax({ // create an AJAX call...
                data: {listType: listType, listId: listId, sortBy: sortBy, orderBy: orderBy, panelHeader: panelHeader, displayStyle: displayStyle, area:area}, // get the form data
                type: 'POST', // GET or POST
                url: '${g.createLink(controller: 'microsite', action: 'summary')}', // the file to call
                success: function (response) { // on success..
                    $('#summary' + area).html(response);
                }
            });
        }
    }

    //    Area 1 button listeners
    $('.pane1Type').click(function(){
        $('.pane1-tabs > .active').next('li').find('a').trigger('click');
    });
    $('#pane1SpecificLeft').click(function(){
        $('.pane1-tabs > .active').prev('li').find('a').trigger('click');
    });
    $('#pane1SpecificRight').click(function(){
        $('.pane1-tabs > .active').next('li').find('a').trigger('click');
    });
    $('.pane1-sortingLeft').click(function(){
        $('.pane1-tabs > .active').prev('li').find('a').trigger('click');
    });
    $('.pane1-sortingRight').click(function(){
        $('.pane1-tabs > .active').next('li').find('a').trigger('click');
        updateSummary(1, false);
    });
    $('#pane1tab4').click(function(){
        updateSummary(1, false);
    });
    $('.pane1-newList').click(function(){
        $('.pane1-tabs > .active').prev('li').prev('li').prev('li').find('a').trigger('click');
    });

    //    Area 2 button listeners
    $('.pane2Type').click(function(){
        $('.pane2-tabs > .active').next('li').find('a').trigger('click');
    });
    $('#pane2SpecificLeft').click(function(){
        $('.pane2-tabs > .active').prev('li').find('a').trigger('click');
    });
    $('#pane2SpecificRight').click(function(){
        $('.pane2-tabs > .active').next('li').find('a').trigger('click');
    });
    $('.pane2-sortingLeft').click(function(){
        $('.pane2-tabs > .active').prev('li').find('a').trigger('click');
    });
    $('.pane2-sortingRight').click(function(){
        $('.pane2-tabs > .active').next('li').find('a').trigger('click');
        if(${params.controller.equals("carousel")}){
//            no side pane display option
            updateSummary(2, false);
        } else {
            updateSummary(2, true);
        }
    });
    $('#pane2tab4').click(function(){
        if(${params.controller.equals("carousel")}){
//            no side pane display option
            updateSummary(2, false);
        } else {
            updateSummary(2, true);
        }
    });
    $('.pane2-newList').click(function(){
        $('.pane2-tabs > .active').prev('li').prev('li').prev('li').find('a').trigger('click');
    });

    //    Area 3 button listeners
    $('.pane3Type').click(function(){
        $('.pane3-tabs > .active').next('li').find('a').trigger('click');
    });
    $('#pane3SpecificLeft').click(function(){
        $('.pane3-tabs > .active').prev('li').find('a').trigger('click');
    });
    $('#pane3SpecificRight').click(function(){
        $('.pane3-tabs > .active').next('li').find('a').trigger('click');
    });
    $('.pane3-sortingLeft').click(function(){
        $('.pane3-tabs > .active').prev('li').find('a').trigger('click');
    });
    $('.pane3-sortingRight').click(function(){
        $('.pane3-tabs > .active').next('li').find('a').trigger('click');
        if(${params.controller.equals("carousel")}){
//            no side pane display option
            updateSummary(3, false);
        } else {
            updateSummary(3, true);
        }
    });
    $('#pane3tab4').click(function(){
        if(${params.controller.equals("carousel")}){
//            no side pane display option
            updateSummary(3, false);
        } else {
            updateSummary(3, true);
        }

    });
    $('.pane3-newList').click(function(){
        $('.pane3-tabs > .active').prev('li').prev('li').prev('li').find('a').trigger('click');
    });

//    listeners for form section completion

    function getList(mediaArea){
        if(${params.action.equals("edit")}){
            var listType = mediaArea.selectorType;
            var listId = mediaArea.selectionId;
            var mediaAreaValue = mediaArea.areaValue;
            var mediaAreaId = mediaArea.id;

            $.ajax({ // create an AJAX call...
                data: {mediaAreaId:mediaAreaId, listType:listType, listId:listId, mediaAreaValue:mediaAreaValue}, // get the form data
                type: 'POST', // GET or POST
                url: '${g.createLink(controller: 'microsite', action: 'currentList')}', // the file to call
                success: function (response) { // on success..
                    $('#pane' + mediaArea.areaValue + 'ListBody').html(response); // update the DIV
                }
            });
        }
    }

    // media source selection listener
    $('.list-type').click(function(){
        var listType = $(this).attr("data-listType");
        var mediaAreaValue = $(this).attr("data-mediaArea");
        var microSiteId = $(this).attr("data-microSiteId");

        $.ajax({ // create an AJAX call...
            data: {listType:listType, mediaAreaValue:mediaAreaValue, microSiteId:microSiteId}, // get the form data
            type: 'POST', // GET or POST
            url: '${g.createLink(controller: 'microsite', action: 'specificList')}', // the file to call
            success: function (response) { // on success..
                $('#pane' + mediaAreaValue + 'ListBody').html(response); // update the DIV
                updateFormCompletion(mediaAreaValue);
                forceFocusList(mediaAreaValue, listType);
            }
        });
    });

    function forceFocusList(area, listType) {
        switch(listType) {
            case "USER_MEDIA_LIST":document.getElementById("pane" +area + "ListId").focus();break;
            case "COLLECTION":document.getElementById("pane" + area + "LanguageId").focus();break;
            case "TAG":document.getElementById("pane" + area + "TagLanguageId").focus();break;
            case "SOURCE":document.getElementById("pane" + area + "ListId").focus();break;
            case "CAMPAIGN":document.getElementById("pane" + area + "ListId").focus();break;
        }
    }

    //changes form area picture to completed if applicable
    function updateFormCompletion(area){
        if(${params.id == null}) {
            if (document.getElementById('pane' + area + 'ListId').value != "") {
                document.getElementById('check-area' + area).innerHTML = '<img src="${assetPath(src: '/microsite/check.png')}" class="check-image" alt="Section complete, check mark" />';

            } else {
                //change to not complete pic
                document.getElementById('check-area' + area).innerHTML = '';
            }
        }
    }

    $('#title').bind('input propertychange', function(){
        if(document.getElementById('title').value != ""){
            document.getElementById('check-title').innerHTML = '<img src="${assetPath(src: '/microsite/check.png')}" class="check-image" alt="Section complete, check mark" />';
        } else {
            document.getElementById('check-title').innerHTML = '';
        }
    });

    $('#footerText').on('input propertychange', function(){
        if(document.getElementById('footerText').value != ""){
            document.getElementById('check-footer').innerHTML = '<img src="${assetPath(src: '/microsite/check.png')}" class="check-image" alt="Section complete, check mark" />';

        } else {
            //change to not complete pic
            document.getElementById('check-footer').innerHTML = '';
        }
    });


//    model carousel navigation listeners
    var modalFocusTitle;

    $('#carousel-example-generic').on('slid.bs.carousel', function (e) {
        if($('.carousel-inner .item:last').hasClass('active')) {
            $("#left-carousel-button").show();
            $("#right-carousel-button").hide();
            document.getElementById("modal-done-button").className = "pull-right btn btn-submit";
            document.getElementById("step-five").focus();
        } else if($('.carousel-inner .item:first').hasClass('active')){
            $("#left-carousel-button").hide();
            $("#right-carousel-button").show();
            document.getElementById("modal-done-button").className = "pull-right btn btn-submit hide";
            document.getElementById("step-one").focus();
        }
        else {
            $("#left-carousel-button").show();
            $("#right-carousel-button").show();
            document.getElementById("modal-done-button").className = "pull-right btn btn-submit hide";
            var carouselData = $(this).data('bs.carousel');
            var currentIndex = carouselData.getActiveIndex();
            switch(currentIndex) {
                case 1:document.getElementById("step-two").focus();break;
                case 2:document.getElementById("step-three").focus();break;
                case 3:document.getElementById("step-four").focus();break;
            }
        }
    });
    $('.header').on('click', function(){
        $("#left-carousel-button").hide();
        $("#right-carousel-button").show();
        document.getElementById("modal-done-button").className = "pull-right btn btn-submit hide";
        modalFocusTitle = "step-one"

    });
    $('.content').on('click', function(){
        $("#left-carousel-button").show();
        $("#right-carousel-button").show();
        document.getElementById("modal-done-button").className = "pull-right btn btn-submit hide";
        switch(document.activeElement.getAttribute("data-slide-to")) {
            case '1':modalFocusTitle = "step-two";break;
            case '2':modalFocusTitle = "step-three";break;
            case '3':modalFocusTitle = "step-four";break;
        }

    });
    $('.footer').on('click', function(){
        $("#left-carousel-button").show();
        $("#right-carousel-button").hide();
        document.getElementById("modal-done-button").className = "pull-right btn btn-submit";
        modalFocusTitle = "step-five"
    });


// content list listeners
    //collections
    $("#pane1ListBody").on("change",".collection-languages",function(){
        var listType = "COLLECTION";
        var mediaArea = $(this).attr("data-mediaArea");
        var microSiteId = $(this).attr("data-microSiteId");
        var language = $(this).val();

        $.ajax({ // create an AJAX call...
            data: {listType:listType, mediaAreaValue:mediaArea, microSiteId:microSiteId,language:language}, // get the form data
            type: 'POST', // GET or POST
            url: '${g.createLink(controller: 'microsite', action: 'specificList')}', // the file to call
            success: function (response) { // on success..
                $('#pane' + mediaArea + 'ListBody').html(response); // update the DIV
                updateFormCompletion(mediaArea);
                document.getElementById("pane1LanguageId").focus();
            }
        });
    });
    $("#pane2ListBody").on("change",".collection-languages",function(){
        var listType = "COLLECTION";
        var mediaArea = $(this).attr("data-mediaArea");
        var microSiteId = $(this).attr("data-microSiteId");
        var language = $(this).val();

        $.ajax({ // create an AJAX call...
            data: {listType:listType, mediaAreaValue:mediaArea, microSiteId:microSiteId,language:language}, // get the form data
            type: 'POST', // GET or POST
            url: '${g.createLink(controller: 'microsite', action: 'specificList')}', // the file to call
            success: function (response) { // on success..
                $('#pane' + mediaArea + 'ListBody').html(response); // update the DIV
                updateFormCompletion(mediaArea);
                document.getElementById("pane" + mediaArea + "LanguageId").focus();
            }
        });
    });
    $("#pane3ListBody").on("change",".collection-languages",function(){
        var listType = "COLLECTION";
        var mediaArea = $(this).attr("data-mediaArea");
        var microSiteId = $(this).attr("data-microSiteId");
        var language = $(this).val();

        $.ajax({ // create an AJAX call...
            data: {listType:listType, mediaAreaValue:mediaArea, microSiteId:microSiteId,language:language}, // get the form data
            type: 'POST', // GET or POST
            url: '${g.createLink(controller: 'microsite', action: 'specificList')}', // the file to call
            success: function (response) { // on success..
                $('#pane' + mediaArea + 'ListBody').html(response); // update the DIV
                updateFormCompletion(mediaArea);
                document.getElementById("pane" + mediaArea + "LanguageId").focus();
            }
        });
    });

    function submitClick(){
        document.getElementById('micrositeForm').submit();
    }

    $('#modal-header').on('shown.bs.modal', function () {
        document.getElementById(modalFocusTitle).focus();
//        document.getElementById("title").focus();
//        $("#title").focus()
    });

</script>