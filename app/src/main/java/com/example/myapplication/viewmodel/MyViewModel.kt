package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {
    //共享数据的核心在于拿到同一个LiveData实例，也就是拿到同一个ViewModel实例，其保存在ViewModelStore中
    //而ViewModelStore是Activity/Fragment提供的（做了屏幕转换的恢复处理，ViewModelStore会保存其数据）
    var progress:MutableLiveData<Int>?=null
    override fun onCleared() {
        //页面销毁回调
        super.onCleared()
        Log.i("wwwwwwwwwwwwwwwww", "onCleared:MyVBiewModel    cleared ")
    }

    fun getCurrentSecond() : MutableLiveData<Int>? {
        if(progress==null){
            progress=MutableLiveData<Int>()
        }
        return progress
    }
}

