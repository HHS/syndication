package com.ctacorp.syndication.media

class Periodical extends MediaItem{
    Period period

    static constraints = {
        period nullable: false
    }

    enum Period{
        ANNUALLY("Annually"), MONTHLY("Monthly"), WEEKLY("Weekly"), DAILY("Daily")

        String displayName

        Period(String displayName){
            this.displayName = displayName
        }

        String toString(){
            displayName
        }
    }
}
