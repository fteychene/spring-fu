package org.springframework.fu.kofu.poc

import org.springframework.beans.factory.support.BeanDefinitionReaderUtils
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.registerBean
import org.springframework.fu.kofu.webmvc.WebMvcServerDsl
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.RouterFunctionDsl
import org.springframework.web.servlet.function.ServerResponse

class PocWebMvcServerDsl(
    val semigroup: Semigroup<RouterFunction<ServerResponse>> = defaultRouteFunctionSemigroup,
    var routers : List<RouterFunction<ServerResponse>> = listOf()): WebMvcServerDsl({ }) {

    override fun router(routes: (RouterFunctionDsl.() -> Unit)) {
        routers += org.springframework.web.servlet.function.router(routes)
    }

    override fun initialize(context: GenericApplicationContext) {
        super.initialize(context)
        with(semigroup) {
            routers.reduceOrNull { x, y -> x.combine(y)}
                ?.let { context.registerBean(BeanDefinitionReaderUtils.uniqueBeanName(RouterFunctionDsl::class.java.name, context)) { it } }
        }
    }
}