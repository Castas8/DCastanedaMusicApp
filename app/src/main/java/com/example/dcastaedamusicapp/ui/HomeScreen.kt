package com.example.dcastaedamusicapp.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.dcastaedamusicapp.data.Album

@Composable
fun HomeScreen(viewModel: MusicViewModel, onAlbumClick: (String) -> Unit) {
    val albumsState by viewModel.albumsState.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            if (albumsState is UiState.Success) {
                val firstAlbum = (albumsState as UiState.Success<List<Album>>).data.firstOrNull()
                firstAlbum?.let {
                    MiniPlayer(album = it)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        // Son los colores del fondo degradado
                        colors = listOf(Color(0xFF6A3A9F), Color(0xFF1E1E1E)),
                        startY = 0f,
                        endY = 800f
                    )
                )
                .verticalScroll(rememberScrollState())
        ) {
            HomeHeader()
            Spacer(modifier = Modifier.height(24.dp))

            when (val state = albumsState) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                is UiState.Success -> {
                    val albums = state.data
                    AlbumsSection(albums = albums, onAlbumClick = onAlbumClick)
                    Spacer(modifier = Modifier.height(24.dp))
                    RecentlyPlayedSection(albums = albums.take(4), onAlbumClick = onAlbumClick)
                }
                is UiState.Error -> {
                    Text(
                        text = "Error: ${state.message}",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "INICIO", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text(text = "Diego Castañeda", color = Color.White.copy(alpha = 0.7f), fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
        IconButton(onClick = { /* TODO */ }) {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = Color.White)
        }
    }
}

@Composable
fun AlbumsSection(albums: List<Album>, onAlbumClick: (String) -> Unit) {
    Column {
        Text(
            text = "Albums",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(albums) { album ->
                AlbumCard(album = album, onClick = { onAlbumClick(album.id) })
            }
        }
    }
}

@Composable
fun AlbumCard(album: Album, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(180.dp)
            .height(220.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
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
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                        startY = 300f
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(text = album.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = album.artist, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.Black)
        }
    }
}

@Composable
fun RecentlyPlayedSection(albums: List<Album>, onAlbumClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Recently Played",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            albums.forEach { album ->
                RecentlyPlayedItem(album = album, onClick = { onAlbumClick(album.id) })
            }
        }
    }
}

@Composable
fun RecentlyPlayedItem(album: Album, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = album.coverUrl,
                contentDescription = album.title,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = album.title, color = Color.White, fontWeight = FontWeight.SemiBold)
                Text(text = album.artist, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun MiniPlayer(album: Album) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2E2E2E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = album.coverUrl,
                contentDescription = album.title,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = album.title, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(text = album.artist, color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            }
            IconButton(onClick = { /* TODO: Play/Pause logic */ }) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play/Pause", tint = Color.White, modifier = Modifier.size(32.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {

}
