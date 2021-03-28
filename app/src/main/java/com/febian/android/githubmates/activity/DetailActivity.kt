package com.febian.android.githubmates.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.febian.android.githubmates.R
import com.febian.android.githubmates.adapter.PagerAdapter
import com.febian.android.githubmates.api.RetrofitService
import com.febian.android.githubmates.databinding.ActivityDetailBinding
import com.febian.android.githubmates.fragment.FollowersFragment
import com.febian.android.githubmates.fragment.FollowingFragment
import com.febian.android.githubmates.model.User
import com.febian.android.githubmates.repository.FavoriteUserRepository
import com.febian.android.githubmates.utils.WrapContentViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var user: User
    private var username: String? = null
    private val favoriteUserRepository = FavoriteUserRepository.get()
    private var isFavorite: Boolean? = null

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val STATE_USER = "state_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra(EXTRA_USERNAME)
        showLoading(true)
        checkIsFavorite()

        if (savedInstanceState == null) {
            getUserDetail()
        } else {
            val stateUser: User? = savedInstanceState.getParcelable(STATE_USER)
            user = stateUser!!
            setUserDetail()
        }
        initTabLayout()

        binding.btnBack.setOnClickListener { this@DetailActivity.finish() }
        binding.btnShare.setOnClickListener { shareData() }
        binding.btnWebProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(user.profileUrl)
            startActivity(intent)
        }
        binding.btnBookmarkFavorite.setOnClickListener {
            GlobalScope.launch {
                val dispatcher = this.coroutineContext
                CoroutineScope(dispatcher).launch {
                    isFavorite = if (isFavorite == true) {
                        favoriteUserRepository.removeFromFavorite(user)
                        false
                    } else {
                        favoriteUserRepository.addToFavorite(user)
                        true
                    }
                }
            }

            val textResource: String = if (isFavorite == true) {
                showAddFavoriteButton()
                getString(R.string.user_removed_from_favorite)
            } else {
                showRemoveFavoriteButton()
                getString(R.string.user_added_to_favorite)
            }

            Snackbar.make(binding.layoutInfo, textResource, Snackbar.LENGTH_LONG)
                .setAction("Close") { }
                .setActionTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                .show()
        }

    }

    private fun getUserDetail() {
        val call = username?.let { RetrofitService.apiInterface.getUserDetail(it) }
        call?.enqueue(object : Callback<User> {
            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        user = data
                        setUserDetail()
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@DetailActivity, "Can't load user data", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun setUserDetail() {
        showLoading(false)
        with(binding) {
            tvFullName.text = user.name ?: user.username
            tvUsername.text = getString(R.string.username, user.username)
            tvCompany.text = user.company ?: "-"
            tvLocation.text = user.location ?: "-"
            tvRepoCount.text = countConverter(user.repositoryCount)
            tvFollowersCount.text = countConverter(user.followers)
            tvFollowingCount.text = countConverter(user.following)
            tvCreatedDate.text = getString(
                R.string.member_since,
                user.createdDate?.split("-")?.get(0)
            )
            Glide.with(this@DetailActivity)
                .load(user.avatarUrl)
                .placeholder(R.color.colorPrimary)
                .into(binding.ivAvatar)
        }
    }

    private fun initTabLayout() {
        val bundle = Bundle()
        bundle.putString(FollowersFragment.ARG_USERNAME, username)

        val followersFragment = FollowersFragment()
        val followingFragment = FollowingFragment()
        followersFragment.arguments = bundle
        followingFragment.arguments = bundle

        val viewPager: WrapContentViewPager = binding.viewPagerDetail
        viewPager.adapter =
            PagerAdapter(supportFragmentManager, listOf(followersFragment, followingFragment))
        val tabs: TabLayout = binding.tabsDetail
        tabs.setupWithViewPager(viewPager)
    }

    private fun shareData() {
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                """
                    Check out this awesome developer's GitHub profile.
                    
                    Username: ${user.username}
                    Repositories: ${user.repositoryCount}
                    Followers: ${user.followers}
                    Profile URL: ${user.profileUrl}
                """.trimIndent()
            )
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share user information to.."))
    }

    private fun checkIsFavorite() {
        GlobalScope.launch {
            val dispatcher = this.coroutineContext
            CoroutineScope(dispatcher).launch {
                if (username?.let { favoriteUserRepository.checkFavoriteUser(it) } == true) {
                    showRemoveFavoriteButton()
                    isFavorite = true
                }
            }
        }
    }

    private fun countConverter(numValue: Int?): String {
        var finalValue: String = numValue.toString()
        if (numValue != null) {
            val suffix = charArrayOf(' ', 'k', 'M', 'B')
            val value = floor(log10(numValue.toDouble())).toInt()
            val base = value / 3
            finalValue = if (value >= 3 && base < suffix.size) {
                DecimalFormat("#0.0").format(
                    numValue / 10.0.pow(base * 3.toDouble())
                ) + suffix[base]
            } else {
                DecimalFormat("#,##0").format(numValue)
            }
        }
        return finalValue
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.userDetailShimmerContainer.visibility = View.VISIBLE
        } else {
            binding.userDetailShimmerContainer.visibility = View.GONE
        }
    }

    private fun showAddFavoriteButton() {
        binding.btnBookmarkFavorite.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
    }

    private fun showRemoveFavoriteButton() {
        binding.btnBookmarkFavorite.setImageResource(R.drawable.ic_baseline_bookmark_24)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(STATE_USER, user)
    }

}