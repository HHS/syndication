function syndicationAction(action)
{
    jQuery('#syndication_action').val(action);
    jQuery(this).closest('form').get(0).submit();
    return false;
}
