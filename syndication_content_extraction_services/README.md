Syndication Content Extraction Services Plugin
===============================================

Dependencies
-----------------------------------------------
### Plugins
- org.grails.plugins:syndication-model:2.1.x

Structure
------------------------------------------------
All content extraction services are provided here for easy re-use. Core services/classes:

    grails-app/services
    └── com
        └── ctacorp
            └── syndication
                └── contentextraction
                    ├── ContentCacheService.groovy
                    ├── ContentRetrievalService.groovy
                    ├── FileService.groovy
                    ├── JsoupWrapperService.groovy
                    ├── MediaPreviewThumbnailService.groovy
                    ├── NativeToolsService.groovy
                    ├── WebUtilService.groovy
                    └── YoutubeService.groovy

    src
    └── com
        └── ctacorp
            └── syndication
                └── exception
                    ├── ContentNotExtractableException.groovy
                    ├── ContentUnretrievableException.groovy
                    └── InaccessibleVideoException.groovy