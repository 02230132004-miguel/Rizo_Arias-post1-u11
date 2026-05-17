package com.example.feature.notes

sealed class NotesEvent {
    data class NavigateToDetail(val noteId: String) : NotesEvent()
    data object NoteDeleted : NotesEvent()
}
