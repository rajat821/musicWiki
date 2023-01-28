package com.rajat.musicwiki

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.rajat.musicwiki.adapter.GenreAdapter
import com.rajat.musicwiki.utils.ConnectionManager
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    lateinit var adapter : GenreAdapter
    lateinit var layoutManager : RecyclerView.LayoutManager
    var tags = ArrayList<String>()

    var isDown= false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutManager = GridLayoutManager(this,2)

        getData(false)

        down.setOnClickListener {
            if(isDown){
                down.setBackgroundResource(R.drawable.ic_arrow_up)
                getData(true)
            }
            else{
                down.setBackgroundResource(R.drawable.ic_arrow_down)
                getData(false)
            }
            isDown = !isDown
        }
    }

    private fun getData(show : Boolean){
        val queue = Volley.newRequestQueue(this@MainActivity)
        val url = "https://ws.audioscrobbler.com/2.0/?method=tag.getTopTags&api_key=61a8ca9e7c0c8b4292706b7999844055&format=json"

        if (ConnectionManager().checkConnectivity(this@MainActivity)) {
            val jsonRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {

                        val topTags = it.getJSONObject("toptags")
                        val tagArr = topTags.getJSONArray("tag")
                        for (i in 0 until tagArr.length()){
                            val state = tagArr.getJSONObject(i)
                            val details = state.getString("name")
                            tags.add(details)
                        }

                        adapter = GenreAdapter(this@MainActivity, tags, show)
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = layoutManager

                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@MainActivity,
                            "Unable to Fetch Data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(this@MainActivity, "Unable to Fetch Data", Toast.LENGTH_SHORT)
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
            val dialog = AlertDialog.Builder(this@MainActivity)
            dialog.setTitle("Failed")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(this@MainActivity)
            }
            dialog.create()
            dialog.show()
        }
    }
}