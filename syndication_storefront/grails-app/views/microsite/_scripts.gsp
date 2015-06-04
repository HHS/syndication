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
                data: {listType: listType, listId: listId, sortBy: sortBy, orderBy: orderBy, panelHeader: panelHeader, displayStyle: displayStyle, sidePanel: sidePanel}, // get the form data
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
                data: {listType: listType, listId: listId, sortBy: sortBy, orderBy: orderBy, panelHeader: panelHeader, displayStyle: displayStyle}, // get the form data
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
            }
        });
    });

    //changes form area picture to completed if applicable
    function updateFormCompletion(area){
        if(document.getElementById('pane' + area + 'ListId').value != ""){
            document.getElementById('check-area' + area).hidden = ""

        } else {
            //change to not complete pic
            document.getElementById('check-area' + area).hidden = "hidden"
        }
    }

    $('#title').on('input', function(){
        if(document.getElementById('title').value != ""){
            document.getElementById('check-title').hidden = ""

        } else {
            //change to not complete pic
            document.getElementById('check-title').hidden = "hidden"
        }
    });

    $('#footerText').change(function(){
        if(document.getElementById('footerText').value != ""){
            document.getElementById('check-footer').hidden = ""

        } else {
            //change to not complete pic
            document.getElementById('check-footer').hidden = "hidden"
        }
    });


//    model carousel navigation listeners
    $('#carousel-example-generic').on('slid.bs.carousel', function () {
        if($('.carousel-inner .item:last').hasClass('active')) {
            document.getElementById("left-carousel-button").hidden = "";
            document.getElementById("right-carousel-button").hidden = "hidden";
            document.getElementById("modal-done-button").className = "pull-right btn btn-submit";
        } else if($('.carousel-inner .item:first').hasClass('active')){
            document.getElementById("left-carousel-button").hidden = "hidden";
            document.getElementById("right-carousel-button").hidden = "";
            document.getElementById("modal-done-button").className = "pull-right btn btn-submit hide";
        }
        else {
            document.getElementById("left-carousel-button").hidden = "";
            document.getElementById("right-carousel-button").hidden = "";
            document.getElementById("modal-done-button").className = "pull-right btn btn-submit hide";
        }
    });
    $('.header').on('click', function(){
        document.getElementById("left-carousel-button").hidden = "hidden";
        document.getElementById("right-carousel-button").hidden = "";
        document.getElementById("modal-done-button").className = "pull-right btn btn-submit hide";
    });
    $('.content').on('click', function(){
        document.getElementById("left-carousel-button").hidden = "";
        document.getElementById("right-carousel-button").hidden = "";
        document.getElementById("modal-done-button").className = "pull-right btn btn-submit hide";
    });
    $('.footer').on('click', function(){
        document.getElementById("left-carousel-button").hidden = "";
        document.getElementById("right-carousel-button").hidden = "hidden";
        document.getElementById("modal-done-button").className = "pull-right btn btn-submit";
    });

</script>