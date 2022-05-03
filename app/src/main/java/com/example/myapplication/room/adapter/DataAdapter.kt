package com.example.myapplication.room.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.room.StudentBean
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter

class DataAdapter(private val context: Context) : RecyclerView.Adapter<DataAdapter.DataViewHolder>() {


    var dataList= arrayListOf<StudentBean>()


    class DataViewHolder(view:View): RecyclerView.ViewHolder(view) {
        var name:TextView = view.findViewById(R.id.name)
        var age:TextView=view.findViewById(R.id.age)
        var sex:TextView=view.findViewById(R.id.sex)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val inflate = LayoutInflater.from(context).inflate(R.layout.holder_list, parent, false)
        return DataViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val studentBean = dataList[position]
        holder.apply {
            name.text=studentBean.name
            age.text=studentBean.age
            sex.text=studentBean.sex
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

   suspend  fun updateData(data:List<StudentBean>){
            processData(data)?.let {
                dataList.apply {
                   clear()
                    addAll(it)
                    MainScope().launch {
                        notifyDataSetChanged()
                    }
                }
            }
    }

    fun insertStudent(data:StudentBean){
        dataList.add(data)
        notifyItemInserted(dataList.size+1)
    }

    fun deleteStudent(data: StudentBean){
        dataList.apply {
            remove(data)
            notifyItemRemoved(indexOf(data))
        }
    }
    /**
     * 筛选数据
     */
    suspend fun processData(data:List<StudentBean>):List<StudentBean>?{
        if(!data.isNotEmpty()) return null
        var processedData= mutableListOf<StudentBean>()
        data.asFlow().filter {
            !( it.name.isEmpty() || it.age.isEmpty() || it.sex.isEmpty())
        }.collect {
            processedData.add(it)
        }
       return processedData
    }
}