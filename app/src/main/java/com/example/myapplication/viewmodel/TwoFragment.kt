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

class TwoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.twofragment, container, false)
        val seekBar = inflate.findViewById<SeekBar>(R.id.two_seekbar)
        val viewModel = fragmentManager?.fragments?.get(0)
             ?.let { ViewModelProvider(it).get(MyViewModel::class.java) }
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
                Log.i("wwwwwwwwwwwwwww", "onProgressChanged: fragmentTwo $progress")
                currentSecond?.value=progress

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        return inflate
    }
}