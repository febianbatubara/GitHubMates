package com.febian.android.githubmates.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.febian.android.githubmates.activity.Application
import com.febian.android.githubmates.activity.DetailActivity
import com.febian.android.githubmates.adapter.ListUserAdapter
import com.febian.android.githubmates.api.RetrofitService
import com.febian.android.githubmates.databinding.FragmentFollowingBinding
import com.febian.android.githubmates.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private val listFollowingAdapter by lazy { context?.let { ListUserAdapter(it) } }
    private lateinit var username: String

    companion object {
        const val ARG_USERNAME = "username_param"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username = it.getString(ARG_USERNAME).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(true)
        setupRecyclerView()
        getFollowingApiCall(username)
    }

    private fun getFollowingApiCall(username: String) {
        val call = RetrofitService.apiInterface.getFollowingList(username)

        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { listFollowingAdapter?.setData(it) }
                    showNotFound(response.body().isNullOrEmpty())
                    showLoading(false)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(Application.appContext, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setupRecyclerView() {
        binding.rvFollowing.layoutManager = LinearLayoutManager(context)
        binding.rvFollowing.adapter = listFollowingAdapter
        binding.rvFollowing.setHasFixedSize(true)

        listFollowingAdapter?.setOnItemClickCallback(object :
            ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USERNAME, data.username)
                startActivity(intent)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.rvFollowing.visibility = View.GONE
            binding.listUserShimmerContainer.visibility = View.VISIBLE
        } else {
            binding.rvFollowing.visibility = View.VISIBLE
            binding.listUserShimmerContainer.visibility = View.GONE
        }
    }

    private fun showNotFound(state: Boolean) {
        if (state) {
            binding.tvNoUserFollowed.visibility = View.VISIBLE
            binding.rvFollowing.visibility = View.GONE
        } else {
            binding.tvNoUserFollowed.visibility = View.GONE
            binding.rvFollowing.visibility = View.GONE
        }
    }

}