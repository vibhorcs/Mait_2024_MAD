package com.devdroid.mynotesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.devdroid.mynotesapp.databinding.ActivityAddNoteBinding
import com.devdroid.mynotesapp.models.Note
import java.text.SimpleDateFormat
import java.util.*

class AddNote : AppCompatActivity() {

    private lateinit var binding : ActivityAddNoteBinding

    private lateinit var note: com.devdroid.mynotesapp.models.Note
    private lateinit var old_note : Note
    var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding  = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        try{
            old_note = intent.getSerializableExtra("current_note") as Note
            binding.editTitle.setText(old_note.title)
            binding.edtNote.setText(old_note.note)
            isUpdate = true
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }

        binding.imgCheck.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val noteDesc = binding.edtNote.text.toString()

            if(title.isNotEmpty() || noteDesc.isNotEmpty() ){
                val formatter = SimpleDateFormat("EEE,d MMM yyyy HH:mm a")

                if(isUpdate){
                    note = Note(
                        old_note.id,
                        title,
                        noteDesc,
                        formatter.format(Date())
                    )
                }else{
                    note = com.devdroid.mynotesapp.models.Note(
                        null,
                        title,
                        noteDesc,
                        formatter.format(Date())
                    )
            }
                val intent = Intent()
                intent.putExtra("note",note)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }else{
                Toast.makeText(this@AddNote,"Please enter some data",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


        }
        binding.imgBack.setOnClickListener {
           onBackPressed()
        }
    }
}