package com.lymors.earnxtra.fragments

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.lymors.earnxtra.R
import com.lymors.earnxtra.models.Users
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class HomeFragment : Fragment() ,OnUserEarnedRewardListener {
    private lateinit var totalDiamonds: TextView
    private lateinit var ref: DatabaseReference
    private lateinit var adView: AdView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var smallDiamond1: LottieAnimationView
    private lateinit var smallDiamond2: LottieAnimationView
    private lateinit var diamond: ImageView
    private lateinit var userId: String

    private var rewardedAdDiamond: RewardedAd? = null
    private var rewardedAdSmallDiamond1: RewardedAd? = null
    private var rewardedAdSmallDiamond2: RewardedAd? = null

    private lateinit var constraintReferalCode:ConstraintLayout
    private lateinit var tvReferal:TextView

    private var isAnimationEnabled = true
    private var rewardEarnedTime: Long = 0

    private var isSmallDiamond2AnimationEnabled = true
    private var smallDiamond2RewardEarnedTime: Long = 0

    private lateinit var sharedPreferences: SharedPreferences
    private val prefKey = "animation_enable_time"
    private val prefKeySmallDiamond1 = "small_diamond_1_enable_time"

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        MobileAds.initialize(requireContext())
        firebaseAuth = FirebaseAuth.getInstance()

        smallDiamond1 = view.findViewById(R.id.smallDiamond1)
        smallDiamond2 = view.findViewById(R.id.smallDiamond2)

        constraintReferalCode = view.findViewById(R.id.constraintReferalCode)
        tvReferal = view.findViewById(R.id.tvReferal)

        sharedPreferences = requireContext().getSharedPreferences("animation_prefs", Context.MODE_PRIVATE)


        diamond = view.findViewById(R.id.diamond)
        adView = view.findViewById(R.id.adView)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        userId = firebaseAuth.currentUser?.uid ?: ""

        ref = FirebaseDatabase.getInstance().getReference("users").child(userId)

        totalDiamonds = view.findViewById(R.id.userName)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(Users::class.java)
                    val userDiamonds = user?.totalDiamonds
                    totalDiamonds.text = userDiamonds.toString()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })

        constraintReferalCode.setOnClickListener {
            val randomFiveDigitNumber = generateRandom5DigitNumber()
            if (tvReferal.text == "Referal Code"){
                tvReferal.text = randomFiveDigitNumber.toString()
                copyToClipboard(randomFiveDigitNumber.toString())
            }else {
                tvReferal.text = "Referal Code"
            }

        }

        loadAdForImageViews()

        diamond.setOnClickListener {
            rewardedAdDiamond?.show(requireActivity(), this@HomeFragment)
        }

        rewardEarnedTime = sharedPreferences.getLong(prefKey, 0L)
        smallDiamond2RewardEarnedTime = sharedPreferences.getLong(prefKeySmallDiamond1, 0L)

        // Check if the animations should be enabled or disabled based on the stored timestamps
        val currentTime = System.currentTimeMillis()
        isAnimationEnabled = (currentTime - rewardEarnedTime) >= TimeUnit.HOURS.toMillis(1)
        isSmallDiamond2AnimationEnabled = (currentTime - smallDiamond2RewardEarnedTime) >= TimeUnit.HOURS.toMillis(1)

        if (!isAnimationEnabled) {
            smallDiamond1.cancelAnimation()
            startAnimationEnableTimer()
        }

        if (!isSmallDiamond2AnimationEnabled) {
            smallDiamond2.cancelAnimation()
            startSmallDiamond2AnimationEnableTimer()
        }


        smallDiamond1.setOnClickListener {
            if (isAnimationEnabled) {
                rewardedAdDiamond?.show(requireActivity(), this@HomeFragment)
            }else{
                Toast.makeText(context, "You will see ad after 1 hour", Toast.LENGTH_SHORT).show()
            }
        }

        smallDiamond2.setOnClickListener {
            if (isSmallDiamond2AnimationEnabled) {
                rewardedAdSmallDiamond1?.show(requireActivity(), this@HomeFragment)
            }else{
                Toast.makeText(context, "You will see ad after 1 hour", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun copyToClipboard(text: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Referal Code is Copy To Clipboard", Toast.LENGTH_SHORT).show()
    }


    private fun loadAdForImageViews() {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            requireContext(),
                    "ca-app-pub-4143014580321603/7112654638", // Replace with your ad unit ID for Diamond
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    super.onAdLoaded(ad)
                    rewardedAdDiamond = ad
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    Toast.makeText(context, "Ad Not Available", Toast.LENGTH_SHORT).show()
                }
            }
        )

        RewardedAd.load(
            requireContext(),
            "ca-app-pub-4143014580321603/2595922603", // Replace with your ad unit ID for SmallDiamond1
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    super.onAdLoaded(ad)
                    rewardedAdSmallDiamond1 = ad
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    Toast.makeText(context, "Ad Not Available", Toast.LENGTH_SHORT).show()
                }
            }
        )

        RewardedAd.load(
            requireContext(),
            "ca-app-pub-4143014580321603/6343595921",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    super.onAdLoaded(ad)
                    rewardedAdSmallDiamond2 = ad
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    Toast.makeText(context, "Ad Not Available", Toast.LENGTH_SHORT).show()
                }
            }
        )

    }
    override fun onUserEarnedReward(p0: RewardItem) {
        val currentValueInt = totalDiamonds.text.toString().toInt()
        val updatedValue = currentValueInt + 5

        totalDiamonds.text = updatedValue.toString()
        FirebaseDatabase.getInstance().getReference("users").child(userId).child("totalDiamonds")
            .setValue(updatedValue)

        // Disable animation for one hour
        if (isAnimationEnabled) {
            isAnimationEnabled = false
            rewardEarnedTime = System.currentTimeMillis()
            smallDiamond1.cancelAnimation()
            startAnimationEnableTimer()

            // Save the reward earned time for animation in SharedPreferences
            sharedPreferences.edit().putLong(prefKey, rewardEarnedTime).apply()
        } else if (isSmallDiamond2AnimationEnabled) {
            isSmallDiamond2AnimationEnabled = false
            smallDiamond2RewardEarnedTime = System.currentTimeMillis()
            smallDiamond2.cancelAnimation()
            startSmallDiamond2AnimationEnableTimer()

            // Save the reward earned time for smallDiamond1 animation in SharedPreferences
            sharedPreferences.edit().putLong(prefKeySmallDiamond1, smallDiamond2RewardEarnedTime).apply()
        }
    }


    private fun startAnimationEnableTimer() {
        val oneHourInMillis = 3600000 // 1 hour in milliseconds

        object : CountDownTimer(oneHourInMillis.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Optionally, you can update a TextView to show the countdown if needed
                val remainingTimeInSeconds = millisUntilFinished / 1000
                Log.d("AnimationTimer", "Seconds remaining: $remainingTimeInSeconds")
            }

            override fun onFinish() {
                // Enable the animation view after one hour
                isAnimationEnabled = true
                smallDiamond1.playAnimation()
            }
        }.start()
    }


    private fun startSmallDiamond2AnimationEnableTimer() {
        val oneHourInMillis = 3600000 // 1 hour in milliseconds

        object : CountDownTimer(oneHourInMillis.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Optionally, you can update a TextView to show the countdown if needed
                val remainingTimeInSeconds = millisUntilFinished / 1000
                Log.d("SmallDiamond1Timer", "Seconds remaining: $remainingTimeInSeconds")
            }

            override fun onFinish() {
                // Enable the animation view after one hour
                isSmallDiamond2AnimationEnabled = true
                smallDiamond2.playAnimation()
            }
        }.start()
    }


    private fun generateRandom5DigitNumber(): Int {
        val random = Random(System.currentTimeMillis())
        return random.nextInt(10000, 100000)
    }
}