package com.lymors.earnxtra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lymors.earnxtra.models.Users

class ResultActivity : AppCompatActivity() {

    lateinit var correctAns: TextView
    lateinit var totalAns:TextView
    lateinit var performance:TextView
    lateinit var output: LinearLayout
    private var diamonds: Int = 0
    private lateinit var userId: String
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        correctAns=findViewById(R.id.correctAns)
        totalAns=findViewById(R.id.totalAns)
        performance=findViewById(R.id.performance)
        output=findViewById(R.id.output)

        val intent = intent
        val correctAnsNo=intent.getStringExtra("correct")
        val totalAnsNo=intent.getStringExtra("total")
        correctAns.text=correctAnsNo
        totalAns.text=totalAnsNo

        val percentage= (correctAnsNo?.toFloat()?.div(totalAnsNo?.toFloat()!!))?.times(100)

        if (percentage != null) {
            when {
                50 <= percentage && percentage <= 99 -> {

                    performance.text="GOOD"
                    output.background=resources.getDrawable(R.drawable.option_bg)

                }
                percentage>=100 -> {
                    performance.text="EXCELLENT"
                    output.background=resources.getDrawable(R.drawable.right_bg)
                }
                percentage<50 -> {
                    performance.text="POOR"
                    output.background=resources.getDrawable(R.drawable.wrong_bg)
                }
            }


        }

        firebaseAuth = FirebaseAuth.getInstance()

        userId = firebaseAuth.currentUser?.uid ?: ""


        val ref = FirebaseDatabase.getInstance().getReference("users").child(userId)


        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(Users::class.java)
                    val userDiamonds = user?.totalDiamonds ?: 0

                    // Convert the correctAnsNo from String to Int
                    val correctAnswers = correctAnsNo?.toInt() ?: 0

                    // Calculate the new totalDiamonds value
                    val newTotalDiamonds = userDiamonds + correctAnswers

                    // Update the value in the database
                    FirebaseDatabase.getInstance().getReference("users")
                        .child(userId).child("totalDiamonds")
                        .setValue(newTotalDiamonds)
                        .addOnSuccessListener {
                            // The value has been successfully updated in the database
                            // You can add any further actions if needed here
                        }
                        .addOnFailureListener {
                            // Handle any errors that occur while updating the value
                        }
                } else {
                    // Handle the case if the data does not exist
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors that occur while retrieving data
            }
        })
    }

    override fun onBackPressed() {
        var intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}