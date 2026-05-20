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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
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
import com.sample.turnapp.feature.people.domain.model.PersonUiModel
import io.github.faridsolgi.date_picker.view.PersianDatePicker
import io.github.faridsolgi.date_picker.view.rememberPersianDatePickerState
import io.github.faridsolgi.persiandatetime.extensions.toDateString
import io.github.faridsolgi.persiandatetime.extensions.toEpochMilliseconds
import io.github.faridsolgi.share.PersianDatePickerDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentBottomSheet(
    appointment: AppointmentUiModel?,
    people: List<PersonUiModel>, // 👈 ADD THIS
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

    // ✅ REPLACED: personId string -> selectedPerson object
    var selectedPerson by remember(appointment) {
        mutableStateOf<PersonUiModel?>(
            null
        )
    }

    // optional: preselect when editing
    LaunchedEffect(appointment, people) {
        selectedPerson = people.firstOrNull { it.id == appointment?.personId }
    }

    // UNIX TIME
    var startTime by remember {
        mutableStateOf(appointment?.startTime ?: 0.0)
    }

    var endTime by remember {
        mutableStateOf(appointment?.endTime ?: 0.0)
    }

    // DIALOG STATE
    var showStartDialog by remember { mutableStateOf(false) }
    var showEndDialog by remember { mutableStateOf(false) }

    // PEOPLE SHEET
    var showPeopleSheet by remember { mutableStateOf(false) }

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

                // TITLE
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("عنوان") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(Modifier.height(16.dp))

                // DESCRIPTION
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("توضیحات") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3,
                    maxLines = 5
                )

                Spacer(Modifier.height(12.dp))

                // ✅ PERSON SELECTOR
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showPeopleSheet = true },
                        shape = RoundedCornerShape(12.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = selectedPerson?.let {
                                    "${it.firstName} ${it.lastName} - ${it.nationalCode}"
                                } ?: "انتخاب شخص",
                                modifier = Modifier.weight(1f),
                                color = if (selectedPerson == null)
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )

                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // START DATE
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {


                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showStartDialog = true },
                        shape = RoundedCornerShape(12.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = startDateState.selectedDate?.toDateString()
                                    ?: "انتخاب تاریخ شروع",
                                modifier = Modifier.weight(1f),
                                color = if (startDateState.selectedDate == null)
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )

                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

// END DATE
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {


                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showEndDialog = true },
                        shape = RoundedCornerShape(12.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = endDateState.selectedDate?.toDateString()
                                    ?: "انتخاب تاریخ پایان",
                                modifier = Modifier.weight(1f),
                                color = if (endDateState.selectedDate == null)
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )

                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null
                            )
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))

                // SAVE / CANCEL
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Button(
                        modifier = Modifier.weight(1f),
                        enabled = selectedPerson != null,
                        onClick = {

                            val startTimeUnix =
                                startDateState.selectedDate
                                    ?.toEpochMilliseconds()
                                    ?.div(1000)
                                    ?: 0L

                            val endTimeUnix =
                                endDateState.selectedDate
                                    ?.toEpochMilliseconds()
                                    ?.div(1000)
                                    ?: 0L

                            onSave(
                                appointment?.id,
                                selectedPerson?.id ?: 0, // ✅ FIXED
                                startTimeUnix,
                                endTimeUnix,
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

        // ================= START PICKER =================
        if (showStartDialog) {
            PersianDatePickerDialog(
                onDismissRequest = { showStartDialog = false },
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
                    ) { Text("تایید") }
                }
            ) {
                PersianDatePicker(state = startDateState)
            }
        }

        // ================= END PICKER =================
        if (showEndDialog) {
            PersianDatePickerDialog(
                onDismissRequest = { showEndDialog = false },
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
                    ) { Text("تایید") }
                }
            ) {
                PersianDatePicker(state = endDateState)
            }
        }

        // ================= PEOPLE BOTTOM SHEET =================
        if (showPeopleSheet) {
            PeopleBottomSheet(
                people = people,
                onDismiss = { showPeopleSheet = false },
                onPersonSelected = { result ->

                    selectedPerson = people.firstOrNull { it.id == result.id }

                    showPeopleSheet = false
                }
            )
        }
    }
}