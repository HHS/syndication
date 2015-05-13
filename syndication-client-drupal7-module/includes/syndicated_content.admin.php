<?php

module_load_include('php','syndicated_content','includes/syndicated_content.util');

/// show links 
function _syndicated_content_settings_page()
{
return '
<ul class="admin-list">

    <li class="leaf">
        <a href="'. url('admin/config/syndicated_content/sources') .'">Content Sources</a>
        <div class="description">Configure Connections to Syndication sources.</div>
    </li>
</ul>
';
/*
    <li class="leaf">
        <a href="'. url('admin/config/syndicated_content/content') .'">Syndicated Item List</a>
        <div class="description">Configure Connections to Syndication sources.</div>
    </li>
*/
}

function _syndicated_content_listings_page()
{
    /// show all metadata for publications and subscriptions
}

/// module settings form
function _syndicated_content_form($form, &$form_state)
{
    // sources  
    $form = _syndicated_content_admin_sources_form($form,$form_state);

    return $form;
}

function _syndicated_content_admin_sources_form($form, &$form_state)
{
    $form['#id'] = 'syndicated-content-admin-sources-form';

    $drupal_types = array(''=>'') + node_type_get_names();
    unset($drupal_types['syndicated_content']);
    $form['#attached']['css'] = array(
        drupal_get_path('module', 'syndicated_content') . '/assets/syndicated_content_settings_form.css',
    );
    /// add in our custom settings tabs
    $form['syndication_sources'] = array(
        '#type'   => 'vertical_tabs',
    );

    /// I use a form-name postfix to distinguish between sources - I don't like this
    /// actions are stored in the VALUE of the button clicked - so js submit of a form won't catch it
    /// how am I really supposed to do this? multiple forms all under one page?

    $sources = db_select('syndicated_content_sources','s')
                ->fields('s')
                ->range(0,1)
                ->execute();
    while ( $source = $sources->fetchAssoc() )
    {
        /// get sources from db
        $type_map = _syndicated_content_type_mapping( $source['id'] );

        $form = _syndicated_content_create_admin_source_form( $form, $form_state, $source, $type_map );

        $org_options = array();
        foreach ( _syndicated_content_source_orgs() as $source_org )
        {
            $name = $source_org['name'];
            if ( !empty($source_org['acronym']) )
            {   
                $name =  "({$source_org['acronym']}) {$name}";
            }
            $org_options[$source_org['id']] = $name;
        }

        $org_selected = empty($source['source_org_id']) ? array( _syndicated_content_get_source_org_id() ) : array($source['source_org_id']);
        $form["syndication_sources_{$source['id']}"]["source_org_id_{$source['id']}"] = array(
            '#type'     => 'select',
            '#multiple' => false,
            '#title'    => t('Organization Name:'),
            '#options'  => $org_options,
            '#default_value' => $org_selected,
        );

        $form["syndication_sources_{$source['id']}"]['submit_update'] = array(
            '#type'  => 'submit',
            '#name'  => "syndication_sources_{$source['id']}",
            '#value' => t('Update Source'),
        );
        /*$form["syndication_sources_{$source['id']}"]['submit_delete'] = array(
            '#type'  => 'submit',
            '#attributes' => array(
                'class' => array('right-button')
            ),
            '#name'  => "syndication_sources_{$source['id']}",
            '#value' => t('Delete Source'),
            '#limit_validation_errors' => array( array("syndication_sources_{$source['id']}","id_{$source['id']}") ),
        );*/
    }

    if ( $sources->rowCount() == 0  )  /// temp: restrict to one source
    {
        $source = array(
            'id'                  => 'new_source',
            'name'                => ' + New Source',
            'source_org_id'           => '',
            'syndication_url'     => '',
            'syndication_tinyurl' => '',
            'cms_manager_url'     => '',
            'cms_manager_id'      => '',
            'key_private'         => '',
            'key_public'          => '',
            'key_secret'          => ''
        );
        $type_map = array(
            'by_syndication_type' => array(),
            'by_drupal_type'      => array(),
            'list'                => array()
        );

        $form = _syndicated_content_create_admin_source_form( $form, $form_state, $source, $type_map );

        $form["syndication_sources_{$source['id']}"]['submit'] = array(
            '#type'        => 'submit',
            '#name'        => "syndication_sources_{$source['id']}",
            '#value'       => t('Create New Source'),
        );
    }
    return $form;
}

function cmp($a, $b)
{
    if ($a->name == $b->name) {
        return 0;
    }
    return ($a->name < $b->name) ? -1 : 1;
}

function _syndicated_content_create_admin_source_form($form,&$form_state,$source,$type_map)
{
        module_load_include('php','syndicated_content','includes/syndicated_content.util');
        
        drupal_add_css(drupal_get_path('module', 'syndicated_content') . '/assets/syndicated_content_settings_form.css');
        drupal_add_js(drupal_get_path('module', 'syndicated_content') . '/assets/syndicated_content_settings_form.js');
        
        $form["syndication_sources_{$source['id']}"] = array(
            '#type'        => 'fieldset',
            '#title'       => t($source['name']),
            '#collapsible' => TRUE,
            '#collapsed'   => FALSE,
            '#group'       => 'syndication_sources',
        );

        $form["syndication_sources_{$source['id']}"]["id_{$source['id']}"] = array(
            '#type'          => 'hidden',
            '#default_value' => $source['id'],
            '#required' => true,
        );

        /*$form["syndication_sources_{$source['id']}"]["name_{$source['id']}"] = array(
            '#type'          => 'textfield',
            '#title'         => t('Syndication Source Name'),
            '#default_value' => ($source['id']=='new_source') ? '' : $source['name'],
            '#required' => false,
        );*/

        $form["syndication_sources_{$source['id']}"]["api_urls"] = array(
            '#type'  => 'fieldset',
            '#title' => t('API URLs'),
        );

        $form["syndication_sources_{$source['id']}"]["api_urls"]["syndication_url_{$source['id']}"] = array(
            '#type'          => 'hidden',
            '#title'         => t('Base Syndication API Url'),
            '#default_value' => $source['syndication_url'],
            '#required' => false,
        );
        /*
        $form["syndication_sources_{$source['id']}"]["api_urls"]["syndication_tinyurl_{$source['id']}"] = array(
            '#type'          => 'textfield',
            '#title'         => t('Base Syndication Tiny Url'),
            '#default_value' => $source["syndication_tinyurl"],
            '#required' => false,
        );*/
        $form["syndication_sources_{$source['id']}"]["api_urls"]["cms_manager_url_{$source['id']}"] = array(
            '#type'          => 'textfield',
            '#title'         => t('Base API URL'),
            '#default_value' => $source["cms_manager_url"],
        );
        $form["syndication_sources_{$source['id']}"]["api_urls"]["ssl_auth_{$source['id']}"] = array(
            '#type'          => 'checkbox',
            '#title'         => t('Bypass SSL'),
            '#default_value' => isset($source['ssl_auth']) ? $source['ssl_auth'] : "",
        );
        $form["syndication_sources_{$source['id']}"]["api_identiy"] = array(
            '#type'  => 'fieldset',
            '#title' => t('API Identity'),
        );
        /*
        $form["syndication_sources_{$source['id']}"]["api_identiy"]["cms_manager_id_{$source['id']}"] = array(
            '#type'          => 'textfield',
            '#title'       => t('This Site\'s CMS Manager Id'),
            '#default_value' => $source["cms_manager_id"],
        );*/
        $form["syndication_sources_{$source['id']}"]["api_identiy"]["key_private_{$source['id']}"] = array(
            '#type'          => 'textfield',
            '#title'         => t('Private Key'),
            '#default_value' => $source['key_private'],
        );
        $form["syndication_sources_{$source['id']}"]["api_identiy"]["key_public_{$source['id']}"] = array(
            '#type'          => 'textfield',
            '#title'         => t('Public Key'),
            '#default_value' => $source['key_public'],
        );
        $form["syndication_sources_{$source['id']}"]["api_identiy"]["key_secret_{$source['id']}"] = array(
            '#type'          => 'textfield',
            '#title'         => t('Secret Key'),
            '#default_value' => $source['key_secret'],
        );
        $form["syndication_sources_{$source['id']}"]["api_identiy"]["key_json_{$source['id']}"] = array(
            '#type'          => 'textarea',
            '#title'         => t('JSON Keys'),
            '#default_value' => "",
        );
        /*
        $form["syndication_sources_{$source['id']}"]["api_identiy"]['submit_test'] = array(
            '#type'  => 'submit',
            '#name'  => "syndication_sources_{$source['id']}",
            '#value' => t('Test Credentials'),
            '#suffix' => '<br /><br />',
            '#limit_validation_errors' => array( array("syndication_sources_{$source['id']}","id_{$source['id']}") ),
        );*/

        /// content types that are allowed to be syndicated
        $form["syndication_sources_{$source['id']}"]["type_mapping"] = array(
            '#type'  => 'fieldset',
            '#title' => t('Content Types'),
        );
        
        $contentTypes  = _syndicated_content_known_types();
        $contentCustom = _syndicated_content_known_types('extended');
        
        $options = array();
        
        $nTypes = node_type_get_types();
        usort($nTypes, "cmp");
        
        foreach ( $nTypes as $type )
        {
            if ( $type->type == 'syndicated_content' ) { continue; }
            $options[$type->type] = $type->name;
            
            $form["syndication_sources_{$source['id']}"]["type_mapping"]["allowed_types_for_{$type->type}_{$source['id']}"] = array(
                '#type'     => 'select',
                '#multiple' => false,
                '#title'    => $type->name,
                '#options'  => $contentTypes,
                '#default_value' => isset($type_map['by_drupal_type'][$type->type][0]) ? $type_map['by_drupal_type'][$type->type][0] : ""
            );
            
            if(isset($type_map['by_drupal_type'][$type->type][0])) {
                if($contentCustom[$type_map['by_drupal_type'][$type->type][0]] == "custom") {
                    $fields = array();
                    $instances = field_info_instances('node', $type->type);
                    //$extra_fields = field_info_extra_fields('node', $type->type, 'form');
                    
                    // Fields.
                    $fields = array(' - Automatic -');
                    foreach ($instances as $name => $instance) {
                        if(strpos($name, "field_") !== false) {
                            $fields[$name] = $instance['label']." (".$name.")";
                        }
                    }
                    $form["syndication_sources_{$source['id']}"]["type_mapping"]["allowed_types_for_{$type->type}_field_{$source['id']}"] = array(
                        '#type'     => 'select',
                        '#multiple' => false,
                        '#title'    => "Field",
                        '#title_display'=> "invisible",
                        '#attributes'   => array('class'=>array("field_layer")),
                        '#options'  => $fields,
                        '#default_value' => isset($type_map['by_drupal_type'][$type->type][1]) ? $type_map['by_drupal_type'][$type->type][1] : ""
                    );
                }
            }
        }
        
        /*
        $selected = array_keys($type_map['by_drupal_type']);
        $form["syndication_sources_{$source['id']}"]["type_mapping"]["allowed_types_{$source['id']}"] = array(
            '#type'     => 'select',
            '#multiple' => true,
            '#title'    => t('Syndication Allowed For:'),
            '#options'  => $options,
            '#default_value' => $selected
        );*/
        return $form;
}

/// module settings handler
function _syndicated_content_admin_sources_form_validate($form, &$form_state)
{
    if ( $form['#id'] == 'syndicated-content-admin-sources-form' )
    {
        /// this is a terrible way to choose which source and which action
        /// where am I supposed to really put this?
        if ( isset($form_state['triggering_element']) && isset($form_state['triggering_element']['#name']) )
        {
            $_syndicated_content_form_id = $form_state['triggering_element']['#name'];
            if ( $_syndicated_content_form_id == 'syndication_sources_new_source' )
            {
                _syndicated_content_create_source_from_form($form,$form_state);
            } else if ( isset($form_state['values'][$_syndicated_content_form_id]) ) {
                $source_id = substr($_syndicated_content_form_id,strlen('syndication_sources_'));
                /// if the button has the word 'update'
                if ( stristr($form_state['values'][$_syndicated_content_form_id],'update')!==FALSE )
                {
                    _syndicated_content_update_source_from_form($form,$form_state,$source_id);
                    _syndicated_content_test_source_from_form($form,$form_state,$source_id);
                /// if the button has the word 'delete'
                } else if ( stristr($form_state['values'][$_syndicated_content_form_id],'delete')!==FALSE ) {
                    _syndicated_content_delete_source_from_form($form,$form_state,$source_id);
                /// if the button has the word 'test'
                } else if ( stristr($form_state['values'][$_syndicated_content_form_id],'test')!==FALSE ) {
                    _syndicated_content_test_source_from_form($form,$form_state,$source_id);
                }
            }
        }
    }
    return $form;
}

function _syndicated_content_create_source_from_form($form, &$form_state)
{
    list($key_private, $key_public, $key_secret) = _syndicated_content_decode_keys($form_state['values'], 0);
    
    $source_id = db_insert('syndicated_content_sources')
        ->fields(array('name','syndication_url','syndication_tinyurl','key_public','key_private','key_secret','cms_manager_url','cms_manager_id', 'ssl_auth' ))
        ->values(array(
            'name'                 => "Syndication Service",
            'source_org_id'        => $form_state['values']['source_org_id_new_source'],
            'syndication_url'      => $form_state['values']['cms_manager_url_new_source'].(substr($form_state['values']["cms_manager_url_new_source"], -1) == "/" ? "" : "/")."api/v2",
            'syndication_tinyurl'  => $form_state['values']["cms_manager_url_new_source"].(substr($form_state['values']["cms_manager_url_new_source"], -1) == "/" ? "" : "/")."TinyUrl",
            'key_private'          => $key_private,
            'key_public'           => $key_public,
            'key_secret'           => $key_secret,
            'cms_manager_url'      => $form_state['values']['cms_manager_url_new_source'],
            'cms_manager_id'       => "ss_manager_id", //$form_state['values']['cms_manager_id_new_source']
            'ssl_auth'             => $form_state['values']['ssl_auth_new_source']
            ))
        ->execute();
    if ( $source_id!==null )
    {
        db_delete('syndicated_content_types')
            ->condition('syndication_source_id',$source_id,'=')
            ->execute();
        
        $ins = db_insert('syndicated_content_types')
            ->fields(array('syndication_source_id','syndication_type','drupal_type','drupal_field'));
        //foreach ( $form_state['values']["allowed_types_new_source"] as $drupal_type )
        foreach ( node_type_get_types() as $type )
        {
            if ( empty($type->type) ) { continue; }
            /// ONLY HTML ALLOWED RIGHT NOW
            $syndicationType = isset($form_state['values']["allowed_types_for_{$type->type}_new_source"]) ? $form_state['values']["allowed_types_for_{$type->type}_new_source"] : "";
            //$drupalField     = isset($form_state['values']["allowed_types_for_{$type->type}_field_new_source"]) ? $form_state['values']["allowed_types_for_{$type->type}_field_new_source"] : "";
            if($syndicationType != "")
                $ins->values(array(
                    'syndication_source_id' => $source_id,
                    'syndication_type'      => $syndicationType,
                    'drupal_type'           => $type->type,
                    'drupal_field'          => ""
                ));
        }
        $ins->execute();
    }
}

function _syndicated_content_update_source_from_form($form, &$form_state, $source_id)
{
    list($key_private, $key_public, $key_secret) = _syndicated_content_decode_keys($form_state['values'], $source_id);
    
    db_update('syndicated_content_sources')
        ->fields(array(
            'name'                 => "Syndication Service",
            'source_org_id'        => $form_state['values']["source_org_id_{$source_id}"],
            'syndication_url'      => $form_state['values']["cms_manager_url_{$source_id}"].(substr($form_state['values']["cms_manager_url_{$source_id}"], -1) == "/" ? "" : "/")."api/v2",
            'syndication_tinyurl'  => $form_state['values']["cms_manager_url_{$source_id}"].(substr($form_state['values']["cms_manager_url_{$source_id}"], -1) == "/" ? "" : "/")."TinyUrl",
            'key_private'          => $key_private,
            'key_public'           => $key_public,
            'key_secret'           => $key_secret,
            'cms_manager_url'      => $form_state['values']["cms_manager_url_{$source_id}"],
            'cms_manager_id'       => "ss_manager_id",
            'ssl_auth'             => $form_state['values']["ssl_auth_{$source_id}"] ))
        ->condition('id', $source_id)
        ->execute();
    drupal_set_message('Syndication source updated');
    db_delete('syndicated_content_types')
        ->condition('syndication_source_id',$source_id,'=')
        ->execute();
    $ins = db_insert('syndicated_content_types')
            ->fields(array('syndication_source_id','syndication_type','drupal_type','drupal_field'));
    
    foreach ( node_type_get_types() as $type )
    {
        if ( $type->type == 'syndicated_content' ) { continue; }
        if ( empty($type->type) ) { continue; }
        
        $syndicationType = isset($form_state['values']["allowed_types_for_{$type->type}_$source_id"]) ? $form_state['values']["allowed_types_for_{$type->type}_$source_id"] : "";
        $drupalField     = isset($form_state['values']["allowed_types_for_{$type->type}_field_$source_id"]) ? $form_state['values']["allowed_types_for_{$type->type}_field_$source_id"] : "";
        $drupalField     = ($drupalField == "0" ? "" : $drupalField);
        
        if($syndicationType != "")
            $ins->values(array(
                'syndication_source_id' => $source_id,
                'syndication_type'      => $syndicationType,
                'drupal_type'           => $type->type,
                'drupal_field'          => $drupalField
            ));
    }
    $ins->execute();
}
function _syndicated_content_decode_keys($formVals, $source_id) {
    $source_id = ($source_id > 0) ? $source_id : "new_source";

    $json           = isset($formVals["key_json_{$source_id}"])    ? $formVals["key_json_{$source_id}"]    : "";
    $key_private    = isset($formVals["key_private_{$source_id}"]) ? $formVals["key_private_{$source_id}"] : "";
    $key_public     = isset($formVals["key_public_{$source_id}"])  ? $formVals["key_public_{$source_id}"]  : "";
    $key_secret     = isset($formVals["key_secret_{$source_id}"])  ? $formVals["key_secret_{$source_id}"]  : "";
    
    if($json != "") {
        $json = str_replace(" Key", "Key", $json);
        $json = json_decode($json);
        $key_private = isset($json->PrivateKey) ? $json->PrivateKey : $key_private;
        $key_public  = isset($json->PublicKey)  ? $json->PublicKey  : $key_public;
        $key_secret  = isset($json->SecretKey)  ? $json->SecretKey  : $key_secret;
    }
    
    return array($key_private, $key_public, $key_secret);
}
function _syndicated_content_delete_source_from_form($form, &$form_state, $source_id)
{
    db_delete('syndicated_content_sources')
        ->condition('id',$source_id,'=')
        ->execute();
    db_delete('syndicated_content_types')
        ->condition('syndication_source_id',$source_id,'=')
        ->execute();
}

function _syndicated_content_test_source_from_form($form, &$form_state, $source_id)
{
    module_load_include('php','syndicated_content','includes/syndicated_content.util');

    $syndication = _syndicated_content_api_factory();
    
    $ssl_auth = isset($form_state['values']["ssl_auth_{$source_id}"]) ? $form_state['values']["ssl_auth_{$source_id}"] : (isset($form_state['values']["ssl_auth_new_source"]) ? $form_state['values']["ssl_auth_new_source"] : 0);
    
    $response = $syndication->testCredentials(array('ssl_auth'=>$ssl_auth));
    if ( !empty($response->status) && $response->status=='200' ) 
    {
       drupal_set_message('Your Credentials Work');
    } else {
       drupal_set_message('Invalid Credentials','error');
    }
}

