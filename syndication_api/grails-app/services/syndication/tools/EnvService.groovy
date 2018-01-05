package syndication.tools

import grails.transaction.Transactional

@Transactional
class EnvService {

    static final INTERESTING_ENV_VARS = [
        'SYNDICATION_ENV',
        'SKIP_CONTENT_EXTRACTION_SLEEP',
        'SYNDICATION_EXTRACTION_CSSCLASSNAME',
        'YOUTUBE_API_KEY',
        'SYNDICATION_INTERNALAUTHHEADER',
        'SYNDICATION_APIPATH',
        'TINYURL_SERVER_URL',
        'API_SERVER_URL',
        'CMSMANAGER_SERVER_URL',
        'ADMIN_SERVER_URL',
        'STOREFRONT_SERVER_URL',
        'TAG_CLOUD_SERVER_URL',
        'TINYURL_DB_URL',
        'SYND3_DB_URL',
        'CMSMANAGER_DB_URL',
        'TAG_CLOUD_DB_URL',
        'MYSQL_USER',
        'MYSQL_PASSWORD',
        'MAIL_HOST',
        'MAIL_PORT',
        'MAIL_DEFAULT_FROM',
        'ELASTICSEARCH_URL',
        'ELASTICSEARCH_PORT',
        'ELASTICSEARCH_BATCH_SIZE',
        'GOOGLE_ANALYTICS_PROFILEID',
        'GOOGLE_ANALYTICS_EMAILADDRESS',
        'GOOGLE_ANALYTICS_P12',
        'AWS_S3_BUCKET',
        'AWS_ACCESS_KEY_ID',
        'AWS_SECRET_ACCESS_KEY',
        'TWITTER_CONSUMER_KEY',
        'TWITTER_CONSUMER_SECRET',
        'TWITTER_ACCESS_TOKEN',
        'TWITTER_ACCESS_TOKEN_SECRET',
        'TINYURL_ADMIN_USERNAME',
        'TINYURL_ADMIN_PASSWORD',
        'CMSMANAGER_VERIFYAUTHPATH',
        'CMSMANAGER_ADMINUSERNAME',
        'CMSMANAGER_DEFAULTPASSWORD',
        'CMSMANAGER_SELFAUTHPATH',
        'CMSMANAGER_PUBLICKEY',
        'CMSMANAGER_PRIVATEKEY',
        'CMSMANAGER_SECRET',
        'CMSMANAGER_HELPEMAILADDRESS',
        'CMSMANAGER_CREATE_TEST_DATA',
        'SYNDICATION_ADMIN_USERNAME',
        'SYNDICATION_ADMIN_PASSWORD',
        'TAG_CLOUD_ADMIN_USERNAME',
        'TAG_CLOUD_ADMIN_PASSWORD',
        'TAG_CLOUD_GENERATETESTDATA',
        'S3TOOL_AWS_S3_BUCKET',
        'S3TOOL_AWS_ACCESS_KEY',
        'S3TOOL_AWS_SECRET_KEY',
        'S3TOOL_SLACK_URL',
        'JAVA_HOME'
    ]

    def dumpEnv() {

        def table = '<table>'
        INTERESTING_ENV_VARS.sort().collect {
            table += "<tr><td><b>${it}</b></td><td>${System.getenv(it) ?: ''}</td></tr>"
        }
        table += '</table>'
    }
}
