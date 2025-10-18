package com.example.dcastaedamusicapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.dcastaedamusicapp.data.Album

@Composable
fun DetailScreen(viewModel: MusicViewModel, albumId: String, onBackClick: () -> Unit) {
    LaunchedEffect(albumId) {
        viewModel.fetchAlbumDetail(albumId)
    }

    val albumDetailState by viewModel.albumDetailState.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            if (albumDetailState is UiState.Success) {
                val album = (albumDetailState as UiState.Success<Album>).data
                MiniPlayer(album = album)
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFF1E1E1E))) {
            when (val state = albumDetailState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color.White)
                }
                is UiState.Success -> {
                    val album = state.data
                    LazyColumn(modifier = Modifier.padding(paddingValues)) {
                        item {
                            DetailHeader(album = album, onBackClick = onBackClick)
                        }
                        item {
                            AboutAlbumCard(album = album)
                        }
                        item {
                            ArtistChip(album = album)
                        }
                        itemsIndexed(List(10) { it + 1 }) { _, trackNumber ->
                            TrackItem(album = album, trackNumber = trackNumber)
                        }
                    }
                }
                is UiState.Error -> {
                    Text(
                        text = "Error: ${state.message}",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp).align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun DetailHeader(album: Album, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
    ) {
        AsyncImage(
            model = album.coverUrl,
            contentDescription = album.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.4f), Color(0xFF1E1E1E)),
                        startY = 0f,
                        endY = 800f
                    )
                )
        )
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(album.title, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(album.artist, color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = { /* TODO */ },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A3A9F))
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Play", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun AboutAlbumCard(album: Album) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("About this album", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(album.description ?: "No description available.", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
        }
    }
}

@Composable
fun ArtistChip(album: Album) {
    Card(
        modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))
    ) {
        Text(
            text = "Artist: ${album.artist}",
            color = Color.White,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun TrackItem(album: Album, trackNumber: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = album.coverUrl,
            contentDescription = album.title,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(6.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${album.title} • Track $trackNumber",
                color = Color.White,
                fontWeight = FontWeight.Normal,
                maxLines = 1
            )
            Text(
                text = album.artist,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        }
    }
}