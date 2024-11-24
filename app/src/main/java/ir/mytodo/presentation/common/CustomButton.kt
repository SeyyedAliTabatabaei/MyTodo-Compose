package ir.mytodo.presentation.common

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ir.mytodo.presentation.ui.theme.CustomColorPalette
import ir.mytodo.presentation.ui.theme.MyTodoTheme
import ir.mytodo.presentation.utilities.CustomComposePreview
import kotlinx.coroutines.launch
import timber.log.Timber

const val DURATION_BUTTON_ANIMATION = 200

@Composable
fun CustomButtonPrimary(
    modifier: Modifier = Modifier,
    title : String,
    isLoading : Boolean = false ,
    isEnable : Boolean = true ,
    onClickButton : () -> Unit
) {

    val animatedButtonVisible by animateFloatAsState(
        targetValue = if (isLoading) 0f else 1f,
        animationSpec = tween(durationMillis = DURATION_BUTTON_ANIMATION),
        label = "Size"
    )



    Box(
        modifier = modifier
            .height(50.dp),
        contentAlignment = Alignment.Center
    ) {
        FilledTonalButton(
            onClick = onClickButton ,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    this.scaleX = animatedButtonVisible
                    this.scaleY = animatedButtonVisible
                }
                .alpha(if (isEnable) 1f else 0.6f),
            colors = ButtonDefaults.buttonColors(
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            ) ,
            shape = RoundedCornerShape(16.dp) ,
            enabled = isEnable
        ) {
            Text(
                text = title ,
                style = MaterialTheme.typography.titleMedium ,
                color = CustomColorPalette.textColor
            )
        }

        CustomCircleProgressBar(isLoading)
    }
}

@Composable
fun CustomCircleProgressBar(isShow : Boolean) {
    val animatedProgressScaleVisible by animateFloatAsState(
        targetValue = if (isShow) 1f else 0f,
        animationSpec = tween(delayMillis = DURATION_BUTTON_ANIMATION, durationMillis = DURATION_BUTTON_ANIMATION),
        label = "Size progress"
    )

    val rotation = remember { Animatable(0f) }
    val progress = remember { Animatable(0f) }

    CircularProgressIndicator(
        modifier = Modifier
            .size(50.dp)
            .graphicsLayer {
                this.rotationZ = rotation.value
                this.scaleX = animatedProgressScaleVisible
                this.scaleY = animatedProgressScaleVisible
            },
        progress = progress.value ,
        strokeWidth = 6.dp,
        strokeCap = StrokeCap.Round
    )

    LaunchedEffect(Unit) {
        launch {
            rotation.animateTo(
                targetValue = 360f ,
                animationSpec = infiniteRepeatable(
                    repeatMode = RepeatMode.Restart ,
                    animation = tween(500 , easing = LinearEasing)
                )
            )
        }

        launch {
            progress.animateTo(
                targetValue = 1f ,
                animationSpec = infiniteRepeatable(
                    repeatMode = RepeatMode.Reverse ,
                    animation = tween(500 , easing = LinearEasing)
                )
            )
        }
    }
}

@CustomComposePreview
@Composable
fun CustomPrimaryButtonPreview() {
    MyTodoTheme {
        CustomButtonPrimary(
            modifier = Modifier.fillMaxWidth(),
            title = "Save"
        ) {

        }
    }
}
