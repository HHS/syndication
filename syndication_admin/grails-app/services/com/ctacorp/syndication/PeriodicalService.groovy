package com.ctacorp.syndication

import grails.transaction.Transactional

@Transactional
class PeriodicalService {

    private GregorianCalendar getToday(){
        def today = new Date().clearTime()
        def cal = new GregorianCalendar()
        cal.setTime(today)
        cal
    }
}
