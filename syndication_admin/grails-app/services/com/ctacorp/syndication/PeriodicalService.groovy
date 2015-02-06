package com.ctacorp.syndication

import com.ctacorp.syndication.commons.mq.Message
import com.ctacorp.syndication.commons.mq.MessageType
import grails.transaction.Transactional

@Transactional
class PeriodicalService {
    def contentRetrievalService
    def remoteCacheService
    def queueService

    def updateDailies(){
        log.info "Daily Periodical Scan initiated"
        //Update happens at midnight server time
        def periodicals = Periodical.findAllByPeriod(Periodical.Period.DAILY)
        updateAndNotify(periodicals)
    }

    def updateWeeklies(){
        def cal = getToday()
        //Update happens early Monday morning server time
        if(cal.get(Calendar.DAY_OF_WEEK) != 2 ){ return }
        log.info "Weekly Periodical Scan initiated"
        def periodicals = Periodical.findAllByPeriod(Periodical.Period.WEEKLY)
        updateAndNotify(periodicals)
    }

    def updateMonthlies(){
        def cal = getToday()
        //Update happens early on the 1st of each month
        if(cal.get(Calendar.DAY_OF_MONTH) != 1 ){ return }
        log.info "Monthly Periodical Scan initiated"
        def periodicals = Periodical.findAllByPeriod(Periodical.Period.MONTHLY)
        updateAndNotify(periodicals)
    }

    private updateAndNotify(periodicals){
        List<Periodical> updated = []
        periodicals.each { Periodical periodical ->
            def freshExtraction = contentRetrievalService.getContentAndMd5Hashcode(periodical.sourceUrl)
            if(freshExtraction.hash != periodical.hash){
                periodical.hash = freshExtraction.hash
                periodical.save(flush:true)
                updated << periodical
            }
        }

        remoteCacheService.flushRemoteCache()
        updated.each { Periodical updatedPeriodical ->
            Message message = new Message(
                    messageType: MessageType.UPDATE,
                    mediaId: updatedPeriodical.id
            )
            queueService.sendDelayedMessage(message)
        }
    }

    private GregorianCalendar getToday(){
        def today = new Date().clearTime()
        def cal = new GregorianCalendar()
        cal.setTime(today)
        cal
    }
}
