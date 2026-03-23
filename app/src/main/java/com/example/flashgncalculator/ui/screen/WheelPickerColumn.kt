package com.example.flashgncalculator.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flashgncalculator.domain.FlashOption
import com.example.flashgncalculator.ui.theme.AppWhite
import com.example.flashgncalculator.ui.theme.MutedWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WheelPickerColumn(
    modifier: Modifier = Modifier,
    title: String,
    options: List<FlashOption>,
    selectedOption: FlashOption,
    onSelected: (FlashOption) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = AppWhite,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedOption.label,
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = AppWhite,
                    unfocusedTextColor = AppWhite,
                    focusedBorderColor = AppWhite,
                    unfocusedBorderColor = MutedWhite,
                    focusedContainerColor = androidx.compose.ui.graphics.Color(0x10111111),
                    unfocusedContainerColor = androidx.compose.ui.graphics.Color(0x10111111),
                    focusedTrailingIconColor = AppWhite,
                    unfocusedTrailingIconColor = MutedWhite
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .height(56.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option.label,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        onClick = {
                            expanded = false
                            if (option != selectedOption) {
                                onSelected(option)
                            }
                        }
                    )
                }
            }
        }
    }
}
