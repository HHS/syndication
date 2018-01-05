environments {
    production {
        server {
            session {
                cookie {
                    domain = System.getenv('ADMIN_SERVER_URL').split('/')[2]
                    setProperty "http-only", true
                    path = '/'
                    secure = true
                }
            }
        }
    }
}