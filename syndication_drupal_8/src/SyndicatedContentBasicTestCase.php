<?php
namespace Drupal\syndicated_content;

class SyndicatedContentBasicTestCase extends DrupalWebTestCase
{

    public static function getInfo()
    {
      return array(
        'name'        => 'Form tests',
        'description' => 'Test Form Alterations',
        'group'       => 'Syndication',
      );
    }

    public function setUp()
    {
        parent::setUp('syndicated_content');

        $perms = user_role_permissions(array(array_search('administrator', user_roles()) => 'administrator'));
        $perms = array_keys($perms[array_search('administrator', user_roles())]);
        $admin_account = $this->drupalCreateUser($perms);
        $this->drupalLogin($admin_account);
        $this->admin_user = \Drupal::entityManager()->getStorage('user')->load($admin_account->uid);

        db_insert('syndicated_content_sources')->fields(array(
            'id'        => 99999,
            'name'      => 'SimpleTestSource',
            'api_url'   => 'http://ctacdev.com:8090/Syndication/api/v1/resources',
            'api_key'   => 'APIKEY',
            'cms_url'   => 'http://ctacdev.com:8090/cms_manager/api/v1/resources',
            'cms_id'    => '99999',
            'tiny_url'  => 'http://ctacdev.com:8090'
        ))->execute();
        $this->$syndication = _syndicated_content_api_factory('99999');

        $this->content = array();

        $this->content['oc']  = $this->drupalCreateNode();

        $this->content['pub'] = $this->drupalCreateNode();
        db_insert('syndicated_content')->fields(array(
            'node_id'        => $this->content['pub']->nid,
            'source_id'      => 99999,
            'media_id'       => 11,
            'media_type'     => 'Html',
            'source_url'     => 'http://www.google.com',
            'tiny_url'       => 'http://www.google.com',
            'locally_owned'  => 1,
            'date_authored'  => gmdate($this->syndication->date_format,strtotime('-2 Hour')),
            'date_updated'   => gmdate($this->syndication->date_format,strtotime('-1 Hour')),
            'date_synced'    => gmdate($this->syndication->date_format),
            'metadata'       => '',
        ))->execute();

        $this->content['sub'] = $this->drupalCreateNode();
        db_insert('syndicated_content')->fields(array(
            'node_id'        => $this->content['sub']->nid,
            'source_id'      => 99999,
            'media_id'       => 22,
            'media_type'     => 'Html',
            'source_url'     => 'http://www.google.com',
            'tiny_url'       => 'http://www.google.com',
            'locally_owned'  => 0,
            'date_authored'  => gmdate($this->syndication->date_format,strtotime('-2 Day')),
            'date_updated'   => gmdate($this->syndication->date_format,strtotime('-1 Day')),
            'date_synced'    => gmdate($this->syndication->date_format),
            'metadata'       => '',
        ))->execute();
    }

    public function tearDown()
    {
        parent::tearDown('syndicated_content');
        db_delete('syndicated_content_sources')
            ->condition('id',99999,'=')
            ->execute();
        db_delete('syndicated_content')
            ->condition('source_id',99999,'=')
            ->execute();
    }

    function testSubscriptionForm()
    {
        $user = \Drupal::currentUser();
        $user = $this->admin_user;

        $url  = "node/add/syndicated-content";
        $this->drupalGet($url);

        $this->assertNoField('edit-body-und-0-value',  'Field removed: body');
        $this->assertField('syndication_search_value', 'Field exists: syndication_search_value');
        $this->assertField('syndication_lookup_value', 'Field exists: syndication_lookup_value');
    }

    function testEditUnPublishedForm()
    {
        $user = \Drupal::currentUser();
        $user = $this->admin_user;

        $url  = "node/{$this->content['oc']->nid}/edit";
        $this->drupalGet($url);

        $this->assertField('syndication_publish_title',       'Field exists: publish title');
        $this->assertField('syndication_publish_type',        'Field exists: publish type');
        $this->assertField('edit-syndication-publish-button', 'Button exists: publish');
    }

    function testEditPublishedForm()
    {
        $user = \Drupal::currentUser();
        $user = $this->admin_user;

        $url  = "node/{$this->content['pub']->nid}/edit";
        $this->drupalGet($url);

        $this->assertFieldByXPath('//*[@id="edit-syndicated-information-id"]', NULL, 'Field exists: syndication id');
        $this->assertField('edit-syndication-unpublish-button',    'Button exists: unpublish');
        $this->assertField('edit-syndication-publish-button',      'Button exists: publish');
    }

    function testEditSubscribedForm()
    {
        $user = \Drupal::currentUser();
        $user = $this->admin_user;

        $url  = "node/{$this->content['sub']->nid}/edit";
        $this->drupalGet($url);

        $this->assertFieldByXPath('//*[@id="edit-syndicated-information-id"]', NULL, 'Field exists: syndication id');
        $this->assertField('edit-syndication-update-button',       'Button exists: unpublish');
        $this->assertField('edit-syndication-unsubscribe-button',  'Button exists: publish');
    }
/*
    function testUpdateListener()
    {
        /// public access
        $this->drupalLogout();

        $request_options = array(
            CURLOPT_POST   => TRUE,
            CURLOPT_URL    => url("syndicated_content/subscription"),
            CURLOPT_NOBODY => FALSE,
            CURLOPT_POSTFIELDS => '{"media_id":"FAKEID"}',
            CURLOPT_HTTPHEADER => array('Content-type: application/json;'),
        );
        $response = $this->curlExec($request_options);
        $this->assertResponse(400,'Bad Media ID = 400 : '.curl_getinfo($this->curlHandle, CURLINFO_HTTP_CODE) );
        /// should respond 400 to a nonexistant id
        /// should respond 200 with a valid media_id in BODY
        #$this->drupalPost($url, null, null, null, null, null, '{"media_id":"11"}');
        #$this->assertResponse(400,'JSON Good Media ID = 200');
        /// should respond 200 with a valid media_id in a POST
        #$this->drupalPost($url, null, array("media_id"=>"11"));
        #$this->assertResponse(200,'POST Good Media ID = 200');
        /// should respond 200 with a valid media_id in a GET
        #$this->drupalGet($url.'?'.drupal_http_build_query(array("media_id"=>"11")));
        #$this->assertResponse(200,'GET Good Media ID = 200');
    }
*/

}
