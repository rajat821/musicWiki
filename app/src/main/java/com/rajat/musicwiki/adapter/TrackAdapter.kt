package com.rajat.musicwiki.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rajat.musicwiki.R
import com.rajat.musicwiki.model.Track
import com.squareup.picasso.Picasso

class TrackAdapter (val context: Context, private val tracks:ArrayList<Track>): RecyclerView.Adapter<TrackAdapter.TrackViewHolder>(){

    class TrackViewHolder(view: View): RecyclerView.ViewHolder(view){
        val img : ImageView = view.findViewById(R.id.img)
        val title : TextView = view.findViewById(R.id.title)
        val artist : TextView = view.findViewById(R.id.sub_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_item_row_1, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]

        if(track.img.toString().isNotEmpty())
            Picasso.get().load(track.img).error(R.drawable.disc).fit().centerCrop().into(holder.img)
        else
            Picasso.get().load(R.drawable.disc).error(R.drawable.disc).fit().centerCrop().into(holder.img)

        holder.title.text = track.title
        holder.artist.text = track.artist
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}