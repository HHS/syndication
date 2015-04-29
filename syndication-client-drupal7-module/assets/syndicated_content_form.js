function gotoPage( submit_button_id, page_num, sort, order )
{
    jQuery('#syndication_goto_page').val((page_num||0)+1);
    jQuery('#syndication_sort_page').val(sort||'');
    jQuery('#syndication_order_page').val(order||'');
    jQuery("input#"+submit_button_id).trigger('mousedown').trigger('mouseup');
    jQuery('#syndication_goto_page').val('');
}

function subscribeToContent( id )
{
    jQuery('#syndication_media_id').val(id);
    jQuery('input#edit-syndication-search-submit').closest('form').get(0).submit();
    jQuery('#syndication_media_id').val('');
}

var syndication_submit_type = '';
var syndication_search_type = '';

jQuery(function(){
    var container = jQuery('#edit-syndication-browse-types');
    jQuery('.form-type-checkbox',container).each(function(i,div){
        jQuery('input[type="checkbox"]',div).attr('id', jQuery('label',div).attr('for') ); 
    });

    
    gotoPage( 'edit-syndication-ajax-browse-submit' )
});
