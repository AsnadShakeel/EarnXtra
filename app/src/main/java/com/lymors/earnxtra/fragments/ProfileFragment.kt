package com.lymors.earnxtra.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lymors.earnxtra.EditProfile
import com.lymors.earnxtra.R
import com.lymors.earnxtra.SettingsActivity
import com.lymors.earnxtra.WithDrawActivity
import com.lymors.earnxtra.models.Users
import kotlin.math.log


class ProfileFragment : Fragment() {

    lateinit var adView: AdView
    lateinit var userName:TextView
    var phoneNumber:String = ""
    var password:String = ""
    var email:String = ""
    var referalCode:String = ""
    var totalDiamonds:Int = 0
    lateinit var settingsCardView:CardView
    lateinit var accountCardView:CardView
    lateinit var paymentCardView:CardView
    lateinit var firebaseAuth:FirebaseAuth
    lateinit var userId:String
    lateinit var ref:DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        MobileAds.initialize(requireContext()) {}

        // Find the AdView in your layout
        adView = view.findViewById(R.id.adView)
        userName = view.findViewById(R.id.userName)

        settingsCardView = view.findViewById(R.id.settings)
        accountCardView = view.findViewById(R.id.accountDetails)
        paymentCardView = view.findViewById(R.id.payment)

        firebaseAuth = FirebaseAuth.getInstance()

        userId = firebaseAuth.currentUser?.uid ?: ""
        ref = FirebaseDatabase.getInstance().getReference("users").child(userId)


        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get the first child as we are expecting only one user here
                    val user = dataSnapshot.getValue(Users::class.java)
                    if (user != null) {
                        val userNameFromDatabase = user.name
                        Log.i("TAG", "onDataChange: $userNameFromDatabase")
                        val referal = user.referalCode
                        val diamond = user.totalDiamonds
                        Log.i("TAG", "onDataChange: $diamond")
                        val phoneNum = user.phoneNumber
                        val em = user.email
                        val passwo = user.password

                        userName.text = userNameFromDatabase ?: ""
                        phoneNumber = phoneNum ?: ""
                        email = em ?: ""
                        password = passwo ?: ""
                        referalCode = referal ?: ""
                        totalDiamonds =
                            diamond ?: 0 // Set a default value in case totalDiamonds is null
                        Log.i("TAG", "onDataChange: $totalDiamonds")


                    } else {
                        // Handle the case if user is null
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors that occur while retrieving data
            }
        })


        paymentCardView.setOnClickListener {
            val intent = Intent(requireContext(),WithDrawActivity::class.java)
            intent.putExtra("userName",userName.text)
            intent.putExtra("email",email)
            intent.putExtra("phone",phoneNumber)
            intent.putExtra("totalDiamonds",totalDiamonds)
            startActivity(intent)
        }

        settingsCardView.setOnClickListener {
            val intent = Intent(requireContext(),SettingsActivity::class.java)
            intent.putExtra("name",userName.text)
            intent.putExtra("phone",phoneNumber)
            intent.putExtra("email",email)
            intent.putExtra("password",password)
            intent.putExtra("referalCode",referalCode)
            intent.putExtra("totalDiamonds",totalDiamonds)
            startActivity(intent)
        }

        accountCardView.setOnClickListener {
            val intent = Intent(requireContext(),EditProfile::class.java)
            intent.putExtra("name",userName.text)
            intent.putExtra("phone",phoneNumber)
            intent.putExtra("email",email)
            intent.putExtra("password",password)
            intent.putExtra("referalCode",referalCode)
            intent.putExtra("totalDiamonds",totalDiamonds)
            startActivity(intent)
        }

        // Load the banner ad
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        return view

}
    }