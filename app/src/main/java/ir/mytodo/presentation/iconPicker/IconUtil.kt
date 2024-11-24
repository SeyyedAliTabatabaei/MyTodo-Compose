package ir.mytodo.presentation.iconPicker

import android.content.Context
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import java.io.BufferedReader
import java.io.InputStreamReader

object IconUtil {

    var iconsList : List<IconItem> = listOf()
    val defaultIcon = "TaskAlt"

    fun createImageVector(name: String): ImageVector? {
        return try {
            val className = "androidx.compose.material.icons.filled.${name}Kt"
            val cl = Class.forName(className)
            val method = cl.declaredMethods.first()
            method.invoke(null, Icons.Filled) as ImageVector
        } catch (ex: Exception) {
            Log.e("ImageNotFound", name)
            null
        }
    }

    fun parseIconItem(line: String): IconItem {
        val splitted = line.split(",")
        val id = splitted[0]
        val name = splitted[1]
        val image = createImageVector(id)

        return IconItem(0 , name, image)
    }

    fun getNamesIcons(context: Context): List<String> {
        val inputStream = context.assets.open("icons-names.txt")
        val reader = BufferedReader(InputStreamReader(inputStream))
        val lines = reader.readLines()
        reader.close()
        return lines
    }

    fun findIconItemById(id : Int) : IconItem {
        return iconsList.find { it.id == id } ?: IconItem(-1 , defaultIcon , createImageVector(defaultIcon) , false)
    }

    fun findIconItemByName(name : String) : IconItem {
        return iconsList.find { it.name == name } ?: IconItem(-1 , defaultIcon , createImageVector(defaultIcon) , false)
    }

    data class IconItem(
        var id: Int = 0,
        var name: String = "",
        var image: ImageVector? = null,
        var selected: Boolean = false
    )

}
