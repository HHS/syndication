package com.ctacorp.syndication.microsites

import com.ctacorp.syndication.microsite.*
import grails.transaction.Transactional

@Transactional
class MicrositeService {

    def deleteMicrosite(Long id) {
        def microSite = MicroSite.get(id)

        if(!microSite){ return }

        microSite.mediaArea1?.delete()
        microSite.mediaArea2?.delete()
        microSite.mediaArea3?.delete()
        microSite.delete()
    }
}
