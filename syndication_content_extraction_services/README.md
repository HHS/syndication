Syndication Content Extraction Services Plugin
===============================================

Dependencies
-----------------------------------------------
### Plugins
- org.grails.plugins:syndication-model:1.5.0_SNAPSHOT4

Add to your buildConfig.groovy/plugins:

    runtime 'org.grails.plugins:syndication-model:1.5.0_SNAPSHOT4'

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
                    ├── JsoupWrapperService.groovy
                    └── WebUtilService.groovy

    src
    └── groovy
        └── com
            └── ctacorp
                └── syndication
                    └── exception
                        ├── ContentNotExtractableException.groovy
                        └── ContentUnretrievableException.groovy