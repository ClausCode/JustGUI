package com.clauscode.packages.justgui.serialize

import com.clauscode.packages.justgui.components.UIComponent
import com.clauscode.packages.justgui.exception.ComponentAlreadyExistsException
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

object UIComponentsSerializer : KSerializer<MutableMap<String, UIComponent>> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UIComponents", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: MutableMap<String, UIComponent>) {
        encoder.encodeString("")
    }

    override fun deserialize(decoder: Decoder): MutableMap<String, UIComponent> {
        val componentsArray = JSONParser().parse(JsonAsStringSerializer.deserialize(decoder)) as JSONArray
        val resultMap: MutableMap<String, UIComponent> = LinkedHashMap()
        for (element in componentsArray) {
            val componentJson = element as JSONObject
            val clazz = Class.forName(componentJson["controller"] as String)
            val jsonData = componentJson["object"] as JSONObject

            val component = (clazz.getDeclaredConstructor().newInstance() as UIComponent).decode(jsonData.toJSONString())

            if (resultMap.containsKey(component.id))
                throw ComponentAlreadyExistsException("Component with id: ${component.id} already exist!")
            resultMap[component.id] = component
        }
        return resultMap
    }
}