package com.mvvmproject.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.datatransport.runtime.dagger.Module
import com.mvvmproject.common.emitErrorMessage
import com.mvvmproject.common.exceptionHandler
import com.mvvmproject.data.repository.VideoDetailsRepository
import com.mvvmproject.domain.response.IndividualWorkoutApiResponse
import com.mvvmproject.common.Resource
import com.mvvmproject.utils.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@Module
class VideoDetailViewModel @Inject constructor(
    private val videoDetailRepository: VideoDetailsRepository,
    private val appContext: Context,
) : ViewModel() {
    private val _videoDetailsLiveData = MutableLiveData<Resource<IndividualWorkoutApiResponse>>()
    val videoDetailsLiveData: LiveData<Resource<IndividualWorkoutApiResponse>> = _videoDetailsLiveData

    fun videoDetailsApi(token: String, postId: Int, timeZone: String) {
        if (isNetworkAvailable(appContext)) {
            viewModelScope.launch(exceptionHandler(_videoDetailsLiveData)) {
                val response = videoDetailRepository.videoDetailsApi(token, postId, timeZone)
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    _videoDetailsLiveData.postValue(Resource.Success(result))
                } else {
                    _videoDetailsLiveData.emitErrorMessage(response)
                }
            }
        } else {
            _videoDetailsLiveData.postValue(Resource.InternetError())
        }
    }
}