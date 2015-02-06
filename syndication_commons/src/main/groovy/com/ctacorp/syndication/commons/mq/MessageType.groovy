package com.ctacorp.syndication.commons.mq

/**
 * Use to define what type of message is being sent through a message queue. Every instance
 * of the Message class requires a messageType field which tells the receiver what kind of
 * message is being sent. This lets the receiver know which fields it should inspect for message
 * processing.
 *
 * <ul>
 *  <li><strong>UPDATE</strong>: A media item has been updated</li>
 *  <li><strong>DELETE</strong>: A media item has been deleted</li>
 *  <li><strong>PUBLISH</strong>: A media item should be published to syndication</li>
 *  <li><strong>REQUEST</strong>: A media item (linked by url) is requested for syndication</li>
 *  <li><strong>QUERY</strong>: Any type of a-sync search function or informational request</li>
 *  <li><strong>SUBSCRIBE</strong>: A CMS is requesting a subscription to a media item</li>
 *  <li><strong>UNSUBSCRIBE</strong>: A CMS is requesting to unsubscribe from a media item</li>
 *  <li><strong>ERROR</strong>: An error has occurred in either the CMS end or the Syndication end</li>
 *  <li><strong>INFO</strong>: Generic infomative message</li>
 * <ul>
 */
enum MessageType{

	IMPORT,
	UPDATE,
	DELETE,
	PUBLISH,
	REQUEST,
	QUERY,
	SUBSCRIBE,
	UNSUBSCRIBE,
	ERROR,
	INFO

    public String prettyName() {
        name().toLowerCase().capitalize()
    }
}
