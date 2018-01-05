<?php /**
 * @file
 * Contains \Drupal\syndicated_content\EventSubscriber\InitSubscriber.
 */

namespace Drupal\syndicated_content\EventSubscriber;

use Symfony\Component\HttpKernel\KernelEvents;
use Symfony\Component\EventDispatcher\EventSubscriberInterface;

class InitSubscriber implements EventSubscriberInterface {

  /**
   * {@inheritdoc}
   */
  public static function getSubscribedEvents() {
    return [KernelEvents::REQUEST => ['onEvent', 0]];
  }

  public function onEvent() {
    module_load_include('php', 'syndicated_content', 'includes/syndicated_content.util');
    _syndicated_content_init();
  }

}
