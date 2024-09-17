package com.devdroid.mynotesapp.database

import androidx.lifecycle.LiveData
import com.devdroid.mynotesapp.models.Note

class NotesRepository(private val noteDao : NoteDao) {

    val allnotes : LiveData<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note:Note){
        noteDao.insert(note)
    }

    suspend fun delete(note:Note){
        noteDao.delete(note)
    }

    suspend fun update(notes:Note){

        noteDao.update(notes.id , notes.title , notes.note)

    }

}