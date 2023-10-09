package catrine.dev.smarthouse

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TopAppBar
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import catrine.dev.smarthouse.model.Camera
import catrine.dev.smarthouse.model.Door
import catrine.dev.smarthouse.model.ModelState
import catrine.dev.smarthouse.model.SmartHouseEntity
import catrine.dev.smarthouse.ui.theme.AppbarBackground
import catrine.dev.smarthouse.ui.theme.BackgroundColor
import catrine.dev.smarthouse.ui.theme.circe
import catrine.dev.smarthouse.ui.theme.TabDivider
import catrine.dev.smarthouse.ui.theme.TextColor
import catrine.dev.smarthouse.ui.theme.TitlesTextColor
import catrine.dev.smarthouse.viewmodels.SmartHouseViewModel
import coil.compose.AsyncImage
import com.example.smarthouse.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.isValid
import io.realm.kotlin.internal.getRealm
import kotlinx.coroutines.launch

/**
 * composable functions for main tab-screen
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabLayout(viewModels: List<Pair<String, SmartHouseViewModel<*>>>) {
    val pagerState = rememberPagerState(pageCount = viewModels.count())
    Column(modifier = Modifier.background(AppbarBackground)) {
        TopAppBar(modifier = Modifier.padding(top = 29.dp), backgroundColor = AppbarBackground) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.appbar_title),
                    style = TextStyle(color = TitlesTextColor),
                    fontWeight = FontWeight(400),
                    fontSize = 21.sp,
                    fontFamily = circe,
                    lineHeight = 26.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        Tabs(pagerState = pagerState, viewModels)

        TabsContent(pagerState = pagerState, viewModels)
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(pagerState: PagerState, viewModels: List<Pair<String, SmartHouseViewModel<*>>>) {
    val scope = rememberCoroutineScope()

    androidx.compose.material.TabRow(selectedTabIndex = pagerState.currentPage,
        backgroundColor = BackgroundColor,
        contentColor = BackgroundColor,
        indicator = { tabPositions ->
            androidx.compose.material.TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 2.dp,
                color = TabDivider
            )
        }) {
        viewModels.forEachIndexed { index, _ ->
            Tab(text = {
                Text(
                    viewModels[index].first, color = TitlesTextColor,
                    fontSize = 17.sp,
                    lineHeight = 16.sp,
                    fontFamily = circe,
                    fontWeight = FontWeight(400),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }, selected = pagerState.currentPage == index, onClick = {
                scope.launch {
                    pagerState.scrollToPage(index)
                }
            })
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabsContent(pagerState: PagerState, viewModels: List<Pair<String, SmartHouseViewModel<*>>>) {
    HorizontalPager(state = pagerState) { page ->
        TabContentScreen(viewModels[page].second)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TabContentScreen(viewModel: SmartHouseViewModel<*>) {
    val state = viewModel.subscribeStates().observeAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.value is ModelState.Loading,
        onRefresh = viewModel::loadRemoteData
    )
    if (state.value is ModelState.Error) {
        Toast.makeText(LocalContext.current, state.value.toString(), Toast.LENGTH_LONG).show()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 7.dp)
            .pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        )
        {
            if (state.value is ModelState.Success) {
                (state.value as ModelState.Success).realmResult.let { results ->
                    items(results.count()) {
                        val item = results[it]
                        if (item.isValid()) {
                            (item as SmartHouseEntity).let { entity ->
                                ListItem(
                                    entity,
                                    it == 0
                                            || (results[it - 1] as SmartHouseEntity)
                                        .getRoomName() != entity.getRoomName()
                                )
                            }
                        }
                    }
                }
            }
        }
        PullRefreshIndicator(
            refreshing = state.value is ModelState.Loading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = if (state.value is ModelState.Loading) TabDivider else Color.LightGray,
        )
    }
}

@Composable
fun ListItem(result: SmartHouseEntity, showRoom: Boolean = true) {
    when (result) {
        is Camera -> {
            CameraItem(showRoom, result)
        }

        is Door -> {
            DoorItem(result, showRoom)
        }

        else -> Text(
            text = "Unknown data type: " + result::class.toString()
                    + "/n" + result.toString(),
            style = MaterialTheme.typography.h5,
            color = Color.Green,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNameField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = Modifier
            .padding(top = 3.dp)
            .background(color = Color.White),
        textStyle = TextStyle(
            textAlign = TextAlign.Start,
            fontSize = 17.sp,
            fontFamily = circe,
            fontWeight = FontWeight(400),
            color = TextColor
        )
    )
}


