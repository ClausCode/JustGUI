package com.clauscode.packages.justgui.serialize

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minestom.server.item.Material

object MaterialSerializer : KSerializer<Material> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UIPosition", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Material) {
        encoder.encodeString(value.name())
    }

    override fun deserialize(decoder: Decoder): Material {
        var namespace = decoder.decodeString()
        if(namespace.indexOf(":") == -1) {
            namespace = "minecraft:$namespace"
        }
        return Material.fromNamespaceId(namespace)!!
    }
}