function now() {
    return moment().format("h:mm:ss a");
}

var tagCloudAddress = "http://localhost:8084/TagCloud/statusCheck";
var tinyUrlAddress = "http://localhost:8082/TinyUrl/statusCheck";
var syndicationAddress = "http://localhost:8080/Syndication/admin/statusCheck";
    var syndicationTinyUrlStatus = "http://localhost:8080/Syndication/statusCheck/tinyUrlStatus"
    var syndicationTagCloudStatus = "http://localhost:8080/Syndication/statusCheck/tagCloudStatus"
var syndicationadminAddress = "http://localhost:8086/SyndicationAdmin/statusCheck";
var storefrontAddress = "http://localhost:8088/SyndicationStorefront/statusCheck";
var cmsManagerAddress = "http://localhost:8088/CMSManager/statusCheck";

var subcomponents = {
    syndicationPanel:{
        'syndicationTinyUrlStatus':syndicationTinyUrlStatus,
        'syndicationTagCloudStatus':syndicationTagCloudStatus
    }
};

var componentNames = {
    'syndicationTinyUrlStatus':'Tiny Url server',
    'syndicationTagCloudStatus':'Tag Cloud server'
}

var serviceCount = 6;

$(Document).ready(function () {
    function setFail (panel){
        setStatus(panel, "danger", "Unreachable!", "<span class='glyphicon glyphicon-warning-sign'></span>");
    }

    function setSuccess (panel){
        setStatus(panel, "success", "All services are go.", "<span class='glyphicon glyphicon-ok-sign'></span>");
    }

    function setWarning (panel){
        setStatus(panel, "warning", "Some services down.", "<span class='glyphicon glyphicon-exclamation-sign'></span>");
    }

    function setPending (panel){
        setStatus(panel, "info", "Checking...", "glyphicon glyphicon-question-sign");
    }

    function setStatus(panel, status, message, symbol){
        var infoBox = panel.find(".statusMessage")
        if(status == "info"){
            panel.find(".spinner").toggle(true);
        } else{
            panel.find(".spinner").toggle(false);
        }

        infoBox.html('<span><span class="text-'+status+'">'+symbol+'</span> '+message+'</span><button type="button" class="btn btn-xs btn-'+status+'" style="float:right">Info</button>');
        panel.removeClass('panel-info panel-success panel-danger panel-warning').
            addClass('panel-'+status);
    }

    function statusCheck(url, panel, worker) {
        $.ajax({
            url: url,
            success: function (data) {
                if (data === "roger") {
                    setSuccess(panel);
                } else if(data === "partial"){
                    setWarning(panel);
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                setFail(panel);
            },
            complete: function () {
                updateOverview();
                updateSystemStatusBarWorker();
                setTimeout(worker, 1000);
            }
        });
    }

    function subStatus(url, panel, componentName){
        $.ajax({
            url: url,
            success: function (data) {
                if (data != "roger") {
                    panel.append('<div class="panel panel-danger"><span class="glyphicon glyphicon-exclamation-sign text-danger pad5"></span><span>'+ componentName +' unreachable.</span></div>')
                } else{
                    panel.append('<div class="panel panel-success"><span class="glyphicon glyphicon-ok-sign text-success pad5"></span><span>'+ componentName +' working.</span></div>')
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                setWarning(panel);
            }
        });
    }

    (function syndicationWorker() {
        var syndPanel = $('#syndicationPanel');
        statusCheck(syndicationAddress,
            syndPanel,
            syndicationWorker);
    })();

    (function tinyUrlWorker() {
        statusCheck(tinyUrlAddress,
            $('#tinyUrlPanel'),
            tinyUrlWorker);
    })();

    (function tagCloudWorker() {
        statusCheck(tagCloudAddress,
            $('#tagCloudPanel'),
            tagCloudWorker);
    })();

    (function syndicationAdminWorker() {
        statusCheck(syndicationadminAddress,
            $('#syndicationAdminPanel'),
            syndicationAdminWorker);
    })();

    (function storefrontWorker() {
        statusCheck(storefrontAddress,
            $('#storefrontPanel'),
            storefrontWorker);
    })();

    (function cmsManagerWorker() {
        statusCheck(cmsManagerAddress,
            $('#cmsManagerPanel'),
            cmsManagerWorker);
    })();

    function updateSystemStatusBarWorker() {
        var successPercent = $(".panel-success.main-panel").size() / serviceCount * 100;
        var pendingPercent = $(".panel-info.main-panel").size() / serviceCount * 100;
        var warningPercent = $(".panel-warning.main-panel").size() / serviceCount * 100;
        var errorPercent   = $(".panel-danger.main-panel").size() / serviceCount * 100;

        $('#systemStatusBar').html(
            '<div class="progress-bar progress-bar-success"     style="width: '+ successPercent +'%"></div>' +
                '<div class="progress-bar progress-bar-info"    style="width: '+ pendingPercent +'%"></div>' +
                '<div class="progress-bar progress-bar-warning" style="width: '+ warningPercent +'%"></div>' +
                '<div class="progress-bar progress-bar-danger"  style="width: '+ errorPercent   +'%"></div>'
        );
    }

    function updateOverview() {
        var errorCount = $(".panel-danger.main-panel").size();
        var warningCount = $(".panel-warning.main-panel").size()
        var pendingCount = $(".panel-info.main-panel").size();
        var runningCount = $(".panel-success.main-panel").size();
        var html = 'Syndication Status - ' + runningCount + '/6' + ' services fully operational.'
        if (errorCount == 6) {
            $("#bannerMessage").removeClass('text-info text-warning text-success').
                addClass('text-danger').
                html(html)
        } else if (errorCount > 0 || warningCount > 0) {
            $("#bannerMessage").removeClass('text-info text-danger text-success').
                addClass('text-warning').
                html(html)
        } else {
            $("#bannerMessage").removeClass('text-info text-danger text-warning').
                addClass('text-success').
                html(html)
        }
    }

    //event handler for button click
    $(document).on('click', '.btn', function () {
        var panel = $(this).parent().parent().parent()
        var footer = panel.find('.panel-footer');

        if(footer.is(":visible")){
            footer.slideToggle('fast');
            return;
        }

        footer.html('');
        panel.find(".spinner").toggle(true);

        var panelName = panel.attr("id");
        var subComps = subcomponents[panelName];

        for(var sc in subComps){
            subStatus(subComps[sc], footer, componentNames[sc]);
        }

        setTimeout(function(){
            footer.slideToggle('fast');
            panel.find(".spinner").toggle(false);
        }, 500);

//		var id = $(this).attr('id').split("_")[1];
//		$.ajax({
//		     url: 'http://localhost:8080/Syndication/api/v1/resources/media/'+id+'/content?imageFloat=left&imageMargin=0,10,10,0',
//		     // url: 'json/data.json',
//		     type: "GET",
//		     dataType: 'jsonp',
//		     success: function(data) {
//		     	$('#dialog').html(data.content);
//		     },
//		     error: function(a, errorString, c) {
//		     	alert(errorString);
//		     }
//		});

//        $("#dialog").load(template)
//            .dialog({
//                autoOpen: true,
//                show: {
//                    effect: "clip",
//                    duration: 150
//                },
//                hide: {
//                    effect: "clip",
//                    duration: 150
//                },
//                modal: true,
//                width: Math.min($(window).width() * 0.8, 640),
//                height: Math.min($(window).height() * 0.8, 480)
//            });
    });
});