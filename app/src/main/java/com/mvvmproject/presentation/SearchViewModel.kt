package com.mvvmproject.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.datatransport.runtime.dagger.Module
import com.mvvmproject.common.Resource
import com.mvvmproject.common.emitErrorMessage
import com.mvvmproject.common.exceptionHandler
import com.mvvmproject.data.repository.SearchRepository
import com.mvvmproject.domain.response.IndividualWorkoutApiResponse
import com.mvvmproject.domain.response.WorkoutSearchAttributesRes
import com.mvvmproject.utils.isNetworkAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@Module
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val appContext: Context,
) : ViewModel() {
    private val _searchAttributeLiveData = MutableLiveData<Resource<WorkoutSearchAttributesRes>>()
    val searchAttributeLiveData: LiveData<Resource<WorkoutSearchAttributesRes>> = _searchAttributeLiveData

    fun searchAttributeApi(token: String) {
        if (isNetworkAvailable(appContext)) {
            viewModelScope.launch(exceptionHandler(_searchAttributeLiveData)) {
                val response = searchRepository.searchAttributeApi(token)
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    _searchAttributeLiveData.postValue(Resource.Success(result, response.code()))
                } else {
                    _searchAttributeLiveData.emitErrorMessage(response)
                }
            }
        } else {
            _searchAttributeLiveData.postValue(Resource.InternetError())
        }
    }
}