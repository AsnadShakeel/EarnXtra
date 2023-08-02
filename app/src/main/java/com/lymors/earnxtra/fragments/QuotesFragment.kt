package com.lymors.earnxtra.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lymors.earnxtra.QuotesActivity
import com.lymors.earnxtra.R
import com.lymors.earnxtra.models.Users
import java.util.*

class QuotesFragment : Fragment(),OnUserEarnedRewardListener {

    private var rewardedAdDiamond: RewardedAd? = null
    private var rewardedAdDiamond2: RewardedAd? = null
    private var rewardedAdDiamond3: RewardedAd? = null
    private var rewardedAdDiamond4: RewardedAd? = null
    private var rewardedAdDiamond5: RewardedAd? = null
    private lateinit var userId: String
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var ref: DatabaseReference
    private var diamonds: Int = 0
    private lateinit var tvQuote1: TextView
    private lateinit var tvQuote2: TextView
    private lateinit var tvQuote3: TextView
    private lateinit var tvQuote4: TextView
    private lateinit var tvQuote5: TextView

    val bestQuotes = listOf(
        "The only way to do great work is to love what you do. - Steve Jobs",
        "The best time to plant a tree was 20 years ago. The second best time is now. - Chinese Proverb",
        "Success is not final, failure is not fatal: It is the courage to continue that counts. - Winston Churchill",
        "The future belongs to those who believe in the beauty of their dreams. - Eleanor Roosevelt",
        "In the middle of every difficulty lies opportunity. - Albert Einstein",
        "The only thing standing between you and your goal is the story you keep telling yourself as to why you can't achieve it. - Jordan Belfort",
        "Your time is limited, don't waste it living someone else's life. - Steve Jobs",
        "Life is what happens when you're busy making other plans. - John Lennon",
        "Don't count the days, make the days count. - Muhammad Ali",
        "The best revenge is massive success. - Frank Sinatra",
        "The two most important days in your life are the day you are born and the day you find out why. - Mark Twain",
        "You miss 100% of the shots you don't take. - Wayne Gretzky",
        "The only thing we have to fear is fear itself. - Franklin D. Roosevelt",
        "It is during our darkest moments that we must focus to see the light. - Aristotle",
        "Do not dwell in the past, do not dream of the future, concentrate the mind on the present moment. - Buddha",
        "The future starts today, not tomorrow. - Pope John Paul II",
        "The journey of a thousand miles begins with one step. - Lao Tzu",
        "You must be the change you wish to see in the world. - Mahatma Gandhi",
        "The best way to predict the future is to create it. - Peter Drucker",
        "Believe you can and you're halfway there. - Theodore Roosevelt",
        "The only limit to our realization of tomorrow will be our doubts of today. - Franklin D. Roosevelt",
        "The only place where success comes before work is in the dictionary. - Vidal Sassoon",
        "It always seems impossible until it is done. - Nelson Mandela",
        "What lies behind us and what lies before us are tiny matters compared to what lies within us. - Ralph Waldo Emerson",
        "Success usually comes to those who are too busy to be looking for it. - Henry David Thoreau",
        "Everything you've ever wanted is on the other side of fear. - George Addair",
        "The only person you are destined to become is the person you decide to be. - Ralph Waldo Emerson",
        "The best preparation for tomorrow is doing your best today. - H. Jackson Brown Jr.",
        "It's not whether you get knocked down, it's whether you get up. - Vince Lombardi",
        "Happiness is not something ready-made. It comes from your own actions. - Dalai Lama",
        "The only true wisdom is in knowing you know nothing. - Socrates",
        "The mind is everything. What you think you become. - Buddha",
        "Keep your face always toward the sunshine, and shadows will fall behind you. - Walt Whitman",
        "The secret of getting ahead is getting started. - Mark Twain",
        "Believe in yourself and all that you are. Know that there is something inside you that is greater than any obstacle. - Christian D. Larson",
        "If you want to achieve greatness stop asking for permission. - Anonymous",
        "If you can dream it, you can achieve it. - Zig Ziglar",
        "The harder the conflict, the greater the triumph. - George Washington",
        "The only way to achieve the impossible is to believe it is possible. - Charles Kingsleigh (Alice in Wonderland)",
        "I have not failed. I've just found 10,000 ways that won't work. - Thomas A. Edison",
        "You can never cross the ocean until you have the courage to lose sight of the shore. - Christopher Columbus",
        "Twenty years from now you will be more disappointed by the things you didn't do than by the ones you did do. - Mark Twain",
        "Do not wait; the time will never be 'just right.' Start where you stand, and work with whatever tools you may have at your command, and better tools will be found as you go along. - George Herbert",
        "Great minds discuss ideas; average minds discuss events; small minds discuss people. - Eleanor Roosevelt",
        "Life isn't about finding yourself. It's about creating yourself. - George Bernard Shaw",
        "Every strike brings me closer to the next home run. - Babe Ruth",
        "When you have a dream, you've got to grab it and never let go. - Carol Burnett",
        "In the end, it's not the years in your life that count. It's the life in your years. - Abraham Lincoln",
        "What you get by achieving your goals is not as important as what you become by achieving your goals. - Zig Ziglar",
        "You are never too old to set another goal or to dream a new dream. - C.S. Lewis",
        "The only impossible journey is the one you never begin. - Tony Robbins",
        "The only place where your dream becomes impossible is in your own thinking. - Robert H. Schuller",
        "When something is important enough, you do it even if the odds are not in your favor. - Elon Musk",
        "The only thing standing between you and your goal is the story you keep telling yourself as to why you can't achieve it. - Jordan Belfort"
    )


    private val QUOTES_KEY = "quotes"
    private val LAST_UPDATE_KEY = "last_update"
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_quotes, container, false)
        MobileAds.initialize(requireContext()) {}

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


        val quote1 = view.findViewById<CardView>(R.id.quote1)
        val quote2 = view.findViewById<CardView>(R.id.quote2)
        val quote3 = view.findViewById<CardView>(R.id.quote3)
        val quote4 = view.findViewById<CardView>(R.id.quote4)
        val quote5 = view.findViewById<CardView>(R.id.quote5)
        tvQuote1 = view.findViewById<TextView>(R.id.tvQuote1)
        tvQuote2 = view.findViewById<TextView>(R.id.tvQuote2)
        tvQuote3 = view.findViewById<TextView>(R.id.tvQuote3)
        tvQuote4 = view.findViewById<TextView>(R.id.tvQuote4)
        tvQuote5 = view.findViewById<TextView>(R.id.tvQuote5)
        firebaseAuth = FirebaseAuth.getInstance()
        updateTextViewsIfDayPassed()

        userId = firebaseAuth.currentUser?.uid ?: ""
        ref = FirebaseDatabase.getInstance().getReference("users").child(userId)


        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(Users::class.java)
                    val userDiamonds = user?.totalDiamonds
                    diamonds = userDiamonds!!
                } else {
                    // Handle the case if the data does not exist
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors that occur while retrieving data
            }
        })

        loadAd()
        quote1.setOnClickListener {
            rewardedAdDiamond?.show(requireActivity(), this@QuotesFragment)
        }

       quote2.setOnClickListener {
            rewardedAdDiamond2?.show(requireActivity(), this@QuotesFragment)
        }

       quote3.setOnClickListener {
            rewardedAdDiamond3?.show(requireActivity(), this@QuotesFragment)
        }

       quote4.setOnClickListener {
            rewardedAdDiamond4?.show(requireActivity(), this@QuotesFragment)
        }

       quote5.setOnClickListener {
            rewardedAdDiamond5?.show(requireActivity(), this@QuotesFragment)
        }

        return view
    }

    private fun updateTextViewsIfDayPassed() {
        val currentDate = Calendar.getInstance()
        val lastUpdateDateMillis = sharedPreferences.getLong(LAST_UPDATE_KEY, 0)
        val lastUpdateDate = Calendar.getInstance().apply { timeInMillis = lastUpdateDateMillis }

        if (currentDate.timeInMillis - lastUpdateDate.timeInMillis >= 24 * 60 * 60 * 1000) {
            // One day has passed, update the TextViews
            val randomQuotes = getRandomQuotes()
            tvQuote1.text = randomQuotes[0]
            tvQuote2.text = randomQuotes[1]
            tvQuote3.text = randomQuotes[2]
            tvQuote4.text = randomQuotes[3]
            tvQuote5.text = randomQuotes[4]

            // Save the current date as the last update date
            val editor = sharedPreferences.edit()
            editor.putLong(LAST_UPDATE_KEY, currentDate.timeInMillis)
            editor.apply()
        }
    }

    private fun getRandomQuotes(): List<String> {
        val random = Random()
        return bestQuotes.shuffled(random).take(5)
    }
    private fun loadAd() {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            requireContext(),
            "ca-app-pub-4143014580321603/4300758658", // Replace with your ad unit ID for Diamond
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    super.onAdLoaded(ad)
                    rewardedAdDiamond = ad
                    rewardedAdDiamond?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                // This method will be called when the ad is closed (e.g., user watched the ad)
                                // Open your desired activity here
                                openNextActivity()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                // Ad failed to show, handle the error if needed
                            }

                            override fun onAdShowedFullScreenContent() {
                                // Ad was shown, handle this if needed
                            }
                        }
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    // Handle ad loading failure here
                    val intent = Intent(context,QuotesActivity::class.java)
                    startActivity(intent)
                }
            }
        )


        RewardedAd.load(
            requireContext(),
            "ca-app-pub-4143014580321603/3228802830", // Replace with your ad unit ID for Diamond
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    super.onAdLoaded(ad)
                    rewardedAdDiamond2 = ad
                    rewardedAdDiamond2?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                // This method will be called when the ad is closed (e.g., user watched the ad)
                                // Open your desired activity here
                                openNextActivity2()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                // Ad failed to show, handle the error if needed
                            }

                            override fun onAdShowedFullScreenContent() {
                                // Ad was shown, handle this if needed
                            }
                        }
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    // Handle ad loading failure here
                    val intent = Intent(context,QuotesActivity::class.java)
                    startActivity(intent)
                }
            }
        )

        RewardedAd.load(
            requireContext(),
            "ca-app-pub-4143014580321603/4506225485", // Replace with your ad unit ID for Diamond
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    super.onAdLoaded(ad)
                    rewardedAdDiamond3 = ad
                    rewardedAdDiamond3?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                // This method will be called when the ad is closed (e.g., user watched the ad)
                                // Open your desired activity here
                                openNextActivity3()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                // Ad failed to show, handle the error if needed
                            }

                            override fun onAdShowedFullScreenContent() {
                                // Ad was shown, handle this if needed
                            }
                        }
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    // Handle ad loading failure here
                }
            }
        )




        RewardedAd.load(
            requireContext(),
            "ca-app-pub-4143014580321603/4928580083", // Replace with your ad unit ID for Diamond
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    super.onAdLoaded(ad)
                    rewardedAdDiamond4 = ad
                    rewardedAdDiamond4?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                // This method will be called when the ad is closed (e.g., user watched the ad)
                                // Open your desired activity here
                                openNextActivity4()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                // Ad failed to show, handle the error if needed
                            }

                            override fun onAdShowedFullScreenContent() {
                                // Ad was shown, handle this if needed
                            }
                        }
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    // Handle ad loading failure here
                    val intent = Intent(context,QuotesActivity::class.java)
                    startActivity(intent)
                }
            }
        )


        RewardedAd.load(
            requireContext(),
            "ca-app-pub-4143014580321603/2870490438", // Replace with your ad unit ID for Diamond
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    super.onAdLoaded(ad)
                    rewardedAdDiamond5 = ad
                    rewardedAdDiamond5?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                // This method will be called when the ad is closed (e.g., user watched the ad)
                                // Open your desired activity here
                                openNextActivity5()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                // Ad failed to show, handle the error if needed
                            }

                            override fun onAdShowedFullScreenContent() {
                                // Ad was shown, handle this if needed
                            }
                        }
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    super.onAdFailedToLoad(adError)
                    // Handle ad loading failure here
                    val intent = Intent(context,QuotesActivity::class.java)
                    startActivity(intent)
                }
            }
        )
    }

    private fun openNextActivity5() {
        val intent = Intent(context, QuotesActivity::class.java)
        intent.putExtra("text", tvQuote5.text.toString())
        startActivity(intent)
    }

    private fun openNextActivity4() {
        val intent = Intent(context, QuotesActivity::class.java)
        intent.putExtra("text", tvQuote4.text.toString())
        startActivity(intent)
    }

    private fun openNextActivity3() {
        val intent = Intent(context, QuotesActivity::class.java)
        intent.putExtra("text", tvQuote3.text.toString())
        startActivity(intent)
    }

    private fun openNextActivity2() {
        val intent = Intent(context, QuotesActivity::class.java)
        intent.putExtra("text", tvQuote2.text.toString())
        startActivity(intent)
    }

    override fun onUserEarnedReward(p0: RewardItem) {
        var d = diamonds.toInt()
        var a = 3
        a+=d
        FirebaseDatabase.getInstance().getReference("users").child(userId).child("totalDiamonds")
            .setValue(a)

    }

    private fun openNextActivity() {
        val intent = Intent(context, QuotesActivity::class.java)
        intent.putExtra("text", tvQuote1.text.toString())
        startActivity(intent)
    }

}