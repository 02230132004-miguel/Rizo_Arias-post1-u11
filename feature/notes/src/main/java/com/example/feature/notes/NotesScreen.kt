package com.example.feature.notes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.domain.model.Note

@Composable
fun NotesScreen(
    onNavigateToDetail: (String) -> Unit,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is NotesEvent.NavigateToDetail -> onNavigateToDetail(event.noteId)
                is NotesEvent.NoteDeleted -> { /* Opcional: Mostrar mensaje */ }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.addDummyNote() }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Nota")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (val s = state) {
                is NotesUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is NotesUiState.Error -> {
                    Text(
                        text = s.message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is NotesUiState.Success -> {
                    NotesList(
                        notes = s.notes,
                        onNoteClick = { viewModel.onNoteClicked(it) },
                        onDelete = { viewModel.deleteNote(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun NotesList(
    notes: List<Note>,
    onNoteClick: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(notes, key = { it.id }) { note ->
            NoteItem(
                note = note,
                onNoteClick = onNoteClick,
                onDelete = onDelete
            )
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onNoteClick: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNoteClick(note.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = note.title, style = MaterialTheme.typography.titleMedium)
                Text(text = note.content, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = { onDelete(note.id) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
fun PreviewLoading() {
    MaterialTheme {
        Surface {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Preview(showBackground = true, name = "Success State")
@Composable
fun PreviewSuccess() {
    MaterialTheme {
        Surface {
            NotesList(
                notes = listOf(
                    Note("1", "Nota Académica 1", "Contenido de StateFlow"),
                    Note("2", "Evento One-shot", "Navegación con SharedFlow")
                ),
                onNoteClick = {},
                onDelete = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Error State")
@Composable
fun PreviewError() {
    MaterialTheme {
        Surface {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Error: No se pudieron cargar las notas",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
