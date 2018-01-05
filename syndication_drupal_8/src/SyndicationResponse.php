<?php
namespace Drupal\syndicated_content;

/**
 * SyndicationResponse 
 * 
 * @author Dan Narkiewicz <dnarkiewicz@ctacorp.com> 
 * @package CTAC\Syndication
 */
class SyndicationResponse
{
  /**
   * http-response content-type 
   * 
   * @var string
   *
   * @access public
   */
  var $format  = null;

  /**
   * http-reponse status 
   * 
   * @var mixed
   *
   * @access public
   */
  var $status  = null;
 
  /**
   * Array of messages
   * 
   * @var array
   * @array format
   *    errorMessage : string
   *    errorDetail  : mixed
   *    errorCode    : string
   *
   * @access public
   */
  var $messages = array();

  /**
   * List of result data 
   * 
   * @var array
   *
   * @access public
   */
  var $results = array();

  /**
   * Success of requested operation, not of http-request
   * 
   * @var boolean
   *
   * @access public
   */
  var $success = null;

  /**
   * Pagination Information 
   * 
   * @var array
   * @array format
   *    count       : int
   *    currentUrl  : string
   *    max         : int
   *    nextUrl     : string
   *    offset      : int
   *    pageNum     : int
   *    previousUrl : string
   *    sort        : string
   *    total       : int
   *    totalPages  : int
   *
   * @access public
   */
  var $pagination = array();

  /**
   * Raw request body content. JSON is decoded. 
   * 
   * @var string
   *
   * @access public
   */
  var $raw = null;

  /**
   * Format for server error messages      
   * 
   * @var array 
   * @array format
   *     errorMessage : string
   *     errorDetail  : mixed
   *     errorCode    : string
   *
   * @access public
   */
  var $empty_message = array(
            'errorMessage' => null,
            'errorDetail'  => null,
            'errorCode'    => null
      );

  /**
   * Format for pagination responses
   *
   * @var array
   * @array format
   *    count       : int
   *    currentUrl  : string
   *    max         : int
   *    nextUrl     : string
   *    offset      : int
   *    pageNum     : int
   *    previousUrl : string
   *    sort        : string
   *    total       : int
   *    totalPages  : int
   *
   * @access public
   */
  var $empty_pagination = array(
            'count'       => null,
            'currentUrl'  => null,
            'max'         => null,
            'nextUrl'     => null,
            'offset'      => null,
            'order'       => null,
            'pageNum'     => null,
            'previousUrl' => null,
            'sort'        => null,
            'total'       => null,
            'totalPages'  => null
      );

  /**
   * Constructor for Response 
   * 
   * @access protected
   * 
   * @return self
   */
  function __construct()
  {
    $this->success    = null;
    $this->format     = null;
    $this->status     = null;
    $this->messages   = array();
    $this->results    = array();
    $this->pagination = $this->empty_pagination;
    $this->raw        = null;
  }

  /**
   * Add pagination info to response
   * 
   * @param array $pagination options
   *    count       : int
   *    currentUrl  : string
   *    max         : int
   *    nextUrl     : string
   *    offset      : int
   *    pageNum     : int
   *    previousUrl : string
   *    sort        : string
   *    total       : int
   *    totalPages  : int
   *
   * @access public
   * 
   * @return void
   */
  function addPagination( $pagination )
  {
    $this->pagination = array_merge($this->empty_pagination,$pagination);
  }

  /**
   * Add message to list of response messages 
   * 
   * @param mixed $message message 
   * @access public
   * 
   * @return void
   */
  function addMessage( $message )
  {
    if ( isset($message['errorMessage']) )
    {
        $this->messages[] = array_merge($this->empty_message,$message);
    } else if ( isset($message[0]) && isset($message[0]['errorMessage']) ) {
        foreach ( $message as $m )
        {
            $this->messages[] = array_merge($this->empty_message,$m);
        }
    }
  }
}
