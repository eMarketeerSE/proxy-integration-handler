package com.emarketeer.aws.apigateway.adapter

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDate

class LocalDateTypeAdapter : JsonDeserializer<LocalDate> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDate {
        return json?.asJsonPrimitive?.asString.let { LocalDate.parse(it) }
    }
}