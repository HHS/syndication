jQuery( document ).ready(function( $ ) {
    $("fieldset#edit-type-mapping .fieldset-wrapper .field_layer").each(function() {
        $(this).parent().prev().addClass("form-item-float-left");
        $(this).parent().addClass("form-item-float-right");
    });
    var fields = $("fieldset#edit-type-mapping .form-item-float-left").get();
    for(var i = 0; i < fields.length; i++) {
        $(fields[i]).before($("<div id='synd_couple_" + i + "' class='synd_couple'></div>"));
        var nxtItem = $(fields[i]).next();
        $("#synd_couple_" + i).append($(fields[i]).detach());
        if(nxtItem.length > 0)
            $("#synd_couple_" + i).append(nxtItem.detach());
    }
	var fields = $("fieldset#edit-type-mapping .form-type-select").get();
	for(var i = 0; i < fields.length; i++) {
		if(!$(fields[i]).hasClass("form-item-float-left") && !$(fields[i]).hasClass("form-item-float-right")) {
			$(fields[i]).before($("<div id='synd_single_" + i + "' class='synd_couple'></div>"));
			$("#synd_single_" + i).append($(fields[i]).detach());
		}	
	}
    $("textarea[id^='edit-key']").parent().parent().hide();
    
    if($("fieldset#edit-api-identiy legend span").length)
    $("fieldset#edit-api-identiy legend span").append($("<span> - </span><a href='#' id='quickAdd'>Quick Add</a>"));
    if($("fieldset#edit-api-identiy legend div").length)
        $("fieldset#edit-api-identiy legend div").append($("<span> - </span><a href='#' id='quickAdd'>Quick Add</a>"));
    $("#quickAdd").click(function() {
        if($(this).html() == "Quick Add") {
            $("input[id^='edit-key']").parent().hide();
            $("textarea[id^='edit-key']").parent().parent().show();
            $(this).html("View Keys");
        } else {
            $("input[id^='edit-key']").parent().show();
            $("textarea[id^='edit-key']").parent().parent().hide();
            $(this).html("Quick Add");
        }
        return false;
    });
});