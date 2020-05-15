package com.grepi.timefighter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var buttonTap : Button
    private lateinit var scoreText : TextView
    private lateinit var timeLeft : TextView
    private var scoreValue = 0
    private var gameStarted = false
    private lateinit var countDownTimer: CountDownTimer
    private val initialCountDown : Long = 60000
    private val countDownInterval : Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonTap = findViewById(R.id.btnTap)
        scoreText = findViewById(R.id.score)
        timeLeft = findViewById(R.id.time)

        scoreText.text = getString(R.string.scoreLabel, scoreValue)

        buttonTap.setOnClickListener{
            incrementsScore()
        }

        resetGame()
    }

    private fun resetGame() {
        scoreValue = 0
        scoreText.text = getString(R.string.scoreLabel, scoreValue)

        val initialTime = initialCountDown / 1000
        timeLeft.text = getString(R.string.timeLabel, initialTime)
        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onFinish() {
                endGame()
            }

            override fun onTick(milisUntilFinished: Long) {
                val timesLeft = milisUntilFinished / 1000
                timeLeft.text = getString(R.string.timeLabel, timesLeft)
            }

        }

        gameStarted = false
    }

    private fun incrementsScore() {
        if (!gameStarted) {
            startGame()
        }
        scoreValue += 1
        val scoreValue = getString(R.string.scoreLabel, scoreValue)
        scoreText.text = scoreValue
    }

    private fun startGame() {
        countDownTimer.start()
        gameStarted = true
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.gameOverMessage, scoreValue), Toast.LENGTH_LONG).show()
        resetGame()
    }
}
