package com.rajat.musicwiki

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayout
import com.rajat.musicwiki.adapter.TabAccessAdapterGenre
import com.rajat.musicwiki.utils.ConnectionManager
import kotlinx.android.synthetic.main.activity_genre_details.*
import org.json.JSONException

class GenreDetails : AppCompatActivity() {

    lateinit var myViewPager : ViewPager
    lateinit var myTabLayout: TabLayout
    lateinit var myAdapter: TabAccessAdapterGenre

    var tag = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre_details)

        if(intent!=null){
            tag = intent.getStringExtra("tag").toString()
        }


        myViewPager = findViewById(R.id.viewPager)
        myAdapter = TabAccessAdapterGenre(supportFragmentManager, tag)
        myViewPager.adapter = myAdapter
        myTabLayout = findViewById(R.id.tabLayout)
        myTabLayout.setupWithViewPager(myViewPager)

        getData()

    }


    private fun getData(){
        val queue = Volley.newRequestQueue(this@GenreDetails)
        val url = "http://ws.audioscrobbler.com/2.0/?method=tag.getinfo&tag=${tag}&api_key=61a8ca9e7c0c8b4292706b7999844055&format=json"

        if (ConnectionManager().checkConnectivity(this@GenreDetails)) {
            val jsonRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {

                        val tags = it.getJSONObject("tag")
                        val wiki = tags.getJSONObject("wiki")
                        val summary = wiki.getString("summary")

                        header.text = tag.capitalize()
                        description.text = summary


                    } catch (e: JSONException) {
                        Log.d("raiiiiiiii",e.toString())
                        Toast.makeText(
                            this@GenreDetails,
                            "Unable to Fetch Data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, Response.ErrorListener {
                    Log.d("raiiiiiiiijjjjj",it.toString())
                    Toast.makeText(this@GenreDetails, "Unable to Fetch Data", Toast.LENGTH_SHORT)
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
            val dialog = AlertDialog.Builder(this@GenreDetails)
            dialog.setTitle("Failed")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(this@GenreDetails)
            }
            dialog.create()
            dialog.show()
        }
    }
}