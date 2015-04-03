package com.ctacorp.syndication

import com.ctacorp.syndication.media.MediaItem
import grails.transaction.Transactional

@Transactional
class LanguageService {

    def mediaItemExists(Language languageInstance) {
        def mediaItems = MediaItem.findByLanguage(languageInstance)
        if (!mediaItems) {
            return false
        }
        return true
    }
}
