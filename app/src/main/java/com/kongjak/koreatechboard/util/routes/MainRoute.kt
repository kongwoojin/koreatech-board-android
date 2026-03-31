package com.kongjak.koreatechboard.util.routes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.kongjak.koreatechboard.R

sealed class MainRoute(val name: String, val stringResource: Int, val icon: ImageVector) {
    data object Home : MainRoute("Home", R.string.route_home, Icons.Filled.Home)
    data object Board : MainRoute("Board", R.string.route_board, Icons.AutoMirrored.Filled.List)
    data object Settings : MainRoute("Settings", R.string.route_settings, Icons.Filled.Settings)
    data object Article : MainRoute("Article", R.string.route_article, Icons.AutoMirrored.Filled.List)
    data object Search : MainRoute("Search", R.string.route_search, Icons.Filled.Search)
    data object Notice : MainRoute("Notice", R.string.route_new_notice, Icons.Filled.Notifications)
    data object Licenses : MainRoute("Licenses", R.string.route_licenses, Icons.AutoMirrored.Filled.List)

    companion object {
        fun valueOf(name: String): MainRoute {
            return when (name.lowercase()) {
                Home.name.lowercase() -> Home
                Board.name.lowercase() -> Board
                Settings.name.lowercase() -> Settings
                Article.name.lowercase() -> Article
                Search.name.lowercase() -> Search
                Notice.name.lowercase() -> Notice
                Licenses.name.lowercase() -> Licenses
                else -> Home
            }
        }
    }
}
