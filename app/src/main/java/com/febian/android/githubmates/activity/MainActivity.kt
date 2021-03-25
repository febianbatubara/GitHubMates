package com.febian.android.githubmates.activity

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.febian.android.githubmates.R
import com.febian.android.githubmates.adapter.ListUserAdapter
import com.febian.android.githubmates.databinding.ActivityMainBinding
import com.febian.android.githubmates.model.User
import com.febian.android.githubmates.model.UserResponse
import com.febian.android.githubmates.viewmodel.UserViewModel
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel
    private val listUserAdapter by lazy { ListUserAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        showLoading(true)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.getUsers()?.observe(this, getListUserObserver)

        binding.btnSetting.setOnClickListener {
            showPopUpMenu()
        }

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

        val closeButtonId: Int = binding.edtSearch.context.resources.getIdentifier(
            "android:id/search_close_btn",
            null,
            null
        )
        val closeButton = binding.edtSearch.findViewById(closeButtonId) as ImageView
        closeButton.setOnClickListener {
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

}

internal class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        private var context: Context? = null
        val appContext: Context?
            get() = context
    }
}