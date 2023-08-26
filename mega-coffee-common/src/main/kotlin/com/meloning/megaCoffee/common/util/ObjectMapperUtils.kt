package com.meloning.megaCoffee.common.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.meloning.megaCoffee.common.constant.Constant
import java.io.IOException

object ObjectMapperUtils {
    private val OBJECT_MAPPER: ObjectMapper = jacksonObjectMapper().apply {
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    fun getObjectMapper(): ObjectMapper = OBJECT_MAPPER

    fun toPrettyJson(source: Any?): String {
        val sourceForSerialization = source ?: return Constant.NULL_STR

        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(sourceForSerialization)
        } catch (ioe: IOException) {
            throw JsonWriteException(ioe)
        }
    }

    class JsonWriteException(cause: Throwable) : RuntimeException(cause)
}
