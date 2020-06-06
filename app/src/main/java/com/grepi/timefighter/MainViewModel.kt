package com.grepi.timefighter

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val scores : MutableLiveData<Int> = MutableLiveData(0)
    val initialTime : MutableLiveData<Long> = MutableLiveData(60)
    val initialCountDown : Long = 60000
    val countDownInterval : Long = 1000
    private lateinit var countDownTimer: CountDownTimer
    private var gameStarted : Boolean = false

    fun incrementScore() {
        if (!gameStarted) {
            startGame()
        }
        scores.let {
            it.value = it.value?.plus(1)
        }
    }

    private fun restoreGame() {
        initialTime.postValue(initialCountDown / countDownInterval)
        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval){
            override fun onFinish() {
                endGame()
            }

            override fun onTick(millisUntilFinished: Long) {
                val timeLeft = millisUntilFinished / 1000
                initialTime.value = timeLeft
            }
        }
        countDownTimer.start()
        gameStarted = true
    }

    private fun resetGame() {
        scores.value = 0
        initialTime.postValue(initialCountDown / countDownInterval)
        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval){
            override fun onFinish() {
                endGame()
            }

            override fun onTick(millisUntilFinished: Long) {
                val timeLeft = millisUntilFinished / 1000
                initialTime.value = timeLeft
            }
        }
        gameStarted = false
    }

    private fun startGame() {
        restoreGame()
        gameStarted = true
    }

    fun endGame() : MutableLiveData<Long> {
        resetGame()
        return initialTime
    }

}