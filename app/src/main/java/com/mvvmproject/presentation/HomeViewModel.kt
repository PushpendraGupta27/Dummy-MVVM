package com.mvvmproject.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvvmproject.common.emitErrorMessage
import com.mvvmproject.common.exceptionHandler
import com.mvvmproject.data.repository.HomeRepository
import com.mvvmproject.domain.response.AllWorkOutResponse
import com.mvvmproject.domain.response.CarouselListResponse
import com.mvvmproject.domain.response.NewWorkoutResponse
import com.mvvmproject.common.Resource
import com.mvvmproject.utils.isNetworkAvailable
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@InstallIn(SingletonComponent::class)
@Module
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val appContext: Context,
) : ViewModel() {
    private val _carouselLiveData = MutableLiveData<Resource<CarouselListResponse>>()
    val carouselLiveData: LiveData<Resource<CarouselListResponse>> = _carouselLiveData

    private val _newWorkoutsLiveData = MutableLiveData<Resource<NewWorkoutResponse>>()
    val newWorkoutsLiveData: LiveData<Resource<NewWorkoutResponse>> = _newWorkoutsLiveData

    private val _allWorkoutsLiveData = MutableLiveData<Resource<AllWorkOutResponse>>()
    val allWorkoutsLiveData: LiveData<Resource<AllWorkOutResponse>> = _allWorkoutsLiveData

    fun getCarousel(token: String) {
        if (isNetworkAvailable(appContext)) {
            viewModelScope.launch(exceptionHandler(_carouselLiveData)) {
                val response = homeRepository.carouselApi(token)
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    _carouselLiveData.postValue(Resource.Success(result))
                } else {
                    _carouselLiveData.emitErrorMessage(response)
                }
            }
        } else {
            _carouselLiveData.postValue(Resource.InternetError())
        }
    }

    fun getNewWorkouts(token: String) {
        if (isNetworkAvailable(appContext)) {
            viewModelScope.launch(exceptionHandler(_newWorkoutsLiveData)) {
                val response = homeRepository.newWorkoutApi(token)
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    _newWorkoutsLiveData.postValue(Resource.Success(result))
                } else {
                    _newWorkoutsLiveData.emitErrorMessage(response)
                }
            }
        } else {
            _newWorkoutsLiveData.postValue(Resource.InternetError())
        }
    }

    fun getAllWorkouts(token: String) {
        if (isNetworkAvailable(appContext)) {
            viewModelScope.launch(exceptionHandler(_allWorkoutsLiveData)) {
                val response = homeRepository.allWorkoutApi(token)
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    _allWorkoutsLiveData.postValue(Resource.Success(result))
                } else {
                    _allWorkoutsLiveData.emitErrorMessage(response)
                }
            }
        } else {
            _allWorkoutsLiveData.postValue(Resource.InternetError())
        }
    }
}