package com.febian.android.githubmates.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.febian.android.githubmates.R
import com.febian.android.githubmates.adapter.ListUserAdapter
import com.febian.android.githubmates.databinding.ActivityFavoriteBinding
import com.febian.android.githubmates.fragment.ReminderSettingFragment
import com.febian.android.githubmates.model.User
import com.febian.android.githubmates.viewmodel.FavoriteUserViewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteUserViewModel: FavoriteUserViewModel
    private val listUserAdapter by lazy { ListUserAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)
        setupRecyclerView()

        favoriteUserViewModel = ViewModelProvider(this).get(FavoriteUserViewModel::class.java)
        favoriteUserViewModel.getFavoriteUsers(this@FavoriteActivity)
            .observe(this@FavoriteActivity, getListFavoriteUserObserver)

        binding.btnSetting.setOnClickListener { showPopUpMenu() }
        binding.btnBack.setOnClickListener { this@FavoriteActivity.finish() }
    }

    private val getListFavoriteUserObserver: Observer<List<User>> =
        Observer { users ->
            users?.let { listUserAdapter.setData(it) }
            showNotFound(users.isNullOrEmpty())
            showLoading(false)
        }

    private fun setupRecyclerView() {
        binding.rvFavoriteUsers.layoutManager = LinearLayoutManager(this)
        binding.rvFavoriteUsers.adapter = listUserAdapter
        binding.rvFavoriteUsers.setHasFixedSize(true)

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USERNAME, data.username)
                startActivity(intent)
            }
        })
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
                    val optionDialogFragment = ReminderSettingFragment()
                    val fragmentManager = supportFragmentManager
                    optionDialogFragment.show(
                        fragmentManager,
                        ReminderSettingFragment::class.java.simpleName
                    )
                    true
                }
                else -> false
            }
        }
        popUp.show()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.listFavoriteUserShimmerContainer.visibility = View.VISIBLE
        } else {
            binding.listFavoriteUserShimmerContainer.visibility = View.GONE
        }
    }

    private fun showNotFound(state: Boolean) {
        if (state) {
            binding.tvNoFavorite.visibility = View.VISIBLE
            binding.ivNotFound.visibility = View.VISIBLE
        } else {
            binding.tvNoFavorite.visibility = View.GONE
            binding.ivNotFound.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        favoriteUserViewModel.getFavoriteUsers(this@FavoriteActivity)
            .observe(this@FavoriteActivity, getListFavoriteUserObserver)
    }

}