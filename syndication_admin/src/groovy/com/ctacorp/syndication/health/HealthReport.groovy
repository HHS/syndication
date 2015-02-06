package com.ctacorp.syndication.health

import com.ctacorp.syndication.health.FlaggedMedia.FailureType

/**
 * Created by sgates on 11/14/14.
 */
class HealthReport {
    FailureType failureType
    String statusCode
    String details
    boolean valid = false
    Long mediaId
}
