package org.springframework.fu.kofu.poc

import org.springframework.context.support.GenericApplicationContext
import org.springframework.fu.kofu.mongo.AbstractMongoDsl

private data class MultiMongoDsl(val configurations: List<AbstractMongoDsl>): AbstractMongoDsl() {
    override fun initialize(context: GenericApplicationContext) {
        println("Configure multi mongo")
        configurations.forEach { it.initialize(context) }
    }
}

val defaultMongoDslSemigroup = object: Semigroup<AbstractMongoDsl> {
    fun AbstractMongoDsl.configurations() = when(this) {
        is MultiMongoDsl -> configurations
        else -> listOf(this)
    }

    override fun AbstractMongoDsl.combine(other: AbstractMongoDsl): AbstractMongoDsl =
        MultiMongoDsl(configurations() + other.configurations())

}