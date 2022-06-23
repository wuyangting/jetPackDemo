package com.example.myapplication.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.Utils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import java.lang.reflect.Constructor

/**
 * 多个页面操作统一数据库使用该ViewModel保证可以收到通知
 * Notes:面向多个页面
 */
class RoomViewModel(private val context: Application):AndroidViewModel(context) {
    private val roomUtils:Utils.RoomUtils= lazy {
        Utils.roomUtils
    }.value

    //共享LiveData
    val studentData= lazy {   MutableLiveData<Event>()}.value

    private val scope= lazy {
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }.value

    //初始化数据库
    init {
       scope.launch {  roomUtils.initRoomDatabase(context)}
    }

    //先不用这种方式
    fun addInsertFlow(insertFlow:()->Flow<StudentBean>){
        scope.launch {
            insertFlow().collect {
                roomUtils.insertData(it)
                launch(Dispatchers.Main) {
                    studentData.value=Event.INSERT(student = it)
                }
            }
        }
    }
    fun addInsertFlow(student: StudentBean){
        scope.launch {
            roomUtils.insertData(student)
            launch(Dispatchers.Main) {
                studentData.value=Event.INSERT(student = student)
            }
        }
    }

    fun addDeleteFlow(deleteFlow:()->Flow<StudentBean>){
        scope.launch {
            deleteFlow().collect {
                roomUtils.deleteData(it)
                launch(Dispatchers.Main) {
                    studentData.value=Event.DELETE(student = it)
                }
            }
        }
    }

   suspend fun queryData() :List<StudentBean>{
       //不是实时的，需要等待后续使用LiveData替换
       var data= listOf<StudentBean>()
        val job =scope.async {
           data= roomUtils.queryData()
        }
        while (true){
            job.await()
            MainScope().launch {
                studentData.value=Event.QUERY(studentList = data)
            }
            return data
        }
    }
    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

    /**
     * 确保事件驱动UI，将其封装为密封类，符合强模式匹配规则
     */
    sealed class Event(student: StudentBean?=null,studentList: List<StudentBean>?=null){
        data class INSERT(val student:StudentBean):Event(student=student)
        data class DELETE(val student:StudentBean):Event(student=student)
        data class UPDATE(val student:StudentBean):Event(student=student)

        //该事件可以进行获取其他界面启动获取数据库的操作，可自定义
        data class QUERY(val studentList:List<StudentBean>):Event(studentList = studentList)
    }
}
