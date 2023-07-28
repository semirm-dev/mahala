package semir.mahovkic.mahala.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import semir.mahovkic.mahala.ui.theme.LightPurple

const val MenuEmptySearchBy = ""
const val MenuSearchByPlaceholder = "Search by party"
const val EmptyFilterByParty = "All parties"
const val EmptyFilterByGroup = "All levels"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownMenuView(
    items: List<DropDownMenuItem<T>>,
    selectedItem: MutableState<DropDownMenuItem<T>>,
    modifier: Modifier = Modifier,
    searchablePlaceholder: String = "",
    searchable: Boolean = false,
    shape: Shape = RoundedCornerShape(50.dp),
    backgroundColor: Color = LightPurple,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
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
                shape = shape,
                shadowElevation = 0.dp,
                modifier = modifier,
                border = BorderStroke(0.3.dp, MaterialTheme.colorScheme.primary)
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
                        textStyle = TextStyle(color = textColor, fontSize = 18.sp),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .width(170.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = backgroundColor,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
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