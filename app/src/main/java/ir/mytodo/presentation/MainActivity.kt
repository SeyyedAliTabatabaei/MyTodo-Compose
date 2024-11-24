package ir.mytodo.presentation

import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import ir.mytodo.R
import ir.mytodo.presentation.home.HomeViewModel
import ir.mytodo.presentation.navGraphs.SetupNavGraph
import ir.mytodo.presentation.taskDetails.TaskDetailsScreen
import ir.mytodo.presentation.ui.theme.MyTodoTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTodoTheme {
                val systemUiController = rememberSystemUiController()
                val systemUiColor = MaterialTheme.colorScheme.background
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = systemUiColor
                    )
                }

                SetupNavGraph(navController = rememberNavController())
            }
        }
    }
}