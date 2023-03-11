package net.inferno.hwbenchmarks.ui.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.inferno.hwbenchmarks.model.BenchmarkModel
import net.inferno.hwbenchmarks.theme.AppTheme
import kotlin.random.Random

@Composable
fun BenchmarkItem(
    benchmark: BenchmarkModel,
    modifier: Modifier = Modifier,
) {
    Card(
        border = BorderStroke(
            width = 2.dp,
            color = Color(0x1FFFFFFF),
        ),
        modifier = Modifier
            .then(modifier),
        elevation = CardDefaults.outlinedCardElevation(
            defaultElevation = 2.dp,
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text(
                text = benchmark.name,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .padding(4.dp),
            ) {
                Text(
                    text = "${benchmark.benchmark} BP",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .weight(1f)
                )

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(Color.White)
                )

                Text(
                    text = "#${benchmark.rank}",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
@Preview
fun BenchmarkItemPreview(
    @PreviewParameter(
        provider = BenchmarkProvider::class,
        limit = 3,
    ) benchmark: BenchmarkModel
) {
    AppTheme {
        BenchmarkItem(benchmark)
    }
}

class BenchmarkProvider : PreviewParameterProvider<BenchmarkModel> {
    override val values: Sequence<BenchmarkModel>
        get() = sequence {
            repeat(5) {
                yield(
                    BenchmarkModel(
                        Random.nextInt(1, 10),
                        "Intel Core i5 8600k",
                        Random.nextInt(200, 1_000),
                        Random.nextInt(1, 10),
                        BenchmarkModel.Type.CPUS,
                    )
                )
            }
        }
}