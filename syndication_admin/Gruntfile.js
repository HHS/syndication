module.exports = function(grunt) {
    grunt.initConfig({
        copy: {
            main: {
                files: [{
                    expand: true,
                    src: ['bower_components/jquery-ui/themes/ui-lightness/jquery-ui.min.css',
                        'bower_components/bootstrap/dist/css/bootstrap.min.css','bower_components/bootstrap/dist/fonts/*',
                        'bower_components/font-awesome/css/*','bower_components/font-awesome/fonts/*', 'bower_components/font-awesome/less/*',
                        'bower_components/font-awesome/scss/*', 'bower_components/magnific-popup/dist/magnific-popup.css'],
                    dest: 'grails-app/assets/stylesheets'
                },
                {
                    expand: true,
                    src: ['bower_components/jquery-ui/themes/ui-lightness/images/*'],
                    dest: 'grails-app/assets/images'
                },
                {
                    expand: true,
                    src: ['bower_components/bootstrap/dist/js/*.min.js', 'bower_components/jquery-ui/ui/minified/jquery-ui.min.js',
                        'bower_components/metisMenu/dist/metisMenu.min.js','bower_components/magnific-popup/dist/jquery.magnific-popup.min.js'],
                    dest: 'grails-app/assets/javascripts'
                }
        ]
        }
    }
});

grunt.loadNpmTasks('grunt-contrib-copy');
grunt.registerTask('default', ['copy']);
};


//module.exports = function (grunt) {
//
//    grunt.loadNpmTasks("grunt-shell");
//
//    grunt.initConfig({d
//
//        copy: {
//            main: {
//                files: {
//                    //src: ["src/html/*.html"], dest: "build/html/", filter: "isFile"
//                    //"build/html/": ["src/html/*"]
//                    //src: ['src/html/*'], dest: 'build/html/', filter: 'isFile' // includes files in path
//                    //src: ['src/html/**'], dest: 'build/html/' // includes files in path and its subdirs
//                    expand: true, src: ['bower_components/bootstrap/'], dest: 'grails-app/assets/stylesheets/bower-styles' // makes all src relative to cwd
//                }
//            }
//            //watch: {
//            //    html: {
//            //        files: ["bower_components/**/dist/**/*.css"],
//            //        tasks: ['move']
//            //    }
//            //}
//        },
//        shell: {
//            options: {
//                stdout: true
//            },
//            localInstall: {
//                command: "./node_modules/.bin/bower update --quiet --offline"
//            },
//
//            webInstall: {
//                command: "./node_modules/.bin/bower update --quiet"
//            }
//        }
//    });
//
//    grunt.registerTask("localInstall", [ "shell:localInstall"]);
//    grunt.registerTask("webInstall", [ "shell:webInstall"]);
//    //grunt.registerTask("copy",["copy"]);
//    grunt.registerTask("default", ["copy"]);
//
//};