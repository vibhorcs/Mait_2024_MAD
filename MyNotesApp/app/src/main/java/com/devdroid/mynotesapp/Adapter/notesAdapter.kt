package com.devdroid.mynotesapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.devdroid.mynotesapp.R
import com.devdroid.mynotesapp.models.Note
import kotlin.random.Random

class notesAdapter(private val context : Context , val listener : NotesClickListener) : RecyclerView.Adapter<notesAdapter.NoteViewHolder>() {

    private val NotesList = ArrayList<Note>()
    private val fullList = ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item,parent , false))
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = NotesList[position]
        holder.title.text = currentNote.title
        holder.title.isSelected = true

        holder.note.text = currentNote.note
        holder.date.text = currentNote.date
        holder.date.isSelected = true

        holder.notes_layout.setCardBackgroundColor(holder.itemView.resources.getColor(randomColor(), null))

        holder.notes_layout.setOnClickListener {
            listener.onItemClicked(NotesList[holder.adapterPosition])
        }

        holder.notes_layout.setOnLongClickListener {
            listener.onLongItemClicked(NotesList[holder.adapterPosition],holder.notes_layout)
            true
        }


    }

    override fun getItemCount(): Int {
        return NotesList.size
    }

    fun updateList(newList : List<Note>){
        fullList.clear()
        fullList.addAll(newList)

        NotesList.clear()
        NotesList.addAll(fullList)
        notifyDataSetChanged()
    }

    fun filterList(search : String){

        NotesList.clear()

        for(item in fullList){

            if(item.title?.lowercase()?.contains(search.lowercase())== true ||
                item.note?.lowercase()?.contains(search.lowercase())== true){

                NotesList.add(item)

            }

        }

        notifyDataSetChanged()

    }

    fun randomColor():Int{
        val list = ArrayList<Int>()
        list.add(R.color.NoteColor1)
        list.add(R.color.NoteColor2)
        list.add(R.color.NoteColor3)
        list.add(R.color.NoteColor4)
        list.add(R.color.NoteColor5)
        list.add(R.color.NoteColor6)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]

    }

    inner class NoteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val notes_layout = itemView.findViewById<CardView>(R.id.cardLayout)
        val title = itemView.findViewById<TextView>(R.id.txt_title)
        val note = itemView.findViewById<TextView>(R.id.txt_note)
        val date = itemView.findViewById<TextView>(R.id.txt_date)
    }

    interface NotesClickListener{

        fun onItemClicked(note:Note)
        fun onLongItemClicked(note:Note,cardView:CardView)

    }

}