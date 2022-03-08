package app.crypto.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.crypto.R
import app.crypto.model.CryptoAsset
import app.crypto.ui.getImageUrl
import app.crypto.ui.theme.NegativeRed
import app.crypto.ui.theme.PositiveGreen
import coil.compose.rememberImagePainter

@Composable
fun CryptoList(
    state: State<MainState>,
    onClick: (CryptoAsset) -> Unit = {},
    bottomBar: @Composable () -> Unit = {}
){

    Scaffold(
        bottomBar = bottomBar
    ) {
        LazyColumn(
            modifier = Modifier
                .testTag("LazyColumn")
                .fillMaxSize()
                .padding(8.dp, 0.dp, 8.dp, 0.dp)
        ){
            items(state.value.cryptoList){ crypto ->
                CryptoItem(
                    state = crypto,
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
fun CryptoItem(
    state: CryptoAsset,
    onClick: (CryptoAsset)-> Unit = {}
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .border(6.dp, Color.Transparent)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .clickable {
                onClick(state)
            },
        //elevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = getImageUrl(state.symbol),
                        builder = {
                            crossfade(true)
                            //placeholder(R.drawable.ic_crypto)
                            error(R.drawable.ic_crypto)
                        }
                    ),
                    modifier = Modifier
                        .width(90.dp)
                        .height(90.dp)
                        .padding(16.dp),
                    contentDescription = null
                )
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = state.symbol,
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colors.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = state.name,
                        style = MaterialTheme.typography.body1,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "€ ${state.priceUsd}",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                val change = state.changePercent24Hr
                val icon = if(change>0.0) "▲" else "▼"
                val color = if(change>0.0) PositiveGreen else NegativeRed
                Text(
                    text = "$icon ${change}%",
                    style = MaterialTheme.typography.body1,
                    color = color,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewCryptoList(){
    val list = listOf(CryptoAsset(
        id = "bitcoin",
        rank = 1,
        symbol = "bitcoin",
        name = "bitcoin",
        supply = 1.0,
        maxSupply = 1,
        marketCapUsd = 100000000,
        volumeUsd24Hr = 100000000,
        priceUsd = 1111233.0341,
        changePercent24Hr = 1.0,
        vwap24Hr = 1.0,
        explorer = "https://wwww.crypto.com"
    ))
    CryptoList(
        state = remember { mutableStateOf(MainState(list))}
    )
}