/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/


package com.ctacorp.syndication.testdata

import com.ctacorp.syndication.social.SocialMediaAccount
import com.ctacorp.syndication.*
import com.ctacorp.syndication.media.*
import com.ctacorp.syndication.authentication.*
import com.ctacorp.syndication.storefront.UserMediaList
import syndication.tinyurl.TinyUrlService
import com.ctacorp.syndication.contentextraction.YoutubeService
import groovy.util.logging.*

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

/**
 * Created with IntelliJ IDEA.
 * User: Steffen Gates
 * Date: 9/6/13
 * Time: 10:53 AM
 *
 */
@Log4j
class TestDataPopulator {
    def grailsApplication
    YoutubeService youtubeService
    TinyUrlService tinyUrlService
    Random ran = new Random()
    def names = []
    def words = []

    private seedHealthReportTestsData(){
        Language english = Language.findByIsoCode("eng")
        Source hhs = Source.findByAcronym("HHS")

//        saveMedia(new Html(sourceUrl:"http://www.example.com", name:"Valid URL, No Markup", language: english, source:hhs))   //bad markup
//        saveMedia(new Html(sourceUrl:"http://www.example.com/jhgfjkfgyt", name:"Bad URL", language: english, source:hhs))      //404
//        saveMedia(new Video(sourceUrl:"http://www.youtube.com/watch?v=jhgvsdkashj", name:"Bad YoutubeVideo", language: english, source:hhs, duration:500))      //bad youtube
//        saveMedia(new Html(sourceUrl:"http://localdev.com:8080/Syndication/test/threeOTwo", name:"Redirect, should work", language: english, source:hhs))      //302
//        saveMedia(new Html(sourceUrl:"http://localdev.com:8080/Syndication/test/fiveHundred", name:"500 error", language: english, source:hhs))      //500
//        saveMedia(new Html(sourceUrl:"http://localdev.com:8080/Syndication/test/shortContent", name:"short content", language: english, source:hhs))     //short content
    }

    private saveMedia(MediaItem mi){
        mi.validate()
        if(mi.hasErrors()){
            println mi.errors
        } else{
            def saved = mi.save(flush:true)
            assert saved
            return saved
        }
    }

    def seedMetrics(){
//        def today = new Date()
//        Random randomViewCount = new Random()
//        def batch = []
//
//        Html.list().each { html ->
//            0.step(300, 5) { days ->
//               batch << new MediaMetric([media: html, day: today - days, apiViewCount: randomViewCount.nextInt(150) + 1, storefrontViewCount: randomViewCount.nextInt(100) + 1])
//            }
//        }
//        batchSaver(MediaMetric, batch)
    }

    def getImageSize(String sourceUrl){
        URL imageUrl = new URL(sourceUrl)
        BufferedImage img = ImageIO.read(imageUrl.openConnection().getInputStream())
        [width:img.width, height:img.height]
    }

    def getImageFormat(String sourceUrl){
        sourceUrl = sourceUrl.toLowerCase()
        if(sourceUrl.endsWith(".jpg")){
            return "jpg"
        }
        if(sourceUrl.endsWith(".jpeg")){
            return "jpg"
        }
        if(sourceUrl.endsWith(".png")){
            return "png"
        }
    }

    def seedNihContent(){
        def videos = []
        //Videos
        def video = youtubeService.importYoutubeVideo("https://www.youtube.com/watch?v=hTz_rGP4v9Y")
        video.description = "X-rays were one of the first forms of biomedical imaging and NIBIB's 60 Seconds of Science explain how they create those images of bones we all know well."
        videos << video.save()
        video = youtubeService.importYoutubeVideo("https://www.youtube.com/watch?v=6jSV6OV7rqE")
        video.description = "Meet Quantum Dot and find out how nanotechnology researchers hope to use quantum dots in the health care of the future."
        videos << video.save()
        video = youtubeService.importYoutubeVideo("https://www.youtube.com/watch?v=dmvV_OvdV18")
        video.description = "National Eye Institute video features Dr. Matt McMahon presenting a fun example and explains how it plays tricks on our eyes."
        videos << video.save()
        video = youtubeService.importYoutubeVideo("https://www.youtube.com/watch?v=wzkQyWpu10E")
        video.description = "This 4-minute video by the National Institutes of Health shows the intricate mechanisms involved in the progression of Alzheimer's disease in the brain."
        videos << video.save()
        video = youtubeService.importYoutubeVideo("https://www.youtube.com/watch?v=BYIeJBDNcpo")
        video.description = "NOMID, or neonatal-onset multisystem inflammatory disease, is a rare disease that is often fatal. This video tells the story of Kayla Martínez, who was diagnosed with NOMID as a baby. It also tells the story of a treatment for NOMID that was discovered by Dr. Raphaela Goldbach-Mansky at the National Institute of Arthritis and Musculoskeletal and Skin Diseases at the National Institutes of Health."
        videos << video.save()
        video = youtubeService.importYoutubeVideo("https://www.youtube.com/watch?v=0DSA_8t4-UA")
        video.description = "The sixth leading cause of death in the United States is the result of hospitalacquired infections which often result in nonhealing wounds colonized by communities of bacteria call biofilms. The research in our lab aims to uncover the mechanisms at the root of the deviation from the normal healing process that results in the development of chronic wounds. These metabolomic studies identify specific metabolite profiles that may be associated with pathogenicity in the chronic wound and could potentially be used in novel noninvasive diagnostics."
        videos << video.save()
        video = youtubeService.importYoutubeVideo("https://www.youtube.com/watch?v=eP92qEDZ6rE")
        video.description = "We have made a video that shows a little bit about the new 3D neuronal imaging techniques we do in the Yuste lab."
        videos << video.save()
        video = youtubeService.importYoutubeVideo("https://www.youtube.com/watch?v=HLFQM1e-vJw")
        video.description = "Simbios is the NIH funded center for PhysicsBased Simulation of Biological Structures. Simbios provides infrastructure software and training to help biomedical researchers understand biological form and function as they create novel drugs synthetic tissues medical devices and surgical interventions. Working with Dr. Scott Delp's biomechanics lab Simbios has developed the free software OpenSim which is now used by scientists around the world to model humans and animals and understand how they move. This video gives an introduction to the project and describes how orthopaedic surgeons use modeling to help plan surgery for children with cerebral palsy."
        videos << video.save()
        video = youtubeService.importYoutubeVideo("https://www.youtube.com/watch?v=gYkP9ED5naA")
        video.description = "Forum celebrating the remarkable advances scientists are making to save, extend, and improve lives worldwide, focusing on the health, social, economic, and other benefits of science and for a renewed commitment to advancing biomedical research."
        videos << video.save()

        video = youtubeService.importYoutubeVideo("https://www.youtube.com/watch?v=dJYzwuYKijU")
        video.description = "In the first segment of the video, study participant Kent Stephenson does voluntary training with spinal stimulation. In the last segment, study participant Rob Summers tosses the medicine ball with research staff member Paul Criscola. Studies were conducted at the Human Locomotion Research Center laboratory, a part of the University of Louisville's Kentucky Spinal Cord Injury Research Center, Frazier Rehab Institute, Louisville Kentucky."
        videos << video.save(flush:true)


    }

    def seedMicrositeData(){
        println "loading microsite data..."
        Image.withTransaction {
            (1..32).each{
                String sourceUrl =  "https://dl.dropboxusercontent.com/u/39235514/syndicationHealthImages/${it}.jpg"
                def dim = getImageSize(sourceUrl)
                new Image(name: "Healthy Visions ${it}", source: Source.findByAcronym("HHS"), language: Language.findByIsoCode("eng"), sourceUrl: sourceUrl, height: dim.height, width: dim.width, imageFormat: getImageFormat(sourceUrl), altText: "A picture of something healthy.").save()
            }
        }

        User storefrontUser = new User(username: "user@example.com", name: "Steffen Gates", password: "ABC123def").save(flush:true)
        assert storefrontUser
        Role storefrontRole = Role.findByAuthority("ROLE_STOREFRONT_USER")
        assert storefrontRole
        UserRole userRole = UserRole.create(storefrontUser, storefrontRole, true)
        assert userRole

        UserMediaList imageList = new UserMediaList(name: "Healthy Living Images", user: storefrontUser, mediaItems: Image.findAllBySourceUrlIlike("https://dl.dropboxusercontent.com/u/39235514/syndicationHealthImages%"), description: "Images of healthy people and lifestyles.").save(flush:true)
        assert imageList

        UserMediaList videoList = new UserMediaList(name: "Noteworthy Youtube Videos", user: storefrontUser, mediaItems: Video.list(), description: "Videos showing the dangers of bad things and the benefits of good things!").save(flush:true)
        assert videoList

        UserMediaList htmlContent = new UserMediaList(name: "Excellent articles", user: storefrontUser, mediaItems: Html.list(max:25, sort:"id", order:"asc"), description: "Thought provoking articles with depth of information").save(flush:true)
        assert htmlContent

        UserMediaList mixedContent = new UserMediaList(name: "Mixed content", user:storefrontUser, mediaItems: Html.list(max:10, sort:"name", order:"asc") + Video.list(max:10, sort:"name", order:"asc") + Image.list(max:10, sort:"name", order:"asc"), description: "A mixed bag of content for all.").save(flush:true)
        assert mixedContent

        println "... microsite data load complete"
    }

    def seedRealExamples() {
        seedUsers()
        seedLanguages()
        seedSources()

        Language english = Language.findByIsoCode("eng")
        Source cdc = Source.findByAcronym("CDC")
        Source hhs = Source.findByAcronym("HHS")
        Source fda = Source.findByAcronym("FDA")


        def items = [
            [title: "Heart Disease & Stroke", url: "http://www.cdc.gov/vitalsigns/HeartDisease-Stroke/index.html", description: "Nearly 1 in 3 deaths in the US each year is caused by heart disease and stroke.", hash: "A1B2C3D"],
            [title: "Be Ready", url: "http://www.cdc.gov/features/beready/index.html", description: "Would you be ready if there were an emergency? Be prepared: throughout September there will be activities across the country to promote emergency preparedness."],
            [title: "Healthy Swimming", url: "http://www.cdc.gov/features/healthyswimming/index.html", description: "Stay healthy and avoid recreational water illnesses (RWIs) when you swim or use the hot tub/spa by following a few simple steps."],
            [title: "Dry Pet Food", url: "http://www.cdc.gov/features/salmonelladrypetfood/index.html", description: "A healthy diet is important for pets. But did you know that dry pet food, treats, and supplements can become contaminated with Salmonella, a harmful germ that can make both people and pets sick? To protect you and your pet from getting sick, it is important to know how to correctly purchase, handle, store, and behave when handling dry pet foods and treats."],
            [title: "Preventing Suicide", url: "http://www.cdc.gov/features/PreventingSuicide/index.html", description: "Each year, more than 36,000 Americans take their own lives and about 465,000 people receive medical care for self-inflicted injuries. September 10th is World Suicide Prevention Day. Help prevent suicide in your community."],
            [title: "Grandkid's Health", url: "http://www.cdc.gov/features/grandparents/index.html", description: "Aging well benefits not only you, but also the people you love. Take steps to live a long and healthy life and guide your grandchildren’s health and safety."],
            [title: "Cholesterol Awareness", url: "http://www.cdc.gov/features/CholesterolAwareness/index.html", description: "Learn what steps you can take to prevent high cholesterol or to reduce your LDL \"bad\" cholesterol level."],
            [title: "Preventing Type2 Diabetes", url: "http://www.cdc.gov/features/Fotonovela/index.html", description: "Fotonovela Tells How to Prevent Type 2 Diabetes"],
            [title: "Treating Conjunctivitis", url: "http://www.cdc.gov/features/conjunctivitis/index.html", description: "Pink Eye: Usually Mild and Easy to Treat"],
            [title: "Whooping Cough", url: "http://www.cdc.gov/Features/Pertussis/", description: "Whooping cough is very contagious and can cause serious illness―especially in babies too young to be fully vaccinated. Protect babies from whooping cough by getting your vaccine and making sure your baby gets his vaccines."],
            [title: "Hand, Foot, and Mouth Disease", url: "http://www.cdc.gov/Features/HandFootMouthDisease/", description: "Hand, foot, and mouth disease is a contagious viral illness. It commonly affects infants and young children. There is no vaccine to prevent the disease. However, you can take simple steps to reduce your risk."],
            [title: "Newborn Screening", url: "http://www.cdc.gov/features/newbornscreening50years/index.html", description: "This year marks 50 years of saving lives through newborn screening. How much do you know about newborn screening? Take our quiz to find out."],
            [title: "September Is World Alzheimer's Month", url: "http://www.cdc.gov/features/worldalzheimersday/index.html", description: "Learn more about Alzheimer's disease and efforts to address the nation's sixth leading cause of death."],
            [title: "Childhood Obesity Awareness", url: "http://www.cdc.gov/features/childhoodobesity/index.html", description: "Fruits and vegetables are important in promoting good health, including helping to lose or manage weight."],
            [title: "Vaccinate against Flu", url: "http://www.cdc.gov/features/flu/index.html", description: "Everyone 6 months and older should get an annual flu vaccine. It takes about two weeks after vaccination for your body to develop an immune response. Get vaccinated now so you will be protected all season long!"],
            [title: "High blood pressure education.", url: "http://www.cdc.gov/Features/HighBloodPressure/index.html", description: "May is High Blood Pressure Education Month. Have you talked about a goal for your blood pressure with your health care provider? If not, do it at your next visit."],
            [title: "Asthma Awareness", url: "http://www.cdc.gov/Features/AsthmaAwareness/index.html", description: "Successful asthma management includes knowing the warning signs of an attack, avoiding things that may trigger an attack, and following the advice of your healthcare provider."],
            [title: "Seasonal Flu", url: "http://www.flu.gov/about_the_flu/seasonal/index.html", description: "Seasonal flu is a contagious respiratory illness caused by flu viruses. Approximately 5-20% of U.S. residents get the flu each year.", hash: "1234abcd"],
            [title: "Ebola Outbreak 2014 - 2015: Information Resources", url: "http://sis.nlm.nih.gov/dimrc/ebola_2014.html", description: "This list of authoritative resources supports the medical response to the 2014-2015 Ebola outbreak. This list is compiled and maintained by the U.S. National Library of Medicine, Disaster Information Management Research Center."],
            [title: "Flu Season Is Here- Vaccinate to Protect You and Your Loved Ones from Flu", url: "http://t.cdc.gov/synd.aspx?js=0&rid=cs_797&url=http://t.cdc.gov/BWJ", description: "Everyone 6 months and older should get an annual flu vaccine. It takes about two weeks after vaccination for your body to develop full protection against the flu. Get vaccinated by October to protect yourself and your loved ones!."],
            [title: "Ebola Viral Disease Outbreak — West Africa, 2014", url: "http://www.cdc.gov/mmwr/preview/mmwrhtml/mm6325a4.htm", description: "On June 24, 2014, this report was posted as an MMWR Early Release on the MMWR website (http://www.cdc.gov/mmwr)."],
            [title: "Take Caution When Bats Are Near - CDC Features", url: "http://www.cdc.gov/Features/Bats/index.html", description: "Bats play an important role in our ecosystem. However, they are also associated with diseases deadly to humans. Learn how you can stay safe when bats are near."],
            [title: "Symptoms", url: "http://www.flu.gov/symptoms-treatment/symptoms/index.html", description: "Find a list of flu symptoms, a comparison of flu and cold symptoms, and guidance on when to seek emergency medical assistance on Flu.gov."],
            [title: "Trends in Foodborne Illness in the United States, 2012 - Bar Graph - CDC Features", url: "http://www.cdc.gov/Features/dsfoodnet2012/figure1.html", description: "Documenting trends—which illnesses are decreasing and increasing—is essential for monitoring our progress in reducing foodborne illness."]

        ]

        def batch = []

        items.each { item ->
            batch << new Html(
                name: item.title,
                description: item.description,
                sourceUrl: item.url,
                dateSyndicationCaptured: new Date(),
                dateSyndicationUpdated: new Date(),
                dateContentAuthored: new Date()-ran.nextInt(365)+30,
                dateContentUpdated: new Date()-ran.nextInt(15),
                dateContentPublished: new Date()-ran.nextInt(10),
                dateContentReviewed: new Date()-ran.nextInt(5),
                language: english,
                externalGuid: "${item.url}".hashCode(),
                source: cdc,
                hash: item.hash
            )
        }

        batchSaver(Html, batch)

        //Alternate Images
        Html.list().each{ html ->
            (ran.nextInt(3)+1).times{
                AlternateImage ai = new AlternateImage(
                    width:ran.nextInt(2038)+10,
                    height:ran.nextInt(1590)+10,
                    name:"Thumbnail ${ran.nextInt(10)}",
                    url:"http://images.hhs.gov/${ran.nextInt(9999)}",
                    mediaItem: html
                )
                ai.save()
                assert ai.id
                html.addToAlternateImages(ai)
            }
        }

        batch = []

        def desc = "June 14, 2012; The U.S. Department of Health and Human Services (HHS) and the U.S. Food and Drug Administration (FDA), in partnership with state and local public health authorities and tobacco prevention professionals throughout the Pacific Northwest, held a Youth and Tobacco Town Hall Meeting on the Seattle campus of the University of Washington. The town hall was part of the broader HHS effort to prevent children from starting to use tobacco and to help current tobacco users quit. Three top U.S. health leaders, public health professionals, tobacco use prevention specialists, educators, advocates, policy makers and—importantly—young people from around the Pacific Northwest attended the meeting. The goals of the event were to share information, tools, and best practices in tobacco use prevention, to enhance existing efforts and to devise new strategies to help youth and young people resist social and industry pressure to initiate tobacco use, or to end tobacco use if it has already begun."
        items = [
            [title: "Healthy Kids", url: "http://www.cdc.gov/features/flu/flu_456px.jpg", width: 345, height: 191, desc: "Healthy happy kids having a fun and healthy time."],
            [title: "Youth & Tobacco Town Hall 1", url: "http://farm9.staticflickr.com/8152/7415645094_e4f9827465_o.jpg", width: 3824, height: 2540, desc: desc],
            [title: "Youth & Tobacco Town Hall 2", url: "http://farm9.staticflickr.com/8005/7415645828_c8df619e8f_o.jpg", width: 3789, height: 2517, desc: desc],
            [title: "Youth & Tobacco Town Hall 3", url: "http://farm6.staticflickr.com/5160/7415662296_17ddb42151_o.jpg", width: 3123, height: 2074, desc: desc],
            [title: "Youth & Tobacco Town Hall 4", url: "http://farm9.staticflickr.com/8166/7415661744_9745bd9a51_o.jpg", width: 2453, height: 3693, desc: desc],
            [title: "Youth & Tobacco Town Hall 5", url: "http://farm8.staticflickr.com/7253/7415655194_3b0129291e_o.jpg", width: 4288, height: 2848, desc: desc],
            [title: "Youth & Tobacco Town Hall 6", url: "http://farm6.staticflickr.com/5445/7415656828_e59b76ecdf_o.jpg", width: 2564, height: 3860, desc: desc],
            [title: "Youth & Tobacco Town Hall 7", url: "http://farm6.staticflickr.com/5039/7415661070_1c3c36dec4_o.jpg", width: 3711, height: 2465, desc: desc],
            [title: "Youth & Tobacco Town Hall 8", url: "http://farm6.staticflickr.com/5115/7415659906_69cebdb0a9_o.jpg", width: 2626, height: 3954, desc: desc],
            [title: "Youth & Tobacco Town Hall 9", url: "http://farm9.staticflickr.com/8008/7415659216_e565ac25da_o.jpg", width: 3905, height: 2593, desc: desc],
            [title: "Youth & Tobacco Town Hall 10", url: "http://farm9.staticflickr.com/8008/7420010158_c2a9f0e1fd_o.jpg", width: 2589, height: 1720, desc: desc],
            [title: "Youth & Tobacco Town Hall 11", url: "http://farm8.staticflickr.com/7259/7415647408_23e02e24e2_o.jpg", width: 3809, height: 2530, desc: desc],
            [title: "Youth & Tobacco Town Hall 12", url: "http://farm8.staticflickr.com/7269/7415651398_ed438c3765_o.jpg", width: 2573, height: 3874, desc: desc],
            [title: "Youth & Tobacco Town Hall 13", url: "http://farm8.staticflickr.com/7247/7415641940_ebe7061ddd_o.jpg", width: 4288, height: 2848, desc: desc],
            [title: "Youth & Tobacco Town Hall 14", url: "http://farm8.staticflickr.com/7275/7415648752_8a59b311af_o.jpg", width: 3451, height: 2292, desc: desc],
            [title: "Youth & Tobacco Town Hall 15", url: "http://farm6.staticflickr.com/5116/7415649474_f0d229bacd_o.jpg", width: 2574, height: 3876, desc: desc],
            [title: "Youth & Tobacco Town Hall 16", url: "http://farm6.staticflickr.com/5444/7415649912_d68112f44f_o.jpg", width: 3246, height: 2156, desc: desc],
            [title: "Youth & Tobacco Town Hall 17", url: "http://farm8.staticflickr.com/7125/7415647976_bba632768e_o.jpg", width: 2722, height: 1808, desc: desc],
            [title: "Youth & Tobacco Town Hall 18", url: "http://farm8.staticflickr.com/7139/7420009470_1cebff7bc8_o.jpg", width: 2832, height: 1881, desc: desc],
            [title: "Youth & Tobacco Town Hall 19", url: "http://farm9.staticflickr.com/8013/7420008656_10a8101885_o.jpg", width: 2449, height: 1627, desc: desc],
            [title: "Youth & Tobacco Town Hall 20", url: "http://farm9.staticflickr.com/8004/7420007630_9841968c8b_o.jpg", width: 2334, height: 1550, desc: desc],
            [title: "Youth & Tobacco Town Hall 21", url: "http://farm8.staticflickr.com/7262/7420005844_229b3c6873_o.jpg", width: 2778, height: 1845, desc: desc],
            [title: "Youth & Tobacco Town Hall 22", url: "http://farm9.staticflickr.com/8150/7415637182_aefc6320d6_o.jpg", width: 3719, height: 2470, desc: desc],
            [title: "Youth & Tobacco Town Hall 23", url: "http://farm9.staticflickr.com/8166/7415635114_d5b25dc707_o.jpg", width: 2286, height: 3442, desc: desc]
        ]

        items.each { item ->
            batch << new Image(
                name: item.title,
                sourceUrl: item.url,
                dateSyndicationCaptured: new Date() - (ran.nextInt(10) + 3),
                dateSyndicationUpdated: new Date() - (ran.nextInt(3)),
                dateContentAuthored: new Date()-ran.nextInt(365)+30,
                dateContentUpdated: new Date()-ran.nextInt(15),
                dateContentPublished: new Date()-ran.nextInt(10),
                dateContentReviewed: new Date()-ran.nextInt(5),
                language: english,
                externalGuid: "${item.url}".hashCode(),
                source: cdc,
                width: item.width,
                height: item.height,
                imageFormat: "jpg",
                altText: item.title,
                description: item.desc
            )
        }

        batchSaver(Image, batch)

        //Infographic MediaItems
        items = [[title: "Get the Facts on Addiction", url:"http://therealcost.betobaccofree.hhs.gov/sites/default/files/images/infographic/did-you-know-few-years.jpg", width:"300", height:"330", desc: "3 out of 4 teen smokers who think they will stop smoking in 5 years don't."],[title:"Get the Facts on Addiction", url:"http://therealcost.betobaccofree.hhs.gov/sites/default/files/images/infographic/did-you-know-cravings-v2.jpg\t", width:"300", height: "330", desc:"Just a few cigarettes per month can lead to cravings in some teens."],[title:"Get the Facts on Health Effects", url:"http://therealcost.betobaccofree.hhs.gov/sites/default/files/images/infographic/fact-id-7-lung-growth.jpg", width:"300", height:"300", desc:"Smoking as a teen can stunt lung growth."],[title:"Get the Facts on Cosmetic Consequences", url:"http://therealcost.betobaccofree.hhs.gov/sites/default/files/images/infographic/accelerates-aging.jpg", width:"300", height:"300", desc:"Smoking accelerates skin aging which could lead to premature wrinkles."]]

        batch = []

        items.each { item ->
            batch << new Infographic(
                    name: item.title,
                    sourceUrl: item.url,
                    dateSyndicationCaptured: new Date() - (ran.nextInt(10) + 3),
                    dateSyndicationUpdated: new Date() - (ran.nextInt(3)),
                    dateContentAuthored: new Date()-ran.nextInt(365)+30,
                    dateContentUpdated: new Date()-ran.nextInt(15),
                    dateContentPublished: new Date()-ran.nextInt(10),
                    dateContentReviewed: new Date()-ran.nextInt(5),
                    language: english,
                    externalGuid: "${item.url}".hashCode(),
                    source: cdc,
                    width: item.width,
                    height: item.height,
                    imageFormat: "jpg",
                    altText: item.title,
                    description: item.desc
            )
        }

        batchSaver(Infographic, batch)

        //PDF MediaItems
        items = [[title:"The Precision Medicine Initiative: Infographic", url:"http://www.nih.gov/precisionmedicine/infographic-printable.pdf", desc:"Precision medicine is an emerging approach for disease prevention and treatment that takes into account"], [title:"HHS Confrence Spending", url:"http://www.hhs.gov/asfr/ogapa/acquisition/policies/files/fy2012-hhs-conference-spending-full.pdf", desc:"The Institute provided opportunities for leadership development through the promotion of effective strategies for "]]

        batch = []

        items.each { item ->
            batch << new PDF(
                    name: item.title,
                    description: item.description,
                    sourceUrl: item.url,
                    dateSyndicationCaptured: new Date(),
                    dateSyndicationUpdated: new Date(),
                    dateContentAuthored: new Date()-ran.nextInt(365)+30,
                    dateContentUpdated: new Date()-ran.nextInt(15),
                    dateContentPublished: new Date()-ran.nextInt(10),
                    dateContentReviewed: new Date()-ran.nextInt(5),
                    language: english,
                    externalGuid: "${item.url}".hashCode(),
                    source: cdc,
                    hash: item.hash
            )
        }

        batchSaver(PDF, batch)

        def youtubeVideos = [
                "http://www.youtube.com/watch?v=-VI_A1TPS6o",
                "http://www.youtube.com/watch?v=Jn9OBSNBE4M",
                "http://www.youtube.com/watch?v=R-oxvOTLCYU",
                "http://www.youtube.com/watch?v=6YWEW3I1fKs",
                "http://www.youtube.com/watch?v=4Zuo88K2Gdc",
                "http://www.youtube.com/watch?v=vXRYmVUOD7g",
                "http://www.youtube.com/watch?v=10gcsrx-ANA",
                "http://www.youtube.com/watch?v=xLnA-ZdKuEY",
                "http://www.youtube.com/watch?v=zMdFj4e0Q18",
                "http://www.youtube.com/watch?v=QW1yodZJpG8",
                //Real Cost videos
                "http://www.youtube.com/watch?v=zhbXENhrkTA",
                "http://www.youtube.com/watch?v=asarKLMCvdo"
        ]

        youtubeVideos.each{
            try{
                youtubeService.importYoutubeVideo(it)
            }catch (e){
                println "couldn't import ${it}"
            }
        }

        def count = Video.count()

        println "$count Videos created."

        seedHealthReportTestsData()
        println "Health report data seeded"

        //Collections ---------------------------
        batch = []

        items = [title: "Random Items", url: "http://tiny.hhs.gov/12345", desc: "These are random items in a collection"]

        def randomCollection = new Collection(
            name: items.title,
            sourceUrl: items.url,
            dateSyndicationCaptured: new Date() - (ran.nextInt(10) + 3),
            dateSyndicationUpdated: new Date() - (ran.nextInt(3)),
            language: english,
            externalGuid: "${items.url}".hashCode(),
            source: hhs,
            description: items.desc
        )

        assert randomCollection.save(flush: true)

        def allItems = MediaItem.list()

        (ran.nextInt(10) + 10).times {
            randomCollection.addToMediaItems(allItems[ran.nextInt(allItems.size())])
        }

        println "${MediaItem.where { eq 'class', 'com.ctacorp.syndication.media.Collection' }.count()} Collections created."

        // Facebook users -----------------------------------
        def fbUser = new SocialMediaAccount(accountName: "steffen.gates.94", accountType: "facebook")
        fbUser.save(flush:true)

        println SocialMediaAccount.count() + " social media accounts created"

        //Seed the campaigns ---------------------------
        seedCampaigns()

        //Random Association -------------
        randomMediaCampaignAssociation()

        //Special one-off collections for Ray
        def oneOff = new Collection(
            name: "Stop Tobacco Now!",
            description: "It's killing us, we need to stop it today!",
            sourceUrl: "http://www.example.com/2356434",
            language: Language.findByIsoCode("eng"),
            source:Source.findByAcronym("HHS")
        )
        oneOff.save(flush:true)
        assert oneOff.id

        oneOff.addToMediaItems(Html.first())
        oneOff.addToMediaItems(Video.first())
        oneOff.addToMediaItems(Image.first())

        oneOff = new Collection(
            name: "Why you need healthcare",
            description: "Because if you didn't, then no one would.",
            sourceUrl: "http://www.example.com/98765434543",
            language: Language.findByIsoCode("eng"),
            source:Source.findByAcronym("HHS")
        )

        oneOff.addToMediaItems(Html.list()[2])
        oneOff.addToMediaItems(Html.list()[3])
        oneOff.addToMediaItems(Html.list()[4])
        oneOff.addToMediaItems(Video.list()[2])
        oneOff.addToMediaItems(Video.list()[3])
        oneOff.addToMediaItems(Video.list()[4])
        oneOff.addToMediaItems(Image.list()[2])
        oneOff.addToMediaItems(Image.list()[3])
        oneOff.addToMediaItems(Image.list()[4])

        oneOff.save(flush:true)
        assert oneOff.id

        //Random Likes --------------------------------------
        count = MediaItem.count()
        User.list().each { user ->
            (ran.nextInt(10)+1).times {
                def mi = MediaItem.load(ran.nextInt(count) + 1)
                user.addToLikes(mi)
            }
        }

        seedMetrics()
        seedExtendedAttributes()
    }

    def seedLanguages() {
        Language.seedDatabaseWithLanguages()
        def eng = Language.findByIsoCode("eng")
        eng.isActive = true
        eng.save(flush:true)
        def spanish = Language.findByIsoCode("spa")
        spanish.isActive = true
        spanish.save(flush:true)
        assert Language.count() >= 450
    }

    def seedSources() {
        Source.findOrSaveWhere(name: "Health and Human Services", acronym: "HHS", websiteUrl: "http://www.hhs.gov").save()
        Source.findOrSaveWhere(name: "National Institute of Allergy and Infectious Diseases", acronym: "NIAID", websiteUrl: "http://www.niaid.nih.gov").save()
        Source.findOrSaveWhere(name: "U S Food and Drug Administration", acronym: "FDA", websiteUrl: "http://www.fda.gov").save()
        Source.findOrSaveWhere(name: "National Institutes of Health", acronym: "NIH", websiteUrl: "http://www.nih.gov").save()
        Source.findOrSaveWhere(name: "National Cancer Institute", acronym: "NCI", websiteUrl: "http://www.cancer.gov").save()
        Source.findOrSaveWhere(name: "Centers for Disease Control and Prevention", acronym: "CDC", websiteUrl: "http://www.cdc.gov").save(flush: true)
        Source.findOrSaveWhere(name: "National Diabetes Education Program", acronym: "NDEP", websiteUrl: "http://ndep.nih.gov").save()
        Source.findOrSaveWhere(name: "National Institute on Drug Abuse", acronym: "NIDA", websiteUrl: "http://www.drugabuse.gov").save()
        Source.findOrSaveWhere(name: "NIH Heart, Lung and Blood Institute", acronym: "NHLB", websiteUrl: "http://www.nhlbi.nih.gov").save()
        Source.findOrSaveWhere(name: "Administration for Children and Families", acronym: "ACF", websiteUrl: "https://www.acf.hhs.gov").save()
        Source.findOrSaveWhere(name: "National Institute on Aging", acronym: "NIA", websiteUrl: "https://www.nia.nih.gov").save()

        assert Source.count() >= 6
    }

    def seedAlternateImages() {
        new AlternateImage(mediaItem: MediaItem.load(5),name: "First Image",url: 'http://www.yahoo.com/anImage.jpg',width: 5,height: 7).save()
        new AlternateImage(mediaItem: MediaItem.load(5),name: "Second Image",url: 'http://www.yahoo.com/anImage2.jpg',width: 5,height: 7).save(flush: true)

        assert AlternateImage.count() >= 2
    }

    def randomMediaCampaignAssociation() {
        def campaigns = Campaign.list()
        def media = MediaItem.list()
        Random ran = new Random()

        campaigns.each { campaign ->
            (ran.nextInt(10)+5).times {
                def med = media[ran.nextInt(media.size())]
                campaign.addToMediaItems(med)
            }
        }
    }

    def seedCampaigns() {
        def diseases = ["Achondroplasia",
            "Acne",
            "AIDS",
            "Albinism",
            "Alcoholic hepatitis",
            "Allergy",
            "Alopecia",
            "Alzheimer's disease",
            "Amblyopia",
            "Amebiasis",
            "Anemia",
            "Aneurdu",
            "Anorexia",
            "Anosmia",
            "Anotia",
            "Anthrax",
            "Appendicitis",
            "Apraxia",
            "Argyria",
            "Arthritis",
            "Aseptic meningitis",
            "Asthenia",
            "Asthma",
            "Astigmatism",
            "Atherosclerosis",
            "Athetosis",
            "Bacterial meningitis",
            "Beriberi",
            "Black Death"
        ]

        def batch = []

        diseases.each {
            batch << new Campaign(
                name: "${it}",
                description: "${it}",
                startDate: new Date(),
                endDate: new Date() + it.size(),
                source:Source.findByAcronym("HHS"))
        }
        
        //custom campaigns
        batch << new Campaign(
                name:"Ebola",
                description:"Ebola News",
                startDate: new Date(),
                endDate: new Date() + 10,
                source:Source.findByAcronym("CDC"),
                mediaItems: MediaItem.findAllByNameInList(["Ebola Outbreak 2014 - 2015: Information Resources","Ebola Viral Disease Outbreak — West Africa, 2014",""])
        )
        
        batch << new Campaign(
                name:"Disease",
                description:"Recent Disease out breaks and preventative measures.",
                startDate: new Date(),
                endDate: new Date() + 10,
                source:Source.findByAcronym("CDC"),
                mediaItems: MediaItem.findAllByNameInList(["Flu Season Is Here- Vaccinate to Protect You and Your Loved Ones from Flu", "Trends in Foodborne Illness in the United States, 2012 - Bar Graph - CDC Features", "Symptoms", "Hand, Foot, and Mouth Disease"])
        )
        
        batch << new Campaign(
                name:"Real Cost Videos",
                description:"A couple of videos",
                startDate: new Date(),
                endDate: new Date() + 10,
                source:Source.findByAcronym("HHS"),
                mediaItems: MediaItem.findAllBySourceUrlInList(["http://www.youtube.com/watch?v=zhbXENhrkTA", "http://www.youtube.com/watch?v=asarKLMCvdo", "http://www.youtube.com/watch?v=a_M9tTwLb9A","http://www.youtube.com/watch?v=mcteRv08Aco"])
        )
        
        batchSaver(Campaign, batch)
    }

    def batchSaver(domain, batch) {
        domain.withTransaction {
            batch.each {
                if (!it.save()) {
                    println it.errors
                }
            }
        }
        println "${batch.size()} ${domain.simpleName}s created."
    }

    def seedImages() {
        def images = ["snow", "snow1", "snow2", "RosyBrown1", "RosyBrown2", "snow3", "LightCoral", "IndianRed1", "RosyBrown3", "IndianRed2", "RosyBrown", "brown1",
            "firebrick1", "brown2", "IndianRed", "IndianRed3", "firebrick2", "snow4", "brown3", "red", "red1", "RosyBrown4", "firebrick3", "red2", "firebrick",
            "brown", "red3", "IndianRed4", "brown4", "firebrick4", "DarkRed", "red4", "maroon", "LightPink1", "LightPink3", "LightPink4", "LightPink2", "LightPink"]

        def batch = []

        images.each {
            def lang = Language.first()
            def source = Source.first()
            batch << new Image(name: "${it}",
                description: "${it}",
                width: 10,
                height: 10 + it.size(),
                imageFormat: "jpg",
                altText: "Color of image is :  ${it}",
                sourceUrl: "http://localhost/Syndication/images/${it}",
                dateSyndicationCaptured: new Date(),
                dateSyndicationUpdated: new Date(),
                language: lang,
                externalGuid: "http://localhost/Syndication/images/${it}".toString().hashCode(),
                source: source)
        }

        batchSaver(Image, batch)
    }

    def seedExtendedAttributes(){
        def names = ["shortDescription","mobileTitle","slangName","purpose","notes","toolTip","accesibilityMessage"]
        def values = ["This is a short description.","Mobile title","Slang name","The purpose of this is to show you the purpose","Some notes would go here","Tool Tips are Useful","Aria Alert messages could go here."]
        assert names.size() == values.size()

        MediaItem.list().each{ MediaItem mi ->
            def max = ran.nextInt(names.size())

            (1..max-1).each{ int index ->
                if(!ExtendedAttribute.findByNameAndMediaItem(names[index], mi)) {
                    mi.addToExtendedAttributes(name: names[index], value: values[index])
                }
            }
        }
    }

    def generateRandomHtmlItems(count){
        def batch = []
        def lang = Language.first()
        def source = Source.first()
        count.times{
            def name = randomName()
            def url = "http://localdev.com/Syndication/htmls/${randomWord()}"

            batch << new Html(name: "${it}",
                    description: randomDescription(),
                    sourceUrl: url,
                    dateSyndicationCaptured: new Date(),
                    dateSyndicationUpdated: new Date(),
                    language: lang,
                    source: source)
        }
        batchSaver(Html, batch)
    }

    def seedHTMLs() {
        def htmls = ["Heart disease",
            "Hepatitis-A",
            "Hepatitis-B",
            "Hepatitis-C",
            "Hepatitis-D",
            "Hepatitis-E",
            "Histiocytosis",
            "HIV",
            "Huntingtonsdisease",
            "Hypermetropia",
            "Hyperopia",
            "Hyperthyroidism",
            "Hypothermia",
            "Hypothyroid",
            "Hypotonia"]

        def batch = []

        htmls.each {
            def lang = Language.first()
            def source = Source.first()
            batch << new Html(name: "${it}",
                description: "${it}",
                sourceUrl: "http://localhost/Syndication/htmls/${it}",
                dateSyndicationCaptured: new Date(),
                dateSyndicationUpdated: new Date(),
                language: lang,
                externalGuid: "http://localhost/Syndication/htmls/${it}".toString().hashCode(),
                source: source)
        }

        batchSaver(Html, batch)
    }

    def seedInfographics() {
        def igrafix = ["Animals",
            "Business",
            "Career",
            "Computers",
            "Education",
            "Entertainment",
            "Environment",
            "Fashion",
            "Finance",
            "Food",
            "Games",
            "Geography",
            "Health",
            "HowTo",
            "Internet",
            "Living",
            "Mobile",
            "Other",
            "People",
            "Politics",
            "Science",
            "Shopping",
            "SocialMediaMarshaller",
            "Sports",
            "Technology",
            "Transportation",
            "Travel"]

        def batch = []

        igrafix.each {
            def lang = Language.last()
            def source = Source.get(2)
            batch << new Infographic(name: "${it}",
                description: "${it}",
                width: 10,
                height: 10 + it.size(),
                sourceUrl: "http://localhost/Syndication/socialMedia/${it}",
                dateSyndicationCaptured: new Date(),
                dateSyndicationUpdated: new Date(),
                language: lang,
                externalGuid: "http://localhost/Syndication/socialMedia/${it}".toString().hashCode(),
                source: source,
                altText: "Alt Text Here",
                imageFormat: "jpg"
            )
        }

        batchSaver(Infographic, batch)
    }

    def seedVideos() {
        def videos = ["google", "youtube", "cnn", "vimeo"]
        int duration = 20

        def batch = []

        videos.each {
            def lang = Language.first()
            def source = Source.last()
            batch << new Video(name: "${it}",
                description: "${it}",
                duration: duration + 5,
                sourceUrl: "http://localhost/Syndication/socialMedia/${it}",
                dateSyndicationCaptured: new Date(),
                dateSyndicationUpdated: new Date(),
                dateContentAuthored: new Date()-ran.nextInt(365)+30,
                dateContentUpdated: new Date()-ran.nextInt(15),
                dateContentPublished: new Date()-ran.nextInt(10),
                dateContentReviewed: new Date()-ran.nextInt(5),
                language: lang,
                externalGuid: "http://localhost/Syndication/socialMedia/${it}".toString().hashCode(),
                source: source)
        }

        batchSaver(Video, batch)
    }

    def seedUsers() {
        def userRole = Role.findOrSaveByAuthority("ROLE_MANAGER")
        assert userRole.id
        100.times {
            def user = new User(name: randomName(), username: randomUsername(), password: "Password123")
            while (!user.validate()) {
                user = new User(name: randomName(), username: randomUsername(), password: "Password123")
            }
            assert user.save(flush: true)
            UserRole.create user, userRole, true
        }
        println "${User.count()} users created."
    }

    String randomName() {
        initNames()
        "${names[ran.nextInt(names.size())]} ${names[ran.nextInt(names.size())]}"
    }

    def initWords(){
        if(!words){
            words = new File("/usr/share/dict/web2").readLines()
        }
    }

    def initNames(){
        if(!names){
            names = new File("/usr/share/dict/propernames").readLines()
        }
    }

    String randomWord(){
        initWords()
        words[ran.nextInt(words.size())]
    }

    String randomSentence(){
        def wordCount = ran.nextInt(12)+3
        def sentence = ""
        wordCount.times {
            sentence += "${randomWord()} "
        }
        "${sentence[0..-2].capitalize()}."
    }

    String randomDescription(){
        def sentenceCount = ran.nextInt(1)+3
        def description = ""
        sentenceCount.times {
            description += "${randomSentence()} "
        }
        description[0..-2]
    }

    String randomUsername() {
        "${names[ran.nextInt(names.size())].toLowerCase()}${names[ran.nextInt(names.size())].capitalize()}@example.com"
    }
}
