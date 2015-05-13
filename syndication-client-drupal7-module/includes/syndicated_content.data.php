<?php

/// BEFORE NODE SAVE - we need to this run before any other actions take place, so I choose hook_validate over hook_presave
/// this catches and syndication specific actions before regular form actions(edit|save|delete) are processed
function _syndicated_content_node_validate( $node, $form, &$form_state )
{
    module_load_include('php','syndicated_content','includes/syndicated_content.util');
    if ( empty($form['type']) || empty($form['type']['#value']) || empty($node->nid) ) { return; }
    
    // Find the drupal content type of the node we are editing
    // pull a list of syndicatable-content-types from module tables
    $syndicatable_content_types = _syndicated_content_type_mapping();
    // is this node_type syndicatable?
    
    //if ( !empty($syndicatable_content_types['by_drupal_type'][$form['type']['#value']]) && !empty($form_state['values']['syndication_action']) )
    if ( !empty($syndicatable_content_types['by_drupal_type'][$form['type']['#value']]) )
    {
        /// check for a syndication_action form field - means we want a spcial syndication action as opposed to a standard save
        $syndication_action = $form_state['values']['syndication_action'];

        /// these are one time actions - reset this so nothing else will see this later in the same request
        $form_state['values']['syndication_action'] = '';
        $form_state['input']['syndication_action']  = '';
        
        switch( $syndication_action )
        {
            case '': break;
            case 'publish':
                $language_id = _syndicated_content_find_language_id( $form_state['values']['syndication_publish_language'] );
                
                $tagArr = isset($form_state['values']['field_tags'][$node->language]) ? $form_state['values']['field_tags'][$node->language] : array();
                
                $params = array(
                    //'name'         => $form_state['values']['syndication_publish_title'],
                    'name'         => $form_state['values']['title'],
                    'language'     => $language_id,
                    'tagNames'     => _syndicated_content_get_tags($tagArr),
                );
                
                _syndicated_content_update($node, $params, true);
                break;
            case 'unpublish':
                /// need syndication_media_id of current node
                $drupaldata = db_query("SELECT * FROM {syndicated_content} WHERE node_id=:node",array(
                    'node'   => $node->nid
                ))->fetchObject();
                /// this node is not subscribed to anything
                if ( empty($drupaldata) )
                {
                    form_set_error('syndication_action', t('This node is not subscribed to any syndicated content.'));
                    return;
                } else {
                    if ( empty($drupaldata->locally_owned) )
                    {
                        form_set_error('syndication_action', t('You cannot unpublish content you do not own.'));
                        return;
                    }
                    //// syndication doesn't actually unpublish anything atm
                    $num_deleted = db_delete('syndicated_content')
                        ->condition('node_id', $node->nid, '=')
                        ->execute();
                    drupal_set_message('UnPublished From Syndication Source.');
                    /*
                    $syndication = _syndicated_content_api_factory();
                    $response = $syndication->unPublishMediaById($drupaldata->media_id);
                    if ( $response->success )
                    {
                        $num_deleted = db_delete('syndicated_content')
                                     ->condition('node_id', $node->nid, '=')
                                     ->execute();
                        drupal_set_message('UnPublished From Syndication Source.');
                    } else {
                        drupal_set_message('Failed to UnPublish.');
                    }
                    */
                }
                _syndicated_content_stop_destination($node);
                break;
            case 'update':
                /// is this node subscribed to anything?
                $drupaldata = db_query("SELECT * FROM {syndicated_content} WHERE node_id=:node",array(
                    'node'   => $node->nid
                ))->fetchObject();
                /// this node is not subscribed to anything
                if ( empty($drupaldata) )
                {
                    form_set_error('syndication_action', t('This node is not subscribed to any syndicated content.'));
                    return;
                } else {
                    $local_url = url('node/'.$drupaldata->node_id);
                    if ( !empty($drupaldata->locally_owned) )
                    {
                        form_set_error('syndication_action', t('You cannot update locally owned content. You may only publish changes.'));
                        return;
                    }
                }
                /// is this node subscribed to anything?
                $drupal_metadata = _syndicated_content_get_metadata_for_node( $node );
                /// this node is not subscribed to anything
                if ( empty($drupal_metadata) )
                {
                    form_set_error('syndication_action', t('This node is not subscribed to any syndicated content.'));
                    return;
                } else {
                    if ( !empty($drupal_metadata['locally_owned']) )
                    {
                        form_set_error('syndication_action', t('You cannot update locally owned content. You may only publish changes.'));
                        return;
                    }
                }

                $updated = _syndicated_content_update_node($node);
                if ( $updated )
                {
                    drupal_set_message(t('Updated content from syndication.'));
                } else if ( $updated===false ) { // bad touch
                    drupal_set_message(t('Failed to updated content from syndication.'));
                }
                break;
            case 'unsubscribe':
                $num_deleted = db_delete('syndicated_content')
                                 ->condition('node_id', $node->nid, '=')
                                 ->execute();
                if ( $num_deleted )
                {
                    drupal_set_message('Unsubscribed from syndicated content');
                }
                _syndicated_content_stop_destination($node);
                break;
            default:
                drupal_set_message('Unsupported syndication action requested: '.$syndication_action);
                _syndicated_content_stop_destination($node);
                break;
        }
        /*
        unset($_GET['destination']);
        unset($_REQUEST['destination']);
        drupal_goto("/node/{$node->nid}/edit");
        */
    }
    /// continue
}

function _syndicated_content_stop_destination($node) {
    unset($_GET['destination']);
    unset($_REQUEST['destination']);
    drupal_goto("/node/{$node->nid}/edit");
}

/// AFTER NODE SAVE - also track metadata
function _syndicated_content_node_insert( $node )
{
    module_load_include('php','syndicated_content','includes/syndicated_content.util');

    /// if this node came through subscribe than it will already be ...
    if ( isset($node->syndication_media_id) /// saved to the db and have a nid
         && !empty($node->nid)              /// marked as new
         && !empty($node->is_new) )         /// stamped with a syndication_media_id
    {
        /// the create for already pulled this from syndication
        /// but it didn't come across in the form, we should have cached it somewhere
        $syndication = _syndicated_content_api_factory();
        $response = $syndication->getMediaById($node->syndication_media_id);
        if ( $response->success && !empty($response->results) )
        {
            /// subscribe to content - not implemented serverside
            //$subscribe_response = $syndication->subscribeById($node->syndication_media_id);
            //if ( !$subscribe_response->success )
            //{
            //    drupal_set_message('Failed to Subscribe to this content ('.$node->syndication_media_id.').');
            //}
            return _syndicated_content_save_metadata_for_node( $node, array_shift($response->results) );
        }
    }
    return false;
}

/// CREATE HANDLER - subscribe handler
function _syndicated_content_form_submit( $form, &$form_state )
{
    global $user;
    if ( in_array($form['#form_id'],array('syndicated_content_node_form')) )
    {
        if ( empty($form_state['values']['syndication_media_id']) )
        {
            /// return to new syndicated content screen
            /// can we return to the same state we were in before?
            drupal_set_message(t('No Syndicated Content Found'));
            return;
        }

        /// are we already tracking something with this ID locally?
        $syndication_media_id = $form_state['values']['syndication_media_id'];
        $drupaldata = db_query("SELECT * FROM {syndicated_content} WHERE syndication_source_id=:syndication_source_id AND media_id=:media_id",array(
            'syndication_source_id' => _syndicated_content_get_source_id(),
            'media_id'  => $syndication_media_id
        ))->fetchObject();

        if ( $drupaldata )
        {
            $local_url = url('node/'.$drupaldata->node_id);
            if ( !empty($drupaldata->locally_owned) )
            {
                drupal_set_message("You cannot ingest locally authored content (<a href=\"{$local_url}\">{$local_url}</a>)");
            } else {
                drupal_set_message("You are already tracking this content (<a href=\"{$local_url}\">{$local_url}</a>)");
            }
            return;
        }
        $drupal_type = '';
        if ( !empty($form_state['values']['syndicated_content_local_type']) )
        {
            $drupal_type = $form_state['values']['syndicated_content_local_type'];
        }
        drupal_goto("/node/add/{$drupal_type}",array('query'=>array(
            'syndication_media_id' => $syndication_media_id
        )));

        /// do not add directly anymore, simple prefill the standard form
        /*
        /// request full content from syndication
        $syndication = _syndicated_content_api_factory();

        $response = $syndication->getMediaMetadataByMediaId($syndication_media_id);
        if ( ! $response->success )
        {
            /// return to new syndicated content screen
            drupal_set_message(t('No Syndicated Content Found.'));
            return;
        }

        $metadata = empty($response->results)?null:array_shift($response->results);
        $title    = empty($metadata['name'])?'Title':$metadata['name'];

        $response = $syndication->getMediaContentByMediaId($syndication_media_id);
        $content  = empty($response->results)?null:$response->results;

        if ( empty($content) )
        {
            /// return to new syndicated content screen
            drupal_set_message(t('No Syndicated Content Found'));
            return;
        }

        /// hop over to the content types creation form

        /// lookup drupal content type associated with syndication content type
        $drupal_type = 'page';

        $node = new stdClass();
        $node->type = $drupal_type;
        /// FROM SYND
        $node->language = LANGUAGE_NONE;
        node_object_prepare($node);
        $node->title = $title;
        $node->body[$node->language][0]['value']   = $content;
        $node->body[$node->language][0]['summary'] = text_summary($content);
        $node->body[$node->language][0]['format']  = 'full_html';
        /// FROM SETTINGS
        $node->status  = 1;
        $node->comment = 0;
        /// DEFAULTS
        $node->created = time();
        $node->changed = time();
        $node->promote = 0;
        $node->sticky  = 0;
        $node->uid = (isset($user->uid) && !empty($user->uid)?$user->uid:1);
        $node->timestamp = time();
        $node->revision = 0;

        $node->is_new = TRUE;

        /// taxonomy: +subscribed

        $node = node_submit($node);
        node_save($node);
        if ( !empty($node->nid) || $node->nid==='0'  )
        {
            drupal_set_message(t('Content saved'));
            db_insert('syndicated_content')->fields(array(
                'node_id'        => $node->nid,
                'syndication_source_id'      => 1, /// hard coded for-now
                'media_id'       => $syndication_media_id,
                'media_type'     => $metadata['mediaType'],
                'source_url'     => $metadata['sourceUrl'],
                'tiny_url'       => $metadata['tinyUrl'],
                'locally_owned'  => 0,
                'date_authored'  => $metadata['dateSyndicationVisible'],
                'date_updated'   => $metadata['dateContentUpdated'],
                'date_synced'    => _syndicated_content_date(),
                'metadata'       => '',
            ))->execute();
            drupal_goto("/node/{$node->nid}");
        } else {
            drupal_set_message(t('Content did not save'));
        }
*/
    } else if ( in_array($form['#form_id'],array('syndicated_content_node_form')) ) {
        /// NEW NODE COMING IN - PRESAVE
    }
}

function _syndicated_content_update($node, $params, $publish = false) {
    global $base_url;
    $node = node_load($node->nid);
    module_load_include('php','syndicated_content','includes/syndicated_content.util');
    $sources        = _syndicated_content_api_sources();
    $source         = array_pop($sources);
    $type_map       = _syndicated_content_type_mapping( $source['id'] );
    $type           = isset($type_map['by_drupal_type'][$node->type][0]) ? $type_map['by_drupal_type'][$node->type][0] : "";
    $custom_field   = isset($type_map['by_drupal_type'][$node->type][1]) ? $type_map['by_drupal_type'][$node->type][1] : "";
    
    $params['source'] = $source['id'];//$source['source_org_id'];
    $params['mediaType'] = $type;
    $params['dateAuthored'] = _syndicated_content_date();
    
    switch(strtolower($type)) {
        case "html":
            $params['sourceUrl'] = url('node/'.$node->nid, array('absolute'=>true) );
            break;
        case "infographic":
        case "image":
            // Iterate through custom content fields looking for the first image.
            $vars = get_object_vars($node);
            $imgObj = null;
            
            if($custom_field != "") {
                $val = isset($vars[$custom_field]) ? $vars[$custom_field] : null;
                if(isset($val[$node->language][0]['filemime'])) {
                    if(strpos($val[$node->language][0]['filemime'], "image") !== false) {
                        $imgObj = $val;
                    }
                }
            } else {
                foreach($vars as $v => $val) {
                    if(strpos($v, "field_") !== false) {
                        if(isset($val[$node->language][0]['filemime'])) {
                            if(strpos($val[$node->language][0]['filemime'], "image") !== false) {
                                $imgObj = $val;
                                break;
                            }
                        }
                    }
                }
            }
            
            // TODO: Right now, we are only going to look for the first image in the set. Later, we may want to enhance to add all the images in the set.
            if(isset($imgObj[$node->language][0])) {
                $params['height'] = $imgObj[$node->language][0]['height'];
                $params['width'] = $imgObj[$node->language][0]['width'];
                $params['sourceUrl'] = $base_url."/".(variable_get('file_public_path', conf_path() . '/files/'.$imgObj[$node->language][0]['filename']));
                $params['imageFormat'] = isset($imgObj[$node->language][0]['filemime']) ? $imgObj[$node->language][0]['filemime'] : $ext;
                $params['altText'] = ($imgObj[$node->language][0]['alt'] != "" ? $imgObj[$node->language][0]['alt'] : $node->title);
                $params['description'] = isset($imgObj[$node->language][0]['description']) ? $imgObj[$node->language][0]['description'] : "Image File";
            }
            break;
        case 'video':
            // Iterate through custom content fields looking for the first video.
            $vars = get_object_vars($node);
            $imgObj = null;
            
            if($custom_field != "") {
                $val = isset($vars[$custom_field]) ? $vars[$custom_field] : null;
                if(isset($val[$node->language][0]['value'])) {
                    $tVal = $val[$node->language][0]['value'];
                    if(strpos($tVal, "http") === 0 && (strpos($tVal, "youtube") > 0 || strpos($tVal, "youtu.be") > 0)) {
                        $imgObj = $val;
                    }
                }
            } else {
                foreach($vars as $v => $val) {
                     if(strpos($v, "field_") !== false) {
                        if(isset($val[$node->language][0]['value'])) {
                            $tVal = $val[$node->language][0]['value'];
                            if(strpos($tVal, "http") === 0 && (strpos($tVal, "youtube") > 0 || strpos($tVal, "youtu.be") > 0)) {
                                $imgObj = $val;
                                break;
                            }
                        }
                    }
                }
            }
            
            if(isset($imgObj[$node->language][0]['value'])) {
                $params['sourceUrl'] = $tVal;
            }
            break;
        default:
            break;
    }
    
    $syndication = _syndicated_content_api_factory();
    $response = $syndication->publishMedia($params);
    #drupal_set_message('<a href="#" onclick="javascript:var d=document.getElementById(\'dbg_s'.__LINE__.'\');d.style.display=(d.style.display==\'none\')?\'block\':\'none\'">SYNDICATION CALL DEBUG</a>:<pre id="dbg_s'.__LINE__.'">'.print_r($response->raw,true).'</pre>','error');
    if ( !$response->success )
    {
        foreach ( $response->messages as $m )
        {
            if ( $m['errorCode']<200 )
            {
                if ( $m['errorCode']==4 && $m['errorDetail']['code']=='url.invalid' )
                {
                    drupal_set_message("Published Content must have public url. Syndication could not find<br />{$m['errorDetail']['rejectedValue']}",'error');
                } else {
                    drupal_set_message("Published Content could not Sync with given data. Please check to make sure all proper fields are filled. ".$m['userMessage'],'error');
                    _syndicated_content_stop_destination($node);
                }
            }
        }
    }
    $syndication_metadata = empty($response->results)?null:array_shift($response->results);
    if ( !empty($syndication_metadata) )
    {
        $syndication_metadata['locallyOwned'] = 1;
        _syndicated_content_save_metadata_for_node( $node, $syndication_metadata );
        drupal_set_message('Published To Syndication Source.');
    } else {
        drupal_set_message('Not Published.','error');
        //_syndicated_content_stop_destination($node);
    }
    // If this is the first time we publish, let's stay on the Edit screen.
    if($publish) {
        _syndicated_content_stop_destination($node);
    }
}

function _syndicated_content_get_tags($tagArr) {
    foreach($tagArr as $ta) {
        $tagNames[] = $ta['name'];
    }
    $tagNames = isset($tagNames) ? $tagNames : null;
    return $tagNames;
}

function _syndicated_content_node_update($node) {
    module_load_include('php','syndicated_content','includes/syndicated_content.util');
    $syndicatable_content_types = _syndicated_content_type_mapping();
    
    if (!empty($syndicatable_content_types['by_drupal_type'][$node->type]) )
    {
        $syndication_action = isset($node->syndication_action) ? $node->syndication_action : "";
        
        $drupaldata = db_query("SELECT * FROM {syndicated_content} WHERE node_id=:node",array(
            'node'   => $node->nid
        ))->fetchObject();
        
        if ( empty($drupaldata) ) {
            // Do nothing if this is not a Syndication object
            return;
        }
        // publish is allowed though
        
        $language_id = _syndicated_content_find_language_id( $node->syndication_publish_language );
        
        $tagArr = isset($node->field_tags[$node->language]) ? $node->field_tags[$node->language] : array();
        
        $params = array(
            //'name'         => $form_state['values']['syndication_publish_title'],
            'name'         => $node->title,
            'language'     => $language_id,
            'tagNames'     => _syndicated_content_get_tags($tagArr),
        );
        
        _syndicated_content_update($node, $params);
    }
}
/// true:success false:failure null:nothing_to_do
function _syndicated_content_update_node( &$node )
{
    $drupal_metadata = _syndicated_content_get_metadata_for_node( $node );
    /// no nodes subscribed to this media id
    if ( empty($drupal_metadata) )
    {
        throw new SyndicationContentUnknownToClientException();
    }

    $syndication = _syndicated_content_api_factory();
    $response = $syndication->getMediaById($drupal_metadata['media_id']);
    $syndication_metadata = empty($response->results)?null:array_shift($response->results);
    if ( empty($syndication_metadata) )
    {
        if ( $response->success )
        {
            /// returned empty content : wut?
            return false;
        } else {
            switch( $response->status )
            {
                case 400:
                    throw new SyndicationContentUnknowToSourceException();
                    break;
                case 500:
                    return false;
            }
        }
    }

    /// we got something back?
    /// we really should validate this before sticking it in the database... OH WELL
    _syndicated_content_save_metadata_for_node( $node, $syndication_metadata );

    switch( strtolower($syndication_metadata['mediaType']) )
    {
        case 'infographic':
        case 'image':
            _syndicated_content_map_image_to_node( $node, $syndication_metadata );
            break;
        case 'html':
        default:
            _syndicated_content_map_html_to_node( $node, $syndication_metadata );
            node_save($node);
            break;
    }
    return true;
}

function _syndicated_content_get_metadata_for_node( $node )
{
    $drupal_data = db_query("SELECT * FROM {syndicated_content} WHERE node_id=:nid",array(
        'nid'   => $node->nid
    ))->fetchAssoc();
    /// no nodes subscribed to this media id
    if ( empty($drupal_data) )
    {
        return false;
    }
    $raw_syndication_data = json_decode( $drupal_data['metadata'] ,true);
    return $drupal_data + $raw_syndication_data;
}

function _syndicated_content_save_metadata_for_node( $node, $syndication_metadata )
{
    $key    = array( 'node_id' => $node->nid );
    $fields = _syndicated_content_metadata_to_sqlfields($syndication_metadata);
    /// don't know where this should come from ?

    return db_merge('syndicated_content')
        ->key( $key )
        ->fields( $fields )
        ->execute();

    /*
    $title = empty($syndication_metadata['name'])?'Title':$syndication_metadata['name'];
     /// update vocabularies

    /// if taxonomy
    /// make sure each tag/category given for this item is in our list of vocab terms

    $campaign_ids   = array();
    $campaign_names = array();
    if ( isset($metadata['campaigns']) && is_array($metadata['campaigns']) )
    {
        foreach ( $metadata['campaigns'] as $campaign )
        {
            $campaign_ids[]   = $campaign['id'];
            $campaign_names[] = $campaign['name'];
        }
    }
    $campaign_ids   = join(',',$campaign_ids);
    $campaign_names = join(',',$campaign_names);

    $tag_ids   = array();
    $tag_names = array();
    if ( isset($metadata['tags']) && is_array($metadata['tags']) )
    {
        foreach ( $metadata['tags'] as $tag )
        {
            $tag_ids[]   = $tag['id'];
            $tag_names[] = $tag['name'];
        }
    }
    $tag_ids   = join(',',$tag_ids);
    $tag_names = join(',',$campaign_names);

    /// set tags/categories form this item as it's vocab terms

    */
}

function _syndicated_content_node_view($node, $view_mode, $langcode) {
    module_load_include('php','syndicated_content','includes/syndicated_content.util');
    $syndicatable_content_types = _syndicated_content_type_mapping();

    if (!empty($syndicatable_content_types['by_drupal_type'][$node->type]) ) {
        if(isset($node->content['body'][0])) {
            if(isset($node->content['body'][0]['#markup'])) {
                $dom = new DOMDocument();
                $total = 0;
                $content = $node->content['body'][0]['#markup'];
                try {
                    $dom->loadHTML($content);
                $xpath = new DOMXPath($dom);
                    $syndicatedContent = $xpath->query('//div[contains(concat(\' \', normalize-space(@class), \' \'), \' syndicate \')]');
                    $total = $syndicatedContent->length;
                } catch(Exception $e) {
                    $total = preg_match_all('/\sclass\s*=\s*["\'].*syndicate.*[\'"].*>/',
                             $content,
                             $matches,
                             PREG_PATTERN_ORDER);
                }
                
                if($total == 0) {
                    $node->content['body'][0] = array(
                        '#markup' => "<div class='syndicate'>".$node->content['body'][0]['#markup']."</div>", 
                    );
                }
            }
        }
    }
}

function _syndicated_content_map_image_to_node( &$node, $syndication_metadata )
{
    $syndication = _syndicated_content_api_factory();
    $response = $syndication->getMediaSyndicateById($syndication_metadata['id']);
    if ( empty($response->results) )
    {
        throw new SyndicationContentUnknownToSourceException();
    }
    $content = empty($response->results)?'':$response->results;
    if ( isset($node->title) && !empty($syndication_metadata['name']) )
    {
        $node->title = $syndication_metadata['name'];
    }
    if ( isset($node->body) )
    {
        $node->body[$node->language][0]['summary'] = text_summary($content);
        $node->body[$node->language][0]['value']   = $content;
    }
}

function _syndicated_content_map_html_to_node( &$node, $syndication_metadata )
{
    /// lookup mapping rules dynamically
    $syndication = _syndicated_content_api_factory();
    $response = $syndication->getMediaContentById($syndication_metadata['id']);
    if ( empty($response->results) )
    {
        throw new SyndicationContentUnknownToSourceException();
    }
    $content = empty($response->results)?'':$response->results;
    if ( isset($node->title) && !empty($syndication_metadata['name']) )
    {
        $node->title = $syndication_metadata['name'];
    }
    if ( isset($node->body) )
    {
        $node->body[$node->language][0]['summary'] = text_summary($content);
        $node->body[$node->language][0]['value']   = str_replace("</div>", "YES</div>", $content);
    }
}
