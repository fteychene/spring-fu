package org.springframework.fu.kofu.poc

import org.springframework.context.support.GenericApplicationContext
import org.springframework.fu.kofu.AbstractDsl
import org.springframework.fu.kofu.ConfigurationDsl
import org.springframework.fu.kofu.mongo.AbstractMongoDsl
import org.springframework.fu.kofu.mongo.MongoDsl
import org.springframework.fu.kofu.mongo.ReactiveMongoDsl

class PocConfiguration(
    private val mongoDslSemigroup: Semigroup<AbstractMongoDsl> = defaultMongoDslSemigroup
): AbstractDsl() {
    private var mongoDsl: List<AbstractMongoDsl> = listOf()

    fun mongoDb(dsl: MongoDsl.() -> Unit) {
        mongoDsl += MongoDsl(dsl)
    }

    fun reactiveMongoDb(dsl: ReactiveMongoDsl.() -> Unit) {
        mongoDsl += ReactiveMongoDsl(dsl)
    }

    override fun initialize(context: GenericApplicationContext) {
        super.initialize(context)
        with(mongoDslSemigroup) {
            mongoDsl.reduceOrNull { x, y -> x.combine(y)}
                ?.run { initialize(context) }
        }
    }
}

fun ConfigurationDsl.poc(dsl: PocConfiguration.() -> Unit) {
    PocConfiguration().apply(dsl).initialize(context)
}