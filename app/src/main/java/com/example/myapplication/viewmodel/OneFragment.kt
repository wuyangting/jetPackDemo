package com.example.myapplication.viewmodel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R

class OneFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.onefragment, container, false)
        val seekBar = inflate.findViewById<SeekBar>(R.id.one_seekbar)
        val viewModel = this?.let { ViewModelProvider(it).get(MyViewModel::class.java) }
        //如果涉及到界面通信就需要拿到同一个ViewModel对象，而他是存储在ViewModelStore中。ViewModelStore又是根据ViewModelOwner得到的
        //所以需要获取到同一个Owner对象就行，拿到Store的管理缓存。这个时候ViewModel是一样的
         val currentSecond = viewModel?.getCurrentSecond()
        currentSecond?.observe(this,object: Observer<Int>{
            override fun onChanged(t: Int?) {
                    Log.i("wwwwwwwwwwwwwww", "onChanged: fragmentOne $t")
                    if (t != null) {
                        seekBar.progress = t
                    }
            }
        })
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i("wwwwwwwwwwwwwww", "onProgressChanged: fragmentOne $progress")
                currentSecond?.value=progress
                ViewModelProvider(this@OneFragment).get(MyViewModel::class.java)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        return inflate
    }
}