/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */
package com.ctacorp.syndication

import com.ctacorp.grails.swagger.annotations.*
import grails.util.Environment

@Model(id = "Language", properties = [
    @ModelProperty(propertyName = "id",         attributes = [@PropertyAttribute(type = "integer", format = "int64",     required = true)]),
    @ModelProperty(propertyName = "name",       attributes = [@PropertyAttribute(type = "string",   required = true)]),
    @ModelProperty(propertyName = "isoCode",    attributes = [@PropertyAttribute(type = "string",   required = true)]),
    @ModelProperty(propertyName = "isActive",   attributes = [@PropertyAttribute(type = "boolean",  required = true)])
])
class Language {
    String name                 // Display Name
    String isoCode              // ISO 639-2 value
    boolean isActive = false    // is this an active language

    static constraints = {
        name nullable: false, blank: false, unique: true
        isoCode nullable: false, blank: false, unique: true, minSize: 3, maxSize: 3
    }

    String toString() { name }

    static marshalling = {
        tagList{
            shouldOutputIdentifier true
            shouldOutputClass false
        }
    }

    def beforeInsert() {
        name = name.capitalize()
        isoCode = isoCode.toLowerCase()
    }

    def beforeUpdate() {
        name = name.capitalize()
        isoCode = isoCode.toLowerCase()
    }

    static namedQueries = {
        listActive{ String sortQuery = "id" ->
            eq "isActive", true
            multiSort(sortQuery)
        }

        //Sort Order -------------------------------------------------------------------
        multiSort{ String sortQuery = "id" ->
            def parts = sortQuery.split(",")
            parts.each{ part ->
                if(part.startsWith("-")){
                    String field = part[1..-1]
                    order(field, 'desc')
                } else{
                    order(part, 'asc')
                }
            }
        }
    }

    static public seedDatabaseWithLanguages() {
        for (language in languageMap) {
            this.findOrSaveWhere(isoCode:language.key, name:language.value, isActive:false).save(flush: true)
        }
    }

    static final private transient Map languageMap = [
            'eng':'English',
            'aar':'Afar',
            'abk':'Abkhazian',
            'ace':'Achinese',
            'ach':'Acoli',
            'ada':'Adangme',
            'ady':'Adyghe; Adygei',
            'afa':'Afro-Asiatic languages',
            'afh':'Afrihili',
            'afr':'Afrikaans',
            'ain':'Ainu',
            'aka':'Akan',
            'akk':'Akkadian',
            'alb':'Albanian',
            'ale':'Aleut',
            'alg':'Algonquian languages',
            'alt':'Southern Altai',
            'amh':'Amharic',
            'ang':'English, Old (ca.450-1100)',
            'anp':'Angika',
            'apa':'Apache languages',
            'ara':'Arabic',
            'arc':'Official Aramaic (700-300 BCE); Imperial Aramaic (700-300 BCE)',
            'arg':'Aragonese',
            'arm':'Armenian',
            'arn':'Mapudungun; Mapuche',
            'arp':'Arapaho',
            'art':'Artificial languages',
            'arw':'Arawak',
            'asm':'Assamese',
            'ast':'Asturian; Bable; Leonese; Asturleonese',
            'ath':'Athapascan languages',
            'aus':'Australian languages',
            'ava':'Avaric',
            'ave':'Avestan',
            'awa':'Awadhi',
            'aym':'Aymara',
            'aze':'Azerbaijani',
            'bad':'Banda languages',
            'bai':'Bamileke languages',
            'bak':'Bashkir',
            'bal':'Baluchi',
            'bam':'Bambara',
            'ban':'Balinese',
            'baq':'Basque',
            'bas':'Basa',
            'bat':'Baltic languages',
            'bej':'Beja; Bedawiyet',
            'bel':'Belarusian',
            'bem':'Bemba',
            'ben':'Bengali',
            'ber':'Berber languages',
            'bho':'Bhojpuri',
            'bih':'Bihari languages',
            'bik':'Bikol',
            'bin':'Bini; Edo',
            'bis':'Bislama',
            'bla':'Siksika',
            'bnt':'Bantu (Other)',
            'bos':'Bosnian',
            'bra':'Braj',
            'bre':'Breton',
            'btk':'Batak languages',
            'bua':'Buriat',
            'bug':'Buginese',
            'bul':'Bulgarian',
            'bur':'Burmese',
            'byn':'Blin; Bilin',
            'cad':'Caddo',
            'cai':'Central American Indian languages',
            'car':'Galibi Carib',
            'cat':'Catalan; Valencian',
            'cau':'Caucasian languages',
            'ceb':'Cebuano',
            'cel':'Celtic languages',
            'cha':'Chamorro',
            'chb':'Chibcha',
            'che':'Chechen',
            'chg':'Chagatai',
            'chi':'Chinese',
            'chk':'Chuukese',
            'chm':'Mari',
            'chn':'Chinook jargon',
            'cho':'Choctaw',
            'chp':'Chipewyan; Dene Suline',
            'chr':'Cherokee',
            'chu':'Church Slavic; Old Slavonic; Church Slavonic; Old Bulgarian; Old Church Slavonic',
            'chv':'Chuvash',
            'chy':'Cheyenne',
            'cmc':'Chamic languages',
            'cop':'Coptic',
            'cor':'Cornish',
            'cos':'Corsican',
            'cpe':'Creoles and pidgins, English based',
            'cpf':'Creoles and pidgins, French-based ',
            'cpp':'Creoles and pidgins, Portuguese-based ',
            'cre':'Cree',
            'crh':'Crimean Tatar; Crimean Turkish',
            'crp':'Creoles and pidgins ',
            'csb':'Kashubian',
            'cus':'Cushitic languages',
            'cze':'Czech',
            'dak':'Dakota',
            'dan':'Danish',
            'dar':'Dargwa',
            'day':'Land Dayak languages',
            'del':'Delaware',
            'den':'Slave (Athapascan)',
            'dgr':'Dogrib',
            'din':'Dinka',
            'div':'Divehi; Dhivehi; Maldivian',
            'doi':'Dogri',
            'dra':'Dravidian languages',
            'dsb':'Lower Sorbian',
            'dua':'Duala',
            'dum':'Dutch, Middle (ca.1050-1350)',
            'dut':'Dutch; Flemish',
            'dyu':'Dyula',
            'dzo':'Dzongkha',
            'efi':'Efik',
            'egy':'Egyptian (Ancient)',
            'eka':'Ekajuk',
            'elx':'Elamite',
            'enm':'English, Middle (1100-1500)',
            'epo':'Esperanto',
            'est':'Estonian',
            'ewe':'Ewe',
            'ewo':'Ewondo',
            'fan':'Fang',
            'fao':'Faroese',
            'fat':'Fanti',
            'fij':'Fijian',
            'fil':'Filipino; Pilipino',
            'fin':'Finnish',
            'fiu':'Finno-Ugrian languages',
            'fon':'Fon',
            'fre':'French',
            'frm':'French, Middle (ca.1400-1600)',
            'fro':'French, Old (842-ca.1400)',
            'frr':'Northern Frisian',
            'frs':'Eastern Frisian',
            'fry':'Western Frisian',
            'ful':'Fulah',
            'fur':'Friulian',
            'gaa':'Ga',
            'gay':'Gayo',
            'gba':'Gbaya',
            'gem':'Germanic languages',
            'geo':'Georgian',
            'ger':'German',
            'gez':'Geez',
            'gil':'Gilbertese',
            'gla':'Gaelic; Scottish Gaelic',
            'gle':'Irish',
            'glg':'Galician',
            'glv':'Manx',
            'gmh':'German, Middle High (ca.1050-1500)',
            'goh':'German, Old High (ca.750-1050)',
            'gon':'Gondi',
            'gor':'Gorontalo',
            'got':'Gothic',
            'grb':'Grebo',
            'grc':'Greek, Ancient (to 1453)',
            'gre':'Greek, Modern (1453-)',
            'grn':'Guarani',
            'gsw':'Swiss German; Alemannic; Alsatian',
            'guj':'Gujarati',
            'gwi':'Gwich in',
            'hai':'Haida',
            'hat':'Haitian; Haitian Creole',
            'hau':'Hausa',
            'haw':'Hawaiian',
            'heb':'Hebrew',
            'her':'Herero',
            'hil':'Hiligaynon',
            'him':'Himachali languages; Western Pahari languages',
            'hin':'Hindi',
            'hit':'Hittite',
            'hmn':'Hmong; Mong',
            'hmo':'Hiri Motu',
            'hrv':'Croatian',
            'hsb':'Upper Sorbian',
            'hun':'Hungarian',
            'hup':'Hupa',
            'iba':'Iban',
            'ibo':'Igbo',
            'ice':'Icelandic',
            'ido':'Ido',
            'iii':'Sichuan Yi; Nuosu',
            'ijo':'Ijo languages',
            'iku':'Inuktitut',
            'ile':'Interlingue; Occidental',
            'ilo':'Iloko',
            'ina':'Interlingua (International Auxiliary Language Association)',
            'inc':'Indic languages',
            'ind':'Indonesian',
            'ine':'Indo-European languages',
            'inh':'Ingush',
            'ipk':'Inupiaq',
            'ira':'Iranian languages',
            'iro':'Iroquoian languages',
            'ita':'Italian',
            'jav':'Javanese',
            'jbo':'Lojban',
            'jpn':'Japanese',
            'jpr':'Judeo-Persian',
            'jrb':'Judeo-Arabic',
            'kaa':'Kara-Kalpak',
            'kab':'Kabyle',
            'kac':'Kachin; Jingpho',
            'kal':'Kalaallisut; Greenlandic',
            'kam':'Kamba',
            'kan':'Kannada',
            'kar':'Karen languages',
            'kas':'Kashmiri',
            'kau':'Kanuri',
            'kaw':'Kawi',
            'kaz':'Kazakh',
            'kbd':'Kabardian',
            'kha':'Khasi',
            'khi':'Khoisan languages',
            'khm':'Central Khmer',
            'kho':'Khotanese; Sakan',
            'kik':'Kikuyu; Gikuyu',
            'kin':'Kinyarwanda',
            'kir':'Kirghiz; Kyrgyz',
            'kmb':'Kimbundu',
            'kok':'Konkani',
            'kom':'Komi',
            'kon':'Kongo',
            'kor':'Korean',
            'kos':'Kosraean',
            'kpe':'Kpelle',
            'krc':'Karachay-Balkar',
            'krl':'Karelian',
            'kro':'Kru languages',
            'kru':'Kurukh',
            'kua':'Kuanyama; Kwanyama',
            'kum':'Kumyk',
            'kur':'Kurdish',
            'kut':'Kutenai',
            'lad':'Ladino',
            'lah':'Lahnda',
            'lam':'Lamba',
            'lao':'Lao',
            'lat':'Latin',
            'lav':'Latvian',
            'lez':'Lezghian',
            'lim':'Limburgan; Limburger; Limburgish',
            'lin':'Lingala',
            'lit':'Lithuanian',
            'lol':'Mongo',
            'loz':'Lozi',
            'ltz':'Luxembourgish; Letzeburgesch',
            'lua':'Luba-Lulua',
            'lub':'Luba-Katanga',
            'lug':'Ganda',
            'lui':'Luiseno',
            'lun':'Lunda',
            'luo':'Luo (Kenya and Tanzania)',
            'lus':'Lushai',
            'mac':'Macedonian',
            'mad':'Madurese',
            'mag':'Magahi',
            'mah':'Marshallese',
            'mai':'Maithili',
            'mak':'Makasar',
            'mal':'Malayalam',
            'man':'Mandingo',
            'mao':'Maori',
            'map':'Austronesian languages',
            'mar':'Marathi',
            'mas':'Masai',
            'may':'Malay',
            'mdf':'Moksha',
            'mdr':'Mandar',
            'men':'Mende',
            'mga':'Irish, Middle (900-1200)',
            'mic':'Mi kmaq; Micmac',
            'min':'Minangkabau',
            'mis':'Uncoded languages',
            'mkh':'Mon-Khmer languages',
            'mlg':'Malagasy',
            'mlt':'Maltese',
            'mnc':'Manchu',
            'mni':'Manipuri',
            'mno':'Manobo languages',
            'moh':'Mohawk',
            'mon':'Mongolian',
            'mos':'Mossi',
            'mul':'Multiple languages',
            'mun':'Munda languages',
            'mus':'Creek',
            'mwl':'Mirandese',
            'mwr':'Marwari',
            'myn':'Mayan languages',
            'myv':'Erzya',
            'nah':'Nahuatl languages',
            'nai':'North American Indian languages',
            'nap':'Neapolitan',
            'nau':'Nauru',
            'nav':'Navajo; Navaho',
            'nbl':'Ndebele, South; South Ndebele',
            'nde':'Ndebele, North; North Ndebele',
            'ndo':'Ndonga',
            'nds':'Low German; Low Saxon; German, Low; Saxon, Low',
            'nep':'Nepali',
            'new':'Nepal Bhasa; Newari',
            'nia':'Nias',
            'nic':'Niger-Kordofanian languages',
            'niu':'Niuean',
            'nno':'Norwegian Nynorsk; Nynorsk, Norwegian',
            'nob':'BokmÃ¥l, Norwegian; Norwegian BokmÃ¥l',
            'nog':'Nogai',
            'non':'Norse, Old',
            'nor':'Norwegian',
            'nqo':'N Ko',
            'nso':'Pedi; Sepedi; Northern Sotho',
            'nub':'Nubian languages',
            'nwc':'Classical Newari; Old Newari; Classical Nepal Bhasa',
            'nya':'Chichewa; Chewa; Nyanja',
            'nym':'Nyamwezi',
            'nyn':'Nyankole',
            'nyo':'Nyoro',
            'nzi':'Nzima',
            'oci':'Occitan (post 1500); ProvenÃ§al',
            'oji':'Ojibwa',
            'ori':'Oriya',
            'orm':'Oromo',
            'osa':'Osage',
            'oss':'Ossetian; Ossetic',
            'ota':'Turkish, Ottoman (1500-1928)',
            'oto':'Otomian languages',
            'paa':'Papuan languages',
            'pag':'Pangasinan',
            'pal':'Pahlavi',
            'pam':'Pampanga; Kapampangan',
            'pan':'Panjabi; Punjabi',
            'pap':'Papiamento',
            'pau':'Palauan',
            'peo':'Persian, Old (ca.600-400 B.C.)',
            'per':'Persian',
            'phi':'Philippine languages',
            'phn':'Phoenician',
            'pli':'Pali',
            'pol':'Polish',
            'pon':'Pohnpeian',
            'por':'Portuguese',
            'pra':'Prakrit languages',
            'pro':'ProvenÃ§al, Old (to 1500)',
            'pus':'Pushto; Pashto',
            'qaa-qtz':'Reserved for local use',
            'que':'Quechua',
            'raj':'Rajasthani',
            'rap':'Rapanui',
            'rar':'Rarotongan; Cook Islands Maori',
            'roa':'Romance languages',
            'roh':'Romansh',
            'rom':'Romany',
            'rum':'Romanian; Moldavian; Moldovan',
            'run':'Rundi',
            'rup':'Aromanian; Arumanian; Macedo-Romanian',
            'rus':'Russian',
            'sad':'Sandawe',
            'sag':'Sango',
            'sah':'Yakut',
            'sai':'South American Indian (Other)',
            'sal':'Salishan languages',
            'sam':'Samaritan Aramaic',
            'san':'Sanskrit',
            'sas':'Sasak',
            'sat':'Santali',
            'scn':'Sicilian',
            'sco':'Scots',
            'sel':'Selkup',
            'sem':'Semitic languages',
            'sga':'Irish, Old (to 900)',
            'sgn':'Sign Languages',
            'shn':'Shan',
            'sid':'Sidamo',
            'sin':'Sinhala; Sinhalese',
            'sio':'Siouan languages',
            'sit':'Sino-Tibetan languages',
            'sla':'Slavic languages',
            'slo':'Slovak',
            'slv':'Slovenian',
            'sma':'Southern Sami',
            'sme':'Northern Sami',
            'smi':'Sami languages',
            'smj':'Lule Sami',
            'smn':'Inari Sami',
            'smo':'Samoan',
            'sms':'Skolt Sami',
            'sna':'Shona',
            'snd':'Sindhi',
            'snk':'Soninke',
            'sog':'Sogdian',
            'som':'Somali',
            'son':'Songhai languages',
            'sot':'Sotho, Southern',
            'spa':'Spanish',
            'srd':'Sardinian',
            'srn':'Sranan Tongo',
            'srp':'Serbian',
            'srr':'Serer',
            'ssa':'Nilo-Saharan languages',
            'ssw':'Swati',
            'suk':'Sukuma',
            'sun':'Sundanese',
            'sus':'Susu',
            'sux':'Sumerian',
            'swa':'Swahili',
            'swe':'Swedish',
            'syc':'Classical Syriac',
            'syr':'Syriac',
            'tah':'Tahitian',
            'tai':'Tai languages',
            'tam':'Tamil',
            'tat':'Tatar',
            'tel':'Telugu',
            'tem':'Timne',
            'ter':'Tereno',
            'tet':'Tetum',
            'tgk':'Tajik',
            'tgl':'Tagalog',
            'tha':'Thai',
            'tib':'Tibetan',
            'tig':'Tigre',
            'tir':'Tigrinya',
            'tiv':'Tiv',
            'tkl':'Tokelau',
            'tlh':'Klingon; tlhIngan-Hol',
            'tli':'Tlingit',
            'tmh':'Tamashek',
            'tog':'Tonga (Nyasa)',
            'ton':'Tonga (Tonga Islands)',
            'tpi':'Tok Pisin',
            'tsi':'Tsimshian',
            'tsn':'Tswana',
            'tso':'Tsonga',
            'tuk':'Turkmen',
            'tum':'Tumbuka',
            'tup':'Tupi languages',
            'tur':'Turkish',
            'tut':'Altaic languages',
            'tvl':'Tuvalu',
            'twi':'Twi',
            'tyv':'Tuvinian',
            'udm':'Udmurt',
            'uga':'Ugaritic',
            'uig':'Uighur; Uyghur',
            'ukr':'Ukrainian',
            'umb':'Umbundu',
            'und':'Undetermined',
            'urd':'Urdu',
            'uzb':'Uzbek',
            'vai':'Vai',
            'ven':'Venda',
            'vie':'Vietnamese',
            'vol':'VolapÃ¼k',
            'vot':'Votic',
            'wak':'Wakashan languages',
            'wal':'Walamo',
            'war':'Waray',
            'was':'Washo',
            'wel':'Welsh',
            'wen':'Sorbian languages',
            'wln':'Walloon',
            'wol':'Wolof',
            'xal':'Kalmyk; Oirat',
            'xho':'Xhosa',
            'yao':'Yao',
            'yap':'Yapese',
            'yid':'Yiddish',
            'yor':'Yoruba',
            'ypk':'Yupik languages',
            'zap':'Zapotec',
            'zbl':'Blissymbols; Blissymbolics; Bliss',
            'zen':'Zenaga',
            'zgh':'Standard Moroccan Tamazight',
            'zha':'Zhuang; Chuang',
            'znd':'Zande languages',
            'zul':'Zulu',
            'zun':'Zuni',
            'zxx':'No linguistic content; Not applicable',
            'zza':'Zaza; Dimili; Dimli; Kirdki; Kirmanjki; Zazaki'
    ]
}
