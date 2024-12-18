package ir.mytodo.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class CustomColorsPalette(
    val textColorSecondary : Color = Color.Unspecified ,
    val textColor : Color = Color.Unspecified ,
    val backgroundCardColor : Color = Color.Unspecified ,
)

val LightCustomColorsPalette = CustomColorsPalette(
    textColorSecondary = LightTextColorSecondary ,
    textColor = LightTextColor ,
    backgroundCardColor = LightCardBackground
)

val DarkCustomColorsPalette = CustomColorsPalette(
    textColorSecondary = DarkTextColorSecondary ,
    textColor = DarkTextColor ,
    backgroundCardColor = DarkCardBackground
)
