<?php
namespace Drupal\syndicated_content;

class SyndicationContentUnknowtoClientException extends Exception
{
    function _construct( $message='Media Item Unknown to Client', $code=SYNDICATION_CONTENT_UNKNOWN_TO_DRUPAL, Exception $previous=null )
    {
        parent::_construct($message, $code, $previous);
    }
}
