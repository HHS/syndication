
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.error

/**
 * Created with IntelliJ IDEA.
 * User: Steffen Gates
 * Date: 12/13/13
 * Time: 1:49 PM
 */
public enum Error {
    GENERIC_SERVER_ERROR("Generic Server Error", "1",
        "The server experienced an un-handled error while processing the " +
        "request. Time: ${new Date()}."
    ),

    RECORD_NOT_FOUND("Record Not Found", "2",
        "The requested record could not be found. Either the supplied ID " +
        "was invalid, or the specified record has been deleted."
    ),

    CONTENT_NOT_EXTRACTABLE("Content Not Extractable", "3",
        "The supplied URL does not contain extractable content. Either " +
            "the markup is missing, in error, or the page is malformed."
    ),

    FIELD_CONSTRAINT_VIOLATION("Field Constraint Violation", "4",
        "The value provided for a field was absent or incorrect. Verify that " +
            "all of the data you provided is correct."
    ),

    INVALID_RESOURCE_FOR_MEDIA_TYPE("Invalid Resource for Media Type", "5",
        "The requested resource is not applicable to the media type currently selected." +
            " Verify that you are performing the correct operation on the correct media item."
    ),

    NOT_AUTHORIZED("Not authorized for resource", "6",
        "The requested operation is not authorized for this resource. Check that your API keys" +
                " are correct and up to date."
    ),

    CONTENT_UNRETRIEVABLE("Content Unretrievable", "7",
            "The sourceUrl for the item is unreachable." +
                " Verify the SourceUrl address.")

    public Error(String name, String code, String message){
        this.name = name
        this.code = code
        this.message = message
    }

    String name
    String code
    String message
}