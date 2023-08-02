package com.lymors.earnxtra

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {
    lateinit var name:String
    lateinit var phone:String
    lateinit var email:String
    lateinit var password:String
    lateinit var referalCode:String
    var totalDiamonds:Int = 0
    lateinit var username:TextView
    lateinit var btnEditProfile:Button
    lateinit var phoneNumber:TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        name = intent.getStringExtra("name").toString()
        phone = intent.getStringExtra("phone").toString()
        email = intent.getStringExtra("email").toString()
        password = intent.getStringExtra("password").toString()
        referalCode = intent.getStringExtra("referalCode").toString()
        totalDiamonds = intent.getIntExtra("totalDiamonds",0)

        username = findViewById(R.id.myName)
        phoneNumber = findViewById(R.id.phone)
        btnEditProfile = findViewById(R.id.btnEditProfile)

        btnEditProfile.setOnClickListener {
            val intent = Intent(this,EditProfile::class.java)
            intent.putExtra("name",name)
            intent.putExtra("email",email)
            intent.putExtra("password",password)
            intent.putExtra("phone",phone)
            intent.putExtra("referalCode",referalCode)
            intent.putExtra("totalDiamonds",totalDiamonds)
            startActivity(intent)
        }

        username.text = name
        phoneNumber.text = phone
    }
}