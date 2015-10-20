package syndication.rest.validation

/**
 * Created by nburk on 10/17/14.
 */
class MediaValidationService {

    def embedValidation(def params){
        String errors = ""
        if(params.flavor && params.flavor != "iframe"){
            if(params?.flavor?.toLowerCase() != "javascript"){
                errors = errors + "Invalid value for flavor, "
            }
        }
        if(params.width && ((params.width?.isInteger() && (params.width as Integer) < 0) || !params.width?.isInteger())){
            errors += "The width must be a positive number, "
        }
        if(params.height && ((params.height?.isInteger() && (params.height as Integer) < 0) || !params.height?.isInteger())){
            errors += "The height must be a positive number, "
        }

        if(errors){
            errors = errors.substring(0, errors.length()-2) + "."
        }
        return errors
    }
}
