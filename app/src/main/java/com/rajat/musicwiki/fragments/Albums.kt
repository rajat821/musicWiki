package com.rajat.musicwiki.fragments

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rajat.musicwiki.R
import com.rajat.musicwiki.adapter.AlbumAdapter
import com.rajat.musicwiki.model.Album
import org.json.JSONException

class Albums(private val tagName : String) : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter : AlbumAdapter
    lateinit var layoutManager :RecyclerView.LayoutManager

    var albumsArr = ArrayList<Album>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_albums, container, false)
        recyclerView = view.findViewById(R.id.recyclerAlbum)
        layoutManager = GridLayoutManager(context,2)
        getData()
        return view
    }

    private fun getData(){
        val queue = Volley.newRequestQueue(context)
        val url = "http://ws.audioscrobbler.com/2.0/?method=tag.gettopalbums&tag=$tagName&api_key=61a8ca9e7c0c8b4292706b7999844055&format=json"

        val jsonRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {

                        val albums = it.getJSONObject("albums")
                        val album = albums.getJSONArray("album")
                        for (i in 0 until album.length()){
                            val single = album.getJSONObject(i)
                            val details = Album(
                                single.getJSONArray("image").getJSONObject(2).getString("#text"),
                                single.getString("name"),
                                single.getJSONObject("artist").getString("name"))
                            albumsArr.add(details)
                        }

                        adapter = AlbumAdapter(context as Activity, albumsArr)
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