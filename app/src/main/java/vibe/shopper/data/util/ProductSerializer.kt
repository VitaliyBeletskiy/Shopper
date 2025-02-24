package vibe.shopper.data.util

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import vibe.shopper.data.model.Chair
import vibe.shopper.data.model.Couch
import vibe.shopper.data.model.Product

object ProductSerializer : JsonContentPolymorphicSerializer<Product>(Product::class) {
    override fun selectDeserializer(
        element: JsonElement,
    ): DeserializationStrategy<Product> = when (element.jsonObject["type"]?.jsonPrimitive?.content) {
        "chair" -> Chair.serializer()
        "couch" -> Couch.serializer()
        else -> throw SerializationException("Unknown type")
    }
}
