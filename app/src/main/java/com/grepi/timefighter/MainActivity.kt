package com.grepi.timefighter

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

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
            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
            it.startAnimation(bounceAnimation)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu , menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.acAbout -> showInfo()
        }
        return true
    }

    @SuppressLint("StringFormatInvalid")
    private fun showInfo() {
        val dialogTitle = getString(R.string.aboutTitle, BuildConfig.VERSION_NAME)
        val dialogMessage = getString(R.string.messageInfo)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()
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
        val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink)
        scoreText.text = scoreValue
        scoreText.startAnimation(blinkAnimation)
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
