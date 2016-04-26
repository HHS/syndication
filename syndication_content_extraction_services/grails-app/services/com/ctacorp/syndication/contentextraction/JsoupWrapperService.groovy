/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.ctacorp.syndication.contentextraction

import com.ctacorp.syndication.commons.util.Util
import groovy.json.JsonSlurper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class JsoupWrapperService {
    static transactional = false
    def grailsApplication
    def webUtilService

    /**
     * This is where the magic happens
     * @param sourceContent
     * @param params
     * @return
     */
    String extract(String sourceContent, Map params){
        Document doc = Jsoup.parse(sourceContent)
        //Read the class defined in params if it exists, else use config file class if exists, else fall back to 'syndicate'
        String extractionCSSClass = params.cssClass ?: grailsApplication.config.syndication.contentExtraction.cssClassName ?: 'syndicate'
        String extractedContent = getElementByClassFromDocument(doc, extractionCSSClass)

        String newUrlBase = params.newUrlBase

        doc = Jsoup.parse(extractedContent)

        String jsonLDText = getJsonLDMetaDataAsString(doc)

        if(Util.isTrue(params.stripStyles, true)){
            removeInlineStylesFromDocument(doc)
        }

        if(Util.isTrue(params.stripScripts)){
            removeScriptsFromDocument(doc)
        }

        if(Util.isTrue(params.stripImages)){
            removeImagesFromDocument(doc)
        }

        if(Util.isTrue(params.stripBreaks)){
            removeBreaksFromDocument(doc)
        }

        if(Util.isTrue(params.stripClasses, true)){
            removeClassesFromDocument(doc)
        }

        if(Util.isTrue(params.stripIds, true)){
            removeInlineIdsFormDocument(doc)
        }

        removeBadTagsFromDocument(doc)

        customizeStyles(doc, params)

        patchRelativeUrlsFromDocument(doc, newUrlBase)

        addAutoFocus(doc)

        stripEmptyBlocks(doc)

        //String
        extractedContent = getElementByClassFromDocument(doc, extractionCSSClass)

        extractedContent
    }

    private final targetEmptyTags = ["div","p","span"]

    String stripEmptyBlocks(Document doc) {
        for (Element element : doc.select(targetEmptyTags.join(", "))) {
            if (!element.hasText() && element.isBlock() && element.children().size() == 0) {
                element.remove();
            }
        }
    }

    //JSON-LD Methods --------------------------------------------------------------------------------------------------

    String updateJsonLD(String inputSnippet, String updatedJsonLdString){
        Document doc = Jsoup.parse(inputSnippet)
        def existingJsonLd = doc.select('script[type=application/ld+json]')
        if(existingJsonLd){
            existingJsonLd.html(updatedJsonLdString)
        } else{
            doc.body().appendElement('script').attr("type", "application/ld+json").html(updatedJsonLdString)
            return doc.body().toString()
        }
    }

    def getJsonLDMetaDataAsJSON(String inputSnippet){
        Document doc = Jsoup.parse(inputSnippet)
        getJsonLDMetaDataAsJSON(doc)
    }

    def getJsonLDMetaDataAsJSON(Document doc){
        def jsonLdText = getJsonLDMetaDataAsString(doc)
        if(jsonLdText) {
            JsonSlurper slurp = new JsonSlurper()
            return slurp.parseText(jsonLdText)
        }
        null
    }

    String getJsonLDMetaDataAsString(Document doc){
        def jsonLdElement = doc.select('script[type=application/ld+json]')
        if(jsonLdElement){
            return jsonLdElement.html()
        }
        null
    }

    // end JSON-LD Methods ---------------------------------------------------------------------------------------------

    String getMetaDescription(String url){
        String page = webUtilService.getPage(url)
        Document doc = Jsoup.parse(page)
        def descriptionMetas = doc.getElementsByAttributeValue("name", "description")

        def desc = descriptionMetas[0]?.attr("content")
        //CDC Bad metadata hack
        if(desc.startsWith("<p>")){
            try {
                return desc[3..(-1) - 4]
            }catch (e){
                log.error("Tried to trim <p> tag, but failed: ${desc}")
                return desc
            }
        }
        desc
    }

    Collection getMetaTags(String url){
        String page = webUtilService.getPage(url)
        Document doc = Jsoup.parse(page)
        def descriptionMetas = doc.getElementsByAttributeValue("name", "keywords")

        def keywords = descriptionMetas[0]?.attr("content")
        if(keywords){
            return keywords.split(",").collect(){it.trim()}
        }
        null
    }

    String getDescriptionFromContent(String content, int sentenceCount = 1){
        String sentence = null
        Document doc = Jsoup.parse(content)

        def ps = doc.select("p")

        for(p in ps){
            log.debug "looking at ${p.text()}"
            if(p.text().length() > 1){
                def parts = p.text().split('\\.')
                if(parts.size() >= 1){
                    log.debug p.text() + "seems like a good fit"
                    sentence = ""
                    int
                    for(int i=0; i<Math.min(parts.size(), sentenceCount); i++){
                        sentence += "${parts[i]}."
                    }
                    break
                }
            }
        }
        log.debug "ended up with: |${sentence}|"
        sentence
    }

    private Document addAutoFocus(Document doc){
        def elements = doc.getElementsByTag("h1")
        if(!elements){ elements = doc.getElementsByTag("h2")}
        if(!elements){ elements = doc.getElementsByTag("h3")}
        if(!elements){ elements = doc.getElementsByTag("h4")}
        if(!elements){ elements = doc.getElementsByTag("h5")}
        if(!elements){ elements = doc.getElementsByTag("h6")}
        if(!elements){ elements = doc.getAllElements()}

        if(elements){
            elements[0].attr("autofocus", "true");
        }
        doc
    }

    private Document customizeStyles(Document doc, Map params){
        //Image Styles ------------------------------------------------------
        String imageStyles = ""
        if(params.imageFloat){
            imageStyles += "float:${params.imageFloat};"
        }

        if(params.imageMargin){
            def vals = params.imageMargin.split(',')
            boolean validValues = true
            if(vals.size() < 4){ validValues = false}

            if(validValues){
                vals.each { val ->
                    if(!val.isNumber()){ validValues = false}
                }
            }

            if(validValues){
                imageStyles += "margin:${vals[0]}px ${vals[1]}px ${vals[2]}px ${vals[3]}px;"
            }
        }

        if(imageStyles){
            Elements images = doc.getElementsByTag("img")
            images.each { image ->
                image.attr("style", imageStyles)
            }
        }

        //Text Styles -------------------------------------------------------
        String textStyles = ""
        if(params["font-size"]){
            if(params["font-size"].isNumber()){
                textStyles += "font-size:${params["font-size"]}pt;"
            }
        }

        if(textStyles){
            Elements ps = doc.select("p")
            ps.each{ p ->
                p.attr("style", textStyles)
            }

            Elements divs = doc.select("div")
            divs.each{ div ->
                div.attr("style", textStyles)
            }

            Elements spans = doc.select("span")
            spans.each{ span ->
                span.attr("style", textStyles)
            }
        }

        doc
    }

    private Document removeInlineStylesFromDocument(Document doc){
        Elements withStyles = doc.select("[style]")
        withStyles.each{ element ->
            element.removeAttr("style")
        }
        doc
    }

    private Document removeInlineIdsFormDocument(Document doc){
        Elements withIds = doc.select("[id]")
        withIds.each{ element ->
            element.removeAttr("id")
        }
        doc
    }

    private Document removeScriptsFromDocument(Document doc){
        Elements scripts = doc.select("script")
        scripts.each{ script ->
            if(script.attr('type') == "application/ld+json"){
                //preserve json-ld data in the extracted content
            } else{
                script.remove()
            }
        }
        doc
    }

    private Document removeImagesFromDocument(Document doc){
        Elements images = doc.select("img")
        images.each{ img ->
            img.remove()
        }
        doc
    }

    private Document removeBreaksFromDocument(Document doc){
        Elements brs = doc.select("br")
        brs.each{ br ->
            br.remove()
        }
        doc
    }

    private Document removeBadTagsFromDocument(Document doc){
        Elements styleBlock = doc.select("style")
        styleBlock.each{ style ->
            style.remove()
        }

        Elements links = doc.select("link")
        links.each{ link ->
            link.remove()
        }
        doc
    }

    private Document removeClassesFromDocument(Document doc){
        Elements eles = doc.select("[class]")
        String extractionClass = grailsApplication.config.syndication.contentExtraction.cssClassName ?: 'syndicate'
        eles.each{ ele ->
            if(extractionClass in ele.attr("class").split(" ")){
                ele.removeAttr("class")
                ele.addClass(extractionClass)
            } else{
                ele.removeAttr("class")
            }
        }
        doc
    }

    private Document patchRelativeUrlsFromDocument(Document doc, String newUrlBase){
        Elements links = doc.getElementsByTag("a")
        links.each{ Element link ->
            link.attr("href", qualifyLink(link.attr("href"), newUrlBase))
        }

        Elements images = doc.getElementsByTag("img")
        images.each { image ->
            image.attr("src", qualifyLink(image.attr("src"), newUrlBase))
        }

        doc
    }

    private String getElementByClassFromDocument(Document doc, String cssClass){
        doc.getElementsByClass(cssClass).toString()
    }

    private String qualifyLink(String link, String base) {
        if(link == "/"){
            return link
        }
        //Ignore mailto links!
        if (link.contains("mailto")) {
            return link
        }
        //Fix page anchor conversion by accident
        if (link.startsWith("#")) {
            return link
        }
        String[] baseURLStack = base.split("/")
        String newURL = ""
        String[] linkStack = link.split("/")
        if(linkStack.size() == 0){
            log.error("The link could not be broken into parts: ${link}")
            return link
        }
        if (linkStack[0].startsWith("http")) { // Fully qualified case
            // println "Qualified Link: " + link
            return link
        }
        if (linkStack[0].length() == 0) { // root case
            for (int i = 0; i < 3; i++) {
                // - http:, , domain.company.whatever - occupy index 0-2
                newURL += baseURLStack[i];
                if (i < 2) {
                    newURL += "/"
                }
            }
            // println "Qualified Link: " + newURL + link
            return newURL + link
        }
        if (linkStack[0].equals("..")) { // Depth case
            int depth = 1 // we already know there is 1
            for (int i = 1; i < linkStack.size(); i++) {
                // if its not a .., then we're done
                if (linkStack[i] != "..") {
                    break
                }
                depth++ // otherwise increase depth
            }
            // pop until max depth is reached
            for (int i = 0; i < baseURLStack.size() - depth ; i++) {
                newURL += baseURLStack[i] + "/"
            }
            // start after the ../../'s
            for (int i = depth; i < linkStack.size(); i++) {
                newURL += linkStack[i]
                if (i < linkStack.size() - 1) {
                    newURL += "/"
                }
            }
            // println "Qualified Link: " + newURL
            return newURL
        }
        if (base.endsWith("/")) { // Same directory case
            // println "Qualified Link: " + base + link
            return base + link
        } else {
            // println "Qualified Link: " + base + "/" + link
            return base + "/" + link
        }
    }
}
