<?php
use Symfony\Component\HttpFoundation\RedirectResponse;

/// BEFORE NODE SAVE - we need to this run before any other actions take place, so I choose hook_validate over hook_presave
/// this catches and syndication specific actions before regular form actions(edit|save|delete) are processed

/**
 * @param $node_id
 * @param $syndication_action
 * @param $syndication_language
 */
function _syndicated_content_node_validate($node_id, $syndication_language, $syndication_action) {

  $node = \Drupal\node\Entity\Node::load($node_id);

  module_load_include('php','syndicated_content','includes/syndicated_content.util');

  if ( empty($node->getType()) || empty($node->id()) ) {
    return;
  }

  // Find the drupal content type of the node we are editing
  // pull a list of syndicatable-content-types from module tables
  $syndicatable_content_types = _syndicated_content_type_mapping();
  // is this node_type syndicatable?

  $sources        = _syndicated_content_api_sources();
  $source         = array_pop($sources);

  //if ( !empty($syndicatable_content_types['by_drupal_type'][$form['type']['#value']]) && !empty($form_state['values']['syndication_action']) )
  if ( !empty($syndicatable_content_types['by_drupal_type'][$node->getType()]) )
  {
      /// check for a syndication_action form field - means we want a spcial syndication action as opposed to a standard save
      //$syndication_action = $form_state['values']['syndication_action'];

      /// these are one time actions - reset this so nothing else will see this later in the same request
      //$form_state['values']['syndication_action'] = '';
      //$form_state['input']['syndication_action']  = '';

      switch( $syndication_action )
      {
          case '': break;

          case 'publish':

              // not connecting to the api server just to get the language id.
              // the id is set int the form drop down 1 = english 2 = spanish. that is what the api server wants
              //$language_id = _syndicated_content_find_language_id( $syndication_language );

              $tagArr = isset($form_state['values']['field_tags'][$node->language]) ? $form_state['values']['field_tags'][$node->language] : array();

              $params = array(
                  //'name'         => $form_state['values']['syndication_publish_title'],
                  'name'         => $node->getTitle(),
                  'language'     => $syndication_language,
                  'tagNames'     => _syndicated_content_get_tags($tagArr),
                  'sourceUrl'    => $source['cms_url'] . 'node/' . $node->id(),
              );

              _syndicated_content_update($node->id(), $params, true);
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
                      form_set_error('syndication_action', t('You cannot delete content you do not own.'));
                      return;
                  }

                  $syndication = _syndicated_content_api_factory();
                  $response = $syndication->unPublishMediaById($drupaldata->media_id);

                  if ( $response->status == 200 )
                  {
                      $num_deleted = db_delete('syndicated_content')
                                   ->condition('node_id', $node->nid, '=')
                                   ->execute();
                      drupal_set_message('Deleted From Syndication Source.');
                  } else {
                      drupal_set_message('Failed to delete source.');
                  }

              }
              _syndicated_content_stop_destination($node->id());
              break;

          case 'archive':
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
                      form_set_error('syndication_action', t('You cannot archive content you do not own.'));
                      return;
                  }

                  $syndication = _syndicated_content_api_factory();
                  $response = $syndication->archiveMediaById($drupaldata->media_id);

                  if ( $response->status == 200 )
                  {
                      // update content to be archived
                      db_update('syndicated_content')
                        ->fields(array('archive' => 1))
                        ->condition('media_id', $drupaldata->media_id)
                        ->execute();

                      drupal_set_message('Archived to Syndication Source.');
                  } else {
                      drupal_set_message('Failed to archive.');
                  }

              }
              _syndicated_content_stop_destination($node->id());

          break;

          case 'unarchive':
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
                      form_set_error('syndication_action', t('You cannot unarchive content you do not own.'));
                      return;
                  }

                  $syndication = _syndicated_content_api_factory();
                  $response = $syndication->unArchiveMediaById($drupaldata->media_id);

                  if ( $response->status == 200 )
                  {
                      // update content to be archived
                      db_update('syndicated_content')
                        ->fields(array('archive' => 0))
                        ->condition('media_id', $drupaldata->media_id)
                        ->execute();

                      drupal_set_message('Unarchived from Syndication Source.');
                  } else {
                      drupal_set_message('Failed to unarchive.');
                  }

              }
              _syndicated_content_stop_destination($node->id());

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
                  //$local_url = url('node/'.$drupaldata->node_id);
                  $local_url = $source['cms_url'].'node/'.$drupaldata->node_id;
                  error_log('update: (local_url) - '.$local_url);
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
              _syndicated_content_stop_destination($node->id());
              break;

          default:
              drupal_set_message('Unsupported syndication action requested: '.$syndication_action);
              _syndicated_content_stop_destination($node->id());
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

/**
 * @param $node
 */
function _syndicated_content_stop_destination($node) {

    unset($_GET['destination']);

    unset($_REQUEST['destination']);

    $response = new \Drupal\Core\Routing\TrustedRedirectResponse("/node/$node/edit");
    $response->send();
}

/// AFTER NODE SAVE - also track metadata
/**
 * @param \Drupal\node\NodeInterface $node
 * @return bool|\Drupal\Core\Database\StatementInterface|int|null
 */
function _syndicated_content_node_insert( \Drupal\node\NodeInterface $node )
{
    module_load_include('php','syndicated_content','includes/syndicated_content.util');

    /// if this node came through subscribe than it will already be ...
    if ( isset($node->syndication_media_id) /// saved to the db and have a nid
         && $node->id()              /// marked as new
         && $node->isNew() )         /// stamped with a syndication_media_id
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
/**
 * @param $form
 * @param $form_state
 */
function _syndicated_content_form_submit( $form, &$form_state )
{
    $user = \Drupal::currentUser();
	
    $sources        = _syndicated_content_api_sources();
    $source         = array_pop($sources);
	
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

        $conn = \Drupal::database();

        $drupaldata = db_query("SELECT * FROM {syndicated_content} WHERE syndication_source_id=:syndication_source_id AND media_id=:media_id",array(
            'syndication_source_id' => _syndicated_content_get_source_id(),
            'media_id'  => $syndication_media_id
        ))->fetchObject();

        if ( $drupaldata )
        {
            //$local_url = url('node/'.$drupaldata->node_id);
            $local_url = $source['cms_url'].'node/'.$drupaldata->node_id;
            //error_log('update: (local_url) - '.$local_url);
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
        $node->language = \Drupal\Core\Language\Language::LANGCODE_NOT_SPECIFIED;
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

/**
 * @param $node_id
 * @param $params
 * @param bool $publish
 */
function _syndicated_content_update($node_id, $params, $publish = false) {

    global $base_url;

    $node = \Drupal\node\Entity\Node::load($node_id);

    module_load_include('php','syndicated_content','includes/syndicated_content.util');

    $sources        = _syndicated_content_api_sources();
    $source         = array_pop($sources);
    $type_map       = _syndicated_content_type_mapping( $source['id'] );
    $type           = isset($type_map['by_drupal_type'][$node->getType()][0]) ? $type_map['by_drupal_type'][$node->getType()][0] : "";
    $custom_field   = isset($type_map['by_drupal_type'][$node->getType()][1]) ? $type_map['by_drupal_type'][$node->getType()][1] : "";

    $params['source'] = $source['source_org_id'];
    $params['mediaType'] = $type;
    $params['dateAuthored'] = _syndicated_content_date();
    $params['sourceUrl'] = $source['cms_url'] . '/node/' . $node->id();

    switch(strtolower($type)) {
        case "html":
            // @FIXME
            // url() expects a route name or an external URI.
            // $params['sourceUrl'] = rtrim($source['cms_url'], '/').url('node/'.$node->nid);

            //error_log('update: (\$params[\'sourceUrl\']) - '.$params['sourceUrl']);
            //error_log('URL: '.url('node/'.$node->nid));
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
                // @FIXME
                // // @FIXME
                // // This looks like another module's variable. You'll need to rewrite this call
                // // to ensure that it uses the correct configuration object.
                // $params['sourceUrl'] = $source['cms_url'].(variable_get('file_public_path', conf_path() . '/files/'.$imgObj[$node->language][0]['filename']));

                $params['imageFormat'] = isset($imgObj[$node->language][0]['filemime']) ? $imgObj[$node->language][0]['filemime'] : $ext;
                $params['altText'] = ($imgObj[$node->language][0]['alt'] != "" ? $imgObj[$node->language][0]['alt'] : $node->title);
                $params['description'] = isset($imgObj[$node->language][0]['description']) ? $imgObj[$node->language][0]['description'] : "Image File";
                //error_log('update: (\$params[\'sourceUrl\']) - '.$params['sourceUrl']);
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
    if ( !$response->success ) {
        foreach ( $response->messages as $m )
        {
            if ( $m['errorCode']<200 )
            {
                if ( $m['errorCode']==4 && $m['errorDetail']['code']=='url.invalid' )
                {
                    drupal_set_message("Published Content must have public url. Syndication could not find<br />{$m['errorDetail']['rejectedValue']}",'error');
                } else {
                    drupal_set_message("Published Content could not Sync with given data. Please try the following:
                    <ul>
                        <li>Please check to make sure all proper fields are filled.
                        <li>Verify that the Syndication Server can communicate with your Web Server.</li>
                        <li>".$m['userMessage']."</li>
                    </ul>",'error');
                    //".$m['userMessage']."
                    _syndicated_content_stop_destination($node->id());
                }
            }
        }
    }

    $syndication_metadata = empty($response->results)?null:array_shift($response->results);

    if ( !empty($syndication_metadata) ) {

        $syndication_metadata['locallyOwned'] = 1;

        _syndicated_content_save_metadata_for_node( $node, $syndication_metadata );

        drupal_set_message('Published To Syndication Source.');

    } else {
        drupal_set_message('Not Published. No Messages Received','error');
        //_syndicated_content_stop_destination($node);
    }

    // If this is the first time we publish, let's stay on the Edit screen.
    if($publish) {
        _syndicated_content_stop_destination($node->id());
    }
}

/**
 * @param $tagArr
 * @return array|null
 */
function _syndicated_content_get_tags($tagArr) {
    foreach($tagArr as $ta) {
        $tagNames[] = $ta['name'];
    }
    $tagNames = isset($tagNames) ? $tagNames : null;
    return $tagNames;
}

/**
 * @param \Drupal\node\NodeInterface $node
 */
function _syndicated_content_node_update($node_id, $syndication_language, $syndication_action) {

    $node = \Drupal\node\Entity\Node::load($node_id);

    module_load_include('php','syndicated_content','includes/syndicated_content.util');
    $syndicatable_content_types = _syndicated_content_type_mapping();

    if (!empty($syndicatable_content_types['by_drupal_type'][$node->getType()]) ) {

      $syndication_action = isset($node->syndication_action) ? $node->syndication_action : "";

      $conn = \Drupal::database();

      $drupaldata = $conn->select('syndicated_content', 'sc')
        ->fields('sc', ['node_id', 'syndication_source_id', 'media_id', 'media_type', 'tiny_url', 'locally_owned', 'date_authored', 'date_updated', 'date_synced',
          'language', 'source', 'source_acronym', 'source_url', 'metadata', 'archive'])
        ->execute()
        ->fetchObject();

      if ( empty($drupaldata) ) {

          // Do nothing if this is not a Syndication object
          // cah uncomment this before going live
          //return;
      }

      // publish is allowed though
      // commenting this out we have the lanuage id in drupaldata variable.
      //$language_id = _syndicated_content_find_language_id( $node->syndication_publish_language );

      $tagArr = isset($node->field_tags[$node->language]) ? $node->field_tags[$node->language] : array();

      $sources        = _syndicated_content_api_sources();
      $source         = array_pop($sources);

      $params = array(
          'name'         => $node->getTitle(),
          'language'     => $syndication_language,
          'tagNames'     => _syndicated_content_get_tags($tagArr),
          'sourceUrl'    => $source['cms_url'] . '/node/' . $node->id(),
      );

        _syndicated_content_update($node->id(), $params);
    }
}

/// true:success false:failure null:nothing_to_do
/**
 * @param $node
 * @return bool
 * @throws SyndicationContentUnknowToSourceException
 */
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
            $node->save();
            break;
    }
    return true;
}

/**
 * @param $node
 * @return bool
 */
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

/**
 * @param \Drupal\node\NodeInterface $node
 * @param $syndication_metadata
 * @return \Drupal\Core\Database\StatementInterface|int|null
 */
function _syndicated_content_save_metadata_for_node( \Drupal\node\NodeInterface $node, $syndication_metadata )
{
    $key    = array( 'node_id' => $node->id() );
    $fields = _syndicated_content_metadata_to_sqlfields($syndication_metadata);
    /// don't know where this should come from ?

    $conn = \Drupal::database();

    return $conn->merge('syndicated_content')
        ->key( $key )
        ->fields( $fields )
        ->execute();
}

/**
 * @param $node
 * @param $view_mode
 * @param $langcode
 */
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
                    libxml_use_internal_errors(true);
                    $dom->loadHTML($content);
                    $xpath = new DOMXPath($dom);
                    $syndicatedContent = $xpath->query('//div[contains(concat(\' \', normalize-space(@class), \' \'), \' syndicate \')]');
                    libxml_clear_errors();
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

/**
 * @param $node
 * @param $syndication_metadata
 */
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

/**
 * @param $node
 * @param $syndication_metadata
 */
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
