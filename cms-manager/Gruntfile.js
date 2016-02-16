module.exports = function(grunt) {
    grunt.initConfig({
        copy: {
            main: {
                files: [{
                    expand: true,
                    src: ['bower_components/bootstrap/dist/css/bootstrap.min.css','bower_components/bootstrap/dist/fonts/*','bower_components/bootstrap/dist/css/bootstrap-theme.min.css',
                        'bower_components/font-awesome/css/*', 'bower_components/font-awesome/fonts/*',
                        'bower_components/font-awesome/scss/*', 'bower_components/datatables/media/css/dataTables.bootstrap.min.css'],
                    dest: 'grails-app/assets/stylesheets'
                    },
                    {
                        expand: true,
                        src: ['bower_components/datatables/media/images/*'],
                        dest: 'grails-app/assets/images'
                    },
                    {
                        expand: true,
                        src: ['bower_components/bootstrap/dist/js/*.min.js', 'bower_components/datatables/media/js/jquery.dataTables.min.js'],
                        dest: 'grails-app/assets/javascripts'
                    }
                ]
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.registerTask('default', ['copy']);
};
