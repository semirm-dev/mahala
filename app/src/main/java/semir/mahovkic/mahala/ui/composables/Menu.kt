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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

const val MenuEmptySearchBy = ""
const val MenuSearchByPlaceholder = "Search by party"
const val EmptyFilterByParty = "All parties"
const val EmptyFilterByGroup = "All levels"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T>DropdownMenuView(
    items: List<DropDownMenuItem<T>>,
    selectedItem: MutableState<DropDownMenuItem<T>>,
    modifier: Modifier = Modifier,
    searchablePlaceholder: String = "",
    searchable: Boolean = false,
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
                modifier = modifier,
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded.value,
                    onExpandedChange = {
                        expanded.value = !expanded.value
                    },
                ) {
                    TextField(
                        value = selectedItem.value.value,
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
                                    !item.value.lowercase().contains(searchBy.value.lowercase())
                                ) {
                                    return@forEach
                                }
                            }

                            DropdownMenuItem(
                                onClick = {
                                    selectedItem.value = item
                                    searchBy.value = MenuEmptySearchBy
                                    expanded.value = false
                                },
                                content = {
                                    Text(text = item.value)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

data class DropDownMenuItem<T>(
    val id: T,
    val value: String
)