package com.kongjak.koreatechboard.ui.components.preference

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kongjak.koreatechboard.ui.theme.preferenceSummary
import com.kongjak.koreatechboard.ui.theme.preferenceTitle

@Composable
fun BasicPreference(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
    content: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier.clickable { onClick() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(modifier = Modifier.weight(1f), text = title, style = MaterialTheme.typography.preferenceTitle)
            Column(horizontalAlignment = Alignment.End) {
                if (content != null) {
                    content()
                }
            }
        }
    }
}

@Composable
fun BasicPreference(
    modifier: Modifier = Modifier,
    title: String,
    summary: String,
    onClick: () -> Unit,
    content: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier.clickable { onClick() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(text = title, style = MaterialTheme.typography.preferenceTitle)
                Text(text = summary, style = MaterialTheme.typography.preferenceSummary)
            }
            Column(horizontalAlignment = Alignment.End) {
                if (content != null) {
                    content()
                }
            }
        }
    }
}

@Composable
fun BasicPreference(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    content: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier.clickable { onClick() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(modifier = Modifier.weight(1f), text = title, style = MaterialTheme.typography.preferenceTitle)
            Column(horizontalAlignment = Alignment.End) {
                if (content != null) {
                    content()
                }
            }
        }
    }
}

@Composable
fun BasicPreference(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    summary: String,
    onClick: () -> Unit,
    content: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier.clickable { onClick() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.padding(end = 8.dp)
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.preferenceTitle
                )
                Text(text = summary, style = MaterialTheme.typography.preferenceSummary)
            }
            Column(horizontalAlignment = Alignment.End) {
                if (content != null) {
                    content()
                }
            }
        }
    }
}
