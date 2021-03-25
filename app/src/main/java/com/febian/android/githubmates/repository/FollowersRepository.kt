package com.febian.android.githubmates.repository

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.febian.android.githubmates.activity.Application
import com.febian.android.githubmates.api.RetrofitService
import com.febian.android.githubmates.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object FollowersRepository {

    private val followersList = MutableLiveData<List<User>>()

    fun getFollowersApiCall(username: String): MutableLiveData<List<User>> {
        val call = RetrofitService.apiInterface.getFollowersList(username)

        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>>
            ) {
                if (response.isSuccessful) {
                    followersList.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(Application.appContext, t.message, Toast.LENGTH_LONG).show()
            }
        })
        return followersList
    }

}