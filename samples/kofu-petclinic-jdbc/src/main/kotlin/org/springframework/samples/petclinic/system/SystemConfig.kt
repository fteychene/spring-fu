package org.springframework.samples.petclinic.system

import com.samskivert.mustache.Mustache
import org.springframework.context.MessageSource
import org.springframework.fu.kofu.configuration
import org.springframework.samples.petclinic.locale
import org.springframework.samples.petclinic.myRouter
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.function.RenderingResponse
import org.springframework.web.servlet.function.router
import java.util.*

val systemConfig = configuration {
    beans {
        bean(::systemRoutes)
    }
}

fun systemRoutes(messageSource: MessageSource) = myRouter(messageSource) {
    GET("/") {
        ok().render("welcome", mapOf(
            "homeActive" to true
        ))
    }
    GET("/oups") {
        throw RuntimeException("Expected: route used to showcase what happens when an exception is thrown")
    }
}
//        .filter { request, next ->
//    when (val response = next.handle(request)) {
//        is RenderingResponse -> {
//            val locale: Locale = request.locale()
//            RenderingResponse.from(response)
//                    .modelAttributes(mapOf(
//                            "en" to (locale.language == "en"),
//                            "fr" to (locale.language == "fr"),
//                            "de" to (locale.language == "de"),
//                            "i18n" to Mustache.Lambda { frag, out ->
//                                val tokens = frag.execute().split("|")
//                                out.write(messageSource.getMessage(tokens[0], tokens.slice(IntRange(1, tokens.size - 1)).toTypedArray(), locale))
//                            },
//                            "homeActive" to true
//                    ))
//                    .build()
//        }
//        else -> response
//    }
//}
