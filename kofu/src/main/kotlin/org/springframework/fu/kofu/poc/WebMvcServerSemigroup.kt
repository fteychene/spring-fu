package org.springframework.fu.kofu.poc

import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.plus


val defaultPocWebServerSemigroup = object: Semigroup<PocWebMvcServerDsl> {
    override fun PocWebMvcServerDsl.combine(other: PocWebMvcServerDsl): PocWebMvcServerDsl =
        apply {
            routers += other.routers
        }
}

val defaultRouteFunctionSemigroup = object: Semigroup<RouterFunction<ServerResponse>> {
    override fun RouterFunction<ServerResponse>.combine(other: RouterFunction<ServerResponse>): RouterFunction<ServerResponse> = this.plus(other)
}