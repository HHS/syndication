jQuery( document ).ready(function( $ ) {
    $("fieldset#edit-type-mapping .fieldset-wrapper .field_layer").each(function() {
        $(this).parent().prev().addClass("form-item-float-left");
        $(this).parent().addClass("form-item-float-right");
    });
    $("textarea[id^='edit-key']").parent().parent().hide();
    
    $("fieldset#edit-api-identiy legend span").append($("<span> - </span><a href='#' id='quickAdd'>Quick Add</a>"));
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