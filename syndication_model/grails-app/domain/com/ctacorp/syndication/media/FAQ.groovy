package com.ctacorp.syndication.media

class FAQ extends MediaItem{
    static hasMany = [questionAndAnswers:QuestionAndAnswer]
    static constraints = {
    }
}
