package com.grepi.timefighter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
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
    private var timeLeftOnTimer : Long = 60000

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

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

        if (savedInstanceState != null) {
            scoreValue = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, scoreValue)
        outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
        countDownTimer.cancel()
        Log.d(TAG, "onSavedInstance saved a $scoreValue and time left $timeLeftOnTimer")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy Called.")
    }

    private fun restoreGame() {
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
        countDownTimer.start()
        gameStarted = true
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
                timeLeftOnTimer = milisUntilFinished
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
