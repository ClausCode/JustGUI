package com.clauscode.packages.justgui.serialize

import com.clauscode.packages.justgui.UIPosition
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object UIPositionSerializer : KSerializer<UIPosition> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UIPosition", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UIPosition) {
        encoder.encodeString("${value.x}:${value.y}")
    }

    override fun deserialize(decoder: Decoder): UIPosition {
        val split = decoder.decodeString().split(":")
        return if(split.size < 2) UIPosition.bySlot(split[0].toInt())
        else UIPosition(split[0].toInt(), split[1].toInt())
    }
}