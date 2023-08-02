package com.lymors.earnxtra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var textView4: TextView
    private lateinit var textView5: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()
        // Set up references to the views
        emailEditText = findViewById(R.id.updatedEmail)
        passwordEditText = findViewById(R.id.updatedPassword)
        textView4 = findViewById(R.id.textView4)
        textView5 = findViewById(R.id.textView5)
        loginButton = findViewById(R.id.button)

        textView5.setOnClickListener{
            startActivity(Intent(this,SignUpActivity::class.java))
        }

        textView4.setOnClickListener{
            startActivity(Intent(this,SignUpActivity::class.java))
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Perform validation if needed

            val originalText = email
            val trimmedText = if (originalText.length > 4) {
                originalText.substring(0, originalText.length - 4)
            } else {
                // Handle the case when the text is less than 4 characters, if needed
                originalText
            }
            // Sign in with email and password
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Login success, navigate to the next activity
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("email",trimmedText)
                        startActivity(intent)
                        finish()
                    } else {
                        // Handle login error
                        Toast.makeText(this, "Wrong Credential", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

}