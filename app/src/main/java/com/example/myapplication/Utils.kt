package com.example.myapplication

import android.content.Context
import com.example.myapplication.room.MyDataBase
import com.example.myapplication.room.StudentDao
import kotlinx.coroutines.flow.Flow
import com.example.myapplication.room.StudentBean
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class Utils {
    companion object{
       val roomUtils=RoomUtils()



    }

    class RoomUtils(){
       private lateinit var studentDao:StudentDao
        suspend fun initRoomDatabase(context:Context){
           //初始化操作应放到IO Thread中，Room会检测Thread
            studentDao =  MyDataBase.getInstance(context).getStudentDao()
                studentDao.apply {
                insertData(StudentBean(name = "张三",age = "18",sex = "男"))
                insertData(StudentBean(name = "李四",age = "38",sex = "男"))
                insertData(StudentBean(name = "王五",age = "68",sex = "男"))
            }
        }

       suspend fun insertData(studentBean: StudentBean):StudentBean{
               studentDao.insertStudent(studentBean)
           return  studentBean
        }

       suspend fun deleteData(studentBean: StudentBean):StudentBean{
               studentDao.deleteStudent(studentBean)
           return  studentBean
        }

        /**
         * TODO 更新需要获取同一个id，后续实现
         */
       suspend fun updateData(studentBean: StudentBean):StudentBean{
                studentDao.updateStudent(studentBean)
            return studentBean
        }

        suspend fun queryData():List<StudentBean>{
            return  studentDao.getStudentList()
        }

    }
}