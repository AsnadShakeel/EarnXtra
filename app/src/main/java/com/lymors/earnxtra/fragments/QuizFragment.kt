package com.lymors.earnxtra.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.lymors.earnxtra.QuizActivity
import com.lymors.earnxtra.R

class QuizFragment : Fragment() {

    lateinit var startQuiz:Button

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_quiz, container, false)

        startQuiz = view.findViewById(R.id.start)

        startQuiz.setOnClickListener {
            val intent = Intent(context,QuizActivity::class.java)
            startActivity(intent)
        }

        return view
    }

}