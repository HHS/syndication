<?php /**
 * @file
 * Contains \Drupal\syndicated_content\Controller\DefaultController.
 */

namespace Drupal\syndicated_content\Controller;

use Drupal\Core\Controller\ControllerBase;

/**
 * Default controller for the syndicated_content module.
 */
class DefaultController extends ControllerBase {

  public function syndicated_content_settings_page() {
    module_load_include('php', 'syndicated_content', 'includes/syndicated_content.admin');
    return _syndicated_content_settings_page();
  }

}
