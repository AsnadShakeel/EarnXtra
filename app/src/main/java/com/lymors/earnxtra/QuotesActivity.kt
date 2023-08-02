package com.lymors.earnxtra

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import android.widget.TextView
import kotlin.random.Random

class QuotesActivity : AppCompatActivity() {
    private val imageResourceIds = arrayOf(
        R.drawable.bg1,
        R.drawable.bg_2,
        R.drawable.bg3,
        R.drawable.bg_4,
        R.drawable.bg5,
        // Add more image resource IDs as needed
    )

    private val colorsIds = arrayOf(
        R.color.c1,
        R.color.c2
//        R.color.c3,
//        R.color.c4,
//        R.color.c5,
//        R.color.c6,
//        R.color.c7,
//        R.color.c8,
//        R.color.c9,
//        R.color.c10,
//        R.color.c11,
//        R.color.c12,
//        R.color.c13
        // Add more image resource IDs as needed
    )
    lateinit var imageView:ImageView
    lateinit var quotesTextView:TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotes)

        imageView = findViewById(R.id.imageView6)
        quotesTextView = findViewById(R.id.quotesTextView)

        val quoteText = intent.getStringExtra("text")
        quotesTextView.text = quoteText

        val randomImageId = getRandomImageResourceId()
        imageView.setImageResource(randomImageId)

        val color = getRandomColor()
        quotesTextView.setTextColor(color)

        imageView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        imageView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
    }

    private fun getRandomImageResourceId(): Int {
        val random = Random
        return imageResourceIds[random.nextInt(imageResourceIds.size)]
    }

    private fun getRandomColor(): Int {
        val random = Random
        return colorsIds[random.nextInt(colorsIds.size)]
    }
}