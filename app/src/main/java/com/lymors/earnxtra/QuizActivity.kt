package com.lymors.earnxtra

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class QuizActivity : AppCompatActivity() {
    lateinit var questionsList: ArrayList<QuestionModel>
    private var index: Int = 0
    lateinit var questionModel: QuestionModel

    private val QUIZ_PREFS = "QuizPrefs"
    private val LAST_QUIZ_DATE_KEY = "LastQuizDate"

    private var correctAnswerCount: Int = 0
    private var wrongAnswerCount: Int = 0

    lateinit var countDown: TextView
    lateinit var questions: TextView
    lateinit var option1: Button
    lateinit var option2: Button
    lateinit var option3: Button
    lateinit var option4: Button

    private var backPressedTime: Long = 0
    private var backToast: Toast? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        countDown = findViewById(R.id.countdown)
        questions = findViewById(R.id.questions)
        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)

        val sharedPreferences = getSharedPreferences(QUIZ_PREFS, Context.MODE_PRIVATE)
        val lastQuizDateMillis = sharedPreferences.getLong(LAST_QUIZ_DATE_KEY, 0)

        if (lastQuizDateMillis > 0 && !isOneDayPassed(lastQuizDateMillis)) {
            // One day has not passed since the last quiz
            // Show a toast or message indicating the quiz will start tomorrow
            Toast.makeText(
                this,
                "Quiz will start tomorrow!",
                Toast.LENGTH_SHORT
            ).show()
            finish() // Close the quiz activity for today
            return
        }

        val editor = sharedPreferences.edit()
        editor.putLong(LAST_QUIZ_DATE_KEY, Calendar.getInstance().timeInMillis)
        editor.apply()

        questionsList = ArrayList()
        questionsList.add(
            QuestionModel(
                "What is actually electricity?",
                "A flow of water",
                "A flow of air",
                "A flow of electrons",
                " A flow of atoms",
                "A flow of electrons"
            )
        )
        questionsList.add(
            QuestionModel(
                "What is the speed of sound?",
                "120 km/h",
                "1,200 km/h",
                "400 km/h",
                "700 km/h",
                "1,200 km/h"
            )
        )
        questionsList.add(
            QuestionModel(
                "What is the main component of the sun?",
                "Liquid lava",
                "Gas",
                "Molten iron",
                "Rock",
                "Gas"
            )
        )
        questionsList.add(
            QuestionModel(
                "Which of the following animals can run the fastest?",
                "Cheetah",
                "Leopard",
                "Tiger",
                "Lion",
                "Cheetah"
            )
        )
        questionsList.add(
            QuestionModel(
                "Which company is known for publishing the Mario video game?",
                "Xbox",
                "Nintendo",
                "SEGA",
                "Electronic Arts",
                "Nintendo"
            )
        )

       questionsList.add(
            QuestionModel(
                "Who is the Ceo Of Google",
                "Sundar Pichai",
                "Steve Jobs",
                "Billi gates",
                "Mark Zuckerberg",
                "Sundar Pichai"
            )
        )

       questionsList.add(
            QuestionModel(
                "Who IDE is used for app development",
                "VS Code",
                "XCode",
                "Android Studio",
                "Intellij Idea",
                "Android Studio"
            )
        )


       questionsList.add(
            QuestionModel(
                "Who was the first scientist?",
                "Newton",
                "Ibn al-Haytham",
                "Nikola",
                "Michael Faraday",
                "Ibn al-Haytham"
            )
        )


        questionsList.add(
            QuestionModel(
                "What is the capital city of Pakistan?",
                "Karachi",
                "Lahore",
                "Islamabad",
                "Peshawar",
                "Islamabad"
            )
        )

        questionsList.add(
            QuestionModel(
                "Which river is often referred to as the lifeline of Pakistan?",
                "Indus River",
                "Nile River",
                "Amazon River",
                "Ganges River",
                "Indus River"
            )
        )

        questionsList.add(
            QuestionModel(
                "Who is the founder of Pakistan?",
                "Liaquat Ali Khan",
                "Allama Iqbal",
                "Muhammad Ali Jinnah",
                "Sir Syed Ahmed Khan",
                "Muhammad Ali Jinnah"
            )
        )

        questionsList.add(
            QuestionModel(
                "What is the national language of Pakistan?",
                "Sindhi",
                "Pashto",
                "Punjabi",
                "Urdu",
                "Urdu"
            )
        )

        questionsList.add(
            QuestionModel(
                "Which mountain range is located in northern Pakistan?",
                "Rocky Mountains",
                "Himalayas",
                "Andes",
                "Karakoram Range",
                "Karakoram Range"
            )
        )

        questionsList.add(
            QuestionModel(
                "Which famous scientist developed the theory of relativity?",
                "Albert Einstein",
                "Isaac Newton",
                "Stephen Hawking",
                "Galileo Galilei",
                "Albert Einstein"
            )
        )


        questionsList.add(
            QuestionModel(
                "What is the largest planet in our solar system?",
                "Mars",
                "Venus",
                "Jupiter",
                "Saturn",
                "Jupiter"
            )
        )

        questionsList.add(
            QuestionModel(
                "Which country is known as the 'Land of the Rising Sun'?",
                "Japan",
                "China",
                "Korea",
                "Thailand",
                "Japan"
            )
        )

        questionsList.add(
            QuestionModel(
                "Which mammal can fly?",
                "Bat",
                "Dolphin",
                "Elephant",
                "Lion",
                "Bat"
            )
        )

        questionsList.add(
            QuestionModel(
                "What is the capital city of Pakistan?",
                "Karachi",
                "Lahore",
                "Islamabad",
                "Peshawar",
                "Islamabad"
            )
        )

        questionsList.add(
            QuestionModel(
                "Which river is often referred to as the lifeline of Pakistan?",
                "Indus River",
                "Nile River",
                "Amazon River",
                "Ganges River",
                "Indus River"
            )
        )

        questionsList.add(
            QuestionModel(
                "Who is the founder of Pakistan?",
                "Liaquat Ali Khan",
                "Allama Iqbal",
                "Muhammad Ali Jinnah",
                "Sir Syed Ahmed Khan",
                "Muhammad Ali Jinnah"
            )
        )

        questionsList.shuffle()
        questionsList = ArrayList(questionsList.subList(0, 5))

        questionModel = questionsList[index]

        setAllQuestions()

        countdown()
    }

    fun countdown() {
        var duration: Long = TimeUnit.SECONDS.toMillis(10)


        object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                var sDuration: String = String.format(
                    Locale.ENGLISH,
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                    )
                )

                countDown.text = sDuration

            }

            override fun onFinish() {
                index++
                if (index < questionsList.size) {
                    questionModel = questionsList[index]
                    setAllQuestions()
                    resetBackground()
                    enableButton()
                    countdown()

                } else {

                    gameResult()

                }


            }


        }.start()


    }


    private fun correctAns(option: Button) {
        option.background = getDrawable(R.drawable.right_bg)

        correctAnswerCount++


    }

    private fun wrongAns(option: Button) {

        option.background = resources.getDrawable(R.drawable.wrong_bg)

        wrongAnswerCount++


    }

    private fun gameResult() {
        var intent = Intent(this, ResultActivity::class.java)

        intent.putExtra("correct", correctAnswerCount.toString())
        intent.putExtra("total", questionsList.size.toString())

        startActivity(intent)
    }


    private fun setAllQuestions() {
        questions.text = questionModel.question
        option1.text = questionModel.option1
        option2.text = questionModel.option2
        option3.text = questionModel.option3
        option4.text = questionModel.option4
    }

    private fun enableButton() {
        option1.isClickable = true
        option2.isClickable = true
        option3.isClickable = true
        option4.isClickable = true
    }

    private fun disableButton() {
        option1.isClickable = false
        option2.isClickable = false
        option3.isClickable = false
        option4.isClickable = false
    }

    private fun resetBackground() {
        option1.background = resources.getDrawable(R.drawable.option_bg)
        option2.background = resources.getDrawable(R.drawable.option_bg)
        option3.background = resources.getDrawable(R.drawable.option_bg)
        option4.background = resources.getDrawable(R.drawable.option_bg)
    }

    fun option1Clicked(view: View) {
        disableButton()
        if (questionModel.option1 == questionModel.answer) {
            option1.background = resources.getDrawable(R.drawable.right_bg)


            correctAns(option1)

        } else {
            wrongAns(option1)
        }
    }

    fun option2Clicked(view: View) {
        disableButton()
        if (questionModel.option2 == questionModel.answer) {
            option2.background = resources.getDrawable(R.drawable.right_bg)


            correctAns(option2)

        } else {
            wrongAns(option2)
        }
    }

    fun option3Clicked(view: View) {
        disableButton()
        if (questionModel.option3 == questionModel.answer) {

            option3.background = resources.getDrawable(R.drawable.right_bg)


            correctAns(option3)


        } else {
            wrongAns(option3)
        }
    }

    fun option4Clicked(view: View) {
        disableButton()
        if (questionModel.option4 == questionModel.answer) {
            option4.background = resources.getDrawable(R.drawable.right_bg)


            correctAns(option4)

        } else {
            wrongAns(option4)
        }
    }

    override fun onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast?.cancel()
            finish()
        } else {
            backToast = Toast.makeText(baseContext, "DOUBLE PRESS TO QUIT GAME", Toast.LENGTH_SHORT)
            backToast?.show()
        }
        backPressedTime = System.currentTimeMillis()

    }

    private fun isOneDayPassed(lastQuizDateMillis: Long): Boolean {
        val currentDate = Calendar.getInstance().timeInMillis
        return currentDate - lastQuizDateMillis >= TimeUnit.DAYS.toMillis(1)
    }

}