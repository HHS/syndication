
/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package syndication.testdata

import com.ctacorp.syndication.social.SocialMediaAccount
import com.ctacorp.syndication.*
import com.ctacorp.syndication.authentication.*
import org.codehaus.groovy.grails.commons.ApplicationAttributes
import syndication.tinyurl.TinyUrlService
import syndication.youtube.YoutubeService
import groovy.util.logging.*

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
    def names

    private seedHealthReportTestsData(){
        Language english = Language.findByIsoCode("eng")
        Source hhs = Source.findByAcronym("HHS")

        saveMedia(new Html(sourceUrl:"http://www.example.com", name:"Valid URL, No Markup", language: english, source:hhs))   //bad markup
        saveMedia(new Html(sourceUrl:"http://www.example.com/jhgfjkfgyt", name:"Bad URL", language: english, source:hhs))      //404
        saveMedia(new Video(sourceUrl:"http://www.youtube.com/watch?v=jhgvsdkashj", name:"Bad YoutubeVideo", language: english, source:hhs, duration:500))      //bad youtube
        saveMedia(new Html(sourceUrl:"http://localdev.com:8080/Syndication/test/threeOTwo", name:"Redirect, should work", language: english, source:hhs))      //302
        saveMedia(new Html(sourceUrl:"http://localdev.com:8080/Syndication/test/fiveHundred", name:"500 error", language: english, source:hhs))      //500
        saveMedia(new Html(sourceUrl:"http://localdev.com:8080/Syndication/test/shortContent", name:"short content", language: english, source:hhs))     //short content
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
        def today = new Date()
        Random randomViewCount = new Random()
        def batch = []

        Html.list().each { html ->
            0.step(300, 5) { days ->
               batch << new MediaMetric([media: html, day: today - days, apiViewCount: randomViewCount.nextInt(150) + 1, storefrontViewCount: randomViewCount.nextInt(100) + 1])
            }
        }
        batchSaver(MediaMetric, batch)
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
            [title: "Gynecologic Cancers", url: "http://www.cdc.gov/cancer/dcpc/resources/features/GynecologicCancers/", description: "Get the facts about the signs, symptoms, and risk factors of gynecologic cancers. When gynecologic cancers are found early, treatment is most effective."],
            [title: "Cholesterol Awareness", url: "http://www.cdc.gov/features/CholesterolAwareness/index.html", description: "Learn what steps you can take to prevent high cholesterol or to reduce your LDL \"bad\" cholesterol level."],
            [title: "Preventing Type2 Diabetes", url: "http://www.cdc.gov/features/Fotonovela/index.html", description: "Fotonovela Tells How to Prevent Type 2 Diabetes"],
            [title: "Treating Conjunctivitis", url: "http://www.cdc.gov/features/conjunctivitis/index.html", description: "Pink Eye: Usually Mild and Easy to Treat"],
            [title: "Prostate Cancer", url: "http://www.cdc.gov/cancer/dcpc/resources/features/ProstateCancer/", description: "Prostate cancer is the most common cancer in men. Talk to your doctor about prostate cancer screening."],
            [title: "Whooping Cough", url: "http://www.cdc.gov/Features/Pertussis/", description: "Whooping cough is very contagious and can cause serious illness―especially in babies too young to be fully vaccinated. Protect babies from whooping cough by getting your vaccine and making sure your baby gets his vaccines."],
            [title: "Hand, Foot, and Mouth Disease", url: "http://www.cdc.gov/Features/HandFootMouthDisease/", description: "Hand, foot, and mouth disease is a contagious viral illness. It commonly affects infants and young children. There is no vaccine to prevent the disease. However, you can take simple steps to reduce your risk."],
            [title: "Newborn Screening", url: "http://www.cdc.gov/features/newbornscreening50years/index.html", description: "This year marks 50 years of saving lives through newborn screening. How much do you know about newborn screening? Take our quiz to find out."],
            [title: "September Is World Alzheimer's Month", url: "http://www.cdc.gov/features/worldalzheimersday/index.html", description: "Learn more about Alzheimer's disease and efforts to address the nation's sixth leading cause of death."],
            [title: "Childhood Obesity Awareness", url: "http://www.cdc.gov/features/childhoodobesity/index.html", description: "Fruits and vegetables are important in promoting good health, including helping to lose or manage weight."],
            [title: "Alcohol Screening", url: "http://www.cdc.gov/features/AlcoholScreening/index.html", description: "Whooping cough is very contagious and can cause serious illness―especially in babies too young to be fully vaccinated. Protect babies from whooping cough by getting your vaccine and making sure your baby gets his vaccines."],
            [title: "Vaccinate against Flu", url: "http://www.cdc.gov/features/flu/index.html", description: "Everyone 6 months and older should get an annual flu vaccine. It takes about two weeks after vaccination for your body to develop an immune response. Get vaccinated now so you will be protected all season long!"],
            [title: "High blood pressure education.", url: "http://www.cdc.gov/Features/HighBloodPressure/index.html", description: "May is High Blood Pressure Education Month. Have you talked about a goal for your blood pressure with your health care provider? If not, do it at your next visit."],
            [title: "Asthma Awareness", url: "http://www.cdc.gov/Features/AsthmaAwareness/index.html", description: "Successful asthma management includes knowing the warning signs of an attack, avoiding things that may trigger an attack, and following the advice of your healthcare provider."],
            [title: "Seasonal Flu", url: "http://www.flu.gov/about_the_flu/seasonal/index.html", description: "Seasonal flu is a contagious respiratory illness caused by flu viruses. Approximately 5-20% of U.S. residents get the flu each year.", hash: "1234abcd"]
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

//        // Real cost content
        items = [
            [title: "Support", url: "http://ctacdev.com/drupal_fda_tobacco/?q=support/index.html", description: "If you smoke, or have experimented with smoking, here are some tools that can help provide the support you need."],
            [title: "How It Begins", url: "http://ctacdev.com/drupal_fda_tobacco/?q=decisions/how-it-begins/index.html", description: "Experimenting with cigarettes is a slippery slope, and first-timers often underestimate how addictive it can be."],
            [title: "Tobacco Plant", url: "http://ctacdev.com/drupal_fda_tobacco/?q=facts/tobacco-plant/index.html", description: "Some people believe that tobacco products that are “natural” or “free of additives” are a safer option. But that’s not true."],
            [title: "Social Pressures", url: "http://ctacdev.com/drupal_fda_tobacco/?q=decisions/how-to-deal/social-pressures/index.html", description: "Even smoking just a little bit opens the door to addiction and smoking-related disease.1 In fact, research shows that just a few cigarettes per month can lead to cravings in some teens."],
            [title: "Depression", url: "http://ctacdev.com/drupal_fda_tobacco/?q=decisions/how-to-deal/depression/index.html", description: "Everyone occasionally feels blue or sad. But these feelings are usually short-lived and pass within a couple of days."],
            [title: "Stress", url: "http://ctacdev.com/drupal_fda_tobacco/?q=decisions/how-to-deal/stress/index.html", description: "Life is stressful. Some people say they smoke as a way of coping with stress — family problems, school problems, work stress, etc. The truth is that smoking doesn’t fix your problems or make them any better."],
            [title: "How To Deal", url: "http://ctacdev.com/drupal_fda_tobacco/?q=decisions/how-to-deal/index.html", description: "Some people smoke because they believe it can help them cope with the difficult situations in their life. The truth is that smoking doesn’t fix your problems or make them any better."],
            [title: "Addiction", url: "http://ctacdev.com/drupal_fda_tobacco/?q=costs/addiction/index.html", description: "There’s no way we can have a conversation about smoking without talking about addiction. Everyone knows that cigarettes are bad for you, but most smokers get hooked before they even realize it."],
            [title: "Health Costs", url: "http://ctacdev.com/drupal_fda_tobacco/?q=costs/health-costs/index.html", description: "Smoking may stain your teeth1 and turn your fingers yellow. It can also harm your skin by destroying its elastic fibers and weakening its ability to repair itself."],
            [title: "Menthol", url: "http://ctacdev.com/drupal_fda_tobacco/?q=facts/menthol/index.html", description: "What’s different about menthol cigarettes? A mint flavor additive with cooling properties. A menthol cigarette is still a cigarette — complete with all the chemicals and addictiveness of any other tobacco product."],
            [title: "Smokeless Tobacco", url: "http://ctacdev.com/drupal_fda_tobacco/?q=facts/smokeless-tobacco/index.html", description: "Smokeless tobacco is different from cigarettes, tobacco used in cigarettes, and roll-your-own tobacco."],
            [title: "Smoked Tobacco", url: "http://ctacdev.com/drupal_fda_tobacco/?q=facts/smoked-tobacco/index.html", description: "How do you add even more chemicals to a cigarette? Light it. "],
            [title: "Other Factors", url: "http://ctacdev.com/drupal_fda_tobacco/?q=decisions/how-to-deal/other-factors/index.html", description: "Research shows that one of the reasons teens start smoking is that they are surrounded with images of smoking — from being around friends and family who smoke, to images of smoking in the movies, to advertisements in convenience stores."],
            [title: "Quitting", url: "http://ctacdev.com/drupal_fda_tobacco/?q=decisions/quitting/index.html", description: "If you smoke, or have experimented with smoking, now is the time to take back control."],
            //Linked Images
            [title: "Nearly 9 out of 10 high schoolers don't smoke", url: "http://ctacdev.com/drupal_fda_tobacco/4-out-5.html"],
            [title: "Health Benefits of Quitting", url: "http://ctacdev.com/drupal_fda_tobacco/health-benefits-quitting.html"],
            [title: "Smoking Could Cost You Teeth", url: "http://ctacdev.com/drupal_fda_tobacco/smoking-could-cost-you-teeth.html"],
            [title: "A Deadly Mix", url: "http://ctacdev.com/drupal_fda_tobacco/deadly-mix.html"],
            [title: "Are You Hooked?", url: "http://ctacdev.com/drupal_fda_tobacco/are-you-hooked.html"],
            [title: "Can You Afford The Costs of Smoking?", url: "http://ctacdev.com/drupal_fda_tobacco/can-you-afford-costs-smoking.html"]
        ]

        batch = []

        items.each { item ->
            batch << new Html(
                name: item.title,
                description: item.description,
                sourceUrl: item.url,
                dateSyndicationCaptured: new Date(),
                dateSyndicationUpdated: new Date(),
                dateContentAuthored: new Date() - 30,
                dateContentUpdated: new Date() - 30,
                dateContentPublished: new Date() - 30,
                dateContentReviewed: new Date() - 30,
                language: english,
                source: Source.findByAcronym("FDA")
            )
        }

        batchSaver(Html, batch)

        batch = []

        def desc = "June 14, 2012; The U.S. Department of Health and Human Services (HHS) and the U.S. Food and Drug Administration (FDA), in partnership with state and local public health authorities and tobacco prevention professionals throughout the Pacific Northwest, held a Youth and Tobacco Town Hall Meeting on the Seattle campus of the University of Washington. The town hall was part of the broader HHS effort to prevent children from starting to use tobacco and to help current tobacco users quit. Three top U.S. health leaders, public health professionals, tobacco use prevention specialists, educators, advocates, policy makers and—importantly—young people from around the Pacific Northwest attended the meeting. The goals of the event were to share information, tools, and best practices in tobacco use prevention, to enhance existing efforts and to devise new strategies to help youth and young people resist social and industry pressure to initiate tobacco use, or to end tobacco use if it has already begun."
        items = [
            [title: "Healthy Kids", url: "http://www.cdc.gov/features/flu/flu_456px.jpg", width: 345, height: 191, desc: "Healthy happy kids having a fun and healthy time."],
            [title: "Youth & Tobacco Town Hall", url: "http://farm9.staticflickr.com/8152/7415645094_e4f9827465_o.jpg", width: 3824, height: 2540, desc: desc],
            [title: "Youth & Tobacco Town Hall", url: "http://farm9.staticflickr.com/8005/7415645828_c8df619e8f_o.jpg", width: 3789, height: 2517, desc: desc],
            [title: "Youth & Tobacco Town Hall", url: "http://farm6.staticflickr.com/5160/7415662296_17ddb42151_o.jpg", width: 3123, height: 2074, desc: desc],
            [title: "Youth & Tobacco Town Hall", url: "http://farm9.staticflickr.com/8166/7415661744_9745bd9a51_o.jpg", width: 2453, height: 3693, desc: desc],
            [title: "Youth & Tobacco Town Hall", url: "http://farm8.staticflickr.com/7253/7415655194_3b0129291e_o.jpg", width: 4288, height: 2848, desc: desc],
            [title: "Youth & Tobacco Town Hall", url: "http://farm6.staticflickr.com/5445/7415656828_e59b76ecdf_o.jpg", width: 2564, height: 3860, desc: desc],
            [title: "Youth & Tobacco Town Hall", url: "http://farm6.staticflickr.com/5039/7415661070_1c3c36dec4_o.jpg", width: 3711, height: 2465, desc: desc],
            [title: "Youth & Tobacco Town Hall", url: "http://farm6.staticflickr.com/5115/7415659906_69cebdb0a9_o.jpg", width: 2626, height: 3954, desc: desc],
            [title: "Youth & Tobacco Town Hall", url: "http://farm9.staticflickr.com/8008/7415659216_e565ac25da_o.jpg", width: 3905, height: 2593, desc: desc],
            [title: "Youth & Tobacco Town Hall", url: "http://farm9.staticflickr.com/8008/7420010158_c2a9f0e1fd_o.jpg", width: 2589, height: 1720, desc: desc],
            [title: "Youth & Tobacco Town Hall", url: "http://farm8.staticflickr.com/7259/7415647408_23e02e24e2_o.jpg", width: 3809, height: 2530, desc: desc],
            [title: "Youth & Tobacco Town Hall", url: "http://farm8.staticflickr.com/7269/7415651398_ed438c3765_o.jpg", width: 2573, height: 3874, desc: desc],
            [title: "Youth & Tobacco Town Hall", url: "http://farm8.staticflickr.com/7247/7415641940_ebe7061ddd_o.jpg", width: 4288, height: 2848, desc: desc],
            [title: "Youth & Tobacco Town Hall", url: "http://farm8.staticflickr.com/7275/7415648752_8a59b311af_o.jpg", width: 3451, height: 2292, desc: desc],
            [title: "Youth & Tobacco Town Hall", url: "http://farm6.staticflickr.com/5116/7415649474_f0d229bacd_o.jpg", width: 2574, height: 3876, desc: desc],
            [title: "Youth & Tobacco Town Hall", url: "http://farm6.staticflickr.com/5444/7415649912_d68112f44f_o.jpg", width: 3246, height: 2156, desc: desc],
            [title: "Youth & Tobacco Town Hall", url: "http://farm8.staticflickr.com/7125/7415647976_bba632768e_o.jpg", width: 2722, height: 1808, desc: desc],
            [title: "Youth & Tobacco Town Hall", url: "http://farm8.staticflickr.com/7139/7420009470_1cebff7bc8_o.jpg", width: 2832, height: 1881, desc: desc],
            [title: "Youth & Tobacco Town Hall", url: "http://farm9.staticflickr.com/8013/7420008656_10a8101885_o.jpg", width: 2449, height: 1627, desc: desc],
            [title: "Youth & Tobacco Town Hall", url: "http://farm9.staticflickr.com/8004/7420007630_9841968c8b_o.jpg", width: 2334, height: 1550, desc: desc],
            [title: "Youth & Tobacco Town Hall", url: "http://farm8.staticflickr.com/7262/7420005844_229b3c6873_o.jpg", width: 2778, height: 1845, desc: desc],
            [title: "Youth & Tobacco Town Hall", url: "http://farm9.staticflickr.com/8150/7415637182_aefc6320d6_o.jpg", width: 3719, height: 2470, desc: desc],
            [title: "Youth & Tobacco Town Hall", url: "http://farm9.staticflickr.com/8166/7415635114_d5b25dc707_o.jpg", width: 2286, height: 3442, desc: desc]
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

        assert youtubeService.importYoutubeVideo("http://www.youtube.com/watch?v=6yZkQqx1lag")
        assert youtubeService.importYoutubeVideo("http://www.youtube.com/watch?v=-VI_A1TPS6o&feature=c4-overview-vl&list=PLrl7E8KABz1GZv0fAUSb7ZNXCdTZJSrzN")
        assert youtubeService.importYoutubeVideo("http://www.youtube.com/watch?v=Jn9OBSNBE4M&feature=c4-overview-vl&list=PLrl7E8KABz1GZv0fAUSb7ZNXCdTZJSrzN")
        assert youtubeService.importYoutubeVideo("http://www.youtube.com/watch?v=R-oxvOTLCYU&feature=c4-overview-vl&list=PLrl7E8KABz1GZv0fAUSb7ZNXCdTZJSrzN")
        assert youtubeService.importYoutubeVideo("http://www.youtube.com/watch?v=6YWEW3I1fKs&feature=c4-overview-vl&list=PLrl7E8KABz1GZv0fAUSb7ZNXCdTZJSrzN")
        assert youtubeService.importYoutubeVideo("http://www.youtube.com/watch?v=4Zuo88K2Gdc&feature=c4-overview-vl&list=PLrl7E8KABz1GOyx_HDSqmwfrq8j_THXgg")
        assert youtubeService.importYoutubeVideo("http://www.youtube.com/watch?v=vXRYmVUOD7g&feature=c4-overview-vl&list=PLrl7E8KABz1GOyx_HDSqmwfrq8j_THXgg")
        assert youtubeService.importYoutubeVideo("http://www.youtube.com/watch?v=10gcsrx-ANA&feature=c4-overview-vl&list=PLrl7E8KABz1GOyx_HDSqmwfrq8j_THXgg")
        assert youtubeService.importYoutubeVideo("http://www.youtube.com/watch?v=xLnA-ZdKuEY&feature=c4-overview-vl&list=PLrl7E8KABz1GOyx_HDSqmwfrq8j_THXgg")
        assert youtubeService.importYoutubeVideo("http://www.youtube.com/watch?v=zMdFj4e0Q18&feature=c4-overview-vl&list=PLrl7E8KABz1GOyx_HDSqmwfrq8j_THXgg")
        assert youtubeService.importYoutubeVideo("http://www.youtube.com/watch?v=QW1yodZJpG8&list=TLUZ7PoLn_4kvubarfg2JjJSlFT8Lf1dBR")

        //Real Cost videos
        assert youtubeService.importYoutubeVideo("http://www.youtube.com/watch?v=zhbXENhrkTA", english, fda)
        assert youtubeService.importYoutubeVideo("http://www.youtube.com/watch?v=asarKLMCvdo", english, fda)
        assert youtubeService.importYoutubeVideo("http://www.youtube.com/watch?v=a_M9tTwLb9A", english, fda)
        assert youtubeService.importYoutubeVideo("http://www.youtube.com/watch?v=mcteRv08Aco", english, fda)

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

        (ran.nextInt(10) + 5).times {
            randomCollection.addToMediaItems(allItems[ran.nextInt(allItems.size())])
        }

        println "${MediaItem.where { eq 'class', 'com.ctacorp.syndication.Collection' }.count()} Collections created."

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
        oneOff.save(flush:true)
        assert oneOff.id
        oneOff.addToMediaItems(Html.list()[2])
        oneOff.addToMediaItems(Video.list()[2])
        oneOff.addToMediaItems(Image.list()[2])

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
            ran.nextInt(10).times {
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

    def seedSocialMedia() {
        def smediaTypes = ["facebook", "twitter", "googlePlus", "pinterest"]

        def batch = []

        smediaTypes.each {
            def lang = Language.first()
            def source = Source.last()
            batch << new SocialMedia(name: "${it}",
                description: "${it}",
                socialMediaType: "${it}",
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

        batchSaver(SocialMedia, batch)
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

    def seedWidgets() {
        def widgets = ["Blogger",
            "Template",
            "Dialog",
            "Alert",
            "Comments",
            "CSS",
            "IPHONE"]

        def batch = []

        widgets.each {
            def lang = Language.first()
            def source = Source.last()
            batch << new Widget(name: "${it}",
                description: "${it}",
                width: 10,
                height: 10 + it.size(),
                code: 99,
                sourceUrl: "http://localhost/Syndication/socialMedia/${it}",
                dateSyndicationCaptured: new Date(),
                dateSyndicationUpdated: new Date(),
                language: lang,
                externalGuid: "http://localhost/Syndication/socialMedia/${it}".toString().hashCode(),
                source: source)
        }

        batchSaver(Widget, batch)
    }

    def seedUsers() {
        def userRole = Role.findOrSaveByAuthority("ROLE_USER")
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
        "${names[ran.nextInt(names.size())]} ${names[ran.nextInt(names.size())]}"
    }

    String randomUsername() {
        "${names[ran.nextInt(names.size())].toLowerCase()}${names[ran.nextInt(names.size())].capitalize()}@example.com"
    }
}
