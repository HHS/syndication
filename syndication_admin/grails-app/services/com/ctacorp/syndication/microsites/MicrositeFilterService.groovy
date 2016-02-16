package com.ctacorp.syndication.microsites

import com.ctacorp.syndication.authentication.User
import com.ctacorp.syndication.contact.EmailContact
import com.ctacorp.syndication.health.HealthReport
import com.ctacorp.syndication.microsite.FlaggedMicrosite
import com.ctacorp.syndication.microsite.MediaSelector
import com.ctacorp.syndication.microsite.MicroSite
import grails.gsp.PageRenderer
import grails.transaction.NotTransactional
import java.util.regex.Pattern
import java.util.regex.Matcher
import grails.transaction.Transactional

@Transactional
class MicrositeFilterService {

    def mailService
    PageRenderer groovyPageRenderer
    def grailsApplication

    def scanAllMicrosites() {
            MicroSite.list().each { site ->
                performValidation(site)
        }
    }

    def flagMicrosite(MicroSite microsite) {
        FlaggedMicrosite fm = FlaggedMicrosite.findByMicrosite(microsite)
        if(!fm) {
            fm = new FlaggedMicrosite()
            sendEmailNotification(microsite)
        }

        fm.microsite = microsite
        fm.message = "Contains questionable language."
        fm.save(flush: true)
    }

    def removeFlag(MicroSite site) {
        FlaggedMicrosite fm = FlaggedMicrosite.findByMicrosite(site)
        fm.delete()
    }

    def rescanItem(Long mediaId){
        boolean flag = performValidation(MicroSite.get(mediaId))
        if(!flag){
            removeFlag(MicroSite.get(mediaId))
        }
    }

    def performValidation(site) {
        List profanityList = getProfanityMap()
        boolean flag = false
        profanityList.each { word ->
                def pattern = ~/(^$word(\\.|\s+))|(\s+$word(\\.|\s+))|(\s+$word(\\.|$))|(^$word(\\.|$))/
                def matcher =  pattern.matcher(site.title)

            if(pattern.matcher(site.title) ||
                    pattern.matcher(site.logoUrl.toString()) ||
                    pattern.matcher(site.footerText) ||
                    pattern.matcher(site.footerLink1.toString()) ||
                    pattern.matcher(site.footerLink2.toString()) ||
                    pattern.matcher(site.footerLink3.toString()) ||
                    pattern.matcher(site.footerLink4.toString()) ||
                    pattern.matcher(site.footerLinkName1) ||
                    pattern.matcher(site.footerLinkName2) ||
                    pattern.matcher(site.footerLinkName3) ||
                    pattern.matcher(site.footerLinkName4) ||
                    pattern.matcher(site?.mediaArea1?.header ?: "") ||
                    pattern.matcher(site?.mediaArea1?.description ?: "")||
                    pattern.matcher(site?.mediaArea2?.header ?: "")||
                    pattern.matcher(site?.mediaArea2?.description ?: "")||
                    pattern.matcher(site?.mediaArea3?.header ?: "")||
                    pattern.matcher(site?.mediaArea3?.description ?: ""))
                        {

                flag = true
            }
        }
        if(flag){
            flagMicrosite(site)
        }
        return flag
    }

    def sendEmailNotification(microsite) {
        def mailRecipiants = EmailContact.list()?.email ?: "syndication@ctacorp.com"

        mailService.sendMail {
            multipart true
            async true
            to mailRecipiants
            subject "Microsite registration request"
            html groovyPageRenderer.render(template: '/micrositeFilter/micrositeFlagEmail', model:[userInstance: microsite.user, microsite:microsite, serverUrl:grailsApplication.config.grails.serverURL])
        }
    }





















    ArrayList getProfanityMap() {
        [
                "4r5e",
                "5h1t",
                "5hit",
                "a55",
                "anal",
                "anus",
                "ar5e",
                "arrse",
                "arse",
                "ass",
                "ass-fucker",
                "asses",
                "assfucker",
                "assfukka",
                "asshole",
                "assholes",
                "asswhole",
                "a_s_s",
                "b!tch",
                "b00bs",
                "b17ch",
                "b1tch",
                "ballbag",
                "balls",
                "ballsack",
                "bastard",
                "beastial",
                "beastiality",
                "bellend",
                "bestial",
                "bestiality",
                "bi+ch",
                "biatch",
                "bitch",
                "bitcher",
                "bitchers",
                "bitches",
                "bitchin",
                "bitching",
                "bloody",
                "blow job",
                "blowjob",
                "blowjobs",
                "boiolas",
                "bollock",
                "bollok",
                "boner",
                "boob",
                "boobs",
                "booobs",
                "boooobs",
                "booooobs",
                "booooooobs",
                "breasts",
                "buceta",
                "bugger",
                "bum",
                "bunny fucker",
                "butt",
                "butthole",
                "buttmuch",
                "buttplug",
                "c0ck",
                "c0cksucker",
                "carpet muncher",
                "cawk",
                "chink",
                "cipa",
                "cl1t",
                "clit",
                "clitoris",
                "clits",
                "cnut",
                "cock",
                "cock-sucker",
                "cockface",
                "cockhead",
                "cockmunch",
                "cockmuncher",
                "cocks",
                "cocksuck",
                "cocksucked",
                "cocksucker",
                "cocksucking",
                "cocksucks",
                "cocksuka",
                "cocksukka",
                "cok",
                "cokmuncher",
                "coksucka",
                "coon",
                "cox",
                "crap",
                "cum",
                "cummer",
                "cumming",
                "cums",
                "cumshot",
                "cunilingus",
                "cunillingus",
                "cunnilingus",
                "cunt",
                "cuntlick",
                "cuntlicker",
                "cuntlicking",
                "cunts",
                "cyalis",
                "cyberfuc",
                "cyberfuck",
                "cyberfucked",
                "cyberfucker",
                "cyberfuckers",
                "cyberfucking",
                "d1ck",
                "damn",
                "dick",
                "dickhead",
                "dildo",
                "dildos",
                "dink",
                "dinks",
                "dirsa",
                "dlck",
                "dog-fucker",
                "doggin",
                "dogging",
                "donkeyribber",
                "doosh",
                "duche",
                "dyke",
                "ejaculate",
                "ejaculated",
                "ejaculates",
                "ejaculating",
                "ejaculatings",
                "ejaculation",
                "ejakulate",
                "f u c k",
                "f u c k e r",
                "f4nny",
                "fag",
                "fagging",
                "faggitt",
                "faggot",
                "faggs",
                "fagot",
                "fagots",
                "fags",
                "fanny",
                "fannyflaps",
                "fannyfucker",
                "fanyy",
                "fatass",
                "fcuk",
                "fcuker",
                "fcuking",
                "feck",
                "fecker",
                "felching",
                "fellate",
                "fellatio",
                "fingerfuck",
                "fingerfucked",
                "fingerfucker",
                "fingerfuckers",
                "fingerfucking",
                "fingerfucks",
                "fistfuck",
                "fistfucked",
                "fistfucker",
                "fistfuckers",
                "fistfucking",
                "fistfuckings",
                "fistfucks",
                "flange",
                "fook",
                "fooker",
                "fuck",
                "fucka",
                "fucked",
                "fucker",
                "fuckers",
                "fuckhead",
                "fuckheads",
                "fuckin",
                "fucking",
                "fuckings",
                "fuckingshitmotherfucker",
                "fuckme",
                "fucks",
                "fuckwhit",
                "fuckwit",
                "fudge packer",
                "fudgepacker",
                "fuk",
                "fuker",
                "fukker",
                "fukkin",
                "fuks",
                "fukwhit",
                "fukwit",
                "fux",
                "fux0r",
                "f_u_c_k",
                "gangbang",
                "gangbanged",
                "gangbangs",
                "gaylord",
                "gaysex",
                "goatse",
                "God",
                "god-dam",
                "god-damned",
                "goddamn",
                "goddamned",
                "hardcoresex",
                "hell",
                "heshe",
                "hoar",
                "hoare",
                "hoer",
                "homo",
                "hore",
                "horniest",
                "horny",
                "hotsex",
                "jack-off",
                "jackoff",
                "jap",
                "jerk-off",
                "jism",
                "jiz",
                "jizm",
                "jizz",
                "kawk",
                "knob",
                "knobead",
                "knobed",
                "knobend",
                "knobhead",
                "knobjocky",
                "knobjokey",
                "kock",
                "kondum",
                "kondums",
                "kum",
                "kummer",
                "kumming",
                "kums",
                "kunilingus",
                "l3i+ch",
                "l3itch",
                "labia",
                "lmfao",
                "lust",
                "lusting",
                "m0f0",
                "m0fo",
                "m45terbate",
                "ma5terb8",
                "ma5terbate",
                "masochist",
                "master-bate",
                "masterb8",
                "masterbat*",
                "masterbat3",
                "masterbate",
                "masterbation",
                "masterbations",
                "masturbate",
                "mo-fo",
                "mof0",
                "mofo",
                "mothafuck",
                "mothafucka",
                "mothafuckas",
                "mothafuckaz",
                "mothafucked",
                "mothafucker",
                "mothafuckers",
                "mothafuckin",
                "mothafucking",
                "mothafuckings",
                "mothafucks",
                "mother fucker",
                "motherfuck",
                "motherfucked",
                "motherfucker",
                "motherfuckers",
                "motherfuckin",
                "motherfucking",
                "motherfuckings",
                "motherfuckka",
                "motherfucks",
                "muff",
                "mutha",
                "muthafecker",
                "muthafuckker",
                "muther",
                "mutherfucker",
                "n1gga",
                "n1gger",
                "nazi",
                "nigg3r",
                "nigg4h",
                "nigga",
                "niggah",
                "niggas",
                "niggaz",
                "nigger",
                "niggers",
                "nob",
                "nob jokey",
                "nobhead",
                "nobjocky",
                "nobjokey",
                "numbnuts",
                "nutsack",
                "orgasim",
                "orgasims",
                "orgasm",
                "orgasms",
                "p0rn",
                "pawn",
                "pecker",
                "penis",
                "penisfucker",
                "phonesex",
                "phuck",
                "phuk",
                "phuked",
                "phuking",
                "phukked",
                "phukking",
                "phuks",
                "phuq",
                "pigfucker",
                "pimpis",
                "piss",
                "pissed",
                "pisser",
                "pissers",
                "pisses",
                "pissflaps",
                "pissin",
                "pissing",
                "pissoff",
                "poop",
                "porn",
                "porno",
                "pornography",
                "pornos",
                "prick",
                "pricks",
                "pron",
                "pube",
                "pusse",
                "pussi",
                "pussies",
                "pussy",
                "pussys",
                "rectum",
                "retard",
                "rimjaw",
                "rimming",
                "s hit",
                "s.o.b.",
                "sadist",
                "schlong",
                "screwing",
                "scroat",
                "scrote",
                "scrotum",
                "semen",
                "sex",
                "sh!+",
                "sh!t",
                "sh1t",
                "shag",
                "shagger",
                "shaggin",
                "shagging",
                "shemale",
                "shi+",
                "shit",
                "shitdick",
                "shite",
                "shited",
                "shitey",
                "shitfuck",
                "shitfull",
                "shithead",
                "shiting",
                "shitings",
                "shits",
                "shitted",
                "shitter",
                "shitters",
                "shitting",
                "shittings",
                "shitty",
                "skank",
                "slut",
                "sluts",
                "smegma",
                "smut",
                "snatch",
                "son-of-a-bitch",
                "spac",
                "spunk",
                "s_h_i_t",
                "t1tt1e5",
                "t1tties",
                "teets",
                "teez",
                "testical",
                "testicle",
                "tit",
                "titfuck",
                "tits",
                "titt",
                "tittie5",
                "tittiefucker",
                "titties",
                "tittyfuc",
                "tittywan",
                "titwank",
                "tosser",
                "turd",
                "tw4t",
                "twat",
                "twathead",
                "twatty",
                "twunt",
                "twunter",
                "v14gra",
                "v1gra",
                "vagina",
                "viagra",
                "vulva",
                "w00se",
                "wang",
                "wank",
                "wanker",
                "wanky",
                "whoar",
                "whore",
                "willies",
                "willy",
                "xrated",
                "xxx"
        ]
    }
}