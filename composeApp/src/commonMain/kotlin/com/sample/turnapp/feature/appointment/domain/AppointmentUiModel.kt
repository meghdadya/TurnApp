package com.sample.turnapp.feature.appointment.domain

import com.sample.turnapp.core.domain.model.DomainModel
import io.github.faridsolgi.persiandatetime.extensions.toDateTimeString
import io.github.faridsolgi.persiandatetime.extensions.toPersianDateTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime


data class AppointmentUiModel(
    val id: Int?,
    val personId: Int,
    val personName: String,
    val phoneNumber: String,
    val startTime: String,
    val endTime: String,
    val title: String,
    val description: String,
    val deleted: Boolean,
    val deleteReason: String? = null,
    val deleteTime: String? = null
) : DomainModel {

    val startTimePersian: String
        get() = startTime.toPersian()

    val endTimePersian: String
        get() = endTime.toPersian()
}

@OptIn(ExperimentalTime::class)
fun String.toPersian(): String {
    return try {
        val localDateTime = LocalDateTime.parse(this)
        val instant = localDateTime.toInstant(TimeZone.currentSystemDefault())
        val persianDateTime = instant
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .toPersianDateTime()
        val result = persianDateTime.toDateTimeString()
        result
    } catch (e: Exception) {
        println("ERROR parsing date: input=$this error=${e.message}")
        ""
    }
}