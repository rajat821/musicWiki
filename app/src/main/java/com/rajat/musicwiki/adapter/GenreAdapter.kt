package com.rajat.musicwiki.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rajat.musicwiki.GenreDetails
import com.rajat.musicwiki.R

class GenreAdapter(val context: Context, val genres:ArrayList<String>, val show : Boolean): RecyclerView.Adapter<GenreAdapter.GenreViewHolder>(){

    class GenreViewHolder(view: View): RecyclerView.ViewHolder(view){
        val tagName : TextView = view.findViewById(R.id.tagName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_item_genre, parent, false)
        return GenreViewHolder(view)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = genres[position]
        holder.tagName.text = genre

        holder.tagName.setOnClickListener {
            val intent = Intent(context,GenreDetails::class.java)
            intent.putExtra("tag",genre)
            (context as Activity).startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        if(!show && genres.size>10) return 10
        return genres.size
    }
}