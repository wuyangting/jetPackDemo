package com.example.myapplication

import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.room.StudentBean

/**
 * 目前需要手动指定参数类型，后续扩展
 */
fun  <T> AppCompatActivity.find (id:Int) :T{
    val findViewById = this.findViewById<View>(id)
    return findViewById as T
}

/**
 * 后续多个页面操作数据库
 */
fun AppCompatActivity.generateStudent(name: EditText, age: EditText, sex: EditText): StudentBean {
    return  StudentBean(name = name.text.toString(),
        age = age.text.toString(),
        sex = sex.text.toString())
}