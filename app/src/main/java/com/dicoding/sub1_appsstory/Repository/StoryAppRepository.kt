package com.dicoding.sub1_appsstory.Repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.dicoding.sub1_appsstory.Data.*
import com.dicoding.sub1_appsstory.Database.StoryDatabase
import com.dicoding.sub1_appsstory.LocalData.PreferenceSetting
import com.dicoding.sub1_appsstory.Retrofit.ApiService
import com.dicoding.sub1_appsstory.RemoteData.Result
import com.dicoding.sub1_appsstory.Data.GetStoryResp
import com.dicoding.sub1_appsstory.Data.DetailStoryResp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.HttpException

class StoryAppRepository(private val database: StoryDatabase, private val apiService: ApiService) {

    fun setPreference(token: String, context: Context) {
        val settingPreference = PreferenceSetting(context)
        settingPreference.setUser(token)
    }

    fun getPreference(context: Context): String? {
        val settingPreference = PreferenceSetting(context)
        return settingPreference.getUser()
    }

    // ReceiveResp == RegisterResp
    fun SignUp(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<ReceiveResp>> =
        liveData {
            emit(Result.Loading)
            try {
                val resp = apiService.register(name, email, password)
                emit(Result.Success(resp))
            } catch (e: Exception) {
                Log.e("SignUpViewModel", "Register: ${e.message.toString()}")
                emit(Result.Error(e.message.toString()))
            }
        }

    fun Login(email: String, password: String): LiveData<Result<LoginResp>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Login: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getStory(token: String): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(config = PagingConfig(
            pageSize = 5
        ),
            remoteMediator = StoryRemoteMediator(token, database, apiService),
            pagingSourceFactory = {
                database.storyyDaoo().getAllStory()
            }
        ).liveData
    }

    fun getStoryDetail(token: String, id: String): LiveData<Result<DetailStoryResp>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStroryDetail(token, id)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun sendStoryy(
        token: String, image: MultipartBody.Part, desc: RequestBody
    ): Result<ReceiveResp> {
        return try {
            val response = apiService.sendStory(token, image, desc)
            Result.Success(response)

        } catch (e: HttpException) {
            val error = e.response()?.errorBody()?.string()
            val jsonObject = JSONObject(error!!)
            val errorMessage = jsonObject.getString("message")
            Result.Error(errorMessage)
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    fun getStoriesMaps(token: String): LiveData<Result<GetStoryResp>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoryMap(token, 1)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("ListStoryViewModel", "getStoriesWithLocation: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }


}