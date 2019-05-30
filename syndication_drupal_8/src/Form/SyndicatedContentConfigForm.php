<?php

namespace Drupal\syndicated_content\Form;

use Drupal\Core\Form\ConfigFormBase;
use Drupal\Core\Form\FormStateInterface;


module_load_include('php','syndicated_content','includes/syndicated_content.util');

/**
 * Class SyndicatedContentConfigForm.
 */
class SyndicatedContentConfigForm extends ConfigFormBase {

  /**
   * {@inheritdoc}
   */
  protected function getEditableConfigNames() {
    return [
      'syndicated_content.syndicatedcontentconfig',
    ];
  }

  /**
   * {@inheritdoc}
   */
  public function getFormId() {
    return 'syndicated_content_config_form';
  }

  /**
   * {@inheritdoc}
   */
  public function buildForm(array $form, FormStateInterface $form_state) {

    $form['source_id'] = array('#type' => 'hidden', '#value' => $this->loadSyndicationSource()['id']);

    $form['base_api_url'] = [
      '#type' => 'textfield',
      '#title' => $this->t('Base API URL'),
      '#default_value' => $this->loadSyndicationSource()['cms_manager_url'],
      '#size' => 60,
    ];

    $form['bypass_ssl'] = [
      '#type' => 'checkboxes',
      '#options' => ['1' => $this->t('Bypass SSL')],
      '#default_value' => $this->loadSyndicationSource()['ssl_auth'],
    ];

    $form['public_url'] = [
      '#type' => 'textfield',
      '#title' => $this->t('Public URL'),
      '#default_value' => $this->loadSyndicationSource()['cms_url'],
      '#size' => 60,
    ];

    $form['private_key'] = [
      '#type' => 'textfield',
      '#title' => $this->t('Private Key'),
      '#default_value' => $this->loadSyndicationSource()['key_private'],
      '#size' => 120,
    ];

    $form['public_key'] = [
      '#type' => 'textfield',
      '#title' => $this->t('Public Key'),
      '#default_value' => $this->loadSyndicationSource()['key_public'],
      '#size' => 120,
    ];

    $form['secret_key'] = [
      '#type' => 'textfield',
      '#title' => $this->t('Secret Key'),
      '#default_value' => $this->loadSyndicationSource()['key_secret'],
      '#size' => 120,
    ];

    // get the content types
    $contentTypes = \Drupal::service('entity_type.manager')->getStorage('node_type')->loadMultiple();

    $conn = \Drupal::database();

    foreach ($contentTypes as $contentType) {

      // get the syndication type to update the select form.
      $syn_type = $conn->select('syndicated_content_types', 'sct')
        ->fields('sct')
        ->condition('sct.drupal_type', $contentType->id())
        ->execute()
        ->fetchAssoc();

      $form['syndication_' . $contentType->id()] = [
        '#type' => 'select',
        '#title' => $contentType->label(),
        '#options' => [
          'html' => $this->t('Html'),
          'image' => $this->t('Image'),
          'infographic' => $this->t('Infographic'),
          'video' => $this->t('Video'),
        ],
        '#default_value' => $syn_type['syndication_type'],
      ];

      // auto syndicate
      // check if field exists
      $check_auto_syndicated_field = $conn->schema()->fieldExists('syndicated_content_types', 'auto_syndicate');
      if($check_auto_syndicated_field) {
        // check to see if content type is auto syndicated
        if ($syn_type['auto_syndicate'] == 1) {
          $auto_syn_checked = 'checked';
        } else {
          $auto_syn_checked = NULL;
        }
        $form['auto_syndicate_' . $contentType->id()] = [
          '#type' => 'checkboxes',
          '#attributes' => array($auto_syn_checked => $auto_syn_checked),
          '#options' => ['1' => $this->t('Auto Syndicate')],
          '#default_value' => $syn_type['auto_syndicate'],
        ];
      }
    }

    // start organization name dropdown
    $org_options = array();

    foreach ( _syndicated_content_source_orgs() as $source_org )  {

      $name = $source_org['name'];

      if ( !empty($source_org['acronym']) ) {

        $name =  "({$source_org['acronym']}) {$name}";
      }

      $org_options[$source_org['id']] = $name;
    }

    $org_selected = empty($source['source_org_id']) ? array( _syndicated_content_get_source_org_id() ) : array($source['source_org_id']);

    $form["syndication_sources_{$source['id']}"]["source_org_id_{$source['id']}"] = [
      '#type' => 'select',
      '#title' => 'Organization Name',
      '#options' => $org_options,
      '#default_value' => $org_selected,
    ];

    return parent::buildForm($form, $form_state);
  }

  /**
   * {@inheritdoc}
   */
  public function validateForm(array &$form, FormStateInterface $form_state) {

    parent::validateForm($form, $form_state);

    $syndication = _syndicated_content_api_factory();

    $ssl_auth = $form_state->getValue('source_id');

    $syndication->api['syndication_url'] = $this->loadSyndicationSource()['syndication_url'];
    $syndication->api['syndication_tinyurl'] = $this->loadSyndicationSource()['syndication_tinyurl'];
    $syndication->api['cms_manager_url'] = $this->loadSyndicationSource()['cms_manager_url'];
    $syndication->api['cms_manager_id'] = 'ss_manager_id';
    $syndication->api['cms_url'] = $this->loadSyndicationSource()['cms_url'];
    $syndication->api['key_secret'] = $this->loadSyndicationSource()['key_secret'];
    $syndication->api['key_public'] = $this->loadSyndicationSource()['key_public'];
    $syndication->api['key_private'] = $this->loadSyndicationSource()['key_private'];

    $response = $syndication->testCredentials(array('ssl_auth'=>$ssl_auth));


    if ( !empty($response->status) && $response->status=='200' )
    {
      drupal_set_message('Syndication Successfully Connected to the Server.');
    } else {
      switch($response->status) {
        case 400:
          drupal_set_message('Syndication could not verify your keys. Please check your Private, Public, and Secret Keys.','error');
          $_SESSION['syndication_error'] = "keys";
          break;
        case 404:
          drupal_set_message('Syndication found a Server, but not a Syndication Server. Please check your Base API URL.','error');
          $_SESSION['syndication_error'] = "url";
          break;
        case "":
          drupal_set_message('Syndication had no response from any Server. Please check your Base API URL.','error');
          $_SESSION['syndication_error'] = "url";
          break;
        default:
          drupal_set_message('Unknown Error when establishing connection to Syndication Server.','error');
          $_SESSION['syndication_error'] = "url";
          break;
      }
    }

  }

  /**
   * {@inheritdoc}
   */
  public function submitForm(array &$form, FormStateInterface $form_state) {

    parent::submitForm($form, $form_state);

    // check to see if there is a source update or insert a new one
    if($this->loadSyndicationSource()['id'] != NULL) {

      $this->updateSyncicationSource($form_state);

    } else {

      $this->insertSyndicationSource($form_state);
    }
  }

  /**
   * Check the database and see if there is a syndicated source.
   */
  private function loadSyndicationSource() {

    $conn = \Drupal::database();

    $source = $conn->select('syndicated_content_sources', 'scs')
      ->fields('scs')
      ->execute()
      ->fetchAssoc();

    return $source;
  }

  /**
   * Insert a new source
   */
  private function insertSyndicationSource($form_state) {

    $conn = \Drupal::database();

    // insert the syncication content sources
    $insert_record = $conn->insert('syndicated_content_sources')->fields(
      array(
        'name' => 'Syndication Service',
        'syndication_url' => $form_state->getValue('base_api_url') . '/api/v2',
        'syndication_tinyurl' => $form_state->getValue('base_api_url') . '/TinyUrl',
        'source_org_id' => $form_state->getValue('source_org_id_')[0],
        'cms_manager_url' => $form_state->getValue('base_api_url'),
        'cms_manager_id' => 'ss_manager_id',
        'key_private' => $form_state->getValue('private_key'),
        'key_public' => $form_state->getValue('public_key'),
        'key_secret' => $form_state->getValue('secret_key'),
        'ssl_auth' => $form_state->getValue('bypass_ssl')[1],
        'cms_url' => $form_state->getValue('public_url')
      )
    )->execute();

    // first remove all the content types
    // you can only choose 1 options form the drop down in configuration.
    $conn->delete('syndicated_content_types')->execute();

    $contentTypes = \Drupal::service('entity_type.manager')->getStorage('node_type')->loadMultiple();

    foreach ($contentTypes as $contentType) {

      $conn->insert('syndicated_content_types')->fields(
        array(
          'syndication_source_id' => $insert_record,
          'syndication_type' => $form_state->getValue('syndication_' . $contentType->id()),
          'drupal_type' => $contentType->id()
        )
      )->execute();
    }
  }

  /**
   * Update an existing source
   */
  private function updateSyncicationSource($form_state) {

    $conn = \Drupal::database();

    $conn->update('syndicated_content_sources')
      ->condition('id', $form_state->getValue('source_id'))
      ->fields([
        'syndication_url' => $form_state->getValue('base_api_url') . '/api/v2',
        'syndication_tinyurl' => $form_state->getValue('base_api_url') . '/TinyUrl',
        'source_org_id' => $form_state->getValue('source_org_id_')[0],
        'cms_manager_url' => $form_state->getValue('base_api_url'),
        'key_private' => $form_state->getValue('private_key'),
        'key_public' => $form_state->getValue('public_key'),
        'key_secret' => $form_state->getValue('secret_key'),
        'ssl_auth' => $form_state->getValue('bypass_ssl')[1],
        'cms_url' => $form_state->getValue('public_url'),
      ])->execute();

    // first remove all the content types
    // you can only choose 1 options form the drop down in configuration.
    $conn->delete('syndicated_content_types')->execute();
     
    $contentTypes = \Drupal::service('entity_type.manager')->getStorage('node_type')->loadMultiple();

    foreach ($contentTypes as $contentType) {

      $conn->insert('syndicated_content_types')->fields(
        array(
          'syndication_source_id' => $form_state->getValue('source_id'),
          'syndication_type' => $form_state->getValue('syndication_' . $contentType->id()),
          'drupal_type' => $contentType->id(),
          'auto_syndicate' => $form_state->getValue('auto_syndicate_' . $contentType->id())[1]
        )
      )->execute();
    }

  }

}
