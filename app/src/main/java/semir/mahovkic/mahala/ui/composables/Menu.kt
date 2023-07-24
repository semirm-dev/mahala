package semir.mahovkic.mahala.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

const val MenuEmptySearchBy = ""

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuView(
    items: List<String>,
    filterBy: MutableState<String>,
    searchablePlaceholder: String = "",
    searchable: Boolean = false
) {
    val expanded = remember { mutableStateOf(false) }
    val searchBy = remember { mutableStateOf(MenuEmptySearchBy) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                shadowElevation = 2.dp,
                modifier = Modifier.align(Alignment.BottomEnd),
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded.value,
                    onExpandedChange = {
                        expanded.value = !expanded.value
                    },
                ) {
                    TextField(
                        value = filterBy.value,
                        onValueChange = {},
                        readOnly = true,
                        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .width(170.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        if (searchable) {
                            SearchView(searchBy, searchablePlaceholder)
                        }

                        items.forEach { item ->
                            if (searchable) {
                                if (searchBy.value.isNotEmpty() &&
                                    !item.lowercase().contains(searchBy.value.lowercase())
                                ) {
                                    return@forEach
                                }
                            }

                            DropdownMenuItem(
                                onClick = {
                                    filterBy.value = item
                                    searchBy.value = MenuEmptySearchBy
                                    expanded.value = false
                                },
                                content = {
                                    Text(text = item)
                                },
                            )
                        }
                    }
                }
            }

        }
    }
}