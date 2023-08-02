package com.lymors.earnxtra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.lymors.earnxtra.models.Users

class SignUpActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var referralCodeEditText: EditText
    private lateinit var signupButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("users")

        // Set up references to the views
        nameEditText = findViewById(R.id.updatedName)
        emailEditText = findViewById(R.id.updatedEmail)
        passwordEditText = findViewById(R.id.updatedPassword)
        phoneNumberEditText = findViewById(R.id.updatedPhoneNumber)
        referralCodeEditText = findViewById(R.id.referalCode)
        signupButton = findViewById(R.id.button)

        signupButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val phoneNumber = phoneNumberEditText.text.toString().trim()
            val referralCode = referralCodeEditText.text.toString().trim()

            if (!validateName() || !validateEmail() || !validatePassword() || !validatePhoneNo() || !validateReferalCode()) {
                return@setOnClickListener
            }

            databaseReference.orderByChild("referalCode").equalTo(referralCode)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            // User with the matching referral code found
                            for (userSnapshot in snapshot.children) {
                                val matchedUser = userSnapshot.getValue(Users::class.java)
                                // Do something with the matched user data
                                // For example, log the user data
                                matchedUser?.let {
                                    matchedUser.totalDiamonds+=50
                                }
                            }
                        } else {
                            // No user with the entered referral code found
                            Log.d("User Data", "No user found with the entered referral code.")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle any errors that occur during the query
                        Log.e("Firebase Query Error", "Error querying Firebase: ${error.message}")
                    }
                })

            // Create user with email and password
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = firebaseAuth.currentUser?.uid ?: ""
//                            val userMap = hashMapOf(
//                                "name" to name,
//                                "email" to email,
//                                "phone" to phoneNumber,
//                                "referralCode" to referralCode
//                            )

                        val user = Users(name, email, phoneNumber, password,referralCode,50)
                        // Save user data in the Firebase Realtime Database
                        val originalText = email
                        val trimmedText = if (originalText.length > 4) {
                            originalText.substring(0, originalText.length - 4)
                        } else {
                            // Handle the case when the text is less than 4 characters, if needed
                            originalText
                        }

// Set the trimmed text to the TextView
                        databaseReference.child(userId).setValue(user)
                            .addOnCompleteListener { databaseTask ->
                                if (databaseTask.isSuccessful) {
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this, "Couldn't SignUp", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "SignUp Error", Toast.LENGTH_SHORT).show()
                    }
                }

    // Perform validation if needed

        }
    }

    private fun validateName(): Boolean {
        val name = nameEditText.text.toString()
        if (name.isEmpty()) {
            nameEditText.error = "Field cannot be empty"
            return false
        } else {
            nameEditText.error = null
            return true
        }
    }


    private fun validatePassword(): Boolean {
        val passwordTry = passwordEditText.text.toString()
        val passwordPattern = "^(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"

        if (passwordTry.isEmpty()) {
            passwordEditText.error = "Field cannot be empty"
            return false
        } else if (!passwordTry.matches(passwordPattern.toRegex())) {
            passwordEditText.error = "Password is too weak"
            return false
        } else {
            passwordEditText.error = null
            return true
        }
    }


    private fun validateEmail(): Boolean {
        val emailVar = emailEditText.text.toString()
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        if (emailVar.isEmpty()) {
            emailEditText.error = "Field cannot be empty"
            return false
        } else if (!emailVar.matches(emailPattern.toRegex())) {
            emailEditText.error = "Invalid email address"
            return false
        } else {
            emailEditText.error = null
            return true
        }
    }

    private fun validatePhoneNo(): Boolean {
        val name = phoneNumberEditText.text.toString()
        if (name.isEmpty()) {
            phoneNumberEditText.error = "Field cannot be empty"
            return false
        } else {
            phoneNumberEditText.error = null
            return true
        }
    }

    private fun validateReferalCode(): Boolean {
        val name = referralCodeEditText.text.toString()
        if (name.isEmpty()) {
            phoneNumberEditText.error = "Field cannot be empty"
            return false
        } else {
            phoneNumberEditText.error = null
            return true
        }
    }
}