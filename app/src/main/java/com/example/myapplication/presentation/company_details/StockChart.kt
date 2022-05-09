package com.example.myapplication.presentation.company_details

import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.domain.model.IntradayInfoModel
import kotlinx.coroutines.withContext
import kotlin.math.round
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StockChart(
    intradayData: List<IntradayInfoModel>,
    graphColor: Color = Color.Green,
    modifier: Modifier = Modifier
) {
    val spacing = 100f

    val transperentGraphColor = remember {
        graphColor.copy(alpha = 0.5f)
    }

    val upperValue = remember(intradayData) {
        intradayData.maxOfOrNull {
            it.closingValue
        }?.plus(1)?.roundToInt() ?: 0
    }

    val lowerValue = remember(intradayData) {
        intradayData.minOfOrNull { it.closingValue }?.toInt() ?: 0
    }

    val density = LocalDensity.current
    val textPaint = remember {
        Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    Canvas(modifier = modifier) {
        val spacePerHour = (size.width - spacing)/ intradayData.size

        // Draw on X axis
        (intradayData.indices step 2).forEach { i ->
            val info = intradayData[i]
            val hour = info.timeStamp.hour
            // Since there is no direct way in compose to add the text on canvas we need to use the old canvas

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    hour.toString(),
                    spacing + i * spacePerHour,
                    size.height - 5,
                    textPaint
                )
            }
        }

        // Draw on Y axis
        val priceStep = (upperValue - lowerValue) / 5f

        (0..4).forEach { i ->
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    round(lowerValue + i * priceStep).toString(),
                    30f,
                    size.height - spacing - i * size.height / 5f,
                    textPaint
                )
            }
        }

        var lastX = 0f
        // Draw path line
        val strokePath = Path().apply {
            val height = size.height

            for(i in intradayData.indices) {
                val info = intradayData[i];
                val nextInfo = intradayData.getOrNull(i + 1) ?: intradayData.last()

                val leftRatio = (info.closingValue - lowerValue) / (upperValue - lowerValue)
                val rightRatio = (nextInfo.closingValue - lowerValue) / (upperValue - lowerValue)

                val x1 = spacing + i * spacePerHour
                val y1 = height - spacing - (leftRatio * height).toFloat()
                val x2 = spacing + (i + 1) * spacePerHour
                val y2 = height - spacing - (rightRatio  * height).toFloat()

                if(i == 0) {
                    moveTo(x1, y1)
                }
                lastX = (x1 +x2)/2f

                quadraticBezierTo(x1, y1, lastX, (y1 +y2)/2f )
            }
        }

        // Draw shape below the graph so that we can apply gradient
        // Copied the exact path that we drew up
        val fillPath = android.graphics.Path(strokePath.asAndroidPath()).asComposePath().apply {
            lineTo(lastX, size.height - spacing)
            lineTo(spacing, size.height - spacing)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    transperentGraphColor,
                    Color.Transparent
                ),
                endY = size.height - spacing
            )
        )

        drawPath(
            path = strokePath,
            color = graphColor,
            style = Stroke(width = 3.dp.toPx(), cap =  StrokeCap.Round),

        )
    }
}