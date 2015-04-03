function now() {
    return moment().format("h:mm:ss a");
}

// var context = "local"
var context = "prod"

var tagCloudAddress
var tinyUrlAddress
var syndicationAddress
    var syndicationTinyUrlStatus
    var syndicationTagCloudStatus
var syndicationadminAddress
var storefrontAddress
var cmsManagerAddress

if(context === "prod"){
  tagCloudAddress               = "https://tagcloud.digitalmedia.hhs.gov/statusCheck";
  tinyUrlAddress                = "https://tiny.hhs.gov/statusCheck";
  syndicationAddress            = "https://api.digitalmedia.hhs.gov/statusCheck";
      syndicationTinyUrlStatus  = "https://api.digitalmedia.hhs.gov/statusCheck/tinyUrlStatus"
      syndicationTagCloudStatus = "https://api.digitalmedia.hhs.gov/statusCheck/tagCloudStatus"
  syndicationadminAddress       = "https://syndicationadmin.digitalmedia.hhs.gov/statusCheck";
  storefrontAddress             = "https://digitalmedia.hhs.gov/statusCheck";
  cmsManagerAddress             = "https://cmsmanager.digitalmedia.hhs.gov/statusCheck";
} else{
  tagCloudAddress               = "http://localhost:8084/TagCloud/statusCheck";
  tinyUrlAddress                = "http://localhost:8088/TinyUrl/statusCheck";
  syndicationAddress            = "http://localhost:8080/Syndication/statusCheck";
    syndicationTinyUrlStatus    = "http://localhost:8080/Syndication/statusCheck/tinyUrlStatus"
    syndicationTagCloudStatus   = "http://localhost:8080/Syndication/statusCheck/tagCloudStatus"
  syndicationadminAddress       = "http://localhost:8086/SyndicationAdmin/statusCheck";
  storefrontAddress             = "http://localhost:8082/SyndicationStorefront/statusCheck";
  cmsManagerAddress             = "http://localhost:9090/CmsManager/statusCheck";
}

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
var refreshTime = 10000;

function setFail (panel){
    setStatus(panel, "danger", "Unreachable!", "<i class='fa fa-exclamation-triangle fa-fw'></i>");
}

function setSuccess (panel){
    setStatus(panel, "success", "No problems detected.", "<i class='fa fa-check fa-fw'></i>");
}

function setWarning (panel){
    setStatus(panel, "warning", "Some services down.", "<i class='fa fa-exclamation-triangle fa-fw'></i>");
}

function setPending (panel){
    setStatus(panel, "info", "Checking...", "<i class='fa fa-circle-o-notch fa-spinning fa-fw'></i>");
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

function getStatus(url, panel, worker) {
    $.getJSON(url + "?callback=?", function(data){
        console.log(data)
        if (data.running === "roger") {
            setSuccess(panel);
        } else if(data.running === "partial"){
            setWarning(panel);
        }
      })
      .fail(function(){
        console.log("error")
        setFail(panel);
      })
      .done(function(){
      })
      .always(function(){
        updateOverview();
        updateSystemStatusBarWorker();
        setTimeout(worker, refreshTime);
      })
}

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

function subStatus(url, panel, componentName){
    $.ajax({
        url: url,
        success: function (data) {
            if (data != "roger") {
                panel.append('<div class="panel panel-danger"><i class="fa fa-exclamation-triangle fa-fw"></i><span>'+ componentName +' unreachable.</span></div>')
            } else{
                panel.append('<div class="panel panel-success"><span class="glyphicon glyphicon-ok-sign text-success pad5"></span><span>'+ componentName +' working.</span></div>')
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            setWarning(panel);
        }
    });
}

$(Document).ready(function () {
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
    });

    (function syndicationWorker() {
        var syndPanel = $('#syndicationPanel');
        getStatus(syndicationAddress,
            syndPanel,
            syndicationWorker);
    })();

    (function tinyUrlWorker() {
        getStatus(tinyUrlAddress,
            $('#tinyUrlPanel'),
            tinyUrlWorker);
    })();

    (function tagCloudWorker() {
        getStatus(tagCloudAddress,
            $('#tagCloudPanel'),
            tagCloudWorker);
    })();

    (function syndicationAdminWorker() {
        getStatus(syndicationadminAddress,
            $('#syndicationAdminPanel'),
            syndicationAdminWorker);
    })();

    (function storefrontWorker() {
        getStatus(storefrontAddress,
            $('#storefrontPanel'),
            storefrontWorker);
    })();

    (function cmsManagerWorker() {
        getStatus(cmsManagerAddress,
            $('#cmsManagerPanel'),
            cmsManagerWorker);
    })();
});
