<?php

define('SYNDICATION_CONTENT_UNKNOWN_TO_CLIENT',911);
define('SYNDICATION_CONTENT_UNKNOWN_TO_SOURCE',912);

class SyndicationContentUnknowtoClientException extends Exception
{
    function _construct( $message='Media Item Unknown to Client', $code=SYNDICATION_CONTENT_UNKNOWN_TO_DRUPAL, Exception $previous=null )
    {
        parent::_construct($message, $code, $previous);
    }
}

class SyndicationContentUnknowToSourceException extends Exception
{
    function _construct( $message='Media Item Unknown to Source', $code=SYNDICATION_CONTENT_UNKNOWN_TO_SOURCE, Exception $previous=null )
    {
        parent::_construct($message, $code, $previous);
    }
}

function _syndicated_content_listener()
{
    /// TYPES OF RESPONSES
    /// 200 : done  : request processed
    /// 400 : never : bad request format, request will never process
    /// 410 : never : media_id read, but does not exist in the system
    /// 404 : later : media_id found, but transient processing problem
    /// 500 : later : exception thrown, transient processing problem

    //if ( isset($_syndicated_content_SERVER['CONTENT_TYPE']) && stripos($_syndicated_content_SERVER['CONTENT_TYPE'],'application/json')!==false )
        $body = @file_get_contents('php://input');
        $data = json_decode($body);
        if ( isset($data->media_id) )
        {
            $media_id = $data->media_id;
        } else {
            if ( isset($_POST['media_id']) )
            {
                $media_id = $_POST['media_id'];
            } else {
                if ( isset($_GET['media_id']) )
                {
                    $media_id = $_GET['media_id'];
                } else {
                    /// NO MEDIA ID FOUND - BAD REQUEST
                    http_response_code(400);
                    echo 'Bad request - no media id found';
                    return;
                }
            }
        }
        try {
            if ( _syndicated_content_update_node_by_media_id($media_id) )
            {
                http_response_code(200);
                echo 'OK';
            } else {
                http_response_code(404);
                echo 'Transient Processing Error';
            }
        } catch( SyndicationContentUnknowtoClientException $e ) {
            http_response_code(410); /// GONE
            echo $e->getMessage();
        } catch( SyndicationContentUnknowToSourceException $e ) {
            http_response_code(410); /// GONE
            echo $e->getMessage();
        } catch( Exception $e ) {
            http_response_code(404);
            echo 'Transient Processing Error';
        }
}
