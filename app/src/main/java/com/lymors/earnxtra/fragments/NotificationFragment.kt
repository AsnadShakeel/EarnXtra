package com.lymors.earnxtra.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lymors.earnxtra.NotificationModel
import com.lymors.earnxtra.R
import com.lymors.earnxtra.models.Users


class NotificationFragment : Fragment() {

    private lateinit var textViewStatus: TextView
    private lateinit var userId: String
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    private lateinit var adView: AdView
    private val CHANNEL_ID = "my_channel_id"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notification, container, false)
        MobileAds.initialize(requireContext())

        firebaseAuth = FirebaseAuth.getInstance()
        userId = firebaseAuth.currentUser!!.uid
        Log.i("TAG", "onCreateView: $userId")

        textViewStatus = view.findViewById(R.id.titleOfNotification)

        adView = view.findViewById(R.id.adView)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        downloadNotificationData(userId) { approve, reject, userData ->
            // Update the TextView based on the notification data
            if (approve) {
                if (userData != null) {
                    // Update the diamonds value
                    val diamondsToDeduct = 200 // Change this to the appropriate value
                    val updatedDiamonds = userData.totalDiamonds - diamondsToDeduct

                    // Upload the updated diamonds value to the Firebase database
                    userData.totalDiamonds = updatedDiamonds
                    val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
                    userRef.setValue(userData)
                }
                textViewStatus.text = "Your withdrawal request is approved by the admin."
                createNotificationChannel("Notification By Admin", textViewStatus.text as String)
            } else if (reject) {
                textViewStatus.text = "Your withdrawal request is rejected by the admin."
                createNotificationChannel("Notification By Admin", textViewStatus.text as String)
            }
        }

        return view
    }

    private fun downloadNotificationData(userId: String, callback: (approve: Boolean, reject: Boolean, userData: Users?) -> Unit) {
        // Assuming you have a reference to the notifications for the user
        val notificationsRef = FirebaseDatabase.getInstance().getReference("Notifications").child(userId)

        // Read the notification data from Firebase
        notificationsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val notification = dataSnapshot.getValue(NotificationModel::class.java)
                val approve = notification?.approved ?: false
                val reject = notification?.rejected ?: false

                // Now, retrieve the user data to get the totalDiamonds value
                val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
                userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(userDataSnapshot: DataSnapshot) {
                        val userData = userDataSnapshot.getValue(Users::class.java)
                        callback(approve, reject, userData)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle any errors that occur while retrieving data
                    }
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors that occur while retrieving data
            }
        })
    }

    private fun createNotificationChannel(title:String,descriptions:String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                title,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = descriptions
            }

            val notificationManager = requireContext().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

}