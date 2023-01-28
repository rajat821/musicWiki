package com.rajat.musicwiki.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rajat.musicwiki.R
import com.rajat.musicwiki.adapter.AlbumAdapter
import com.rajat.musicwiki.adapter.ArtistAdapter
import com.rajat.musicwiki.model.Album
import com.rajat.musicwiki.model.Artist
import org.json.JSONException

class Artists(val tagName : String) : Fragment() {


    lateinit var recyclerView: RecyclerView
    lateinit var adapter : ArtistAdapter
    lateinit var layoutManager : RecyclerView.LayoutManager

    var artistsArr = ArrayList<Artist>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_artists, container, false)

        recyclerView = view.findViewById(R.id.recyclerArtist)
        layoutManager = GridLayoutManager(context,2)
        getData()
        return view
    }


    private fun getData(){
        val queue = Volley.newRequestQueue(context)
        val url = "http://ws.audioscrobbler.com/2.0/?method=tag.gettopartists&tag=$tagName&api_key=61a8ca9e7c0c8b4292706b7999844055&format=json"

        val jsonRequest =
            object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                try {

                    val artists = it.getJSONObject("topartists")
                    val artist = artists.getJSONArray("artist")
                    for (i in 0 until artist.length()){
                        val single = artist.getJSONObject(i)
                        val details = Artist(
                            single.getJSONArray("image").getJSONObject(2).getString("#text"),
                            single.getString("name"))
                        artistsArr.add(details)
                    }

                    adapter = ArtistAdapter(context as Activity, artistsArr)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = layoutManager

                } catch (e: JSONException) {
                    Toast.makeText(
                        context,
                        "Unable to Fetch Data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }, Response.ErrorListener {
                Toast.makeText(context, "Unable to Fetch Data", Toast.LENGTH_SHORT)
                    .show()
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["User-Agent"] = "Mozilla/5.0"
                    return headers
                }
            }

        queue.add(jsonRequest)
    }
}