package com.rajat.musicwiki

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rajat.musicwiki.adapter.GenreAdapter
import com.rajat.musicwiki.utils.ConnectionManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_album_details.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.recyclerView
import org.json.JSONException

class AlbumDetails : AppCompatActivity() {

    lateinit var adapter : GenreAdapter
    lateinit var layoutManager : RecyclerView.LayoutManager
    var tags = ArrayList<String>()

    var albumStr = ""
    var artistStr = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_details)

        if(intent!=null){
            albumStr=intent.getStringExtra("album").toString()
            artistStr=intent.getStringExtra("artist").toString()
        }

        layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        getData()

    }

    private fun getData(){
        val queue = Volley.newRequestQueue(this@AlbumDetails)
        val url = "https://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key=61a8ca9e7c0c8b4292706b7999844055&artist=$artistStr&album=$albumStr&format=json"

        if (ConnectionManager().checkConnectivity(this@AlbumDetails)) {
            val jsonRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {

                        val albums = it.getJSONObject("album")
                        artist.text = albums.getString("artist")
                        val tag = albums.getJSONObject("tags")
                        val tagArr = tag.getJSONArray("tag")
                        for (i in 0 until tagArr.length()){
                            val state = tagArr.getJSONObject(i)
                            val details = state.getString("name")
                            tags.add(details)
                        }
                        val img=albums.getJSONArray("image").getJSONObject(3).getString("#text")
                        if(img.isNotEmpty())
                            Picasso.get().load(img).error(R.drawable.disc).fit().centerCrop().into(cover)
                        else
                            Picasso.get().load(R.drawable.disc).error(R.drawable.disc).fit().centerCrop().into(cover)

                        album.text=albums.getString("name")
                        try {
                            description.text=albums.getJSONObject("wiki").getString("summary")
                        }
                        catch (e:JSONException)
                        {
                            Toast.makeText(this@AlbumDetails,"No Description",Toast.LENGTH_SHORT).show()
                        }


                        adapter = GenreAdapter(this@AlbumDetails, tags, true)
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = layoutManager

                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@AlbumDetails,
                            "Unable to Fetch Data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(this@AlbumDetails, "Unable to Fetch Data", Toast.LENGTH_SHORT)
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
        else {
            val dialog = AlertDialog.Builder(this@AlbumDetails)
            dialog.setTitle("Failed")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(this@AlbumDetails)
            }
            dialog.create()
            dialog.show()
        }
    }
}