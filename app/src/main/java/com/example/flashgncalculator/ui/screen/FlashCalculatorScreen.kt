package com.example.flashgncalculator.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.flashgncalculator.FlashCalculatorUiState
import com.example.flashgncalculator.domain.FlashOption
import com.example.flashgncalculator.ui.theme.AppBlack
import com.example.flashgncalculator.ui.theme.AppWhite
import com.example.flashgncalculator.ui.theme.SoftWhite
import com.example.flashgncalculator.ui.theme.StatusGreen
import com.example.flashgncalculator.ui.theme.StatusRed

@Composable
fun FlashCalculatorScreen(
    uiState: FlashCalculatorUiState,
    onGnChanged: (Int) -> Unit,
    onApertureSelected: (FlashOption) -> Unit,
    onDistanceSelected: (FlashOption) -> Unit,
    onIsoSelected: (FlashOption) -> Unit
) {
    var showHelpDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppBlack
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBlack)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeaderBar(
                    isPowerOutOfRange = uiState.isPowerOutOfRange,
                    onHelpClick = { showHelpDialog = true }
                )

                Spacer(modifier = Modifier.height(36.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    GnSliderSection(
                        gn = uiState.gn,
                        onGnChanged = onGnChanged
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    OutputSection(
                        output = uiState.output,
                        statusMessage = uiState.powerStatusMessage
                    )

                    Spacer(modifier = Modifier.height(72.dp))

                    PickerSection(
                        uiState = uiState,
                        onApertureSelected = onApertureSelected,
                        onDistanceSelected = onDistanceSelected,
                        onIsoSelected = onIsoSelected
                    )
                }
            }

            if (showHelpDialog) {
                AlertDialog(
                    onDismissRequest = { showHelpDialog = false },
                    title = {
                        Text(text = "闪光灯 GN 说明", color = AppWhite)
                    },
                    text = {
                        Text(
                            text = "拖动 GN 滑杆并选择光圈、距离和 ISO，系统会根据 GN 与曝光关系自动映射到最接近的常见闪光输出档位。顶部绿色指示代表当前结果在闪光灯可用功率范围内；红色指示代表计算结果已经超过满功率，或低于最小功率。红色提示文案会直接显示当前越界原因。",
                            color = SoftWhite
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { showHelpDialog = false }) {
                            Text("知道了", color = AppWhite)
                        }
                    },
                    containerColor = Color(0xFF121214)
                )
            }
        }
    }
}

@Composable
private fun HeaderBar(
    isPowerOutOfRange: Boolean,
    onHelpClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
                .size(10.dp)
                .clip(CircleShape)
                .background(if (isPowerOutOfRange) StatusRed else StatusGreen)
        )

        TopHelpButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = onHelpClick
        )
    }
}

@Composable
fun TopHelpButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(52.dp)
            .clip(CircleShape)
            .semantics { contentDescription = "帮助说明" }
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawCircle(
                    color = Color(0xFF1E7BFF),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f)
                )
            }
            Text(
                text = "?",
                color = Color(0xFF1E7BFF),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun GnSliderSection(
    gn: Int,
    onGnChanged: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "GN $gn",
            style = MaterialTheme.typography.headlineLarge,
            color = AppWhite
        )

        Spacer(modifier = Modifier.height(34.dp))

        androidx.compose.material3.Slider(
            value = gn.toFloat(),
            onValueChange = { onGnChanged(it.toInt()) },
            valueRange = 1f..80f,
            steps = 78,
            colors = androidx.compose.material3.SliderDefaults.colors(
                thumbColor = AppWhite,
                activeTrackColor = Color(0xFF1E7BFF),
                inactiveTrackColor = Color(0xFF2A2A2F)
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun OutputSection(
    output: String,
    statusMessage: String?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "OUTPUT",
            style = MaterialTheme.typography.titleMedium,
            color = AppWhite
        )

        Spacer(modifier = Modifier.height(12.dp))

        Icon(
            imageVector = Icons.Outlined.KeyboardArrowDown,
            contentDescription = null,
            tint = AppWhite,
            modifier = Modifier.size(30.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = output,
            style = MaterialTheme.typography.displayLarge,
            color = AppWhite
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier.height(28.dp),
            contentAlignment = Alignment.Center
        ) {
            if (statusMessage != null) {
                Text(
                    text = statusMessage,
                    color = StatusRed,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun PickerSection(
    uiState: FlashCalculatorUiState,
    onApertureSelected: (FlashOption) -> Unit,
    onDistanceSelected: (FlashOption) -> Unit,
    onIsoSelected: (FlashOption) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        WheelPickerColumn(
            modifier = Modifier.fillMaxWidth(),
            title = "光圈",
            options = uiState.apertureOptions,
            selectedOption = uiState.selectedAperture,
            onSelected = onApertureSelected
        )

        WheelPickerColumn(
            modifier = Modifier.fillMaxWidth(),
            title = "距离 (m)",
            options = uiState.distanceOptions,
            selectedOption = uiState.selectedDistance,
            onSelected = onDistanceSelected
        )

        WheelPickerColumn(
            modifier = Modifier.fillMaxWidth(),
            title = "感光度",
            options = uiState.isoOptions,
            selectedOption = uiState.selectedIso,
            onSelected = onIsoSelected
        )
    }
}


