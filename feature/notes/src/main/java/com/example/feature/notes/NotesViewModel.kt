package com.example.feature.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.Note
import com.example.core.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NotesUiState>(NotesUiState.Loading)
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<NotesEvent>()
    val event: SharedFlow<NotesEvent> = _event.asSharedFlow()

    init {
        repository.getNotes()
            .onEach { notes ->
                _uiState.value = NotesUiState.Success(notes)
            }
            .catch { e ->
                _uiState.value = NotesUiState.Error(e.message ?: "Error desconocido")
            }
            .launchIn(viewModelScope)
    }

    fun onNoteClicked(id: String) {
        viewModelScope.launch {
            _event.emit(NotesEvent.NavigateToDetail(id))
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            repository.deleteNote(id)
            _event.emit(NotesEvent.NoteDeleted)
        }
    }

    fun addDummyNote() {
        viewModelScope.launch {
            val newNote = Note(
                id = UUID.randomUUID().toString(),
                title = "Nota Nueva ${System.currentTimeMillis() % 1000}",
                content = "Contenido generado automáticamente."
            )
            repository.addNote(newNote)
        }
    }
}
