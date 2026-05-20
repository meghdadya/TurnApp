@file:OptIn(ExperimentalMaterial3Api::class)

package com.sample.turnapp.feature.people.presentation

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
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.sample.turnapp.core.ui.theme.TurnAppTheme
import com.sample.turnapp.feature.people.domain.model.PeopleFilter
import com.sample.turnapp.feature.people.domain.model.PersonUiModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeopleScreen(
    viewModel: PeopleViewModel = koinViewModel()
) {

    val state = viewModel.currentState
    viewModel.initialData.collectAsState(Unit)

    // ---------------- DELETE SHEET STATE ----------------
    var showDeleteBottomSheet by remember { mutableStateOf(false) }
    var selectedPersonId by remember { mutableStateOf<Int?>(null) }
    var deleteReason by remember { mutableStateOf("") }

    // ---------------- ADD/EDIT SHEET STATE ----------------
    var showPersonBottomSheet by remember { mutableStateOf(false) }
    var selectedPerson by remember { mutableStateOf<PersonUiModel?>(null) }

    // ---------------- DELETE SHEET ----------------
    val deleteSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (showDeleteBottomSheet) {

        CompositionLocalProvider(
            LocalLayoutDirection provides LayoutDirection.Rtl
        ) {
            TurnAppTheme {
                ModalBottomSheet(
                    onDismissRequest = {
                        showDeleteBottomSheet = false
                        deleteReason = ""
                        selectedPersonId = null
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
                            text = "حذف شخص",
                            style = TurnAppTheme.typography.title.medium,
                            color = TurnAppTheme.colors.textPrimary
                        )

                        Spacer(Modifier.height(TurnAppTheme.dimens.paddingSmall))

                        Text(
                            text = "آیا مطمئن هستید که می‌خواهید این شخص را حذف کنید؟",
                            style = TurnAppTheme.typography.body1.medium,
                            color = TurnAppTheme.colors.textSecondary
                        )

                        Spacer(Modifier.height(TurnAppTheme.dimens.paddingLarge))

                        OutlinedTextField(
                            value = deleteReason,
                            onValueChange = { deleteReason = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("دلیل حذف") },
                            maxLines = 4,
                            shape = RoundedCornerShape(
                                TurnAppTheme.dimens.buttonRadiusLarge
                            )
                        )

                        Spacer(Modifier.height(TurnAppTheme.dimens.paddingLarge))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                TurnAppTheme.dimens.paddingMedium
                            )
                        ) {

                            Button(
                                onClick = {
                                    selectedPersonId?.let { id ->
                                        viewModel.setEvent(
                                            PeopleContract.PeopleUiEvent.OnDeleteClick(
                                                id = id,
                                                deleteReason = deleteReason
                                            )
                                        )
                                    }
                                    showDeleteBottomSheet = false
                                    deleteReason = ""
                                    selectedPersonId = null
                                },
                                modifier = Modifier.weight(1f),
                                enabled = deleteReason.isNotBlank(),
                                shape = RoundedCornerShape(
                                    TurnAppTheme.dimens.buttonRadiusLarge
                                )
                            ) {
                                Text("بله، حذف")
                            }

                            OutlinedButton(
                                onClick = {
                                    showDeleteBottomSheet = false
                                    deleteReason = ""
                                    selectedPersonId = null
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(
                                    TurnAppTheme.dimens.buttonRadiusLarge
                                )
                            ) {
                                Text("خیر")
                            }
                        }
                    }
                }
            }
        }
    }

    // ---------------- ADD / EDIT SHEET ----------------
    if (showPersonBottomSheet) {

        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )

        var firstName by remember { mutableStateOf(selectedPerson?.firstName ?: "") }
        var lastName by remember { mutableStateOf(selectedPerson?.lastName ?: "") }
        var socialNumber by remember { mutableStateOf(selectedPerson?.nationalCode ?: "") }
        var phoneNumber by remember { mutableStateOf(selectedPerson?.phoneNumber ?: "") }

        CompositionLocalProvider(
            LocalLayoutDirection provides LayoutDirection.Rtl
        ) {
            ModalBottomSheet(
                onDismissRequest = {
                    showPersonBottomSheet = false
                    selectedPerson = null
                },
                sheetState = sheetState,
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
                        text = if (selectedPerson == null) "افزودن شخص" else "ویرایش شخص",
                        style = TurnAppTheme.typography.title.medium,
                        color = TurnAppTheme.colors.textPrimary
                    )

                    Spacer(Modifier.height(TurnAppTheme.dimens.paddingMedium))

                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("نام") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(TurnAppTheme.dimens.paddingMedium))

                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("نام خانوادگی") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(TurnAppTheme.dimens.paddingMedium))

                    OutlinedTextField(
                        value = socialNumber,
                        onValueChange = { socialNumber = it },
                        label = { Text("کد ملی") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(TurnAppTheme.dimens.paddingMedium))

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("شماره تلفن") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(TurnAppTheme.dimens.paddingLarge))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            TurnAppTheme.dimens.paddingMedium
                        )
                    ) {

                        Button(
                            onClick = {
                                viewModel.setEvent(
                                    PeopleContract.PeopleUiEvent.OnSavePerson(
                                        id = selectedPerson?.id?.toLong() ?: 0,
                                        firstName = firstName,
                                        lastName = lastName,
                                        socialNumber = socialNumber,
                                        phoneNumber = phoneNumber
                                    )
                                )

                                showPersonBottomSheet = false
                                selectedPerson = null
                            },
                            modifier = Modifier.weight(1f),
                            enabled =
                                firstName.isNotBlank() &&
                                        lastName.isNotBlank() &&
                                        socialNumber.isNotBlank() &&
                                        phoneNumber.isNotBlank(),
                            shape = RoundedCornerShape(
                                TurnAppTheme.dimens.buttonRadiusLarge
                            )
                        ) {
                            Text(if (selectedPerson == null) "افزودن" else "ویرایش")
                        }

                        OutlinedButton(
                            onClick = {
                                showPersonBottomSheet = false
                                selectedPerson = null
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("لغو")
                        }
                    }
                }
            }
        }
    }

    // ---------------- MAIN UI ----------------
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        HeaderSection()

        Spacer(Modifier.height(TurnAppTheme.dimens.paddingMedium))

        SearchSection(
            query = state.searchQuery,
            onQueryChange = {
                viewModel.setEvent(
                    PeopleContract.PeopleUiEvent.OnSearchQueryChange(it)
                )
            }
        )

        Spacer(Modifier.height(TurnAppTheme.dimens.paddingMedium))

        FilterSection(
            selectedFilter = when (state.selectedFilter) {
                PeopleFilter.ALL -> "همه"
                PeopleFilter.ACTIVE -> "فعال"
                PeopleFilter.DELETED -> "حذف‌شده"
            },
            onFilterSelected = { value ->
                val filter = when (value) {
                    "همه" -> PeopleFilter.ALL
                    "فعال" -> PeopleFilter.ACTIVE
                    else -> PeopleFilter.DELETED
                }

                viewModel.setEvent(
                    PeopleContract.PeopleUiEvent.OnFilterSelected(filter)
                )
            }
        )

        Spacer(Modifier.height(TurnAppTheme.dimens.paddingMedium))

        TopSection(
            count = state.filteredPeople.size,
            onAddClick = {
                selectedPerson = null
                showPersonBottomSheet = true
            }
        )

        Spacer(Modifier.height(TurnAppTheme.dimens.paddingMedium))

        PeopleList(
            people = state.filteredPeople,
            onEditClick = { person ->
                selectedPerson = person
                showPersonBottomSheet = true
            },
            onDeleteClick = { person ->
                selectedPersonId = person.id
                showDeleteBottomSheet = true
            },
            onRestoreClick = { person ->
                viewModel.setEvent(
                    PeopleContract.PeopleUiEvent.OnRestoreClick(
                        id = person.id
                    )
                )
            }
        )
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
            .padding(
                horizontal = TurnAppTheme.dimens.paddingMedium
            ),
        singleLine = true,
        shape = RoundedCornerShape(
            TurnAppTheme.dimens.buttonRadiusLarge
        ),
        placeholder = {
            Text(
                text = "جستجوی نام یا کد ملی",
                style = TurnAppTheme.typography.body1.medium,
                color = TurnAppTheme.colors.textSecondary
            )
        },
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
            .padding(
                horizontal = TurnAppTheme.dimens.paddingMedium
            ),
        verticalAlignment = Alignment.CenterVertically,
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
            text = "افراد ($count)",
            style = TurnAppTheme.typography.body1.medium,
            color = TurnAppTheme.colors.textPrimary,
            fontWeight = FontWeight.SemiBold
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

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "لیست افراد",
                style = TurnAppTheme.typography.title.medium,
                color = TurnAppTheme.colors.white
            )

            Spacer(
                modifier = Modifier.height(
                    TurnAppTheme.dimens.paddingXSmall
                )
            )

            Text(
                text = "مدیریت اشخاص",
                style = TurnAppTheme.typography.body1.medium,
                color = TurnAppTheme.colors.white.copy(alpha = 0.7f)
            )
        }
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



@Composable
private fun PeopleList(
    people: List<PersonUiModel>,
    onEditClick: (PersonUiModel) -> Unit = {},
    onDeleteClick: (PersonUiModel) -> Unit = {},
    onRestoreClick: (PersonUiModel) -> Unit = {}
) {
    LazyColumn(modifier = Modifier.padding(horizontal = TurnAppTheme.dimens.paddingSmall)) {

        items(people) { person ->

            PersonItem(
                person = person,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
                onRestoreClick = onRestoreClick
            )

            if (person != people.last()) {

                HorizontalDivider(
                    color = TurnAppTheme.colors.separatorPrimary
                )
            }
        }
    }
}

@Composable
private fun PersonItem(
    person: PersonUiModel,
    onEditClick: (PersonUiModel) -> Unit = {},
    onDeleteClick: (PersonUiModel) -> Unit = {},
    onRestoreClick: (PersonUiModel) -> Unit = {}
) {

    val backgroundColor = if (person.deleted) {
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

        // Avatar
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = when (person.id) {
                        1 -> TurnAppTheme.colors.backgroundBlueContainer
                        2 -> TurnAppTheme.colors.backgroundRedContainer
                        else -> TurnAppTheme.colors.backgroundYellowContainer
                    },
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = person.firstName.take(2),
                style = TurnAppTheme.typography.body1.medium,
                color = when (person.id) {
                    1 -> TurnAppTheme.colors.textBlue
                    2 -> TurnAppTheme.colors.textRed
                    else -> TurnAppTheme.colors.textYellow
                },
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.width(
                TurnAppTheme.dimens.paddingSemiSmall
            )
        )

        // Content
        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "${person.firstName} ${person.lastName}",
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
                text = person.nationalCode,
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

        if (person.deleted) {

            ActionButton(
                backgroundColor = TurnAppTheme.colors.backgroundGreenContainer,
                iconTint = TurnAppTheme.colors.textGreen,
                icon = Icons.Rounded.Refresh,
                onClick = {
                    onRestoreClick(person)
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
                        onEditClick(person)
                    }
                )

                ActionButton(
                    backgroundColor = TurnAppTheme.colors.backgroundOnElevatedItems,
                    iconTint = TurnAppTheme.colors.textRed,
                    icon = Icons.Rounded.Delete,
                    onClick = {
                        onDeleteClick(person)
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

@Preview
@Composable
private fun PeopleScreenPreview() {

    TurnAppTheme {
        PeopleScreen()
    }
}