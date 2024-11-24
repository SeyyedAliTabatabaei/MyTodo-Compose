package ir.mytodo.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp

val FontRegular = androidx.compose.ui.text.font.Font(ir.mytodo.R.font.iransanse_regular)
val FontBold = androidx.compose.ui.text.font.Font(ir.mytodo.R.font.iransanse_bold)
val FontMedium = androidx.compose.ui.text.font.Font(ir.mytodo.R.font.iransanse_medium)

// Set of Material typography styles to start with
val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily(FontBold) ,
        fontSize = 20.sp ,
    ) ,
    titleMedium = TextStyle(
        fontFamily = FontFamily(FontBold) ,
        fontSize = 16.sp
    ) ,
    titleSmall = TextStyle(
        fontFamily = FontFamily(FontBold) ,
        fontSize = 14.sp
    ) ,

    bodyLarge = TextStyle(
        fontFamily = FontFamily(FontMedium) ,
        fontSize = 16.sp
    ) ,
    bodyMedium = TextStyle(
        fontFamily = FontFamily(FontMedium) ,
        fontSize = 14.sp
    ) ,
    bodySmall = TextStyle(
        fontFamily = FontFamily(FontMedium) ,
        fontSize = 12.sp
    )
)