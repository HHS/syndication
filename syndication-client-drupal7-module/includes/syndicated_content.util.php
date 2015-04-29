<?php
function _syndicated_content_init() {
    $sources = _syndicated_content_api_sources();
    $source = array();
    foreach($sources as $source_temp) {
        if(isset($source_temp['name'])) {
            $source = $source_temp;
            break;
        }
    }
    $ssl_auth = isset($source['ssl_auth']) ? $source['ssl_auth'] : 0;
    if(!defined('SYNDICATIONAPICLIENT_SSL_AUTH'))
        define('SYNDICATIONAPICLIENT_SSL_AUTH', $ssl_auth);
}
function _syndicated_content_api_sources()
{
    $results = db_select('syndicated_content_sources', 's')
                 ->fields('s')
                 ->range(0,1) /// temp: restrict to one source
                 ->execute();
    $sources = array();
    while( $source = $results->fetchAssoc() )
    {
        $sources[$source['id']] = $source;
    }
    return $sources;
}

/// ignores source_id for now
/// media types straight from api call
function _syndicated_content_media_types( $source_id=null )
{
    $synd = _syndicated_content_api_factory($source_id);
    $res = $synd->getMediaTypes();
    $media_types = array();
    if ( $res->success ) 
    {
        foreach ( $res->results as $mt )
        {
            $media_types[$mt['name']] = $mt['description'];
        }
    }
    return $media_types;
}

function _syndicated_content_source_orgs( $source_id=null )
{
    $synd = _syndicated_content_api_factory($source_id);
    $res = $synd->getSources();
    $org_sources = array();
    if ( $res->success ) 
    {
        foreach ( $res->results as $os )
        {
            $org_sources[$os['id']] = $os;
        }
    }
    return $org_sources;
}


function _syndicated_content_find_language_id( $language_code=null, $source_id=null )
{
    if ( empty($language_code) ) { $language_code == 'eng'; }
    $drup_languages = _syndicated_content_languages_iso_639_2();
    $synd = _syndicated_content_api_factory($source_id);
    $response = $synd->getLanguages();
    if ( $response->success )
    {
        foreach( $response->results as $lang )
        {
            if ( $lang['isoCode'] == $language_code )
            {
                return $lang['id'];
            }
        } 
    }
    return null;
}

/// ignores source_id for now
/// mapped types
function _syndicated_content_type_mapping( $source_id=null )
{
    $first_source = db_select('syndicated_content_sources','s')
                        ->fields('s')
                        ->range(0,1)
                        ->execute();
    while( $s = $first_source->fetchAssoc() )
    {
        $source_id = $s['id'];
    }
    $results = db_select('syndicated_content_types', 't')
                 ->fields('t')
                 ->condition('syndication_source_id',$source_id,'=')
                 ->execute();
    $types = array(
        'by_syndication_type' => array(),
        'by_drupal_type'      => array(),
        'list'                => array()
    );
    while( $type = $results->fetchAssoc() )
    {
        $types['list'][] = $type;
        if ( !isset($types['by_syndication_type'][$type['syndication_type']]) ) 
        {  
            $types['by_syndication_type'][$type['syndication_type']] = array(); 
        }
        if ( !isset($types['by_drupal_type'][$type['drupal_type']]) ) 
        {  
            $types['by_drupal_type'][$type['drupal_type']] = array(); 
        }
        $types['by_syndication_type'][$type['syndication_type']][] = $type['drupal_type'];
        $types['by_drupal_type'][$type['drupal_type']][]           = $type['syndication_type'];
        $types['by_drupal_type'][$type['drupal_type']][]           = $type['drupal_field'];
    }
    return $types;
}

/*
 *  _syndicated_content_known_types
 *  
 * Description: We have to know a base of content types and whether we need to find a specific custom field or not.
 *              This can probably get extended through the API, but we'll set this up quickly so there is a
 *              centralized location.
 * 
 * Parameters:
 *      $format      (string) - 'extended' will show the field requirements
 *
 */
function _syndicated_content_known_types($format = "") {
    $contentTypes = array(
        ''=>'',
        'Html'=>"",
        'Image'=>"custom",
        'Infographic'=>"custom",
        'Video'=>"custom"
    );
    
    if($format == "") {
        $ct = array();
        
        foreach($contentTypes as $key => $val) {
            $ct[$key] = $key;
        }
        $contentTypes = $ct;
    }
    
    return $contentTypes;
}

/// ignores source_id for now
function _syndicated_content_api_factory( $source_id=null )
{
    $sources = _syndicated_content_api_sources();
    $source  = array_pop($sources);
    return new Syndication($source);
}

/// use only while there is a single source
function _syndicated_content_get_source_id()
{
    $sources = db_select('syndicated_content_sources','s')
        ->fields('s')
        ->range(0,1)
        ->execute();
    $source = $sources->fetchAssoc();
    if ( $source )
    {
        return $source['id'];
    }
    return false;
} 

function _syndicated_content_get_source_org_id( $source_id=null)
{
    $sources = db_select('syndicated_content_sources','s')
        ->fields('s')
        ->range(0,1)
        ->execute();
    $source = $sources->fetchAssoc();
    if ( $source )
    {
        return $source['source_org_id'];
    }
    return false;
}

function _syndicated_content_date($date=null)
{
    if ( $date===null ) { $date=time(); }
    return gmdate('Y-m-d\TH:i:s\Z',$date);
}

function _syndicated_content_metadata_to_sqlfields( $metadata )
{
    $fields = array(
        'syndication_source_id' => !empty($metadata['syndication_source_id']) ? $metadata['syndication_source_id'] : _syndicated_content_get_source_id(),
        'media_id'        => $metadata['id'],
        'media_type'      => $metadata['mediaType'],
        'source_url'      => $metadata['sourceUrl'],
        'tiny_url'        => $metadata['tinyUrl'],
        'date_authored'   => !empty($metadata['dateSyndicationVisible']) ? $metadata['dateSyndicationVisible'] : '',
        'date_updated'    => !empty($metadata['dateContentUpdated'])     ? $metadata['dateContentUpdated']     : '',
        'date_synced'     => _syndicated_content_date(),
        'language'        => ( isset($metadata['language']) &&
                               isset($metadata['language']['name']) )
                                   ? $metadata['language']['name'] : '',
        'source'          => ( isset($metadata['source']) &&
                               isset($metadata['source']['name']) )
                                   ? $metadata['source']['name'] : '',
        'source_acronym'  => ( isset($metadata['source']) &&
                               isset($metadata['source']['acronym']) )
                                   ? $metadata['source']['acronym'] : '',
        'metadata'        => json_encode($metadata),
        /* /// maybe if no taxonomy module we should handle this ourselves?
           /// taxonomy fields are indexed automatically by setting default search to "Node search module"
           /// we would have to open this up to search somehow though
           /// i think this would require another module
        'tag_ids'          => $tag_ids,
        'tags'             => $tag_names,
        */
    );
    if ( isset( $metadata['locallyOwned']) )
    {
        $fields['locally_owned'] = $metadata['locallyOwned'];
    }
    return $fields;
}

function _syndicated_content_build_metadata_view( $metadata )
{
    $html = 'Nothing';
    switch ( strtolower($metadata['mediaType']) )
    {
        case 'html':
        default:
            $html = "
              <script type=\"text/javascript\">jQuery('#syndication_media_id').val(". drupal_json_encode(check_plain($metadata['id'])) .");</script>
              <table id=\"lookup_metadata\">
              <tbody>
                <tr>
                  <th>Name</th>          <td>". check_plain($metadata['name']) ."</td>
                  <td rowspan=\"5\"><img src=\"". check_url($metadata['thumbnailUrl']) ."\" height=\"188\" width=\"250\" /></td>
                </tr><tr>
                  <th>Description</th>   <td>". check_markup($metadata['description'],'filtered_html')."</td>
                </tr><tr>
                  <th>Source</th>  <td>". check_plain($metadata['source']['name']) ."</td>
                </tr><tr>
                  <th>Authored</th> <td>". date('D, d M Y, g:i a T',strtotime($metadata['dateContentAuthored'])) ."</td>
                </tr><tr>
                  <th>Last Updated</th>  <td>". date('D, d M Y, g:i a T',strtotime($metadata['dateContentUpdated']))  ."</td>
                </tr><tr>
                  <th>Id</th>            <td colspan=\"2\">". check_plain($metadata['id'])      ."</td>
                </tr><tr>
                  <th>Tiny URL</th>      <td colspan=\"2\">". check_url($metadata['tinyUrl'])   ."</td>
                </tr><tr>
                  <th>Source URL</th>    <td colspan=\"2\">". check_url($metadata['sourceUrl']) ."</td>
                </tr>
              </tbody></table>
            ";
        break;
    }
    return $html;
}

function _syndicated_content_build_metadata_table_view2( $submit_button_id, $list, $pagination )
{
    $header = array(
      array('data'=>t(''),                                     'width'=>'1em'),
      array('data'=>t('Name'),         'field'=>'name',        'width'=>'19%'),
      array('data'=>t('Description'),  'field'=>'description', 'width'=>'50%'),
      array('data'=>t('Source'),       'field'=>'source',      'width'=>'30%'),
    );

    $rows   = array();
    foreach ( $list as $metadata )
    {
      $org_name = ( !empty($metadata['source']) && !empty($metadata['source']['name']) ) ? $metadata['source']['name'] : '';
      $rows[] = array('data'=>array(
        'select'       => "<input type=\"radio\" id=\"synd_radio_{$metadata['id']}\"
                                  name=\"syndication_media_id\"
                                  value=\"{$metadata['id']}\"/>",
        'name'         => "<label for=\"synd_radio_{$metadata['id']}\">{$metadata['name']}<br /><span style=\"font-size:0.8em;opacity:0.6;\">{$metadata['mediaType']}</span></label>",
        'description'  => $metadata['description'],
        'source'       => "{$org_name}<br /><a href=\"{$metadata['sourceUrl']}\" data-preview=\"{$metadata['thumbnailUrl']}\">{$metadata['sourceUrl']}</a>",
      ));
    }

    /// why does this need to be set? does theme_table use it somehow?
    $_GET['page'] = floor(  $pagination['offset'] / $pagination['max'] );
    pager_default_initialize($pagination['total'],  $pagination['max']);
    $html = theme_table(array(
        'header'     => $header,
        'rows'       => $rows,
        'attributes' => array(),
        'sticky'     => false,
        'caption'    => "",
        'colgroups'  => array(),
        'empty'      => t("No Results")
    ));
    $html .= theme('pager');

    return _syndicated_content_ajaxify_table_theme( $html, $submit_button_id );
}

function _syndicated_content_build_metadata_table_view( $submit_button_id, $list, $pagination )
{
    $header = array(
      array('data'=>t(''),                                     'width'=>'1em'),
      array('data'=>t('Name'),         'field'=>'name',        'width'=>'19%'),
      array('data'=>t('Description'),  'field'=>'description', 'width'=>'80%'),
    );

    $rows   = array();
    foreach ( $list as $metadata )
    {
      $org_name = ( !empty($metadata['source']) && !empty($metadata['source']['name']) ) ? $metadata['source']['name'] : '';
      $rows[] = array('data'=>array(
        'select'       => "<input type=\"radio\" id=\"synd_radio_{$metadata['id']}\"
                                  name=\"syndication_media_id\"
                                  value=\"{$metadata['id']}\"/>",
        'name'         => "<label for=\"synd_radio_{$metadata['id']}\">{$metadata['name']}<br /><span style=\"font-size:0.8em;opacity:0.6;\">{$metadata['mediaType']}</span></label>",
        'description'  => $metadata['description'] ."<br /><span style=\"opacity:0.6;\">{$org_name}</span><br /><a href=\"{$metadata['sourceUrl']}\">{$metadata['sourceUrl']}</a>",
      ));
    }

    /// why does this need to be set? does theme_table use it somehow?
    $_GET['page'] = floor(  $pagination['offset'] / $pagination['max'] );
    pager_default_initialize($pagination['total'],  $pagination['max']);
    $html = theme_table(array(
        'header'     => $header,
        'rows'       => $rows,
        'attributes' => array(),
        'sticky'     => false,
        'caption'    => "",
        'colgroups'  => array(),
        'empty'      => t("No Results")
    ));
    $html .= theme('pager');

    return _syndicated_content_ajaxify_table_theme( $html, $submit_button_id );
}


function _syndicated_content_ajaxify_table_theme( $html, $submit_button_id )
{
    /// ajaxify sorting
    $html = preg_replace_callback('/<a .*?title="sort by.*?>/',function($m) use ($submit_button_id)
    {
        /// pull out just the href
        $m[0] = preg_replace_callback('/ href="(.*?)"/',function($h) use ($submit_button_id)
        {
            $url_regex  = "/^(?:(?P<scheme>[^:\/?#]+):\/\/)?(?:(?P<userinfo>[^\/@]*)@)?(?P<host>[^\/?#]*)(?P<path>[^?#]*?(?:\.(?P<format>[^\.?#]*))?)?(?:\?(?P<query>[^#]*))?(?:#(?P<fragment>.*))?$/i";
            $link = array();
            preg_match( $url_regex, $h[1], $u );
            if ( !empty($u['query']) )
            {
                $query  = html_entity_decode( $u['query'] );
                $page   = '';
                $sort   = '';
                $order  = '';
                //$params = preg_split( '/&(amp;)?/', $u['query'] );
                $params = preg_split( '/&(?!amp;)/', $query );
                foreach ( $params as $p )
                {
                    list($k,$v) = explode('=',$p);
                    if ( $k=='page'  ) { $page  = $v; }
                    if ( $k=='sort'  ) { $sort  = $v; }
                    if ( $k=='order' ) { $order = $v; }
                }
                return " href=\"javascript:gotoPage('{$submit_button_id}',{$page},'{$sort}','{$order}')\"";
            }
            return $h[0]; 
        },$m[0]);
        return $m[0];
    },$html);

    /// ajaxify pagination
    $html = preg_replace(
        '/(class="pager\-.*?)href=".*?([?&;]page=([^&#]+?))?"/',
        '${1}href="javascript:gotoPage(\''.$submit_button_id.'\',${3})"',
        $html
    );

    return $html;
}

function _syndicated_content_languages_iso_639_2()
{
    return array(
        "eng" => "English",
        "spa" => "Spanish",
    );
    /*
        "aar" => "Afar",
        "abk" => "Abkhazian",
        "ace" => "Achinese",
        "ach" => "Acoli",
        "ada" => "Adangme",
        "ady" => "Adyghe; Adygei",
        "afa" => "Afro-Asiatic languages",
        "afh" => "Afrihili",
        "afr" => "Afrikaans",
        "ain" => "Ainu",
        "aka" => "Akan",
        "akk" => "Akkadian",
        "alb" => "Albanian",
        "ale" => "Aleut",
        "alg" => "Algonquian languages",
        "alt" => "Southern Altai",
        "amh" => "Amharic",
        "ang" => "English, Old (ca.450-1100)",
        "anp" => "Angika",
        "apa" => "Apache languages",
        "ara" => "Arabic",
        "arc" => "Official Aramaic (700-300 BCE); Imperial Aramaic (700-300 BCE)",
        "arg" => "Aragonese",
        "arm" => "Armenian",
        "arn" => "Mapudungun; Mapuche",
        "arp" => "Arapaho",
        "art" => "Artificial languages",
        "arw" => "Arawak",
        "asm" => "Assamese",
        "ast" => "Asturian; Bable; Leonese; Asturleonese",
        "ath" => "Athapascan languages",
        "aus" => "Australian languages",
        "ava" => "Avaric",
        "ave" => "Avestan",
        "awa" => "Awadhi",
        "aym" => "Aymara",
        "aze" => "Azerbaijani",
        "bad" => "Banda languages",
        "bai" => "Bamileke languages",
        "bak" => "Bashkir",
        "bal" => "Baluchi",
        "bam" => "Bambara",
        "ban" => "Balinese",
        "baq" => "Basque",
        "bas" => "Basa",
        "bat" => "Baltic languages",
        "bej" => "Beja; Bedawiyet",
        "bel" => "Belarusian",
        "bem" => "Bemba",
        "ben" => "Bengali",
        "ber" => "Berber languages",
        "bho" => "Bhojpuri",
        "bih" => "Bihari languages",
        "bik" => "Bikol",
        "bin" => "Bini; Edo",
        "bis" => "Bislama",
        "bla" => "Siksika",
        "bnt" => "Bantu (Other)",
        "bos" => "Bosnian",
        "bra" => "Braj",
        "bre" => "Breton",
        "btk" => "Batak languages",
        "bua" => "Buriat",
        "bug" => "Buginese",
        "bul" => "Bulgarian",
        "bur" => "Burmese",
        "byn" => "Blin; Bilin",
        "cad" => "Caddo",
        "cai" => "Central American Indian languages",
        "car" => "Galibi Carib",
        "cat" => "Catalan; Valencian",
        "cau" => "Caucasian languages",
        "ceb" => "Cebuano",
        "cel" => "Celtic languages",
        "cha" => "Chamorro",
        "chb" => "Chibcha",
        "che" => "Chechen",
        "chg" => "Chagatai",
        "chi" => "Chinese",
        "chk" => "Chuukese",
        "chm" => "Mari",
        "chn" => "Chinook jargon",
        "cho" => "Choctaw",
        "chp" => "Chipewyan; Dene Suline",
        "chr" => "Cherokee",
        "chu" => "Old Slavonic; Old Bulgarian",
        "chv" => "Chuvash",
        "chy" => "Cheyenne",
        "cmc" => "Chamic languages",
        "cop" => "Coptic",
        "cor" => "Cornish",
        "cos" => "Corsican",
        "cpe" => "Creoles and pidgins, English based",
        "cpf" => "Creoles and pidgins, French-based ",
        "cpp" => "Creoles and pidgins, Portuguese-based ",
        "cre" => "Cree",
        "crh" => "Crimean Tatar; Crimean Turkish",
        "crp" => "Creoles and pidgins ",
        "csb" => "Kashubian",
        "cus" => "Cushitic languages",
        "cze" => "Czech",
        "dak" => "Dakota",
        "dan" => "Danish",
        "dar" => "Dargwa",
        "day" => "Land Dayak languages",
        "del" => "Delaware",
        "den" => "Slave (Athapascan)",
        "dgr" => "Dogrib",
        "din" => "Dinka",
        "div" => "Divehi; Dhivehi; Maldivian",
        "doi" => "Dogri",
        "dra" => "Dravidian languages",
        "dsb" => "Lower Sorbian",
        "dua" => "Duala",
        "dum" => "Dutch, Middle (ca.1050-1350)",
        "dut" => "Dutch; Flemish",
        "dyu" => "Dyula",
        "dzo" => "Dzongkha",
        "efi" => "Efik",
        "egy" => "Egyptian (Ancient)",
        "eka" => "Ekajuk",
        "elx" => "Elamite",
        "enm" => "English, Middle (1100-1500)",
        "epo" => "Esperanto",
        "est" => "Estonian",
        "ewe" => "Ewe",
        "ewo" => "Ewondo",
        "fan" => "Fang",
        "fao" => "Faroese",
        "fat" => "Fanti",
        "fij" => "Fijian",
        "fil" => "Filipino; Pilipino",
        "fin" => "Finnish",
        "fiu" => "Finno-Ugrian languages",
        "fon" => "Fon",
        "fre" => "French",
        "frm" => "French, Middle (ca.1400-1600)",
        "fro" => "French, Old (842-ca.1400)",
        "frr" => "Northern Frisian",
        "frs" => "Eastern Frisian",
        "fry" => "Western Frisian",
        "ful" => "Fulah",
        "fur" => "Friulian",
        "gaa" => "Ga",
        "gay" => "Gayo",
        "gba" => "Gbaya",
        "gem" => "Germanic languages",
        "geo" => "Georgian",
        "ger" => "German",
        "gez" => "Geez",
        "gil" => "Gilbertese",
        "gla" => "Gaelic; Scottish Gaelic",
        "gle" => "Irish",
        "glg" => "Galician",
        "glv" => "Manx",
        "gmh" => "German, Middle High (ca.1050-1500)",
        "goh" => "German, Old High (ca.750-1050)",
        "gon" => "Gondi",
        "gor" => "Gorontalo",
        "got" => "Gothic",
        "grb" => "Grebo",
        "grc" => "Greek, Ancient (to 1453)",
        "gre" => "Greek, Modern (1453-)",
        "grn" => "Guarani",
        "gsw" => "Swiss German; Alemannic; Alsatian",
        "guj" => "Gujarati",
        "gwi" => "Gwich'in",
        "hai" => "Haida",
        "hat" => "Haitian; Haitian Creole",
        "hau" => "Hausa",
        "haw" => "Hawaiian",
        "heb" => "Hebrew",
        "her" => "Herero",
        "hil" => "Hiligaynon",
        "him" => "Himachali languages; Western Pahari languages",
        "hin" => "Hindi",
        "hit" => "Hittite",
        "hmn" => "Hmong; Mong",
        "hmo" => "Hiri Motu",
        "hrv" => "Croatian",
        "hsb" => "Upper Sorbian",
        "hun" => "Hungarian",
        "hup" => "Hupa",
        "iba" => "Iban",
        "ibo" => "Igbo",
        "ice" => "Icelandic",
        "ido" => "Ido",
        "iii" => "Sichuan Yi; Nuosu",
        "ijo" => "Ijo languages",
        "iku" => "Inuktitut",
        "ile" => "Interlingue; Occidental",
        "ilo" => "Iloko",
        "ina" => "Interlingua (International Auxiliary Language Association)",
        "inc" => "Indic languages",
        "ind" => "Indonesian",
        "ine" => "Indo-European languages",
        "inh" => "Ingush",
        "ipk" => "Inupiaq",
        "ira" => "Iranian languages",
        "iro" => "Iroquoian languages",
        "ita" => "Italian",
        "jav" => "Javanese",
        "jbo" => "Lojban",
        "jpn" => "Japanese",
        "jpr" => "Judeo-Persian",
        "jrb" => "Judeo-Arabic",
        "kaa" => "Kara-Kalpak",
        "kab" => "Kabyle",
        "kac" => "Kachin; Jingpho",
        "kal" => "Kalaallisut; Greenlandic",
        "kam" => "Kamba",
        "kan" => "Kannada",
        "kar" => "Karen languages",
        "kas" => "Kashmiri",
        "kau" => "Kanuri",
        "kaw" => "Kawi",
        "kaz" => "Kazakh",
        "kbd" => "Kabardian",
        "kha" => "Khasi",
        "khi" => "Khoisan languages",
        "khm" => "Central Khmer",
        "kho" => "Khotanese; Sakan",
        "kik" => "Kikuyu; Gikuyu",
        "kin" => "Kinyarwanda",
        "kir" => "Kirghiz; Kyrgyz",
        "kmb" => "Kimbundu",
        "kok" => "Konkani",
        "kom" => "Komi",
        "kon" => "Kongo",
        "kor" => "Korean",
        "kos" => "Kosraean",
        "kpe" => "Kpelle",
        "krc" => "Karachay-Balkar",
        "krl" => "Karelian",
        "kro" => "Kru languages",
        "kru" => "Kurukh",
        "kua" => "Kuanyama; Kwanyama",
        "kum" => "Kumyk",
        "kur" => "Kurdish",
        "kut" => "Kutenai",
        "lad" => "Ladino",
        "lah" => "Lahnda",
        "lam" => "Lamba",
        "lao" => "Lao",
        "lat" => "Latin",
        "lav" => "Latvian",
        "lez" => "Lezghian",
        "lim" => "Limburgan; Limburger; Limburgish",
        "lin" => "Lingala",
        "lit" => "Lithuanian",
        "lol" => "Mongo",
        "loz" => "Lozi",
        "ltz" => "Luxembourgish; Letzeburgesch",
        "lua" => "Luba-Lulua",
        "lub" => "Luba-Katanga",
        "lug" => "Ganda",
        "lui" => "Luiseno",
        "lun" => "Lunda",
        "luo" => "Luo (Kenya and Tanzania)",
        "lus" => "Lushai",
        "mac" => "Macedonian",
        "mad" => "Madurese",
        "mag" => "Magahi",
        "mah" => "Marshallese",
        "mai" => "Maithili",
        "mak" => "Makasar",
        "mal" => "Malayalam",
        "man" => "Mandingo",
        "mao" => "Maori",
        "map" => "Austronesian languages",
        "mar" => "Marathi",
        "mas" => "Masai",
        "may" => "Malay",
        "mdf" => "Moksha",
        "mdr" => "Mandar",
        "men" => "Mende",
        "mga" => "Irish, Middle (900-1200)",
        "mic" => "Mi'kmaq; Micmac",
        "min" => "Minangkabau",
        "mis" => "Uncoded languages",
        "mkh" => "Mon-Khmer languages",
        "mlg" => "Malagasy",
        "mlt" => "Maltese",
        "mnc" => "Manchu",
        "mni" => "Manipuri",
        "mno" => "Manobo languages",
        "moh" => "Mohawk",
        "mon" => "Mongolian",
        "mos" => "Mossi",
        "mul" => "Multiple languages",
        "mun" => "Munda languages",
        "mus" => "Creek",
        "mwl" => "Mirandese",
        "mwr" => "Marwari",
        "myn" => "Mayan languages",
        "myv" => "Erzya",
        "nah" => "Nahuatl languages",
        "nai" => "North American Indian languages",
        "nap" => "Neapolitan",
        "nau" => "Nauru",
        "nav" => "Navajo; Navaho",
        "nbl" => "Ndebele, South; South Ndebele",
        "nde" => "Ndebele, North; North Ndebele",
        "ndo" => "Ndonga",
        "nds" => "Low German; Low Saxon; German, Low; Saxon, Low",
        "nep" => "Nepali",
        "new" => "Nepal Bhasa; Newari",
        "nia" => "Nias",
        "nic" => "Niger-Kordofanian languages",
        "niu" => "Niuean",
        "nno" => "Norwegian Nynorsk; Nynorsk, Norwegian",
        "nob" => "Bokmål, Norwegian; Norwegian Bokmål",
        "nog" => "Nogai",
        "non" => "Norse, Old",
        "nor" => "Norwegian",
        "nqo" => "N'Ko",
        "nso" => "Pedi; Sepedi; Northern Sotho",
        "nub" => "Nubian languages",
        "nwc" => "Classical Newari; Old Newari; Classical Nepal Bhasa",
        "nya" => "Chichewa; Chewa; Nyanja",
        "nym" => "Nyamwezi",
        "nyn" => "Nyankole",
        "nyo" => "Nyoro",
        "nzi" => "Nzima",
        "oci" => "Occitan (post 1500); Provençal",
        "oji" => "Ojibwa",
        "ori" => "Oriya",
        "orm" => "Oromo",
        "osa" => "Osage",
        "oss" => "Ossetian; Ossetic",
        "ota" => "Turkish, Ottoman (1500-1928)",
        "oto" => "Otomian languages",
        "paa" => "Papuan languages",
        "pag" => "Pangasinan",
        "pal" => "Pahlavi",
        "pam" => "Pampanga; Kapampangan",
        "pan" => "Panjabi; Punjabi",
        "pap" => "Papiamento",
        "pau" => "Palauan",
        "peo" => "Persian, Old (ca.600-400 B.C.)",
        "per" => "Persian",
        "phi" => "Philippine languages",
        "phn" => "Phoenician",
        "pli" => "Pali",
        "pol" => "Polish",
        "pon" => "Pohnpeian",
        "por" => "Portuguese",
        "pra" => "Prakrit languages",
        "pro" => "Provençal, Old (to 1500)",
        "pus" => "Pushto; Pashto",
        "que" => "Quechua",
        "raj" => "Rajasthani",
        "rap" => "Rapanui",
        "rar" => "Rarotongan; Cook Islands Maori",
        "roa" => "Romance languages",
        "roh" => "Romansh",
        "rom" => "Romany",
        "rum" => "Romanian; Moldavian; Moldovan",
        "run" => "Rundi",
        "rup" => "Aromanian; Arumanian; Macedo-Romanian",
        "rus" => "Russian",
        "sad" => "Sandawe",
        "sag" => "Sango",
        "sah" => "Yakut",
        "sai" => "South American Indian (Other)",
        "sal" => "Salishan languages",
        "sam" => "Samaritan Aramaic",
        "san" => "Sanskrit",
        "sas" => "Sasak",
        "sat" => "Santali",
        "scn" => "Sicilian",
        "sco" => "Scots",
        "sel" => "Selkup",
        "sem" => "Semitic languages",
        "sga" => "Irish, Old (to 900)",
        "sgn" => "Sign Languages",
        "shn" => "Shan",
        "sid" => "Sidamo",
        "sin" => "Sinhala; Sinhalese",
        "sio" => "Siouan languages",
        "sit" => "Sino-Tibetan languages",
        "sla" => "Slavic languages",
        "slo" => "Slovak",
        "slv" => "Slovenian",
        "sma" => "Southern Sami",
        "sme" => "Northern Sami",
        "smi" => "Sami languages",
        "smj" => "Lule Sami",
        "smn" => "Inari Sami",
        "smo" => "Samoan",
        "sms" => "Skolt Sami",
        "sna" => "Shona",
        "snd" => "Sindhi",
        "snk" => "Soninke",
        "sog" => "Sogdian",
        "som" => "Somali",
        "son" => "Songhai languages",
        "sot" => "Sotho, Southern",
        "srd" => "Sardinian",
        "srn" => "Sranan Tongo",
        "srp" => "Serbian",
        "srr" => "Serer",
        "ssa" => "Nilo-Saharan languages",
        "ssw" => "Swati",
        "suk" => "Sukuma",
        "sun" => "Sundanese",
        "sus" => "Susu",
        "sux" => "Sumerian",
        "swa" => "Swahili",
        "swe" => "Swedish",
        "syc" => "Classical Syriac",
        "syr" => "Syriac",
        "tah" => "Tahitian",
        "tai" => "Tai languages",
        "tam" => "Tamil",
        "tat" => "Tatar",
        "tel" => "Telugu",
        "tem" => "Timne",
        "ter" => "Tereno",
        "tet" => "Tetum",
        "tgk" => "Tajik",
        "tgl" => "Tagalog",
        "tha" => "Thai",
        "tib" => "Tibetan",
        "tig" => "Tigre",
        "tir" => "Tigrinya",
        "tiv" => "Tiv",
        "tkl" => "Tokelau",
        "tlh" => "Klingon; tlhIngan-Hol",
        "tli" => "Tlingit",
        "tmh" => "Tamashek",
        "tog" => "Tonga (Nyasa)",
        "ton" => "Tonga (Tonga Islands)",
        "tpi" => "Tok Pisin",
        "tsi" => "Tsimshian",
        "tsn" => "Tswana",
        "tso" => "Tsonga",
        "tuk" => "Turkmen",
        "tum" => "Tumbuka",
        "tup" => "Tupi languages",
        "tur" => "Turkish",
        "tut" => "Altaic languages",
        "tvl" => "Tuvalu",
        "twi" => "Twi",
        "tyv" => "Tuvinian",
        "udm" => "Udmurt",
        "uga" => "Ugaritic",
        "uig" => "Uighur; Uyghur",
        "ukr" => "Ukrainian",
        "umb" => "Umbundu",
        "und" => "Undetermined",
        "urd" => "Urdu",
        "uzb" => "Uzbek",
        "vai" => "Vai",
        "ven" => "Venda",
        "vie" => "Vietnamese",
        "vol" => "Volapük",
        "vot" => "Votic",
        "wak" => "Wakashan languages",
        "wal" => "Walamo",
        "war" => "Waray",
        "was" => "Washo",
        "wel" => "Welsh",
        "wen" => "Sorbian languages",
        "wln" => "Walloon",
        "wol" => "Wolof",
        "xal" => "Kalmyk; Oirat",
        "xho" => "Xhosa",
        "yao" => "Yao",
        "yap" => "Yapese",
        "yid" => "Yiddish",
        "yor" => "Yoruba",
        "ypk" => "Yupik languages",
        "zap" => "Zapotec",
        "zbl" => "Blissymbols; Blissymbolics; Bliss",
        "zen" => "Zenaga",
        "zgh" => "Standard Moroccan Tamazight",
        "zha" => "Zhuang; Chuang",
        "znd" => "Zande languages",
        "zul" => "Zulu",
        "zun" => "Zuni",
        "zxx" => "No linguistic content; Not applicable",
        "zza" => "Zaza; Dimili; Dimli; Kirdki; Kirmanjki; Zazaki",
    );
    */
}

