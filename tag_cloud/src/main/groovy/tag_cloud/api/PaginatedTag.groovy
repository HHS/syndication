package tag_cloud.api

import tagcloud.domain.Tag

/**
 * ApiResponse is a holder and utility class representing the API response returned to the client
 * as a ReST Response.
 */
class PaginatedTag {
    List<Tag> tagList
    Long total
    Long dataSize
    Long max
    Long offset
}
