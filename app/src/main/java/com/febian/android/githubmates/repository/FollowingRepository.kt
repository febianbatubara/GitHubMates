package com.febian.android.githubmates.repository

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.febian.android.githubmates.activity.Application
import com.febian.android.githubmates.api.RetrofitService
import com.febian.android.githubmates.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object FollowingRepository {

    private val followingList = MutableLiveData<List<User>>()

    fun getFollowingApiCall(username: String): MutableLiveData<List<User>> {
        val call = RetrofitService.apiInterface.getFollowingList(username)

        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>>
            ) {
                if (response.isSuccessful) {
                    followingList.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(Application.appContext, t.message, Toast.LENGTH_LONG).show()
            }
        })
        return followingList
    }

}