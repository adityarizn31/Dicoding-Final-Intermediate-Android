package com.dicoding.sub1_appsstory.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.sub1_appsstory.Data.ListStoryItem
import com.dicoding.sub1_appsstory.Data.ReceiveResp
import com.dicoding.sub1_appsstory.RemoteData.Result
import com.dicoding.sub1_appsstory.Repository.StoryAppRepository
import com.dicoding.sub1_appsstory.Retrofit.ApiConfig
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MainViewModel(private val storyAppRepository: StoryAppRepository) : ViewModel() {

    private val LiveDataResult = MutableLiveData<Result<ReceiveResp>>()
    private val token = MutableLiveData<String?>()

    fun registerNewUser(name: String, email: String, password: String) =
        storyAppRepository.SignUp(name, email, password)

    fun addNewStory(
        token: String,
        image: MultipartBody.Part,
        desc: RequestBody
    ) : LiveData<Result<ReceiveResp>> {
        viewModelScope.launch {
            val result = storyAppRepository.sendStoryy(token, image, desc)
            LiveDataResult.value = result
        }
        return LiveDataResult
    }

    fun loginNewUser(
        email: String, password: String
    ) = storyAppRepository.Login(email, password)

    fun getStory(
        token: String
    ) = storyAppRepository.getStory(token)

    fun getPreference(
        context: Context
    ): LiveData<String?> {
        val DataToken = storyAppRepository.getPreference(context)
        token.value = DataToken
        return token
    }

    fun setPreference(
        token: String, context: Context
    ) = storyAppRepository.setPreference(token, context)

    fun getStoryDetail(
        token: String, id: String
    ) = storyAppRepository.getStoryDetail(token, id)

    fun story(token: String): LiveData<PagingData<ListStoryItem>> =
        storyAppRepository.getStory(token).cachedIn(viewModelScope)

    fun getMapsStories(token:String) = storyAppRepository.getStoriesMaps(token)
}