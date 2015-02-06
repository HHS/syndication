package com.ctacorp.syndication

class Periodical extends MediaItem{
    Period period

    static constraints = {
        period nullable: false
    }

    enum Period{
        MONTHLY("Monthly"), WEEKLY("Weekly"), DAILY("Daily")

        String displayName

        Period(String displayName){
            this.displayName = displayName
        }

        String toString(){
            displayName
        }
    }
}
