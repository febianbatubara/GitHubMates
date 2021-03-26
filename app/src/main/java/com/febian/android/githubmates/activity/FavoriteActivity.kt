package com.febian.android.githubmates.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.febian.android.githubmates.R
import com.febian.android.githubmates.adapter.ListUserAdapter
import com.febian.android.githubmates.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val listUserAdapter by lazy { ListUserAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSetting.setOnClickListener { showPopUpMenu() }
        binding.btnBack.setOnClickListener { this@FavoriteActivity.finish() }

    }

    private fun showPopUpMenu() {
        val popUp = PopupMenu(this@FavoriteActivity, binding.btnSetting)
        popUp.menuInflater.inflate(R.menu.main_menu, popUp.menu)
        popUp.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.change_language_settings -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                R.id.reminder_settings -> {
                    true
                }
                else -> false
            }
        }
        popUp.show()
    }
}