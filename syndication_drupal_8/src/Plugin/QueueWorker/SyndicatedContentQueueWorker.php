<?php

namespace Drupal\syndicated_content\Plugin\QueueWorker;

use Drupal\Core\Queue\QueueWorkerBase;

/**
 *
 * @QueueWorker(
 * id = "syndicated_content_message_queue",
 * title = @Translation("Syndicated queue processor"),
 * cron = {"time" = 60}
 * )
 */
class SyndicatedContentQueueWorker extends QueueWorkerBase  {

  /**
   * {@inheritdoc}
   */
  public function processItem($data) {

    module_load_include('php','syndicated_content','includes/syndicated_content.data');
    set_time_limit(0);
    _syndicated_content_node_update($data->nid, $data->syndication_language, $data->syndication_action);
  }
}