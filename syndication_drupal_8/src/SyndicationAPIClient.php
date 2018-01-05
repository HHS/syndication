<?php
namespace Drupal\syndicated_content;

/**
 * SyndicationAPIClient 
 * 
 * @author Dan Narkiewicz <dnarkiewicz@ctacorp.com> 
 * @package CTAC\Syndication
 */
class SyndicationAPIClient
{
    /**
     * Settings for outgoing requests
     *
     * @var array
     * @array format
     *
     * @access public
     */
    var $api = array(
        'syndication_base'    => '',
        'syndication_url'     => '',
        'syndication_tinyurl' => '',
        'cms_manager_base'    => '',
        'cms_manager_url'     => '',
        'cms_manager_id'      => '',
        'cms_url'             => '',
        'key_secret'          => '',
        'key_public'          => '', 
        'key_private'         => ''
    );

    /**
     * Date format required by Syndication Server 
     * 
     * @var string 
     * @access public
     */
    var $date_format = 'Y-m-d\TH:i:s\Z';

    /**
     * Constructor for Syndication interface
     * 
     * @param mixed $api array of or filepath to config settings
     * @param mixed $key associative key within config of syndication settings
     * @access protected
     * 
     * @return Syndication
     */
    function __construct ( $api=null, $key=null )
    {
        $settings = array();
        if ( is_array($api) )
        {
            $settings = $api;
        } else if ( is_string($api) && is_file($api) && is_readable($api) ) {
            /// try and see if it's a php file that returns a value
            try {
                ob_start();    
                $php = (include $api);
                ob_end_clean();
            } catch(Exception $e) {
                $php = null;
            }
            if ( is_array($php) && !empty($php) ) 
            {
                if ( !empty($key) && !empty($php[$key]) ) 
                {
                    $setting = $php[$key];
                } else {
                    $settings = $php;
                }

            /// try and see if it's an ini file
            } else {
                try {
                    $ini = parse_ini_file($api);
                } catch (Exception $e) {
                    $ini = null;
                }
                if ( is_array($ini) && !empty($ini) )
                {  
                    if ( !empty($key) && !empty($ini[$key]) ) 
                    {
                        $setting = $ini[$key];
                    } else {
                        $settings = $ini;
                    }

                /// try and see if it's a json file
                } else {
                    try {
                        $contents = file_get_contents($api);
                        $json = json_decode($contents,true);
                    } catch (Exception $e) {
                        $json = null;
                    }
                    if ( is_array($json) && !empty($json) )  
                    {
                        if ( !empty($key) && !empty($json[$key]) ) 
                        {
                            $setting = $json[$key];
                        } else {
                            $settings = $json;
                        }


                    }
                }
            }
        }
        foreach ( array_keys($this->api) as $k )
        {
            if ( isset($settings[$k]) )
            {
                $this->api[$k] = $settings[$k];
            }
        }
    }

    /// CLIENT FUNCTIONS

    /**
     * Get Client Version : the versions of the server api this client can talk too 
     * 
     * @access public
     * 
     * @return string
     */
    function getClientApiVersions()
    {
        return array( '2' );
    }

    /**
     * Get Server Version : the version of the API on the configured server
     * 
     * @access public
     * 
     * @return string Version identification string
     */
    function getServerApiVersion()
    { 
        try
        {
            $result = $this->apiCall('get',"{$this->api['syndication_base']}/swagger/api",array(),'json');
            if ( !empty($result['content']) && is_array($result['content']) && isset($result['content']['apiVersion']) )
            {
               return $result['content']['apiVersion']; 
            }
            return null;
        } catch ( Exception $e ) {
            return null;
        }
    }

    /**
     * Calls an debug url which will do nothing but validate our key. 200 http response means valid.
     * 
     * @access public
     * 
     * @return SyndicationResponse ->results[]
     *      empty
     */
    function testCredentials($params = array())
    {
        try
        {
            $result = $this->apiCall('get',"{$this->api['cms_manager_url']}/keyTest", $params);
            return $this->createResponse($result,'Test APIKey Credentials','ApiKey');
        } catch ( Exception $e ) {
            return $this->createResponse($e,'API Call');
        }
    }

    /// INTERNAL FUNCTIONS

    /**
     * Parse a URL string into array. 
     * Based on RFC3986 'URI Generic Syntax' regex. Added 'Format' as the last dot expression of the path. Does not include character encoding restrictions.
     * 
     * @param mixed $url url
     *
     * @access public
     * @return array keys 
     *      scheme
     *      userinfo
     *      host
     *      path
     *      format
     *      query
     *      fragment 
     */
    function parseUrl($url)
    {
        $simple_url = "/^(?:(?P<scheme>[^:\/?#]+):\/\/)(?:(?P<userinfo>[^\/@]*)@)?(?P<host>[^\/?#]*)(?P<path>[^?#]*?(?:\.(?P<format>[^\.?#]*))?)?(?:\?(?P<query>[^#]*))?(?:#(?P<fragment>.*))?$/i";
        $url_parts  = array();
        preg_match($simple_url, $url, $url_parts);
        return $url_parts;
    }

    /**
     * Guess response format directly from the url. 
     * Checks the path for a known file extension. Default for no file extension is 'raw'. Basic image types combined into 'image'.
     * 
     * @param mixed $url url 
     * @access public
     * 
     * @return string response format 
     */
    function guessFormatFromUrl ($url)
    {
        $url_parts = $this->parseUrl($url);
        if ( empty($url_parts) )
        {
            return 'raw';
        }
        $format = !empty($url_parts['format'])?$url_parts['format']:'raw';
        if ( in_array($format,array('jpg','jpeg','png','gif')) ) { $format = 'image'; }
        return $format;
    }

    /**
     * Guess response format from response headers. Defaults to 'raw'. Checks Content-Type header. If no content-type header and first character of body is '{', assume 'json'. 
     * 
     * @param mixed $response response 
     * @access public
     * 
     * @return string format
     */
    function guessFormatFromResponse ($response)
    {
        if ( stripos($response['content_type'],'json')       !== false ) { return 'json';  }
        if ( stripos($response['content_type'],'image')      !== false ) { return 'image'; }
        if ( stripos($response['content_type'],'html')       !== false ) { return 'html';  }
        if ( stripos($response['content_type'],'text')       !== false ) { return 'text';  }
        if ( stripos($response['content_type'],'javascript') !== false ) { return 'js';    }
        /// last ditch effort to guess json
        if ( is_string($response['content']) && $response['content']{0} == '{' ) { return 'json';  }
        return 'raw';
    }

    /**
     * Decode Http Status code into string
     * 
     * @param mixed $status status 
     * @access public
     * 
     * @return string status message
     */
    function httpStatusMessage ( $status )
    {
        # rfc2616-sec10
        $messages = array(
            // [Informational 1xx]
            100=>'100 Continue',
            101=>'101 Switching Protocols',
            // [Successful 2xx]
            200=>'200 OK',
            201=>'201 Created',
            202=>'202 Accepted',
            203=>'203 Non-Authoritative Information',
            204=>'204 No Content',
            205=>'205 Reset Content',
            206=>'206 Partial Content',
            // [Redirection 3xx]
            300=>'300 Multiple Choices',
            301=>'301 Moved Permanently',
            302=>'302 Found',
            303=>'303 See Other',
            304=>'304 Not Modified',
            305=>'305 Use Proxy',
            306=>'306 (Unused)',
            307=>'307 Temporary Redirect',
            // [Client Error 4xx]
            400=>'400 Bad Request',
            401=>'401 Unauthorized',
            402=>'402 Payment Required',
            403=>'403 Forbidden',
            404=>'404 Not Found',
            405=>'405 Method Not Allowed',
            406=>'406 Not Acceptable',
            407=>'407 Proxy Authentication Required',
            408=>'408 Request Timeout',
            409=>'409 Conflict',
            410=>'410 Gone',
            411=>'411 Length Required',
            412=>'412 Precondition Failed',
            413=>'413 Request Entity Too Large',
            414=>'414 Request-URI Too Long',
            415=>'415 Unsupported Media Type',
            416=>'416 Requested Range Not Satisfiable',
            417=>'417 Expectation Failed',
            // [Server Error 5xx]
            500=>'500 Internal Server Error',
            501=>'501 Not Implemented',
            502=>'502 Bad Gateway',
            503=>'503 Service Unavailable',
            504=>'504 Gateway Timeout',
            505=>'505 HTTP Version Not Supported'
        );
        return isset($messages[$status])? $messages[$status] : null;
    }

    /**
     * Wraps curl response or exception into a common SyndicationResponse Object. 
     * 
     * @param mixed $from Curl Response or Exception 
     * @param string $action Devloper friendly description of what triggered response
     * @param mixed $api_key API Key used to connect 
     *
     * @access public
     * @return SyndicationResponse object
     *      ->format   : string  http response format
     *      ->status   : string  http response status
     *      ->messages : array   developer friendly error messages
     *      ->results  : array
     *      ->success  : boolean
     */
    function createResponse ( $from, $action="Process Request", $api_key=null )
    {
        $response = new SyndicationResponse();
        $response->raw = $from;

        /// an exception was thrown
        if ( is_subclass_of($from,'Exception') )
        {
            $response->success = false;
            $response->status  = $from->getCode();
            $response->format  = 'Exception';
            $response->addMessage(array(
                'errorCode'    => $from->getCode(),
                'errorMessage' => $from->getMessage(),
                'errorDetail'  => "{$action} Exception"
            ));
            return $response;

        /// we got a response from the server
        } else if ( is_array($from)
            && !empty($from['http'])
            && !empty($from['format']) )
        {
            $status = isset($from['http']['http_code']) ? intval($from['http']['http_code']) : null;
            $response->status = $status;
            /// SUCCESS
            if ( $status>=200 && $status<=299 )
            {
                $response->success = true;
            /// CLIENT SIDE ERROR
            } else if ( $status>=400 && $status<=499 ) {
                /// BAD API KEY    
                if ( $status == 401 ) {
                    $errorDetail = "Unauthorized. Check API Key.";
                    /// VALID URL but specific id given does not exist 
                } else if ( $status == 404 && !empty($api_key) ) {
                    $errorDetail = "Failed to {$action}. {$api_key} Not Found.";
                    /// Error in the request
                } else {
                    $errorDetail = "Failed to {$action}. Request Error.";
                }
                $response->success  = false;
                $response->addMessage(array(
                    'errorCode'    => $status,
                    'errorMessage' => $this->httpStatusMessage($status),
                    'errorDetail'  => $errorDetail
                ));
                /// SERVER SIDE ERROR
            } else if ( $status>=500 && $status<=599 ) {
                $response->success  = false;
                $response->addMessage(array(
                    'errorCode'    => $status,
                    'errorMessage' => $this->httpStatusMessage($status),
                    'errorDetail'  => "Failed to {$action}. Server Error."
                ));
            }

            if ( $from['format']=='json' )
            {
                /// for any json response
                /// [meta] and [results] expected back from api
                /// [meta][messages] should be consumed if found
                /// [meta][pagination] should be consumed if found 
                /// [message] was changed to plural, check for both for now just incase

                /// look for meta
                if ( isset($from['meta']) )
                {
                    if ( isset($from['meta']['pagination']) )
                    {
                        $response->addPagination($from['meta']['pagination']);
                    }
                    if ( isset($from['meta']['messages']) )
                    {
                        $response->addMessage($from['content']['meta']['messages']);
                    }
                    if ( isset($from['meta']['message']) )
                    {
                        $response->addMessage($from['content']['meta']['message']);
                    }
                } else if ( isset($from['content']) && isset($from['content']['meta']) ) {
                    if ( isset($from['content']['meta']['pagination']) )
                    {
                        $response->addPagination($from['content']['meta']['pagination']); 
                    }
                    if ( isset($from['content']['meta']['messages']) )
                    {
                        $response->addMessage($from['content']['meta']['messages']);
                    }
                    if ( isset($from['content']['meta']['message']) )
                    {
                        $response->addMessage($from['content']['meta']['message']);
                    }
                }
                /// look for results
                if ( isset($from['content']) )
                {
                    if ( isset($from['content']['results']) ) 
                    {
                        $response->results = (array)$from['content']['results'];
                    } else {
                        $response->results = (array)$from['content'];
                    }
                }
                $response->format = 'json';
                return $response;
            } else if ( $from['format']=='image' ) {
                $response->format  = 'image';
                
                /// a single string: base64 encoded image : imagecreatefromstring?
                $response->results = $from['content'];
                return $response;
            /// unknown format
            } else {
                $response->format  = $from['format'];
                /// a single string : html : filtered_html?
                $response->results = $from['content'];
                return $response;
            }
        }
        /// we got something weird - can't deal with this
        $response->success = false;
        $status = null;
        if ( is_array($from) && !empty($from['http']) && isset($from['http']['http_status']) )
        {
            $status = $from['http']['http_status'];
        }
        $response->addMessage(array(
            'errorCode'    => $status,
            'errorMessage' => $this->httpStatusMessage($status),
            'errorDetail'  => "Unknown response from Server."
        ));
        return $response;
    }

    /**
     * Makes http request to a Syndication Service 
     * 
     * @param mixed $http_method http method 
     * @param mixed $url url 
     * @param array $params query params 
     * @param mixed $response_format expected response format 
     * @param mixed $request_format format of outgoing request
     *
     * @access public
     * @return array format
     *      http    : array   curl info about request and response
     *      content : string  response body
     *      format  : string  response format
     */
    function apiCall ( $http_method, $url, $params=array(), $response_format=null, $request_format=null )
    {
        if ( empty($response_format) )
        {
            $response_format = $this->guessFormatFromUrl($url);
        }

        $ssl_auth = 0;
        if(defined('SYNDICATIONAPICLIENT_SSL_AUTH'))
            $ssl_auth = SYNDICATIONAPICLIENT_SSL_AUTH;
        if(isset($params['ssl_auth'])) {
            $ssl_auth = $params['ssl_auth'];
            unset($params['ssl_auth']);
        }

        /*
        foreach ( $params as $p=>$param )
        {
            if ( empty($param) && $param!==0 && $param!=='0' ) 
            {
                unset( $params[$p] );
            }
        }
        */
        /// ascending order is default, descending order is speicified by a '-' sign 
        /// if 'order' param is set, reinterpret that as part of sort param 
        if ( !empty($params['sort']) && !empty($params['order']) ) 
        {
            // clean up sort, strip off leading '-'
            if ( $params['sort']{0} != '-' )
            {
                $params['sort'] = substr( $params['sort'], 1 );
            }
            if ( strtolower(substr($params['order'],4)) == 'desc' )
            {
                $params['sort'] = '-'.$params['sort'];
            } 
        }

        $http_params = '';

        /// our request format type
        $request_headers = array();
        switch( $request_format )
        {
            case 'html':
                $http_params = http_build_query($params,'','&');
                $request_headers[] = 'Content-Type: text/html;charset=UTF-8';
                break;
            case 'xml':
                $http_params = http_build_query($params,'','&');
                $request_headers[] = 'Content-Type: text/xml;charset=UTF-8';
                break;
            case 'json':
                $http_params = json_encode($params);
                $request_headers[] = 'Content-Type: application/json;charset=UTF-8';
                break;
            default:
                @$http_params = http_build_query($params,'','&');
                $request_headers[] = 'Content-Type: application/x-www-form-urlencoded;charset=UTF-8';
                break;
        }

        $request_headers[] = 'Date: '.gmdate('D, d M Y H:i:s', time()).' GMT';

        /// ask for a specific format type of response
        if ( !empty($response_format) )
        {
            switch( $response_format )
            {
                case 'html':
                    $request_headers[] = 'Accept: text/html;charset=UTF-8';
                    break;
                case 'json':
                    $request_headers[] = 'Accept: application/json;charset=UTF-8';
                    break;
                case 'js':
                    $request_headers[] = 'Accept: application/javascript;charset=UTF-8';
                    break;
                case 'text':
                    $request_headers[] = 'Accept: text/plain;charset=UTF-8';
                    break;
                case 'image':
                    $request_headers[] = 'Accept: image/*;';
                    break;
            }
        }

        /// content-length required for apiKeyGen
        switch ( strtolower($http_method) )
        {
            case 'post':
            case 'put':
            case 'delete':
                $request_headers[] = 'Content-Length: '.strlen($http_params);
                break;
            case 'get':
            default:
                $request_headers[] = 'Content-Length: 0';
                break;
        }
        
        $apiKey = $this->apiGenerateKey( $http_method, $url, $http_params, $request_headers );
        $request_headers[] = "Authorization: syndication_api_key {$apiKey}";

        $curl = $this->apiBuildCurlRequest( $http_method, $url, $http_params, $request_headers, $response_format ); 


        /// do some temp memory writing bs to capture curl's output to grab actual request string
        curl_setopt($curl, CURLOPT_VERBOSE, true);
        $verbose = fopen('php://temp', 'rw+');
        curl_setopt($curl, CURLOPT_STDERR, $verbose);

        if($ssl_auth == 1) {
                curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);
        }
        
        $content = curl_exec($curl);
        rewind($verbose);
        $verbose_log = stream_get_contents($verbose);
        $http = curl_getinfo($curl);
        $http['verbose_log'] = $verbose_log;

        if ($content === false)
        {
            curl_close($curl);
            throw new Exception('Syndication: No Response: '. $http['http_code'], $http['http_code'] );
            return null;
        }
        curl_close($curl);

        if ( empty($response_format) )
        {
            $response_format = $this->guessFormatFromResponse($http);
        }

        $api_response = array(
            'http'    => $http,
            'content' => $content,
            'format'  => $response_format
        );
        /// test result content-type for JSON / HTML / IMG
        /// json needs to be decoded
        /// html stay as text
        /// images need to be: base64_encoded string or image resource
        if ( $response_format=='image' )
        {
            // as GD handle ?
            // $api_response['content'] = imagecreatefromstring($content);
        } else if ( $response_format=='text' ) {
            // nuthin
        } else if ( $response_format=='html' ) {
            // any html cleaning ?
        } else if ( $response_format=='js'   ) {
            // any xss cleaning ?
        } else if ( $response_format=='json' ) {
            try {
                $decoded = json_decode($content,true);
                if ( $decoded === null ) 
                {
                    /// bad json should return empty, or return raw unencoded values?
                } else if ( isset($decoded['results']) ) {
                    if ( empty($decoded['results']) || count($decoded['results'])==1 && empty($decoded['results'][0]) )
                    {
                        $decoded['results'] = array();
                    }
                }
                $api_response['content'] = $decoded;
            } catch ( Exception $e ) { 
                /// bad json should return empty, or return raw unencoded values?
            }
        }
        return $api_response;
    }

    /**
     * Builds Curl Object capable of talking to Syndication Service
     * 
     * @param string $http_method http request method 
     * @param string $url url 
     * @param array $http_params query params 
     * @param array $headers headers 
     * @param string $response_format expected http response format 
     *
     * @access public
     * @return curl resouce handle
     */
    function apiBuildCurlRequest( $http_method, $url, $http_params='', $headers=array(), $response_format='' )
    {
       
        $curl = curl_init();

        curl_setopt($curl, CURLOPT_USERAGENT,      'Syndication-Client/php v1'); // Useragent string to use for request
        curl_setopt($curl, CURLOPT_FOLLOWLOCATION, true );
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true );
        if ( $response_format=='image' )
        {
            curl_setopt($curl, CURLOPT_HEADER,         false );
            curl_setopt($curl, CURLOPT_BINARYTRANSFER, true  );
        }
        switch ( strtolower($http_method) )
        {
            case 'post':
                //curl_setopt( $curl, CURLOPT_POST,          true      );
                curl_setopt( $curl, CURLOPT_CUSTOMREQUEST, 'POST'    );
                if ( !empty($http_params) )
                {
                    curl_setopt( $curl, CURLOPT_POSTFIELDS,    $http_params );
                }
                break;
            case 'put':
                curl_setopt( $curl, CURLOPT_CUSTOMREQUEST, 'PUT'        );
                if ( !empty($http_params) )
                {
                    curl_setopt( $curl, CURLOPT_POSTFIELDS,    $http_params );
                }
                break;
            case 'delete':
                curl_setopt( $curl, CURLOPT_CUSTOMREQUEST, 'DELETE' );
                if ( !empty($http_params) )
                {
                    curl_setopt( $curl, CURLOPT_POSTFIELDS,    $http_params  );
                }
                break;
            case 'get':
            default:
                curl_setopt( $curl, CURLOPT_HTTPGET, true ); 
                if ( !empty($http_params) )
                {
                    $url .= (strpos($url,'?')===FALSE?'?':'&') . $http_params;
                }
                break;
        }
        curl_setopt( $curl, CURLOPT_HTTPHEADER,     $headers);
        /** / // debug request output
        curl_setopt( $curl, CURLOPT_VERBOSE, 1 );
        curl_setopt( $curl, CURLOPT_STDERR,  fopen('php://stdout', 'w') );
        /**/

        curl_setopt( $curl, CURLOPT_CONNECTTIMEOUT, 5  ); // seconds attempting to connect
        curl_setopt( $curl, CURLOPT_TIMEOUT,        10 ); // seconds cURL allowed to execute
        /** / // forces new connections
        curl_setopt( $curl, CURLOPT_FORBID_REUSE,  true );
        curl_setopt( $curl, CURLOPT_FRESH_CONNECT, true );
        curl_setopt( $curl, CURLOPT_MAXCONNECTS,   1);
        /**/
        curl_setopt( $curl, CURLOPT_URL, $url );

        return $curl;
    }

    /**
     * Generate API Key.
     * Use public/private keys to sign this request. Used by Syndication Service to verify request authenticity.  
     * 
     * @param string $http_method http request method 
     * @param string $url url 
     * @param array $query http query params 
     * @param array $headers http request headers 
     * 
     * @access public
     * @return string Api Key
     */
    function apiGenerateKey( $http_method, $url, $query, $headers )
    {
      // ordered and scrubbed headers: date,content-type,content-length;
      $canonicalizedHeaders  = '';
      $desiredHeaders = array('date','content-type','content-length');
      $headerData = array();
      rsort($headers);
      foreach ( $headers as $header )
      {
        $pos = strpos($header,':');
        if ( $pos )
        {
          $name  = strtolower(trim(substr($header,0,$pos)));
          $value = substr($header,$pos+1);
          $headerData[$name] = trim($value);
          if ( in_array($name,$desiredHeaders) )
          {
            $canonicalizedHeaders .= $name .':'. trim(str_replace(array('\n','\r'),' ',$value))."\n";
          }
        }
      }
      $canonicalizedHeaders = trim($canonicalizedHeaders);

      // just the clean url path - JS LOGIC IS DIFF - gets up to ? OR all url - this is prob wrong on zack's part, ignoring fragment
      // js logic would include fragment if no ? is found
      $url_parts = $this->parseUrl($url);
      $canonicalizedResource = ( !empty($url_parts) && !empty($url_parts['path']) ) ? trim($url_parts['path']) : '';

      // array of: date,content-type,http method;
      $requestMethod = strtoupper($http_method);
       
      /// hash of the body - md5
      $hashedData    = md5($query);
      $signingString = "{$requestMethod}\n".
                       "{$hashedData}\n".
                       "{$canonicalizedHeaders}\n".
                       "{$canonicalizedResource}";
      $computedHash  = base64_encode(hash_hmac('md5', $signingString, $this->api['key_secret'], true ));

      /// share public key are our hash
      return "{$this->api['key_public']}:{$computedHash}";# \n\n header order: $h \n\n Signing String:\n $signingString";
    }
}
