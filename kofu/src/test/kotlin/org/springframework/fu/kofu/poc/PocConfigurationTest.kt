package org.springframework.fu.kofu.poc

import org.junit.jupiter.api.Test
import org.springframework.fu.kofu.application
import org.springframework.util.SocketUtils

class PocConfigurationTest {
    @Test
    fun `test poc configuration`() {
        val app = application {
            poc {
                mongoDb {
                    uri = "mongodb://localhost:${SocketUtils.findAvailableTcpPort()}/test"
                    embedded()
                }
                mongoDb {
                    uri = "mongodb://localhost:${SocketUtils.findAvailableTcpPort()}/test2"
                    embedded()
                }
            }
        }
        app.run()
    }
}