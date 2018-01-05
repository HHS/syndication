package com.ctacorp.syndication
import com.ctacorp.syndication.commons.mq.Message

/**
 * Created by sgates on 1/5/15.
 */
interface GenericQueueService {
    void sendMessage(Message message)

    void sendDelayedMessage(Message message)

    void flushCache()
}