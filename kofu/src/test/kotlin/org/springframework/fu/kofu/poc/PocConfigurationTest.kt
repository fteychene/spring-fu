package org.springframework.fu.kofu.poc

import org.junit.jupiter.api.Test
import org.springframework.fu.kofu.application
import org.springframework.fu.kofu.templating.thymeleaf
import org.springframework.fu.kofu.webApplication
import org.springframework.util.SocketUtils

class PocConfigurationTest {
    @Test
    fun `test poc configuration`() {
        val app = webApplication {
            poc {
                mongoDb {
                    uri = "mongodb://localhost:${SocketUtils.findAvailableTcpPort()}/test"
                    embedded()
                }
                mongoDb {
                    uri = "mongodb://localhost:${SocketUtils.findAvailableTcpPort()}/test2"
                    embedded()
                }
                webMvc {
                    router {
                        GET("/vets.html") {
                            ok().render("welcome")
                        }
                    }
                }
                // ...
                webMvc {
                    // Router are merged in one RouterFunction before being registered as bean
                    router {
                        GET("/vets.html") {
                            ok().render("welcome")
                        }
                    }
                    router {
                        GET("/vets.html") {
                            ok().render("welcome")
                        }
                    }
                }
            }
        }
        app.run()
    }
}