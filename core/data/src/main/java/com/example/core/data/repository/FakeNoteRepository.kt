package com.example.core.data.repository

import com.example.core.domain.model.Note
import com.example.core.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeNoteRepository @Inject constructor() : NoteRepository {
    
    private val _notes = MutableStateFlow<List<Note>>(
        listOf(
            Note("1", "Nota de Bienvenida", "Esta es una nota inicial en memoria."),
            Note("2", "Recordatorio", "Comprar leche al salir del trabajo.")
        )
    )

    override fun getNotes(): Flow<List<Note>> = _notes

    override suspend fun addNote(note: Note) {
        _notes.update { currentList -> currentList + note }
    }

    override suspend fun deleteNote(id: String) {
        _notes.update { currentList -> currentList.filter { it.id != id } }
    }
}
