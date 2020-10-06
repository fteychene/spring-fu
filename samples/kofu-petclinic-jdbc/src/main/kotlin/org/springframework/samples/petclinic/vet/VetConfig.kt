package org.springframework.samples.petclinic.vet

import com.samskivert.mustache.Mustache
import org.springframework.context.MessageSource
import org.springframework.fu.kofu.configuration
import org.springframework.samples.petclinic.locale
import org.springframework.samples.petclinic.myRouter
import org.springframework.web.servlet.function.RenderingResponse
import org.springframework.web.servlet.function.router
import java.lang.IllegalStateException

val vetConfig = configuration {
    // Lambda based for for native application compat because of https://github.com/oracle/graal/issues/2500
    beans {
        bean { JdbcVetRepositoryImpl(ref()) }
        bean(::vetRoutes)
    }
}

fun vetRoutes(messageSource: MessageSource, vetRepository: VetRepository) = myRouter(messageSource) {
    GET("/vets.html") {
        ok().render(
            "vets/vetList", mapOf(
                "vets" to vetRepository.findAll().toList(),
                "hasSpecialty" to Mustache.Lambda { frag, out ->
                    (frag.context() as? Vet)?.let { vet ->
                        out.write(
                            if (vet.specialties.isNotEmpty()) frag.execute()
                            else "none"
                        )
                    } ?: throw IllegalStateException("hasSpecialty should be called for a Vet")
                },
                "vetActive" to true
            )
        )
    }
    GET("/vets") {
        ok().body(object {
            val vets = vetRepository.findAll().toList()
        })
    }
}