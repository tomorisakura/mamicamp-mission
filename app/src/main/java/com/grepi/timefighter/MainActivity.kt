package com.grepi.timefighter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.BuildConfig
import com.grepi.timefighter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.viewmodel = mainViewModel
        binding.lifecycleOwner = this

        mainViewModel.endGame().observe(this, Observer {
            if (it <= 0 ) {
                Toast.makeText(this, getString(R.string.total_score, mainViewModel.scores.value.toString()), Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_about -> { showDialog() }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("StringFormatInvalid")
    private fun showDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.app_name, BuildConfig.VERSION_NAME))
        builder.setMessage(getString(R.string.message_info))
        builder.create().show()
    }

}
