package org.springframework.fu.kofu.poc

import org.springframework.context.support.GenericApplicationContext
import org.springframework.fu.kofu.AbstractDsl
import org.springframework.fu.kofu.ConfigurationDsl
import org.springframework.fu.kofu.mongo.AbstractMongoDsl
import org.springframework.fu.kofu.mongo.MongoDsl
import org.springframework.fu.kofu.webmvc.WebMvcServerDsl
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.ServerResponse

class PocConfiguration(
    val mongoDslSemigroup: Semigroup<AbstractMongoDsl> = defaultMongoDslSemigroup,
    val webMvcServerSemigroup: Semigroup<PocWebMvcServerDsl> = defaultPocWebServerSemigroup,
    val routeFunctionSemigroup: Semigroup<RouterFunction<ServerResponse>> = defaultRouteFunctionSemigroup,

    ): AbstractDsl() {
    private var mongoDsl: List<AbstractMongoDsl> = listOf()
    private var webMvcServerDsl: List<PocWebMvcServerDsl> = listOf()

    fun mongoDb(dsl: MongoDsl.() -> Unit) {
        mongoDsl += MongoDsl(dsl)
    }

    fun webMvc(dsl: PocWebMvcServerDsl.() -> Unit =  {}) {
        webMvcServerDsl += PocWebMvcServerDsl(routeFunctionSemigroup).apply(dsl)
    }

    override fun initialize(context: GenericApplicationContext) {
        super.initialize(context)
        with(mongoDslSemigroup) {
            mongoDsl.reduceOrNull { x, y -> x.combine(y)}
                ?.run { initialize(context) }
        }
        with(webMvcServerSemigroup) {
            webMvcServerDsl.reduceOrNull { x, y -> x.combine(y)}
                ?.run { initialize(context) }
        }
    }
}

fun ConfigurationDsl.poc(dsl: PocConfiguration.() -> Unit) {
    PocConfiguration().apply(dsl).initialize(context)
}