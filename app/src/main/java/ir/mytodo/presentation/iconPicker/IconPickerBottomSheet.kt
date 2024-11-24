package ir.mytodo.presentation.iconPicker

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.mytodo.R
import ir.mytodo.presentation.common.CustomButtonPrimary
import ir.mytodo.presentation.common.CustomEditText
import ir.mytodo.presentation.ui.theme.CustomColorPalette
import ir.mytodo.presentation.ui.theme.CustomColorsPalette
import ir.mytodo.presentation.utilities.CustomComposePreview
import kotlinx.coroutines.launch
import java.io.BufferedReader
//import java.io.BufferedReader
import java.io.InputStreamReader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconPickerBottomSheet(
    state : SheetState,
    dismissDialog : (IconUtil.IconItem) -> Unit
) {
    val scope = rememberCoroutineScope()
    val selectedItemId = remember{ mutableIntStateOf(0) }
    var searchText by remember { mutableStateOf("") }
    val filteredIconList = remember(searchText) {
        IconUtil.iconsList.filter {  iconItem ->
            iconItem.name.contains(searchText, ignoreCase = true)
        }
    }

    ModalBottomSheet(
        sheetState = state,
        onDismissRequest = { dismissDialog(IconUtil.findIconItemById(selectedItemId.intValue)) } ,
    ) {

        CustomEditText(
            defaultValue = searchText ,
            lableText = stringResource(id = R.string.search) ,
            singleLine = true ,
            paddingTop = 10.dp,
            onValueChange = { searchText = it}
        )

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 20.dp) ,
            columns = GridCells.Adaptive(50.dp) ,
        ) {
            items(
                items = filteredIconList ,
                key = { iconItem -> iconItem.id},
            ) { iconItem ->
                val isSelected = selectedItemId.intValue == iconItem.id
                iconItem.image?.let { iconVector ->
                    Image(
                        modifier = Modifier
                            .padding(5.dp)
                            .size(50.dp)
                            .clickable {
                                selectedItemId.intValue = iconItem.id
                            },
                        imageVector = iconVector,
                        contentDescription = "Icon" ,
                        colorFilter = ColorFilter.tint(getIconColor(isSelected, CustomColorPalette))
                    )
                }
            }
        }

        CustomButtonPrimary(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp, start = 16.dp, bottom = 20.dp, top = 10.dp),
            title = stringResource(id = R.string.confirm)
        ) {
            scope.launch {
                dismissDialog(IconUtil.findIconItemById(selectedItemId.intValue))
                state.hide()
            }
        }

    }



}


@Composable
private fun getIconColor(
    isSelected : Boolean,
    CustomColorPalette: CustomColorsPalette
) = if (isSelected)
    MaterialTheme.colorScheme.primary
else
    CustomColorPalette.textColor





//@OptIn(ExperimentalMaterial3Api::class)
//@CustomComposePreview
//@Composable
//fun IconPickerBottomSheetPreview() {
//    MaterialTheme {
//        IconPickerBottomSheet(state =  rememberModalBottomSheetState()) {
//        }
//    }
//
//}
