package com.example.composetest

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import java.util.*
import kotlin.math.roundToInt

@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var triggered by mutableStateOf(false)
            Language(language = "en") { triggered = !triggered }
            Column {
                CustomSwitchButtons()
                SwipeableSample()
                SwitchableSample()
                SwitchableCustomSample()
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun Language(language: String, triggered: () -> Unit) {

    val newLocale = Locale(language)
    Locale.setDefault(newLocale)
    val context = LocalContext.current
    context.resources.configuration.setLocale(newLocale)
    val localConfig = LocalConfiguration.current
    val localsList = LocalConfiguration.current.locales
    val localConf = localsList[0].country
    val tempStr = stringResource(id =R.string.temperature)
    val lc = LocalContext.current.resources
    Log.d("Test_Locale", localConf)
}

@Composable
fun CustomSwitchButtons() {
    val temperatureState = remember {
        mutableStateOf(true)
    }
    val onButtonClick = {
        temperatureState.value = !temperatureState.value
    }
    val celsiusBackgroundColor = if (temperatureState.value) Color.DarkGray else Color.LightGray
    val kelvinBackgroundColor = if (temperatureState.value) Color.LightGray else Color.DarkGray

    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(5.dp))
            .background(color = Color.LightGray)
            .width(300.dp)
            .height(50.dp)
    ) {
        Row {
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = celsiusBackgroundColor),
                onClick = onButtonClick,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(text = "Цельсий")
            }
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = kelvinBackgroundColor),
                onClick = onButtonClick,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(text = "Кельвин")
            }

        }
    }
}

@Preview
@Composable
fun CustomSwitchButtonsPreview() {
    CustomSwitchButtons()
}

@ExperimentalMaterialApi
@Preview
@Composable
fun ShowSwitchPreview() {
    SwipeableSample()
}

@Composable
fun ToggleableButton() {

    var checked by remember { mutableStateOf(false) }
// content that you want to make toggleable
    Text(
        modifier = Modifier.toggleable(
            value = checked, onValueChange = { checked = it },
            role = Role.Switch
        ),
        text = checked.toString()
    )
}

@Preview
@Composable
fun SwitchableSamplePreview() {
    SwitchableSample()
}

@Composable
fun SwitchableSample() {
    val mutableState = remember {
        mutableStateOf(false)
    }
    Box(modifier = Modifier.padding(start = 10.dp)) {
        Switch(
            checked = mutableState.value,
            onCheckedChange = { mutableState.value = !mutableState.value },
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
        )
    }

}


@ExperimentalMaterialApi
@Preview
@Composable
fun SwitchableCostumSamplePreview() {
    SwitchableCustomSample()
}

@Composable
@ExperimentalMaterialApi
fun <T : Any> rememberSwipeableStateFor(
    value: T,
    onValueChange: (T) -> Unit,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec
): SwipeableState<T> {
    val swipeableState = remember {
        SwipeableState(
            initialValue = value,
            animationSpec = animationSpec,
            confirmStateChange = { true }
        )
    }
    val forceAnimationCheck = remember { mutableStateOf(false) }
    LaunchedEffect(value, forceAnimationCheck.value) {
        if (value != swipeableState.currentValue) {
            swipeableState.animateTo(value)
        }
    }
    DisposableEffect(swipeableState.currentValue) {
        if (value != swipeableState.currentValue) {
            onValueChange(swipeableState.currentValue)
            forceAnimationCheck.value = !forceAnimationCheck.value
        }
        onDispose { }
    }
    return swipeableState
}

@ExperimentalMaterialApi
@Composable
fun SwitchableCustomSample() {
    val mutableState = remember {
        mutableStateOf(false)
    }

    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val onCheckedChange = { mutableState.value = !mutableState.value }
    val minBound = 0f
    val maxBound = 36.75f
    val AnimationSpec = TweenSpec<Float>(durationMillis = 100)
    val DefaultSwitchPadding = 2.dp
    val SwitchWidth = 300.dp
    val SwitchHeight = 50.dp
    val swipeableStateFor =
        rememberSwipeableState(initialValue = mutableState.value, animationSpec = AnimationSpec)
    val swipeableState =
        rememberSwipeableStateFor(
            mutableState.value, onValueChange = { mutableState.value = !mutableState.value },
            animationSpec = AnimationSpec
        )

    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val TrackWidth = 34.dp
    val TrackStrokeWidth = 14.dp
    val colors: SwitchColors = SwitchDefaults.colors()
    val toggleableModifier =

        Modifier.toggleable(
            value = true,
            onValueChange = { mutableState.value = !mutableState.value },
            enabled = true,
            role = Role.Switch, interactionSource = interactionSource,
            indication = null
        )


    Box(
        modifier = Modifier
            .toggleable(
                value = true,
                onValueChange = { mutableState.value = !mutableState.value },
                enabled = true,
                role = Role.Switch, interactionSource = interactionSource,
                indication = null
            )
            .swipeable(
                state = swipeableState,
                anchors = mapOf(minBound to false, maxBound to true),
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Horizontal,
                enabled = true,
                reverseDirection = isRtl,
                interactionSource = interactionSource,
                resistance = null
            )
            .wrapContentSize(Alignment.Center)
            .padding(DefaultSwitchPadding)
            .requiredSize(SwitchWidth, SwitchHeight)
            .background(Color.Gray)
    ) {
        val trackColor by colors.trackColor(true, mutableState.value)
        Canvas(
            Modifier
                .align(Alignment.Center)
                .fillMaxSize()
        ) {
            val strokeRadius = TrackStrokeWidth.toPx() / 2
            drawLine(
                trackColor,
                Offset(strokeRadius, center.y),
                Offset(TrackWidth.toPx() - strokeRadius, center.y),
                TrackStrokeWidth.toPx(),
                StrokeCap.Round
            )
        }
    }


}

@ExperimentalMaterialApi
@Composable

fun SwipeableSample() {
    val width = 300.dp
    val squareSize = 150.dp
    val mutableState = remember {
        mutableStateOf(false)
    }
    val AnimationSpec = TweenSpec<Float>(durationMillis = 1000)
    val swipeableState =
        rememberSwipeableStateFor(
            mutableState.value, onValueChange = { mutableState.value = !mutableState.value },
            animationSpec = AnimationSpec
        )

    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val minBound = 0f
    val maxBound = with(LocalDensity.current) { squareSize.toPx() }
//    val anchors = mapOf(0f to 0, sizePx to 1) // Maps anchor points (in px) to states
    val anchors = mapOf(minBound to false, maxBound to true)
    val trackColor by SwitchDefaults.colors().trackColor(true, mutableState.value)
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .width(width)

            .toggleable(
                value = mutableState.value,
                role = Role.Switch,
                enabled = true,
                interactionSource = interactionSource,
                indication = null,
                onValueChange = { mutableState.value = !mutableState.value })
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
            .clip(shape = RoundedCornerShape(5.dp))

            .background(Color.LightGray)
    ) {
        AddTexts()
        Box(
            Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .height(50.dp)
                .width(squareSize)
                .clip(shape = RoundedCornerShape(5.dp))
                .background(Color.DarkGray.copy(0.5f))
        )
    }
}

@Composable
fun AddTexts() {
    Row(horizontalArrangement = Arrangement.Center) {
        Text(
            text = stringResource(R.string.temperature),

            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        )
        Text(
            text = "Кельвин",

            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        )

    }
}