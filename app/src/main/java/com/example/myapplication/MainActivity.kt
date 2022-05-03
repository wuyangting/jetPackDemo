package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.myapplication.room.RoomActivity
import kotlin.reflect.KFunction1

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn_room).setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                destToRoom()
            }
        })
    }

    fun destToRoom(){
        //跳转到使用Room组件库的页面
        startActivity(Intent(this,RoomActivity::class.java))
    }
}