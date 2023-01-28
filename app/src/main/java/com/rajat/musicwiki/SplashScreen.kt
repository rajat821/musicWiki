package com.rajat.musicwiki

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.rajat.musicwiki.utils.ConnectionManager

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.red_700)
        getWindow().decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        if (ConnectionManager().checkConnectivity(this@SplashScreen)) {

            Handler().postDelayed({
                    val startAct = Intent(
                        this@SplashScreen,
                        MainActivity::class.java
                    )
                    startActivity(startAct)
                    finish()
            }, 2000)
        }else {
            val dialog = AlertDialog.Builder(this@SplashScreen)
            dialog.setTitle("Failed")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(this@SplashScreen)
            }
            dialog.create()
            dialog.show()
        }

    }
}