package com.sample.turnapp.feature.appointment.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
            people = state.people,
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

    LazyColumn(Modifier.padding(horizontal = TurnAppTheme.dimens.paddingSmall)) {

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

    val backgroundColor = if (item.deleted) {
        TurnAppTheme.colors.backgroundRedContainer
    } else {
        TurnAppTheme.colors.backgroundPrimary
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(
                horizontal = TurnAppTheme.dimens.paddingSemiSmall,
                vertical = TurnAppTheme.dimens.paddingSemiSmall
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Content
        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = item.title,
                style = TurnAppTheme.typography.body1.medium,
                color = TurnAppTheme.colors.textPrimary,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier.height(
                    TurnAppTheme.dimens.paddingXSmall
                )
            )

            Text(
                text = item.personName,
                style = TurnAppTheme.typography.body1.light,
                color = TurnAppTheme.colors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier.height(
                    TurnAppTheme.dimens.paddingXSmall
                )
            )

            Text(
                text = "${item.startTimePersian} → ${item.endTimePersian}",
                style = TurnAppTheme.typography.body1.light,
                color = TurnAppTheme.colors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(
            modifier = Modifier.width(
                TurnAppTheme.dimens.paddingSemiSmall
            )
        )

        if (item.deleted) {

            ActionButton(
                backgroundColor = TurnAppTheme.colors.backgroundGreenContainer,
                iconTint = TurnAppTheme.colors.textGreen,
                icon = Icons.Rounded.Refresh,
                onClick = {
                    onRestore(item)
                }
            )

        } else {

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                ActionButton(
                    backgroundColor = TurnAppTheme.colors.backgroundOnElevatedItems,
                    iconTint = TurnAppTheme.colors.textPrimary,
                    icon = Icons.Rounded.Edit,
                    onClick = {
                        onEdit(item)
                    }
                )

                ActionButton(
                    backgroundColor = TurnAppTheme.colors.backgroundOnElevatedItems,
                    iconTint = TurnAppTheme.colors.textRed,
                    icon = Icons.Rounded.Delete,
                    onClick = {
                        onDelete(item)
                    }
                )
            }
        }
    }
}

@Composable
private fun ActionButton(
    backgroundColor: Color,
    iconTint: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(
                RoundedCornerShape(
                    TurnAppTheme.dimens.buttonRadiusMedium
                )
            )
            .background(backgroundColor)
            .border(
                width = TurnAppTheme.dimens.border,
                color = TurnAppTheme.colors.separatorPrimary,
                shape = RoundedCornerShape(
                    TurnAppTheme.dimens.buttonRadiusMedium
                )
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(18.dp)
        )
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
        shape = RoundedCornerShape(
            TurnAppTheme.dimens.buttonRadiusLarge
        ),
        placeholder = { Text("جستجو بر اساس عنوان یا شخص") },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = TurnAppTheme.colors.textSecondary
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = TurnAppTheme.colors.backgroundElevatedItems,
            unfocusedContainerColor = TurnAppTheme.colors.backgroundElevatedItems,
            disabledContainerColor = TurnAppTheme.colors.backgroundElevatedItems,
            focusedBorderColor = TurnAppTheme.colors.active,
            unfocusedBorderColor = TurnAppTheme.colors.separatorPrimary,
            cursorColor = TurnAppTheme.colors.active,
            focusedTextColor = TurnAppTheme.colors.textPrimary,
            unfocusedTextColor = TurnAppTheme.colors.textPrimary
        )
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
            modifier = Modifier.clickable{
                onAddClick()
            },
            color = TurnAppTheme.colors.primaryAction,
            shape = RoundedCornerShape(
                TurnAppTheme.dimens.buttonRadiusMedium
            )
        ) {

            Text(
                text = "افزودن",
                modifier = Modifier.padding(
                    horizontal = TurnAppTheme.dimens.paddingMedium,
                    vertical = TurnAppTheme.dimens.paddingSmall
                ),
                color = TurnAppTheme.colors.white,
                style = TurnAppTheme.typography.body1.medium,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = "قرارها ($count)",
            style = TurnAppTheme.typography.body1.medium,
            color = TurnAppTheme.colors.textPrimary,
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