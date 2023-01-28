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
import com.rajat.musicwiki.ArtistDetails
import com.rajat.musicwiki.R
import com.rajat.musicwiki.model.Artist
import com.squareup.picasso.Picasso

class ArtistAdapter (val context: Context, private val artists:ArrayList<Artist>): RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>(){

    class ArtistViewHolder(view: View): RecyclerView.ViewHolder(view){
        val relative : RelativeLayout = view.findViewById(R.id.relative)
        val img : ImageView = view.findViewById(R.id.img)
        val title : TextView = view.findViewById(R.id.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_item_row_2, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = artists[position]

        if(artist.img.toString().isNotEmpty())
            Picasso.get().load(artist.img).error(R.drawable.disc).fit().centerCrop().into(holder.img)
        else
            Picasso.get().load(R.drawable.disc).error(R.drawable.disc).fit().centerCrop().into(holder.img)
        holder.title.text = artist.title

        holder.relative.setOnClickListener {
            val intent = Intent(context, ArtistDetails::class.java)
            intent.putExtra("artist",artist.title)
            (context as Activity).startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return artists.size
    }
}