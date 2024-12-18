package ir.mytodo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ir.mytodo.presentation.iconPicker.IconUtil

@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initIcons()
    }

    private fun initIcons() {
        IconUtil.apply {
            iconsList = getNamesIcons(this@MyApp)
                .mapIndexed { index, iconItem ->
                    parseIconItem(iconItem).copy(id = index)
                }
        }
    }
}
