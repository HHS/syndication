$(document).ready(function(){
    loadAdminErrorLog()

    initButtonListeners()
})

function initButtonListeners(){
    //Tabs ------------------------------------------------------------------------------
    $('#adminTab').click(function(){
        loadAdminErrorLog()
        toggleAdminPill($('#adminErrorButton'))
    })

    $('#adminlogDownload').click(function(){
        var replacedHref=$("#adminlogDownload").parent('a').attr("href").replace( /(downloadFor=)[\d\D]+/ig, '$1'+$('#currentToken').val());
        $("#adminlogDownload").parent('a').attr("href", replacedHref);

    })

    $('#apiTab').click(function(){
        loadApiErrorLog()
        toggleApiPill($('#apiErrorButton'))
    })
    $('#cmsTab').click(function(){
        loadCmsLog()
        toggleCmsPill($('#cmsLogButton'))
    })
    $('#storefrontTab').click(function(){
        loadStorefrontErrorLog()
        toggleStorefrontPill($('#storefrontErrorButton'))
    })
    $('#tinyTab').click(function(){
        loadTinyErrorLog()
        toggleTinyPill($('#tinyErrorButton'))
    })
    $('#tagTab').click(function(){
        loadTagErrorLog()
        toggleTagPill($('#tagErrorButton'))
    })

    //Admin -----------------------------------------------------------------------------
    $('#adminFullscreenButton').click(function(){
        $('#adminLogDisplay').removeClass("logViewPanelConstrained")
    })

    $('#adminConstrainedScreenButton').click(function(){
        $('#adminLogDisplay').addClass("logViewPanelConstrained")
    })

    //API -----------------------------------------------------------------------------
    $('#apiFullscreenButton').click(function(){
        $('#apiLogDisplay').removeClass("logViewPanelConstrained")
    })

    $('#apiConstrainedScreenButton').click(function(){
        $('#apiLogDisplay').addClass("logViewPanelConstrained")
    })

    $('#apiErrorButton').click(function(){
        loadApiErrorLog()
        toggleApiPill($(this))
    })

    $('#apiInfoButton').click(function(){
        loadApiInfoLog()
        toggleApiPill($(this))
    })

    function toggleApiPill(pill){
        $('.apiPill').removeClass("active")
        pill.parent().addClass("active")
    }

    //CMS Manager -----------------------------------------------------------------------------
    $('#cmsFullscreenButton').click(function(){
        $('#cmsLogDisplay').removeClass("logViewPanelConstrained")
    })

    $('#cmsConstrainedScreenButton').click(function(){
        $('#cmsLogDisplay').addClass("logViewPanelConstrained")
    })

    $('#cmsLogButton').click(function(){
        loadCmsLog()
        toggleCmsPill($(this))
    })

    $('#cmsApiKeyLogButton').click(function(){
        loadCmsApiKeyLog()
        toggleCmsPill($(this))
    })

    function toggleCmsPill(pill){
        $('.cmsPill').removeClass("active")
        pill.parent().addClass("active")
    }

    //Storefront -----------------------------------------------------------------------------
    $('#storefrontFullscreenButton').click(function(){
        $('#storefrontLogDisplay').removeClass("logViewPanelConstrained")
    })

    $('#storefrontConstrainedScreenButton').click(function(){
        $('#storefrontLogDisplay').addClass("logViewPanelConstrained")
    })

    $('#storefrontErrorButton').click(function(){
        loadStorefrontErrorLog()
        toggleStorefrontPill($(this))
    })

    $('#storefrontInfoButton').click(function(){
        loadStorefrontInfoLog()
        toggleStorefrontPill($(this))
    })

    function toggleStorefrontPill(pill){
        $('.storefrontPill').removeClass("active")
        pill.parent().addClass("active")
    }

    //TinyURL -----------------------------------------------------------------------------
    $('#tinyFullscreenButton').click(function(){
        $('#tinyLogDisplay').removeClass("logViewPanelConstrained")
    })

    $('#tinyConstrainedScreenButton').click(function(){
        $('#tinyLogDisplay').addClass("logViewPanelConstrained")
    })

    $('#tinyErrorButton').click(function(){
        loadTinyErrorLog()
        toggleTinyPill($(this))
    })

    $('#tinyInfoButton').click(function(){
        loadTinyInfoLog()
        toggleTinyPill($(this))
    })

    function toggleTinyPill(pill){
        $('.tinyPill').removeClass("active")
        pill.parent().addClass("active")
    }

    //TagCloud -----------------------------------------------------------------------------
    $('#tagFullscreenButton').click(function(){
        $('#tagLogDisplay').removeClass("logViewPanelConstrained")
    })

    $('#tagConstrainedScreenButton').click(function(){
        $('#tagLogDisplay').addClass("logViewPanelConstrained")
    })

    $('#tagErrorButton').click(function(){
        loadTagErrorLog()
        toggleTagPill($(this))
    })

    $('#tagInfoButton').click(function(){
        loadTagInfoLog()
        toggleTagPill($(this))
    })

    function toggleTagPill(pill){
        $('.tagPill').removeClass("active")
        pill.parent().addClass("active")
    }


}

function loadAdminErrorLog(){
    loadLog("adminErrorLog", $('#adminLogDisplay'), $('#pageNumberforAdmin'))
}

function loadApiInfoLog(){
    loadLog("apiInfoLog", $('#apiLogDisplay'))
}

function loadApiErrorLog(){
    loadLog("apiErrorLog", $('#apiLogDisplay'))
}

function loadCmsLog(){
    loadLog("cmsLog", $('#cmsLogDisplay'))
}

function loadCmsApiKeyLog(){
    loadLog("cmsApiKeyLog", $('#cmsLogDisplay'))
}

function loadStorefrontInfoLog(){
    loadLog("storefrontInfoLog", $('#storefrontLogDisplay'))
}

function loadStorefrontErrorLog(){
    loadLog("storefrontErrorLog", $('#storefrontLogDisplay'))
}

function loadTinyInfoLog(){
    loadLog("tinyInfoLog", $('#tinyLogDisplay'))
}

function loadTinyErrorLog(){
    loadLog("tinyErrorLog", $('#tinyLogDisplay'))
}

function loadTagInfoLog(){
    loadLog("tagInfoLog", $('#tagLogDisplay'))
}

function loadTagErrorLog(){
    loadLog("tagErrorLog", $('#tagLogDisplay'))
}

function loadLog(name, logWindow, pageWindow){
    $.getJSON(name, function(data){
        logWindow.html(data.logData)
        if(data.pageNextBack){
            pageWindow.html(data.pageNextBack)
        }

        $('#pageNumberforAdmin a').click(function(e) {
            e.preventDefault();
            loadLog('adminErrorLog?firstForwardToken='+data.firstForwardToken+'&token='+$(this).data("token"),$('#adminLogDisplay'), $('#pageNumberforAdmin'))
        })
        logWindow.scrollTop(logWindow[0].scrollHeight);

    })

}