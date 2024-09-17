package com.devdroid.mynotesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.devdroid.mynotesapp.Adapter.notesAdapter
import com.devdroid.mynotesapp.database.NoteDatabase
import com.devdroid.mynotesapp.databinding.ActivityMainBinding
import com.devdroid.mynotesapp.models.Note
import com.devdroid.mynotesapp.models.NoteViewModel

class MainActivity : AppCompatActivity(), notesAdapter.NotesClickListener,PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var database : NoteDatabase

    private lateinit var viewModel : NoteViewModel
    lateinit var adapter : notesAdapter
    lateinit var selectedNote : Note

    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if(result.resultCode == Activity.RESULT_OK){
            val note = result.data?.getSerializableExtra("note")as? Note
            if(note!=null){
                viewModel.updateNote(note)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //
        initUI()

        viewModel = ViewModelProvider(this,
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)

        viewModel.allnotes.observe(this) { list ->
            list?.let {
                adapter.updateList(list)
            }
        }

        database =  NoteDatabase.getDatabase(this)

    }

    private fun initUI(){

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2,LinearLayout.VERTICAL)
        adapter = notesAdapter(this, this)
        binding.recyclerView.adapter = adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->

            if(result.resultCode == Activity.RESULT_OK){
                val note = result.data?.getSerializableExtra("note")as? Note
                if(note!=null){
                    viewModel.insertNote(note)
                }
            }

        }

        binding.fbAddNote.setOnClickListener {
            val intent = Intent(this,AddNote::class.java)
            getContent.launch(intent)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
               return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if(p0!=null){
                    adapter.filterList(p0)
                }
                return true
            }

        })

    }

    override fun onItemClicked(note: Note) {
        val intent = Intent(this@MainActivity,AddNote::class.java)
        intent.putExtra("current_note",note)
        updateNote.launch(intent)
    }

    override fun onLongItemClicked(note: Note, cardView: CardView) {
        selectedNote = note;
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView:CardView){
        val popup = PopupMenu(this,cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.deleteNote){
            viewModel.deleteNote(selectedNote)
            return true
        }
        return false
    }
}