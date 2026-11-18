package org.umn.gearhub.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import org.umn.gearhub.ui.theme.OnYellowContainer
import org.umn.gearhub.ui.theme.YellowContainer

@Composable
fun AuthScaffold(
    title: String,
    fields: @Composable ColumnScope.() -> Unit,
    actions: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetShape = MaterialTheme.shapes.large.copy(
        bottomStart = ZeroCornerSize,
        bottomEnd = ZeroCornerSize
    )
    val yellow = MaterialTheme.colorScheme.secondary

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .imePadding()
    ) {
        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .clipToBounds()
        ) {
            Canvas(Modifier.matchParentSize()) {
                val w = size.width
                val h = size.height

                val bigRadius = w * 0.50f
                val bigCenter = Offset(w * 0.18f, h * 0.30f)
                drawCircle(
                    brush = Brush.linearGradient(
                        colors = listOf(yellow.copy(alpha = 0.55f), yellow.copy(alpha = 0f)),
                        start = Offset(bigCenter.x, bigCenter.y - bigRadius),
                        end = Offset(bigCenter.x, bigCenter.y + bigRadius)
                    ),
                    radius = bigRadius,
                    center = bigCenter
                )

                val smallRadius = w * 0.34f
                val smallCenter = Offset(w * 0.86f, h * 0.62f)
                drawCircle(
                    brush = Brush.linearGradient(
                        colors = listOf(yellow.copy(alpha = 0.55f), yellow.copy(alpha = 0f)),
                        start = Offset(smallCenter.x, smallCenter.y - smallRadius),
                        end = Offset(smallCenter.x, smallCenter.y + smallRadius)
                    ),
                    radius = smallRadius,
                    center = smallCenter
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 8.dp)
            )
        }

        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.52f)
                .background(MaterialTheme.colorScheme.surface, sheetShape)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(start = 28.dp, end = 28.dp, top = 28.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                fields()
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 16.dp)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                actions()
            }
        }
    }
}

@Composable
fun AuthButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    useMutedColor: Boolean = false
) {
    val containerColor = if (useMutedColor) YellowContainer else MaterialTheme.colorScheme.secondary
    val contentColor = if (useMutedColor) OnYellowContainer else MaterialTheme.colorScheme.onSecondary

    Button(
        onClick = onClick,
        enabled = !isLoading,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor,
            disabledContentColor = contentColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = contentColor,
                strokeWidth = 2.dp
            )
        } else {
            Text(text, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun AuthSwitchPrompt(
    prompt: String,
    action: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Text("$prompt ", style = MaterialTheme.typography.bodyMedium)
        Text(
            text = action,
            style = MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.Underline),
            color = if (enabled) Color.Unspecified else MaterialTheme.colorScheme.outline,
            modifier = Modifier.clickable(enabled = enabled, onClick = onClick)
        )
    }
}