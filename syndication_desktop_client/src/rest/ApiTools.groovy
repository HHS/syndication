package rest

import com.ctacorp.commons.api.key.utils.AuthorizationHeaderGenerator
import config.ConfigLoader
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import rest.model.Language
import rest.model.Organization
import rest.model.Tag
import rest.model.TagLanguage

import javax.swing.JOptionPane

import org.springframework.web.client.RestTemplate

import java.nio.charset.Charset

/**
 * Created by sgates on 10/2/14.
 */
class ApiTools {
    ConfigLoader configLoader
    def gui
    AuthorizationHeaderGenerator generator
    AuthorizationHeaderGenerator.KeyAgreement keyAgreement

    ApiTools(ConfigLoader configLoader, gui){
        this.configLoader = configLoader
        this.gui = gui

        initGenerator()
    }

    def initGenerator(){
        keyAgreement = new AuthorizationHeaderGenerator.KeyAgreement()
        keyAgreement.setPublicKey(config.publicKey)
        keyAgreement.setPrivateKey(config.privateKey)
        keyAgreement.setSecret(config.secret)
        generator = new AuthorizationHeaderGenerator('syndication_api_key', keyAgreement)
    }

    String getJsonBodyForPublishRequest(Map data){
        JsonBuilder jb = new JsonBuilder(data)
        jb.toPrettyString()
    }

    String getJsonBodyForPublishRequest(Long language, Long source, String sourceUrl, String name, String description, tagList){
        def data = [
                language:language,
                source:source,
                sourceUrl:sourceUrl,
                name:name,
                description:description,
                tags:tagList*.id
        ]
        JsonBuilder jb = new JsonBuilder(data)
        jb.toPrettyString()
    }

    private validExtensions = [".jpg", ".jpeg", ".png"]
    Map verifyImage(String imageUrl){
        boolean validFormat = false
        validExtensions.each { ext ->
            if(imageUrl.toLowerCase().contains(ext)){
                validFormat = true
            }
        }
        if(!validFormat){
            return [valid:false, msg:'Could not detect a valid extension. Valid extensions: ' + validExtensions]
        }
        def url = new URL(imageUrl)
        def content
        try {
            content = url.bytes
        } catch(e){
            println e
            return [valid:false, msg:'Could not connect to server hosting file.']
        }
        [valid:true, msg:'Image countent found!']
    }

    List getTagIdsFromModel(tagList){
        tagList*.id
    }

    String publish(String jsonBody, String mediaPath){
        RestTemplate rest = new RestTemplate()
        HttpHeaders headers = new HttpHeaders()
        String requestUrl = config.apiUrl + "/api/v2/resources/media/${mediaPath}"

        def rHeaders = [
                'Date': new Date().toString(),
                'Content-Type': "application/json",
                'Content-Length': jsonBody.bytes.size() as String,
                'Accept': 'application/json'
        ]

        def apiKeyHeaderValue = generator.getApiKeyHeaderValue(rHeaders, requestUrl, 'POST', jsonBody)

        headers.add('Date', rHeaders.Date)
        headers.setContentType(MediaType.APPLICATION_JSON)
        headers.add('Content-Length', jsonBody.bytes.size() as String)
        headers.add('Accept', 'application/json')
        headers.add('Authorization', apiKeyHeaderValue)

        HttpEntity request = new HttpEntity( jsonBody, headers );
        JsonSlurper slurp = new JsonSlurper()
        try {
            def resp = rest.postForObject(requestUrl, request, String.class)
            def json = slurp.parseText(resp)
            String formattedResp = "Details:\n"
            def item = json.results[0]
            formattedResp += "ID: ${item.id}\nName: ${item.name}\nStorefrontUrl: https://digitalmedia.hhs.gov/storefront/storefront/showContent/${item.id}\n"
            return formattedResp
        } catch(HttpClientErrorException e){
            def status = e.statusCode
            def json = slurp.parseText(e.responseBodyAsString)
            String errorMessage = "Errors:\n"
            json.meta.messages.each{ message ->
                errorMessage += "Code: ${message.errorCode}. Message: ${message.errorMessage}\nExplanation: ${message.userMessage}\nDetails: ${message.errorDetail}\n"
            }
            errorMessage = errorMessage[0..-2]
            return errorMessage
        } catch(HttpServerErrorException e){
            System.out.println("Server side error")
            e.printStackTrace()
            return "Errors:\nThere was a server side error processing ${jsonBody}"
        }
        return null
    }

    def getConfig(){
        configLoader.config
    }

    String reachable(String url){
        try {
            return new URL(url).text
        } catch(e){
            println "couldn't reach host: ${url}"
        }
    }

    String markupFound(String content){
        Document doc = Jsoup.parse(content)
        doc.getElementsByClass("syndicate").toString()
    }

    List<Language> getLanguages(){
        def languages = []
        def json = getJsonFromUrl(config.apiUrl, "/api/v2/resources/languages.json")

        json.results.each{ lang ->
            languages << new Language(name: lang.name, id:lang.id)
        }

        languages.sort{it.name}
    }

    List<Organization> getOrganizations(){
        def organizations = []
        def json = getJsonFromUrl(config.apiUrl, "/api/v2/resources/sources.json")

        json.results.each{ org ->
            organizations << new Organization(name: org.name, id:org.id)
        }

        organizations.sort{it.name}
    }

    List queryTags(String q){
        def tags = []
        def json = getJsonForTagQuery(config.apiUrl, "/api/v2/resources/tags/query.json?q=${q}")

        json.results.each{ tag ->
            tags << Tag.getTagFromJson(tag)
        }
        tags
    }

    def getJsonForTagQuery(String url, String path){
        String resp
        try{
            resp = new URL(url + path).text
        } catch(e){
            resp = "{}"
        }
        def slurp = new JsonSlurper()
        slurp.parseText(resp)
    }

    def getJsonFromUrl(String url, String path){
        String resp
        try{
            resp = new URL(url + path).text
        } catch(e){
            String updatedUrl = JOptionPane.showInputDialog("The saved API URL is not reachable. Are you sure it's correct? If address is invalid, please correct it:", config.apiUrl)
            if(!updatedUrl){
                JOptionPane.showMessageDialog(null, "Application cannot run without a valid API Url. Please contact syndication@ctacorp.com for support")
                System.exit(1)
            }
            if(updatedUrl.endsWith("/")){ updatedUrl = updatedUrl[0..-2] }
            def c = config
            configLoader.saveConfig(c.publicKey, c.privateKey, c.secret, updatedUrl)
            gui.loadConfig()
            return getJsonFromUrl(updatedUrl, path)
        }
        def slurp = new JsonSlurper()
        slurp.parseText(resp)
    }
}
