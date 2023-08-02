package com.lymors.earnxtra

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lymors.earnxtra.models.Users

class EditProfile : AppCompatActivity() {

    lateinit var name:String
    lateinit var email:String
    lateinit var password:String
    lateinit var phoneNumber:String
    lateinit var referalCode:String
    var totalDiamonds:Int = 0
    lateinit var updatedName:EditText
    lateinit var updatedPassword:EditText
    lateinit var updatedPhoneNumber:EditText
    lateinit var updatedEmail:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        name = intent.getStringExtra("name").toString()
        email = intent.getStringExtra("email").toString()
        password = intent.getStringExtra("password").toString()
        phoneNumber = intent.getStringExtra("phone").toString()
        referalCode = intent.getStringExtra("referalCode").toString()
        totalDiamonds = intent.getIntExtra("totalDiamonds",0)

        updatedName = findViewById(R.id.updatedName)
        updatedPassword = findViewById(R.id.updatedPassword)
        updatedPhoneNumber = findViewById(R.id.updatedPhoneNumber)
        updatedEmail = findViewById(R.id.updatedEmail)

        updatedName.setText(name)
        updatedPassword.setText(password)
        updatedPhoneNumber.setText(phoneNumber)
        updatedEmail.setText(email)

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val currentUserID: String = firebaseAuth.currentUser?.uid ?: ""
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val userRef: DatabaseReference = database.reference.child("users").child(currentUserID)
        val btnUpdate: Button = findViewById(R.id.btnUpdate)

        btnUpdate.setOnClickListener {
            val updatedName: String = updatedName.text.toString().trim()
            val updatedEmail: String = updatedEmail.text.toString().trim()
            val updatedPassword: String = updatedPassword.text.toString().trim()
            val updatedPhoneNumber: String = updatedPhoneNumber.text.toString().trim()

            // Update the user information in the database
            val updatedUser = Users(updatedName,updatedEmail,updatedPhoneNumber,updatedPassword,referalCode,totalDiamonds)
            userRef.setValue(updatedUser)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                    // Do any other actions if needed after successful update
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Failed to update profile. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}