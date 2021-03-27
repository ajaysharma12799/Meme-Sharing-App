package com.company.memesharing

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {

    var currentImageURL : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }

    private fun loadMeme() {

        //Fetching ImageView
        val MemeImageView = findViewById<ImageView>(R.id.memeImageView)
        // Fetching Progress Bar
        val ProgressBarView = findViewById<ProgressBar>(R.id.progressBar)

        ProgressBarView.visibility = View.VISIBLE
        // Meme URL
        val currentMemeURL = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
        val JsonRequest = JsonObjectRequest(Request.Method.GET, currentMemeURL, null,
            Response.Listener { response ->
                currentImageURL = response.getString("url")
                Glide.with(this).load(currentImageURL).listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        ProgressBarView.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        ProgressBarView.visibility = View.GONE
                        return false
                    }

                }).into(MemeImageView)
            },
            Response.ErrorListener {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            })

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(JsonRequest)
    }

    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, currentImageURL)
        startActivity(Intent.createChooser(intent, "Share This Meme With..."))
    }

    fun nextMeme(view: View) {
        loadMeme()
    }
}