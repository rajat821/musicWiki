package com.rajat.musicwiki

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rajat.musicwiki.adapter.AlbumAdapter
import com.rajat.musicwiki.adapter.GenreAdapter
import com.rajat.musicwiki.adapter.TrackAdapter
import com.rajat.musicwiki.model.Album
import com.rajat.musicwiki.model.Track
import com.rajat.musicwiki.utils.ConnectionManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_artist_details.*
import org.json.JSONException

class ArtistDetails : AppCompatActivity() {

    lateinit var adapter1 : GenreAdapter
    lateinit var adapter2 : TrackAdapter
    lateinit var adapter3 : AlbumAdapter

    lateinit var layoutManager1 : RecyclerView.LayoutManager
    lateinit var layoutManager2 : RecyclerView.LayoutManager
    lateinit var layoutManager3 : RecyclerView.LayoutManager

    var tags = ArrayList<String>()
    var albumsArr = ArrayList<Album>()
    var tracksArr = ArrayList<Track>()

    var artistStr = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_details)

        if(intent!=null){
            artistStr=intent.getStringExtra("artist").toString()
        }

        layoutManager1= LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        layoutManager2= LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        layoutManager3= LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)

        getData()
    }

    private fun getData(){
        val queue = Volley.newRequestQueue(this@ArtistDetails)
        val url1 = "https://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=$artistStr&api_key=61a8ca9e7c0c8b4292706b7999844055&format=json"
        val url2 = "https://ws.audioscrobbler.com/2.0/?method=artist.gettoptracks&artist=$artistStr&api_key=61a8ca9e7c0c8b4292706b7999844055&format=json"
        val url3 = "https://ws.audioscrobbler.com/2.0/?method=artist.gettopalbums&artist=$artistStr&api_key=61a8ca9e7c0c8b4292706b7999844055&format=json"


        if (ConnectionManager().checkConnectivity(this@ArtistDetails)) {
            val jsonRequest1 =
                object : JsonObjectRequest(Request.Method.GET, url1, null, Response.Listener {
                    try {
                        val artists = it.getJSONObject("artist")
                        artist.text = artists.getString("name")
                        val tag = artists.getJSONObject("tags")
                        val tagArr = tag.getJSONArray("tag")
                        for (i in 0 until tagArr.length()){
                            val state = tagArr.getJSONObject(i)
                            val details = state.getString("name")
                            tags.add(details)
                        }
                        val img=artists.getJSONArray("image").getJSONObject(3).getString("#text")
                        if(img.isNotEmpty())
                            Picasso.get().load(img).error(R.drawable.disc).fit().centerCrop().into(cover)
                        else
                            Picasso.get().load(R.drawable.disc).error(R.drawable.disc).fit().centerCrop().into(cover)

                        try {
                            description.text=artists.getJSONObject("bio").getString("summary")
                        }
                        catch (e: JSONException)
                        {
                            Toast.makeText(this@ArtistDetails,"No Description", Toast.LENGTH_SHORT).show()
                        }
                        play_count.text = artists.getJSONObject("stats").getString("playcount")
                        followers.text = artists.getJSONObject("stats").getString("listeners")

                        adapter1 = GenreAdapter(this@ArtistDetails, tags, true)
                        recyclerView.adapter = adapter1
                        recyclerView.layoutManager = layoutManager1

                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@ArtistDetails,
                            "Unable to Fetch Data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(this@ArtistDetails, "Unable to Fetch Data", Toast.LENGTH_SHORT)
                        .show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["User-Agent"] = "Mozilla/5.0"
                        return headers
                    }
                }

            val jsonRequest2 =
                object : JsonObjectRequest(Request.Method.GET, url2, null, Response.Listener {
                    try {

                        val tracks = it.getJSONObject("toptracks")
                        val track = tracks.getJSONArray("track")
                        for (i in 0 until track.length()){
                            val single = track.getJSONObject(i)
                            val details = Track(
                                single.getJSONArray("image").getJSONObject(2).getString("#text"),
                                single.getString("name"),
                                single.getJSONObject("artist").getString("name"))
                            tracksArr.add(details)
                        }

                        adapter2 = TrackAdapter(this@ArtistDetails, tracksArr)
                        recyclerTrack.adapter = adapter2
                        recyclerTrack.layoutManager = layoutManager2

                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@ArtistDetails,
                            "Unable to Fetch Data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(this@ArtistDetails, "Unable to Fetch Data", Toast.LENGTH_SHORT)
                        .show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["User-Agent"] = "Mozilla/5.0"
                        return headers
                    }
                }

            val jsonRequest3 =
                object : JsonObjectRequest(Request.Method.GET, url3, null, Response.Listener {
                    try {
                        val albums = it.getJSONObject("topalbums")
                        val album = albums.getJSONArray("album")
                        for (i in 0 until album.length()){
                            val single = album.getJSONObject(i)
                            val details = Album(
                                single.getJSONArray("image").getJSONObject(2).getString("#text"),
                                single.getString("name"),
                                single.getJSONObject("artist").getString("name"))
                            albumsArr.add(details)
                        }
                        adapter3 = AlbumAdapter(this@ArtistDetails, albumsArr)
                        recyclerAlbum.adapter = adapter3
                        recyclerAlbum.layoutManager = layoutManager3

                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@ArtistDetails,
                            "Unable to Fetch Data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(this@ArtistDetails, "Unable to Fetch Data", Toast.LENGTH_SHORT)
                        .show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["User-Agent"] = "Mozilla/5.0"
                        return headers
                    }
                }

            queue.add(jsonRequest1)
            queue.add(jsonRequest2)
            queue.add(jsonRequest3)
        }
        else {
            val dialog = AlertDialog.Builder(this@ArtistDetails)
            dialog.setTitle("Failed")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(this@ArtistDetails)
            }
            dialog.create()
            dialog.show()
        }
    }
}