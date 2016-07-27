<?php

define( '_SYNDICATED_CONTENT_SHORTLENGTH', 255  );
define( '_SYNDICATED_CONTENT_LONGLENGTH',  2000 );

/// FORM ALTERATIONS
function _syndicated_content_form_alter( &$form, &$form_state, $form_id )
{
    /// for date and language functions
    module_load_include('php','syndicated_content','includes/syndicated_content.util');

    if ( empty($form['type']) || empty($form['type']['#value']) ) { return; }

    /// Create Form for Drupal SyndicatedContentType : for subscribing to new content
    if ( $form['type']['#value']=='syndicated_content' && !isset($form_state['node']->nid) ) {
        _syndicated_content_alter_subscribe_form( $form, $form_state, $form_id );
        return;
    }

    $type_map = _syndicated_content_type_mapping();

    /// only mess with syndicatable types
    if ( empty( $type_map['by_drupal_type'][$form['type']['#value']] ) )
    {
        return;
    }

    /// we have fresh creation form - maybe we can pre-populate some fields
    if ( !isset($form_state['node']->nid) )
    {
        $p = drupal_get_query_parameters();
        if ( isset($p['syndication_media_id']) )
        {
            /// are we already tracking something with this ID locally?
            $source_id = _syndicated_content_get_source_id();
            if ( !$source_id ) { return; }
            $drupal_metadata = db_query("SELECT * FROM {syndicated_content} WHERE syndication_source_id=:syndication_source_id AND media_id=:media_id",array(
                'syndication_source_id' => $source_id,
                'media_id'  => $p['syndication_media_id']
            ))->fetchObject();
            /// we are already tracking metadata for this id
            if ( $drupal_metadata )
            {
                $local_url = url('node/'.$drupal_metadata->node_id);
                if ( !empty($drupal_metadata->locally_owned) )
                {
                    drupal_set_message("You cannot ingest locally authored content (<a href=\"{$local_url}\">{$local_url}</a>)");
                } else {
                    drupal_set_message("You are already tracking this content (<a href=\"{$local_url}\">{$local_url}</a>)");
                }
            } else {
                $syndication = _syndicated_content_api_factory();
                $response = $syndication->getMediaById($p['syndication_media_id']);
                if ( $response->success && !empty($response->results))
                {
                    $syndication_metadata = array_shift($response->results);
                    _syndicated_content_alter_create_form( $syndication_metadata, $form, $form_state, $form_id );
                }
            }
        }
    // if we have a node and it's possible to syndicate it, add stuff to edit form
    } else if ( isset($form_state['node']->nid) ) {
        $source_id = _syndicated_content_get_source_id();
        if ( !$source_id ) { return; }

        $form['#attached']['js'] = array(
            drupal_get_path('module', 'syndicated_content') . '/assets/syndicated_content_edit_form.js',
        );

        $query = db_select('syndicated_content', 'c')
                    ->fields('c')
                    ->condition('syndication_source_id',$source_id)
                    ->condition('node_id',$form_state['node']->nid)
                    ->range(0, 1);
        $exec = $query->execute();
        $drupal_metadata = $exec->fetchAssoc();
        /// Not Published yet to Syndication
        if ( empty($drupal_metadata) )
        {
            _syndicated_content_alter_edit_form_unpublished( $type_map, $form, $form_state, $form_id );
        /// Already Published to Syndication
        } else {
            _syndicated_content_alter_edit_form_published( $drupal_metadata, $form, $form_state, $form_id );
        }
    }
}

/// admin screen additions

function _syndicated_content_form_node_admin_content_alter(&$form, &$form_state, $form_id) 
{
    module_load_include('php','syndicated_content','includes/syndicated_content.data');

    if ( $form_id!=='node_admin_content' ) { return; }

    /// pull metadata of nodes - need a load_multiple version of _get_metadata_for_node()
    $nodes = node_load_multiple(array_keys($form['admin']['nodes']['#options']));
    $meta = array();
    foreach ($nodes as $node) 
    {
        $meta[$node->nid] = array( 'type'=>$node->type, 'data'=>_syndicated_content_get_metadata_for_node( $node ) );
    }

    /// need to figure out how to add sorting

    /// add column to results
    $keys = array_keys($form['admin']['nodes']['#header']);
    $last_key   = array_pop($keys);
    $last_value = array_pop($form['admin']['nodes']['#header']);
    $form['admin']['nodes']['#header']['syndicated'] = array('data' => t('Syndication'),'sort'=>'ASC');
    $form['admin']['nodes']['#header'][$last_key]    = $last_value;

    $syndicatable_content_types = _syndicated_content_type_mapping();

    foreach ($form['admin']['nodes']['#options'] as $nid => $row) 
    {
        if ( !empty($meta[$nid]) && !empty($meta[$nid]['data']) ) 
        {
            $synd_info = ( !empty($meta[$nid]['data']['locally_owned']) ) ? 'syndicated' : 'subscribed';
        } else {
            $synd_info = ( empty($syndicatable_content_types['by_drupal_type'][$meta[$nid]['type']]) ) ? '<span style="opacity:0.6;">un-syndicatable</span>' : 'not syndicated';
        }
        $form['admin']['nodes']['#options'][$nid]['syndicated'] = $synd_info;
    }

}

/// alter standard node creation form

function _syndicated_content_alter_create_form( $syndication_metadata, &$form, &$form_state, $form_id )
{  
    $syndicatable_content_types = _syndicated_content_type_mapping();

    if ( empty($syndicatable_content_types['by_drupal_type'][$form['type']['#value']]) || empty($syndication_metadata) ) { return; }

    ///  pre-fill live data
    if ( strtolower($syndication_metadata['mediaType']) == 'html' )
    {
        _syndicated_content_map_html_to_page_form($syndication_metadata,$form,$form_state,$form_id);
    } else if ( strtolower($syndication_metadata['mediaType']) == 'image' ) {
        _syndicated_content_map_image_to_page_form($syndication_metadata,$form,$form_state,$form_id);
    } else {
        _syndicated_content_map_any_to_unknown_form($syndication_metadata,$form,$form_state,$form_id);
    }

    /// give an edit-form a new area for syndication interaction
    $form['syndication_information'] = array(
        '#type'        => 'fieldset',
        '#title'       => t('Syndication'),
        '#collapsible' => TRUE,
        '#collapsed'   => TRUE,
        '#group'       => 'additional_settings',
        '#attributes'  => array(
            'class' => array('node-form-syndication-information'),
        ),
        '#weight' => -10, /// 100, ///-10,
        '#access' => user_access('administer nodes'),
    );

    $form['syndication_information']['syndication_media_id'] = array(
        '#type'  => 'hidden',
        '#value' => $syndication_metadata['id'],
    );

    $form['syndication_information']['syndicated_information_id'] = array(
        '#type'   => 'item',
        '#title'  => t('Media Id'),
        '#markup' => $syndication_metadata['id'],
    );

    $form['syndication_information']['syndicated_information_url'] = array(
        '#type'   => 'item',
        '#title'  => t('Source Url'),
        '#markup' => $syndication_metadata['sourceUrl'],
    );

    if ( $syndication_metadata['tinyUrl'] != 'NotMapped' )
    {
        $form['syndication_information']['syndicated_information_tiny'] = array(
            '#type'   => 'item',
            '#title'  => t('Tiny Url'),
            '#markup' => $syndication_metadata['tinyUrl'],
        );
    }

    $form['syndication_information']['syndicated_information_owned'] = array(
        '#type'   => 'item',
        '#title'  => t('Content Owner'),
        '#markup' => 'Syndication: you will be subscribed to this content',
    );

    $form['syndication_information']['syndicated_information_updated'] = array(
        '#type'   => 'item',
        '#title'  => t('Last Content Update'),
        '#markup' => $syndication_metadata['dateContentUpdated'],
    );

}

function _syndicated_content_map_html_to_page_form( $syndication_metadata, &$form, &$form_state, $form_id )
{
    $syndication = _syndicated_content_api_factory();
    $response = $syndication->getMediaSyndicateById($syndication_metadata['id']);
    if ( !empty($response->results) )
    {
        $form['title']['#default_value'] = !empty($syndication_metadata['name'])?$syndication_metadata['name']:'';
        if ( isset($form['body']) &&
             isset($form['body'][$form['body']['#language']]) &&
             isset($form['body'][$form['body']['#language']][0]) )
        {
            $form['body'][$form['body']['#language']][0]['#default_value']            = $response->results;
            $form['body'][$form['body']['#language']][0]['summary']['#default_value'] = text_summary($response->results);
            $form['body'][$form['body']['#language']][0]['#format']                   = 'full_html';
        }
    }
}

function _syndicated_content_map_image_to_page_form( $syndication_metadata, &$form, &$form_state, $form_id )
{
    $syndication = _syndicated_content_api_factory();
    $response = $syndication->getMediaSyndicateById($syndication_metadata['id']);
    if ( !empty($response->results) )
    {
        $form['title']['#default_value'] = !empty($syndication_metadata['name'])?$syndication_metadata['name']:'';
        if ( isset($form['body']) &&
             isset($form['body'][$form['body']['#language']]) &&
             isset($form['body'][$form['body']['#language']][0]) )
        {
            $form['body'][$form['body']['#language']][0]['#default_value']            = $response->results;
            $form['body'][$form['body']['#language']][0]['summary']['#default_value'] = text_summary($response->results);
            $form['body'][$form['body']['#language']][0]['#format']                   = 'full_html';
        }
    }
}

function _syndicated_content_map_any_to_unknown_form( $syndication_metadata, &$form, &$form_state, $form_id )
{
    $syndication = _syndicated_content_api_factory();
    $response = $syndication->getMediaSyndicateById($syndication_metadata['id']);
    if ( !empty($response->results) )
    {
        // try and set the title
        if ( isset($form['title']) ) 
        {
            if ( empty($form['title']['#default_value']) && !empty($syndication_metadata['name']) ) 
            {
                $form['title']['#default_value'] = $syndication_metadata['name'];
            }
        }
        if ( isset($form['body']) ) 
        {
            $form['body'][$form['body']['#language']][0]['#format'] = 'full_html';
            if ( isset($form['body'][$form['body']['#language']][0]['#default_value']) )
            {
                $form['body'][$form['body']['#language']][0]['#default_value'] .= $response->results;
            } else {
                $form['body'][$form['body']['#language']][0]['#default_value']  = $response->results;
            }
            if ( ! isset($form['body'][$form['body']['#language']][0]['summary']['#default_value']) ) 
            {
                $form['body'][$form['body']['#language']][0]['summary']['#default_value'] = text_summary($response->results);
            }
        }
    }
}

/// alter SyndicatedContentType form 

function _syndicated_content_alter_subscribe_form( &$form, &$form_state, $form_id )
{
      /// inject some javascript stuff before form submission
      $form['#attached']['js'] = array(
        drupal_get_path('module', 'syndicated_content') . '/assets/syndicated_content_form.js',
      );
      $form['#attached']['css'] = array(
        drupal_get_path('module', 'syndicated_content') . '/assets/syndicated_content_form.css',
      );
      $form['actions']['submit']['#submit'][0] = 'syndicated_content_form_submit';

      /// drupal requires presence of TITLE even though we aren't using it
      /// give it junk data and hide it
      $form['title']['#value']    = 'Syndicated Content Creation';
      $form['title']['#type']     = 'value';
      $form['title']['#required'] = FALSE;

      /// add in our custom settings tabs
      $form['syndication_settings'] = array(
        '#type'   => 'vertical_tabs',
        '#weight' => -4
      );

      _syndicated_content_alter_form_add_lookup_fields( $form, $form_state, $form_id );
      _syndicated_content_alter_form_add_search_fields( $form, $form_state, $form_id );
      _syndicated_content_alter_form_add_browse_fields( $form, $form_state, $form_id );

      _syndicated_content_alter_form_add_subscribe_button( $form, $form_state );

      /// holds content id used for subscription
      $form['syndication_settings']['syndication_media_id'] = array(
          '#type'          => 'hidden',
          '#default_value' => '',
          '#attributes'    => array( 'id'=>'syndication_media_id' )
      );

      $form['syndication_search']['syndicated_search_list'] = array(
        '#type'   => 'item',
        '#title'  => t('Items'),
        '#markup' => '<div id="search_metadata">'. _syndicated_content_build_metadata_table_view( 'edit-syndication-ajax-search-submit', array(), array('max'=>1,'total'=>0,'offset'=>0) ) .'',
      );

      /// get rid of most of the standard options
      unset($form['actions']['submit']);
      unset($form['menu']);
      unset($form['author']);
      unset($form['revision_information']);
      unset($form['comment_settings']);
      unset($form['path']);
      unset($form['options']);
      unset($form['actions']['preview']);
      unset($form['additional_settings']);
}

function _syndicated_content_alter_form_add_lookup_fields( &$form, &$form_state, $form_id )
{
    $form['syndication_lookup'] = array(
        '#type'        => 'fieldset',
        '#title'       => t('Syndicated Content Lookup'),
        '#collapsible' => TRUE,
        '#collapsed'   => FALSE,
        '#group'       => 'syndication_settings',
        '#attributes'  => array(
            'class'   => array('node-form-syndication-information'),
            'onclick' => "javascript:syndication_submit_type='lookup';"
        ),
        '#weight'      => -2,
        '#access'      => user_access('administer nodes'),
    );

    $form['syndication_lookup']['syndication_lookup_value'] = array(
        '#type'        => 'textfield',
        '#maxlength'   => _SYNDICATED_CONTENT_SHORTLENGTH,
        //'#title'       => t('Lookup Content by Source Url, Tiny Url, or Id'),
        '#title'       => t('Lookup Content by Source Url or Id'),
        '#access'      => user_access('administer nodes'),
    );

    $form['syndication_lookup']['syndication_ajax_lookup_submit'] = array(
        '#type'        => 'submit',
        '#ajax'        => array(
            'callback' => 'syndicated_content_lookup_ajax',
            'wrapper'  => 'lookup_metadata',
        ),
        '#value'       => t('Lookup'),
        '#access' => user_access('administer nodes'),
    );

    $form['syndication_lookup']['syndicated_content_metadata'] = array(
        '#type'   => 'item',
        '#title'  => t('Content Information'),
        '#markup' => '<div id="lookup_metadata">No Results</div>',
    );

}

function _syndicated_content_alter_form_add_search_fields( &$form, &$form_state, $form_id )
{
    /// tab for searching content
    $form['syndication_search'] = array(
        '#type'        => 'fieldset',
        '#title'       => t('Syndicated Content Search'),
        '#collapsible' => TRUE,
        '#collapsed'   => TRUE,
        '#group'       => 'syndication_settings',
        '#attributes'  => array(
            'class'   => array('node-form-syndication-information'),
            'onclick' => "javascript:syndication_submit_type='search';"
        ),
        '#weight' => -3,
        '#access' => user_access('administer nodes'),
    );

    /// sub-tabs for reg/advanced search
    $form['syndication_search']['syndication_search_settings'] = array(
        '#type'   => 'vertical_tabs',
    );

    /// holds next page of paginated results to load
    $form['syndication_search']['syndication_goto_page'] = array(
        '#type'          => 'hidden',
        '#default_value' => '',
        '#attributes'    => array( 'id'=>'syndication_goto_page' )
    );

    _syndicated_content_alter_form_add_reg_search_fields( $form, $form_state, $form_id );
    _syndicated_content_alter_form_add_adv_search_fields(   $form, $form_state, $form_id );

    /// hidden button - used by javascript to submit page-turns
    $form['syndication_search']['syndication_ajax_search_submit'] = array(
        '#type'        => 'submit',
        '#ajax'        => array(
            'callback' => 'syndicated_content_search_ajax',
            'wrapper'  => 'search_metadata',
        ),
        '#value'  => t('Ajax Search'),
    );

}

function _syndicated_content_alter_form_add_subscribe_button( &$form, &$form_state, $form_section=null )
{
    if ( !empty($form_section) && !empty($form[$form_section]) ) 
    {
        $fields =& $form[$form_section];
    } else { 
        $fields =& $form;
    }

    $drupal_types  = node_type_get_names();
    $type_map      = _syndicated_content_type_mapping();
    $default_type  = 'article'; // '';
    $allowed_types = array();
    foreach ( $type_map['by_drupal_type'] as $type=>$synd_types )
    {
        if ( empty($drupal_types[$type]) ) { continue; }
        if ( empty($default_type) && stristr($type,'page')==='page' ) 
        {
            $default_type = $type;
        }
        $allowed_types[$type] = $drupal_types[$type];
    }
    $fields['syndicated_content_local_type'] = array(
        '#type'        => 'select',
        '#title'       => t(' local type'),
        '#attributes'  => array( 'id'=>'syndicated_content_local_type', 'class'=>array('syndication-form-local-type') ),
        '#options'     => $allowed_types,
        '#default_value' => array($default_type),
        '#weight'        => 101,
    );

    $submit_button = $form['actions']['submit'];
    $submit_button['#submit'][0] = 'syndicated_content_form_submit';
    $submit_button['#value']     = 'Subscribe to Content';
    $submit_button['#weight']    = 100; 
    $submit_button['#attributes']['class'][] = 'syndication-form-subscribe-submit'; 
    $fields['submit'] = $submit_button;

}

function _syndicated_content_alter_form_add_reg_search_fields( &$form, &$form_state, $form_id )
{

    $form['syndication_search']['syndication_reg_search'] = array(
        '#type'        => 'fieldset',
        '#title'       => t('Search'),
        '#collapsible' => TRUE,
        '#collapsed'   => FALSE,
        '#group'       => 'syndication_search_settings',
        '#attributes'  => array(
        ),
    );

    $form['syndication_search']['syndication_reg_search']['syndication_search_value'] = array(
        '#type'        => 'textfield',
        '#maxlength'   => _SYNDICATED_CONTENT_LONGLENGTH,
        '#title'       => t('Whole-word search'),
        '#attributes'    => array( 'id'=>'syndication_search_value' )
    );

    $form['syndication_search']['syndication_reg_search']['syndication_reg_search_submit'] = array(
        '#type'        => 'submit',
        '#ajax'        => array(
            'callback' => 'syndicated_content_search_ajax',
            'wrapper'  => 'search_metadata',
        ),
        '#value'  => t('Search'),
    );

}

function _syndicated_content_alter_form_add_adv_search_fields( &$form, &$form_state, $form_id )
{
    $form['syndication_search']['syndication_adv_search'] = array(
        '#type'        => 'fieldset',
        '#title'       => t('Advanced Search'),
        '#collapsible' => TRUE,
        '#collapsed'   => TRUE,
        '#group'       => 'syndication_search_settings',
        '#attributes'  => array(
        ),
    );

    $form['syndication_search']['syndication_adv_search']['syndication_adv_name'] = array(
        '#type'        => 'textfield',
        '#maxlength'   => _SYNDICATED_CONTENT_SHORTLENGTH,
        '#title'       => t('Name Contains'),
        '#attributes'    => array( 'id'=>'syndication_adv_name' )
    );
    $form['syndication_search']['syndication_adv_search']['syndication_adv_description'] = array(
        '#type'        => 'textfield',
        '#maxlength'   => _SYNDICATED_CONTENT_LONGLENGTH,
        '#title'       => t('Description Contains'),
        '#attributes'    => array( 'id'=>'syndication_adv_description' )
    );
    $form['syndication_search']['syndication_adv_search']['syndication_adv_source'] = array(
        '#type'        => 'textfield',
        '#maxlength'   => _SYNDICATED_CONTENT_LONGLENGTH,
        '#title'       => t('Source Url Contains'),
        '#attributes'    => array( 'id'=>'syndication_adv_source' )
    );
    $form['syndication_search']['syndication_adv_search']['syndication_adv_source'] = array(
        '#type'        => 'textfield',
        '#maxlength'   => _SYNDICATED_CONTENT_LONGLENGTH,
        '#title'       => t('Source Organization Contains'),
        '#attributes'    => array( 'id'=>'syndication_adv_source' )
    );

    $mediaTypes = _syndicated_content_media_types();
    $form['syndication_search']['syndication_adv_search']['syndication_adv_media_types'] = array(
        '#type'        => 'select',
        '#title'       => t('Media Type'),
        '#attributes'  => array( 'id'=>'syndication_adv_media_types' ),
        '#multiple'    => true,
        '#options'     => $mediaTypes,
        '#size'        => count($mediaTypes)
    );
    $form['syndication_search']['syndication_adv_search']['syndication_adv_language'] = array(
        '#type'        => 'select',
        '#title'       => t('Langauge'),
        '#attributes'  => array( 'id'=>'syndication_adv_language' ),
        '#options'     => array(''=>'') + _syndicated_content_languages_iso_639_2(),
    );

    $form['syndication_search']['syndication_adv_search']['syndication_adv_authored'] = array(
        '#type'        => 'item',
        '#title'       => t('Date Authored'),
        'syndication_adv_authored_when' => array(
            '#type'        => 'select',
            '#attributes'  => array( 'id'=>'syndication_adv_authored_when' ),
            '#options'     => array(''=>'','on'=>'on','before'=>'before','since'=>'since','between'=>'between'),
        ),
        'syndication_adv_authored_start' => array(
            '#type'        => 'date',
            '#attributes'  => array( 'id'=>'syndication_adv_authored_start' ),
            '#states' => array('visible'=>array(
                          array(':input[name="syndication_adv_authored_when"]' => array('value' => 'on')),
                    'xor',array(':input[name="syndication_adv_authored_when"]' => array('value' => 'before')),
                    'xor',array(':input[name="syndication_adv_authored_when"]' => array('value' => 'since')),
                    'xor',array(':input[name="syndication_adv_authored_when"]' => array('value' => 'between')),
            )),
        ),
        'syndication_adv_authored_between' => array(
            '#type'         => 'date',
            '#field_prefix' => 'and',
            '#attributes'   => array( 'id'=>'syndication_adv_authored_between' ),
            '#states' => array(
                'visible' => array(
                    ':input[name="syndication_adv_authored_when"]' => array('value' => 'between'),
                ),
            ),
        ),
    );

    $form['syndication_search']['syndication_adv_search']['syndication_adv_updated'] = array(
        '#type'        => 'item',
        '#title'       => t('Date Updated'),
        'syndication_adv_updated_when' => array(
            '#type'        => 'select',
            '#attributes'  => array( 'id'=>'syndication_adv_updated_when' ),
            '#options'     => array(''=>'','on'=>'on','before'=>'before','since'=>'since','between'=>'between'),
        ),
        'syndication_adv_updated_start' => array(
            '#type'        => 'date',
            '#attributes'  => array( 'id'=>'syndication_adv_updated_start' ),
            '#states' => array('visible'=>array(
                array(':input[name="syndication_adv_updated_when"]' => array('value' => 'on')),
                'xor',array(':input[name="syndication_adv_updated_when"]' => array('value' => 'before')),
                'xor',array(':input[name="syndication_adv_updated_when"]' => array('value' => 'since')),
                'xor',array(':input[name="syndication_adv_updated_when"]' => array('value' => 'between')),
            )),
        ),
        'syndication_adv_updated_between' => array(
            '#type'         => 'date',
            '#field_prefix' => 'and',
            '#attributes'   => array( 'id'=>'syndication_adv_updated_between' ),
            '#states' => array(
                'visible' => array(
                    ':input[name="syndication_adv_updated_when"]' => array('value' => 'between'),
                ),
            ),
        ),
    );

    $form['syndication_search']['syndication_adv_search']['syndication_adv_search_submit'] = array(
        '#type'        => 'submit',
        '#ajax'        => array(
            'callback' => 'syndicated_content_search_ajax',
            'wrapper'  => 'search_metadata',
        ),
        '#value'       => t('Advanced Search'),
    );
}

function _syndicated_content_alter_form_add_browse_fields( &$form, &$form_state, $form_id )
{
    $form['syndication_browse'] = array(
        '#type'        => 'fieldset',
        '#title'       => t('Syndicated Content Browse'),
        '#collapsible' => TRUE,
        '#collapsed'   => TRUE,
        '#group'       => 'syndication_settings',
        '#attributes'  => array(
            'class'   => array('node-form-syndication-information'),
            'onclick' => "javascript:syndication_submit_type='browse';"
        ),
        '#weight' => -4,
        '#access' => user_access('administer nodes'),
    );

    $form['syndication_browse']['syndication_ajax_browse_submit'] = array(
        '#type'        => 'submit',
        '#ajax'        => array(
            'callback' => 'syndicated_content_browse_ajax',
            'wrapper'  => 'browse_metadata',
        ),
        '#value'  => t('Browse'),
    );

    $form['syndication_browse']['syndicated_content_metadata'] = array(
        '#type'   => 'item',
        '#title'  => t('Content Information'),
        '#markup' => '<div id="browse_metadata">No Results</div>',
    );

}

/// alter edit form

function _syndicated_content_alter_edit_form_unpublished( $type_map, &$form, &$form_state, $form_id )
{
    /// give an edit-form a new area for syndication interaction
    $form['syndication_information'] = array(
        '#type'        => 'fieldset',
        '#title'       => t('Syndication'),
        '#collapsible' => TRUE,
        '#collapsed'   => TRUE,
        '#group'       => 'additional_settings',
        '#attributes'  => array(
            'class' => array('node-form-syndication-information'),
        ),
        '#weight' => -10, /// 100 10,
        '#access' => user_access('syndication publish rights'),
    );

    /*
    /// type dropdown - pulled from module settings - the list of synd-types this drupal-type can be published as
    $sources = array();
    /// get sources from db
    $sources = db_select('syndicated_content_sources', 's')
        ->fields('s',array('id','name'))
        ->range(0,1) /// temp: only one source allowed
        ->execute()
        ->fetchAllKeyed();
    $num_sources = count($sources);
    if ( $num_sources>1 )
    {
        $form['syndication_information']['syndication_publish_source'] = array(
            '#type' => 'select',
            '#title' => t('Syndication Source'),
            '#options' => $sources,
        );
    } else if ( $num_sources==1 ) {
        reset($sources);
        $only_source = key($sources);
        $form['syndication_information']['syndication_publish_source'] = array(
            '#type'  => 'hidden',
            '#value' => $only_source,
        );
    } else {
        return;
    }
    */

    $form['syndication_information']['syndication_action'] = array(
        '#type'          => 'hidden',
        '#default_value' => '',
        '#attributes'    => array( 'id'=>'syndication_action' )
    );


    /// title input box - filled in from local title
    $form['syndication_information']['syndication_publish_title'] = array(
        '#type'          => 'textfield',
        '#maxlength'     => _SYNDICATED_CONTENT_SHORTLENGTH,
        '#default_value' => $form_state['node']->title,
        '#title'         => t('Syndication Title'),
        '#disabled'      => TRUE,
    );

    $languages = _syndicated_content_languages_iso_639_2();
    $form['syndication_information']['syndication_publish_language'] = array(
        '#type'          => 'select',
        '#title'         => t('Language'),
        '#default_value' => 'eng',
        '#options'       => $languages,
    );

/*
    /// type dropdown - pulled from module settings - the list of synd-types this drupal-type can be published as
    $types = isset($syndication_types[$form_state['node']->type]) ? $syndication_types[$form_state['node']->type] : array('Collection','Html','Image');
    $form['syndication_information']['syndication_publish_type'] = array(
        '#type'          => 'select',
        '#title'         => t('Syndication Type'),
        '#default_value' => 'Html',
        '#options'       => $types,
    );
*/
    /// button: publish to syndication
    $form['syndication_information']['syndication_publish_button'] = array(
        '#type'       => 'button',
        '#value'      => 'Publish to Syndication',
        '#attributes' => array(
            'onclick' => "syndicationAction('publish');return false;",
        ),
    );
}

function _syndicated_content_alter_edit_form_published( $drupal_metadata, &$form, &$form_state, $form_id )
{
    /// give an edit-form a new area for syndication interaction
    $form['syndication_information'] = array(
        '#type'        => 'fieldset',
        '#title'       => t('Syndication'),
        '#collapsible' => TRUE,
        '#collapsed'   => TRUE,
        '#group'       => 'additional_settings',
        '#attributes'  => array(
            'class' => array('node-form-syndication-information'),
        ),
        '#weight' => -10, /// 100 -10,
        '#access' => user_access('syndication publish rights'),
    );

    $synd_metadata = @json_decode($drupal_metadata['metadata'],true);

    $form['syndication_information']['syndication_publish_title'] = array(
        '#type'          => 'textfield',
        '#maxlength'     => _SYNDICATED_CONTENT_SHORTLENGTH,
        '#default_value' => $synd_metadata['name'],
        '#title'         => t('Syndication Title'),
        '#maxlength'     => 255,
        '#disabled'      => TRUE,
    );
    $languages = _syndicated_content_languages_iso_639_2();
    $form['syndication_information']['syndication_publish_language'] = array(
        '#type'          => 'select',
        '#title'         => t('Language'),
        '#default_value' => $synd_metadata['language']['isoCode'],
        '#options'       => $languages,
    );

    $form['syndication_information']['syndicated_information_id'] = array(
      '#type'   => 'item',
      '#title'  => t('Media Id'),
      '#markup' => $drupal_metadata['media_id'],
    );

    $form['syndication_information']['syndicated_information_url'] = array(
      '#type'   => 'item',
      '#title'  => t('Source Url'),
      '#markup' => $drupal_metadata['source_url'],
    );

    if ( $drupal_metadata['tiny_url'] != 'NotMapped' )
    {
        $form['syndication_information']['syndicated_information_tiny'] = array(
            '#type'   => 'item',
            '#title'  => t('Tiny Url'),
            '#markup' => $drupal_metadata['tiny_url'],
        );
    }

    $form['syndication_information']['syndicated_information_owned'] = array(
      '#type'   => 'item',
      '#title'  => t('Content Owner'),
      '#markup' => $drupal_metadata['locally_owned'] ? 'Locally Owned: you have published this content' : 'Syndication: you are subscribed to this content',
    );

    if ( $drupal_metadata['locally_owned'] )
    {
      $form['syndication_information']['syndicated_information_published'] = array(
        '#type'   => 'item',
        '#title'  => t('Last Publish to Syndication'),
        '#markup' => $drupal_metadata['date_authored'],
      );
    } else {
      $form['syndication_information']['syndicated_information_updated'] = array(
        '#type'   => 'item',
        '#title'  => t('Last Content Update'),
        '#markup' => $drupal_metadata['date_updated'],
      );
    }

    $form['syndication_information']['syndicated_information_synced'] = array(
      '#type'   => 'item',
      '#title'  => t('Last Syndication Sync'),
      '#markup' => $drupal_metadata['date_synced'],
    );


    /*
    $form['syndication_information']['syndicated_information_source'] = array(
      '#type'   => 'item',
      '#title'  => t('Host'),
      '#markup' => $content['host'],
    );
    */

    $form['syndication_information']['syndication_action'] = array(
        '#type'          => 'hidden',
        '#default_value' => '',
        '#attributes'    => array( 'id'=>'syndication_action' )
    );

    /// We have already published this locally-owned content
    if ( $drupal_metadata['locally_owned'] )
    {
        /// button: push changes to syndication
        /*$form['syndication_information']['syndication_publish_button'] = array(
            '#type'        => 'button',
            '#value'       => 'Publish Changes to Syndication',
            '#attributes'  => array(
                'onclick' => "syndicationAction('republish');return false;",
            ),
        );*/

        // button to delete from Syndication
        $form['syndication_information']['syndication_unpublish_button'] = array(
            '#type'        => 'button',
            '#value'       => 'Delete From Syndication',
            '#attributes'  => array(
                'onclick' => "syndicationAction('unpublish');return false;",
            ),
        );


        $query = db_select('syndicated_content', 'c')
          ->fields('c')
          ->condition('node_id', $drupal_metadata['node_id'])
          ->range(0, 1);
        $exec = $query->execute();
        $sc_archived = $exec->fetchAssoc();

        if($sc_archived['archive'] == 0) {
            // button to delete from Syndication
            $form['syndication_information']['syndication_archive_button'] = array(
              '#type'        => 'button',
              '#value'       => 'Archive to Syndication',
              '#attributes'  => array(
                'onclick' => "syndicationAction('archive');return false;",
              ),
            );
        } else {
            // button to delete from Syndication
            $form['syndication_information']['syndication_unarchive_button'] = array(
              '#type'        => 'button',
              '#value'       => 'Unarchive from Syndication',
              '#attributes'  => array(
                'onclick' => "syndicationAction('unarchive');return false;",
              ),
            );
        }

    /// We are subscribed to this piece of content from synd-server
    } else {
       /// button: update from syndication source
       $form['syndication_information']['syndication_update_button'] = array(
           '#type'        => 'button',
           '#value'       => 'Update From Syndication',
           '#name'        => 'op',
           '#attributes'  => array(
               'onclick' => "return syndicationAction('update');",
           ),
       );
       /// button: un-subscribe
       $form['syndication_information']['syndication_unsubscribe_button'] = array(
           '#type'        => 'button',
           '#value'       => 'Unsubscribe From Content',
           '#name'        => 'op',
           '#attributes'  => array(
               'onclick' => "return syndicationAction('unsubscribe');",
           ),
       );
       /// checkbox: update as revision ?
    }
}

