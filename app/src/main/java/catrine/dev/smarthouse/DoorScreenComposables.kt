package catrine.dev.smarthouse

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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import catrine.dev.smarthouse.model.Door
import catrine.dev.smarthouse.ui.theme.BackgroundColor
import catrine.dev.smarthouse.ui.theme.TextColor
import catrine.dev.smarthouse.ui.theme.TitlesTextColor
import catrine.dev.smarthouse.ui.theme.circe
import coil.compose.AsyncImage
import com.example.smarthouse.R
import io.realm.kotlin.Realm
import io.realm.kotlin.internal.getRealm

@Composable
fun DoorItem(result: Door, showRoom: Boolean) {
    if (showRoom)
        result.room?.let { it1 ->
            Text(
                text = it1,
                modifier = Modifier.padding(top = 10.dp),
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    fontSize = 21.sp,
                    fontFamily = circe,
                    fontWeight = FontWeight(300),
                    color = TitlesTextColor
                )
            )
        }

    result.snapshot?.let { it2 ->
        val shape = RoundedCornerShape(12.dp, 12.dp)
        Box(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .fillMaxWidth()
                .shadow(elevation = 0.5.dp, shape = shape),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                model = it2,
                contentDescription = stringResource(R.string.image_description),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxSize()
            )
            Image(
                painterResource(id = R.drawable.play_button),
                contentDescription = "",
                modifier = Modifier.align(
                    Alignment.Center
                )
            )
            if (result.favorites == true) {
                Image(
                    painterResource(id = R.drawable.star),
                    contentDescription = "",
                    modifier = Modifier
                        .align(
                            Alignment.TopEnd
                        )
                        .padding(1.dp)
                )
            } else {
                Image(
                    painterResource(id = R.drawable.star_2),
                    contentDescription = "",
                    modifier = Modifier
                        .align(
                            Alignment.TopEnd
                        )
                        .padding(1.dp)
                )
            }
        }
    }
    result.name?.let { it3 ->
        val visibleState = remember {
            MutableTransitionState(false).apply {
                targetState = false
            }
        }
        val lockDoorIconState = remember {
            MutableTransitionState(false).apply {
                targetState = false
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = it3,
                style = TextStyle(
                    textAlign = TextAlign.Start,
                    fontSize = 17.sp,
                    fontFamily = circe,
                    fontWeight = FontWeight(400),
                    color = TextColor
                ),
                modifier = Modifier.clickable {
                    visibleState.targetState = !visibleState.targetState
                }
            )
            Spacer(modifier = Modifier
                .weight(1f, true)
                .clickable { visibleState.targetState = !visibleState.targetState })
            Image(
                painterResource(
                    id = if (lockDoorIconState.targetState) {
                        R.drawable.lockoff
                    } else {
                        R.drawable.closed_lock
                    }
                ),
                contentDescription = "",
                modifier = Modifier
                    .padding(top = 2.dp)
                    .clickable { lockDoorIconState.targetState = !lockDoorIconState.targetState },
                alignment = Alignment.Center
            )
            AnimatedVisibility(
                visibleState = visibleState, enter = fadeIn() + slideInHorizontally(),
                exit = fadeOut() + slideOutHorizontally()
            ) {
                Row() {
                    val editNameState = remember { mutableStateOf(false) }
                    Image(
                        painterResource(id = R.drawable.edit_3),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(8.dp, 2.dp, 12.dp, 2.dp)
                            .clickable { editNameState.value = true }
                    )
                    Image(
                        painterResource(id = R.drawable.star_2),
                        contentDescription = "",
                        modifier = Modifier.padding(6.dp, 2.dp, 1.dp, 2.dp)
                    )
                    if (editNameState.value) {
                        ShowEditDoorNameDialog(result = result, editNameState, visibleState)
                    }
                }
            }
        }
    }
}

@Composable
fun ShowEditDoorNameDialog(
    result: Door,
    editNameState: MutableState<Boolean>,
    visibleState: MutableTransitionState<Boolean>
) {
    val openDialog = remember { mutableStateOf(true) }
    val text = remember { mutableStateOf(result.name) }
    if (openDialog.value) {
        val realm = result.getRealm<Realm>()
        AlertDialog(backgroundColor = BackgroundColor,
            onDismissRequest = { openDialog.value = false },
            title = {
                Text(
                    text = stringResource(R.string.edit_door_name_dialog_title),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 17.sp,
                    fontFamily = circe,
                    fontWeight = FontWeight(400),
                    color = TextColor
                )
            },
            text = {
                EditNameField(
                    value = text.value.toString(),
                    onValueChange = {
                        text.value = it
                    },
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .background(BackgroundColor)
                )
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {
                            realm?.writeBlocking {
                                val door = query(Door::class, "id == ${result.id}").first().find()
                                door?.name = text.value
                            }
                            openDialog.value = false
                            editNameState.value = false
                            visibleState.targetState = false
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(stringResource(R.string.edit_name_dialog_apply_btn))
                    }
                    Button(
                        onClick = {
                            openDialog.value = false
                            editNameState.value = false
                            visibleState.targetState = false
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(stringResource(R.string.edit_name_dialog_dismiss_btn))
                    }
                }
            })
    }
}
