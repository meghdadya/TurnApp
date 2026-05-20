package com.sample.turnapp.feature.appointment.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sample.turnapp.feature.people.domain.model.PersonUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeopleBottomSheet(
    people: List<PersonUiModel>,
    onDismiss: () -> Unit,
    onPersonSelected: (PersonSelectionResult) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var query by remember { mutableStateOf("") }

    val filteredList = remember(query, people) {
        people.filter {
            it.firstName.contains(query, true) ||
                    it.lastName.contains(query, true) ||
                    it.nationalCode.contains(query, true)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            // 🔎 Search
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search name or national ID") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            // 📋 List
            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.8f)
            ) {
                items(filteredList) { person ->

                    PersonRow(
                        person = person,
                        onClick = {
                            onPersonSelected(
                                PersonSelectionResult(
                                    id = person.id,
                                    fullName = "${person.firstName} ${person.lastName}",
                                    nationalCode = person.nationalCode
                                )
                            )
                            onDismiss()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PersonRow(
    person: PersonUiModel,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    ) {

        Text(
            text = "${person.firstName} ${person.lastName}",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "National ID: ${person.nationalCode}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}