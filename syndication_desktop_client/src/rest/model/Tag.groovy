package rest.model

/**
 * Created by sgates on 12/3/14.
 */
class Tag implements Comparable{
    String name
    TagType tagType
    TagLanguage tagLanguage
    long id

    String toString(){
        name
    }

    static Tag getTagFromJson(json){
        new Tag(name:json.name,
                id:json.id,
                tagLanguage: new TagLanguage(
                    name:json.language.name,
                    id:json.language.id,
                    isoCode: json.language.isoCode),
                tagType: new TagType(
                    id:json.type.id,
                    type:json.type.name
                )
        )
    }

    @Override
    int compareTo(Object o) {
        if(o instanceof Tag){
            Tag other = (Tag) o;
            return Long.compare(id, other.id);
        }
        return -1;
    }
}
