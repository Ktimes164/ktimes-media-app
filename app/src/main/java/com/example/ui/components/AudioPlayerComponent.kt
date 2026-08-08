package com.example.ui.components

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ElectricYellow
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.PrimaryPurpleLight
import com.example.ui.theme.StudioWhite
import com.example.ui.theme.VibrantOrange
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun AudioPlayerComponent(
    audioUrl: String,
    title: String,
    sampleId: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var durationMs by remember { mutableIntStateOf(0) }
    var currentPositionMs by remember { mutableIntStateOf(0) }
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var isUserSeeking by remember { mutableStateOf(false) }

    val mediaPlayer = remember {
        MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
    }

    DisposableEffect(audioUrl) {
        onDispose {
            try {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                }
                mediaPlayer.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Periodically update progress
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            try {
                if (mediaPlayer.isPlaying && !isUserSeeking) {
                    currentPositionMs = mediaPlayer.currentPosition
                    durationMs = mediaPlayer.duration
                    if (durationMs > 0) {
                        sliderPosition = currentPositionMs.toFloat() / durationMs.toFloat()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            delay(300)
        }
    }

    fun playPause() {
        try {
            if (isPlaying) {
                mediaPlayer.pause()
                isPlaying = false
            } else {
                if (currentPositionMs == 0 && !isLoading) {
                    isLoading = true
                    mediaPlayer.reset()
                    mediaPlayer.setDataSource(audioUrl)
                    mediaPlayer.prepareAsync()
                    mediaPlayer.setOnPreparedListener { mp ->
                        isLoading = false
                        durationMs = mp.duration
                        mp.start()
                        isPlaying = true
                    }
                    mediaPlayer.setOnCompletionListener {
                        isPlaying = false
                        currentPositionMs = 0
                        sliderPosition = 0f
                    }
                    mediaPlayer.setOnErrorListener { _, _, _ ->
                        isLoading = false
                        isPlaying = false
                        true
                    }
                } else {
                    mediaPlayer.start()
                    isPlaying = true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            isLoading = false
            isPlaying = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(PrimaryPurple, PrimaryPurpleLight)
                )
            )
            .border(1.dp, PrimaryPurpleLight, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Player Title Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = "Audio Spot",
                tint = ElectricYellow,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "AUDIO PREVIEW • $sampleId",
                color = ElectricYellow,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Audio Waveform Visualizer Animation
        AudioWaveformView(isPlaying = isPlaying)

        Spacer(modifier = Modifier.height(12.dp))

        // Progress Slider
        Slider(
            value = sliderPosition,
            onValueChange = {
                isUserSeeking = true
                sliderPosition = it
            },
            onValueChangeFinished = {
                isUserSeeking = false
                if (durationMs > 0) {
                    val seekToMs = (sliderPosition * durationMs).toInt()
                    mediaPlayer.seekTo(seekToMs)
                    currentPositionMs = seekToMs
                }
            },
            colors = SliderDefaults.colors(
                thumbColor = ElectricYellow,
                activeTrackColor = ElectricYellow,
                inactiveTrackColor = StudioWhite.copy(alpha = 0.3f)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Time Indicators
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatMs(currentPositionMs),
                color = StudioWhite.copy(alpha = 0.8f),
                fontSize = 11.sp
            )
            Text(
                text = formatMs(durationMs),
                color = StudioWhite.copy(alpha = 0.8f),
                fontSize = 11.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Controls Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    val target = (currentPositionMs - 10000).coerceAtLeast(0)
                    mediaPlayer.seekTo(target)
                    currentPositionMs = target
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Replay10,
                    contentDescription = "Rewind 10s",
                    tint = ElectricYellow
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Big Play/Pause Button in Vibrant Orange
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(VibrantOrange)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = StudioWhite,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(28.dp)
                    )
                } else {
                    IconButton(onClick = { playPause() }) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = StudioWhite,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AudioWaveformView(isPlaying: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp)
            .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val barCount = 28
        val randomHeights = remember { List(barCount) { Random.nextFloat().coerceIn(0.2f, 1.0f) } }

        for (i in 0 until barCount) {
            val animFraction = remember { Animatable(0.3f) }

            LaunchedEffect(isPlaying) {
                if (isPlaying) {
                    animFraction.animateTo(
                        targetValue = randomHeights[i],
                        animationSpec = infiniteRepeatable(
                            animation = tween(durationMillis = 250 + (i % 5) * 80, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        )
                    )
                } else {
                    animFraction.snapTo(0.2f)
                }
            }

            val heightFactor = if (isPlaying) animFraction.value else randomHeights[i] * 0.25f

            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height((28 * heightFactor).dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        if (isPlaying) ElectricYellow else ElectricYellow.copy(alpha = 0.5f)
                    )
            )
        }
    }
}

private fun formatMs(ms: Int): String {
    if (ms <= 0) return "00:00"
    val seconds = (ms / 1000) % 60
    val minutes = (ms / (1000 * 60)) % 60
    return String.format("%02d:%02d", minutes, seconds)
}

