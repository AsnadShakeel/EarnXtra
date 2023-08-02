package com.lymors.earnxtra

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth

class WithDrawActivity : AppCompatActivity() {

    lateinit var bankingDetails: EditText
    lateinit var withdrawButton: AppCompatButton
    lateinit var spinner: Spinner
    var userTotalDiamonds: Int = 0 // Change the type to Int
    var phoneNumber: String = ""
    var email: String = ""
    var userName: String = ""
    lateinit var firebaseAuth: FirebaseAuth
    var userId: String = ""

    @SuppressLint("ResourceAsColor", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_with_draw)

        bankingDetails = findViewById(R.id.phoneNumberEditText)
        withdrawButton = findViewById(R.id.withdrawButton)

        firebaseAuth = FirebaseAuth.getInstance()
        userId = firebaseAuth.currentUser!!.uid

        userTotalDiamonds = intent.getIntExtra("totalDiamonds", 0) // Get the integer value from the intent
        phoneNumber = intent.getStringExtra("phone").toString()
        userName = intent.getStringExtra("userName").toString()
        email = intent.getStringExtra("email").toString()

        // Sample data for the spinner options
        val withdrawOptions = arrayOf("Supported Banks Are","Easy Paisa", "Jazz Cash", "Binance", "NayaPay","Western Union")

        spinner = findViewById(R.id.spinnerOptions)
        val adapter = ArrayAdapter(this, R.layout.spinner_item_dropdown, withdrawOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Set an item selected listener for the spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // You can perform any actions based on the selected option here
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case when nothing is selected
            }
        }

        // Enable or disable the withdraw button based on totalDiamonds value from the database
        if (userTotalDiamonds >= 200) {
            withdrawButton.isEnabled = true
        } else {
            withdrawButton.isEnabled = false
            withdrawButton.setBackgroundColor(R.color.gray)
            withdrawButton.setTextColor(R.color.white)
            Toast.makeText(this, "Your diamonds are not enough for withdrawal", Toast.LENGTH_SHORT).show()
        }
    }

    fun onWithdrawButtonClick(view: View) {
        if (userTotalDiamonds >= 200) {
            if (bankingDetails.text.isNotEmpty()) {
                // Create an Intent to send data to the admin app
//                val intent = Intent(Intent.ACTION_SEND).apply {
//                    // Put the necessary data as extras in the Intent
//                    putExtra("userName", userName)
//                    putExtra("email", email)
//                    putExtra("phoneNumber", bankingDetails.text.toString())
//                    putExtra("spinnerItem", spinner.selectedItem.toString())
//                }
//                // Set the action to a unique identifier that the admin app can listen to
//                intent.action = "com.lymors.adminearnxtra.ACTION_RECEIVE_DATA"
//
//
//                // Set the package name of the admin app. Replace 'com.lymors.adminearnxtra' with the actual package name of the admin app.
//                intent.setPackage("com.lymors.adminearnxtra")
//
//                // Start the activity in the admin app
//                startActivity(intent)

                val intent = Intent("com.lymors.adminearnxtra.ACTION_SEND_DATA").apply {
                    // Put the necessary data as extras in the Intent
                    putExtra("userName", userName)
                    putExtra("email", email)
                    putExtra("userId", userId)
                    putExtra("phoneNumber", bankingDetails.text.toString())
                    putExtra("spinnerItem", spinner.selectedItem.toString())
                }
// Set the package name of the admin app. Replace 'com.lymors.adminearnxtra' with the actual package name of the admin app.
                intent.setPackage("com.lymors.adminearnxtra")

// Send the broadcast with the intent containing the data to the admin app
                sendBroadcast(intent)

                Toast.makeText(this, "Withdraw Request Sent To Admin", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Phone Number Cannot Be Empty", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Your diamonds are not enough for withdrawal", Toast.LENGTH_SHORT).show()
        }
    }
}