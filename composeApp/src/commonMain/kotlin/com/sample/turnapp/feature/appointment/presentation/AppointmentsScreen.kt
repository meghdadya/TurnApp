package com.sample.turnapp.feature.appointment.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.sample.turnapp.core.ui.theme.TurnAppTheme
import com.sample.turnapp.feature.appointment.domain.AppointmentUiModel
import com.sample.turnapp.feature.appointment.domain.AppointmentsFilter
import com.sample.turnapp.feature.appointment.presentation.components.AppointmentBottomSheet
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    viewModel: AppointmentsViewModel = koinViewModel()
) {

    val state = viewModel.currentState
    viewModel.initialData.collectAsState(Unit)

    // ---------------- DELETE SHEET STATE ----------------
    var showDeleteBottomSheet by remember { mutableStateOf(false) }
    var selectedAppointmentId by remember { mutableStateOf<Int?>(null) }
    var deleteReason by remember { mutableStateOf("") }

    // ---------------- ADD/EDIT SHEET ----------------
    var showAppointmentBottomSheet by remember { mutableStateOf(false) }
    var selectedAppointment by remember { mutableStateOf<AppointmentUiModel?>(null) }

    val deleteSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // =========================================================
    // DELETE BOTTOM SHEET
    // =========================================================
    if (showDeleteBottomSheet) {

        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            TurnAppTheme {
                ModalBottomSheet(
                    onDismissRequest = {
                        showDeleteBottomSheet = false
                        deleteReason = ""
                        selectedAppointmentId = null
                    },
                    sheetState = deleteSheetState,
                    containerColor = TurnAppTheme.colors.backgroundPrimary,
                    dragHandle = null
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = TurnAppTheme.dimens.paddingMedium,
                                vertical = TurnAppTheme.dimens.paddingLarge
                            )
                            .navigationBarsPadding()
                    ) {

                        Text(
                            text = "حذف قرار ملاقات",
                            style = TurnAppTheme.typography.title.medium,
                            color = TurnAppTheme.colors.textPrimary
                        )

                        Spacer(Modifier.height(TurnAppTheme.dimens.paddingSmall))

                        Text(
                            text = "آیا از حذف این قرار مطمئن هستید؟",
                            style = TurnAppTheme.typography.body1.medium,
                            color = TurnAppTheme.colors.textSecondary
                        )

                        Spacer(Modifier.height(TurnAppTheme.dimens.paddingLarge))

                        OutlinedTextField(
                            value = deleteReason,
                            onValueChange = { deleteReason = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("دلیل حذف") },
                            maxLines = 4
                        )

                        Spacer(Modifier.height(TurnAppTheme.dimens.paddingLarge))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            Button(
                                modifier = Modifier.weight(1f),
                                enabled = deleteReason.isNotBlank(),
                                onClick = {
                                    selectedAppointmentId?.let { id ->
                                        viewModel.setEvent(
                                            AppointmentsContract.AppointmentsUiEvent.OnDeleteClick(
                                                id = id,
                                                deleteReason = deleteReason
                                            )
                                        )
                                    }

                                    showDeleteBottomSheet = false
                                    deleteReason = ""
                                    selectedAppointmentId = null
                                }
                            ) {
                                Text("حذف")
                            }

                            OutlinedButton(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    showDeleteBottomSheet = false
                                    deleteReason = ""
                                    selectedAppointmentId = null
                                }
                            ) {
                                Text("لغو")
                            }
                        }
                    }
                }
            }
        }
    }

    // =========================================================
    // ADD / EDIT BOTTOM SHEET
    // =========================================================

    if (showAppointmentBottomSheet) {

        AppointmentBottomSheet(
            appointment = selectedAppointment,
            onDismiss = {
                showAppointmentBottomSheet = false
                selectedAppointment = null
            },
            onSave = { id, personId, startTime, endTime, title, description ->

                viewModel.setEvent(
                    AppointmentsContract.AppointmentsUiEvent.OnSaveAppointment(
                        id = id,
                        personId = personId,
                        startTime = startTime,
                        endTime = endTime,
                        title = title,
                        description = description
                    )
                )

                showAppointmentBottomSheet = false
                selectedAppointment = null
            }
        )
    }

    // =========================================================
    // MAIN UI
    // =========================================================
    Column(modifier = Modifier.fillMaxSize()) {

        HeaderSection()

        Spacer(Modifier.height(12.dp))

        SearchSection(
            query = state.searchQuery,
            onQueryChange = {
                viewModel.setEvent(
                    AppointmentsContract.AppointmentsUiEvent.OnSearchQueryChange(it)
                )
            }
        )

        Spacer(Modifier.height(TurnAppTheme.dimens.paddingMedium))

        FilterSection(
            selectedFilter = when (state.selectedFilter) {
                AppointmentsFilter.ALL -> "همه"
                AppointmentsFilter.ACTIVE -> "فعال"
                AppointmentsFilter.DELETED -> "حذف‌شده"
            },
            onFilterSelected = { value ->
                val filter = when (value) {
                    "همه" -> AppointmentsFilter.ALL
                    "فعال" -> AppointmentsFilter.ACTIVE
                    else -> AppointmentsFilter.DELETED
                }

                viewModel.setEvent(
                    AppointmentsContract.AppointmentsUiEvent.OnFilterSelected(filter)
                )
            }
        )

        Spacer(Modifier.height(TurnAppTheme.dimens.paddingMedium))

        TopSection(
            count = state.filteredAppointments.size,
            onAddClick = {
                selectedAppointment = null
                showAppointmentBottomSheet = true
            }
        )

        Spacer(Modifier.height(12.dp))

        AppointmentList(
            items = state.filteredAppointments,
            onEdit = {
                selectedAppointment = it
                showAppointmentBottomSheet = true
            },
            onDelete = {
                selectedAppointmentId = it.id
                showDeleteBottomSheet = true
            },
            onRestore = {
                viewModel.setEvent(
                    AppointmentsContract.AppointmentsUiEvent.OnRestoreClick(it.id!!)
                )
            }
        )
    }
}

@Composable
private fun AppointmentList(
    items: List<AppointmentUiModel>,
    onEdit: (AppointmentUiModel) -> Unit,
    onDelete: (AppointmentUiModel) -> Unit,
    onRestore: (AppointmentUiModel) -> Unit
) {

    LazyColumn {

        items(items) { item ->

            AppointmentItem(
                item = item,
                onEdit = onEdit,
                onDelete = onDelete,
                onRestore = onRestore
            )

            HorizontalDivider()
        }
    }
}

@Composable
private fun AppointmentItem(
    item: AppointmentUiModel,
    onEdit: (AppointmentUiModel) -> Unit,
    onDelete: (AppointmentUiModel) -> Unit,
    onRestore: (AppointmentUiModel) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(
                if (item.deleted)
                    TurnAppTheme.colors.backgroundRedContainer
                else
                    TurnAppTheme.colors.backgroundPrimary
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(modifier = Modifier.weight(1f)) {

            Text(item.title, fontWeight = FontWeight.Bold)

            Text(item.personName, style = TurnAppTheme.typography.body1.light)

            Text("${item.startTimePersian} → ${item.endTimePersian}")
        }

        if (item.deleted) {

            IconButton(onClick = { onRestore(item) }) {
                Icon(Icons.Rounded.Refresh, contentDescription = null)
            }

        } else {

            IconButton(onClick = { onEdit(item) }) {
                Icon(Icons.Rounded.Edit, contentDescription = null)
            }

            IconButton(onClick = { onDelete(item) }) {
                Icon(Icons.Rounded.Delete, contentDescription = null)
            }
        }
    }
}

@Composable
private fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(TurnAppTheme.colors.active)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = "لیست قرار ملاقات‌ها",
                style = TurnAppTheme.typography.title.medium,
                color = TurnAppTheme.colors.white
            )

            Text(
                text = "مدیریت نوبت‌ها",
                style = TurnAppTheme.typography.body1.medium,
                color = TurnAppTheme.colors.white.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun SearchSection(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = TurnAppTheme.dimens.paddingMedium),
        placeholder = { Text("جستجو بر اساس عنوان یا شخص") },
        singleLine = true
    )
}

@Composable
private fun TopSection(
    count: Int,
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = TurnAppTheme.dimens.paddingMedium),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Surface(
            modifier = Modifier.clickable { onAddClick() },
            color = TurnAppTheme.colors.primaryAction
        ) {
            Text(
                text = "افزودن",
                modifier = Modifier.padding(12.dp),
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = "قرارها ($count)",
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun FilterSection(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = TurnAppTheme.dimens.paddingMedium
            ),
        horizontalArrangement = Arrangement.spacedBy(
            TurnAppTheme.dimens.paddingSmall
        )
    ) {

        listOf(
            "همه",
            "حذف‌شده",
            "فعال"
        ).forEach { item ->

            val selected = selectedFilter == item

            FilterChip(
                selected = selected,
                onClick = {
                    onFilterSelected(item)
                },
                label = {
                    Text(
                        text = item,
                        style = TurnAppTheme.typography.body1.medium
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = TurnAppTheme.colors.active,
                    selectedLabelColor = TurnAppTheme.colors.primaryAction,
                    containerColor = TurnAppTheme.colors.backgroundOnElevatedItems,
                    labelColor = TurnAppTheme.colors.textSecondary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selected,
                    borderColor = TurnAppTheme.colors.transparent,
                    selectedBorderColor = TurnAppTheme.colors.transparent
                )
            )
        }
    }
}