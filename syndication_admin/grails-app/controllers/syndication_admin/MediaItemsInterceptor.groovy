package syndication_admin

import com.ctacorp.syndication.media.MediaItem


class MediaItemsInterceptor {

    def mediaItemsService
    MediaItemsInterceptor(){
        match(controller: 'collection|html|image|infographic|tweet|video', action: 'show|edit|update|delete')

    }


    boolean before() {
        if(!mediaItemsService.ifPublisherValid(MediaItem.get(params.id))){
            response.sendError(404)
            return false
        }
        return true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
