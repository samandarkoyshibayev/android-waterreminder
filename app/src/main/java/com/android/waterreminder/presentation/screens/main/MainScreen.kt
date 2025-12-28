package com.android.waterreminder.presentation.screens.main

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.android.waterreminder.R
import com.android.waterreminder.presentation.screens.analysis.AnalysisScreen
import com.android.waterreminder.presentation.screens.home.HomeScreen
import com.android.waterreminder.presentation.screens.profile.ProfileScreen
import com.android.waterreminder.presentation.screens.setting.SettingScreen

class MainScreen : Screen {
    @Composable
    override fun Content() {
        MainScreenContent()
    }
}

@Composable
fun MainScreenContent() {
    TabNavigator(HomeTab) { tabNavigator ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            CurrentTab()
            AppBottomNavigationBar(
                selectIndex = tabNavigator.current.options.index.toInt(),
                onClick = { index ->
                    when (index) {
                        0 -> tabNavigator.current =
                            HomeTab
                        1 -> tabNavigator.current =
                            AnalysisTab
                        2 -> tabNavigator.current =
                            SettingTab
                        3-> tabNavigator.current =
                            ProfileTab
                    }
                }
            )
        }
    }
}


object HomeTab : Tab {
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    override fun Content() {
        HomeScreen().Content()
    }

    override val options: TabOptions
        @Composable get() {
            return TabOptions(
                index = 0u,
                title = "Home"
            )
        }
}

object AnalysisTab: Tab {
    @Composable
    override fun Content() {
        AnalysisScreen().Content()
    }

    override val options: TabOptions
        @Composable get() {
            return TabOptions(
                index = 1u,
                title = "Analysis"
            )
        }
}

object SettingTab : Tab {
    @Composable
    override fun Content() {
        SettingScreen().Content()
    }

    override val options: TabOptions
        @Composable get() {
            return TabOptions(
                index = 2u,
                title = "Setting"
            )
        }
}

object ProfileTab : Tab {
    @Composable
    override fun Content() {
        ProfileScreen().Content()
    }

    override val options: TabOptions
        @Composable get() {
            return TabOptions(
                index = 3u,
                title = "Profile"
            )
        }
}



@Composable
fun AppBottomNavigationBar(
    selectIndex: Int,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        Pair("Home", painterResource(id = R.drawable.ic_home)),
        Pair("Analysis" ,painterResource(id = R.drawable.ic_analysis)),
        Pair("Setting", painterResource(id = R.drawable.ic_settings)),
        Pair("Profile", painterResource(id = R.drawable.ic_profile)),
    )
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = item.second,
                        contentDescription = item.first,
                        tint = if(selectIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                    )
                },
                label = {
                    Text(
                        text = item.first,
                        color = if(selectIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary

                    )
                },
                selected = index == selectIndex,
                onClick = { onClick(index) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF51267D),
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color(0xFF51267D),
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun PreviewBottomBar() {
    AppBottomNavigationBar(
        selectIndex = 0,
        onClick = {}
    )
}