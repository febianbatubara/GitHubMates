package com.febian.android.consumerapp.activity

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import com.febian.android.consumerapp.R
import com.febian.android.consumerapp.adapter.ListUserAdapter
import com.febian.android.consumerapp.database.FavoriteUserDatabaseContract
import com.febian.android.consumerapp.databinding.ActivityMainBinding

private const val CODE_USER = 1

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var binding: ActivityMainBinding
    private val listUserAdapter by lazy { ListUserAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)
        setupRecyclerView()
        LoaderManager.getInstance(this).initLoader(
            CODE_USER,
            null,
            this
        )

        binding.btnSetting.setOnClickListener { showPopUpMenu() }
        binding.btnBack.setOnClickListener { this@MainActivity.finish() }
    }

    private fun setupRecyclerView() {
        binding.rvFavoriteUsers.layoutManager = LinearLayoutManager(this)
        binding.rvFavoriteUsers.adapter = listUserAdapter
        binding.rvFavoriteUsers.setHasFixedSize(true)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return when (id) {
            CODE_USER -> CursorLoader(
                applicationContext,
                FavoriteUserDatabaseContract.FavouriteUsersColumns.CONTENT_URI,
                null,
                null,
                null,
                null
            )
            else -> throw IllegalArgumentException()
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (loader.id == CODE_USER) {
            try {
                if (data != null && data.count > 0) {
                    listUserAdapter.setData(data)
                    showNotFound(false)
                } else {
                    showNotFound(true)
                }

                showLoading(false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        listUserAdapter.setData(null)
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

    private fun showPopUpMenu() {
        val popUp = PopupMenu(this@MainActivity, binding.btnSetting)
        popUp.menuInflater.inflate(R.menu.main_menu, popUp.menu)
        popUp.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.change_language_settings -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                else -> false
            }
        }
        popUp.show()
    }

    override fun onResume() {
        super.onResume()

    }

}
