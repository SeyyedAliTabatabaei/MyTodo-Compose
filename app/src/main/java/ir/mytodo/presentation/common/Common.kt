package ir.mytodo.presentation.common

import android.widget.Toolbar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ir.mytodo.R
import ir.mytodo.presentation.ui.theme.CustomColorPalette
import ir.mytodo.presentation.ui.theme.MyTodoTheme
import ir.mytodo.presentation.utilities.CustomComposePreview
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun CustomEditText(
    defaultValue : String = "" ,
    lableText : String = "" ,
    minLine : Int = 1,
    paddingTop : Dp = 10.dp ,
    singleLine : Boolean = false ,
    errorMessage : String = "" ,
    isError : Boolean = false ,
    onValueChange : (String) -> Unit = {}
) {

    Column {
        OutlinedTextField(
            value = defaultValue,
            onValueChange = onValueChange ,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp, start = 16.dp, top = paddingTop, bottom = 0.dp),
            shape = RoundedCornerShape(16.dp) ,
            textStyle = MaterialTheme.typography.bodyMedium
                .copy(color = CustomColorPalette.textColor) ,
            label = {
                Text(
                    text = lableText ,
                    style = MaterialTheme.typography.bodyMedium
                )
            } ,
            minLines = minLine ,
            singleLine = singleLine ,
            isError = isError
        )

        AnimatedVisibility(visible = isError) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp, start = 16.dp, top = 5.dp),
                text = errorMessage ,
                style = MaterialTheme.typography.bodyMedium ,
                color = Color.Red
            )
        }

    }

}


@Composable
fun CustomToolbar(
    title : String ,
    backIconClick : () -> Unit = {}
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            modifier = Modifier.clickable {
                backIconClick()
            } ,
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back" ,
            colorFilter = ColorFilter.tint(CustomColorPalette.textColor)
        )

        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp),
            text = title ,
            style = MaterialTheme.typography.titleMedium
        )

    }

}

@CustomComposePreview
@Composable
fun CustomToolbarPreview() {
    MyTodoTheme {
        Column {
            CustomToolbar(stringResource(id = R.string.add_task))
            CustomEditText(
                defaultValue = "" ,
                lableText = "Your title" ,
                isError = true ,
                errorMessage = "Text error"
            )
        }
    }
    
}
