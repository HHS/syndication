package com.ctacorp.syndication

import com.ctacorp.syndication.commons.mq.Message
import com.ctacorp.syndication.commons.mq.MessageType
import com.ctacorp.syndication.media.MediaItem
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.grails.datastore.mapping.core.Datastore
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEventListener
import org.springframework.context.ApplicationEvent
import org.grails.datastore.mapping.engine.event.EventType

class MediaItemChangeListener extends AbstractPersistenceEventListener {
    static def log = LogFactory.getLog(this)
    GenericQueueService queueService
    def mediaPreviewThumbnailJobService

    protected MediaItemChangeListener(Datastore datastore, GenericQueueService queueService, mediaPreviewThumbnailJobService) {
        super(datastore)
        this.queueService = queueService
        this.mediaPreviewThumbnailJobService = mediaPreviewThumbnailJobService
    }

    @Override
    protected void onPersistenceEvent(AbstractPersistenceEvent event) {
        if(event.entityObject instanceof MediaItem) {
            switch (event.eventType) {
                case EventType.PostUpdate:
                    queueService.flushCache()
                    queueService.sendDelayedMessage(new Message(messageType:MessageType.UPDATE, mediaId:event.entityObject.id))
                    mediaPreviewThumbnailJobService.delayedPreviewAndThumbnailGeneration(Long.valueOf(event.entityObject.id))
                    break
                case EventType.PostDelete:
                    queueService.flushCache()
                    queueService.sendDelayedMessage(new Message(messageType:MessageType.DELETE, mediaId:event.entityObject.id))
                    break
                case EventType.PostInsert:
                    mediaPreviewThumbnailJobService.delayedPreviewAndThumbnailGeneration(Long.valueOf(event.entityObject.id))
                    break
            }
        }
    }

    @Override
    boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return true
    }

    @SuppressWarnings(["GroovyUnusedDeclaration", "GrUnresolvedAccess"])
    public static void initialize(GrailsApplication grailsApplication, queueService, mediaPreviewThumbnailJobService) {
        log.info("initializing ${MediaItemChangeListener.class.name}")
        grailsApplication.mainContext.eventTriggeringInterceptor.datastores.each { k, datastore ->
            grailsApplication.mainContext.addApplicationListener new MediaItemChangeListener(datastore, queueService, mediaPreviewThumbnailJobService)
        }
    }
}