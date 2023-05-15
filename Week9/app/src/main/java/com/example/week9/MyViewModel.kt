package com.example.week9

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MyViewModel : ViewModel() {
    val myValue = MutableLiveData<Int>()

    init {
        myValue.value = 0
    }

    fun increase() {
        myValue.value = myValue.value?.plus(1)
    }
}