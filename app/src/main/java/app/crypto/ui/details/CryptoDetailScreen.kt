package app.crypto.ui.details

import android.view.LayoutInflater
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import app.crypto.R
import app.crypto.data.PriceEntity
import app.crypto.ui.getImageUrl
import coil.compose.rememberImagePainter
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.data.Set
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
@Composable
fun CryptoDetailScreen(
    viewModel: CryptoViewModel = hiltViewModel()
){

    val cryptoState = viewModel.state.collectAsState()
    val historyState = viewModel.history.collectAsState()
    val crypto = cryptoState.value
    val symbol = cryptoState.value?.symbol ?: ""
    val history = historyState.value

    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        scaffoldState = rememberScaffoldState(
            snackbarHostState = snackBarHostState
        )
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = getImageUrl(symbol),
                        builder = {
                            crossfade(true)
                            placeholder(R.drawable.ic_crypto)
                            error(R.drawable.ic_crypto)
                        }
                    ),
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .padding(16.dp, 0.dp),
                    contentDescription = null
                )
                Text(
                    text = symbol,
                    style = MaterialTheme.typography.h4.copy(
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    ),
                    color = MaterialTheme.colors.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(
                modifier = Modifier
                    .padding(16.dp, 8.dp)
                    .fillMaxWidth()
                    .height(300.dp)
                    //.weight(3f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
            ){

                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    factory = {
                        LayoutInflater.from(it)
                            .inflate(R.layout.crypto_chart, null, false)
                    },
                    update = { view ->
                        val anyChartView = view.findViewById<AnyChartView>(R.id.any_chart_view)
                        if(history.isNotEmpty()){
                            anyChartView.plotChart(history = history)
                        }
                    }
                )
                Text(
                    modifier = Modifier
                        .padding(16.dp),
                    text = stringResource(id = R.string.chart_description),
                    style = MaterialTheme.typography.body1,
                    color = Color.Gray,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                modifier = Modifier
                    .padding(16.dp, 8.dp),
                text = stringResource(id = R.string.status),
                style = MaterialTheme.typography.h6,
                color = Color.Gray,
                overflow = TextOverflow.Ellipsis
            )
            Column(
                modifier = Modifier
                    .padding(16.dp, 0.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    DataRow(
                        "Symbol:",
                        "${crypto?.symbol}"
                    )
                    DataRow(
                        "Name:",
                        "${crypto?.name}"
                    )
                    DataRow(
                        "Price:",
                        "€${crypto?.priceUsd}"
                    )
                    val change = crypto?.changePercent24Hr ?: 0.0
                    DataRow(
                        "Change:",
                    "$change%",
                    )
                    crypto?.supply?.let {
                        DataRow(
                            "Supply:",
                            "€${it.toLong().div(1000000)}M"
                        )
                    }
                    crypto?.maxSupply?.let {
                        val data = if(it >= 0) "Not Available"
                        else "€${it.div(1000000)}M"
                        DataRow(
                            "Max Supply:",
                            data
                        )
                    }
                    crypto?.volumeUsd24Hr?.let {
                        DataRow(
                            "Volume:",
                            "€${it.div(1000000)}M"
                        )
                    }
                    crypto?.marketCapUsd?.let {
                        DataRow(
                            "Cap:",
                            "€${it.div(1000000)}M"
                        )
                    }

                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            val scope = rememberCoroutineScope()
            crypto?.explorer?.let { url ->
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = {
                        scope.launch {
                            snackBarHostState.showSnackbar(
                                message = url,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                ) {
                    Text(
                        text = "Info",
                        style = MaterialTheme.typography.h6,
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    LaunchedEffect(Unit){
        viewModel.messages.collect {
            snackBarHostState.showSnackbar(
                message = it,
                duration = SnackbarDuration.Short
            )
        }
    }
}

@Composable
fun DataRow(
    label: String,
    value: String,
){
    Row(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface,
            maxLines = 1
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface,
            maxLines = 1
        )
    }
}

fun AnyChartView.plotChart(
    history: List<PriceEntity>
){
    val cartesian = AnyChart.line()

    cartesian.animation(true)
    cartesian.padding(30, 20, 30, 5)
    cartesian.crosshair().enabled(true)
    cartesian.crosshair().yLabel(true)

    cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
    cartesian.xAxis(0).labels()
        .padding(5, 5, 5, 5)

    val sdf = SimpleDateFormat("MM-dd")
    val cal = Calendar.getInstance()
    val seriesData = history.map{
        cal.timeInMillis = it.time
        ValueDataEntry(
            sdf.format(cal.time),
            it.priceUsd
        )
    }
    val set = Set.instantiate()
    val series1Mapping = set.mapAs ("{ x: 'x', value: 'value' }")

    set.data(seriesData)
    val series1 = cartesian.line(series1Mapping)
    series1.name("Price")
    series1.hovered().markers().enabled(true)
    series1.hovered().markers()
        .type(MarkerType.CIRCLE)
        .size(6)

    setChart(cartesian)
}