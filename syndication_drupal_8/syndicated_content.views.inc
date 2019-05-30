<?php

/**
 * Implements hook_views_data().
 */
function syndicated_content_views_data() {

  $data = array();

  // base data
  $data['syndicated_content']['table']['group'] = t('Syndicated Content');

  $data['syndicated_content']['table']['base'] = [
    'title' => t('Syndicated Content'),
    'help' => t('Content that is sent to the Syndication API System.'),
    // need to create query plugin in order to use query_id
    //'query_id' => 'syndicatedcontent',
  ];

  // join node_field_data table
  $data['syndicated_content']['table']['join'] = [
    'node_field_data' => array(
      'left_field' => 'nid',
      'field' => 'node_id',
    ),
  ];


  $data['syndicated_content']['node_id'] = array(
    'title' => t('Syndicated Content'),
    'help' => t('Relate syndiated content to the node content'),
    'relationship' => array(
      'base' => 'node_field_data',
      'base field' => 'nid',
      'id' => 'standard',
      'label' => t('Syndicated Content'),
    ),
  );

  $data['syndicated_content']['media_id'] = [
    'title' => t('Media ID'),
    'help' => t('Media ID from the Syndication System.'),
    'field' => [
      'id' => 'numeric',
    ],
  ];

  $data['syndicated_content']['date_synced'] = [
    'title' => t('Date Synced'),
    'help' => t('Date content was syndicated.'),
    'field' => [
      'id' => 'date',
    ],
  ];

  $data['syndicated_content']['source'] = [
    'title' => t('Organization Name'),
    'help' => t('Sourc organization name.'),
    'field' => [
      'id' => 'standard',
    ],
  ];

  $data['syndicated_content']['source_url'] = [
    'title' => t('Source URL'),
    'help' => t('Node url of the syndicated content..'),
    'field' => [
      'id' => 'standard',
    ],
  ];

  return $data;
}