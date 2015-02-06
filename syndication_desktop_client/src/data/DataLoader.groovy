package data

import groovy.sql.Sql
import rest.model.Tag

import javax.swing.JLabel

/**
 * Created by sgates on 10/3/14.
 */
class DataLoader {
    def sql

    DataLoader(){
        sql = Sql.newInstance( 'jdbc:h2:data/localDatabase', 'sa', '', 'org.h2.Driver' )
        initTables()
    }

    def initTables(){
        sql.execute('''
        CREATE TABLE IF NOT EXISTS media(
            id MEDIUMINT NOT NULL AUTO_INCREMENT,
            name varchar(255),
            url varchar(2000),
            desc varchar(2000),
            lang INTEGER,
            org INTEGER,
            PRIMARY KEY (id))
        '''
        )
    }

    def addMedia(String name, String url, String desc, Long lang, Long org){
        if(url.endsWith("/")){ url = url[0..-2]}
        def media = sql.dataSet("media")
        media.add(name:name, url:url, desc:desc, lang:lang, org:org)
    }

    def updateMedia(Long id, String name, String url, String desc, Long lang, Long org){
        if(url.endsWith("/")){ url = url[0..-2]}
        sql.executeUpdate("update media set name=${name}, url=${url}, desc=${desc}, lang=${lang}, org=${org} where id=${id}")
    }

    def deleteMedia(Long id){
        sql.execute("delete from media where id=${id}")
    }

    MediaItem getRecord(long id){
        MediaItem mi
        sql.eachRow("SELECT * FROM media WHERE id=${id}"){ media ->
            mi = new MediaItem(id:media.id, name:media.name, url:media.url, desc: media.desc, org:media.org, lang:media.lang)
        }
        mi
    }

    List<MediaItem> getAllRecords(){
        def mediaList = []
        sql.eachRow("SELECT * FROM media"){ media ->
            mediaList << new MediaItem(id:media.id, name:media.name, url:media.url, desc: media.desc, org:media.org, lang:media.lang)
        }

        mediaList
    }

    class MediaItem{
        Long id
        String name
        String url
        String desc
        Long org
        Long lang
        List<Tag> tags

        int status = 0

        String toString(){
            "${id}: ${name} - ${url}"
        }
    }
}
