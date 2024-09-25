package com.devdroid.mynotesapp.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.devdroid.mynotesapp.database.NoteDatabase
import com.devdroid.mynotesapp.database.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : NotesRepository

    val allnotes : LiveData<List<Note>>

    init {
        val dao = NoteDatabase.getDatabase(application).getNoteData()
        repository = NotesRepository(dao)
        allnotes = repository.allnotes
    }

    fun deleteNote(note : Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(note)
    }

    fun insertNote(note : Note) = viewModelScope.launch (Dispatchers.IO){
        repository.insert(note)
    }

    fun updateNote(note:Note) = viewModelScope.launch (Dispatchers.IO){
        repository.update(note)
    }

}