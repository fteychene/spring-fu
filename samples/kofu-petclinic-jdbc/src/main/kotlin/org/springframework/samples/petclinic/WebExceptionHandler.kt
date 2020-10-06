package org.springframework.samples.petclinic

import org.springframework.boot.autoconfigure.web.ErrorProperties
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource

class WebExceptionHandler(errorAttributes: ErrorAttributes,
                          viewResolver: ErrorViewResolver,
                          errorProperties: ErrorProperties,
                          applicationContext: ApplicationContext,
                          private val messageSource: MessageSource):
        AbstractErrorController(errorAttributes, listOf(viewResolver)) {

    override fun getErrorPath(): String {
        return "test"
    }


}