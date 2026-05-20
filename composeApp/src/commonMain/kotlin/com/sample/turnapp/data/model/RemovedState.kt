package com.sample.turnapp.data.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = RemovedStateSerializer::class)
enum class RemovedState(val value: Int) {
    ACTIVE(0),
    REMOVED(1),
    ALL(2)
}
object RemovedStateSerializer : KSerializer<RemovedState> {

    override val descriptor = PrimitiveSerialDescriptor("RemovedState", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: RemovedState) {
        encoder.encodeInt(value.value)
    }

    override fun deserialize(decoder: Decoder): RemovedState {
        return when (decoder.decodeInt()) {
            0 -> RemovedState.ACTIVE
            1 -> RemovedState.REMOVED
            else -> RemovedState.ALL
        }
    }
}