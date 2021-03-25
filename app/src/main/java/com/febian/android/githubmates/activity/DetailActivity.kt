package com.febian.android.githubmates.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.febian.android.githubmates.R
import com.febian.android.githubmates.adapter.SectionsPagerAdapter
import com.febian.android.githubmates.api.RetrofitService
import com.febian.android.githubmates.databinding.ActivityDetailBinding
import com.febian.android.githubmates.fragment.FollowersFragment
import com.febian.android.githubmates.model.User
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val STATE_USER = "state_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra(EXTRA_USERNAME)

        showLoading(true)

        if (savedInstanceState == null) {
            getUserDetail()
        } else {
            val stateUser: User? = savedInstanceState.getParcelable(STATE_USER)
            user = stateUser!!
            setUserDetail()
        }

        val bundle = Bundle()
        bundle.putString(FollowersFragment.ARG_USERNAME, username)

        val sectionPagerAdapter = SectionsPagerAdapter(this@DetailActivity, bundle)
        val viewPager: ViewPager2 = binding.viewPagerDetail
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabsDetail
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        binding.btnBack.setOnClickListener { this@DetailActivity.finish() }
        binding.btnShare.setOnClickListener { shareData() }
        binding.btnWebProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(user.profileUrl)
            startActivity(intent)
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
                Log.v("DEBUG : ", t.message.toString())
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

    private fun countConverter(numValue: Int?): String? {
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(STATE_USER, user)
    }

}