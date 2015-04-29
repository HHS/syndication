jQuery( document ).ready(function( $ ) {
    $("fieldset#edit-type-mapping .fieldset-wrapper .field_layer").each(function() {
        console.log('found');
        $(this).parent().prev().addClass("form-item-float-left");
        $(this).parent().addClass("form-item-float-right");
    });
});