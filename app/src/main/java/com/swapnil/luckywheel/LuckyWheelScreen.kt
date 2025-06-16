package com.swapnil.luckywheel

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.swapnil.luckywheel.ui.theme.Poppins
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LuckyWheelScreen() {
    val items = listOf("$ 50", "$ 150", "$ 25", "$ 1000", "$ 1", "$ 500", "$ 5", "$ 10")
    var result by remember { mutableStateOf("") }
    var isChestAnimation by remember {
        mutableStateOf(false)
    }
    var isCoinAnimation by remember {
        mutableStateOf(false)
    }
    val chestComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.chest))
    val coinComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.coins))
    val lightComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.light))

    val chestProgress by animateLottieCompositionAsState(
        chestComposition,
        isPlaying = isChestAnimation // Infinite repeat mode
    )
    val coinProgress by animateLottieCompositionAsState(
        coinComposition,
        isPlaying = isCoinAnimation // Infinite repeat mode
    )
    val lightProgress by animateLottieCompositionAsState(
        lightComposition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isChestAnimation // Infinite repeat mode
    )
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val isTab = configuration.screenWidthDp >= 600 && configuration.screenHeightDp >= 600
    val coroutineScope = rememberCoroutineScope()
    val boxSizeDp = remember { mutableStateOf(IntSize(0, 0)) }
    val density = LocalDensity.current
    val widthDp = with(density) { boxSizeDp.value.width.toDp() }
    val heightDp = with(density) { boxSizeDp.value.height.toDp() }

    if(isTab && !isPortrait) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight() // Optional, if you want full height
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.3f)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Splash Image",
                    modifier = Modifier
                        .size(220.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds, // Keeps the center portion of the image
                )
                Text(
                    text = "DreamTrade",
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 35.sp,
                    fontFamily = Poppins,
                )

            }
            Spacer(
                Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.background)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFF23af92), Color(0xFF121212)),
                            center = Offset.Unspecified, // or specify a center like Offset(0f, 0f)
                            radius = 1500f // Adjust based on screen size
                        )
                    )
                /*.drawWithCache {
                    val brushSize = 400f
                    val brush = Brush.linearGradient(
                        colors = brushColors,
                        start = Offset(animatedOffset, animatedOffset),
                        end = Offset(animatedOffset + brushSize, animatedOffset + brushSize),
                        tileMode = TileMode.Mirror
                    )
                    onDrawBehind {
                        drawRect(brush)
                    }
                }*/,
                //.background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {

                WheelStand(
                    modifier = Modifier
                        .padding(top = (heightDp + heightDp * 0.4f))
                        .fillMaxWidth(0.7f)
                        .height(150.dp)
                )


                LuckyWheel(
                    items = items,
                    onSpinEnd = { index ->
                        coroutineScope.launch {
                            result = "You got: ${items[index]}"
                            isChestAnimation = true
                            delay(1300)
                            isCoinAnimation = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .padding(16.dp)
                        .onGloballyPositioned { coordinates ->
                            val sizePx = coordinates.size // size in pixels
                            boxSizeDp.value = sizePx
                        },
                    fontSize = 50f
                )
                this@Row.AnimatedVisibility(
                    visible = isChestAnimation,
                    enter = scaleIn(
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                    ),
                    exit = scaleOut()
                ) {

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LottieAnimation(
                            composition = lightComposition,
                            progress = { lightProgress },
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .align(Alignment.Center)

                        )
                        LottieAnimation(
                            composition = chestComposition,
                            progress = { chestProgress },
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                // .padding(top = screenHeight * 0.25f)
                                .align(Alignment.Center)
                        )
                    }



                }
                this@Row.AnimatedVisibility(
                    visible = isCoinAnimation,
                    enter = scaleIn(
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                    ),
                    exit = scaleOut()
                ) {
                    LottieAnimation(
                        composition = coinComposition,
                        progress = { coinProgress },
                        modifier = Modifier
                            .size(500.dp)
                            .align(Alignment.Center)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 100.dp),
                        contentAlignment = Alignment.TopCenter
                    )
                    {
                        Image(
                            painter = painterResource(id = R.drawable.green_button),
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(250.dp)
                                .padding(16.dp)
                                .offset(y = -135.dp),
                            contentDescription = "price",
                            contentScale = ContentScale.FillBounds
                        )

                        Text(
                            text = result,
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = -22.dp)
                                .padding(top = 5.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Poppins,
                        )
                    }
                }
            }
        }
    }
    else if(!isTab && isPortrait) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFF23af92), Color(0xFF121212)),
                        center = Offset.Unspecified, // or specify a center like Offset(0f, 0f)
                        radius = 1500f // Adjust based on screen size
                    )
                )
        )
        {

            WheelStand(
                modifier = Modifier
                    .padding(top = (heightDp + heightDp * 0.4f))
                    .fillMaxWidth()
                    .height(150.dp)
            )


            LuckyWheel(
                items = items,
                onSpinEnd = { index ->
                    coroutineScope.launch {

                        result = "You got: ${items[index]}"
                        isChestAnimation = true
                        delay(1300)
                        isCoinAnimation = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .onGloballyPositioned { coordinates ->
                        val sizePx = coordinates.size // size in pixels
                        boxSizeDp.value = sizePx
                    },
            )
            AnimatedVisibility(
                visible = isChestAnimation,
                enter = scaleIn(
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                ),
                exit = scaleOut()
            ) {

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LottieAnimation(
                        composition = lightComposition,
                        progress = { lightProgress },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .align(Alignment.Center)

                    )
                    LottieAnimation(
                        composition = chestComposition,
                        progress = { chestProgress },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            // .padding(top = screenHeight * 0.25f)
                            .align(Alignment.Center)
                    )
                }


            }
            AnimatedVisibility(
                visible = isCoinAnimation,
                enter = scaleIn(
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                ),
                exit = scaleOut()
            ) {
                LottieAnimation(
                    composition = coinComposition,
                    progress = { coinProgress },
                    modifier = Modifier
                        .size(500.dp)
                        .align(Alignment.Center)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 100.dp),
                    contentAlignment = Alignment.TopCenter
                )
                {
                    Image(
                        painter = painterResource(id = R.drawable.green_button),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(16.dp)
                            .offset(y = -110.dp),
                        contentDescription = "price",
                        contentScale = ContentScale.FillBounds
                    )

                    Text(
                        text = result,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins,
                    )
                }
            }

            /* Text(
                 result, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier
                     .padding(top = 600.dp)
             )*/
        }
    }
    else if(isTab && isPortrait) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight() // Optional, if you want full height
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.3f)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Splash Image",
                    modifier = Modifier
                        .size(220.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds, // Keeps the center portion of the image
                )
                Text(
                    text = "DreamTrade",
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 35.sp,
                    fontFamily = Poppins,
                )

            }
            Spacer(
                Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.background)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFF23af92), Color(0xFF121212)),
                            center = Offset.Unspecified, // or specify a center like Offset(0f, 0f)
                            radius = 1500f // Adjust based on screen size
                        )
                    )
                /*.drawWithCache {
                    val brushSize = 400f
                    val brush = Brush.linearGradient(
                        colors = brushColors,
                        start = Offset(animatedOffset, animatedOffset),
                        end = Offset(animatedOffset + brushSize, animatedOffset + brushSize),
                        tileMode = TileMode.Mirror
                    )
                    onDrawBehind {
                        drawRect(brush)
                    }
                }*/,
                //.background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {

                WheelStand(
                    modifier = Modifier
                        .padding(top = (heightDp + heightDp * 0.4f))
                        .fillMaxWidth(0.7f)
                        .height(150.dp)
                )


                LuckyWheel(
                    items = items,
                    onSpinEnd = { index ->
                        coroutineScope.launch {

                            result = "You got: ${items[index]}"
                            isChestAnimation = true
                            delay(1300)
                            isCoinAnimation = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .padding(16.dp)
                        .onGloballyPositioned { coordinates ->
                            val sizePx = coordinates.size // size in pixels
                            boxSizeDp.value = sizePx
                        },
                    fontSize = 50f
                )
                this@Column.AnimatedVisibility(
                    visible = isChestAnimation,
                    enter = scaleIn(
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                    ),
                    exit = scaleOut()
                ) {

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LottieAnimation(
                            composition = lightComposition,
                            progress = { lightProgress },
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .align(Alignment.Center)

                        )
                        LottieAnimation(
                            composition = chestComposition,
                            progress = { chestProgress },
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                // .padding(top = screenHeight * 0.25f)
                                .align(Alignment.Center)
                        )
                    }



                }
                this@Column.AnimatedVisibility(
                    visible = isCoinAnimation,
                    enter = scaleIn(
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                    ),
                    exit = scaleOut()
                ) {
                    LottieAnimation(
                        composition = coinComposition,
                        progress = { coinProgress },
                        modifier = Modifier
                            .size(500.dp)
                            .align(Alignment.Center)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 100.dp),
                        contentAlignment = Alignment.TopCenter
                    )
                    {
                        Image(
                            painter = painterResource(id = R.drawable.green_button),
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(250.dp)
                                .padding(16.dp)
                                .offset(y = -135.dp),
                            contentDescription = "price",
                            contentScale = ContentScale.FillBounds
                        )

                        Text(
                            text = result,
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = -22.dp)
                                .padding(top = 5.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Poppins,
                        )
                    }
                }
            }
        }
    }
    else {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight() // Optional, if you want full height
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.3f)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Splash Image",
                    modifier = Modifier
                        .size(220.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds, // Keeps the center portion of the image
                )
                Text(
                    text = "DreamTrade",
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 35.sp,
                    fontFamily = Poppins,
                )

            }
            Spacer(
                Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.background)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFF23af92), Color(0xFF121212)),
                            center = Offset.Unspecified, // or specify a center like Offset(0f, 0f)
                            radius = 1500f // Adjust based on screen size
                        )
                    )
                /*.drawWithCache {
                    val brushSize = 400f
                    val brush = Brush.linearGradient(
                        colors = brushColors,
                        start = Offset(animatedOffset, animatedOffset),
                        end = Offset(animatedOffset + brushSize, animatedOffset + brushSize),
                        tileMode = TileMode.Mirror
                    )
                    onDrawBehind {
                        drawRect(brush)
                    }
                }*/,
                //.background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.TopCenter
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top=12.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    WheelStand(
                        modifier = Modifier
                            .padding(top = (heightDp *0.8f))
                            .fillMaxWidth(0.6f)
                            .height(150.dp)
                    )
                    LuckyWheel(
                        items = items,
                        onSpinEnd = { index ->
                            coroutineScope.launch {
                                result = "You got: ${items[index]}"
                                isChestAnimation = true
                                delay(1300)
                                isCoinAnimation = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .padding(16.dp)
                            .onGloballyPositioned { coordinates ->
                                val sizePx = coordinates.size // size in pixels
                                boxSizeDp.value = sizePx
                            },
                        fontSize = 45f,
                        buttonPadding = 8.dp,
                        imageSize = 45.dp,
                        needleHeight = 40.dp,
                        needleWidth = 30.dp,
                        needleOffset = -18.dp
                    )
                }



                this@Row.AnimatedVisibility(
                    visible = isChestAnimation,
                    enter = scaleIn(
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                    ),
                    exit = scaleOut()
                ) {

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        LottieAnimation(
                            composition = lightComposition,
                            progress = { lightProgress },
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .align(Alignment.Center)

                        )
                        LottieAnimation(
                            composition = chestComposition,
                            progress = { chestProgress },
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                // .padding(top = screenHeight * 0.25f)
                                .align(Alignment.Center)
                        )
                    }



                }
                this@Row.AnimatedVisibility(
                    visible = isCoinAnimation,
                    enter = scaleIn(
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                    ),
                    exit = scaleOut()
                ) {
                    LottieAnimation(
                        composition = coinComposition,
                        progress = { coinProgress },
                        modifier = Modifier
                            .size(500.dp)
                            .align(Alignment.Center)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 100.dp),
                        contentAlignment = Alignment.TopCenter
                    )
                    {
                        Image(
                            painter = painterResource(id = R.drawable.green_button),
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(250.dp)
                                .padding(16.dp)
                                .offset(y = -135.dp),
                            contentDescription = "price",
                            contentScale = ContentScale.FillBounds
                        )

                        Text(
                            text = result,
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = -22.dp)
                                .padding(top = 5.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Poppins,
                        )
                    }
                }
            }
        }
    }
}

/*
@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    LuckyWheelScreen()
}
*/

