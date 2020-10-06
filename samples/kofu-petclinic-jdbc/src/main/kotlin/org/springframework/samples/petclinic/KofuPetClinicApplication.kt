package org.springframework.samples.petclinic

import com.samskivert.mustache.Mustache
import org.springframework.context.MessageSource
import org.springframework.core.io.ClassPathResource
import org.springframework.fu.kofu.jdbc.jdbc
import org.springframework.fu.kofu.messageSource
import org.springframework.fu.kofu.webApplication
import org.springframework.fu.kofu.webmvc.mustache
import org.springframework.fu.kofu.webmvc.webMvc
import org.springframework.samples.petclinic.owner.ownerConfig
import org.springframework.samples.petclinic.pet.petConfig
import org.springframework.samples.petclinic.system.systemConfig
import org.springframework.samples.petclinic.vet.vetConfig
import org.springframework.samples.petclinic.visit.visitConfig
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.function.RenderingResponse
import org.springframework.web.servlet.function.RouterFunctionDsl

fun myRouter(messageSource: MessageSource, router: RouterFunctionDsl.() -> Unit) =
    org.springframework.web.servlet.function.router {
        apply(router)
        onError<Throwable> { error, request ->
            val locale = request.locale()
            RenderingResponse.create("error/500").modelAttributes(mapOf(
                "lang" to locale.language,
                "i18n" to Mustache.Lambda { frag, out ->
                    val tokens = frag.execute().split("|")
                    out.write(messageSource.getMessage(tokens[0], tokens.slice(IntRange(1, tokens.size - 1)).toTypedArray(), locale))
                },
                "errorActive" to true
            )).build()
        }
        onError<NoHandlerFoundException> { error, request ->
            val locale = request.locale()
            RenderingResponse.create("error/400").modelAttributes(mapOf(
                "lang" to locale.language,
                "i18n" to Mustache.Lambda { frag, out ->
                    val tokens = frag.execute().split("|")
                    out.write(messageSource.getMessage(tokens[0], tokens.slice(IntRange(1, tokens.size - 1)).toTypedArray(), locale))
                },
                "errorActive" to true
            )).build()
        }
        after { request, response ->
            when (response) {
                is RenderingResponse -> {
                    val locale = request.locale()
                    RenderingResponse.from(response)
                        .modelAttributes(response.model() + mapOf(
                            "lang" to locale.language,
                            "i18n" to Mustache.Lambda { frag, out ->
                                val tokens = frag.execute().split("|")
                                out.write(messageSource.getMessage(tokens[0], tokens.slice(IntRange(1, tokens.size - 1)).toTypedArray(), locale))
                            }
                        ))
                        .build()
                }
                else -> response
            }
        }
    }

val app = webApplication {
    messageSource {
        basename = "messages/messages"
    }
    webMvc {
        mustache()
        converters {
            string()
            resource()
            jackson {
                indentOutput = true
            }
        }
        router {
            resources("/webjars/**", ClassPathResource("META-INF/resources/webjars/"))
        }

    }
    jdbc {
        schema = listOf("classpath*:db/h2/schema.sql")
        data = listOf("classpath*:db/h2/data.sql")
    }
    enable(systemConfig)
    enable(vetConfig)
    enable(ownerConfig)
    enable(visitConfig)
    enable(petConfig)
}

fun main() {
    app.run()
}