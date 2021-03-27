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
import com.febian.android.githubmates.databinding.FragmentFollowerBinding
import com.febian.android.githubmates.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersFragment : Fragment() {

    private lateinit var binding: FragmentFollowerBinding
    private val listFollowersAdapter by lazy { context?.let { ListUserAdapter(it) } }
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
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(true)
        setupRecyclerView()
        getFollowersApiCall(username)
    }

    private fun getFollowersApiCall(username: String) {
        val call = RetrofitService.apiInterface.getFollowersList(username)

        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { listFollowersAdapter?.setData(it) }
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
        binding.rvFollowers.layoutManager = LinearLayoutManager(context)
        binding.rvFollowers.adapter = listFollowersAdapter
        binding.rvFollowers.setHasFixedSize(true)

        listFollowersAdapter?.setOnItemClickCallback(object :
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
            binding.rvFollowers.visibility = View.GONE
            binding.listUserShimmerContainer.visibility = View.VISIBLE
        } else {
            binding.rvFollowers.visibility = View.VISIBLE
            binding.listUserShimmerContainer.visibility = View.GONE
        }
    }

    private fun showNotFound(state: Boolean) {
        if (state) {
            binding.tvNoFollowers.visibility = View.VISIBLE
            binding.rvFollowers.visibility = View.GONE
        } else {
            binding.tvNoFollowers.visibility = View.GONE
            binding.rvFollowers.visibility = View.GONE
        }
    }

}