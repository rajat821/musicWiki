package com.rajat.musicwiki.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rajat.musicwiki.AlbumDetails
import com.rajat.musicwiki.GenreDetails
import com.rajat.musicwiki.R
import com.rajat.musicwiki.model.Album
import com.squareup.picasso.Picasso

class AlbumAdapter (val context: Context, private val albums:ArrayList<Album>): RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>(){

    class AlbumViewHolder(view: View): RecyclerView.ViewHolder(view){
        val relative : RelativeLayout = view.findViewById(R.id.relative)
        val img : ImageView = view.findViewById(R.id.img)
        val title : TextView = view.findViewById(R.id.title)
        val artist : TextView = view.findViewById(R.id.sub_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_item_row_1, parent, false)
        return AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albums[position]

        if(album.img.toString().isNotEmpty())
            Picasso.get().load(album.img).error(R.drawable.disc).fit().centerCrop().into(holder.img)
        else
            Picasso.get().load(R.drawable.disc).error(R.drawable.disc).fit().centerCrop().into(holder.img)
        holder.title.text = album.title
        holder.artist.text = album.artist

        holder.relative.setOnClickListener {
            val intent = Intent(context, AlbumDetails::class.java)
            intent.putExtra("album",album.title)
            intent.putExtra("artist",album.artist)
            (context as Activity).startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return albums.size
    }
}