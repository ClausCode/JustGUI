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
        return Material.fromNamespaceId(decoder.decodeString())!!
    }
}