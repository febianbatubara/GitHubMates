package com.febian.android.githubmates.activity

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.febian.android.githubmates.R
import com.febian.android.githubmates.adapter.ListUserAdapter
import com.febian.android.githubmates.databinding.ActivityMainBinding
import com.febian.android.githubmates.fragment.ReminderSettingFragment
import com.febian.android.githubmates.model.User
import com.febian.android.githubmates.model.UserResponse
import com.febian.android.githubmates.repository.FavoriteUserRepository
import com.febian.android.githubmates.viewmodel.UserViewModel
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel
    private val listUserAdapter by lazy { ListUserAdapter(this) }
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)
        setupRecyclerView()

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.getUsers()?.observe(this, getListUserObserver)

        binding.edtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
            private var searchJob: Job? = null

            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.edtSearch.clearFocus()
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                searchJob?.cancel()
                searchJob = coroutineScope.launch {
                    query?.let {
                        delay(500)
                        if (it.isEmpty()) {
                            userViewModel.getUsers()
                                ?.observe(this@MainActivity, getListUserObserver)
                        } else {
                            userViewModel.searchUser(query)
                                ?.observe(this@MainActivity, searchListUserObserver)
                        }
                    }
                }
                return false
            }
        })

        binding.btnSetting.setOnClickListener {
            showPopUpMenu()
        }
        binding.btnBookmarkFavorite.setOnClickListener {
            startActivity(Intent(this, FavoriteActivity::class.java))
        }

        val closeButtonId: Int = binding.edtSearch.context.resources.getIdentifier(
            "android:id/search_close_btn",
            null,
            null
        )
        val closeSearchButton = binding.edtSearch.findViewById(closeButtonId) as ImageView
        closeSearchButton.setOnClickListener {
            binding.edtSearch.setQuery("", false)
            binding.edtSearch.clearFocus()
        }
    }

    private val getListUserObserver: Observer<List<User>> =
        Observer { users ->
            showNotFound(false)
            users?.let { listUserAdapter.setData(it) }
            showLoading(false)
        }

    private val searchListUserObserver: Observer<UserResponse> =
        Observer { userResponse ->
            userResponse.users?.let { listUserAdapter.setData(it) }
            showNotFound(userResponse.users.isNullOrEmpty())
            showLoading(false)
        }

    private fun setupRecyclerView() {
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.adapter = listUserAdapter
        binding.rvUsers.setHasFixedSize(true)

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USERNAME, data.username)
                startActivity(intent)
            }
        })
    }

    private fun showPopUpMenu() {
        val popUp = PopupMenu(this@MainActivity, binding.btnSetting)
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
            binding.listUserShimmerContainer.visibility = View.VISIBLE
        } else {
            binding.listUserShimmerContainer.visibility = View.GONE
        }
    }

    private fun showNotFound(state: Boolean) {
        if (state) {
            binding.tvNotFound.visibility = View.VISIBLE
            binding.ivNotFound.visibility = View.VISIBLE
        } else {
            binding.tvNotFound.visibility = View.GONE
            binding.ivNotFound.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

}

internal class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        FavoriteUserRepository.initialize(this)
    }

    companion object {
        private var context: Context? = null
        val appContext: Context?
            get() = context
    }
}