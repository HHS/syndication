<?php

function _syndicated_content_lookup_ajax( $form, &$form_state )
{
    module_load_include('php','syndicated_content','includes/syndicated_content.util');

    $syndication = _syndicated_content_api_factory();
    $key = $form_state['values']['syndication_lookup_value'];

    if ( is_numeric($key) )
    {
        $response = $syndication->getMediaById($key);
    } else if ( valid_url($key, true) ) {
        /*
        $len = strlen($syndication->api['tiny_url_source']);
        $tip = substr($key,0,$len);
        if ( !empty($tip) && $len && $tip == $syndication->api['tiny_url_source'] )
        {
            $response = $syndication->getMediaMetadataByTinyUrl($key);
        } else {
            $response = $syndication->getMediaMetadataByUrl($key);
        }
        */
        $response = $syndication->searchMedia($key);
    } else {
        $response = SyndicationResponse();
        $response->status = 400;
        $message  = SyndicationResponse::empty_message;
        $message['errorMessage'] = 'Not A Valid Key';
        $response->message[] = $message;
    }
    /// call search on api
    if ( empty($response->results) )
    {
        if ( !empty($response->message) )
        {
            return "<div id=\"lookup_metadata\" style=\"text-align:center\">No Results Found<br />{$response->message['errorMessage']}</div>";
        } else {
            return "<div id=\"lookup_metadata\" style=\"text-align:center\">No Results Found</div>";
        }
    } else {
        return _syndicated_content_build_metadata_view( $response->results[0] );
    }
}

function _syndicated_content_search_ajax( $form, &$form_state )
{
    module_load_include('php','syndicated_content','includes/syndicated_content.util');

    $per_page = 5;
    if ( !isset($_SESSION['syndication_search']) )
    {
        $_SESSION['syndication_search'] = array(
            'type' => '',
            'reg'  => array('params'=>array('q'=>'')),
            'adv'  => array('params'=>array('max'=>$per_page,'offset'=>0,'sort'=>'','order'=>''))
        );
    }
    $new_page = !empty($form_state['values']['syndication_goto_page']) ? max(intval($form_state['values']['syndication_goto_page']),1) : 1;

    $syndication = _syndicated_content_api_factory();
    $response    = null;

    /// if form was submitted from hidden ajax button vi javascript - reload previous search from session
    if ( isset($form_state['triggering_element']) &&
         isset($form_state['triggering_element']['#value']) &&
               $form_state['triggering_element']['#value'] == 'Ajax Search' )
    {
        if ( !empty($_SESSION['syndication_search']['type']) )
        {
            switch( $_SESSION['syndication_search']['type'] )
            {
                case 'adv':
                    $response = $syndication->getMedia($_SESSION['syndication_search']['adv']['params']);
                    break;
                case 'reg':
                    $response = $syndication->searchMedia($_SESSION['syndication_search']['reg']['params']);
                    break;
            }
        }
    } else
    /// if form was submitted from advanced search tab - new search
    if ( isset($form_state['values']['syndication_search_settings__active_tab']) &&
               $form_state['values']['syndication_search_settings__active_tab'] == 'edit-syndication-adv-search' )
    {
        $params = array(
            'max'    => $per_page,
            'offset' => max( $per_page * ($new_page-1), 0 ),
            'sort'   => '',
            'order'  => ''
        );
        if ( !empty($form_state['values']['syndication_adv_name']) )
        {
            $params['nameContains'] = $form_state['values']['syndication_adv_name'];
        }
        if ( !empty($form_state['values']['syndication_adv_description']) )
        {
            $params['descriptionContains'] = $form_state['values']['syndication_adv_description'];
        }
        if ( !empty($form_state['values']['syndication_adv_source']) )
        {
            $params['sourceUrlContains'] = $form_state['values']['syndication_adv_source'];
            $params['sourceContains']    = $form_state['values']['syndication_adv_source'];
        }
        if ( !empty($form_state['values']['syndication_adv_media_types']) )
        {
            $params['mediaTypes'] = implode(',',$form_state['values']['syndication_adv_media_types']);
        }
        if ( !empty($form_state['values']['syndication_adv_language']) )
        {
            $params['languageValue'] = $form_state['values']['syndication_adv_language'];
        }
        if ( !empty($form_state['values']['syndication_adv_authored_when']) )
        {
            $start = null;
            $end   = null;
            if ( !empty($form_state['values']['syndication_adv_authored_start']) )
            {
                $s = $form_state['values']['syndication_adv_authored_start'];
                $start = _syndicated_content_date( strtotime($s['year']."-".$s['month']."-".$s['day']) );
            }
            if ( !empty($form_state['values']['syndication_adv_authored_between']) )
            {
                $e = $form_state['values']['syndication_adv_authored_between'];
                $end = _syndicated_content_date( strtotime($e['year']."-".$e['month']."-".$e['day']) );
            }
            switch( $form_state['values']['syndication_adv_authored_when'] )
            {
                case 'on':
                    $params['dateContentAuthored'] = $start;
                    break;
                case 'since':
                    $params['authoredSinceDate'] = $start;
                    break;
                case 'before':
                    $params['authoredBeforeDate'] = $start;
                    break;
                case 'between':
                    $params['authoredInRange'] = "$start,$end";
                    break;
            }
        }
        if ( !empty($form_state['values']['syndication_adv_updated_when']) )
        {
            $updated_start = null;
            $updated_end   = null;
            if ( !empty($form_state['values']['syndication_adv_updated_start']) )
            {
                $s = $form_state['values']['syndication_adv_updated_start'];
                $updated_start = _syndicated_content_date( strtotime($s['year']."-".$s['month']."-".$s['day']) );
            }
            if ( !empty($form_state['values']['syndication_adv_updated_between']) )
            {
                $e = $form_state['values']['syndication_adv_updated_between'];
                $updated_end = _syndicated_content_date( strtotime($e['year']."-".$e['month']."-".$e['day']) );
            }
            switch( $form_state['values']['syndication_adv_updated_when'] )
            {
                case 'on':
                    $params['dateContentUpdated'] = $start;
                    break;
                case 'since':
                    $params['updatedSinceDate'] = $start;
                    break;
                case 'before':
                    $params['updatedBeforeDate'] = $start;
                    break;
                case 'between':
                    $params['updatedInRange'] = "$start,$end";
                    break;
            }
        }
        $response = $syndication->getMedia($params);

    } else
    /// if form was submitted from regular search tab - new search
    if ( isset($form_state['values']['syndication_search_settings__active_tab']) &&
               $form_state['values']['syndication_search_settings__active_tab'] == 'edit-syndication-reg-search' )
    {
        /// we are using regular search
        if ( !empty($form_state['values']['syndication_search_value']) )
        {
            $response = $syndication->searchMedia($form_state['values']['syndication_search_value']);
        }
    }
    if ( empty($response) || empty($response->results) )
    {
        return "<div id=\"search_metadata\" style=\"text-align:center\">No Results Found</div>";
    }

    $pagination = array();
    /// add in sorting stuff to pagination here?
    return '<div id="search_metadata">'. _syndicated_content_build_metadata_table_view( 'edit-syndication-ajax-search-submit', $response->results, $pagination ) .'</div>';
}

function _syndicated_content_browse_ajax( $form, &$form_state )
{
    module_load_include('php','syndicated_content','includes/syndicated_content.util');

    $per_page = 5;
    $params   = array(
        'max'=>$per_page, 'offset'=>0, 'sort'=>'', 'order'=>'', 'mediaType'=>'Html,Image,Infographic,Video'
    );
    $new_page = !empty($form_state['values']['syndication_goto_page']) ? max(intval($form_state['values']['syndication_goto_page']),1) : 1;
    $params['offset'] = max( $per_page * ($new_page-1), 0 );

    if ( !empty($form_state['values']['syndication_sort_page']) ) {
        $params['sort']  = $form_state['values']['syndication_sort_page'];
    }
    if ( !empty($form_state['values']['syndication_order_page']) ) {
        $params['order']  = $form_state['values']['syndication_sort_page'];
    }

    /// grab list of syndication_types
    $syndication = _syndicated_content_api_factory();
    $response    = null;

    /// if form was submitted from hidden ajax button vi javascript - reload previous search from session
    if ( isset($form_state['triggering_element']) &&
         isset($form_state['triggering_element']['#value']) &&
               $form_state['triggering_element']['#value'] == 'Browse' )
    {
        $response = $syndication->getMedia($params);
    }
    if ( empty($response) || empty($response->results) )
    {
        return "<div id=\"browse_metadata\" style=\"text-align:center\">No Results Found</div>";
    }
    /// add in sorting stuff to pagination here?
    return '<div id="browse_metadata">'. _syndicated_content_build_metadata_table_view( 'edit-syndication-ajax-browse-submit', $response->results, $response->pagination ) .'</div>';
}
