package com.example.myapplication.room

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.find
import com.example.myapplication.generateStudent
import com.example.myapplication.room.RoomViewModel.Event.INSERT
import com.example.myapplication.room.adapter.DataAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.coroutines.suspendCoroutine

class RoomActivity : AppCompatActivity() {
    private lateinit var   edtName: EditText
    private lateinit var   edtAge: EditText
    private lateinit var   edtSex: EditText
  private  lateinit var adapter:DataAdapter
  private  lateinit var roomViewModel: RoomViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        initView()

        roomViewModel=ViewModelProvider(this,object :ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                modelClass.constructors
            return    modelClass.getConstructor(Application::class.java).newInstance(application)
            }
        }).get(RoomViewModel::class.java).apply {
            studentData.observe(this@RoomActivity,object: Observer<RoomViewModel.Event> {
                override fun onChanged(it: RoomViewModel.Event?) {
                    if(it==null){return}
                    when (it) {
                        is INSERT ->{
                            Log.i("Wwwwwwwwwww", "insert .....")
                            adapter.insertStudent(it.student)
                        }
                        is RoomViewModel.Event.DELETE->{
                            Log.i("Wwwwwwwwwww", "delete .....")
                            adapter.deleteStudent(it.student)
                        }
                        is RoomViewModel.Event.UPDATE->{
                            Log.i("Wwwwwwwwwww", "update .....")
                        }
                        is RoomViewModel.Event.QUERY->{
                            Log.i("Wwwwwwwwwww", "query .....")
                            MainScope().launch {
                                adapter.updateData(it.studentList)
                            }
                        }
                    }
                }
            })
            //注册
//            addInsertFlow {insertDataFlow()}
        }
    }

    private fun initView() {
        val recycler = find<RecyclerView>(R.id.recycler_room)
        adapter= DataAdapter(this).also { recycler.adapter = it }
        recycler.layoutManager= LinearLayoutManager(this)
       edtName= find(R.id.edit_name)
        edtAge=find(R.id.edit_age)
       edtSex= find(R.id.edit_sex)
        find<Button>(R.id.btn_insert).setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                //添加Flow：使ViewModel收到Flow的值
                // 目前只有当前操作数据库因此本页面更新也走ViewModel的流程
                insertDataFlow()
            }
        })

        find<Button>(R.id.btn_query).setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                CoroutineScope(Dispatchers.IO).launch {
                    val queryData = roomViewModel.queryData()
                    adapter.updateData(queryData)
                }
            }
        })
    }

    //将数据源头放入flow中
    private fun insertDataFlow():Unit{
        roomViewModel.addInsertFlow(generateStudent(edtName,edtAge,edtSex))

        //TODO 目前不采用这种方式
        //Problems：1.如何在flow中接受监听发射值？   如何获取到flow中的Corouting（flow会检测corouting是否一致紧张并发）
        //2.使用callBackFlow或channe在collect之后又会关闭，无法复用。如何找到复用方法
//        return flow {
//            find<Button>(R.id.btn_insert).setOnClickListener(object :View.OnClickListener{
//                override fun onClick(v: View?) {
//                    //添加Flow：使ViewModel收到Flow的值
//                    // 目前只有当前操作数据库因此本页面更新也走ViewModel的流程
//
//                }
//            })
//           emit(generateStudent(edtName,edtAge,edtSex))
//        }.filter {
//            it.age.isNotEmpty()&& it.name.isNotEmpty()&& it.sex.isNotEmpty()
//        }.flowOn(Dispatchers.Main)
    }
}

