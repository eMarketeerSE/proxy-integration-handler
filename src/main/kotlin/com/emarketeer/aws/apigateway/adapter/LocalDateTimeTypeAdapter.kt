package com.emarketeer.aws.apigateway.adapter

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeTypeAdapter : JsonSerializer<LocalDateTime> {
    override fun serialize(src: LocalDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(DateTimeFormatter.ofPattern(rfc3339Format).format(ZonedDateTime.of(src, ZoneOffset.UTC)))
    }

    companion object {
        private const val rfc3339Format = "yyyy-MM-dd'T'HH:mm:ssXXX"
    }
}