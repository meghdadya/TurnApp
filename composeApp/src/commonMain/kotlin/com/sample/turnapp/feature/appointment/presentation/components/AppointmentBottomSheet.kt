package com.sample.turnapp.feature.appointment.presentation.components


// IMPORTS FROM LIBRARY
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.sample.turnapp.core.ui.theme.TurnAppTheme
import com.sample.turnapp.feature.appointment.domain.AppointmentUiModel
import io.github.faridsolgi.date_picker.view.PersianDatePicker
import io.github.faridsolgi.date_picker.view.rememberPersianDatePickerState
import io.github.faridsolgi.persiandatetime.extensions.toDateString
import io.github.faridsolgi.persiandatetime.extensions.toEpochMilliseconds
import io.github.faridsolgi.share.PersianDatePickerDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentBottomSheet(
    appointment: AppointmentUiModel?,
    onDismiss: () -> Unit,
    onSave: (
        id: Int?,
        personId: Int,
        startTime: Long,
        endTime: Long,
        title: String,
        description: String
    ) -> Unit
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var title by remember(appointment) {
        mutableStateOf(appointment?.title ?: "")
    }

    var description by remember(appointment) {
        mutableStateOf(appointment?.description ?: "")
    }

    var personId by remember(appointment) {
        mutableStateOf(appointment?.personId?.toString() ?: "")
    }

    // UNIX TIME
    var startTime by remember {
        mutableStateOf(
            appointment?.startTime ?: 0.0
        )
    }

    var endTime by remember {
        mutableStateOf(
            appointment?.endTime ?: 0.0
        )
    }

    // DIALOG STATE
    var showStartDialog by remember {
        mutableStateOf(false)
    }

    var showEndDialog by remember {
        mutableStateOf(false)
    }

    // PICKER STATE
    val startDateState = rememberPersianDatePickerState()

    val endDateState = rememberPersianDatePickerState()

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = TurnAppTheme.colors.backgroundPrimary,
            dragHandle = null
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding()
            ) {

                Text(
                    text = if (appointment == null)
                        "افزودن قرار"
                    else
                        "ویرایش قرار",
                    style = TurnAppTheme.typography.title.medium
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    label = {
                        Text("عنوان")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    label = {
                        Text("توضیحات")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = personId,
                    onValueChange = {
                        personId = it
                    },
                    label = {
                        Text("شناسه شخص")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                // START DATE
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showStartDialog = true }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "شروع", style = MaterialTheme.typography.labelMedium)
                        Text(
                            text = startDateState.selectedDate?.toDateString() ?: "",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    IconButton(onClick = { showStartDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

// END DATE
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showEndDialog = true }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "پایان", style = MaterialTheme.typography.labelMedium)
                        Text(
                            text = endDateState.selectedDate?.toDateString() ?: "",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    IconButton(onClick = { showEndDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            val startTime: Long =
                                startDateState.selectedDate?.toEpochMilliseconds()?.div(1000) ?: 0L

                            val endTime: Long =
                                endDateState.selectedDate?.toEpochMilliseconds()?.div(1000) ?: 0L

                            onSave(
                                appointment?.id,
                                personId.toIntOrNull() ?: 0,
                                startTime,
                                endTime,
                                title,
                                description
                            )

                        }
                    ) {

                        Text("ذخیره")
                    }

                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = onDismiss
                    ) {

                        Text("لغو")
                    }
                }
            }
        }

        // START DATE PICKER
        if (showStartDialog) {

            PersianDatePickerDialog(
                onDismissRequest = {
                    showStartDialog = false
                },
                confirmButton = {

                    TextButton(
                        onClick = {

                            startTime =
                                startDateState.selectedDate
                                    ?.toEpochMilliseconds()
                                    ?.toDouble()
                                    ?: 0.0

                            showStartDialog = false
                        }
                    ) {

                        Text("تایید")
                    }
                }
            ) {

                PersianDatePicker(
                    state = startDateState
                )
            }
        }

        // END DATE PICKER
        if (showEndDialog) {

            PersianDatePickerDialog(
                onDismissRequest = {
                    showEndDialog = false
                },
                confirmButton = {

                    TextButton(
                        onClick = {

                            endTime =
                                endDateState.selectedDate
                                    ?.toEpochMilliseconds()
                                    ?.toDouble()
                                    ?: 0.0

                            showEndDialog = false
                        }
                    ) {

                        Text("تایید")
                    }
                }
            ) {

                PersianDatePicker(
                    state = endDateState
                )
            }
        }
    }
}
