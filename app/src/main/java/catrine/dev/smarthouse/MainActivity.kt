package catrine.dev.smarthouse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Tab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import catrine.dev.smarthouse.FontCirce.fontFamily
import com.example.smarthouse.R
import catrine.dev.smarthouse.model.Camera
import catrine.dev.smarthouse.model.Door
import catrine.dev.smarthouse.ui.theme.SmartHouseTheme
import catrine.dev.smarthouse.viewmodels.CamerasViewModel
import catrine.dev.smarthouse.viewmodels.DoorsViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
object FontCirce {
    val fontProvider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )
    val fontName = GoogleFont("Circe")
    val fontFamily = FontFamily(Font(googleFont = fontName, fontProvider = fontProvider))
}
class MainActivity : ComponentActivity() {
    private val camerasViewModel : CamerasViewModel by viewModels<CamerasViewModel>()
    private val doorsViewModel: DoorsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            SmartHouseTheme {

                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    TransparentSystemBars()
                    TabLayout()

                   // Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun TransparentSystemBars() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    DisposableEffect(systemUiController, useDarkIcons) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )

        onDispose {}
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabLayout(){
    val pagerState = rememberPagerState(pageCount = 2)
 Column (modifier = Modifier.background(Color.White)){
     androidx.compose.material.TopAppBar(backgroundColor = Color.Cyan) {
         Column (modifier = Modifier.fillMaxSize(),
             horizontalAlignment = Alignment.CenterHorizontally,
             verticalArrangement = Arrangement.Center){
             Text(text = "Мой дом", style = TextStyle(color = Color.DarkGray),
                 fontWeight = FontWeight.Bold, fontSize = 20.sp,
                 fontFamily = fontFamily,
                 modifier = Modifier.padding(all = 5.dp),
                 textAlign = TextAlign.Center)
         }
     }
     Tabs(pagerState = pagerState)

     TabsContent(pagerState = pagerState)
 }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(pagerState: PagerState){
    val list = listOf("Камеры", "Двери")
    val scope = rememberCoroutineScope()

    androidx.compose.material.TabRow(selectedTabIndex = pagerState.currentPage, backgroundColor = Color.Black,
        contentColor = Color.Green, indicator = {tabPositions ->
            androidx.compose.material.TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState,tabPositions),
                height = 2.dp,
                color = Color.Blue
            )
        }) {
        list.forEachIndexed { index, _ ->
            Tab( text = {
                        Text(list[index], color = if (pagerState.currentPage == index) Color.Magenta else Color.LightGray)
            },selected = pagerState.currentPage == index, onClick = { scope.launch {
                pagerState.scrollToPage(index)
            }})
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabsContent(pagerState: PagerState){
    HorizontalPager(state = pagerState) {
        page ->
        when(page) {
            0 -> TabContentScreen(data = "Camera screen")
            1 -> TabContentScreen(data = "Door screen")
        }
    }
}

@Composable
fun TabContentScreen(data: String){
    Column (modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        Text(text = data,
            style = androidx.compose.material.MaterialTheme.typography.h5,
            color = Color.Green,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center)
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//            text = "Hello  $name!",
//            modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    SmartHouseTheme {
//        Greeting("Android")
//    }
//}