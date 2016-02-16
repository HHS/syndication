package com.ctacorp.syndication.media

class QuestionAndAnswer extends MediaItem{
    String answer

    static constraints = {
        answer      nullable: false, blank: false, maxSize: 2000
    }
}
