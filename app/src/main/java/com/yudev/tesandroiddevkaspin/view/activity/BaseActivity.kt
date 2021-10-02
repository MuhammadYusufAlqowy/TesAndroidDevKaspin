package com.yudev.tesandroiddevkaspin.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.yudev.tesandroiddevkaspin.data.myutil.MyLoading

abstract class BaseActivity<V: ViewBinding>:AppCompatActivity() {
    lateinit var layout: V
    lateinit var loading: MyLoading.Build
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = getBindingView()
        setContentView(layout.root)
        loading = MyLoading.Build(this)
    }

    abstract fun getBindingView(): V
}