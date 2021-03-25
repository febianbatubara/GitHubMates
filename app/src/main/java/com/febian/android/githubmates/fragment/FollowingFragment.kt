package com.febian.android.githubmates.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.febian.android.githubmates.activity.DetailActivity
import com.febian.android.githubmates.adapter.ListUserAdapter
import com.febian.android.githubmates.databinding.FragmentFollowingBinding
import com.febian.android.githubmates.model.User
import com.febian.android.githubmates.viewmodel.FollowingViewModel

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private lateinit var followingViewModel: FollowingViewModel
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
    ): View? {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(true)
        setupRecyclerView()
        followingViewModel = ViewModelProvider(this@FollowingFragment).get(FollowingViewModel::class.java)
        followingViewModel.getFollowing(username)
            ?.observe(viewLifecycleOwner, getListFollowingObserver)
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

    private val getListFollowingObserver: Observer<List<User>> =
        Observer { users ->
            users?.let { listFollowingAdapter?.setData(it) }
            showNotFound(users.isNullOrEmpty())
            showLoading(false)
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