package com.example.week7

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MyViewModel(private val count: Int) : ViewModel() {
    val countLiveData = MutableLiveData<Int>()

    init {
        countLiveData.value = count
    }

    fun increaseCount() {
        countLiveData.value = (countLiveData.value ?: 0) + 1
    }

    fun decreaseCount() {
        countLiveData.value = (countLiveData.value ?: 0) - 1
    }

    class MyViewModelFactory(private val count: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MyViewModel(count) as T
        }
    }
}
